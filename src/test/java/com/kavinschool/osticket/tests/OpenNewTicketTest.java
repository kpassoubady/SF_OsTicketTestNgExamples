package com.kavinschool.osticket.tests;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kavinschool.osticket.pages.OpenNewTicketPage;
import com.kavinschool.osticket.pages.SupportCenterHomePage;
import com.kavinschool.osticket.utils.Utils;

public class OpenNewTicketTest extends Utils{
	private WebDriver driver;	
	private StringBuffer verificationErrors = new StringBuffer();
	

	
	@BeforeMethod
	public void setUp() throws Exception {
	    driver = getGridDriver();	
		//driver = getDriver();
		navigateToBaseURL();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	@Test
	public void testOpenTicket(Method m) throws Exception {
		final String strName = "Kangs";
		final String strEmail = "test.kavinschool@gmail.com";
		final String strPhone = "123-456-7890";
		final String strHelpTopic = "Education";
		final String strTimeStamp = getDateTimeStamp();
		final String strSubject = "Testing Subject " + strTimeStamp;
		final String strTicketMessage = "This is a test message: " + strTimeStamp;
		String screenShotfileName;

		System.out.println("Before SupportCenterHomePage");
		SupportCenterHomePage homePage = PageFactory.initElements(driver,
				SupportCenterHomePage.class);
		
		OpenNewTicketPage newTicket = homePage.openNewTicket();
		Assert.assertEquals("KavinSchool:: Support Ticket System", homePage.getTitle());

		screenShotfileName = getScreenShotFileName(this.getClass().getName(),
				m.getName());
		saveScreenShotTo(screenShotfileName);
		
		System.out.println("Before SupportCenterHomePage");
		
		newTicket.typeName(strName);
		newTicket.typeEmail(strEmail);
		newTicket.typePhone(strPhone);
		newTicket.selectHelpTopic(strHelpTopic, "topicId");
		newTicket.typeSubject(strSubject);
		newTicket.typeMessage(strTicketMessage);
		
//		screenShotfileName = getScreenShotFileName(this.getClass().getName(),
//				m.getName());
//		saveScreenShotTo(screenShotfileName);
		
		newTicket.submitTicket();
		
//		screenShotfileName = getScreenShotFileName(this.getClass().getName(),
//				m.getName());
//		saveScreenShotTo(screenShotfileName);
		
		delay(3000);
	}
}
