package org.agileware.test.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class AbstractDelegatingWebDriverTest {

	private static interface TakesScreenshotWebDriver extends WebDriver, TakesScreenshot {
	}

	private AbstractDelegatingWebDriver mock = mock(AbstractDelegatingWebDriver.class, CALLS_REAL_METHODS);

	@Before
	public void setUp() {
		mock.delegate = spy(new HtmlUnitDriver(true));
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
		mock.delegate = mock(WebDriver.class);
		when(mock.delegate.findElement(by)).thenReturn(null);
		mock.findElement(by);
		verify(mock.delegate).findElement(by);
	}

	@Test
	public void testFindElements() {
		By by = By.id("something");
		List<WebElement> elements = mock.findElements(by);
		verify(mock.delegate).findElements(by);
		assertEquals(0, elements.size());
	}

	@Test
	public void testGet() {
		mock.get("https://github.com/");
		verify(mock.delegate).get("https://github.com/");
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
		mock.get("https://github.com/");
		mock.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		mock.executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 500);");
		verify((JavascriptExecutor) mock.delegate).executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 500);");
	}

	@Test(expected = WebDriverException.class)
	public void testExecuteAsyncScriptWrongDriver() {
		mock.delegate = mock(WebDriver.class);
		mock.executeAsyncScript("window.open()");
	}

	@Test
	public void testExecuteScript() {
		mock.get("https://github.com/");
		mock.executeScript("window.open('','other')");
		verify((JavascriptExecutor) mock.delegate).executeScript("window.open('','other')");
	}

	@Test(expected = WebDriverException.class)
	public void testExecuteScriptWrongDriver() {
		mock.delegate = mock(WebDriver.class);
		mock.executeScript("window.open()");
	}

	@Test
	public void testGetScreenshotAs() {
		mock.delegate = mock(TakesScreenshotWebDriver.class);
		mock.getScreenshotAs(OutputType.BASE64);
		mock.delegate.close();
		verify((TakesScreenshot) mock.delegate).getScreenshotAs(OutputType.BASE64);
	}

	@Test(expected = WebDriverException.class)
	public void testGetScreenshotAsWrongDriver() {
		mock.delegate = mock(WebDriver.class);
		mock.getScreenshotAs(OutputType.BASE64);
	}

}
