package org.agileware.test.web;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class CompareInternetExplorerPerformanceIT {

	@Test
	public void notShared() {
		for (int i = 0; i < ComparePerformanceIT.LOOP_COUNT; i++) {
			// Create a new instance of the Microsoft IE driver
			// Notice that the remainder of the code relies on the interface,
			// not the implementation.
			WebDriver browser = new InternetExplorerDriver();

			for (int j = 0; j < ComparePerformanceIT.PAGE_COUNT; j++) {
				ComparePerformanceIT.navigate(browser);
			}

			browser.quit();
		}
	}

	@Test
	public void shared() {
		// Initializes the shared web driver on the intended driver
		SharedWebDriver.init(new InternetExplorerDriver());

		for (int i = 0; i < ComparePerformanceIT.LOOP_COUNT; i++) {
			// Obtain a new instance of the shared driver
			WebDriver browser = new SharedWebDriver();

			for (int j = 0; j < ComparePerformanceIT.PAGE_COUNT; j++) {
				ComparePerformanceIT.navigate(browser);
			}

			browser.quit();
		}

		SharedWebDriver.destroy();
	}
}
