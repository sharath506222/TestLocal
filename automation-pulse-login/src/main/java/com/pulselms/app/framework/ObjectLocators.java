package com.pulselms.app.framework;

import io.appium.java_client.MobileBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pulselms.app.framework.BrowserBotException;

/**
 * This class loads the properties files
 * 
 */
public class ObjectLocators {
	protected WebDriver driver;
	protected WebDriverWait wait;

	private static Properties propsMobile = new Properties();
	private static Properties propsWeb = new Properties();

	static {
		FileInputStream mobileStream = null;
		FileInputStream webStream = null;
		try {
			if (EnvParameters.PLATFORM.equalsIgnoreCase("Android")) {
				mobileStream = new FileInputStream(EnvParameters.TEST_ROOT_DIR
						+ File.separator + "ObjectRepo" + File.separator
						+ "Android_ObjectRepository.properties");
			} else if (EnvParameters.PLATFORM.equalsIgnoreCase("IOS")) {
				mobileStream = new FileInputStream(EnvParameters.TEST_ROOT_DIR
						+ File.separator + "ObjectRepo" + File.separator + "IOS_ObjectRepository.properties");
			} else {
				
				throw new BrowserBotException(
						"Not a valid environment for Mobile Testing");
			}
			if (EnvParameters.WEB_TEST.equalsIgnoreCase("true")) {
				webStream = new FileInputStream(EnvParameters.TEST_ROOT_DIR
						+ File.separator + "ObjectRepo" + File.separator + "WebPortal_ObjectRepository.properties");
			}
			propsMobile.load(mobileStream);
			propsWeb.load(webStream);
	
		} catch (IOException e) {
			propsMobile = null;
			propsWeb = null;
			// do nothing
			// System.err.println("Failed to read: ObjectRepository.properties");
		}
	}

	/**
	 * Retrieve the property value based on the property name
	 * 
	 * @param locatorName
	 * @return property value
	 * @throws IOException
	 */
	public static String getLocator(String locatorName) {
		if (propsMobile == null) {
			// System.err.println("Failed to read: ObjectRepository.properties");
			throw new BrowserBotException(
					"Failed to read: ObjectRepository.properties -> It is either not present or not readable");
		}
		String locvalue = propsMobile.getProperty(locatorName);
		System.out.println("Locator="+locvalue);
		return locvalue;
	}
	
	/**
	 * Retrieve the property value based on the property name
	 * 
	 * @param locatorName
	 * @return property value
	 * @throws IOException
	 */
	public static String getWebLocator(String locatorName) {
		if (propsWeb == null) {
			// System.err.println("Failed to read: ObjectRepository.properties");
			throw new BrowserBotException(
					"Failed to read: ObjectRepository.properties -> It is either not present or not readable");
		}
		String locvalue = propsWeb.getProperty(locatorName);
		return locvalue;
	}


	/**
	 * function to get the object locator by
	 * 
	 * @param propKey
	 *            element locator in the form locator;locator_value
	 * @return By
	 */
	public static By getBySelector(String propKey) {
		// get the value from selenium.properties and split the type and value
		String[] split = propKey.split(";");
		String type = split[0];

		// generate the By selector based on the type
		if (type.equalsIgnoreCase("id")) {
			return By.id(split[1]);
		} else if (type.equalsIgnoreCase("css")) {
			return By.cssSelector(split[1]);
		} else if (type.equalsIgnoreCase("tagname")) {
			return By.tagName(split[1]);
		} else if (type.equalsIgnoreCase("classname")
				|| type.equalsIgnoreCase("class")) {
			return By.className(split[1]);
		} else if (type.equalsIgnoreCase("name")) {
			return By.name(split[1]);
		} else if (type.equalsIgnoreCase("xpath")) {
			return By.xpath(split[1]);
		} else if (type.equalsIgnoreCase("link")) {
			return By.linkText(split[1]);
		} else if (type.equalsIgnoreCase("AccessibilityId")) {
			return MobileBy.AccessibilityId(split[1]);
		} else if (type.equalsIgnoreCase("android")) {
			return MobileBy.AndroidUIAutomator(split[1]);
		} else if (type.equalsIgnoreCase("ios")) {
			return MobileBy.IosUIAutomation(split[1]);
		} else {
			throw new BrowserBotException("Invalid element locator parameter -"
					+ propKey);
		}
	}
}
