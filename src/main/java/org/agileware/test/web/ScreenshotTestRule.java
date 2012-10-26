package org.agileware.test.web;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Augments JUnit test suites based on Selenium WebDriver by generating a
 * browser screenshot every time a test failure occurs.
 * 
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 * 
 */
public class ScreenshotTestRule implements TestRule {
	public static final String DEFAULT_OUTPUT_FOLDER = "target/surefire-reports/";

	protected WebDriver driver;

	protected String outputFolder;

	/**
	 * @param driver
	 *            the WebDriver instance used in the tests
	 */
	public ScreenshotTestRule(WebDriver driver) {
		this(driver, DEFAULT_OUTPUT_FOLDER);
	}

	/**
	 * @param driver
	 *            the WebDriver instance used in the tests
	 * @param outputFolder
	 *            the folder where screenshots will be saved
	 */
	public ScreenshotTestRule(WebDriver driver, String outputFolder) {
		this.driver = driver;
		this.outputFolder = outputFolder;
		new File(outputFolder).mkdirs();
	}

	/**
	 * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement,
	 *      org.junit.runner.Description)
	 */
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
				} catch (Throwable t) {
					capture(description.getMethodName());
					throw t;
				}
			}
		};
	}

	/**
	 * Obtains a screenshot from the browser and saves it in the output folder.
	 * 
	 * @param method
	 *            name of the failing test method
	 */
	private void capture(String method) {
		try {
			File target = new File(outputFolder, "screenshot-" + method + ".png");
			FileOutputStream out = new FileOutputStream(target);
			out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			out.close();
		} catch (Exception e) {
			// No need to crash the tests if the screenshot fails
			new RuntimeException("Cannot save screenshot for test " + method, e).printStackTrace();
		}
	}

}
