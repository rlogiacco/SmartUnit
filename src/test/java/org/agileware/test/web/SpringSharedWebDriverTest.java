package org.agileware.test.web;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.junit.Cucumber;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
@RunWith(Cucumber.class)
@Cucumber.Options(glue={"org.agileware.test.web.stepdefs"},features = "src/test/resources/sample.feature", format = "html:target/cucumber")
public class SpringSharedWebDriverTest {

	@SuppressWarnings("unused")
	@Autowired
	private SharedWebDriver browser;
}
