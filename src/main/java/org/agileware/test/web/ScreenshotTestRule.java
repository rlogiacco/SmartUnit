package org.agileware.test.web;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotTestRule implements TestRule {
	public static final String DEFAULT_OUTPUT_FOLDER = "target/surefire-reports/";

	private WebDriver driver;

	private String outputFolder;
	
	public ScreenshotTestRule(WebDriver driver) {
		this(driver, DEFAULT_OUTPUT_FOLDER);
	}
	
	public ScreenshotTestRule(WebDriver driver, String outputFolder) {
		this.driver = driver;
		this.outputFolder = outputFolder;
		new File(outputFolder).mkdirs();
	}

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
	
	private void capture(String method) {
		try {
			FileOutputStream out = new FileOutputStream(outputFolder + "screenshot-" + method + ".png");
			out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			out.close();
		} catch (Exception e) {
			// No need to crash the tests if the screenshot fails
			new RuntimeException("Cannot save screenshot for test " + method, e).printStackTrace();
		}
	}

}
