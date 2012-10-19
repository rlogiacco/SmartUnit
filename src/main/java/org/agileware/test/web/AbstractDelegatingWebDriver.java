/**
 * 
 */
package org.agileware.test.web;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 * 
 */
public abstract class AbstractDelegatingWebDriver implements WebDriver, TakesScreenshot, JavascriptExecutor {
	protected WebDriver delegate;

	public AbstractDelegatingWebDriver(WebDriver delegate) {
		this.delegate = delegate;
	}

	/**
	 * @return
	 */
	public WebDriver getDelegate() {
		return this.delegate;
	}

	public void close() {
		delegate.close();
	}

	/**
	 * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
	 */
	public WebElement findElement(By by) {
		return delegate.findElement(by);
	}

	/**
	 * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
	 */
	public List<WebElement> findElements(By by) {
		return delegate.findElements(by);
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
		if (delegate instanceof JavascriptExecutor) {
			return ((JavascriptExecutor) delegate).executeAsyncScript(script, args);
		} else {
			throw new WebDriverException("Delegate implementation `" + delegate.getClass()
					+ "` does not support this feature");
		}
	}

	/**
	 * @see org.openqa.selenium.JavascriptExecutor#executeScript(java.lang.String,
	 *      java.lang.Object[])
	 */
	public Object executeScript(String script, Object... args) throws WebDriverException {
		if (delegate instanceof JavascriptExecutor) {
			return ((JavascriptExecutor) delegate).executeScript(script, args);
		} else {
			throw new WebDriverException("Delegate implementation `" + delegate.getClass()
					+ "` does not support this feature");
		}
	}

	/**
	 * @see org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.OutputType)
	 */
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		if (delegate instanceof TakesScreenshot) {
			return ((TakesScreenshot) delegate).getScreenshotAs(target);
		} else {
			throw new WebDriverException("Delegate implementation `" + delegate.getClass()
					+ "` does not support this feature");
		}
	}
}
