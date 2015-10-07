package com.pulselms.app.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.pulselms.app.framework.BrowserBotException;

/**
 * EnvParameters class will be loading all the environment related parameters
 * @author Cognizant
 *
 */
public class EnvParameters {

	// General Config
	private static final String PROP_FILE = "config.properties";

	public static final String PLATFORM;
	public static final String VERSION;
	public static final String DEVICE;
	public static final String ENV;
	public static final String APPIUM_SERVER;
	public static final String TEST_ROOT_DIR;
	
	public static final String WEB_TEST;
	public static final String WEB_BROWSER_TYPE;
	public static final String FIREFOX_BIN;
	
	private static Properties properties = new Properties();

	/**
	 * enumeration for Operating System Type
	 *
	 */
	public enum OSType {
		windows,
		mac,
		linux,
		windows64
	}

	static{
		// Loading General Configurations
		TEST_ROOT_DIR = System.getProperty("user.dir");
		FileInputStream in = null;
		try{
				in = new FileInputStream(TEST_ROOT_DIR + File.separator	+ PROP_FILE);
			}catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new BrowserBotException(PROP_FILE + " -> Config file not found, Please specify the correct config file");
			}
		
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BrowserBotException("Failure loading property file -> " + e.getMessage());
		}	

		// Load PLATFORM from Platform property
		if (System.getProperty("Platform")!= null && !(System.getProperty("Platform").equalsIgnoreCase(""))){
			PLATFORM = System.getProperty("Platform");
		}else if (properties.getProperty("Platform") != null && !(properties.getProperty("Platform").equalsIgnoreCase(""))){
			PLATFORM = properties.getProperty("Platform");
		}else{
			throw new BrowserBotException("Platform property not set");
		}

		// Load VERSION from Version property
		if (System.getProperty("Version")!= null && !(System.getProperty("Version").equalsIgnoreCase(""))){
			VERSION = System.getProperty("Version");
		}else if (properties.getProperty("browser.version") != null && !(properties.getProperty("Version").equalsIgnoreCase(""))){
			VERSION = properties.getProperty("Version");
		}else{
			VERSION = "";
		}

		// Load DEVICE from DeviceName property
		if (System.getProperty("DeviceName")!= null && !(System.getProperty("DeviceName").equalsIgnoreCase(""))){
			DEVICE = System.getProperty("DeviceName");
		}else if (properties.getProperty("DeviceName") != null && !(properties.getProperty("DeviceName").equalsIgnoreCase(""))){
			DEVICE = properties.getProperty("DeviceName");
		}else{
			DEVICE = "";
		}

		// Load ENV from Env property
		
		if (System.getProperty("Env")!= null && !(System.getProperty("Env").equalsIgnoreCase(""))){
				ENV = System.getProperty("Env");
			}else if (properties.getProperty("Env") != null && !(properties.getProperty("Env").equalsIgnoreCase(""))){
				ENV = properties.getProperty("Env");
			}
			else{
				throw new BrowserBotException("Environment property not set, "
						+ "it is mandatory to set the Environment");
			}
		

		// Load APPIUM_SERVER from AppiumServer property
		if (System.getProperty("AppiumServer")!= null && !(System.getProperty("AppiumServer").equals(""))){
			APPIUM_SERVER = System.getProperty("AppiumServer");
		}else if (properties.getProperty("AppiumServer") != null && !(properties.getProperty("AppiumServer").equals(""))){
			APPIUM_SERVER = properties.getProperty("AppiumServer");
		}
		else{
			throw new BrowserBotException("Appium Server property not set, "
					+ "it is mandatory to set the Appium Server");
		}
		
		// Load WEB_BROWSER from web.browser property
		if (System.getProperty("WebTest")!= null && !(System.getProperty("WebTest").equalsIgnoreCase(""))){
			WEB_TEST = System.getProperty("WebTest");
		}else if (properties.getProperty("WebTest") != null && !(properties.getProperty("WebTest").equalsIgnoreCase(""))){
			WEB_TEST = properties.getProperty("WebTest");
		}else{
			throw new BrowserBotException("WebTest property not set");
		}
		
		// Load WEB_BROWSER from web.browser property
				if (System.getProperty("Browser")!= null && !(System.getProperty("Browser").equalsIgnoreCase(""))){
					WEB_BROWSER_TYPE = System.getProperty("Browser");
				}else if (properties.getProperty("Browser") != null && !(properties.getProperty("Browser").equalsIgnoreCase(""))){
					WEB_BROWSER_TYPE = properties.getProperty("Browser");
				}else{
					throw new BrowserBotException("Browser property not set");
				}
		
		// Load FIREFOX_BIN from firefox.bin property
				if (WEB_BROWSER_TYPE.equalsIgnoreCase("firefox")){
					if (System.getProperty("firefox.bin")!= null && !(System.getProperty("firefox.bin").equalsIgnoreCase(""))){
						FIREFOX_BIN = System.getProperty("firefox.bin");
					}else if (properties.getProperty("Browser") != null && !(properties.getProperty("Browser").equalsIgnoreCase(""))){
						FIREFOX_BIN = properties.getProperty("firefox.bin");
					}
					else{
						throw new BrowserBotException("firefox.bin property not set, "
								+ "it is mandate for tests to run on firefox browser");
					}
				}else{
					if (System.getProperty("firefox.bin")!= null && !(System.getProperty("firefox.bin").equalsIgnoreCase(""))){
						FIREFOX_BIN = System.getProperty("firefox.bin");
					}else if (properties.getProperty("Browser") != null && !(properties.getProperty("Browser").equalsIgnoreCase(""))){
						FIREFOX_BIN = properties.getProperty("firefox.bin");
					}else{
						FIREFOX_BIN = null;
					}
				}


	}
	
	/**
	 * Gets the Operating System from system property
	 * 
	 * @return OSType enum
	 */
	public static OSType getOSname() {
		String osType = System.getProperty("os.name");
		// Reporter.log("System properties os.name is : "+osType, true);
		return getOSname(osType);
	}

	/**
	 * Gets the Operation system enumeration type from string
	 * an overloaded method
	 * 
	 * @param osType
	 * @return OSType enum
	 */
	public static OSType getOSname(String osType) {
		if (osType.toLowerCase().contains("win"))
			if (System.getenv("PROCESSOR_ARCHITECTURE").contains("86") && System.getenv("PROCESSOR_ARCHITEW6432") != null){
				return OSType.windows;
			}else{
				return OSType.windows;
			}
		else if (osType.toLowerCase().contains("win") && System.getProperty("os.arch").equalsIgnoreCase("x64"))
			return OSType.windows64;
		else if (osType.toLowerCase().contains("mac"))
			return OSType.mac;
		else if (osType.toLowerCase().contains("linux"))
			return OSType.linux;
		else
			return OSType.windows; // default to window
	}
	

	/**
	 * Description : this method will get the value of a property from the
	 * EnvParameters.properties
	 * 
	 * @param propertyname
	 *            [String]
	 * @return Property value [String]
	 * @Usage getPropertyValue ("web.browser") will return the
	 *             browsername
	 */
	public static String getPropertyValue(String propertyname) {
		return properties.getProperty(propertyname);
	}
}