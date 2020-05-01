package com.kavinschool.osticket.tests;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kavinschool.osticket.pages.SupportCenterHomePage;
import com.kavinschool.osticket.utils.Utils;

public class SupportCenterHomePageTest extends Utils{

	private WebDriver driver;
	
	@BeforeMethod
	public void setUp() throws Exception {
		System.out.println("setUp");
		driver = getGridDriver();
		//driver = getDriver();
		driver.get("http://osticket.kavinschool.com/");
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		System.out.println("tearDown");
		driver.quit();
	}
	
	@Test
	public void test() throws InterruptedException {
		System.out.println("testOpenTicket");
		SupportCenterHomePage homePage = PageFactory.initElements(driver,
				SupportCenterHomePage.class);
		Assert.assertEquals("KavinSchool:: Support Ticket System", homePage.getTitle());
		homePage.clickAndOpenNewTicket();
		Thread.sleep(3000);
	}
}
