Feature: As a user I want to be able to connect to google

Scenario: As a user I connect to google

	Given the user has a web browser
	And opens the URL "https://www.google.com"
	Then the Google home page should be shown
	
Scenario: As a user I connect to google

	Given the user has a web browser
	And opens the URL "https://www.google.ie"
	Then the Google Ireland home page should be shown
	
Scenario: As a user I connect to google

	Given the user has a web browser
	And opens the URL "https://www.google.it"
	Then the Google Italy home page should be shown
		
	