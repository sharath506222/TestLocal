package com.pulselms.app.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import com.pulselms.app.framework.BrowserBotException;
import com.pulselms.app.framework.Until;

/**
 * A class representing a bot. Bot is an automation agent that executes commands
 * on the calling functions behalf. This method is advantageous while working on
 * a project with multiple developers, This brings in discipline while coding
 * 
 * @author Cognizant 
 * 
 */
public class WebDriverBase {

	protected static WebDriver driver;
	// Wait for the page to load indefinitely
	// private static final int PAGE_LOAD_TIMEOUT = -1;
	private static final int TIMEOUT_IN_SECONDS = 120;
	private static final int POLL_INTERVAL = 500;
	protected static final Logger log = Logger.getLogger(WebDriverBase.class);
		
	public void setDriver(WebDriver driver){
		this.driver=driver;
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
/*	public WebDriverBase(WebDriver driver) {
		this.driver=driver;
	} */
	/**
	 * 
	 * A generic method wait is used to make the web driver to wait until a
	 * specific event has occurred or until the timeout. It uses FluentWait to
	 * wait for the particular condition. Each wait instance defines the maximum
	 * amount of time to wait for a particular condition, as well as the
	 * frequency with which to check the condition. Also, the user may configure
	 * the wait to ignore specific types of exceptions while waiting, such as
	 * NoSuchElementExceptions when searching for an element on the page.
	 * 
	 * This is an internal method
	 * 
	 * @param <U>
	 * @param condition
	 * @return <U>
	 */
	public <U> U wait(ExpectedCondition<U> condition) {

		FluentWait<WebDriver> wait =
				new FluentWait<WebDriver>(this.driver).ignoring(
						RuntimeException.class).withTimeout(TIMEOUT_IN_SECONDS,
								TimeUnit.SECONDS).pollingEvery(POLL_INTERVAL,
										TimeUnit.MILLISECONDS);
		try {
			return wait.until(condition);
		} catch (TimeoutException err) {
			String errMessage =
					"Bot encountered a timeout while waiting for a condition,  "
							+ err.getLocalizedMessage();
			throw new BrowserBotException(errMessage);
		}
	}

	/**
	 * This method is a overloaded method of wait_internal providing flexibility in 
	 * the wait time by providing timeoutInSeconds
	 * 
	 * This is an internal method
	 * 
	 * @param condition
	 * @param timeoutInSeconds
	 * @return <U>
	 */
	public <U> U wait(ExpectedCondition<U> condition,
			int timeoutInSeconds) {

		FluentWait<WebDriver> wait =
				new FluentWait<WebDriver>(this.driver).ignoring(
						RuntimeException.class).withTimeout(timeoutInSeconds,
								TimeUnit.SECONDS).pollingEvery(POLL_INTERVAL,
										TimeUnit.MILLISECONDS);
		try {
			return wait.until(condition);
		} catch (TimeoutException err) {
			String errMessage =
					"Bot encountered a timeout while waiting for a condition,  "
							+ err.getLocalizedMessage();
			throw new BrowserBotException(errMessage);
		}

	}

	/**
	 * Method to making the driver to wait for a time interval before proceeding further
	 * 
	 * @param timeOutInSeconds
	 */
	public void sleep(int timeOutInSeconds) {
		try {
			Thread.sleep(timeOutInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void refresh()
	{
		driver.navigate().refresh();
	}
	
	
	/**
	 * This method controls the time to load the page
	 */
	protected void pageLoadTime(int loadTimeInSec){
		driver.manage().timeouts().pageLoadTimeout(loadTimeInSec, TimeUnit.SECONDS);
	}

	/**
	 * clicks on the given web element which uses click method to click.
	 * findElement method is used to locate the web element with the given
	 * selector which will return the first matching element on the current
	 * page.
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 */
	protected void clickOnElement(String elementLocator) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		WebElement element = driver.findElement(locator);
		if (element.isDisplayed()) {            // can be replaced with Expected conditions' elementToBeClickable 
			element.click();
			log.info("Clicked on element: " + locator);
		}
	}

	/**
	 * Method to return the current window handle
	 * 
	 * @return the current window handle
	 */
	protected String getWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * clicks on the given web element which uses click method to click.
	 * findElement method is used to locate the web element with the given
	 * selector which will return the first matching element on the current
	 * page. Suppose, if the element text is passed as an input instead of the
	 * web element for this function then it will find the web element which has
	 * this inner text and will click on that
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @param elementText
	 *            the text of the element
	 **/
	protected void clickOnElementWithText(String elementLocator,
			String elementText) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		List<WebElement> elementList = driver.findElements(locator);
		int index = 0;
		for (WebElement element : elementList) {
			if (element.getText().trim().equalsIgnoreCase(elementText)
					&& element.isDisplayed()) {
				element.click();
				index++;
				break;
			}
		}
		if (index == elementList.size()) {
			throw new RuntimeException(
					"Could not locate any element described by the locator "
							+ elementLocator.toString() + " with text "
							+ elementText);
		}
	}
	
	protected void clickOnElementAtPosition(String elementLocator, int x, int y) {
		
		By locator = ObjectLocators.getBySelector(elementLocator);
		WebElement element = driver.findElement(locator);
		Actions action = new Actions(this.driver);
		action.moveToElement(element, x, y).click().build().perform();
	}

	/**
	 * simulates typing into an element, which may set its value.
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue)
	 *            that specifies the selector
	 * @param text
	 *            the keys to send to the element
	 */
	protected void type(String elementLocator, String text) {
		//By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Entering Text");
		WebElement element = wait(Until.elementToBeClickable(elementLocator));
		element.sendKeys(text);
		log.info("Entered " + text + " into the " + elementLocator + " text field");
	}

	/**
	 * simulates typing into an element, which may set its value.
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue)
	 *            that specifies the selector
	 * @param text
	 *            the keys to send to the element
	 */
	protected void type(String elementLocator, String text, Keys keys) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Entering " + text + " into the " + locator
				+ " text field and pressing " + keys);
		WebElement element = driver.findElement(locator);
		element.sendKeys(text);
		element.sendKeys(keys);
	}

	/**
	 * navigates to a particular page with the given url. It uses driver get
	 * method to load a new web page in the current browser window with the
	 * given url
	 * 
	 * @param url
	 *            the URL where the browser has to navigate
	 */
	protected void goToUrl(String url) {
		log.info("Loading the URL:" + url);
		this.driver.get(url);
	}

	/**
	 * Retrieves the specified css attribute for a given web element.
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @param attribute
	 *            the CSS attribute to be fetched for the given element
	 * @return String the attribute pertaining to the element
	 */
	protected String getCssAttribute(String elementLocator, String attribute) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Getting CSS value of " + attribute + " from the locator "
				+ locator);
		return driver.findElement(locator).getCssValue(attribute);
	}

	/**
	 * Retrieves the specified attribute for a given web element
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @param attribute
	 *            the attribute name to be fetched for the given element
	 * @return String the attribute pertaining to the element
	 */
	protected String getAttribute(String elementLocator, String attribute) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		String value = driver.findElement(locator).getAttribute(attribute);
		log.info("Read "+attribute+" attribute value " + value + ", from the locator "
				+ locator);
		return value;
	}

	/**
	 * Submits the form via the specified element. If this current element is a
	 * form, or an element within a form, then this will be submitted to the
	 * remote server. If this causes the current page to change, then this
	 * method will block until the new page is loaded
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 */
	protected void submit(String elementLocator) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Submitting the Form");
		driver.findElement(locator).submit();
	}

	/**
	 * Retrieves the number of windows that is currently opened. It uses size
	 * method of driver getWindowHandles method to get the count of the browser
	 * windows
	 * 
	 * @return int the count of browser windows
	 */
	protected int getNumberOfOpenWindows() {
		return driver.getWindowHandles().size();
	}

	/**
	 * navigate is an abstraction allowing the driver to access the browser's
	 * history and to navigate to a given URL. navigateBack is used to navigate
	 * to the previous page. It uses back method of driver navigate method for
	 * this purpose
	 */
	protected void navigateBack() {
		driver.navigate().back();
		log.info("Navigating to the previous page");
	}

	/**
	 * navigate is an abstraction allowing the driver to access the browser's
	 * history and to navigate to a given URL. navigateForward is used to
	 * navigate to the next page. It uses forward method of driver navigate for
	 * this purpose
	 */
	protected void navigateForward() {
		driver.navigate().forward();
		log.info("Navigating to the next page");
	}

	/**
	 * selectDropDown is used to select an option from the given drop down web
	 * element. It will set the value based on the visible text provided
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue)
	 * @param visibleText
	 *            the option to select
	 */
	protected void selectValueFromDropDown(String elementLocator,
			String visibleText) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Selecting " + visibleText + " from the DropDown");
		WebElement dropDownElement = driver.findElement(locator);
		Select dropDownSelect = new Select(dropDownElement);
		dropDownSelect.selectByVisibleText(visibleText);
	}

	/**
	 * selectDropDown is used to select an option from the given drop down web
	 * element. It will set the value based on the index value provided
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue)
	 * @param index
	 *            the index value of the option to select
	 */
	protected void selectValueFromDropDown(String elementLocator, int index) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Selecting " + index + " from the DropDown");
		WebElement dropDownElement = driver.findElement(locator);
		Select dropDownSelect = new Select(dropDownElement);
		dropDownSelect.selectByIndex(index);
	}

	/**
	 * method to perform the drag and drop action
	 * 
	 * @param fromElementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @param toElementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 */
	protected void dragAndDrop(String fromElementLocator,
			String toElementLocator) {
		By fromLocator = ObjectLocators.getBySelector(fromElementLocator);
		By toLocator = ObjectLocators.getBySelector(toElementLocator);
		Actions action = new Actions(this.driver);
		action.dragAndDrop(driver.findElement(fromLocator),
				driver.findElement(toLocator)).build().perform();
	}

	/**
	 * Forces the refresh operation. It will load the driver instance in the
	 * Actions class and then will perform the refresh operation
	 * 
	 */
	protected void forceRefresh() {
		Actions action = new Actions(this.driver);
		log.info("Forcefully refreshing the page");
		action.keyDown(Keys.CONTROL).sendKeys(Keys.F5).keyUp(Keys.CONTROL)
		.perform();
	}

	/**
	 * Method to perform the hover over action on the given element
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 */
	protected void hoverOver(String elementLocator) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		Actions action = new Actions(this.driver);
		log.info("Hovering over the mouse on the element " + locator);
		WebElement element = driver.findElement(locator);
		action.moveToElement(element).build().perform();
	}

	/**
	 * Method to press the key. It uses sendKeys method to send the keys to the
	 * given web element
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @param key
	 *            the keys to send to the element
	 */
	protected void pressKey(String elementLocator, Keys key) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		driver.findElement(locator).sendKeys(key);
	}

	/**
	 * A helper method to see if the element is enabled . An Element might be
	 * present in a hTML page However it might not be enabled We use this method
	 * to check and see if the element is enabled or not
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @return returns true if the element is enabled
	 */
	protected Boolean isElementEnabled(String elementLocator) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		return driver.findElement(locator).isEnabled();
	}

	/**
	 * switches the focus to the window based on the title. It will get all the
	 * window handles using driver getWindowHandles method then focus to the
	 * corresponding window with the given title
	 * 
	 * @param titleOfNewWindow
	 *            the string that specifies the title of the window
	 */
	protected void switchToWindowByTitle(String titleOfNewWindow) {
		Set<String> windowHandles = driver.getWindowHandles();
		for (String windowHandle : windowHandles) {
			driver.switchTo().window(windowHandle);
			if (driver.getTitle().contains(titleOfNewWindow)) {
				break;
			}
		}
	}

	/**
	 * Returns focus to the main browser window by using defaultContent method
	 * of the driver switchTo method
	 * 
	 */
	protected void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	/**
	 * Refreshes the page
	 * 
	 */
	protected void refreshPage() {
		driver.navigate().refresh();
	}

	/**
	 * Performs double click on an element. Action and Actions classes in
	 * interactions are used
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 */
	protected void doubleClick(String elementLocator) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		Actions builder = new Actions(driver);
		WebElement element = driver.findElement(locator);
		Action hoverOverRegistrar = (Action) builder.doubleClick(element);
		hoverOverRegistrar.perform();
	}

	/**
	 * Deletes all the cookies. It uses deleteAllCookies of driver manage method
	 * 
	 */
	protected void clearCookies() {
		this.driver.manage().deleteAllCookies();
	}

	/**
	 * Returns the displayed element from a list of similarly named elements by
	 * getting the element locator as the input value
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @return returns the displayed element
	 */
	protected WebElement getDisplayedElement(String elementLocator) {
		log.info("Finding the displayed Element for the locator provided--"
				+ elementLocator);
		By locator = ObjectLocators.getBySelector(elementLocator);
		List<WebElement> elementList = findElements(locator);
		for (WebElement element : elementList) {
			if (element.isDisplayed())
				return element;
		}
		throw new BrowserBotException("Element not found--" + elementLocator
				+ " displayed");
	}

	/**
	 * Gets a string representing the current URL of the page which is currently
	 * loaded in the browser
	 * 
	 * @return String the current URL
	 */
	protected String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/**
	 * Retrieves the size for the specified element.
	 * 
	 * @param propKey
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @return int the size for the element
	 */
	protected int getListSize(String propKey) {
		By locator = ObjectLocators.getBySelector(propKey);
		return findElements(locator).size();
	}

	/**
	 * Finds all the Web Elements that matches the given by selector
	 * 
	 * @param by
	 *            By object to identify the element(s)
	 * @return List<WebElement> the list of web elements that is found
	 * @throws BrowserBotException
	 *             if no element is found
	 */
	private List<WebElement> findElements(By by) throws BrowserBotException {
		try {
			return driver.findElements(by);
		} catch (NoSuchElementException e) {
			String msg = "Element could not be located " + by.toString();
			log.info(msg);
			throw new BrowserBotException(msg);
		}
	}

	/**
	 * Closes the current pop up window
	 */
	protected void closePopupWindow() {
		driver.close();
		for (String name : driver.getWindowHandles()) {
			driver.switchTo().window(name);
			log.info("popup window closed : " + name);
			break;
		}
	}

	/**
	 * Closes the given pop up window without switching to any other window
	 * 
	 * @param windowID
	 *            the window id that should be closed
	 */
	protected void closePopupWindow(String windowID) {
		driver.switchTo().window(windowID).close();
	}

	/**
	 * Closes the given popup window and switches to random window
	 * 
	 * @param windowID
	 *            the window id that should be closed
	 */
	protected void closepopUpAndSwitchtoParent(String windowID) {
		closePopupWindow(windowID);
		for (String name : driver.getWindowHandles()) {
			driver.switchTo().window(name);
			break;
		}
	}

	/**
	 * Determines if a specific element is present
	 * 
	 * @param propKey
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @return boolean true/false if element is found/not found
	 */
	protected boolean isElementPresent(String propKey) {
		By locator = ObjectLocators.getBySelector(propKey);
		log.debug("Checking the presence of the Element: " + propKey + " : "
				+ propKey);
		return isElementPresent(locator);
	}
	
	protected boolean isElementPresentInElement(WebElement baseElement, String propKey) {
		By locator = ObjectLocators.getBySelector(propKey);
		log.debug("Checking the presence of the Element: " + propKey + " : "
				+ propKey);
		return isElementPresent(baseElement,locator);
	}
	
	protected WebElement findElement(String propKey) {
		By locator = ObjectLocators.getBySelector(propKey);
		return driver.findElement(locator);
	}

	/**
	 * Determines if a specific element is visible
	 * 
	 * @param propKey
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @return boolean true/false if element is visible/not visible
	 */
	protected boolean isElementVisible(String propKey) {
		By locator = ObjectLocators.getBySelector(propKey);
		log.debug("Checking the presence of the Visble: " + propKey + " : "
				+ propKey);
		return isElementVisible(locator);
	}

	/**
	 * Determines if a specific element is checked
	 * 
	 * @param propKey
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @return boolean true/false if element is checked/unchecked
	 */
	protected boolean isElementChecked(String propKey) {
		By locator = ObjectLocators.getBySelector(propKey);
		log.debug("Checking the presence of the Visble: " + propKey + " : "
				+ propKey);
		return isElementChecked(locator);
	}

	/**
	 * Determines if an element is present with the given by selector
	 * 
	 * @param by
	 *            By locator to find the element
	 * @return boolean true/false if element is found/not found
	 */
	private boolean isElementPresent(By by) {
		try {
			WebElement element = driver.findElement(by);
			if (element != null) {
				log.debug("Element is present: " + by.toString());
				return true;
			}
			log.warn("Element is NOT present: " + by.toString());
			return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}
	
	private boolean isElementPresent(WebElement baseElement, By by) {
		try {
			WebElement element = baseElement.findElement(by);
			if (element != null) {
				log.debug("Element is present: " + by.toString());
				return true;
			}
			log.warn("Element is NOT present: " + by.toString());
			return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Determines if an element is checked with the given by selector
	 * 
	 * @param by
	 *            By locator to find the element
	 * @return boolean true/false if element is checked/unchecked
	 */
	private boolean isElementChecked(By by) {
		try {
			WebElement element = driver.findElement(by);
			if (element.isSelected()) {
				log.debug("Element is checked: " + by.toString());
				return true;
			}
			log.warn("Element is NOT checked: " + by.toString());
			return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Determines if an element is visible with the given by selector
	 * 
	 * @param by
	 *            By locator to find the element
	 * @return boolean true/false if element is visible/not visible
	 */
	private boolean isElementVisible(By by) {
		try {
			WebElement element = driver.findElement(by);
			if (element.isDisplayed()) {
				log.debug("Element is present: " + by.toString());
				return true;
			}
			log.warn("Element is NOT present: " + by.toString());
			return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Method to switch to a parent window. It will get all the window handles
	 * and will iterate over the handles and then focus the particular window
	 * checking that window is not the parent window
	 * 
	 * @param parentWindowId
	 *            the id of the parent window
	 * 
	 */
	protected void switchToParentWindow(String parentWindowId) {
		String windowId = "";
		Set<String> set = driver.getWindowHandles();
		log.info("Number of windows opened: " + set.size());
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			windowId = (String) iterator.next();
			if (windowId.equals(parentWindowId)) {
				log.info("Switching to the window: " + parentWindowId);
				driver.switchTo().window(parentWindowId);
			}
			log.info("windowId" + windowId);
		}
	}

	/**
	 * Method for selecting multiple options from the list box. It uses Actions
	 * class for keyDown and keyUp methods
	 * 
	 * @param listBoxLocator
	 *            the locator of the list box
	 * @param indexes
	 *            the indexes of all the items
	 */
	protected void selectMultipleListItems(String listBoxLocator,
			int[] indexes) {
		By locator = ObjectLocators.getBySelector(listBoxLocator);
		Actions action = new Actions(this.driver);
		WebElement listItem = driver.findElement(locator);
		List<WebElement> listOptions =
				listItem.findElements(By.tagName("option"));
		action.keyDown(Keys.CONTROL).perform();
		for (int i : indexes) {
			listOptions.get(i).click();
		}
		action.keyUp(Keys.CONTROL).perform();
	}

	/**
	 * Switch to the pop up window by getting parent window as the input value
	 * It gets the window handles and compares it with parent window to select
	 * the new pop up window other than parent window
	 * 
	 * @param parentWindowId
	 *            of the parent window
	 */
	protected void switchToPopUpWindow(String parentWindowId) {
		String windowId = "";
		Set<String> set = driver.getWindowHandles();
		log.info("Number of windows opened: " + set.size());
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			windowId = (String) iterator.next();
			if (!windowId.equals(parentWindowId)) {
				log.info("Switching to the window: " + parentWindowId);
				driver.switchTo().window(windowId);
			}
			log.info("Popup windowId" + windowId);
		}
	}

	/**
	 * Method for switching to the frame of the given frame id
	 * 
	 * @param frameId
	 */
	protected void switchToFrame(int frameId) {
		log.info("Switching to the frame: " + frameId);
		driver.switchTo().frame(frameId);
	}

	/**
	 * Method for switching to the frame of the given frame name or selector
	 * 
	 * @param frameName
	 */
	protected void switchToFrame(String frameName) {
		log.info("Switching to the frame: " + frameName);
		By locator = null;
		try{
			locator = ObjectLocators.getBySelector(frameName);
		}catch(BrowserBotException e){
			locator = null;
		}
		if (locator == null){
			wait(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
		}else{
			wait(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
		}
	}

	/**
	 * gets the visible inner text of the specified web element, including
	 * sub-elements,without any leading or trailing whitespace
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the selector
	 * @return the text of the given element
	 */
	protected String getText(String elementLocator) {
		System.out.println("element-->"+elementLocator);
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Getting the text of the element: " + locator);
		return this.driver.findElement(locator).getText();
	}
	
	
	protected String getTextWithInElement(WebElement baseElement, String elementLocator) {
		By locator = ObjectLocators.getBySelector(elementLocator);
		log.info("Getting the text of the element: " + locator);
		return baseElement.findElement(locator).getText();
	}

	/**
	 * Returns the text from the alert window
	 * 
	 * @return the text of the alert
	 */
	protected String getAlertText() {
		log.info("Getting the Alert text");
		return driver.switchTo().alert().getText();
	}

	/**
	 * Accepts the alert window
	 */
	protected void acceptAlert() {
		log.info("Confirming the operation");
		driver.switchTo().alert().accept();
	}

	/**
	 * Dismisses the alert window
	 */
	protected void dismissAlert() {
		log.info("Cancelling the operation");
		driver.switchTo().alert().dismiss();
	}

	/**
	 * Executes the JavaScript of the given script using the object of
	 * JavaScriptExecutor and returns the resulting text The executeScript
	 * method of JavaScriptExecutor is used
	 * 
	 * @param script
	 *            JavaScript that is to be executed
	 * @return text
	 */
	protected String executeJavaScript(String script) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String text = (String) jsExecutor.executeScript(script);
		return text;
	}

	/**
	 * Returns the windowset containing all the opened window handles
	 * 
	 * @return The set of windows in the windowhandle list
	 */
	protected Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	/**
	 * Gets the current window title
	 * 
	 * @return the current window title
	 */
	protected String getWindowTitle() {
		log.info("Getting the title of the page");
		return driver.getTitle();
	}

	/**
	 * Switches to window having the specified element
	 * 
	 * @param elementLocator
	 *            the locator for the element
	 * @return True if switch is performed. False if no window with the expected
	 *         element is found
	 */
	protected boolean switchToWindowWithElement(String elementLocator) {
		for (String window : driver.getWindowHandles()) {
			driver.switchTo().window(window);
			if (isElementPresent(elementLocator))
				return true;
		}
		return false;
	}

	/**
	 * Switches to window having the specified url part
	 * 
	 * @param URLPart
	 *            the part of url to match
	 * @return True if switch is performed. False if no window with the expected
	 *         url part is found
	 */
	protected boolean switchToWindowWithURLPart(String URLPart) {
		for (String window : driver.getWindowHandles()) {
			driver.switchTo().window(window);
			if (getCurrentUrl().contains(URLPart))
				return true;
		}
		return false;
	}

	/**
	 * Get the text of similarly named elements
	 * 
	 * @param elementLocator
	 * @return list of text present in similarly named elements
	 */
	protected List<String> getTextOfSimilarElements(String elementLocator) {
		List<WebElement> elementList =
				wait(Until.elementsToBeDisplayed(elementLocator));
		List<String> elementTextList = new ArrayList<String>();

		log.debug("There are " + elementList.size()
				+ " Similar elements(Element with locator " + elementLocator
				+ " )");
		for (WebElement anElement : elementList) {
			elementTextList.add(anElement.getText());
		}
		return elementTextList;
	}
	
	protected List<WebElement> getSimilarElements(String elementLocator) {
		
		log.debug("Getting Similar elements with locator " + elementLocator
				+ " )");
		return wait(Until.elementsToBeDisplayed(elementLocator));
	}

	/**
	 * Get the html page source of the current web page
	 * 
	 * @return String page source
	 */
	protected String getPageSource() {
		return this.getPageSource();

	}
	
	/**
	 * Use this method to check/uncheck the checkbox irrespective of its current state
	 * 
	 * @param elementLocator
	 * 			  the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 * @param check
	 * 			  true to check the checkbox, false to uncheck it
	 */
	protected void setCheckBox(String elementLocator,boolean check){
		WebElement checkbox = wait(Until.elementToBeClickable(elementLocator));
		if (check){
			if (!checkbox.isSelected()){
				checkbox.click();
			}
		}else{
			if (checkbox.isSelected()){
				checkbox.click();
			}
		}
	}

	/**
	 * Clear the specified text box.
	 * 
	 * @param elementLocator
	 *            the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 */
	protected void clearTextBox(String elementLocator) {
		wait(Until.elementToBeClickable(elementLocator)).clear();
	}
	
	/**
	 * performs select radio button/Check Box using Java Script
	 * 
	 * @param propKey
	 * 				the key associated to Locator(LocatorType;LocatorValue) that
	 *            	specifies the by selector
	 * @param select
	 * 				Set this to true to select the check box/radio button, false to deselect them
	 */
	protected void chooseOptionUsingJavaScript(String propKey,boolean select)
	{
		By by = ObjectLocators.getBySelector(propKey);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;  
		log.info("Choosing the option button "+by + " using Java Script");
		jsExecutor.executeScript("arguments[0].checked = arguments[1];", driver.findElement(by),select);
	}

	/**
	 * performs click operation using Java Script
	 * 
	 * @param propKey
	 * 			  the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 */
	protected void clickOnElementUsingJavaScript(String propKey)
	{
		By by = ObjectLocators.getBySelector(propKey);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;  
		log.info("Clicking on the element "+by+" using JavaScript");
		jsExecutor.executeScript("arguments[0].click();", driver.findElement(by));
	}
	
	/**
	 * performs type operation using Java Script
	 * 
	 * @param propKey
	 * 			  the key associated to Locator(LocatorType;LocatorValue) that
	 *            specifies the by selector
	 */
	protected void typeUsingJavaScript(String propKey,String text)
	{
		By by = ObjectLocators.getBySelector(propKey);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;  
		log.info("typing "+ text +" on the element "+by+" using JavaScript");
		jsExecutor.executeScript("arguments[0].value=arguments[1];", driver.findElement(by),text);
	}
	
	/**
	 * performs scroll into view of the element using java script
	 * 
	 * @param propKey
	 * 			the key associated to Locator(LocatorType;LocatorValue) that
	 *          specifies the by selector
	 */
	protected void scrollToElementJavaScript(String propKey, boolean scrollTop)
	{
		By by = ObjectLocators.getBySelector(propKey);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;  
		log.info("scrolling to the element "+by+" using JavaScript");
		jsExecutor.executeScript("arguments[0].scrollIntoView(arguments[1]);", driver.findElement(by),scrollTop);
	}
	
	/**
	 * @return current browser name the driver is running on
	 */
	protected String getBrowser(){
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = cap.getBrowserName().toLowerCase();
		return browserName;
	}
}
