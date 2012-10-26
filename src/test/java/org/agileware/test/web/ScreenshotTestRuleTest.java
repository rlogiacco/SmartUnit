package org.agileware.test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 * 
 */
public class ScreenshotTestRuleTest {

	private static interface TakesScreenshotWebDriver extends WebDriver, TakesScreenshot {
		
	}
	
	@Test
	public void testApply() throws Throwable {
		WebDriver driver = mock(TakesScreenshotWebDriver.class);
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

	@Test
	public void testApplyNoScreenshot() throws Throwable {
		ScreenshotTestRule rule = new ScreenshotTestRule(new HtmlUnitDriver());
		Statement base = new Statement() {

			@Override
			public void evaluate() throws Throwable {
				return;
			}
		};
		rule.apply(base, Description.createTestDescription(this.getClass(), "working")).evaluate();
		File screenshot = new File(ScreenshotTestRule.DEFAULT_OUTPUT_FOLDER, "screenshot-working.png");
		assertFalse(screenshot.exists());
	}
}