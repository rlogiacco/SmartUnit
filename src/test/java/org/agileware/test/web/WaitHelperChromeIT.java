package org.agileware.test.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.agileware.test.web.WaitHelper.waitOn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WaitHelperChromeIT {
	private WebDriver browser;

	@Before
	public void before() {
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, ChromeDriver.class.getName());
		browser = SharedWebDriver.open();
	}

	@Test
	public void testUntilAdded() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("delayed")).click();
		try {
			browser.findElement(By.id("wait-if"));
			fail("Element found");
		} catch (NoSuchElementException e) {
			assertNotNull(waitOn(browser, 6, SECONDS).untilAdded(By.id("wait-if")));
		}
	}

	@Test
	public void testUntilRemoved() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("async")).click();
		browser.findElement(By.id("wait-if"));
		assertNotNull(waitOn(browser, 6000).untilRemoved(By.id("wait-if")));
		try {
			browser.findElement(By.id("wait-if"));
			fail("Element found");
		} catch (NoSuchElementException e) {
			// expected, but here only
		}
	}

	@Test
	public void testUntilShown() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("delayed")).click();
		assertFalse(browser.findElement(By.id("wait-show")).isDisplayed());
		assertTrue(waitOn(browser, 6, SECONDS).untilShown(By.id("wait-show")).isDisplayed());
	}

	@Test
	public void testUntilVisible() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("delayed")).click();
		try {
			browser.findElement(By.id("wait-if"));
			fail("Element found");
		} catch (NoSuchElementException e) {
			assertTrue(waitOn(browser, 6000).untilShown(By.id("wait-if")).isDisplayed());
		}
	}

	@Test
	public void testUntilHidden() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("async")).click();
		assertTrue(browser.findElement(By.id("wait-show")).isDisplayed());
		assertFalse(waitOn(browser, 6, SECONDS).untilHidden(By.id("wait-show")).isDisplayed());
	}

	@Test
	public void testUntilEnabled() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("delayed")).click();
		assertFalse(browser.findElement(By.id("wait-enable")).isEnabled());
		assertTrue(waitOn(browser, 6000).untilEnabled(By.id("wait-enable")).isEnabled());
	}

	@Test
	public void testUntilDisabled() {
		browser.get(SharedWebDriverTest.TEST_PAGE);
		browser.findElement(By.id("async")).click();
		assertTrue(browser.findElement(By.id("wait-enable")).isEnabled());
		waitOn(browser, 6, SECONDS).untilDisabled(By.id("wait-enable"));
		assertFalse(browser.findElement(By.id("wait-enable")).isEnabled());
		assertEquals("1", browser.findElement(By.id("counter")).getText());
	}
}
