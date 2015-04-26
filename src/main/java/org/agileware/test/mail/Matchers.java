package org.agileware.test.mail;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {
	public static Matcher<Message> bySubject(final Matcher<String> matcher) {
		return new TypeSafeMatcher<Message>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("subject ");
				matcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(Message message) {
				try {
					return matcher.matches(message.getSubject());
				} catch (MessagingException e) {
					return false;
				}
			}
		};
	}
	
	public static Matcher<Message> byContent(final Matcher<String> matcher) {
		return new TypeSafeMatcher<Message>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("subject ");
				matcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(Message message) {
				try {
					return matcher.matches(message.getContent());
				} catch (Exception e) {
					return false;
				}
			}
		};
	}

	public static Matcher<Message> bySender(final Matcher<String> matcher) {
		return new TypeSafeMatcher<Message>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("from ");
				matcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(Message message) {
				try {
					return matcher.matches(message.getFrom());
				} catch (MessagingException e) {
					return false;
				}
			}
		};
	}

	public static Matcher<Message> byRecipient(final RecipientType recipientType, final Matcher<String> matcher) {
		return new TypeSafeMatcher<Message>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("from ");
				matcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(Message message) {
				try {
					for (Address address : message.getRecipients(recipientType)) {
						return matcher.matches(address.toString());
					}
				} catch (MessagingException e) {
					// fail silently
				}
				return false;
			}
		};
	}

	public static Matcher<Message> byTo(final Matcher<String> matcher) {
		return byRecipient(RecipientType.TO, matcher);
	}

	public static Matcher<Message> byCc(final Matcher<String> matcher) {
		return byRecipient(RecipientType.CC, matcher);
	}

	public static Matcher<Message> byBcc(final Matcher<String> matcher) {
		return byRecipient(RecipientType.BCC, matcher);
	}
	

	public static Matcher<Integer> greaterThan(final int value) {
		return new TypeSafeMatcher<Integer>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("value ").appendValue(value);
			}

			@Override
			protected boolean matchesSafely(Integer match) {
				return match > value;
			}
		};
	}
	
	public static Matcher<Integer> greaterEquals(final int value) {
		return new TypeSafeMatcher<Integer>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("value ").appendValue(value);
			}

			@Override
			protected boolean matchesSafely(Integer match) {
				return match >= value;
			}
		};
	}

	public static Matcher<Integer> equalsTo(final int value) {
		return new TypeSafeMatcher<Integer>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("value ").appendValue(value);
			}

			@Override
			protected boolean matchesSafely(Integer match) {
				return match == value;
			}
		};
	}
	
	public static Matcher<Integer> lesserEquals(final int value) {
		return new TypeSafeMatcher<Integer>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("value ").appendValue(value);
			}

			@Override
			protected boolean matchesSafely(Integer match) {
				return match <= value;
			}
		};
	}
	
	public static Matcher<Integer> lesserThan(final int value) {
		return new TypeSafeMatcher<Integer>() {

			/**
			 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
			 */
			public void describeTo(Description description) {
				description.appendText("value ").appendValue(value);
			}

			@Override
			protected boolean matchesSafely(Integer match) {
				return match < value;
			}
		};
	}
	
	public static Matcher<String> equals(final String value) {
		return null;
	}
	
	public static Matcher<String> equalsIgnoreCase(final String value) {
		return null;
	}
	
	public static Matcher<String> contains(final String value) {
		return null;
	}
	
	public static Matcher<String> containsIgnoreCase(final String value) {
		return null;
	}
	
	public static Matcher<String> startsWith(final String value) {
		return null;
	}
	
	public static Matcher<String> startsWithIgnoreCase(final String value) {
		return null;
	}
	
	public static Matcher<String> endsWith(final String value) {
		return null;
	}

	public static Matcher<String> endsWithIgnoreCase(final String value) {
		return null;
	}
}
