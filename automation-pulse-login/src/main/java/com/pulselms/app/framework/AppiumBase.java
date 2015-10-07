package com.pulselms.app.framework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;



/**
 * @author Cognizant
 * 
 * A class representing a bot for Appium Library. Bot is an automation agent that executes commands
 * on the calling functions behalf. This method is advantageous while working on
 * a project with multiple developers, This brings in discipline while coding
 * 
 */
public class AppiumBase extends WebDriverBase {



	protected AppiumDriver<?> driver;
	// Methods specific to AppiumDriver
	
	/*	public AppiumBase(AppiumDriver<?> appdriver) {
	super((WebDriver) appdriver);
	this.driver = appdriver;
	// TODO Auto-generated constructor stub
} */
	
	public void setDriver(AndroidDriver<?> appdriver){
		super.driver = (WebDriver)appdriver;
		this.driver = appdriver;
	}
	
		
	/**
	 * Close the app which was provided in the capabilities at session creation
	 */
	protected void closeApp(){
		this.driver.closeApp();
		log.info("closed the app specified at session creation");
	}

	/**
	 * Reset the currently running app for this session
	 */
	public void resetApp(){
		this.driver.resetApp();
		log.info("Reset the currently running app for this session");
	}

	/**
	 * Get all defined Strings from an Android app for the default language
	 * 
	 * @return a string of all the localized strings defined in the app
	 */
	protected String getAppStrings(){
		return this.driver.getAppStrings();
	}

	/**
	 * Get all defined Strings from an Android app for the specified language
	 * 
	 * @param language - strings language code
	 * @return a string of all the localized strings defined in the app
	 */
	protected String getAppStrings(String language){
		return this.driver.getAppStrings(language);
	}

	/**
	 * Send a key event to the device
	 * Refer key constants defined in AndroidKeyCode & IOSKeyCode
	 * 
	 * @param key
	 */
	public void sendKeyEvent(int key){
		log.info("Sending key event : " + key);
		((AndroidDeviceActionShortcuts) this.driver).pressKeyCode(key);
	}

	/**
	 * Send a key event to the device
	 * Refer key constants defined in AndroidKeyCode & IOSKeyCode
	 * Metastate constants defined in AndroidKeyMetastate
	 * 
	 * @param key
	 * @param metastate
	 */
	protected void sendKeyEvent(int key, Integer metastate){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			android.pressKeyCode(key,metastate);
			log.info("Sending key event : " + key + " with the metastate " + metastate);
		}else{
			log.warn("sendKeyEvent is not supported in IOS");
		}
	}

	/**
	 * Lock the device (bring it to the lock screen) for a given number of seconds
	 * 
	 * @param seconds - number of seconds to lock the screen for
	 */
	protected void lockScreen(int seconds){
		this.driver.lockScreen(seconds);
		log.info("Screen will be locked for " + seconds + " seconds");
	}

	/**
	 * Unlock the device using adb shell command
	 * 
	 */	
	public void unlockScreen() {
		try {
			Runtime.getRuntime().exec("adb shell input keyevent 82");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
/*	public void unlockScreen()  {
		
		String cmd = "adb shell am start -n io.appium.unlock/.Unlock";
	    String cmdreturn = "";
	    Process pr = null;
	    Runtime run = Runtime.getRuntime();
	    try {
	    pr = run.exec(cmd);
	    pr.waitFor();
	    } catch(IOException e) {
	    	// to-do
	    } catch(InterruptedException e) {
	    	//to-do
	    }
	    BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	    try {
	    while ((cmdreturn=buf.readLine())!=null) {
	        //to-do
	    }
	    }catch(IOException e){
	    	
	    }
	} */

	/**
	 * Launch the app which was provided in the capabilities at session creation
	 */
	public void launchApp(){
		this.driver.launchApp();
		log.info("Launched the app which was provided in the capabilities at session creation");
	}
	
	

	/**
	 * rotates the screen to Landscape
	 */
	public void rotateDeviceToLandscape(){
		try {
		this.driver.rotate(ScreenOrientation.LANDSCAPE);
		} catch (WebDriverException e)
		{
			//to-do
		}
	
		log.info("Rotated screen to LANDSCAPE");
	}
	
	public void pressHomeButton() {
		
		sendKeyEvent(AndroidKeyCode.HOME);
		
	}
	
	public void pressBackButton() {
		sendKeyEvent(AndroidKeyCode.BACK);
	}

	/**
	 * rotates the screen to portrait
	 */
	public void rotateDeviceToPortrait(){
		this.driver.rotate(ScreenOrientation.PORTRAIT);
		log.info("Rotated screen to LANDSCAPE");
	}

	/**
	 * @return the current orientation of the device
	 */
	protected ScreenOrientation getDeviceOrientation(){
		ScreenOrientation position = this.driver.getOrientation();
		log.info("Screen is in " + position.toString());
		return position;
	}

	/**
	 * Method for tapping the center of an element on the screen
	 *  
	 * @param elementlocator 
	 * 			  the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @param duration in milli seconds
	 */
	protected void tapElement(String elementlocator,int duration){
		WebElement elem = wait(Until.elementToBeClickable(elementlocator));
		this.driver.tap(1, elem, duration);
		log.info("tapped at element: " + elementlocator);
	}

	/**
	 * Method for tapping the center of an element on the screen for 500 milli seconds
	 *  
	 * @param elementlocator 
	 * 			  the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 */
	protected void tapElement(String elementlocator){
		WebElement elem = wait(Until.elementToBeClickable(elementlocator));
		this.driver.tap(1, elem, 500);
		log.info("tapped at element: " + elementlocator);
	}

	/**
	 * method for tapping a position on the screen for 500 milli seconds
	 *  
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	protected void tapPostion(int x, int y){
		this.driver.tap(1, x, y, 500);
		log.info("tapped at x=" + x + " ; y="+y);
	}

	/**
	 * method for tapping a position on the screen for specified duration
	 *  
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param duration in milli seconds
	 */
	protected void tapPostion(int x, int y,int duration){
		this.driver.tap(1, x, y, 500);
		log.info("tapped at x=" + x + " ; y="+y);
	}

	
	/**
	 * Convenience method for swiping across the screen
	 * 
	 * @param startx - starting x coordinate
	 * @param starty - starting y coordinate
	 * @param endx - ending x coordinate
	 * @param endy - ending y coordinate
	 * @param duration - amount of time in milliseconds for the entire swipe action to take
	 */
	protected void swipeAtPosition(int startx, int starty, int endx, int endy, int duration){
		this.driver.swipe(startx, starty, endx, endy, duration);
		log.info("swiped from ("+ startx +"," +starty+") to ("+endx+","+endy+") in " + duration + " ms");
	}
	
	public void swipeElement(String elementLocator) {
		  
		WebElement el = driver.findElement(ObjectLocators.getBySelector(elementLocator));
		 String orientation = driver.getOrientation().value();

		  // get the X coordinate of the upper left corner of the element, then add the element's width to get the rightmost X value of the element
		  int leftX = el.getLocation().getX();
		  int rightX = leftX + el.getSize().getWidth();

		  // get the Y coordinate of the upper left corner of the element, then subtract the height to get the lowest Y value of the element
		  int upperY = el.getLocation().getY();
		  int lowerY = upperY - el.getSize().getHeight();
		  int middleY = (upperY - lowerY) / 2;

		  if (orientation.equals("portrait")) {
		    // Swipe from just inside the left-middle to just inside the right-middle of the element over 500ms
		      driver.swipe(leftX + 5, middleY, rightX - 5, middleY, 500);
		  }
		  else if (orientation.equals("landscape")) {
		    // Swipe from just inside the right-middle to just inside the left-middle of the element over 500ms
		    driver.swipe(rightX - 5, middleY, leftX + 5, middleY, 500);
		  }
		}


	/**
	 * Convenience method for "zooming in" on an element on the screen. 
	 * "zooming in" refers to the action of two appendages pressing the screen 
	 * and sliding away from each other. NOTE: This convenience method slides touches
	 * away from the element, if this would happen to place one of them off the screen, 
	 * appium will return an outOfBounds error. 
	 * In this case, revert to using the MultiTouchAction api instead of this method.
	 * 
	 * @param elementlocator
	 * 				the key associated to Locator(LocatorType;LocatorValue) that
	 *            	specifies the selector
	 */
	protected void zoomElement(String elementlocator){
		WebElement el = this.driver.findElement(ObjectLocators.getBySelector(elementlocator));
		this.driver.zoom(el);
		log.info("Zoomed at element:" + elementlocator);
	}

	/**
	 * Convenience method for "zooming in" on an element on the screen. 
	 * "zooming in" refers to the action of two appendages pressing the screen 
	 * and sliding away from each other. NOTE: This convenience method slides touches
	 * away from the element, if this would happen to place one of them off the screen, 
	 * appium will return an outOfBounds error. 
	 * In this case, revert to using the MultiTouchAction api instead of this method.
	 * 
	 * @param x - x coordinate to start zoom on
	 * @param y - y coordinate to start zoom on
	 */
	protected void zoomAtPoint(int x,int y){
		this.driver.zoom(x,y);
		log.info("Zoomed at point x:" + x +", y:" + y);
	}

	/**
	 * Set the airplane mode of the device. This is an Android-only method
	 * 
	 * @param enable - true/false to enable/disable airplane mode
	 */
	protected void setAirplaneMode(boolean enable){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			setting.setAirplaneMode(enable);
			android.setNetworkConnection(setting);
			String mode=enable? "ON" : "OFF";
			log.info("Airplan mode is set to " + mode);
		}else{
			log.warn("setAirplaneMode method is allowed only in android");
		}
	}

	/**
	 * Set the data connection of the device. This is an Android-only method
	 * 
	 * @param enable - true/false to enable/disable data connection
	 */
	protected void setDataConnection(boolean enable){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			setting.setData(enable);
			android.setNetworkConnection(setting);
			String mode=enable? "ON" : "OFF";
			log.info("Data Connection is set to " + mode);
		}else{
			log.warn("setDataConnection method is allowed only in android");
		}
	}

	/**
	 * Set the Wifi connection of the device. This is an Android-only method
	 * 
	 * @param enable - true/false to enable/disable wifi connection
	 */
	public void setWifiConnection(boolean enable){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			setting.setWifi(enable);
			android.setNetworkConnection(setting);
			String mode=enable? "ON" : "OFF";
			log.info("Wifi Connection is set to " + mode);
		}else{
			log.warn("setWifiConnection method is allowed only in android");
		}
	}

	/**
	 * Get the current airplan mode settings of the device. This is an Android-only method
	 * 
	 * @return airplan mode status of the device
	 */
	protected boolean getAirplanMode(){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			return setting.airplaneModeEnabled();
		} else{
			log.warn("getAirplanMode method is allowed only in android");
			return false;
		}
	}

	/**
	 * Get the current data connection settings of the device. This is an Android-only method
	 * 
	 * @return data connection status of the device
	 */
	protected boolean getDataConnection(){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			return setting.dataEnabled();
		} else{
			log.warn("getAirplanMode method is allowed only in android");
			return false;
		}
	}

	/**
	 * Get the current wifi connection settings of the device. This is an Android-only method
	 * 
	 * @return wifi connection status of the device
	 */
	protected boolean getWifiConnection(){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			return setting.wifiEnabled();
		} else{
			log.warn("getAirplanMode method is allowed only in android");
			return false;
		}
	}

	/**
	 * Runs the current app as a background app for the number of seconds requested. 
	 * This is a synchronous method, it returns after the back has been returned to the foreground.
	 * 
	 * @param seconds - Number of seconds to run App in background
	 */
	public void runAppInBackground(int seconds){
		log.info("Running app in background");
		this.driver.runAppInBackground(seconds);
		log.info("Running app in background completed");
	}

	/**
	 * Remove the specified app from the device (uninstall)
	 * 
	 * @param bundleId - the bunble identifier (or app id) of the app to remove
	 * 				e.g., "com.example.android.apis"
	 */
	protected void uninstallApp(String bundleId){
		this.driver.removeApp(bundleId);
		log.info("Uninstalled app - " + bundleId);
	}

	/**
	 * Checks if an app is installed on the device
	 * 
	 * @param bundleId - bundleId of the app to install eg., "com.example.android.apis"
	 * @return True if app is installed, false otherwise
	 */
	protected boolean isAppInstalled(String bundleId){
		return this.driver.isAppInstalled(bundleId);
	}

	/**
	 * Install an app on the mobile device
	 * 
	 * @param appPath - path to app to install
	 */
	protected void installApp(String appPath){
		this.driver.installApp(appPath);
		log.info(appPath + " installed in device");
	}

	/**
	 * Open the notification shade, on Android devices. Android only method.
	 */
	protected void openAndroidNotifications(){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			android.openNotifications();
			log.info("Opened the android notifications");
		} else{
			log.warn("openAndroidNotifications method is allowed only in android");
		}
	}

	/**
	 * Convenience method for pinching an element on the screen. 
	 * "pinching" refers to the action of two appendages pressing the screen and sliding towards each other. 
	 * NOTE: This convenience method places the initial touches around the element, 
	 * if this would happen to place one of them off the screen, appium with return an outOfBounds error. 
	 * In this case, revert to using the MultiTouchAction api instead of this method.
	 * 
	 * @param elementlocator - 	the key associated to Locator(LocatorType;LocatorValue) that
	 *            	specifies the selector
	 */
	protected void pinchElement(String elementlocator){
		WebElement el = this.driver.findElement(ObjectLocators.getBySelector(elementlocator));
		this.driver.pinch(el);
		log.info("pinched element:" + elementlocator);
	}

	/**
	 * Convenience method for pinching an element on the screen. 
	 * "pinching" refers to the action of two appendages pressing the screen and sliding towards each other. 
	 * NOTE: This convenience method places the initial touches around the element, 
	 * if this would happen to place one of them off the screen, appium with return an outOfBounds error. 
	 * In this case, revert to using the MultiTouchAction api instead of this method.
	 * 
	 * @param x - x coordinate to terminate the pinch on
	 * @param y - y coordinate to terminate the pinch on
	 */
	protected void pinchAtPoint(int x, int y){
		this.driver.pinch(x,y);
		log.info("pinched at x:" + x + ", y:"+ y);
	}

	/**
	 * Hides the keyboard if it is showing. On iOS, there are multiple strategies for hiding the keyboard.
	 * Defaults to the "tapOutside" strategy (taps outside the keyboard).
	 * Switch to using hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Done") if this doesn't work.
	 */
	protected void hideKeyboard(){
		this.driver.hideKeyboard();
		log.info("hideKeyboard method invoked");
	}

	/**
	 * Switches to the first available WEBVIEW context in hybrid app
	 */
	protected void switchToWebView(){
		Set<String> contextNames = driver.getContextHandles();
		String context = null;
		for (String contextName : contextNames) {
			if (contextName.contains("WEBVIEW")){
				context=contextName;
				driver.context(contextName);
				break;
			}
		}
		if (getContext().equalsIgnoreCase(context)){
			log.info("Swiched to context:" + getContext());
		}else{
			log.error("context switching to WEBVIEW failed");
			throw new RuntimeException("WEBVIEW context not available for the current app");
		}
	}

	/**
	 * Switches to the first available NATIVE_APP context in hybrid app
	 */
	protected void switchToNativeView(){
		Set<String> contextNames = getContextHandles();
		String context = null;
		for (String contextName : contextNames) {
			if (contextName.contains("NATIVE_APP")){
				context=contextName;
				driver.context(contextName);
				break;
			}
		}
		if (getContext().equalsIgnoreCase(context)){
			log.info("Swiched to context:" + getContext());
		}else{
			log.error("context switching to NATIVE_APP failed");
			throw new RuntimeException("NATIVE_APP context not available for the current app");
		}
	}

	/**
	 * Switches to the specified context
	 * @param context - exact context to switch to or a regExp
	 */
	protected void switchToContext(String context){
		Set<String> contextNames = getContextHandles();
		String context_match = null;
		for (String contextName : contextNames) {
			if (contextName.matches(context)){
				context_match=contextName;
				driver.context(context_match);
				break;
			}
		}
		if (driver.getContext().equalsIgnoreCase(context_match)){
			log.info("Swiched to context:" + getContext());
		}else{
			log.error("context switching to "+ context +" failed");
			throw new RuntimeException(context + " context not available for the current app");
		}
	}

	/**
	 * @return current context of the app
	 */
	protected String getContext(){
		return this.driver.getContext();
	}

	/**
	 * @return a set with all available context in the app
	 */
	protected Set<String> getContextHandles(){
		return this.driver.getContextHandles();
	}

	/**
	 * @return current activity being run on the mobile device 
	 */
	protected String getCurrentActivity(){
		if (this.driver instanceof AndroidDriver<?>){
			AndroidDriver<?> android = (AndroidDriver<?>)this.driver;
			return android.currentActivity();
		}else{
			log.warn("getCurrentActivity method is allowed only in android");
			return null;
		}
	}

	protected void clearTextField(String elementLocator){
		WebElement element = driver.findElement(ObjectLocators.getBySelector(elementLocator));
		double x = element.getLocation().getX() + element.getSize().width -5;
		double y = element.getLocation().getY() + ((double) element.getSize().height / 3);
		tapPostion((int)x, (int)y);
		while (!element.getText().isEmpty()) {
			sendKeyEvent(AndroidKeyCode.BACKSPACE);
		}
	}
}
