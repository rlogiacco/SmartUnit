package org.agileware.test.mail;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class MailClientTest {

	protected String protocol;

	@Autowired
	private MailSender sender;

	private MailClient client = new MailClient();

	private void sendTestMessage(final String from, final String to, final String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(from);
		message.setSubject(subject);
		message.setText("message sent on " + System.currentTimeMillis());
		sender.send(message);
	}

	public void reset(String account) throws Exception {
		try {
			client.connect(protocol + "://" + account + ":" + account + "@localhost");
			client.deleteAll();
			client.close();
		} catch (MessagingException me) {
			// fail silently
		}
	}

	@Test
	public void testConnect() throws Exception {
		sendTestMessage("me", "test", "something");
		client.connect(protocol + "://test:test@localhost");
	}

	@Test(expected = MessagingException.class)
	public void testConnectNone() throws Exception {
		client.connect(protocol + "://none:none@localhost");
	}

	@Test
	public void testDeleteAll() throws Exception {
		this.reset("deleteAll");
		
		for (int i = 0; i < 10; i++) {
			sendTestMessage("me", "deleteAll", "something" + i);
		}

		client.connect(protocol + "://deleteAll:deleteAll@localhost");

		assertEquals(10, client.listAll().length);
		client.deleteAll().flush();
		assertEquals(0, client.listAll().length);
	}

	@Test
	public void testDeleteAllEmptyInbox() throws Exception {
		this.reset("deleteAll");
		client.connect(protocol + "://deleteAll:deleteAll@localhost");
		assertEquals(0, client.listAll().length);
		client.deleteAll().flush();
		assertEquals(0, client.listAll().length);
	}

	@Test
	public void testPop() {
		fail("Not yet implemented");
	}

	@Test
	public void testListAll() throws Exception {
		client.connect(protocol + "://listAll:listAll@localhost").deleteAll().close();
		System.out.println("OK");
		// Thread.sleep(2000);
		assertEquals(0, client.connect("pop3://listAll:listAll@localhost").listAll().length);
		for (int i = 0; i < 10; i++) {
			sendTestMessage("me", "listAll", "something" + i);
		}
		client.connect(protocol + "://listAll:listAll@localhost");
		assertEquals(10, client.listAll().length);

		for (int i = 0; i < 10; i++) {
			sendTestMessage("me", "listAll", "something" + i);
		}

		assertEquals(20, client.listAll().length);

		client.deleteAll().close();
	}

	@Test
	public void testFind() throws Exception {
		client.connect(protocol + "://find:find@localhost");
		for (int i = 0; i < 10; i++) {
			sendTestMessage("me", "find", "something" + i);
		}

		// assertEquals(1, client.find().length);
	}

	@Test
	public void testFindNoMatch() throws Exception {
		client.connect(protocol + "://find:find@localhost");
		for (int i = 0; i < 10; i++) {
			sendTestMessage("me", "deleteAll", "something" + i);
		}

		assertEquals(10, client.listAll().length);
	}

}
