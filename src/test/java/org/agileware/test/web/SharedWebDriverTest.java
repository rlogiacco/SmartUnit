package org.agileware.test.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
	public void create() {
		SharedWebDriver.init();
		browser = new SharedWebDriver();

		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.init();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}

	@Test
	public void createWithDelegate() throws Exception {
		SharedWebDriver.init(driver.newInstance());
		browser = new SharedWebDriver();

		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.init();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}

	@Test
	public void createWithDriverClass() throws Exception {
		SharedWebDriver.init(driver);
		browser = new SharedWebDriver();

		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.init();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}

	@Test(expected = NullPointerException.class)
	public void createFailing() throws Exception {
		browser = new SharedWebDriver();
	}

	@Test
	public void init() {
		browser = SharedWebDriver.init();

		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.init();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}

	@Test(expected = WebDriverException.class)
	public void initFails() {
		System.setProperty(SharedWebDriver.SELENIUM_DRIVER_PROPERTY, "some.non.existing.Class");
		browser = SharedWebDriver.init();
	}

	@Test
	public void initWithDriverInstance() throws Exception {
		browser = SharedWebDriver.init(driver.newInstance());

		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.init();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}

	@Test
	public void initWithDriverClass() throws Exception {
		browser = SharedWebDriver.init(driver);

		assertEquals(1, browser.getWindowHandles().size());
		String original = browser.getWindowHandle();
		SharedWebDriver.init();
		assertEquals(1, browser.getWindowHandles().size());
		assertTrue(browser.getWindowHandles().contains(original));
	}

	@Test(expected = WebDriverException.class)
	public void initWithDriverClassFailing() throws Exception {
		browser = SharedWebDriver.init(AbstractDelegatingWebDriver.class);
		fail("Did it really manage to instantiate an abstract class? " + browser.getDelegate().getClass());
	}

	@Test
	public void close() throws Exception {
		browser = SharedWebDriver.init(new HtmlUnitDriver(true));
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
		browser = SharedWebDriver.init(new HtmlUnitDriver(true));
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
		browser = SharedWebDriver.init(driver);
		SharedWebDriver.destroy();
		SharedWebDriver.init((WebDriver) null);
		SharedWebDriver.destroy();
		SharedWebDriver.destroy();
	}
}
