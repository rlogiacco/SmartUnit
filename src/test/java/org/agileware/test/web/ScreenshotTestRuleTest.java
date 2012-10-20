package org.agileware.test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
public class ScreenshotTestRuleTest {

	@Test
	public void testApply() throws Throwable {
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.google.com");
		ScreenshotTestRule rule = new ScreenshotTestRule(driver);
		Statement base = new Statement() {

			@Override
			public void evaluate() throws Throwable {
				throw new UnsupportedOperationException();

			}
		};
		try {
			rule.apply(base, Description.createTestDescription(this.getClass(), "self")).evaluate();
		} catch (UnsupportedOperationException uoe) {
			File screenshot = new File(ScreenshotTestRule.DEFAULT_OUTPUT_FOLDER, "screenshot-self.png");
			assertTrue(screenshot.exists());
			assertTrue(screenshot.length() > 0);
		} finally {
			driver.quit();
		}
	}

	@Test
	public void testApplyException() throws Throwable {
		ScreenshotTestRule rule = new ScreenshotTestRule(new HtmlUnitDriver(), "target/some/sort/of/path/");
		Statement base = new Statement() {

			@Override
			public void evaluate() throws Throwable {
				throw new UnsupportedOperationException();

			}
		};
		try {
			rule.apply(base, Description.createTestDescription(this.getClass(), "self")).evaluate();
		} catch (UnsupportedOperationException uoe) {
			File screenshot = new File("target/some/sort/of/path/", "screenshot-self.png");
			assertEquals(0, screenshot.length());
		}
	}
}