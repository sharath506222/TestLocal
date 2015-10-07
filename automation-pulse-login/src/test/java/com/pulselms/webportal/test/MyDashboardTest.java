package com.pulselms.webportal.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.pulselms.app.framework.PageFactory;
import com.pulselms.app.framework.WebBaseTest;
import com.pulselms.webportal.page.DashboardPage;
import com.pulselms.webportal.page.LoginPage;

public class MyDashboardTest extends WebBaseTest {
	
	@Test
	public void myClassHeadingValidationTest() {
		
		LoginPage loginPage=PageFactory.instantiatePage(driver, LoginPage.class);
		loginPage.loadPage();
		loginPage.login("cob@example.com", "secret12");
		DashboardPage dashboardPage=PageFactory.instantiatePage(driver, DashboardPage.class);
		dashboardPage.waitForDashboardPageToLoad();
		Assert.assertEquals(dashboardPage.isMyClassHeadingPresent(), true);
	}

}
