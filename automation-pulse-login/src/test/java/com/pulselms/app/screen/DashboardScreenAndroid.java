package com.pulselms.app.screen;

import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.pulselms.app.framework.AppiumBase;
import com.pulselms.app.framework.ObjectLocators;
import com.pulselms.app.framework.Until;

/**
 * Screen Class which represents the DashBoard Screen
 * 
 * @author Cognizant
 * 
 */

public class DashboardScreenAndroid extends AppiumBase {
	private static String DashboardHeader = ObjectLocators
			.getLocator("DashboardHeader");
	private static String SignOut = ObjectLocators.getLocator("SignOut");
	
	
	public boolean isDashBoardHeaderPresent() {

		if (isElementPresent(DashboardHeader))
			return true;
		else
			return false;
	}
	
	public void signOut() {

		clickOnElement(SignOut);
		wait(Until.elementToBeInvisible(SignOut));
	}
	
	public boolean isSignOutNotPresent() {

		if (isElementChecked(SignOut))
			return false;
		else
			return true;
	}
	
}
