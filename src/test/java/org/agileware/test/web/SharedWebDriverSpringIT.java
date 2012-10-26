package org.agileware.test.web;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.junit.Cucumber;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
@RunWith(Cucumber.class)
@Cucumber.Options(glue="org.agileware.test.web.stepdefs",features = "classpath:sample.feature", format = "html:target/cucumber")
public class SharedWebDriverSpringIT {

	@SuppressWarnings("unused")
	@Autowired
	private SharedWebDriver browser;
}
