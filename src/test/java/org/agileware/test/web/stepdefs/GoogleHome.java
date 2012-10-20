package org.agileware.test.web.stepdefs;

import static org.junit.Assert.*;

import org.agileware.test.web.SharedWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.en.And;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;

public class GoogleHome {
	
	@Autowired
	private SharedWebDriver browser;

	@Given("^the user has a web browser$")
	public void the_user_has_a_web_browser() throws Throwable {
		assertNotNull(browser);
	}

	@And("^opens the URL \"([^\"]*)\"$")
	public void opens_the_URL(String arg) throws Throwable {
		browser.get(arg);
	}

	@Then("^the Google home page should be shown$")
	public void the_google_home_page_should_be_shown() throws Throwable {
		assertTrue(browser.getCurrentUrl().contains("http://www.google"));
	}
	
	@Then("^the Google Ireland home page should be shown$")
	public void the_google_ireland_home_page_should_be_shown() throws Throwable {
		assertEquals("http://www.google.ie/", browser.getCurrentUrl());
	}
	
	@Then("^the Google Italy home page should be shown$")
	public void the_google_italy_home_page_should_be_shown() throws Throwable {
		assertEquals("http://www.google.it/", browser.getCurrentUrl());
	}
}