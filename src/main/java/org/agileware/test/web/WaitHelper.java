package org.agileware.test.web;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Predicate;

public class WaitHelper {

	private WebDriver driver;
	private long duration;
	private TimeUnit timeUnit;

	public WaitHelper(WebDriver driver, long millis) {
		this(driver, millis, TimeUnit.MILLISECONDS);
	}

	public WaitHelper(WebDriver driver, long duration, TimeUnit timeUnit) {
		this.driver = driver;
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	/**
	 * @param by
	 */
	public void untilAdded(final By by, long duration, TimeUnit timeUnit) {
		this.untilAdded(by, duration, timeUnit);
	}

	/**
	 * @param by
	 */
	public void untilAdded(final By by) {
		new FluentWait<WebDriver>(driver).withTimeout(duration, timeUnit).until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver browser) {
				try {
					browser.findElement(by);
					return true;
				} catch (NoSuchElementException nsee) {
					return false;
				}
			}
		});
	}

	/**
	 * @param by
	 */
	public void untilRemoved(final By by) {
		this.untilRemoved(by, duration, timeUnit);
	}

	/**
	 * @param by
	 * @param duration
	 * @param timeUnit
	 */
	public void untilRemoved(final By by, long duration, TimeUnit timeUnit) {
		new FluentWait<WebDriver>(driver).withTimeout(duration, timeUnit).until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver browser) {
				try {
					browser.findElement(by);
					return false;
				} catch (NoSuchElementException nsee) {
					return true;
				}
			}
		});
	}

	/**
	 * @param by
	 */
	public void untilShown(final By by) {
		this.untilShown(by, duration, timeUnit);
	}

	/**
	 * @param by
	 * @param duration
	 * @param timeUnit
	 */
	public void untilShown(final By by, long duration, TimeUnit timeUnit) {
		new FluentWait<WebDriver>(driver).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver browser) {
				return browser.findElement(by).isDisplayed();
			}
		});
	}

	/**
	 * @param by
	 */
	public void untilHidden(final By by) {
		this.untilHidden(by, duration, timeUnit);
	}

	/**
	 * @param by
	 * @param duration
	 * @param timeUnit
	 */
	public void untilHidden(final By by, long duration, TimeUnit timeUnit) {
		new FluentWait<WebDriver>(driver).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver browser) {
				return !browser.findElement(by).isDisplayed();
			}
		});
	}
}
