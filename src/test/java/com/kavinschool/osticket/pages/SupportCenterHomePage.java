package com.kavinschool.osticket.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class SupportCenterHomePage {
private WebDriver driver;
		
	@FindBy(how = How.CSS, using = "input.button2")
	@CacheLookup
	private WebElement openNewTicketButton;
	
	
	public SupportCenterHomePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getTitle() {
		return driver.getTitle();
		
	}
	
	public void clickAndOpenNewTicket() {
		openNewTicketButton.click();
	}
	
	public OpenNewTicketPage openNewTicket() {
		openNewTicketButton.click();
		return PageFactory.initElements(driver, OpenNewTicketPage.class);
	}
}
