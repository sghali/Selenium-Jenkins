package com.phunware.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.phunware.constants.GlobalConstants;
import com.phunware.jaxb.entity.Testcase;
import com.phunware.jaxb.entity.Testcase.Case;
import com.phunware.jaxb.entity.Testcase.Case.Param;
import com.phunware.util.JaxbUtil;
import com.phunware.util.ReportUtils;
import com.phunware.util.SeleniumUtils;
import com.phunware.util.SoftAssert;

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

/**
 * @author bhargavas
 * @since 07/29/2013 This Class is for login into application
 * 
 */

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class Login extends Suite {
	private static Logger logger = Logger.getLogger(Login.class);
	private static String childSuite = "login";
	private static boolean suiteExecution = false;
	private static WebElement element = null;
	private static boolean isTextMatching = false;
	private static List<String> testcaseList = new ArrayList<String>();
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private Testcase loginSuite = null;
	private List<Case> testcases = null;
	private SoftAssert m_assert;

	/**
	 * this method performs launch browser operation
	 */
	@BeforeClass
	public void setUp() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Index Page Description for Results
		ReportUtils.setIndexPageDescription();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the Test suite is added for execution
		for (String testSuite : scenarioslist) {
			if (childSuite.equalsIgnoreCase(testSuite)) {
				suiteExecution = true;
				break;
			}
		}
		if (!suiteExecution) {
			logger.warn("Test suite [Login] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [Login] is not added for execution", false);
			throw new SkipException(
					"Test suite [Login] is not added for execution");
		}
		// reading Login input file
		logger.info("reading Login Input file");
		loginSuite = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH + GlobalConstants.LOGIN_FILE,
				Testcase.class);
		if (loginSuite != null) {
			testcases = loginSuite.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());
				}
			}
		}
		if (testcaseList.size() == 0) {
			logger.warn("No TestCase added for execution in [Login] suite");
			ReportUtils.setStepDescription(
					"No TestCase added for execution in [Login] suite", false);
			throw new SkipException(
					"No TestCase added for execution in [Login] suite");
		}
		logger.info("reading of [Login] input file is successful");
		logger.info("The testcases for execution in [Login] suite is "
				+ testcaseList);
		logger.info("opening [" + configproperties.get(0) + "] browser");
		boolean isOpened = SeleniumUtils.launchBrowser();
		if (!isOpened) {
			logger.error("Error while launching " + configproperties.get(0)
					+ " browser");
			ReportUtils.setStepDescription("Error while launching "
					+ configproperties.get(0) + " browser", true);
			m_assert.assertTrue(isOpened, "Error while launching "
					+ configproperties.get(0) + " browser");
		}
		m_assert.assertAll();
	}

	/**
	 * Login functionality for invalid user credentials.
	 */

	@Test(priority = 0)
	public void verifyLoginForInvalidUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if the verifyLoginForInvalidUser is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyLoginForInvalidUser")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyLoginForInvalidUser] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyLoginForInvalidUser] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyLoginForInvalidUser] is not added for execution");
		}
		// reading the parameters from input xml file
		testcaseArgs = getTestData("verifyLoginForInvalidUser");
		// Identify Username and Password
		logger.info("Identify Username and Password fields");
		WebElement userelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatorvalue());
		if (userelement == null) {
			logger.error("Unable to identify Username text field");
			ReportUtils.setStepDescription(
					"Unable to identify Username text field", true);
			m_assert.fail("Unable to identify Username text field");
		}
		// SeleniumUtils.highlight(userelement);
		// clear the username textbox
		SeleniumUtils.clearText(userelement);
		WebElement passwordelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatorvalue());
		if (passwordelement == null) {
			logger.error("Unable to identify Password text field");
			ReportUtils.setStepDescription(
					"Unable to identify Password text field", true);
			m_assert.fail("Unable to identify Password text field");
		}
		// clean the password textbox
		SeleniumUtils.clearText(passwordelement);
		// Identify Sign in button
		logger.info("Identify Sign in button");
		WebElement signinButton = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SigninButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SigninButton")
								.getLocatorvalue());
		if (signinButton == null) {
			logger.error("Unable to identify Sign in button");
			ReportUtils.setStepDescription("Unable to identify Sign in button",
					true);
			m_assert.fail("Unable to identify Sign in button");
		}
		// Login into application
		SeleniumUtils.login(userelement, passwordelement, signinButton,
				testcaseArgs.get("username"), testcaseArgs.get("password"));
		logger.info("Verify the error message for invalid credentials");
		SeleniumUtils.sleepThread(4);
		WebElement errorelement = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ErrorMessage_Invalid")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ErrorMessage_Invalid")
						.getLocatorvalue());
		// Get the actual Text of the error message
		String ActualErrorTextForInvalidLogin = SeleniumUtils
				.getText(errorelement);
		// Get the Expected text
		String ExpectedErrorTextForInvalidLogin = Suite.objectRepositoryMap
				.get("ErrorMessage_Invalid").getErrortext();
		// Compare the texts
		if (configproperties.get(0).equalsIgnoreCase("IE")) {
			isTextMatching = SeleniumUtils.assertEqual(
					ActualErrorTextForInvalidLogin, "× Invalid credentials.");
		} else {
			isTextMatching = SeleniumUtils.assertEqual(
					ActualErrorTextForInvalidLogin,
					ExpectedErrorTextForInvalidLogin);
		}
		if (!isTextMatching) {
			logger.error("The Return text [" + ActualErrorTextForInvalidLogin
					+ "]" + " " + "and" + " " + "The Expected text ["
					+ ExpectedErrorTextForInvalidLogin + "]" + " "
					+ "are not equal");
			ReportUtils
					.setStepDescription(
							"Fails at identification of Error message for invalid credentials",
							testcaseArgs.get("username") + "/"
									+ testcaseArgs.get("password"),
							ExpectedErrorTextForInvalidLogin,
							ActualErrorTextForInvalidLogin, true);
			m_assert.assertEquals(ActualErrorTextForInvalidLogin,
					ExpectedErrorTextForInvalidLogin);
		}
		m_assert.assertAll();
	}

	/**
	 * @param username
	 * @param password
	 *            this method performs login operation for valid credentials.
	 */
	@Test(priority = 1)
	public void verifyLoginForValidUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyLoginForValidUser")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyLoginForValidUser] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyLoginForValidUser] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyLoginForValidUser] is not added for execution");
		}
		// reading input data
		testcaseArgs = getTestData("verifyLoginForValidUser");
		logger.info("Login into the application with username  "
				+ testcaseArgs.get("username") + " " + "and password  "
				+ testcaseArgs.get("password"));
		logger.info("Identification of Username and Password");
		// Identification of Username and Password
		WebElement userelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatorvalue());
		if (userelement == null) {
			logger.error("Unable to identify Username text field");
			ReportUtils.setStepDescription(
					"Unable to identify Username text field", true);
			m_assert.fail("Unable to identify Username text field");
		}
		// clear the username textbox
		SeleniumUtils.clearText(userelement);
		WebElement passwordelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatorvalue());
		if (passwordelement == null) {
			logger.error("Unable to identify Password text field");
			ReportUtils.setStepDescription(
					"Unable to identify Password text field", true);
			m_assert.fail("Unable to identify Password text field");
		}
		// clean the password textbox
		SeleniumUtils.clearText(passwordelement);
		// Identify Sign in button
		logger.info("Identify Sign in button");
		WebElement signinButton = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SigninButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SigninButton")
								.getLocatorvalue());
		if (signinButton == null) {
			logger.error("Unable to identify Sign in button");
			ReportUtils.setStepDescription("Unable to identify Sign in button",
					true);
			m_assert.fail("Unable to identify Sign in button");
		}
		// Login into application
		SeleniumUtils.login(userelement, passwordelement, signinButton,
				testcaseArgs.get("username"), testcaseArgs.get("password"));
		SeleniumUtils.sleepThread(4);
		// Identify Initial Page header text
		logger.info("Identification of Initial Page Header element");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		// If Initial page header element is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Initial Page] Header Element");
			ReportUtils.setStepDescription(
					"Unable to identify [Initial Page] Header Element", true);
			m_assert.fail("Unable to identify [Initial Page] Header Element");
		}
		// Get the text of the header element
		String InitialHeaderText = SeleniumUtils.getText(element);
		// Get the Expected text
		String ExpectedInitialHeaderText = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InitialHeaderText,
				ExpectedInitialHeaderText);
		if (!isTextMatching) {
			logger.info("User is landed on [" + InitialHeaderText + "] page");
			logger.info("Navigate to [" + ExpectedInitialHeaderText + "] page");
			logger.info("Verify if User has Switch button ");
			// Identify Switch element
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("SwitchBtn")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("SwitchBtn")
									.getLocatorvalue());
			// If Switch element is null then throw the error and exit
			if (element == null) {
				logger.error("Unable to identify [Switch Button]");
				ReportUtils.setStepDescription(
						"Unable to identify [Switch Button]", true);
				m_assert.fail("Unable to identify [Switch Button]");
			}
			// Click on Switch button
			logger.info("Click on [Switch Button]");
			boolean isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Switch Button]");
				ReportUtils.setStepDescription(
						"Unable to click on [Switch Button]", true);
				m_assert.fail("Unable to click on [Switch Button]");
			}
			// Enter organization name in text box
			logger.info("Identify Switch button - Organization text box");
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("SwitcBtnOrganizationTextbox")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("SwitcBtnOrganizationTextbox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Switch button Organization textbox is not present. So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
				ReportUtils.setStepDescription(
						"Switch button Organization textbox is not present. So unable to move to ["
								+ testcaseArgs.get("organization")
								+ "] Organization", true);
				m_assert.fail("Switch button Organization textbox is not present. So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
			}
			logger.info("Enter the Organization ["
					+ testcaseArgs.get("organization")
					+ "] name in the Switch btn Organization text box");
			// Enter the organization name in text box
			SeleniumUtils.type(element, testcaseArgs.get("organization"));
			SeleniumUtils.sleepThread(3);
			// Switch to organization as per the browser
			if (configproperties.get(0).equalsIgnoreCase("FIREFOX")
					|| configproperties.get(0).equalsIgnoreCase("IE")) {
				// Identify the Organization dropdown
				WebElement orgListbox = SeleniumUtils.findobject(
						Suite.objectRepositoryMap.get(
								"SwitcBtnOrganizationDropdown")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("SwitcBtnOrganizationDropdown")
								.getLocatorvalue());
				// If Organization dropdown is null then throw the error and
				// exit
				if (orgListbox == null) {
					logger.error("Switch button Organization dropdown is not present. "
							+ "So unable to move to Organization");
					ReportUtils
							.setStepDescription(
									"Switch button Organization dropdown is not present. "
											+ "So unable to move to Organization",
									true);
					m_assert.fail("Switch button Organization dropdown is not present. "
							+ "So unable to move to Organization");
				}
				// Click on Organization dropdown
				isClicked = SeleniumUtils.clickOnElement(orgListbox);
				if (!isClicked) {
					logger.error("Unable to click on organization in a list");
					ReportUtils.setStepDescription(
							"Unable to click on organization in a list", true);
					m_assert.fail("Unable to click on organization in a list");
				}
			} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
					|| configproperties.get(0).equalsIgnoreCase("SAFARI")) {
				element.sendKeys(Keys.ENTER);
				SeleniumUtils.sleepThread(4);
			}
			// Verify the landing Page
			logger.info("Verify if user is landed on [Your Applications] page");
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Login failed : Unabel to identify [Your Applications] header element");
				ReportUtils
						.setStepDescription(
								"Login failed : Unabel to identify [Your Applications] header element",
								true);
				m_assert.fail("Login failed : Unabel to identify [Your Applications] header element");
			}
			// Get the text
			String landingPageText = SeleniumUtils.getText(element);
			// Get the actual text
			String ExpLandingPageText = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare the both texts
			isTextMatching = SeleniumUtils.assertEqual(landingPageText,
					ExpLandingPageText);
			if (!isTextMatching) {
				logger.error("Login failed. User is not landed on [Your Applications] page");
				ReportUtils
						.setStepDescription(
								"Login failed. User is not landed on [Your Applications] page",
								true);
				m_assert.fail("Login failed. User is not landed on [Your Applications] page");
			}
		}
		m_assert.assertAll();
	}

	/**
	 * @since 07/25/2013 This method is to send the input data to the Test
	 *        method
	 * @return
	 */

	public Map<String, String> getTestData(String testcase) {
		if (loginSuite != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			testcases = loginSuite.getCase();
			for (Case thisCase : testcases) {
				String runMode = thisCase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)
						&& testcase.equalsIgnoreCase(thisCase.getName())) {
					List<Param> paramList = thisCase.getParam();
					for (Param param : paramList) {
						testcasesMap.put(param.getId(), param.getValue());
					}
				}
			}
			return testcasesMap;
		}
		return null;
	}

	/**
	 * this method performs sign out operation
	 */
	@AfterClass
	public void tearDown() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		logger.info("Identify Sign Out button");
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SignoutButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SignoutButton")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Sign Out button");
			ReportUtils.setStepDescription(
					"Unable to identify Sign Out button", false);
			m_assert.fail("Unable to identify Sign Out button");
		}
		// Click on Sign Out button
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on Sign Out button");
			ReportUtils.setStepDescription(
					"Unable to click on Sign Out button", false);
			m_assert.fail("Unable to identify Sign Out button");
		}
		SeleniumUtils.closeBrowser();
		m_assert.assertAll();
	}
}
