package com.pulselms.webportal.page;

import com.pulselms.app.framework.ObjectLocators;
import com.pulselms.app.framework.Until;
import com.pulselms.app.framework.WebDriverBase;

public class DashboardPage extends WebDriverBase {
	
	private static String MyDashboard = ObjectLocators.getWebLocator("MyDashboard");
	private static String MyClass = ObjectLocators.getWebLocator("MyClass");
	private static String Mycontent=ObjectLocators.getWebLocator("");
	private static String Mypage=ObjectLocators.getWebLocator("");
	private static String ClickName= ObjectLocators.getWebLocator("ClickName");
	private static String Signout=ObjectLocators.getWebLocator("Signout");

	
	public void waitForDashboardPageToLoad() {
		
		wait(Until.elementToBePresent(MyDashboard));
		
	}
	
	public boolean isMyClassHeadingPresent() {
		
		return isElementPresent(MyClass);
	}
	
	public boolean isMyContentHeadingPresent()
	{
		return isElementPresent(Mycontent);
	}
	
	public boolean isMyPagePresent()
	{
		return isElementPresent(Mypage);
	}
	
	
	public void signOut()
	{
		clickOnElement(ClickName);
		clickOnElement(Signout);
//		wait(Until.elementToBeInvisible(Signout));
	}
	
}
