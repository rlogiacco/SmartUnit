package org.agileware.test.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class SharedWebDriverFirefoxIT {
	
	private SharedWebDriver browser;
	
	private Class<? extends WebDriver> driver;
	
	@Before
	public void before() {
		driver = FirefoxDriver.class;
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, driver.getName());
	}
	
	@After
	public void after() {
		SharedWebDriver.destroy();
	}

	@Test
	public void open() {
		browser = SharedWebDriver.open();
		
		Assert.assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.open();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		Assert.assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test(expected=WebDriverException.class)
	public void openFails() {
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, "some.non.existing.Class");
		browser = SharedWebDriver.open();
	}
	
	@Test
	public void openWithDriverInstance() throws Throwable {
		browser = SharedWebDriver.open(driver.newInstance());
		
		Assert.assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.open();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		Assert.assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test
	public void openWithDriverClass() throws Throwable {
		browser = SharedWebDriver.open(driver);
		
		Assert.assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.open();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		Assert.assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test(expected=WebDriverException.class)
	public void openWithDriverClassFailing() throws Throwable {
		browser = SharedWebDriver.open(AbstractDelegatingWebDriver.class);
	}
	
	@Test
	public void close() throws Exception {
		browser = SharedWebDriver.open(driver);
		
		Assert.assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		browser.close();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		browser.executeScript("window.open('','other')");
		Assert.assertEquals(2, browser.getWindowHandles().size());
		browser.switchTo().window("other");
		browser.close();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		Assert.assertTrue(browser.getWindowHandles().contains(original));
	}
	
	@Test
	public void quit() throws Exception {
		browser = SharedWebDriver.open(driver);
		
		Assert.assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		browser.quit();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		
		for (int i = 0; i < 5; i++) {
			browser.executeScript("window.open()");
		}
		Assert.assertEquals(6, browser.getWindowHandles().size());
		browser.quit();
		Assert.assertEquals(1, browser.getWindowHandles().size());
		Assert.assertTrue(browser.getWindowHandles().contains(original));
		
		browser.executeScript("window.open()");
		browser.switchTo().window(original);
		browser.close();
		browser.quit();
		
		Assert.assertEquals(1, browser.getWindowHandles().size());
		Assert.assertFalse(browser.getWindowHandle().equals(original));
		
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
		Assert.assertTrue(elapsed > 0);
	}

}
