package org.agileware.test.mail;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.Assert.assertEquals;

public abstract class MailClientTest {

	protected String protocol;

	@Autowired
	private MailSender sender;

	private void sendMessage(final String from, final String to, final String subject) throws InterruptedException {
		this.sendMessages(1, from, to, subject);
	}
	
	private void sendMessages(final int count, final String from, final String to, final String subject) throws InterruptedException {
		for (int i = 0; i < count; i++) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setFrom(from);
			message.setSubject("[" + i + "] " + subject);
			message.setText("message sent on " + System.currentTimeMillis());
			sender.send(message);
		}
		Thread.sleep(1000);
	}

	public void reset(String account) throws Exception {
		sendMessage("me", account, "something");
		MailClient client = new MailClient(protocol + "://" + account + ":" + account + "@localhost");
		
		try {
			client.deleteAll().waitForCount(0);
		} catch (TimeoutException te) {
			System.out.println("Reset failed");
			throw te;
		}
		
	}

	public void inspect(String account) throws Exception {
		Message[] messages = new MailClient(protocol + "://" + account + ":" + account + "@localhost").listAll();
		System.out.println("# inspecting: " + account);
		for (Message message : messages) {
			System.out.println("--- message ---");
			System.out.println(MailClient.toString(message));
		}
		System.out.println("# inspection complete: found " + messages.length);
	}
	
	@Test
	public void testConnect() throws Exception {
		sendMessage("me", "test", "something");
		new MailClient(protocol + "://test:test@localhost");
	}

	@Test(expected = MessagingException.class)
	public void testConnectNone() throws Exception {
		new MailClient(protocol + "://none:none@localhost");
	}

	@Test
	public void testDeleteAll() throws Exception {
		this.reset("deleteAll");
		
		sendMessages(10, "me", "deleteAll", "something");

		MailClient client = new MailClient(protocol + "://deleteAll:deleteAll@localhost");

		assertEquals(10, client.listAll().length);
		client.deleteAll().waitForCount(0);
		assertEquals(0, client.listAll().length);
	}

	@Test
	public void testDeleteAllEmptyInbox() throws Exception {
		this.reset("deleteAll");
		
		MailClient client = new MailClient(protocol + "://deleteAll:deleteAll@localhost");
		assertEquals(0, client.listAll().length);
		client.deleteAll().waitForCount(0);
		assertEquals(0, client.listAll().length);
	}

	@Test
	public void testListAll() throws Exception {
		this.reset("listAll");
		
		MailClient client = new MailClient(protocol + "://listAll:listAll@localhost");

		assertEquals(0, client.listAll().length);
		sendMessages(10, "me", "listAll", "something");
		assertEquals(10, client.listAll().length);

		sendMessages(10, "me", "listAll", "something");

		assertEquals(20, client.listAll().length);
	}

	@Test
	public void testFind() throws Exception {
		this.reset("find");

		sendMessages(10, "me", "find", "something");

		MailClient client = new MailClient(protocol + "://find:find@localhost");
		assertEquals(10, client.find(SearchTerms.from("me")).length);
	}

	@Test
	public void testFindNoMatch() throws Exception {
		this.reset("find");
		
		sendMessages(10, "me", "findNone", "something");
		
		MailClient client = new MailClient(protocol + "://find:find@localhost");
		assertEquals(0, client.listAll().length);
	}

}
