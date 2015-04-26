package org.agileware.test.mail;

import java.util.Date;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.search.*;

public class SearchTerms {
	
	public static SearchTerm and(SearchTerm... searchTerms) {
		return new AndTerm(searchTerms);
	}
	
	public static SearchTerm or(SearchTerm... searchTerms) {
		return new OrTerm(searchTerms);
	}
	
	public static SearchTerm not(SearchTerm searchTerm) {
		return new NotTerm(searchTerm);
	}

	public static SearchTerm recipient(RecipientType type, String pattern) {
		return new RecipientStringTerm(type, pattern);
	}
	
	public static SearchTerm from(String pattern) {
		return new FromStringTerm(pattern);
	}

	public static SearchTerm to(String pattern) {
		return new RecipientStringTerm(RecipientType.TO, pattern);
	}

	public static SearchTerm cc(String pattern) {
		return new RecipientStringTerm(RecipientType.CC, pattern);
	}

	public static SearchTerm bcc(String pattern) {
		return new RecipientStringTerm(RecipientType.BCC, pattern);
	}
	
	public static SearchTerm replyTo(String pattern) {
		return new HeaderTerm("Reply-To", pattern);
	}

	public static SearchTerm received(Compare comparison, Date date) {
		return new ReceivedDateTerm(comparison.value, date);
	}
	
	public static SearchTerm receivedBefore(Date date) {
		return SearchTerms.received(Compare.LT, date);
	}
	
	public static SearchTerm receivedAfter(Date date) {
		return SearchTerms.received(Compare.GT, date);
	}
	
	public static SearchTerm sent(Compare comparison, Date date) {
		return new SentDateTerm(comparison.value, date);
	}
	
	public static SearchTerm sentBefore(Date date) {
		return SearchTerms.received(Compare.LT, date);
	}
	
	public static SearchTerm sentAfter(Date date) {
		return SearchTerms.received(Compare.GT, date);
	}
	
	public static SearchTerm subject(String pattern) {
		return new SubjectTerm(pattern);
	}
	
	public static SearchTerm content(String pattern) {
		return new BodyTerm(pattern);
	}
	
	public static SearchTerm header(String name, String pattern) {
		return new HeaderTerm(name, pattern);
	}
	
	public static SearchTerm size(Compare comparison, int size) {
		return new SizeTerm(comparison.value, size);
	}
	
	public static SearchTerm flagsOn(Flags flags) {
		return new FlagTerm(flags, true);
	}
	
	public static SearchTerm flagsOff(Flags flags) {
		return new FlagTerm(flags, false);
	}
	
	public static SearchTerm flagOn(Flag flag) {
		return new FlagTerm(new Flags(flag), true);
	}
	
	public static SearchTerm flagOff(Flag flag) {
		return new FlagTerm(new Flags(flag), false);
	}
	
	public static SearchTerm read() {
		return new FlagTerm(new Flags(Flag.SEEN), true);
	}
	
	public static SearchTerm unread() {
		return new FlagTerm(new Flags(Flag.SEEN), false);
	}
	
	public static SearchTerm answered() {
		return new FlagTerm(new Flags(Flag.ANSWERED), true);
	}
	
	public static SearchTerm unanswered() {
		return new FlagTerm(new Flags(Flag.ANSWERED), false);
	}

	public static enum Compare {
		LE(1), LT(2), EQ(3), NE(4), GT(5), GE(6);
		
		private final int value;

		private Compare(int value) {
			this.value = value;
		}
	}
}
