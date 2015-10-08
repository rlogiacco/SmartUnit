package org.agileware.test.web;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * @author Roberto Lo Giacco <rlogiacco@gmail.com>
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(glue = "org.agileware.test.web.stepdefs", features = "classpath:sample.feature", format = "html:target/cucumber")
public class SharedWebDriverSpringIT {

}
