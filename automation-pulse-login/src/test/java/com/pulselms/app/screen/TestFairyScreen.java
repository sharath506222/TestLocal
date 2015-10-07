package com.pulselms.app.screen;

import com.pulselms.app.framework.AppiumBase;
import com.pulselms.app.framework.ObjectLocators;
import com.pulselms.app.framework.Until;

/**
 * Screen Class which represents the Test Fairy Screen
 * @author Cognizant
 *
 */

public class TestFairyScreen extends AppiumBase {
	
	private static String TestFairy_PrivacyPolicy=ObjectLocators.getLocator("TestFairy_PrivacyPolicy");
	private static String PrivacyPolicy_YesButton=ObjectLocators.getLocator("PrivacyPolicy_YesButton");
		
	public void closeTestFairyPrivacyPolicy() {
		
		if (isElementPresent(TestFairy_PrivacyPolicy))
		{
			clickOnElement(PrivacyPolicy_YesButton);
			wait(Until.elementToBeInvisible(TestFairy_PrivacyPolicy));
		}
	}
	

}
