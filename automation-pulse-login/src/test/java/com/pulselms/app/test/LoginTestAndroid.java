package com.pulselms.app.test;


import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.pulselms.app.screen.DashboardScreenAndroid;
import com.pulselms.app.screen.LoginScreenAndroid;
import com.pulselms.app.framework.ObjectLocators;
import com.pulselms.app.framework.PageFactory;
import com.pulselms.app.framework.WebDriverBase;


/**
 * Login Screen Test class which has test scenarios for Login module
 * 
 * @author Cognizant
 *
 */

public class LoginTestAndroid extends BaseTestAndroid {
	
	private static String email = "ram@example.com";
	private static String password = "secret12";
	private static String Email = ObjectLocators.getLocator("Email");
	private static String Password = ObjectLocators.getLocator("Password");
	
	
	/**
	 * Test script to verify successful sign-in with valid email and valid password
	 */
	@Test
	public void TC001_SigninVerificationOfSuccessfulLogin() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.login(email, password);
		loginScreenAndroid.waitForLoginProcess();
		DashboardScreenAndroid dashboardScreen = PageFactory.instantiatePage(driver, DashboardScreenAndroid.class);
		Assert.assertEquals(dashboardScreen.isDashBoardHeaderPresent(), true);
		dashboardScreen.signOut();
		Assert.assertEquals(dashboardScreen.isSignOutNotPresent(), true);
	}
	

	/**
	 * Test script to verify sign-in error message with invalid email-id and valid password
	 */
	@Test
	public void TC003_SigninVerificationOfErrorMessageForInvalidEmailId() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.login("invalid@invalid.com","secret12");
		loginScreenAndroid.waitForErrorMessage();
		Assert.assertEquals(loginScreenAndroid.getLoginErrorMessage(), "That email/user name is not recognised. Please try again.");
	}
	
	/**
	 * Test script to verify sign-in error message with valid email-id and invalid password
	 */
	@Test
	public void TC004_SigninVerificationOfErrorMessageForInvalidPassword() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.login("ram@example.com","invalid12");
		loginScreenAndroid.waitForErrorMessage();
		Assert.assertEquals(loginScreenAndroid.getLoginErrorMessage(), "That email/user name is not recognised. Please try again.");
	}
	
	/**
	 * Test script to verify sign-in error message with both email-id and password as blanks
	 */
	@Test
	public void TC006_SigninVerificationOfErrorMessageForBothFieldsBlank() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.login("","");
		loginScreenAndroid.waitForErrorMessage();
		Assert.assertEquals(loginScreenAndroid.getLoginErrorMessage(), "Please enter your email address/user name. Please enter your password.");
	}
	
	/**
	 * Test script to verify sign-in error message with email-id field as blank
	 */
	@Test
	public void TC007_SigninVerificationOfErrorMessageIfUserNameIsBlank() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.login("","secret12");
		loginScreenAndroid.waitForErrorMessage();
		Assert.assertEquals(loginScreenAndroid.getLoginErrorMessage(), "Please enter your email address/user name.");
	}
	
	/**
	 * Test script to verify sign-in error message with password field as blank
	 */
	@Test
	public void TC008_SigninVerificationOfErrorMessageIfPasswordIsBlank() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.login("ram@example.com","");
		loginScreenAndroid.waitForErrorMessage();
		Assert.assertEquals(loginScreenAndroid.getPasswordErrorMessage(), "Please enter your password.");
	}
	
	/**
	 * Test script to verify sign-in page behavior after orientation change
	 */
	@Test
	public void TC016_SigninVerfiyingBehaviorOfApplicationAfterOrientationChange() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.type(Email, "ram@example.com");
		loginScreenAndroid.type(Password, "secret12");
		loginScreenAndroid.rotateDeviceToLandscape();
		Assert.assertEquals(loginScreenAndroid.isSignInHeaderPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isEmailTextFieldPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isPasswordTextFieldPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isSignInButtonPresent(), true);
		loginScreenAndroid.rotateDeviceToPortrait();
		Assert.assertEquals(loginScreenAndroid.isSignInHeaderPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isEmailTextFieldPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isPasswordTextFieldPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isSignInButtonPresent(), true);
		
	}
	
	/**
	 * Test script to verify sign-in page behavior after device is locked and unlocked.
	 */
	@Test
	public void TC017_SigninVerfiyingApplicationAfterDeviceIsLocked() {
		handleTestFairySecutiryPopup();
		LoginScreenAndroid loginScreenAndroid = PageFactory.instantiatePage(driver, LoginScreenAndroid.class);
		loginScreenAndroid.waitForLoginScreenToLoad();
		loginScreenAndroid.type(Email, email);
		loginScreenAndroid.type(Password, password);
		loginScreenAndroid.lockScreen(5);
		loginScreenAndroid.unlockScreen();
		Assert.assertEquals(loginScreenAndroid.isSignInHeaderPresent(), true);
		Assert.assertEquals(loginScreenAndroid.isEmailTextPresent(), true);
		
	}
	
}
