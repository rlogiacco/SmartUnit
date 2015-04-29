package org.agileware.test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class SharedWebDriverTest {
	
	private SharedWebDriver browser;
	
	private Class<? extends WebDriver> driver;
	
	public static final String TEST_PAGE = "http://run.plnkr.co/plunks/17M8WwGto2uieNqFXKje/";
	
	@Before
	public void before() {
		driver = HtmlUnitDriver.class;
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, driver.getName());
	}
	
	@After
	public void after() {
		SharedWebDriver.destroy();
	}

	@Test
	public void open() {
		browser = SharedWebDriver.open();
		
		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.open();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test(expected=WebDriverException.class)
	public void openFails() {
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, "some.non.existing.Class");
		browser = SharedWebDriver.open();
	}
	
	@Test
	public void openWithDriverInstance() throws Exception {
		browser = SharedWebDriver.open(driver.newInstance());
		
		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.open();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test
	public void openWithDriverClass() throws Exception {
		browser = SharedWebDriver.open(driver);
		
		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.open();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test(expected=WebDriverException.class)
	public void openWithDriverClassFailing() throws Exception {
		browser = SharedWebDriver.open(AbstractDelegatingWebDriver.class);
	}
	
	@Test
	public void close() throws Exception {
		browser = SharedWebDriver.open(new HtmlUnitDriver(true));
		browser.get("http://www.google.com");
		
		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		browser.close();
		assertEquals(1, browser.getWindowHandles().size());
		browser.executeScript("window.open('','other')");
		assertEquals(2, browser.getWindowHandles().size());
		browser.switchTo().window("other");
		browser.close();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test
	public void quit() throws Exception {
		browser = SharedWebDriver.open(new HtmlUnitDriver(true));
		browser.get("http://www.google.com");
		
		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		browser.quit();
		assertEquals(1, browser.getWindowHandles().size());
		assertEquals(original, browser.getWindowHandle());
		
		for (int i = 0; i < 5; i++) {
			browser.executeScript("window.open('','other-" + i + "')");
		}
		assertEquals(6, browser.getWindowHandles().size());
		browser.quit();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
		
		browser.executeScript("window.open('','other')");
		browser.switchTo().window(original);
		browser.close();
		browser.quit();
		
		assertEquals(1, browser.getWindowHandles().size());
		assertFalse(browser.getWindowHandle().equals(original));
		
	}
	
	@Test
	public void destroy() throws Throwable {
		browser = SharedWebDriver.open(driver);
		SharedWebDriver.destroy();
		SharedWebDriver.open((WebDriver)null);
		SharedWebDriver.destroy();
		SharedWebDriver.destroy();
	}
	
	public void perf() throws Exception {
		browser = SharedWebDriver.open(driver);
		browser.get("http://www.google.com");
		long elapsed = (Long)browser.executeScript("return (performance.timing.loadEventEnd - performance.timing.connectStart)");
		assertTrue(elapsed > 0);
	}

}
