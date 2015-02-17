package org.agileware.test.web.stepdefs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.agileware.test.web.SharedWebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class GoogleHome {
	
	@Autowired
	private SharedWebDriver browser;
	
	@After
	public void embedScreenshot(Scenario result) {  
        if (result.isFailed()) {  
            try {  
                byte[] screenshot = ((TakesScreenshot) browser).getScreenshotAs(OutputType.BYTES);  
                result.embed(screenshot, "image/png");  
            } catch (WebDriverException wde) {  
                System.err.println(wde.getMessage());  
            } catch (ClassCastException cce) {  
                cce.printStackTrace();  
            }  
        }
	}

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
		assertTrue(browser.getCurrentUrl().startsWith("https://www.google"));
	}
	
	@Then("^the Google Ireland home page should be shown$")
	public void the_google_ireland_home_page_should_be_shown() throws Throwable {
		assertEquals("https://www.google.ie/", browser.getCurrentUrl());
	}
	
	@Then("^the Google Italy home page should be shown$")
	public void the_google_italy_home_page_should_be_shown() throws Throwable {
		assertEquals("https://www.google.it/", browser.getCurrentUrl());
	}
}