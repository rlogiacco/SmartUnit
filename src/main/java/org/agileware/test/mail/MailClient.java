package org.agileware.test.mail;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.SearchTerm;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;

public class MailClient {

	private Folder folder;
	private final Store store;
	private final int timeout;

	public MailClient(String url) throws MessagingException {
		this(url, "INBOX", 10);
	}
	
	public MailClient(String url, int timeout) throws MessagingException {
		this(url, "INBOX", timeout);
	}
	
	public MailClient(String url, String folderName) throws MessagingException {
		this(url, folderName, 10);
	}

	public MailClient(String url, String folderName, int timeout) throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);

		this.store = session.getStore(new URLName(url));
		this.store.connect();
		this.folder = store.getFolder(folderName);
		this.folder.open(Folder.READ_WRITE);
		this.timeout = timeout;
	}

	public MailClient deleteAll() throws MessagingException {
		for (Message message : folder.getMessages()) {
			message.setFlag(Flag.DELETED, true);
		}
		this.update();
		return this;
	}

	public Message[] find(SearchTerm search) throws MessagingException {
		this.update();
		return folder.search(search);
	}

	public Message[] listAll() throws MessagingException {
		this.update();
		Message[] messages = folder.getMessages();
//		FetchProfile profile = new FetchProfile();
//		profile.add(FetchProfile.Item.ENVELOPE);
//		folder.fetch(messages, profile);
		return messages;
	}

	public MailClient update() throws MessagingException {
		folder.close(true);
		//store.close();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//store.connect();
		folder = store.getFolder(folder.getName());
		folder.open(Folder.READ_WRITE);
		return this;
	}

	public void close() throws Exception {
		folder.close(true);
		store.close();
	}

	public Message waitFor(final SearchTerm search, final Matcher<Message> matcher) throws MessagingException {
		return new FluentWait<MailClient>(this).withTimeout(timeout, TimeUnit.SECONDS).ignoring(NoSuchElementException.class).until(new Function<MailClient, Message>() {
			public Message apply(MailClient client) {
				try {
					Message[] messages = client.find(search);
					for (Message message : messages) {
						if (matcher.matches(message)) {
							return message;
						}
					}
					throw new NoSuchElementException("No match found");
				} catch (MessagingException me) {
					throw new NoSuchElementException("No match found");
				}
			}
		});
	}

	public Message[] waitForCount(final int exactly) throws MessagingException {
		return this.waitForCount(null, Matchers.equalTo(exactly));
	}
	
	public Message[] waitForCount(final Matcher<Integer> matcher) throws MessagingException {
		return this.waitForCount(null, matcher);
	}

	public Message[] waitForCount(final SearchTerm search, final Matcher<Integer> matcher) throws MessagingException {
		return new FluentWait<MailClient>(this).withTimeout(timeout, TimeUnit.SECONDS).ignoring(NoSuchElementException.class).until(new Function<MailClient, Message[]>() {
			public Message[] apply(MailClient client) {
				try {
					Message[] result = search != null ? client.find(search) :client.listAll();
					if (!matcher.matches(result.length)) {
						System.out.println("Message count doesn't match: " + result.length);
						throw new NoSuchElementException("Message count doesn't match");
					}
					return result;
				} catch (MessagingException me) {
					throw new NoSuchElementException("Message count can't be retrieved");
				}
			}
		});
	}
	
	public static String toString(Address[] addresses) {
		StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		for (Address address : addresses) {
			builder.append(address.toString());
			builder.append(", ");
		}
		if (builder.length() > 2) {
			builder.setLength(builder.length() - 2);
		}
		builder.append(" ]");
		return builder.toString();
	}
	
	public static String toString(Flags flags) {
		StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		for (Flag flag : flags.getSystemFlags()) {
			builder.append(flag.toString());
			builder.append(", ");
		}
		if (builder.length() > 2) {
			builder.setLength(builder.length() - 2);
		}
		builder.append(" ]");
		return builder.toString();
	}

	public static String toString(Message message) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("FROM: ").append(toString(message.getFrom()));
		builder.append("TO  : ").append(toString(message.getRecipients(RecipientType.TO)));
		builder.append("SUBJ: ").append(message.getSubject());
		builder.append("SENT: ").append(message.getSentDate());
		builder.append("FLAG: ").append(toString(message.getFlags()));
		return builder.toString();
	}
}
