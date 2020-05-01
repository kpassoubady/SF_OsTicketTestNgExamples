package com.kavinschool.osticket.utils;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

//import com.opera.core.systems.OperaDriver;

public class Utils {

	private WebDriver driver;
	protected String browserType;
	private final String SERV_PROP_FILE = "src/test/resources/server.properties.txt";
	private Properties props;
	protected ChromeDriverService service;
	public final String PAGE_TITLE = "KavinSchool:: Support Ticket System";
	protected String googleChromeDriverPath;


	protected WebDriver getDriver() throws FileNotFoundException, IOException {
		props = new Properties();
		props.load(new FileInputStream(SERV_PROP_FILE));
		browserType = props.getProperty("mode");

		System.out.println("BrowserType: " + browserType);
		if (browserType.equalsIgnoreCase("firefox")) {
			String ffBrowserPath = props.getProperty("ff_browser_path");
			File file = new File(ffBrowserPath);
			System.setProperty("webdriver.firefox.bin", file.getAbsolutePath());
			driver = new FirefoxDriver();
		}

		if (browserType.equalsIgnoreCase("iexplore")) {
			String ieDriverPath = props.getProperty("ie_driver_path");	
			File file = new File(ieDriverPath);
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			driver = new InternetExplorerDriver();
		}

		if (browserType.equalsIgnoreCase("googlechrome")) {
			String gcDriverPath = props.getProperty("gc_driver_path");	
			File file = new File(gcDriverPath);
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver = new ChromeDriver();
			
		}

//		if (browserType.equalsIgnoreCase("opera")) {
//			driver = new OperaDriver();
//		}

		if (browserType.equalsIgnoreCase("htmlunit")) {
			driver = new HtmlUnitDriver();
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		}

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return driver;
	}

	protected WebDriver getGridDriver() throws FileNotFoundException,
			IOException {
		props = new Properties();
		props.load(new FileInputStream(SERV_PROP_FILE));
		browserType = props.getProperty("mode");
		
		String seleniumGridHub = props.getProperty("selenium_grid_hub");
		LoggingPreferences logs = new LoggingPreferences(); 
		logs.enable(LogType.DRIVER, Level.OFF); 
		DesiredCapabilities capability=null;

		System.out.println("BrowserType: " + browserType);
		
		if (browserType.equalsIgnoreCase("firefox")) {
			String ffBrowserPath = props.getProperty("ff_browser_path");
			File file = new File(ffBrowserPath);
			System.setProperty("webdriver.firefox.bin", file.getAbsolutePath());
			capability = DesiredCapabilities.firefox();
			capability.setBrowserName("firefox");
			capability.setJavascriptEnabled(true);
			capability.setCapability(CapabilityType.LOGGING_PREFS, logs); 
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
		}

		if (browserType.equalsIgnoreCase("iexplore")) {
			String ieDriverPath = props.getProperty("ie_driver_path");	
//			String ieBrowserPath = props.getProperty("ie_browser_path");
			File file = new File(ieDriverPath);
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			capability = DesiredCapabilities.internetExplorer();
			capability.setBrowserName("iexplore");
			capability.setJavascriptEnabled(true);
			capability.setCapability(CapabilityType.LOGGING_PREFS, logs);
			capability.setCapability(
				    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				    true);
			capability.setCapability("ignoreProtectedModeSettings", true);
			capability.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		}

		if (browserType.equalsIgnoreCase("googlechrome")) {
			String gcDriverPath = props.getProperty("gc_driver_path");	
			String gcBrowserPath = props.getProperty("gc_browser_path");
			File file = new File(gcDriverPath);
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			
			service = new ChromeDriverService.Builder()
			// Older Versions use the below method
			// .usingChromeDriverExecutable(new
			// File(googleChromeDriverPath))
			// -- For New Version use the below method
			.usingDriverExecutable(new File(gcDriverPath))
			.usingAnyFreePort().build();
			service.start();
			
			capability = DesiredCapabilities.chrome();
			capability.setBrowserName("chrome");
			capability.setCapability("chrome.switches", Arrays.asList(
					"--start-maximized",
					"--silent",
					"--ignore-certificate-errors", 
					"--disable-popup-blocking",
					"--disable-translate"));
			capability.setCapability("chrome.binary", gcBrowserPath);
			capability.setCapability(CapabilityType.LOGGING_PREFS, logs);
			capability.setJavascriptEnabled(true);
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
		}

		// Headless Browser
		if (browserType.equalsIgnoreCase("htmlunit")) {
			capability = DesiredCapabilities.htmlUnit();
			capability.setCapability(CapabilityType.LOGGING_PREFS, logs); 
			capability.setBrowserName("");
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		}

		driver = new ScreenShotRemoteWD(new URL(seleniumGridHub), capability);
		
		if (!browserType.equalsIgnoreCase("htmlunit")) {
			driver.manage().window().maximize();
		}
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return driver;
	}	
	
	protected String getBaseURL()  {
		props = new Properties();
		try {
			props.load(new FileInputStream(SERV_PROP_FILE));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return props.getProperty("url");
	}

	protected void navigateToBaseURL() {
		driver.navigate().to(getBaseURL() + "/osticket/");
	}
	
	protected void openURL() {
		driver.get(getBaseURL() + "/osticket/");
	}
	
	protected void navigateToGmail() {
		driver.navigate().to(getBaseURL() + "/osticket/");
	}
	
	protected void clearAndType(WebElement field, String text) {
		field.clear();
		field.sendKeys(text);
	}

	protected String getDateTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHmmss");
		java.util.Date today = new java.util.Date();
		return formatter.format(new java.sql.Timestamp(today.getTime()));
	}

	protected boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	protected void selectOption(WebDriver driver, final String selectOption,
			final String selectLocator) {
		Select select = new Select(driver.findElement(By.name(selectLocator)));
		select.selectByVisibleText(selectOption);
	}
	
	protected void selectOption(WebElement selectedElement, final String selectOption)
			 {
		Select select = new Select(selectedElement);
		select.selectByVisibleText(selectOption);
	}
	
	protected boolean isTextPresent(WebDriver driver, String Text) {
		try {
			return driver.findElement(By.tagName("body")).getText()
					.contains(Text);
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	protected void saveScreenShotTo(String screenShotfileName) {
		System.out.println("saveScreenShotTo");
		if (browserType.equalsIgnoreCase("htmlunit"))
			return;
		try {
			writeScreenShot(screenShotfileName);
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}
	}

	private void writeScreenShot(String screenShotfileName)
			throws IOException {
		System.out.println("writeScreenShot: " + screenShotfileName);
		File tempScrShotFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(tempScrShotFile, new File(screenShotfileName));
	}

	// This method creates a file name with the following format
	// ScreenShot/Date/time_classname_testname.png
	// ScreenShot is a folder
	// Date is a folder
	// time_classname_testname.png is a file
	// Date format is yyyyMMdd
	// time format is HHmmssSSS
	// className and methodName special characters ".][" are replaced with "_"
	protected String getScreenShotFileName(String className, String methodName) {
		DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateFormat1 = new SimpleDateFormat("HHmmssSSS");
		String now = dateFormat1.format(new Date());
		String today = dateFormat2.format(new Date());
		String fileName;
		System.out.println("Method Name:" + methodName);
		System.out.println("Class Name:" + className);
		if (methodName != null)
			fileName = className + "." + methodName;
		else
			fileName = className;
		fileName = "target/Screenshots/" + today + "/" + now + '_' + browserType + '_' 
				+ fileName.replaceAll("\\.|\\[|\\]", "_") + ".png";
		System.out.println("name:" + fileName);
		return fileName;
	}

	protected void waitForText(String text, By by,
			String timeoutMsg) throws Exception {
		for (int second = 0;; second++) {
			if (second >= 60)
				fail(timeoutMsg);
			try {
				if (text.equals(driver.findElement(by).getText()))
					break;
			} catch (Exception e) {
			}
			delay(1000);
		}
	}

	protected void delay(int seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void clickAt(WebDriver driver, By by, int xOffset, int yOffset) {
		WebElement element = driver.findElement(by);
		Actions builder = new Actions(driver);
		Action action = builder.moveToElement(element, 10, 10).click().build();
		action.perform();
	}
}
