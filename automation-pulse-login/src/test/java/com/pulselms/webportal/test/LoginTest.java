package com.pulselms.webportal.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.pulselms.app.framework.PageFactory;
import com.pulselms.app.framework.WebBaseTest;
import com.pulselms.webportal.page.DashboardPage;
import com.pulselms.webportal.page.LoginPage;

public class LoginTest extends WebBaseTest{
	
	// valid Email & password
	@Test     
	public void pearsonPulseLoginTest() {
		
		LoginPage loginPage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginPage.loadPage();
		loginPage.login("cob@example.com", "secret12");
		DashboardPage dashboardPage=PageFactory.instantiatePage(driver, DashboardPage.class);
		dashboardPage.waitForDashboardPageToLoad();
		Assert.assertEquals(driver.getCurrentUrl(), "http://box01.noip.me:3000/#/dashboard");
	}
	
	// login with invalidEmail
	@Test                  
	public void pearsonPulseLoginwithInvalidEmailTest()
	{
		LoginPage loginpage = PageFactory.instantiatePage(driver, LoginPage.class);
		loginpage.loadPage();
		loginpage.login("cob1@example.com", "secret12");
		Assert.assertEquals(loginpage.getLoginErrorMsg(), "That email address/password is not recognised. Please try again.");
		
	}
	
	// login with invalidPassword
	@Test                      
	public void pearsonPulseLoginWithInvalidPasswordTest() {
		
		LoginPage loginPage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginPage.loadPage();
		loginPage.login("cob@example.com", "dasdsdfsfd");
		Assert.assertEquals(loginPage.getLoginErrorMsg(), "That email address/password is not recognised. Please try again.");
	}
	
	//login with Blankfields
	@Test                    
	public void pearsonPulseLoginwithBlankfields() 
	{
		LoginPage loginpage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginpage.loadPage();
		loginpage.login("", "");
		Assert.assertEquals(loginpage.getLoginBothBlankfieldError(), "Please enter your email address/user name.Please enter your password." );
	}
		
	//login with user blank
	@Test
	public void pearsonPulseLoginwithUserBlank()
	{
		LoginPage loginpage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginpage.loadPage();
		loginpage.login("","secret12");
		Assert.assertEquals(loginpage.getLoginUserfieldBlankError(), "Please enter your email address/user name.");
	}
	
	//login with password blank
	@Test
	public void pearsonPulseLoginwithPassBlank()
	{
		LoginPage loginpage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginpage.loadPage();
		loginpage.login("cob@example.com", "");
		Assert.assertEquals(loginpage.getLoginPassfieldBlankError(), "Please enter your password.");
	}
	
	//logout
	@Test
	public void logOutTest()
	{
		LoginPage loginpage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginpage.loadPage();
		loginpage.login("cob@example.com", "secret12");
		DashboardPage dashboardPage = PageFactory.instantiatePage(driver, DashboardPage.class);
		dashboardPage.waitForDashboardPageToLoad();
		dashboardPage.signOut();
		Assert.assertEquals(driver.getCurrentUrl(), "http://box01.noip.me:3000/#/");
	}
}
