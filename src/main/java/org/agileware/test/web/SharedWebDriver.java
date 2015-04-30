package org.agileware.test.web;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * This is a Selenium WebDriver implementation to be used as a wrapper for any
 * real web browser driver implementation to prevent a new web browser window to
 * be created on each test.
 * 
 * <p>
 * This solution represents a compromise between test performances and
 * reliability: we are not in a perfectly clean environment at the beginning of
 * every test, but we are not paying the price of creating a new browser
 * instance.
 * </p>
 * 
 * <p>
 * This implementation supports two types of test: without any dependency
 * injection mechanism and with dependency injection, like Spring Test or
 * Cucumber JVM.
 * </p>
 * 
 * 
 * <p>
 * <b>No injection</b>
 * </p>
 * 
 * <p>
 * A classic singleton pattern is implemented and the browser will be destroyed
 * upon JVM destruction.
 * </p>
 * 
 * <p>
 * New browser instances should be obtained by using one of the
 * <code>open</code> static methods. The no argument version uses the
 * <i>selenium.driver</i> system property to specify the delegate implementation
 * web driver class.
 * </p>
 * 
 * <p>
 * In case a browser shutdown is required, the static <code>destroy</code>
 * method must be used as both <code>close</code> and <code>quit</code>
 * WebDriver methods implementations prevents the browser process to be closed
 * by leaving at least one browser window open.
 * </p>
 * 
 * <p>
 * <b>With injection</b>
 * </p>
 * 
 * <p>
 * The instance creation must occur throughout one of the <code>open</code>
 * static methods.
 * </p>
 * 
 * <p>
 * Browser shutdown upon test suite completion is still supported through JVM
 * shutdown hook.
 * </p>
 * 
 * <p>
 * The following is an example of Spring configuration:
 * </p>
 *
 * <pre>
 *   &lt;bean id="driver" class="org.agileware.test.web.SharedWebDriver" factory-method="open"&gt;
 *     &lt;constructor-arg ref="delegate" /&gt;
 *   &lt;/bean&gt;
 * 
 *   &lt;bean id="delegate" class="org.openqa.selenium.chrome.ChromeDriver"&gt;
 *     &lt;!-- capabilities can be set here --&gt;
 *   &lt;/bean&gt;
 * </pre>
 */
public class SharedWebDriver extends AbstractDelegatingWebDriver {
	public static final String SELENIUM_DRIVER_PROPERTY = "selenium.driver";

	private static SharedWebDriver instance;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				SharedWebDriver.destroy();
			}
		});
	}

	/**
	 * Creates a new instance wrapping the provided delegate WebDriver
	 * implementation.
	 * 
	 * @param delegate
	 *            the delegate WebDriver instance
	 */
	protected SharedWebDriver(WebDriver delegate) {
		super(delegate);
	}

	/**
	 * Returns the current shared instance or creates a new one using the
	 * provided delegate implementation.
	 * 
	 * @param delegate
	 *            the delegate WebDriver instance to use if no shared instance
	 *            is available
	 * @return the shared instance
	 */
	public static synchronized SharedWebDriver open(WebDriver delegate) {
		if (instance == null) {
			instance = new SharedWebDriver(delegate);
		} else {
			instance.delegate.manage().deleteAllCookies();
		}
		return instance;
	}

	/**
	 * Returns the current shared instance or creates a new one using the
	 * <i>selenium.driver</i> system property to determine the delegate
	 * implementation.
	 * 
	 * @return the shared instance
	 * @throws WebDriverException
	 *             if the delegate implementation cannot be created
	 */
	@SuppressWarnings("unchecked")
	public static SharedWebDriver open() throws WebDriverException {
		try {
			Class<?> delegate = Class.forName(System.getProperty(SELENIUM_DRIVER_PROPERTY));
			return open((Class<? extends WebDriver>) delegate);
		} catch (ClassNotFoundException cnfe) {
			throw new WebDriverException("Driver class not found", cnfe);
		}
	}

	/**
	 * Returns the current shared instance or creates a new one using the
	 * provided WebDriver implementation type to create the delegate instance.
	 * 
	 * @param delegate
	 *            the WebDriver implementation type
	 * @return the shared instance
	 * @throws WebDriverException
	 *             if the delegate instance cannot be created
	 */
	public static synchronized SharedWebDriver open(Class<? extends WebDriver> delegate) throws WebDriverException {
		if (instance == null) {
			try {
				instance = new SharedWebDriver(delegate.newInstance());
			} catch (Exception e) {
				throw new WebDriverException("Cannot create delegate", e);
			}
		} else {
			instance.delegate.manage().deleteAllCookies();
		}
		return instance;
	}

	/**
	 * Closes the current browser window only if it is not the last open one.
	 * 
	 * @see org.openqa.selenium.WebDriver#close()
	 */
	public synchronized void close() {
		// Prevent closing the browser instance: the shutdown hook will do it
		if (delegate.getWindowHandles().size() > 1) {
			delegate.close();
		}
	}

	/**
	 * Closes all browser windows but the currently selected one which is
	 * reused.
	 * 
	 * @see org.openqa.selenium.WebDriver#quit()
	 */
	public synchronized void quit() {
		// Prevent closing the browser instance: the shutdown hook will do it
		String[] handles = delegate.getWindowHandles().toArray(new String[delegate.getWindowHandles().size()]);
		String current = handles[0];
		try {
			delegate.switchTo().window(delegate.getWindowHandle());
			current = delegate.getWindowHandle();
		} catch (WebDriverException wde) {
			// no current window
			delegate.switchTo().window(current);
		}
		for (String handle : handles) {
			if (!handle.equals(current)) {
				delegate.switchTo().window(handle).close();
			}
		}
		delegate.switchTo().window(current);
	}

	/**
	 * Destroys the current shared instance closing all the browser windows.
	 * This method is here as last resort: if you find yourself using this
	 * method you might want to reconsider the usage of this WebDriver
	 * implementation.
	 */
	public static synchronized void destroy() {
		try {
			instance.delegate.quit();
		} catch (NullPointerException npe) {
			// do nothing
		} finally {
			instance = null;
		}
	}
}
