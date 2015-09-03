package org.agileware.test.web;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CompareFirefoxPerformanceIT {
	private int loopCount = Integer.parseInt(System.getProperty("loopCount"));
	private int pageCount = Integer.parseInt(System.getProperty("pageCount"));

	private void navigate(final WebDriver browser) {
		for (int i = 0; i < pageCount; i++) {
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
	
	@Test
	public void notShared() {
		for (int i = 0; i < loopCount; i++) {
			// Create a new instance of the Firefox driver
			// Notice that the remainder of the code relies on the interface,
			// not the implementation.
			final WebDriver browser = new FirefoxDriver();

			this.navigate(browser);

			browser.quit();
		}
	}

	@Test
	public void shared() {
		// Initializes the shared web driver on the intended driver
		SharedWebDriver.init(new FirefoxDriver());

		for (int i = 0; i < loopCount; i++) {
			// Obtain a new instance of the shared driver
			final WebDriver browser = new SharedWebDriver();

			this.navigate(browser);

			browser.quit();
		}

		SharedWebDriver.destroy();
	}
}
