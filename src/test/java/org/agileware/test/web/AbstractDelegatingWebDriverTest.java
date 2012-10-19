package org.agileware.test.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class AbstractDelegatingWebDriverTest {

	private AbstractDelegatingWebDriver mock = mock(AbstractDelegatingWebDriver.class, CALLS_REAL_METHODS);

	@Before
	public void setUp() {
		mock.delegate = spy(new HtmlUnitDriver());
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, "org.openqa.selenium.firefox.FirefoxDriver");
	}

	@Test
	public void testGetDelegate() {
		assertEquals(mock.delegate, mock.getDelegate());
	}

	@Test
	public void testClose() {
		mock.close();
		verify(mock.delegate).close();
	}

	@Test
	public void testFindElement() {
		By by = By.id("something");
		try {
			mock.findElement(by);
		} catch (NoSuchElementException nsee) {
			verify(mock.delegate).findElement(by);
		}
	}

	@Test
	public void testFindElements() {
		By by = By.id("something");
		try {
			mock.findElements(by);
		} catch (NoSuchElementException nsee) {
			verify(mock.delegate).findElements(by);
		}
	}

	@Test
	public void testGet() {
		mock.get("http://www.google.com");
		verify(mock.delegate).get("http://www.google.com");
	}

	@Test
	public void testGetCurrentUrl() {
		mock.getCurrentUrl();
		verify(mock.delegate).getCurrentUrl();
	}

	@Test
	public void testGetPageSource() {
		mock.getPageSource();
		verify(mock.delegate).getPageSource();
	}

	@Test
	public void testGetTitle() {
		mock.getTitle();
		verify(mock.delegate).getTitle();
	}

	@Test
	public void testGetWindowHandle() {
		mock.getWindowHandle();
		verify(mock.delegate).getWindowHandle();
	}

	@Test
	public void testGetWindowHandles() {
		mock.getWindowHandles();
		verify(mock.delegate).getWindowHandles();
	}

	@Test
	public void testManage() {
		mock.manage();
		verify(mock.delegate).manage();
	}

	@Test
	public void testNavigate() {
		mock.navigate();
		verify(mock.delegate).navigate();
	}

	@Test
	public void testSwitchTo() {
		mock.switchTo();
		verify(mock.delegate).switchTo();
	}

	@Test
	public void testQuit() {
		mock.quit();
		verify(mock.delegate).quit();
	}

	@Test
	public void testExecuteAsyncScript() {
		try {
			mock.executeAsyncScript("window.open()");
		} catch (UnsupportedOperationException uoe) {
			verify((JavascriptExecutor) mock.delegate).executeAsyncScript("window.open()");
		}
	}

	@Test(expected = WebDriverException.class)
	public void testExecuteAsyncScriptWrongDriver() {
		mock.delegate = mock(WebDriver.class);
		mock.executeAsyncScript("window.open()");
	}

	@Test
	public void testExecuteScript() {
		try {
			mock.executeScript("window.open()");
		} catch (UnsupportedOperationException uoe) {
			verify((JavascriptExecutor) mock.delegate).executeScript("window.open()");
		}
	}

	@Test(expected = WebDriverException.class)
	public void testExecuteScriptWrongDriver() {
		mock.delegate = mock(WebDriver.class);
		mock.executeScript("window.open()");
	}

	@Test
	public void testGetScreenshotAs() {
		mock.delegate = spy(new FirefoxDriver());
		mock.getScreenshotAs(OutputType.BASE64);
		mock.delegate.close();
		verify((FirefoxDriver) mock.delegate).getScreenshotAs(OutputType.BASE64);
	}

	@Test(expected = WebDriverException.class)
	public void testGetScreenshotAsWrongDriver() {
		mock.delegate = mock(WebDriver.class);
		mock.getScreenshotAs(OutputType.BASE64);
	}

}
