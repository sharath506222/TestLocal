package com.pulselms.app.framework;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.pulselms.app.framework.EnvParameters;
import com.pulselms.app.framework.LoggerUtil;
import com.pulselms.app.framework.Utils;
import com.pulselms.app.framework.EnvParameters.OSType;

public class WebBaseTest {

	protected WebDriver driver;

	/**
	 * Wrapper function for LoggerUtil.log method
	 * 
	 * @param message
	 * @param level
	 *            (String)
	 */
	protected void log(String message, String level) {
		LoggerUtil.log(message, level);
	}

	/**
	 * Wrapper function for LoggerUtil.log method
	 * 
	 * @param message
	 */
	protected void log(String message) {
		LoggerUtil.log(message);
	}

	@Parameters({ "Browser" })
	@BeforeMethod(alwaysRun = true)
	public void testSetup(@Optional String browser) throws Exception {

		if (StringUtils.isEmpty(browser)) {
			browser = EnvParameters.WEB_BROWSER_TYPE;
		}
		if (browser.equals("chrome")) {
			try {
				setupChromeDriver();
			} catch (Exception e) {
				LoggerUtil.log(e.getMessage(), Level.DEBUG);
			}
		}
		if (browser.equals("iexplore")) {
			try {
				setupIEDriver();
			} catch (Exception e) {
				LoggerUtil.log(e.getMessage(), Level.DEBUG);
			}
		}

		driver = buildWebDriver(browser);

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		if (driver instanceof OperaDriver || driver instanceof AndroidDriver
				|| driver instanceof IOSDriver) {
			// do nothing
		} else {
			driver.manage().window().maximize();
		}

	}

	public WebDriver buildWebDriver(String browser) {

		if (browser.equals("firefox")) {
			if (EnvParameters.FIREFOX_BIN == null) {
				throw new SkipException("firefox binary is not set");
			}
			System.setProperty("webdriver.firefox.bin",
					EnvParameters.FIREFOX_BIN);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setAcceptUntrustedCertificates(true);
			firefoxProfile.setAssumeUntrustedCertificateIssuer(true);
			driver = new FirefoxDriver(firefoxProfile);
		} else if (browser.equals("iexplore")) {
			DesiredCapabilities ieCapabilities = DesiredCapabilities
					.internetExplorer();
			ieCapabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			driver = new InternetExplorerDriver(ieCapabilities);
		} else if (browser.equals("chrome")) {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability("chrome.switches",
					Arrays.asList("--ignore-certificate-errors"));
			driver = new ChromeDriver(capabilities);
		} else if (browser.equals("*safari")) {
			DesiredCapabilities capabilities = DesiredCapabilities.safari();
			SafariOptions opt = new SafariOptions();
			opt.setUseCleanSession(false);
			capabilities.setCapability(SafariOptions.CAPABILITY, opt);
			return new SafariDriver(capabilities);
		} else if (browser.equals("*opera")) {
			return new OperaDriver();
		} else {
			throw new SkipException("Browser " + browser
					+ " not supported by this framework");
		}
		return driver;

	}

	/**
	 * Runs after every test method to quit the driver instance
	 * 
	 * @param _result
	 */

	@AfterMethod(alwaysRun = true)
	public void postTestCase(ITestResult _result) {
		if (driver != null) {
			driver.quit();
		}
		System.out.println("Test Status is " + _result.getStatus());
	}

	/**
	 * Runs after every test class to kill the Chrome/IE drivers, if not
	 * properly exited
	 * 
	 * @throws Exception
	 */
	@AfterSuite(alwaysRun = true)
	public void tearDown() throws Exception {
		killIEDriver();
		killChromeDriver();

	}

	/**
	 * Kills the running process IEDriverServer.exe
	 */
	private static void killIEDriver() {
		String _processName = "IEDriverServer.exe";
		if (Utils.isProcessRuning(_processName) == true)
			Utils.killProcess(_processName);
	}

	/**
	 * Kills the running process chromedriver.exe
	 */
	private static void killChromeDriver() {
		String _processName = "chromedriver.exe";
		if (Utils.isProcessRuning(_processName) == true)
			Utils.killProcess(_processName);
	}

	/**
	 * Method to setup IE driver based on the OS type
	 * 
	 * @throws Exception
	 */
	private static void setupIEDriver() throws Exception {
		String ieProperty = "webdriver.ie.driver";

		File targetIEdriver32 = null;

		// dont need it for other OS because IE is not available
		if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {
			targetIEdriver32 = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "ie"
					+ File.separator + "win32" + File.separator
					+ "IEDriverServer.exe");
		}

		if (targetIEdriver32.exists()) {
			System.setProperty(ieProperty, targetIEdriver32.getAbsolutePath());
			return;
		} else {
			InputStream reader = null;
			if (com.pulselms.app.framework.EnvParameters.getOSname() == EnvParameters.OSType.windows) {
				reader = WebBaseTest.class
						.getResourceAsStream("/drivers/ie/win32/IEDriverServer.exe");
			}

			if (reader.available() > 0) {
				new File(targetIEdriver32.getParent()).mkdirs();
				FileOutputStream writer = new FileOutputStream(targetIEdriver32);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = reader.read(buffer)) > 0) {
					writer.write(buffer, 0, length);
				}
				/*
				 * int ch = reader.read(); while (ch != -1) { writer.write(ch);
				 * ch = reader.read(); }
				 */
				writer.close();
				reader.close();
				targetIEdriver32.setExecutable(true, false);
				System.setProperty(ieProperty,
						targetIEdriver32.getAbsolutePath());
			} else
				LoggerUtil.log(" IEDriverServer.exe is not found in the jar");
		}
	}

	/**
	 * Method to setup Chrome Driver based on the OS type
	 * 
	 * @throws Exception
	 */
	private static void setupChromeDriver() throws Exception {

		String ChromProp = "webdriver.chrome.driver";
		new EnvParameters();
		File targetChromedriver = null;
		if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {
			targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "chrome"
					+ File.separator + "win" + File.separator
					+ "chromedriver.exe");
		}

		else if (EnvParameters.getOSname() == OSType.mac) {
			targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "chrome"
					+ File.separator + "mac" + File.separator + "chromedriver");
		} else if (EnvParameters.getOSname() == OSType.linux) {
			targetChromedriver = new File(EnvParameters.TEST_ROOT_DIR
					+ File.separator + "drivers" + File.separator + "chrome"
					+ File.separator + "linux" + File.separator
					+ "chromedriver");
		}

		if (targetChromedriver.exists()) {

			System.setProperty(ChromProp, targetChromedriver.getAbsolutePath());

			return;
		} else {
			InputStream reader = null;
			if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {
				reader = Utils.class
						.getResourceAsStream("/drivers/chrome/win/chromedriver.exe");
			} else if (EnvParameters.getOSname() == EnvParameters.OSType.mac) {
				reader = Utils.class
						.getResourceAsStream("/drivers/chrome/mac/chromedriver");
			} else if (EnvParameters.getOSname() == EnvParameters.OSType.linux) {
				reader = Utils.class
						.getResourceAsStream("/drivers/chrome/linux/chromedriver");
				LoggerUtil.log("Copying the driver is successfull");
			} else {
				LoggerUtil.log("The chrome driver copying is not successfull");
			}

			if (reader.available() > 0) {
				new File(targetChromedriver.getParent()).mkdirs();
				FileOutputStream writer = new FileOutputStream(
						targetChromedriver);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = reader.read(buffer)) > 0) {
					writer.write(buffer, 0, length);
				}
				/*
				 * int ch = reader.read(); while (ch != -1) { writer.write(ch);
				 * ch = reader.read(); }
				 */
				writer.close();
				reader.close();
				targetChromedriver.setExecutable(true, false);
				System.setProperty(ChromProp,
						targetChromedriver.getAbsolutePath());
			} else {
				LoggerUtil.log("Cannot find chromedriver in the jar");
			}
		}
	}

}
