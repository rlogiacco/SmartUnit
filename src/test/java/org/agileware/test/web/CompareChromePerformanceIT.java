package org.agileware.test.web;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class CompareChromePerformanceIT {

	@Test
	public void notShared() {
		for (int i = 0; i < ComparePerformanceIT.LOOP_COUNT; i++) {
			// Create a new instance of the Google Chrome driver
			// Notice that the remainder of the code relies on the interface,
			// not the implementation.
			final WebDriver browser = new ChromeDriver();

			for (int j = 0; j < ComparePerformanceIT.PAGE_COUNT; j++) {
				ComparePerformanceIT.navigate(browser);
			}

			browser.quit();
		}
	}

	@Test
	public void shared() throws Exception {
		// Initializes the shared web driver on the intended driver
		SharedWebDriver.init(new ChromeDriver());

		for (int i = 0; i < ComparePerformanceIT.LOOP_COUNT; i++) {
			// Obtain a new instance of the shared driver
			final WebDriver browser = new SharedWebDriver();

			for (int j = 0; j < ComparePerformanceIT.PAGE_COUNT; j++) {
				ComparePerformanceIT.navigate(browser);
			}

			browser.quit();
		}
		SharedWebDriver.destroy();
	}
}
