package org.agileware.test.mail;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Predicate;

public class MailClient {

	private Session session;
	private Store store;

	public MailClient connect(String url) throws Exception {
		Properties props = new Properties();
		session = Session.getDefaultInstance(props);

		store = session.getStore(new URLName(url));
		store.connect();
		return this;
	}

	public MailClient deleteAll() throws Exception {
		return this.deleteAll("INBOX");
	}

	public MailClient deleteAll(String folderName) throws Exception {
		Folder folder = store.getFolder(folderName);
		folder.open(Folder.READ_WRITE);
		for (Message message : folder.getMessages()) {
			System.out.println("deleting: " + message.getContent());
			message.setFlag(Flag.DELETED, true);
		}
		folder.close(true);
		store.close();
		new FluentWait<String>(folderName)
				.withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS)
				.until(new Predicate<String>() {
			public boolean apply(String folderName) {
				
				try {
					store.connect();
					store.getFolder(folderName);
					Folder folder = store.getFolder(folderName);
						return folder.getMessageCount() <= 0;
				} catch (MessagingException e) {
					e.printStackTrace();
					return false;
				} finally {
					try {
						store.close();
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		return this;
	}

	public Message pop() throws Exception {
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);
		Message message = folder.getMessage(1);
		message.setFlag(Flag.DELETED, true);
		return message;
	}

	public Message[] listAll() throws Exception {
		return this.listAll("INBOX");
	}
	public Message[] listAll(String folderName) throws Exception {
		Folder folder = store.getFolder(folderName);
		folder.open(Folder.READ_WRITE);
		//List<Message> messages = new ArrayList<Message>();
		for (Message message : folder.getMessages()) {
			if (message.getFlags().contains(Flag.DELETED)) {
				System.out.println("DELETED BUT LISTED " + message.getContent());
			}
		}
		Message[] messages = folder.getMessages();
		//FetchProfile profile = new FetchProfile();
		//profile.add(FetchProfile.Item.ENVELOPE);
		//folder.fetch(messages, profile);
		return messages;
	}
	
	public MailClient flush() throws Exception {
		store.close();
		store.connect();
		return this;
	}
	
	public MailClient close() throws Exception {
		store.close();
		return this;
	}
}
