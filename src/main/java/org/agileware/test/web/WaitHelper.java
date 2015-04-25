package org.agileware.test.web;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;

/**
 * 
 *
 */
public class WaitHelper {

	private SearchContext context;
	private long duration;
	private TimeUnit timeUnit;

	/**
	 * @param context
	 * @param duration
	 * @param timeUnit
	 */
	protected WaitHelper(SearchContext context, long duration, TimeUnit timeUnit) {
		this.context = context;
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	/**
	 * @param context
	 * @param millis
	 * @return
	 */
	public static WaitHelper waitOn(SearchContext context, long millis) {
		return waitOn(context, millis, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param context
	 * @param duration
	 * @param timeUnit
	 * @return
	 */
	public static WaitHelper waitOn(SearchContext context, long duration, TimeUnit timeUnit) {
		return new WaitHelper(context, duration, timeUnit);
	}

	/**
	 * @param by
	 * @return
	 */
	public WebElement untilAdded(final By by) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
			public WebElement apply(SearchContext context) {
				return context.findElement(by);
			}
		});
	}

	/**
	 * @param by
	 * @return
	 */
	public WebElement untilRemoved(final By by) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
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
	 * @param by
	 * @return
	 */
	public WebElement untilShown(final By by) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
			public WebElement apply(SearchContext context) {
				WebElement result = context.findElement(by);
				if (!result.isDisplayed()) {
					throw new NoSuchElementException("Element not visible");
				}
				return result;
			}
		});
	}

	/**
	 * @param by
	 * @return
	 */
	public WebElement untilHidden(final By by) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
			public WebElement apply(SearchContext context) {
				WebElement result = context.findElement(by);
				if (result.isDisplayed()) {
					throw new NoSuchElementException("Element is visible");
				}
				return result;
			}
		});
	}

	/**
	 * @param by
	 * @return
	 */
	public WebElement untilEnabled(final By by) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
			public WebElement apply(SearchContext context) {
				WebElement result = context.findElement(by);
				if (!result.isEnabled()) {
					throw new NoSuchElementException("Element not enabled");
				}
				return result;
			}
		});
	}

	/**
	 * @param by
	 * @return
	 */
	public WebElement untilDisabled(final By by) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
			public WebElement apply(SearchContext context) {
				WebElement result = context.findElement(by);
				if (result.isEnabled()) {
					throw new NoSuchElementException("Element is enabled");
				}
				return result;
			}
		});
	}

	/**
	 * @param by
	 * @param matcher
	 * @return
	 */
	public WebElement until(final By by, final Matcher<WebElement> matcher) {
		return new FluentWait<SearchContext>(context).withTimeout(duration, timeUnit).ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {
			public WebElement apply(SearchContext context) {
				WebElement result = context.findElement(by);
				if (!matcher.matches(result)) {
					throw new NoSuchElementException("Element does not match");
				}
				return result;
			}
		});
	}
}
