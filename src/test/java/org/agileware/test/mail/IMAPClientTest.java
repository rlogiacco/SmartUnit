package org.agileware.test.mail;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/mail.xml")
public class IMAPClientTest extends MailClientTest {

	public IMAPClientTest() {
		super.protocol = "imap";
	}
}
