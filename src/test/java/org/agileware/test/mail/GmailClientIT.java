package org.agileware.test.mail;

import javax.mail.Flags.Flag;
import javax.mail.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.lift.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/mail.xml")
@ActiveProfiles("gmail")
public class GmailClientIT {

	private final static String GMAIL_IMAP ="imaps://smartunit%40agileware.org:50m37h1n6@imap.gmail.com/";
	
	@Autowired
	private MailSender sender;

	private MailClient client;
	
	private void sendMessage(String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("smartunit@agileware.org");
		message.setSubject(subject);
		message.setText(body);
		sender.send(message);
	}
	
	@Before
	public void before() throws Exception {
		client = new MailClient(GMAIL_IMAP);
	}
	
	@Test
	public void testListAll() throws Exception {
		Message[] messages = client.listAll();
		for(Message message: messages) {
			System.out.println(message.getSubject());			
		}
	}
	
	@Test
	public void testReadAll() throws Exception {
		for (Message message : client.find(SearchTerms.unread())) {
			message.setFlag(Flag.SEEN, true);
		}
		assertEquals(0, client.find(SearchTerms.unread()).length);
	}
	
	@Test
	public void testUnread() throws Exception {
		int unread = client.find(SearchTerms.unread()).length;
		
		this.sendMessage("test", "ciao");
		
		client.waitForCount(SearchTerms.unread(), Matchers.exactly(unread + 1));
		assertEquals(unread + 1, client.find(SearchTerms.unread()).length);
		for (Message message : client.find(SearchTerms.unread())) {
			if (message.getSubject().equals("test"))
				message.setFlag(Flag.SEEN, true);
		}
		client.close();
	}

}
