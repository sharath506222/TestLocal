package com.pulselms.webportal.page;

import com.pulselms.app.framework.ObjectLocators;
import com.pulselms.app.framework.Until;
import com.pulselms.app.framework.WebDriverBase;

public class LoginPage extends WebDriverBase {
	
	private static String EmailAddress = ObjectLocators.getWebLocator("EmailAddress");
	private static String Password= ObjectLocators.getWebLocator("Password");
	private static String LoginButton= ObjectLocators.getWebLocator("LoginButton");
	private static String LoginError= ObjectLocators.getWebLocator("LoginError");
	private static String LoginErrorbothBlankfield= ObjectLocators.getWebLocator("LoginErrorBlankfield");
	private static String LoginErrorUserBlank=ObjectLocators.getWebLocator("LoginErrorUserBlank");
	private static String LoginErrorPassBlank=ObjectLocators.getWebLocator("LoginErrorPassBlank");

	
	
	public void loadPage() {
		
		goToUrl("http://box01.noip.me:3000/#/");
		wait(Until.elementToBePresent(EmailAddress));
		wait(Until.elementsToBePresent(Password));
		wait(Until.elementsToBeDisplayed(LoginButton));
		
	}
	
	public void login(String User, String Pass) {
		
		type(EmailAddress,User);
		type(Password,Pass);
		clickOnElement(LoginButton);
		Until.elementToBeInvisible(EmailAddress);
	}
	
	public String getLoginErrorMsg() {
		
		wait(Until.textToBePresentInElement(LoginError, "email"));
		return getText(LoginError).trim();
	}
	
	public String getLoginBothBlankfieldError() 
	{
		wait(Until.textToBePresentInElement(LoginErrorbothBlankfield, "email"));
		return getText(LoginErrorbothBlankfield).trim();
	}
	
	public String getLoginUserfieldBlankError()
	{
		wait(Until.textToBePresentInElement(LoginErrorUserBlank, "email"));
		return getText(LoginErrorUserBlank).trim();
	}
	
	public String getLoginPassfieldBlankError()
	{
		wait(Until.textToBePresentInElement(LoginErrorPassBlank, "password"));
		return getText(LoginErrorPassBlank).trim();
	}
	
	

}
