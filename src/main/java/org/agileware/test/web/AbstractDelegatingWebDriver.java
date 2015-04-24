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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * Abstract base class providing a delegator pattern implementation for Selenium
 * WebDriver implementations.
 */
public abstract class AbstractDelegatingWebDriver implements WebDriver, TakesScreenshot, JavascriptExecutor {
	/**
	 * The WebDriver implementation to delegate operations to.
	 */
	protected WebDriver delegate;

	/**
	 * Creates a delegating WebDriver around a WebDriver implementation.
	 * 
	 * @param delegate
	 *            the WebDriver implementation instance to delegate operations
	 *            to.
	 */
	public AbstractDelegatingWebDriver(WebDriver delegate) {
		this.delegate = delegate;
	}

	/**
	 * @return the delegate WebDriver instance
	 */
	public WebDriver getDelegate() {
		return this.delegate;
	}

	/**
	 * @see org.openqa.selenium.WebDriver#close()
	 */
	public void close() {
		delegate.close();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
	 */
	public WebElement findElement(By by) {
		try {
			return new DelegatingWebElement(delegate.findElement(by));
		} catch (NoSuchElementException nsee) {
			nsee.addInfo("Find clause", by.toString());
			throw nsee;
		}
	}

	/**
	 * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
	 */
	public List<WebElement> findElements(By by) {
		try {
			List<WebElement> results = new ArrayList<WebElement>();
			for (WebElement webElement : delegate.findElements(by)) {
				results.add(new DelegatingWebElement(webElement));
			}
			return results;
		} catch (NoSuchElementException nsee) {
			nsee.addInfo("Find clause", by.toString());
			throw nsee;
		}
	}

	/**
	 * @see org.openqa.selenium.WebDriver#get(java.lang.String)
	 */
	public void get(String url) {
		delegate.get(url);
	}

	/**
	 * @see org.openqa.selenium.WebDriver#getCurrentUrl()
	 */
	public String getCurrentUrl() {
		return delegate.getCurrentUrl();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#getPageSource()
	 */
	public String getPageSource() {
		return delegate.getPageSource();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#getTitle()
	 */
	public String getTitle() {
		return delegate.getTitle();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#getWindowHandle()
	 */
	public String getWindowHandle() {
		return delegate.getWindowHandle();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#getWindowHandles()
	 */
	public Set<String> getWindowHandles() {
		return delegate.getWindowHandles();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#manage()
	 */
	public Options manage() {
		return delegate.manage();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#navigate()
	 */
	public Navigation navigate() {
		return delegate.navigate();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#switchTo()
	 */
	public TargetLocator switchTo() {
		return delegate.switchTo();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#quit()
	 */
	public void quit() {
		delegate.quit();
	}

	/**
	 * @see org.openqa.selenium.JavascriptExecutor#executeAsyncScript(java.lang.String,
	 *      java.lang.Object[])
	 */
	public Object executeAsyncScript(String script, Object... args) throws WebDriverException {
		try {
			return ((JavascriptExecutor) delegate).executeAsyncScript(script, args);
		} catch (ClassCastException cce) {
			throw new WebDriverException("Delegate implementation `" + delegate.getClass()
					+ "` does not support this feature");
		}
	}

	/**
	 * @see org.openqa.selenium.JavascriptExecutor#executeScript(java.lang.String,
	 *      java.lang.Object[])
	 */
	public Object executeScript(String script, Object... args) throws WebDriverException {
		try {
			return ((JavascriptExecutor) delegate).executeScript(script, args);
		} catch (ClassCastException cce) {
			throw new WebDriverException("Delegate implementation `" + delegate.getClass()
					+ "` does not support this feature");
		}
	}

	/**
	 * @see org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.OutputType)
	 */
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		try {
			return ((TakesScreenshot) delegate).getScreenshotAs(target);
		} catch (ClassCastException cce) {
			throw new WebDriverException("Delegate implementation `" + delegate.getClass()
					+ "` does not support this feature");
		}
	}

	protected class DelegatingWebElement implements WebElement {
		
		protected WebElement delegate;

		public DelegatingWebElement(WebElement delegate) {
			this.delegate = delegate;
		}
		
		public void click() {
			delegate.click();
		}

		public void submit() {
			delegate.submit();
		}

		public void sendKeys(CharSequence... keysToSend) {
			delegate.sendKeys(keysToSend);
		}

		public void clear() {
			delegate.clear();
		}

		public String getTagName() {
			return delegate.getTagName();
		}

		public String getAttribute(String name) {
			return delegate.getAttribute(name);
		}

		public boolean isSelected() {
			return delegate.isSelected();
		}

		public boolean isEnabled() {
			return delegate.isEnabled();
		}

		public String getText() {
			return delegate.getText();
		}

		public List<WebElement> findElements(By by) {
			try {
				return delegate.findElements(by);
			} catch (NoSuchElementException nsee) {
				nsee.addInfo("Find clause", by.toString());
				throw nsee;
			}
		}

		public WebElement findElement(By by) {
			try {
				return delegate.findElement(by);
			} catch (NoSuchElementException nsee) {
				nsee.addInfo("Find clause", by.toString());
				throw nsee;
			}
		}

		public boolean isDisplayed() {
			return delegate.isDisplayed();
		}

		public Point getLocation() {
			return delegate.getLocation();
		}

		public Dimension getSize() {
			return delegate.getSize();
		}

		public String getCssValue(String propertyName) {
			return delegate.getCssValue(propertyName);
		}
	}
}
