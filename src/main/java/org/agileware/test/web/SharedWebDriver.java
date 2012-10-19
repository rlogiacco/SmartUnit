package org.agileware.test.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 * 
 */
public class SharedWebDriver extends AbstractDelegatingWebDriver {
	public final static String SELENIUM_DRIVER_PROPERTY = "selenium.driver";
	public final static String SELENIUM_DRIVER_CAPABILITIES_PROPERTY = "selenium.driver.capabilities";

	private static SharedWebDriver instance;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (instance != null && instance.delegate != null) {
					instance.delegate.quit();
				}
			}
		});
	}

	public SharedWebDriver(WebDriver delegate) {
		super(delegate);
	}

	/**
	 * @param delegate
	 * @return
	 * @throws WebDriverException
	 */
	public static SharedWebDriver open(WebDriver delegate) throws WebDriverException {
		if (instance == null) {
			instance = new SharedWebDriver(delegate);
		} else {
			instance.delegate.manage().deleteAllCookies();
		}
		return instance;
	}

	/**
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public static SharedWebDriver open() throws WebDriverException {
		try {
			Class<?> delegate = Class.forName(System.getProperty(SELENIUM_DRIVER_PROPERTY));
			return open((Class<? extends WebDriver>) delegate);
		} catch (ClassNotFoundException e) {
			throw new WebDriverException("Driver class not found", e);
		}
	}

	/**
	 * @param delegate
	 * @param capabilities
	 * @return
	 * @throws RuntimeException
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

	public static void destroy() {
		if (instance != null) {
			if (instance.delegate != null) {
				instance.delegate.quit();
			}
			instance = null;
		}
	}
}
