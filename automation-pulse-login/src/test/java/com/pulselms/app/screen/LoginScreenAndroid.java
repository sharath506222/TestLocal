package com.pulselms.app.screen;

import org.openqa.selenium.WebElement;

import com.pulselms.app.framework.AppiumBase;
import com.pulselms.app.framework.ObjectLocators;
import com.pulselms.app.framework.Until;

import io.appium.java_client.android.AndroidDriver;

/**
 * Screen Class which represents the Login Screen
 * @author Cognizant
 *
 */

public class LoginScreenAndroid extends AppiumBase {

	private static String SignInHeader = ObjectLocators.getLocator("SignInHeader");
	private static String Email = ObjectLocators.getLocator("Email");
	private static String Password = ObjectLocators.getLocator("Password");
	private static String Submit = ObjectLocators.getLocator("Submit");
	private static String LoginError = ObjectLocators.getLocator("LoginError");
	private static String email = "ram@example.com";
	private static String password = "secret12";
	// private static String VersionNumber = ObjectLocators.getLocator("VersionNumber");

	/*
	 * public LoginScreen(AppiumDriver<?> driver) { super(driver); }
	 */

	public void login(String user, String pass) {

		type(Email, user);
		type(Password, pass);
		clickOnElement(Submit);

	}
	
	public void type(String elementLocator, String text) {
		
		log.info("Entering Text");
		WebElement element = wait(Until.elementToBeDisplayed(elementLocator));
		element.sendKeys(text);
		log.info("Entered " + text + " into the " + elementLocator + " text field");
		
	}
	
	public void openAndroidNotifications(){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			android.openNotifications();
			log.info("Opened the android notifications");
		} else{
			log.warn("openAndroidNotifications method is allowed only in android");
		}
	}
	
	public void lockScreen(int seconds){
		this.driver.lockScreen(seconds);
		log.info("Screen will be locked for " + seconds + " seconds");
	}
	
	public void waitForLoginScreenToLoad() {
		
		wait(Until.elementsToBeDisplayed(SignInHeader));
	}

	public void waitForLoginProcess() {

		wait(Until.elementToBeInvisible(Email));
	}
	
	public void waitForErrorMessage() {
		
		wait(Until.elementToBeDisplayed(LoginError));
	}

	public String getSignInHeaderText() {
		
		return getText(SignInHeader);
	}
	
	public boolean isSignInHeaderPresent() {

		if (isElementPresent(SignInHeader))
			return true;
		else
			return false;
	}
	
	public boolean isEmailTextPresent() {
		if (isElementPresent(email))
			return true;
		else
			return false;
	}

	public boolean isEmailTextFieldPresent() {

		if (isElementPresent(Email))
			return true;
		else
			return false;
	}

	public boolean isPasswordTextFieldPresent() {

		if (isElementPresent(Password))
			return true;
		else
			return false;
	}

	public boolean isSignInButtonPresent() {

		if (isElementPresent(Submit))
			return true;
		else
			return false;
	}

	/* public boolean isVersionNumberPresent() {

		if (isElementPresent(VersionNumber))
			return true;
		else
			return false;
	} */

	public String getLoginErrorMessage() {

		wait(Until.textToBePresentInElement(LoginError, "user name"));
		return getText(LoginError).trim();
	}

	public String getUsernameErrorMessage() {

		wait(Until.textToBePresentInElement(LoginError, "username"));
		return getText(LoginError).trim();
	}

	public String getPasswordErrorMessage() {

		wait(Until.textToBePresentInElement(LoginError, "password"));
		return getText(LoginError).trim();
	}

	public String getOfflineLoginErrorMessage() {

		wait(Until.textToBePresentInElement(LoginError, "Network"));
		return getText(LoginError).trim();
	}

}
