package com.pulselms.app.framework;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.pulselms.app.framework.ObjectLocators;

/**
 * 
 * Class with Canned Conditions to wait until .Portion of this is already
 * available in the ExpectedConditions in webdriver. This was needed because
 * this wait will be exposed to the user and we should not expose the web
 * element to the user. Also some of the classes in the canned
 * ExcepectedConditions do not work correctly
 * 
 * 
 * @author Cognizant
 * 
 */
public class Until {

    static Logger log = Logger.getLogger(Until.class);

    /**
     * An expectation for checking that all elements present on the web page 
     * that match the elementLocator are displayed.
     * 
     * @param elementLocator
     * 			  the Locator(LocatorType;LocatorValue) that specifies the selector
     * @return the list of WebElements once they are located
     */
    public static ExpectedCondition<List<WebElement>> elementsToBeDisplayed(
            final String elementLocator) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the elements specified by " + locator + " are displayed");
        return ExpectedConditions.visibilityOfAllElementsLocatedBy(locator);
    }
    
    /**
     * An expectation for checking the element present on the web page 
     * that match the elementLocator are displayed.
     * 
     * @param elementLocator
     * 			  the Locator(LocatorType;LocatorValue) that specifies the object selector
     * @return the WebElement once it is located and visible
     */
    public static ExpectedCondition<WebElement> elementToBeDisplayed(
            final String elementLocator) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the element " + locator + " is displayed");
        return ExpectedConditions.visibilityOfElementLocated(locator);
    }
    
    /**
     * An expectation for checking that there is at least one element present on a web page.
     * 
     * @param elementLocator
     * 				the Locator(LocatorType;LocatorValue) that specifies the object selector
     * @return the list of WebElements once they are located
     */
    public static ExpectedCondition<List<WebElement>> elementsToBePresent(
            final String elementLocator) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the elements specified by " + locator + " are present");
        return ExpectedConditions.presenceOfAllElementsLocatedBy(locator);
    }
    
    /**
     * An expectation for checking that an element is present on the DOM of a page. 
     * This does not necessarily mean that the element is visible.
     * 
     * @param elementLocator
     * 				the Locator(LocatorType;LocatorValue) that specifies the object selector
     * @return the WebElement once it is located
     */
    public static ExpectedCondition<WebElement> elementToBePresent(
            final String elementLocator) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the elements specified by " + locator + " is present");
        return ExpectedConditions.presenceOfElementLocated(locator);
    }

    /**
     * An expectation for checking an element is visible and enabled such that you can click it.
     * 
     * @param elementLocator
     * 			the Locator(LocatorType;LocatorValue) that specifies the selector
     * @return the WebElement once it is located and clickable (visible and enabled)
     */
    public static ExpectedCondition<WebElement> elementToBeClickable(
            final String elementLocator) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the element " + locator
                + " becomes clickable");
        return ExpectedConditions.elementToBeClickable(locator);
    }

    /**
     * An expectation for checking that an element is either invisible or not present on the DOM.
     * 
     * @param elementLocator
     * 			the Locator(LocatorType;LocatorValue) that specifies the object
     * @return boolean
     */
    public static ExpectedCondition<Boolean> elementToBeInvisible(
            final String elementLocator) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the element " + locator + " becomes invisible");
        return ExpectedConditions.invisibilityOfElementLocated(locator);
    }

    /**
     * wait until a new window opens up
     * 
     * @param numberOfCurrentWindowsOpened
     * @return boolean
     */
    public static ExpectedCondition<Boolean> newWindowOpens(
            final int numberOfCurrentWindowsOpened) {
        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getWindowHandles().size() >= numberOfCurrentWindowsOpened;
            }
        };

    }

    /**
     * wait until a CSS property changes
     * 
     * @param elementLocator
     * 				the Locator(LocatorType;LocatorValue) that specifies the object
     * @param attribute
     * @param newProperty
     * @return
     */
    public static ExpectedCondition<Boolean> cssPropertyChangesTo(
            final String elementLocator, final String attribute,
            final String expectedProperty) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the element's " + locator + " css property "
                + attribute + " is changed to " + expectedProperty);
        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                try {
                    WebElement ele = d.findElement(locator);
                    if (ele != null && ele.isDisplayed()) {
                        System.out.println(ele.getCssValue(attribute));
                        return ele.getCssValue(attribute).trim().contains(expectedProperty);
                    } else {
                        return false;
                    }
                } catch (StaleElementReferenceException err) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "Until the css attribute" + expectedProperty
                        + " of element " + locator.toString() + "changes to "
                        + expectedProperty;
            }

        };
    }

	/**
	 * An expectation for checking if the given text is present in the element that matches the given locator.
	 * 
	 * @param elementLocator
	 * 			the Locator(LocatorType;LocatorValue) that specifies the object
	 * @param expectedText
	 * 			to be present in the element found by the locator
	 * @return
	 * 		true once the first element located by locator contains the given text
	 */
	public static ExpectedCondition<Boolean> textToBePresentInElement(
            final String elementLocator, final String expectedText) {
        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the text " + expectedText
                + " is displayed on the element" + locator);
        return ExpectedConditions.textToBePresentInElementLocated(locator,
                expectedText);
    }
	
		
	/**
	 * An expectation for checking if the given text is present in the specified elements value attribute.
	 * 
	 * @param elementLocator
	 * 			the Locator(LocatorType;LocatorValue) that specifies the object
	 * @param expectedText
	 * 			 to be present in the value attribute of the element found by the locator
	 * @return
	 * 		true once the value attribute of the first element located by locator contains the given text
	 */
	public static ExpectedCondition<Boolean> textToBePresentInElementValue(
            final String elementLocator, final String expectedText) {

        final By locator = ObjectLocators.getBySelector(elementLocator);
        log.info("Waiting until the text " + expectedText
                + " is displayed on the element" + locator);
        return ExpectedConditions.textToBePresentInElementLocated(locator,
                expectedText);
    }

    /**
     * An expectation for checking if the current page URL contains expected text.
     * 
     * @param expectedParam
     * 				the text to be present in the current page URL
     * @return true if the URL contains the expected text, false otherwise
     */
    public static ExpectedCondition<Boolean> urlContainsParam(
            final String expectedParam) {
        log.info("Waiting until the URL contains the string: " + expectedParam);
        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(expectedParam);
            }

            @Override
            public String toString() {
                return "Until the URL Contains the param : " + expectedParam;
            }
        };
    }

    /**
     * An expectation for checking that the title contains a case-sensitive substring
     * 
     * @param expectedPageTitle
     * 				the fragment of title expected
     * @return true when the title matches, false otherwise
     */
    public static ExpectedCondition<Boolean> titleContains(
            final String expectedPageTitle) {
        return ExpectedConditions.titleContains(expectedPageTitle);
    }
    
    /**
     * An expectation for checking the title of a page.
     * 
     * @param expectedPageTitle
     * 					the expected title, which must be an exact match
     * @return true when the title matches, false otherwise
     */
    public static ExpectedCondition<Boolean> titleIs(
            final String expectedPageTitle) {
        return ExpectedConditions.titleIs(expectedPageTitle);
    }

}
