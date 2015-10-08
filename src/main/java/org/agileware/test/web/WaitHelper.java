package org.agileware.test.web;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.Matchers;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;

import lombok.NonNull;

/**
 * This helper class provides a few utility methods really helpful in testing
 * asynchronous web applications like those made with AngularJS or jQuery.
 * 
 * <p>
 * Many JavaScript frameworks operate by manipulating the page DOM either by
 * adding/removing or by showing/hiding elements as a consequence of a user
 * action: this implies your test code needs to synchronize with those
 * asynchronous events in order to proceed.
 * </p>
 * 
 * <p>
 * The utility methods presented in here provide the basic facility to create
 * this synchronization points.
 * </p>
 * 
 * <p>
 * More advanced synchronization than presented here is achievable by directly
 * using the <code>org.openqa.selenium.support.ui.Wait</code> or
 * <code>org.openqa.selenium.support.ui.FluentWait</code> APIs and the code
 * provided hereby can be used as a reference for such advanced usage.
 * </p>
 */
public class WaitHelper {

	private static long defaultTimeout = 5000;
	private static long defaultInterval = 500;

	private SearchContext context;
	private long timeout, interval;
	private TimeUnit timeUnit;

	/**
	 * @param context
	 *            the <code>WebDriver</code> or <code>WebElement</code> used as
	 *            search root
	 * @param timeout
	 *            the maximum wait duration
	 * @param timeUnit
	 *            the maximum wait time unit
	 */
	protected WaitHelper(@NonNull SearchContext context, long timeout, long interval, @NonNull TimeUnit timeUnit) {
		this.context = context;
		this.timeout = timeout;
		this.interval = interval;
		this.timeUnit = timeUnit;
	}

	/**
	 * @param context
	 *            the <code>WebDriver</code> or <code>WebElement</code> used as
	 *            search root
	 * @return a <code>WaitHelper</code> instance operating on the default
	 *         parameters
	 */
	public static WaitHelper waitOn(SearchContext context) {
		return waitOn(context, defaultTimeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param context
	 *            the <code>WebDriver</code> or <code>WebElement</code> used as
	 *            search root
	 * @param timeout
	 *            the maximum wait in milliseconds
	 * @return a <code>WaitHelper</code> instance operating on the above
	 *         parameters
	 */
	public static WaitHelper waitOn(SearchContext context, long timeout) {
		return waitOn(context, timeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param context
	 *            the <code>WebDriver</code> or <code>WebElement</code> used as
	 *            search root
	 * @param timeout
	 *            the maximum wait in milliseconds
	 * @param interval
	 *            the polling interval in milliseconds
	 * @return a <code>WaitHelper</code> instance operating on the above
	 *         parameters
	 */
	public static WaitHelper waitOn(SearchContext context, long timeout, long interval) {
		return waitOn(context, timeout, interval, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param context
	 *            the <code>WebDriver</code> or <code>WebElement</code> used as
	 *            search root
	 * @param timeout
	 *            the maximum wait duration
	 * @param timeUnit
	 *            the maximum wait time unit
	 * @return a <code>WaitHelper</code> instance operating on the above
	 *         parameters
	 */
	public static WaitHelper waitOn(SearchContext context, long timeout, TimeUnit timeUnit) {
		return new WaitHelper(context, timeout, defaultInterval, timeUnit);
	}

	/**
	 * @param context
	 *            the <code>WebDriver</code> or <code>WebElement</code> used as
	 *            search root
	 * @param timeout
	 *            the maximum wait duration
	 * @param interval
	 *            the polling interval in milliseconds
	 * @param timeUnit
	 *            the maximum wait time unit
	 * @return a <code>WaitHelper</code> instance operating on the above
	 *         parameters
	 */
	public static WaitHelper waitOn(SearchContext context, long timeout, long interval, TimeUnit timeUnit) {
		return new WaitHelper(context, timeout, interval, timeUnit);
	}

	/**
	 * @param millis
	 *            the maximum wait in milliseconds to be used
	 *            as default value
	 */
	public static void setDefaultTimeout(long millis) {
		WaitHelper.defaultTimeout = millis;
	}

	/**
	 * @param millis
	 *            the polling interval in milliseconds to be used
	 *            as default value
	 */
	public static void setDefaultInterval(long millis) {
		WaitHelper.defaultInterval = millis;
	}

	/**
	 * Holds the execution until an element matching the search condition is
	 * <b>found</b> or the maximum wait expires.
	 * 
	 * @param by
	 *            search condition
	 * @return the found element
	 */
	public WebElement untilAdded(final @NonNull By by) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					public WebElement apply(SearchContext context) {
						return context.findElement(by);
					}
				});
	}

	/**
	 * Holds the execution until <b>no</b> element matching the search condition
	 * is <b>found</b> or the maximum wait expires.
	 * 
	 * WARNING: the return value of this method might be <code>null</code> and,
	 * even when it is not, it represents a stale node.
	 * 
	 * @param by
	 *            search condition
	 * @return the last known version of the element or <code>null</code> if the
	 *         element was never found
	 */
	public WebElement untilRemoved(final @NonNull By by) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					private WebElement result;

					public WebElement apply(SearchContext context) {
						try {
							result = context.findElement(by);
						} catch (NoSuchElementException nsee) {
							return result;
						}
						throw new NoSuchElementException("Element found");
					}
				});
	}

	/**
	 * Holds the execution until an element matching the search condition is
	 * <b>found and visible</b> or the maximum wait expires.
	 * 
	 * @param by
	 *            search condition
	 * @return the found element
	 */
	public WebElement untilShown(final @NonNull By by) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					public WebElement apply(SearchContext context) {
						WebElement result = context.findElement(by);
						try {
							if (!result.isDisplayed()) {
								throw new NoSuchElementException("Element not visible");
							}
							return result;
						} catch (StaleElementReferenceException sere) {
							throw new NoSuchElementException("Element is currently stale");
						}
					}
				});
	}

	/**
	 * Holds the execution until an element matching the search condition is
	 * <b>found and non visible</b> or the maximum wait expires.
	 * 
	 * @param by
	 *            search condition
	 * @return the found element
	 */
	public WebElement untilHidden(final @NonNull By by) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					public WebElement apply(SearchContext context) {
						WebElement result = context.findElement(by);
						try {
							if (result.isDisplayed()) {
								throw new NoSuchElementException("Element is visible");
							}
							return result;
						} catch (StaleElementReferenceException sere) {
							throw new NoSuchElementException("Element is currently stale");
						}
					}
				});
	}

	/**
	 * Holds the execution until an element matching the search condition is
	 * <b>found and enabled</b> or the maximum wait expires.
	 * 
	 * @param by
	 *            search condition
	 * @return the found element
	 */
	public WebElement untilEnabled(final @NonNull By by) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					public WebElement apply(SearchContext context) {
						WebElement result = context.findElement(by);
						try {
							if (!result.isEnabled()) {
								throw new NoSuchElementException("Element not enabled");
							}
							return result;
						} catch (StaleElementReferenceException sere) {
							throw new NoSuchElementException("Element is currently stale");
						}
					}
				});
	}

	/**
	 * Holds the execution until an element matching the search condition is
	 * <b>found and disabled</b> or the maximum wait expires.
	 * 
	 * @param by
	 *            search condition
	 * @return the found element
	 */
	public WebElement untilDisabled(final @NonNull By by) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					public WebElement apply(SearchContext context) {
						WebElement result = context.findElement(by);
						try {
							if (result.isEnabled()) {
								throw new NoSuchElementException("Element is enabled");
							}
							return result;
						} catch (StaleElementReferenceException sere) {
							throw new NoSuchElementException("Element is currently stale");
						}
					}
				});
	}

	/**
	 * Holds the execution until an element matching the search condition is
	 * <b>found and the generic matcher is verified</b> or <b>not found</b> or
	 * the maximum wait expires.
	 * 
	 * @see org.openqa.selenium.lift.Matchers
	 * @see org.hamcrest.Matcher
	 * 
	 * @param by
	 *            search condition
	 * @param matcher
	 *            the matching condition to check
	 * @return the found element
	 */
	public WebElement until(final @NonNull By by, final @NonNull Matcher<WebElement> matcher) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, WebElement>() {
					public WebElement apply(SearchContext context) {
						WebElement result = context.findElement(by);
						try {
							if (!matcher.matches(result)) {
								throw new NoSuchElementException("Element does not match");
							}
							return result;
						} catch (StaleElementReferenceException sere) {
							throw new NoSuchElementException("Element is currently stale");
						}
					}
				});
	}

	/**
	 * Holds the execution until at the elements count matching the search
	 * condition <b>matches with the generic matcher condition</b> or the
	 * maximum wait expires.
	 * 
	 * @see org.openqa.selenium.lift.Matchers
	 * @see org.hamcrest.Matcher
	 * 
	 * @param by
	 *            search condition
	 * @param matcher
	 *            the matching condition to check
	 * @return a <code>java.util.List</code> containing found elements
	 */
	public List<WebElement> untilCount(final @NonNull By by, final @NonNull Matcher<Integer> matcher) {
		return new FluentWait<SearchContext>(context)
				.withTimeout(timeout, timeUnit)
				.pollingEvery(interval, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.until(new Function<SearchContext, List<WebElement>>() {
					public List<WebElement> apply(SearchContext context) {
						List<WebElement> result = context.findElements(by);
						try {
							if (!matcher.matches(result.size())) {
								throw new NoSuchElementException("Element does not match");
							}
							return result;
						} catch (StaleElementReferenceException sere) {
							throw new NoSuchElementException("Element is currently stale");
						}
					}
				});
	}

	/**
	 * Holds the execution until at the elements count matching the search
	 * condition <b>matches the provided count</b> or the maximum wait expires.
	 * 
	 * @see org.openqa.selenium.lift.Matchers
	 * @see org.hamcrest.Matcher
	 * 
	 * @param by
	 *            search condition
	 * @param count
	 *            the element count to match
	 * @return a <code>java.util.List</code> containing found elements
	 */
	public List<WebElement> untilCount(final @NonNull By by, final int count) {
		return this.untilCount(by, Matchers.exactly(count));
	}
}
