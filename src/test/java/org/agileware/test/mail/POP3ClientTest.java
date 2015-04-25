package org.agileware.test.mail;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/mail.xml")
public class POP3ClientTest extends MailClientTest {
	public POP3ClientTest() {
		super.protocol = "pop3";
	}
}
