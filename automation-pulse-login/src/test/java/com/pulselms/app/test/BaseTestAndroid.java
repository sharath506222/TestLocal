package com.pulselms.app.test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.pulselms.app.framework.BrowserBotException;
import com.pulselms.app.framework.EnvParameters;
import com.pulselms.app.framework.PageFactory;
import com.pulselms.app.screen.TestFairyScreen;

/**
 * This is Base Test for all Mobile Test classes that has pre and post 
 * test configuration for Appium
 * @author Cognizant
 *
 */
public class BaseTestAndroid {

	protected static AppiumDriver<?> driver;
	private static String AppPackage;
	private static String AppActivity;
	private static String AppWaitActivity;
	protected static final Logger log = Logger.getLogger(BaseTestAndroid.class);

	@BeforeClass
	public void setUp() {
		selectAppPackageDetails();
	}

	@BeforeMethod
	public void setUpMethod() throws MalformedURLException {

		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("VERSION", EnvParameters.VERSION);
		cap.setCapability("deviceName", EnvParameters.DEVICE);
		cap.setCapability("platformName", EnvParameters.PLATFORM);
		cap.setCapability("noReset", "false");
		cap.setCapability("appPackage", AppPackage);
		cap.setCapability("appActivity", AppActivity);
		cap.setCapability("appWaitActivity", AppWaitActivity);
		File appDir = new File("apk");
		File app = new File(appDir, "app-dev-release_1.1.apk");
		cap.setCapability("app", app.getAbsolutePath());
		if (EnvParameters.PLATFORM.equals("Android")) {
			driver = new AndroidDriver<WebElement>(new URL(
					EnvParameters.APPIUM_SERVER), cap);
		} else if (EnvParameters.PLATFORM.equals("IOS")) {
			driver = new IOSDriver<WebElement>(new URL(
					EnvParameters.APPIUM_SERVER), cap);
		}
		else
			throw new BrowserBotException("Not a valid Platform");
		setWifiOn();
		setDeviceInPortrait();
	}
	
	@AfterMethod
	public void tearDown() {
		
		driver.closeApp();
	    driver.quit();
	}

	private void selectAppPackageDetails() {

		if (EnvParameters.ENV.equals("PROD")) {
			AppPackage = "com.pearson.android.readerplus";
			AppActivity = "com.testfairy.sdk.activities.WelcomeActivity";
			AppWaitActivity = "com.testfairy.sdk.activities.WelcomeActivity";

		} else if (EnvParameters.ENV.equals("DEV")) {
			AppPackage = "com.pearson.android.pulse.dev";
			AppActivity = "com.testfairy.sdk.activities.WelcomeActivity";
			AppWaitActivity = "com.testfairy.sdk.activities.WelcomeActivity";
		} else if (EnvParameters.ENV.equals("UAT")) {
			AppPackage = "com.pearson.android.readerplus.uat";
			AppActivity = "com.testfairy.sdk.activities.WelcomeActivity";
			AppWaitActivity = "com.testfairy.sdk.activities.WelcomeActivity";
		} else
			throw new BrowserBotException("Not a valid Environment");
	}
	
	public void handleTestFairySecutiryPopup() {
		
		log.info("Checking for TestFairy Security Popup");
		TestFairyScreen testFairyScreen=PageFactory.instantiatePage(driver, TestFairyScreen.class);
		testFairyScreen.closeTestFairyPrivacyPolicy();
		
	}
	
	public void setDeviceInPortrait(){
		this.driver.rotate(ScreenOrientation.PORTRAIT);
		log.info("Set device screen in PORTRAINT mode");
	}

	
	public void setWifiOn() {
		
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			if(!(setting.wifiEnabled()))
			{
				setting.setWifi(true);
				android.setNetworkConnection(setting);
			}
				
		}
		
	}
	
}
