package com.phunware.util;

import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidDriver;
import io.selendroid.SelendroidLauncher;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import com.phunware.constants.GlobalConstants;
import com.phunware.test.Suite;

/**
 * @author bhargavas This class contains all Selenium methods
 * 
 */
public class SeleniumUtils extends Suite {
	private static Logger logger = Logger.getLogger(SeleniumUtils.class);
	private static WebDriver driver = null; // Create Object for Webdriver
											// interface

	/**
	 * This method is used to launch browser as per the config properties
	 */
	public static boolean launchBrowser() {
		try {
			// Identify the platform on which user is working on
			Platform current = Platform.getCurrent();
			if (current.toString().equalsIgnoreCase("MAC")) {
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					driver = new FirefoxDriver();
					// Initialize driver object to firefox
				} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
					System.setProperty("webdriver.ie.driver",
							GlobalConstants.iebrowser);
					driver = new InternetExplorerDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
					System.setProperty("webdriver.chrome.driver",
							GlobalConstants.CHROME_DRIVER_FOR_MAC);
					driver = new ChromeDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
					driver = new SafariDriver();
				}
			} else {
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					driver = new FirefoxDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
					System.setProperty("webdriver.ie.driver",
							GlobalConstants.iebrowser);
					driver = new InternetExplorerDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
					System.setProperty("webdriver.chrome.driver",
							GlobalConstants.chromebrowser);
					ChromeOptions cromeOpt = new ChromeOptions();
					cromeOpt.addArguments("--test-type");
					driver = new ChromeDriver(cromeOpt);
				} else if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
					driver = new SafariDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("ANDROID")) {
					SelendroidConfiguration config = new SelendroidConfiguration();
					// Add the selendroid-test-app to the standalone server
					config.addSupportedApp("D:\\mobile_Automation\\android\\"
							+ "adt-bundle-windows-x86_64-20130219\\sdk\\"
							+ "platform-tools\\selendroid-test-app-0.9.0 .apk");
					SelendroidLauncher selendroidServer = new SelendroidLauncher(
							config);
					selendroidServer.lauchSelendroid();
					// Create the selendroid capabilities and specify to use an
					// emulator and selendroid's test app
					SelendroidCapabilities caps = new SelendroidCapabilities(
							"io.selendroid.testapp:0.9.0");
					driver = new SelendroidDriver(caps);
				}
			}
			// Set implicit time for driver object
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.get(configproperties.get(1));
			driver.manage().window().maximize();// Maximize the window
			ReportUtils.setWebDriverReports(driver);
			return true;
		} catch (Exception e) {
			logger.error("Error while Initializing the driver..."
					+ e.toString());
			return false;
		}
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @return element if true else null find the element in the application
	 * 
	 */
	public static WebElement findobject(String locatortype, String locatorvalue) {
		WebElement element = null;
		try {
			if (locatortype.equalsIgnoreCase("name")) {
				element = driver.findElement(By.name(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("link")) {
				element = driver.findElement(By.linkText(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("id")) {
				element = driver.findElement(By.id(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("class")) {
				element = driver.findElement(By.className(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("cssSelector")) {
				element = driver.findElement(By.cssSelector(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("xpath")) {
				element = driver.findElement(By.xpath(locatorvalue));
			}
			return element;
		} catch (Exception e) {
			logger.error("Unable to identify the element with " + locatortype
					+ " " + "and value " + locatorvalue);
		}
		return null;
	}

	/**
	 * @param alertText
	 * @param expected
	 *            - compares the expected text with actual text and asserts the
	 *            user
	 * @return
	 */
	public static boolean assertEqual(String alertText, String expected) {
		boolean assertionFlag = false;
		try {
			alertText = alertText.trim();
			expected = expected.trim();
			if (alertText.equalsIgnoreCase(expected)) {
				assertionFlag = true;
			} else {
				logger.error("Text comparison fails. The expected text [ "
						+ expected + " ] and actual text [" + alertText
						+ " ] are not same");
			}
		} catch (Exception e) {
			logger.error("Unexpected error while comparing texts" + e);
		}
		return assertionFlag;
	}

	/**
	 * @param userelement
	 * @param passwordelement
	 * @param button
	 * @param username
	 * @param password
	 *            performs login operation
	 */
	public static void login(WebElement userelement,
			WebElement passwordelement, WebElement button, String username,
			String password) {
		try {
			userelement.sendKeys(username);
			passwordelement.sendKeys(password);
			button.click();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param errorelement
	 * @return text of the object if exists else null
	 * 
	 */
	public static String getText(WebElement element) {
		try {
			return element.getText();
		} catch (Exception e) {
			logger.error("Error while reading the text of a element. The error message is "
					+ e);
			return null;
		}
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 *            performs click operation on element
	 */
	public static void click(String locatortype, String locatorvalue) {
		WebElement element = null;
		try {
			element = findobject(locatortype, locatorvalue);
			if (element != null) {
				element.click();
			} else {
				logger.error("Unable to identify the element. So Click operation is not performed");
			}
		} catch (Exception e) {
			logger.error("Error while clicking on element");
		}
	}

	/**
	 * close the browser
	 */
	public static void closeBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param element
	 * @param string
	 *            this method is used to enter the text in textbox
	 */
	public static void type(WebElement element, String string) {
		try {
			element.sendKeys(string);
		} catch (Exception e) {
			logger.error("Error while typing the text" + e);
		}
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @param option
	 *            - option value text
	 * @return
	 * 
	 *         this method is used to select the item from the drop down using
	 *         text
	 */
	public static boolean selectDropdownByText(String locatortype,
			String locatorvalue, String option) {
		WebElement element = null;
		try {
			element = findobject(locatortype, locatorvalue);
			Select select = new Select(element);
			select.selectByVisibleText(option);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * To scroll down the browser using javascript
	 */
	public static void scrollDown() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,450)", "");
		} catch (Exception e) {
			logger.error("Error while scrolling down " + e.toString());
		}
	}

	/**
	 * Clicks on OK button of Alert window
	 */
	public static void acceptAlertWindow() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			sleepThread(2);
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error("Unexpected error at Alert window " + e.toString());
		}
	}

	/**
	 * This method is used to close the Alert window
	 * 
	 */
	public static void dismissAlertWindow() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error("Unexpected error at Alert window " + e.toString());
		}
	}

	/**
	 * @param element
	 *            To clean the text of text box
	 */
	public static void clearText(WebElement element) {
		try {
			element.clear();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param element
	 * @return - returns the count of elements in a drop down
	 */
	public static int getCountOfDropDownValues(WebElement element) {
		int number = 0;
		try {
			Select select = new Select(element);
			number = select.getOptions().size();
			return number;
		} catch (Exception e) {
			logger.error(e);
		}
		return number;
	}

	/**
	 * @return - returns the Title of a page
	 */
	public static String getTitle() {
		try {
			return driver.getTitle();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * - performs back operation on browser
	 * 
	 */
	public static void navigateToBackWard() {
		Navigation navig = driver.navigate();
		navig.back();
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @return WebElement if identifies else null
	 * 
	 *         - wait till the element gets identified
	 */
	public static WebElement waitForElementToIdentify(String locatortype,
			String locatorvalue) {
		WebElement element = null;
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(15, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class,
							TimeoutException.class);
			element = wait.until(ExpectedConditions.visibilityOf(findobject(
					locatortype, locatorvalue)));
		} catch (Exception e) {
			logger.error("Unexpected error " + e);
		}
		return element;
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @param text
	 * @return
	 * 
	 *         - wait till the element (with expected text) gets identified
	 */
	public static boolean wait_For_Element_To_Display_Having_Text(
			String locatortype, String locatorvalue, String text) {
		Boolean flag = false;
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class,
							TimeoutException.class);
			if (locatortype.equalsIgnoreCase("xpath")) {
				flag = wait.until(ExpectedConditions.textToBePresentInElement(
						By.xpath(locatorvalue), text));
			} else if (locatortype.equalsIgnoreCase("id")) {
				flag = wait.until(ExpectedConditions.textToBePresentInElement(
						By.id(locatorvalue), text));
			} else if (locatortype.equalsIgnoreCase("name")) {
				flag = wait.until(ExpectedConditions.textToBePresentInElement(
						By.id(locatorvalue), text));
			}
		} catch (Exception e) {
			logger.error("Unexpected error " + e);
		}
		return flag;
	}

	/**
	 * - refresh the browser
	 */
	public static void refreshPage() {
		try {
			driver.navigate().refresh();
		} catch (Exception e) {
			logger.error("Unexpected error while refreshing the browser "
					+ e.toString());
		}
	}

	/**
	 * @param element
	 * @return Number or 0
	 * 
	 *         - returns number of rows in a table
	 */
	public static int getCountFromTable(WebElement element) {
		try {
			return element.findElements(By.tagName("tr")).size();
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	/**
	 * @param element
	 * 
	 *            - performs Mouse (Hover) Action on Web Element
	 */
	public static void mouseHoverOnElement(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param element
	 * @param attribute
	 * @return
	 * 
	 */
	public static String getAttributeValue(WebElement element, String attribute) {
		try {
			return element.getAttribute(attribute);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * - switch to Default Window
	 */
	public static void switchToDefaultWindow() {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param i
	 * 
	 *            - Sleep the specific time
	 */
	public static void sleepThread(int i) {
		try {
			Thread.sleep(i * 1000);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @return - returns Current URL
	 */
	public static String getURL() {
		try {
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @return
	 * 
	 *         - find list of child elements associate with parent element
	 */
	public static List<WebElement> findobjects(String locatortype,
			String locatorvalue) {
		List<WebElement> elements = null;
		try {
			if (locatortype.equalsIgnoreCase("name")) {
				elements = driver.findElements(By.name(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("link")) {
				elements = driver.findElements(By.linkText(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("id")) {
				elements = driver.findElements(By.id(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("class")) {
				elements = driver.findElements(By.className(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("cssSelector")) {
				elements = driver.findElements(By.cssSelector(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("xpath")) {
				elements = driver.findElements(By.xpath(locatorvalue));
			}
			return elements;
		} catch (Exception e) {
			logger.error("Unable to identify the element with id "
					+ locatortype + " " + "and value " + locatorvalue);
		}
		return null;
	}

	/**
	 * - Check if Alert window present or not
	 * 
	 * @return
	 */
	public static boolean isAlertWindowDialogPresent() {
		boolean flag = false;
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
			wait.withTimeout(15, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoAlertPresentException.class)
					.ignoring(TimeoutException.class);
			Alert alertWindow = wait.until(ExpectedConditions.alertIsPresent());
			if (alertWindow != null) {
				flag = true;
				return flag;
			}
		} catch (NoAlertPresentException Ex) {
			logger.error("No Alert Window is present " + Ex.toString());
		} catch (TimeoutException e) {
			logger.error("Time out Exception " + e.toString());
		}
		return flag;
	}

	public static int getTextFromTable(WebElement element, int row, int col) {
		int number = 0;
		try {
			number = Integer.parseInt(element.findElement(
					By.xpath(".//tr[" + row + "]/td[" + col + "]")).getText());
			return number;
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	/**
	 * @return
	 */
	public static boolean switchToChildWindow() {
		boolean flag = false;
		try {
			// get the current window handle
			String parentHandle = driver.getWindowHandle();
			// Identify number of Child windows
			Set<String> windows = driver.getWindowHandles();
			int numberOfWindows = windows.size();
			if (numberOfWindows > 1) {
				for (String popUpHandle : windows) {
					if (!popUpHandle.equals(parentHandle)) {
						driver.switchTo().window(popUpHandle);
						driver.close();
						flag = true;
					}
				}
			} else {
				logger.error("No Child windows are present");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean selectDropdownByTextFromList(WebElement element,
			String value) {
		List<WebElement> numberOfLists = null;
		try {
			numberOfLists = element.findElements(By.tagName("li"));
			for (WebElement list : numberOfLists) {
				if (value.equalsIgnoreCase(list.getText())) {
					list.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean PressEnterKey(WebElement element) {
		try {
			element.sendKeys(Keys.ENTER);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickOnElement(WebElement element) {
		try {
			if (element != null) {
				element.click();
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 
	 */
	public static boolean typeKeys(WebElement element, Keys key) {
		boolean flag = false;
		try {
			element.sendKeys(key);
			flag = true;
			return flag;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * @param element
	 *            - POI element
	 * @param xoffset
	 * @param yoffset
	 *            - Used to create POI using X and Y offset values
	 * 
	 */
	public static void createPOI(WebElement element, int xoffset, int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset);
			actions.clickAndHold().release().build().perform();
		} catch (Exception e) {
			logger.error("Unable to create Point Of Interest with x " + xoffset
					+ " Y " + yoffset + " coordinates");
		}
	}

	/**
	 * @param element
	 *            - Way Point element
	 * @param xoffset
	 * @param yoffset
	 *            - Used to create WayPoint
	 */
	public static void createWayPoint(WebElement element, int xoffset,
			int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset);
			actions.clickAndHold().release().perform();
		} catch (Exception e) {
			logger.error("x, y are required");
		}
	}

	public static void routes(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().perform();
		} catch (Exception e) {
			logger.error("no such route found");
		}
	}

	// Alert handling for Safari browser (Click Ok)
	public static void acceptAlertWindowInSafariBrowser() {
		try {
			((JavascriptExecutor) driver)
					.executeScript("window.confirm = function(msg){return true;};");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// Navigate to specific url
	public static void navigateToUrl(String url) {
		try {
			driver.navigate().to(url);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method is used to get the current window handle ID
	 * 
	 * @return
	 */
	public static String getWindowHandle() {
		String window = null;
		try {
			window = driver.getWindowHandle();
		} catch (Exception e) {
			logger.error(e);
		}
		return window;
	}

	/**
	 * This method is used to switch the driver to window having given window
	 * handle ID
	 * 
	 * @param windowHandle
	 */
	public static void switchToWindow(String windowHandle) {
		try {
			driver.switchTo().window(windowHandle);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method is used to highlight the given element
	 * 
	 * @param element
	 */
	public static void highlight(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int i = 0; i <= 5; i++) {
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "color: red; border: 5px solid red;");
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "");
		}
	}

	/**
	 * @param element
	 * @param xoffset
	 * @param yoffset
	 *            - Used to move the element
	 */
	public static void moveElementByCoordinates(WebElement element,
			int xoffset, int yoffset) {
		try {
			// element.click();
			Actions actions = new Actions(driver);
			actions.clickAndHold(element).build().perform();
			actions.moveByOffset(xoffset, yoffset).build().perform();
		} catch (Exception e) {
			logger.error("Unable to move the element with x [" + xoffset
					+ "] and y [" + yoffset + "] coordinates");
		}

	}

	/**
	 * Identify new child window if available then navigate to child window
	 */
	public static void identifyAndNavigateNewWindow() {
		try {
			Set<String> childWindows = driver.getWindowHandles();
			if (childWindows.size() == 1) {
				logger.error("No new window is available");
				return;
			} else if (childWindows.size() == 0) {
				logger.error("No windows are available");
				return;
			} else {
				String currentWindow = driver.getWindowHandle();
				for (String window : childWindows) {
					if (!currentWindow.equalsIgnoreCase(window)) {
						driver.switchTo().window(window);
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param element
	 */
	public static void switchToFrame(WebElement element) {
		try {
			driver.switchTo().frame(element);
		} catch (Exception e) {
			logger.error("Unable to switch to Frame");
		}
	}

	/**
	 * @param element
	 *            This method is used to perform Ctrl+A operation on element
	 */
	public static void selectTextUsingKeys(WebElement element) {
		try {
			element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		} catch (Exception e) {
			logger.error("Unable to perform select operation on given text");
		}

	}

	/**
	 * @param element
	 * @param value
	 * @return Used to select item from drop down when the tag name is -options
	 */
	public static boolean selectOptionFromDropdown(WebElement element,
			String value) {
		List<WebElement> numberOfLists = null;
		try {
			numberOfLists = element.findElements(By.tagName("option"));
			for (WebElement list : numberOfLists) {
				if (value.equalsIgnoreCase(list.getText())) {
					list.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param element
	 *            - Zone creator
	 * @param xoffset
	 * @param yoffset
	 *            - Used to Draw Circles/Zones
	 */
	public static void createZone_In_MapEditor(WebElement element, int xoffset,
			int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset).clickAndHold()
					.moveByOffset(50, 50).release().build().perform();
		} catch (Exception e) {
			logger.error("x, y are required");
		}
	}

	/**
	 * @param xoffset
	 * @param yoffset
	 *            - Used to remove the zone
	 */
	public static void deleteZone_In_MapEditor(int xoffset, int yoffset) {
		try {
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset).clickAndHold().release()
					.click().build().perform();
		} catch (Exception e) {
			logger.error("Unable to remove the Zone with x [" + xoffset
					+ "] and y [" + yoffset + "] coordinates");
		}
	}

	/**
	 * @param locator
	 * @return
	 */
	public static String getTextFromAlertPopup(String locator) {
		String txt = null;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			txt = (String) js.executeScript("return document"
					+ ".getElementsByClassName('" + locator + "')[0]"
					+ ".lastChild.wholeText");
		} catch (Exception e) {
			logger.error("Error while retrieving the text " + e.toString());
		}
		return txt;
	}

	/**
	 * @param element
	 *            -
	 */
	public static void doubleClickUsingActions(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(element).build().perform();
		} catch (Exception e) {
			logger.error("Unexpected error " + e.toString());
		}

	}

	public static void waitUsingJavaScript(int sec) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// js.executeScript("setTimeout(function(){}," + sec * 10000 + ")");
			js.executeAsyncScript("function sleep(milliseconds) "
					+ "{var start = new Date().getTime();for "
					+ "(var i = 0; i < 1e10; i++) {"
					+ "if ((new Date().getTime() - start) > " + sec * 1000
					+ "){break;}}}");

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * - Return text from Alert Window
	 * 
	 * @return
	 */
	public static String getTextFromAlertWindow() {
		String alertText = null;
		try {
			alertText = driver.switchTo().alert().getText();
			return alertText;
		} catch (Exception e) {
			logger.error("Error while retrieving text from Alert Window "
					+ e.toString());
		}
		return alertText;
	}

	/**
	 * @param locatorvalue
	 * 
	 *            - Wait Till the Page Load symbol disappears
	 */
	public static void waitTillPageLoads(String locatorvalue) {
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
			wait.ignoring(NoSuchElementException.class)
					.ignoring(TimeoutException.class)
					.ignoring(StaleElementReferenceException.class)
					.ignoring(WebDriverException.class)
					.pollingEvery(2, TimeUnit.SECONDS)
					.withTimeout(15, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By
					.xpath(locatorvalue)));
		} catch (Exception e) {
			logger.error("Error while loading page " + e.toString());
		}
	}

	public static void clickElementThroughActions(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.clickAndHold(element).release().build().perform();
		} catch (Exception e) {
			logger.error("Error while clicking on Element " + e.toString());
		}

	}

	/**
	 * @param element
	 *            - Element on which ClickAndHold operation is being performed
	 */
	public static void perform_clickAndHold_On_Element(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.clickAndHold(element).build().perform();
		} catch (Exception e) {
			logger.error("Unexpected error while performing click On Hold "
					+ e.toString());
		}
	}

	/**
	 * @param element
	 *            - Drop down element
	 * @return
	 */
	public static String get_Default_Item_In_Dropdown(WebElement element) {
		try {
			Select select = new Select(element);
			String Selected_Option_Text = select.getFirstSelectedOption()
					.getText();
			return Selected_Option_Text;
		} catch (Exception e) {
			logger.error("Error while getting default selected item from Dropdown "
					+ e.toString());
		}
		return null;
	}

	/**
	 * @param element
	 *            - WebElement to perform click
	 */
	public static void click_Using_JavaScript(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			logger.error("Error while doing click operation with JavaScript "
					+ e.toString());
		}
	}

	/**
	 * This Method is used to Click on OK button on Alert Window
	 * 
	 */
	public static void accept_AlertWindow_Using_JavaScript() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.confirm=function(msg){return true;}", "");
		} catch (Exception e) {
			logger.error("Error while doing click on OK button in Alert window "
					+ "using java Script " + e.toString());
		}
	}

	/**
	 * @param i
	 *            - Integer number to perform substring operation on given
	 *            String
	 * @return
	 */
	public static String perform_SubString_And_Trim(String actual_text, int i) {
		String converted_text = null;
		try {
			converted_text = actual_text.substring(1).trim();
		} catch (Exception e) {
			logger.error("Error while doing String operations..."
					+ e.toString());
		}
		return converted_text;
	}
}
