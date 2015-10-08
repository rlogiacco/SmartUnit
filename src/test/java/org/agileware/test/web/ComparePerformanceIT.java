package org.agileware.test.web;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;


/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class ComparePerformanceIT {

	public final static int LOOP_COUNT = Integer.parseInt(System.getProperty("loopCount"));
	public final static int PAGE_COUNT = Integer.parseInt(System.getProperty("pageCount"));

	@Test
	public void notShared() {
		for (int i = 0; i < LOOP_COUNT; i++) {
			// Create a new instance of the html unit driver
			// Notice that the remainder of the code relies on the interface,
			// not the implementation.
			WebDriver browser = new HtmlUnitDriver(BrowserVersion.CHROME);

			for (int j = 0; j < PAGE_COUNT; j++) {
				navigate(browser);
			}

			browser.quit();
		}
	}

	@Test
	public void shared() {
		// Initializes the shared web driver on the intended driver
		SharedWebDriver.init(new HtmlUnitDriver(BrowserVersion.CHROME));

		for (int i = 0; i < LOOP_COUNT; i++) {
			// Obtain a new instance of the shared driver
			WebDriver browser = new SharedWebDriver();
			for (int j = 0; j < PAGE_COUNT; j++) {
				navigate(browser);
			}

			browser.quit();
		}

		SharedWebDriver.destroy();
	}

	public static void navigate(final WebDriver browser) {
		// And now use this to visit Google
		browser.get("http://www.google.com");

		// Find the text input element by its name
		WebElement element = browser.findElement(By.name("q"));

		// Enter something to search for
		element.sendKeys("Cheese!");

		// Now submit the form.
		element.submit();

		// Check the title of the page
		WaitHelper.waitOn(browser, 10000).until(By.tagName("title"), new BaseMatcher<WebElement>() {

			public boolean matches(Object item) {
				return browser.getTitle().startsWith("Cheese!");
			}

			public void describeTo(Description description) {
				// TODO Auto-generated method stub

			}
		});
	}
}
