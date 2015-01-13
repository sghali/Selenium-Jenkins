package com.phunware.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.phunware.constants.GlobalConstants;
import com.phunware.jaxb.entity.Testcase;
import com.phunware.jaxb.entity.Testcase.Case;
import com.phunware.jaxb.entity.Testcase.Case.Param;
import com.phunware.util.JaxbUtil;
import com.phunware.util.ReportUtils;
import com.phunware.util.SeleniumCustomUtils;
import com.phunware.util.SeleniumUtils;
import com.phunware.util.SoftAssert;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class CommonScenarios extends Suite {
	private static Logger logger = Logger.getLogger(CommonScenarios.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static boolean isTextMatching;
	private static WebElement element = null;
	private static String childSuite = "commonscenarios";
	private static boolean suiteExecution = false;
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private static boolean isClicked;
	private Testcase commonscenariosSuite = null;
	private List<Case> testcases = null;
	private SoftAssert m_assert;

	/**
	 * Read the commonscenarios input xml file and add the name of the test
	 * cases into testcaseList.
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
			logger.warn("Test suite [CommonScenarios] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [CommonScenarios] is not added for execution",
					false);
			throw new SkipException(
					"Test suite [CommonScenarios] is not added for execution");
		}
		// Reading PhunwareClient input file
		logger.info("reading [CommonScenarios] Input file");
		// Unmarshal the xml file
		commonscenariosSuite = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.COMMONSCENARIOS_FILE, Testcase.class);
		// check if nodes are present
		if (commonscenariosSuite != null) {
			// take test cases into List
			testcases = commonscenariosSuite.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				// check runmode and add the cases into list
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());
				}
			}
		}
		// check the size of the cases
		if (testcaseList.size() == 0) {
			logger.warn("No TestCase added for execution in [CommonScenarios]");
			ReportUtils
					.setStepDescription(
							"No TestCase added for execution in [CommonScenarios] suite",
							false);
			throw new SkipException(
					"No TestCase added for execution in [CommonScenarios]");
		}
		logger.info("reading [CommonScenarios] input file successful");
		logger.info("The testcases for execution in [CommonScenarios]"
				+ testcaseList);
		m_assert.assertAll();
	}

	/**
	 * This method login into the application as per the input
	 */
	@Test(priority = 1)
	public void loginWithGoogleAccount() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check loginAs Test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("loginWithGoogleAccount")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [loginWithGoogleAccount] is not added for execution"
					+ " in [CommonScenarios]");
			ReportUtils.setStepDescription(
					"Test case [loginWithGoogleAccount] is not added for execution"
							+ " in [CommonScenarios]", false);
			throw new SkipException(
					"Test case [loginWithGoogleAccount] is not added for execution"
							+ " in [CommonScenarios]");
		}
		// read the params data
		testcaseArgs = getTestData("loginWithGoogleAccount");
		// Opening the browser
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
		// Get the current window handle
		String currentWindowHandleId = SeleniumUtils.getWindowHandle();
		// Identify Sign in with Google
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("SignInWithGoogle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("SignInWithGoogle")
						.getLocatorvalue());
		// If Landing page header element is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Sign in with Google] Link");
			ReportUtils.setStepDescription(
					"Unable to identify [Sign in with Google] Link", true);
			m_assert.fail("Unable to identify [Sign in with Google] Link");
		}
		// Click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify New window and Navigate to new child window
		SeleniumUtils.identifyAndNavigateNewWindow();
		// Identify Gmail Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("SignInGmail_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("SignInGmail_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String GmailHeaderText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpGmailHeaderText = Suite.objectRepositoryMap.get(
				"SignInGmail_HeaderElement").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(GmailHeaderText,
				ExpGmailHeaderText);
		if (!isTextMatching) {
			logger.error("[Gmail header] text matching failed:"
					+ " The expected text is [" + ExpGmailHeaderText
					+ "] and the actual return text is [" + GmailHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"[Gmail header] text matching failed", "",
					ExpGmailHeaderText, GmailHeaderText, true);
			m_assert.fail("[Gmail header] text matching failed:"
					+ " The expected text is [" + ExpGmailHeaderText
					+ "] and the actual return text is [" + GmailHeaderText
					+ "]");
		}
		// Identify Current Email id Element
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("CurrentEmail")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CurrentEmail").getLocatorvalue());
		if (element == null) {
			// Identify Gmail UserName
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("GmailUserName")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("GmailUserName")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Gmail - UserName text box]");
				ReportUtils.setStepDescription("Unable to identify "
						+ "[Gmail - UserName text box]", true);
				m_assert.fail("Unable to identify [Gmail - UserName text box]");
			}
			// Type UserName
			SeleniumUtils.type(element, testcaseArgs.get("gmailusername"));
			// Identify Password
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("GmailPassword")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("GmailPassword")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to Identify [Password text box]"
						+ " link in Gmail");
				ReportUtils.setStepDescription(
						"Unable to Identify [Password text box]"
								+ " link in Gmail", true);
				m_assert.fail("Unable to Identify [Password text box]"
						+ " link in Gmail");
			}
			// Type password
			SeleniumUtils.type(element, testcaseArgs.get("gmailpassword"));
			// Identify Sign In button
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("GmailSignin")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("GmailSignin")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to Identify [Sign In button]"
						+ " in Gmail");
				ReportUtils.setStepDescription(
						"Unable to Identify [Sign In button]" + " in Gmail",
						true);
				m_assert.fail("Unable to Identify [Sign In button]"
						+ " in Gmail");
			}
			// Click on Sign in
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		} else {
			// Get the text
			String CurrentEmailText = SeleniumUtils.getText(element);
			// Get the Exp text
			String ExpCurrentEmailText = Suite.objectRepositoryMap.get(
					"CurrentEmail").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(GmailHeaderText,
					ExpCurrentEmailText);
			if (!isTextMatching) {
				logger.info("The current Email id is [" + CurrentEmailText
						+ "]");
				logger.info("Navigate to [" + ExpGmailHeaderText + "]");
				// Identify Sign in with a different account
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("SignInWithDifferentAct")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("SignInWithDifferentAct")
								.getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Sign in with different user]"
							+ " link in Gmail");
					ReportUtils.setStepDescription("Unable to Identify"
							+ " [Sign in with different user] link in Gmail",
							true);
					m_assert.fail("Unable to Identify [Sign in with different user]"
							+ " link in Gmail");
				}
				// Click
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(3);
				// Identify Add Account link
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("AddAccountLink")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AddAccountLink").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Add Account]"
							+ " link in Gmail");
					ReportUtils.setStepDescription("Unable to Identify"
							+ " [Add Account] link in Gmail", true);
					m_assert.fail("Unable to Identify [Add Account]"
							+ " link in Gmail");
				}
				// Click
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(4);
				// Identify Gmail UserName
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailUserName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailUserName").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to identify [Gmail - UserName text box]");
					ReportUtils.setStepDescription("Unable to identify "
							+ "[Gmail - UserName text box]", true);
					m_assert.fail("Unable to identify [Gmail - UserName text box]");
				}
				// Type UserName
				SeleniumUtils.type(element, testcaseArgs.get("gmailusername"));
				// Identify Password
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailPassword")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailPassword").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Password text box]"
							+ " link in Gmail");
					ReportUtils.setStepDescription(
							"Unable to Identify [Password text box]"
									+ " link in Gmail", true);
					m_assert.fail("Unable to Identify [Password text box]"
							+ " link in Gmail");
				}
				// Type password
				SeleniumUtils.type(element, testcaseArgs.get("gmailpassword"));
				// Identify Sign In button
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailSignin")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailSignin").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Sign In button]"
							+ " in Gmail");
					ReportUtils
							.setStepDescription(
									"Unable to Identify [Sign In button]"
											+ " in Gmail", true);
					m_assert.fail("Unable to Identify [Sign In button]"
							+ " in Gmail");
				}
				// Click on Sign in
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(4);
			} else {
				// Identify Password input field
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailPassword")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailPassword").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Password text box]"
							+ " link in Gmail");
					ReportUtils.setStepDescription(
							"Unable to Identify [Password text box]"
									+ " link in Gmail", true);
					m_assert.fail("Unable to Identify [Password text box]"
							+ " link in Gmail");
				}
				// Type password
				SeleniumUtils.type(element, testcaseArgs.get("gmailpassword"));
				// Identify Submit button
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailSignin")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailSignin").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Sign In button]"
							+ " in Gmail");
					ReportUtils
							.setStepDescription(
									"Unable to Identify [Sign In button]"
											+ " in Gmail", true);
					m_assert.fail("Unable to Identify [Sign In button]"
							+ " in Gmail");
				}
				// Click on Sign in
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(4);
			}
		}
		SeleniumUtils.sleepThread(3);
		// Navigate to Default Window
		SeleniumUtils.switchToWindow(currentWindowHandleId);
		logger.info("Verify the landing page after login");
		SeleniumUtils.sleepThread(5);
		// Identify Landing page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		// If Landing page header element is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Initial Page] Header Element");
			ReportUtils.setStepDescription(
					"Unable to identify [Initial Page] Header Element", true);
			m_assert.fail("Unable to identify [Initial Page] Header Element");
		}
		// Get the text of the header element
		String InitialHeaderText = SeleniumUtils.getText(element);
		// Get the Expected text
		String ExpInitialHeaderText = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InitialHeaderText,
				ExpInitialHeaderText);
		if (!isTextMatching) {
			logger.error("User is not landed on [Your Applications] page");
			ReportUtils.setStepDescription(
					"User is not landed on [Your Applications] page", true);
			m_assert.fail("User is not landed on [Your Applications] page");
		}
		// Click on Sign Out button
		// Identify Clients tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("ClientTablogo")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTablogo")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts] tab");
			ReportUtils.setStepDescription("Unable to identify [Accounts] tab",
					true);
			m_assert.fail("Unable to identify [Accounts] tab");
		}
		// Click on Clients tab
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [Accounts] tab");
			ReportUtils.setStepDescription("Unable to click on [Accounts] tab",
					true);
			m_assert.fail("Unable to click on [Accounts] tab");
		}
		SeleniumUtils.sleepThread(5);
		// Identify Sign Out button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts tab - Sign Out] button");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Accounts tab - Sign Out] button",
							true);
			m_assert.fail("Unable to identify [Accounts tab - Sign Out] button");
		}
		logger.info("Click on Sign Out button");
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click [Accounts tab - Sign Out] button");
			ReportUtils.setStepDescription(
					"Unable to click [Accounts tab - Sign Out] button", true);
			m_assert.fail("Unable to click [Accounts tab - Sign Out] button");
		}
		SeleniumUtils.sleepThread(4);
		// Identify Login page text
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("LoginPageText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("LoginPageText").getLocatorvalue());
		if (element == null) {
			logger.error("Sign Out unsuccessful : Unable to identify Login page elements");
			ReportUtils
					.setStepDescription(
							"Sign Out unsuccessful : Unable to identify Login page elements",
							true);
			m_assert.fail("Sign Out unsuccessful : Unable to identify Login page elements");
		}
		// Get the text
		String LoginText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpLoginText = Suite.objectRepositoryMap.get("LoginPageText")
				.getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(LoginText, ExpLoginText);
		if (!isTextMatching) {
			logger.error("Sign Out fails : The expected text in Login page is ["
					+ ExpLoginText
					+ "] and the actual return text is ["
					+ LoginText + "]");
			ReportUtils.setStepDescription("Sign Out fails", "", ExpLoginText,
					LoginText, true);
			m_assert.assertEquals(LoginText, ExpLoginText);
		}
		// Close the browser
		SeleniumUtils.closeBrowser();
		m_assert.assertAll();
	}

	@Test(priority = 2)
	public void validateInActiveUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check loginAs Test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validateInActiveUser")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [validateInActiveUser] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [validateInActiveUser] is not added for execution"
					+ " in [CommonScenarios]");
			ReportUtils.setStepDescription(
					"Test case [validateInActiveUser] is not added for execution"
							+ " in [CommonScenarios]", false);
			throw new SkipException(
					"Test case [validateInActiveUser] is not added for execution"
							+ " in [CommonScenarios]");
		}
		// read the params data
		testcaseArgs = getTestData("validateInActiveUser");
		// Opening the browser
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
		// Identify the username and password fields
		logger.info("Identify Username, Password & Signin buttons");
		WebElement userelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatorvalue());
		WebElement passwordelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatorvalue());
		WebElement buttonelement = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SigninButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SigninButton")
								.getLocatorvalue());
		// If username and password empty then throw exception...
		if (userelement == null || passwordelement == null
				|| buttonelement == null) {
			logger.error("Unable to identify username or password or signin elements");
			ReportUtils
					.setStepDescription(
							"Unable to identify username or password or signin elements",
							true);
			m_assert.fail("Unable to identify username or password or signin elements");
		}
		// Login into the application with username and password
		logger.info("login into the application");
		SeleniumUtils.login(userelement, passwordelement, buttonelement,
				testcaseArgs.get("username"), testcaseArgs.get("password"));
		logger.info("Verify the landing page after login");
		SeleniumUtils.sleepThread(5);
		// Identify Landing page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		// If Landing page header element is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Initial Page] Header Element");
			ReportUtils.setStepDescription(
					"Unable to identify [Initial Page] Header Element", true);
			m_assert.fail("Unable to identify [Initial Page] Header Element");
		}
		// Get the text of the header element
		String InitialHeaderText = SeleniumUtils.getText(element);
		// Get the Expected text
		String ExpInitialHeaderText = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InitialHeaderText,
				ExpInitialHeaderText);
		if (isTextMatching) {
			logger.info("User is on [Your Applications] page");
			// Identify Company name
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientTabOrganizationName")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabOrganizationName")
							.getLocatorvalue());
			// Get the text
			String organizationName = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(organizationName,
					testcaseArgs.get("organization"));
			if (!isTextMatching) {
				logger.info("User is landed on [Your Applications] page of "
						+ organizationName + " organization");
				logger.info("Navigate to " + testcaseArgs.get("organization")
						+ " organization");
				// Identify Switch element
				element = SeleniumUtils.findobject(Suite.objectRepositoryMap
						.get("SwitchBtn").getLocatortype(),
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
				isClicked = SeleniumUtils.clickOnElement(element);
				if (!isClicked) {
					logger.error("Unable to click on [Switch Button]");
					ReportUtils.setStepDescription(
							"Unable to click on [Switch Button]", true);
					m_assert.fail("Unable to click on [Switch Button]");
				}
				logger.info("Click operation on [Switch Button] is successful");
				// Enter organization name in text box
				logger.info("Identify Switch button - Organization text box");
				SeleniumUtils.sleepThread(1);
				element = SeleniumUtils
						.findobject(
								Suite.objectRepositoryMap.get(
										"SwitcBtnOrganizationTextbox")
										.getLocatortype(),
								Suite.objectRepositoryMap.get(
										"SwitcBtnOrganizationTextbox")
										.getLocatorvalue());
				if (element == null) {
					logger.error("Switch button Organization textbox is not present. "
							+ "So unable to move to ["
							+ testcaseArgs.get("organization")
							+ "] Organization");
					ReportUtils.setStepDescription(
							"Switch button Organization textbox is not present. "
									+ "So unable to move to ["
									+ testcaseArgs.get("organization")
									+ "] Organization", true);
					m_assert.fail("Switch button Organization textbox is not present. "
							+ "So unable to move to ["
							+ testcaseArgs.get("organization")
							+ "] Organization");
				}
				logger.info("Enter the Organization ["
						+ testcaseArgs.get("organization")
						+ "] name in the Switch btn Organization text box");
				// Enter the organization name in text box
				SeleniumUtils.type(element, testcaseArgs.get("organization"));
				SeleniumUtils.sleepThread(3);
				// Switch to organization as per the browser
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					// Identify the Organization dropdown
					WebElement orgListbox = SeleniumUtils.findobject(
							Suite.objectRepositoryMap.get(
									"SwitcBtnOrganizationDropdown")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"SwitcBtnOrganizationDropdown")
									.getLocatorvalue());
					// If Organization dropdown is null then throw the error and
					// exit
					if (orgListbox == null) {
						logger.error("Switch button Organization dropdown is not present. "
								+ "So unable to move to Organization");
						ReportUtils.setStepDescription(
								"Switch button Organization dropdown is not present. "
										+ "So unable to move to Organization",
								false);
						m_assert.fail("Switch button Organization dropdown is not present. "
								+ "So unable to move to Organization");
					}
					// Click on Organization dropdown
					isClicked = SeleniumUtils.clickOnElement(orgListbox);
					if (!isClicked) {
						logger.error("Unable to click on organization in a list");
						ReportUtils.setStepDescription(
								"Unable to click on organization in a list",
								true);
						m_assert.fail("Unable to click on organization in a list");

					}
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
						|| configproperties.get(0).equalsIgnoreCase("SAFARI")
						|| configproperties.get(0).equalsIgnoreCase("IE")) {
					element.sendKeys(Keys.ENTER);
					SeleniumUtils.sleepThread(4);
				}
				// Verify the landing Page
				logger.info("Verify if user is landed on [Your Applications] page");
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatortype(),
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatorvalue());
				if (element == null) {
					logger.error("Login failed : Unabel to identify [Your Applications]"
							+ " header element");
					ReportUtils.setStepDescription(
							"Login failed : Unabel to identify [Your Applications]"
									+ " header element", true);
					m_assert.fail("Login failed : Unabel to identify [Your Applications]"
							+ " header element");
				}
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
		} else {
			logger.info("User is landed on [" + InitialHeaderText + "] page");
			logger.info("Navigate to [" + ExpInitialHeaderText + "] page");
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
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Switch Button]");
				ReportUtils.setStepDescription(
						"Unable to click on [Switch Button]", true);
				m_assert.fail("Unable to click on [Switch Button]");
			}
			logger.info("Click operation on [Switch Button] is successful");
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
				logger.error("Switch button Organization textbox is not present. "
						+ "So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
				ReportUtils.setStepDescription(
						"Switch button Organization textbox is not present. "
								+ "So unable to move to ["
								+ testcaseArgs.get("organization")
								+ "] Organization", true);
				m_assert.fail("Switch button Organization textbox is not present. "
						+ "So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
			}
			logger.info("Enter the Organization ["
					+ testcaseArgs.get("organization")
					+ "] name in the Switch btn Organization text box");
			// Enter the organization name in text box
			SeleniumUtils.type(element, testcaseArgs.get("organization"));
			SeleniumUtils.sleepThread(3);
			// Switch to organization as per the browser
			if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
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
					ReportUtils.setStepDescription(
							"Switch button Organization dropdown is not present. "
									+ "So unable to move to Organization",
							false);
					m_assert.fail("Switch button Organization dropdown is not present. "
							+ "So unable to move to Organization");
				}
				// Click on Organization dropdown
				isClicked = SeleniumUtils.clickOnElement(orgListbox);
				if (!isClicked) {
					logger.error("Unable to select on organization in a list");
					ReportUtils.setStepDescription(
							"Unable to select on organization in a list", true);
					m_assert.fail("Unable to select on organization in a list");

				}
			} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
					|| configproperties.get(0).equalsIgnoreCase("SAFARI")
					|| configproperties.get(0).equalsIgnoreCase("IE")) {
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
				logger.error("Login failed : Unabel to identify [Your Applications]"
						+ " header element");
				ReportUtils.setStepDescription(
						"Login failed : Unabel to identify [Your Applications]"
								+ " header element", true);
				m_assert.fail("Login failed : Unabel to identify [Your Applications]"
						+ " header element");
			}
			String landingPageText = SeleniumUtils.getText(element);
			// Get the actual text
			String ExpLandingPageText = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare the both texts
			isTextMatching = SeleniumUtils.assertEqual(landingPageText,
					ExpLandingPageText);
			if (!isTextMatching) {
				logger.error("Login failed. User is not landed on "
						+ "[Your Applications] page");
				ReportUtils.setStepDescription(
						"Login failed. User is not landed on "
								+ "[Your Applications] page", true);
				m_assert.fail("Login failed. User is not landed on "
						+ "[Your Applications] page");
			}
		}
		// Verify if user is on [Add User] page
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		// Get the text
		String UsersAndRolestabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpUsersAndRolestabText = Suite.objectRepositoryMap.get(
				"ClientTabUsersAndRoles").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(UsersAndRolestabText,
				ExpUsersAndRolestabText);
		if (!isTextMatching) {
			logger.error("[Users & Roles] tab text matching failed. Expected text is ["
					+ ExpUsersAndRolestabText
					+ "] and the return text is ["
					+ UsersAndRolestabText + "]");
			ReportUtils.setStepDescription(
					"[Users & Roles] tab text matching failed", "",
					ExpUsersAndRolestabText, UsersAndRolestabText, true);
			m_assert.fail("[Users & Roles] tab text matching failed. Expected text is ["
					+ ExpUsersAndRolestabText
					+ "] and the return text is ["
					+ UsersAndRolestabText + "]");
		}
		// Click on [Users & Roles]
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// click on New User Button
		logger.info("Click on New User to create new user account");
		// Identify New User button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatorvalue());
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on New User button");
			ReportUtils.setStepDescription(
					"Unable to click on New User button", false);
			m_assert.fail("Unable to identify New User button");
		}
		SeleniumUtils.sleepThread(5);
		// First Name text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		// Enter first name
		SeleniumUtils.type(element, testcaseArgs.get("firstName"));
		// Last Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatorvalue());
		// Enter Last name
		SeleniumUtils.type(element, testcaseArgs.get("lastName"));
		// Identify Email
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		// Enter Email
		SeleniumUtils.type(element, testcaseArgs.get("email"));
		// Identify Password
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		// Enter password
		SeleniumUtils.type(element, testcaseArgs.get("password"));
		// Identify Time zone link
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserCurrentLocationTimeZoneLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserCurrentLocationTimeZoneLink")
						.getLocatorvalue());
		SeleniumUtils.scrollDown();
		// Identify User Active Status dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserInactiveStatusDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserInactiveStatusDropdown")
						.getLocatorvalue());
		// Select Inactive from drop down
		boolean isSelected = SeleniumUtils.selectDropdownByTextFromList(
				element, "Inactive");
		if (!isSelected) {
			logger.error("Unable to select Active/Inactive from drop down");
			ReportUtils.setStepDescription(
					"Unable to select Active/Inactive from drop down", true);
			m_assert.fail("Unable to select Active/Inactive from drop down");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on Save button");
			ReportUtils.setStepDescription("Unable to click on Save button",
					true);
			m_assert.fail("Unable to click on Save button");
		}
		logger.info("Validate the success message");
		SeleniumUtils.sleepThread(3);
		// Identify Success Message or Email Already taken error message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"SaveUserMsgOrEmailAlreadyTakenMsg").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SaveUserMsgOrEmailAlreadyTakenMsg").getLocatorvalue());
		// Get the text
		String Message = SeleniumUtils.getText(element);
		// Get the exp error message text
		String ErrorMessage = Suite.objectRepositoryMap.get(
				"ClientEmailAlreadyTakenMSG").getExptext();
		// First compare the return text with error text
		if (!Message.equalsIgnoreCase(ErrorMessage)) {
			String SuccessMessage = Suite.objectRepositoryMap.get(
					"ClientCreateNewUserSuccessMSG").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(Message, SuccessMessage);
			if (!isTextMatching) {
				logger.error("New User success message text matching failed."
						+ " The Expected text is [" + SuccessMessage
						+ "] and the return text is [" + Message + "]");
				ReportUtils.setStepDescription(
						"New User success message text matching failed", "",
						SuccessMessage, Message, true);
				m_assert.fail("New User success message text matching failed."
						+ " The Expected text is [" + SuccessMessage
						+ "] and the return text is [" + Message + "]");
			}
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			// Click on Users & Roles
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Users List table
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ClientUserRolesUsersListTable").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientUserRolesUsersListTable").getLocatorvalue());
			// Identify the just created role
			boolean isUserPresent = SeleniumCustomUtils.verifyUserInTable(
					element,
					testcaseArgs.get("firstName") + " "
							+ testcaseArgs.get("lastName"));
			if (!isUserPresent) {
				logger.error("Created new User ["
						+ testcaseArgs.get("firstName") + " "
						+ testcaseArgs.get("lastName")
						+ "] is not present in Users list");
				ReportUtils.setStepDescription(
						"Created new User [" + testcaseArgs.get("firstName")
								+ " " + testcaseArgs.get("lastName")
								+ "] is not present in Users list", true);
				m_assert.fail("Created new User ["
						+ testcaseArgs.get("firstName") + " "
						+ testcaseArgs.get("lastName")
						+ "] is not present in Users list");
			}
		} else {
			logger.error("The given Email address is already taken. "
					+ "Please use another Email");
			ReportUtils.setStepDescription(
					"The given Email address is already taken. "
							+ "Please use another Email", true);
			m_assert.fail("The given Email address is already taken. "
					+ "Please use another Email");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("New User creation is successful");
		// Sign Out from the application
		logger.info("Signing Out from the application -----");
		// Identify Clients tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("ClientTablogo")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTablogo")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts] tab");
			ReportUtils.setStepDescription("Unable to identify [Accounts] tab",
					true);
			m_assert.fail("Unable to identify [Accounts] tab");
		}
		// Click on Clients tab
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [Accounts] tab");
			ReportUtils.setStepDescription("Unable to click on [Accounts] tab",
					true);
			m_assert.fail("Unable to click on [Accounts] tab");
		}
		SeleniumUtils.sleepThread(5);
		// Identify Sign Out button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts tab - Sign Out]"
					+ " button");
			ReportUtils.setStepDescription(
					"Unable to identify [Accounts tab - Sign Out]" + " button",
					true);
			m_assert.fail("Unable to identify [Accounts tab - Sign Out]"
					+ " button");
		}
		logger.info("Click on Sign Out button");
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click [Accounts tab - Sign Out] button");
			ReportUtils.setStepDescription(
					"Unable to click [Accounts tab - Sign Out] button", true);
			m_assert.fail("Unable to click [Accounts tab - Sign Out] button");
		}
		SeleniumUtils.sleepThread(4);
		// Identify Login page text
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("LoginPageText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("LoginPageText").getLocatorvalue());
		// Get the text
		String LoginText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpLoginText = Suite.objectRepositoryMap.get("LoginPageText")
				.getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(LoginText, ExpLoginText);
		if (!isTextMatching) {
			logger.error("Sign Out fails : The expected text in Login page is ["
					+ ExpLoginText
					+ "] and the actual return text is ["
					+ LoginText + "]");
			ReportUtils.setStepDescription("Sign Out fails", "", ExpLoginText,
					LoginText, true);
			m_assert.assertEquals(LoginText, ExpLoginText);
		}
		logger.info("Sign Out is successful");
		userelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatorvalue());
		passwordelement = SeleniumUtils.findobject(Suite.objectRepositoryMap
				.get("PasswordTextbox").getLocatortype(),
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatorvalue());
		buttonelement = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SigninButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SigninButton")
								.getLocatorvalue());
		// If username and password empty then throw exception...
		if (userelement == null || passwordelement == null
				|| buttonelement == null) {
			logger.error("Unable to identify username or password or signin elements");
			ReportUtils
					.setStepDescription(
							"Unable to identify username or password or signin elements",
							true);
			m_assert.fail("Unable to identify username or password or signin elements");
		}
		// Login into the application with username and password
		logger.info("login into the application");
		SeleniumUtils.login(userelement, passwordelement, buttonelement,
				testcaseArgs.get("email"), testcaseArgs.get("password"));
		logger.info("Verify the landing page after login");
		SeleniumUtils.sleepThread(5);
		// Identify InActive Header
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AccountInactiveHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AccountInactiveHeader")
						.getLocatorvalue());
		// Get the text
		String InActiveHeader = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpInActiveHeader = Suite.objectRepositoryMap.get(
				"AccountInactiveHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InActiveHeader,
				ExpInActiveHeader);
		if (!isTextMatching) {
			logger.error("Account Inactive Header element text matching failed. "
					+ "The expected text in Login page is ["
					+ ExpInActiveHeader
					+ "] and the actual return text is ["
					+ InActiveHeader + "]");
			ReportUtils
					.setStepDescription("Account Inactive Header element text "
							+ "matching failed", "", ExpInActiveHeader,
							InActiveHeader, true);
			m_assert.fail("Account Inactive Header element text matching failed. "
					+ "The expected text in Login page is ["
					+ ExpInActiveHeader
					+ "] and the actual return text is ["
					+ InActiveHeader + "]");
		}
		// Identify Account Inactive Paragraph text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AccountInactiveParagraph")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AccountInactiveParagraph")
						.getLocatorvalue());
		// Get the text
		String InActiveParagraph = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpInActiveParagraph = Suite.objectRepositoryMap.get(
				"AccountInactiveParagraph").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InActiveParagraph,
				ExpInActiveParagraph);
		if (!isTextMatching) {
			logger.error("Account Inactive Paragraph element text matching failed. "
					+ "The expected text in Login page is ["
					+ ExpInActiveParagraph
					+ "] and the actual return text is ["
					+ InActiveParagraph
					+ "]");
			ReportUtils.setStepDescription(
					"Account Inactive Paragraph element text "
							+ "matching failed", "", ExpInActiveHeader,
					InActiveHeader, true);
			m_assert.fail("Account Inactive Paragraph element text matching failed. "
					+ "The expected text in Login page is ["
					+ ExpInActiveParagraph
					+ "] and the actual return text is ["
					+ InActiveParagraph
					+ "]");
		}
		// Close the browser
		SeleniumUtils.closeBrowser();
		m_assert.assertAll();
	}

	/**
	 * This method login into the application as per the input
	 */
	@Test(priority = 3)
	public void loginAs() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check loginAs Test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("loginAs")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [loginAs] is not added for execution"
					+ " in [CommonScenarios]");
			ReportUtils.setStepDescription(
					"Test case [loginAs] is not added for execution"
							+ " in [CommonScenarios]", false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution"
							+ " in [CommonScenarios]");
		}
		// read the params data
		testcaseArgs = getTestData("loginAs");
		// Opening the browser
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
		// Identify the username and password fields
		logger.info("Identify Username, Password & Signin buttons");
		WebElement userelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatorvalue());
		WebElement passwordelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatorvalue());
		WebElement buttonelement = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SigninButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SigninButton")
								.getLocatorvalue());
		// If username and password empty then throw exception...
		if (userelement == null || passwordelement == null
				|| buttonelement == null) {
			logger.error("Unable to identify username or password or signin elements");
			ReportUtils
					.setStepDescription(
							"Unable to identify username or password or signin elements",
							true);
			m_assert.fail("Unable to identify username or password or signin elements");
		}
		// Login into the application with username and password
		logger.info("login into the application");
		SeleniumUtils.login(userelement, passwordelement, buttonelement,
				testcaseArgs.get("username"), testcaseArgs.get("password"));
		logger.info("Verify the landing page after login");
		SeleniumUtils.sleepThread(5);
		// Identify Landing page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		// If Landing page header element is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Initial Page] Header Element");
			ReportUtils.setStepDescription(
					"Unable to identify [Initial Page] Header Element", true);
			m_assert.fail("Unable to identify [Initial Page] Header Element");
		}
		// Get the text of the header element
		String InitialHeaderText = SeleniumUtils.getText(element);
		// Get the Expected text
		String ExpInitialHeaderText = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InitialHeaderText,
				ExpInitialHeaderText);
		if (isTextMatching) {
			logger.info("User is on [Your Applications] page");
			// Identify Company name
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientTabOrganizationName")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabOrganizationName")
							.getLocatorvalue());
			// Get the text
			String organizationName = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(organizationName,
					testcaseArgs.get("organization"));
			if (!isTextMatching) {
				logger.info("User is landed on [Your Applications] page of "
						+ organizationName + " organization");
				logger.info("Navigate to " + testcaseArgs.get("organization")
						+ " organization");
				// Identify Switch element
				element = SeleniumUtils.findobject(Suite.objectRepositoryMap
						.get("SwitchBtn").getLocatortype(),
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
				isClicked = SeleniumUtils.clickOnElement(element);
				if (!isClicked) {
					logger.error("Unable to click on [Switch Button]");
					ReportUtils.setStepDescription(
							"Unable to click on [Switch Button]", true);
					m_assert.fail("Unable to click on [Switch Button]");
				}
				logger.info("Click operation on [Switch Button] is successful");
				// Enter organization name in text box
				logger.info("Identify Switch button - Organization text box");
				SeleniumUtils.sleepThread(1);
				element = SeleniumUtils
						.findobject(
								Suite.objectRepositoryMap.get(
										"SwitcBtnOrganizationTextbox")
										.getLocatortype(),
								Suite.objectRepositoryMap.get(
										"SwitcBtnOrganizationTextbox")
										.getLocatorvalue());
				if (element == null) {
					logger.error("Switch button Organization textbox is not present. "
							+ "So unable to move to ["
							+ testcaseArgs.get("organization")
							+ "] Organization");
					ReportUtils.setStepDescription(
							"Switch button Organization textbox is not present. "
									+ "So unable to move to ["
									+ testcaseArgs.get("organization")
									+ "] Organization", true);
					m_assert.fail("Switch button Organization textbox is not present. "
							+ "So unable to move to ["
							+ testcaseArgs.get("organization")
							+ "] Organization");
				}
				logger.info("Enter the Organization ["
						+ testcaseArgs.get("organization")
						+ "] name in the Switch btn Organization text box");
				// Enter the organization name in text box
				SeleniumUtils.type(element, testcaseArgs.get("organization"));
				SeleniumUtils.sleepThread(3);
				// Switch to organization as per the browser
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					// Identify the Organization dropdown
					WebElement orgListbox = SeleniumUtils.findobject(
							Suite.objectRepositoryMap.get(
									"SwitcBtnOrganizationDropdown")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"SwitcBtnOrganizationDropdown")
									.getLocatorvalue());
					// If Organization dropdown is null then throw the error and
					// exit
					if (orgListbox == null) {
						logger.error("Switch button Organization dropdown is not present. "
								+ "So unable to move to Organization");
						ReportUtils.setStepDescription(
								"Switch button Organization dropdown is not present. "
										+ "So unable to move to Organization",
								false);
						m_assert.fail("Switch button Organization dropdown is not present. "
								+ "So unable to move to Organization");
					}
					// Click on Organization dropdown
					isClicked = SeleniumUtils.clickOnElement(orgListbox);
					if (!isClicked) {
						logger.error("Unable to click on organization in a list");
						ReportUtils.setStepDescription(
								"Unable to click on organization in a list",
								true);
						m_assert.fail("Unable to click on organization in a list");

					}
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
						|| configproperties.get(0).equalsIgnoreCase("SAFARI")
						|| configproperties.get(0).equalsIgnoreCase("IE")) {
					element.sendKeys(Keys.ENTER);
					SeleniumUtils.sleepThread(4);
				}
				// Verify the landing Page
				logger.info("Verify if user is landed on [Your Applications] page");
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatortype(),
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatorvalue());
				if (element == null) {
					logger.error("Login failed : Unabel to identify [Your Applications]"
							+ " header element");
					ReportUtils.setStepDescription(
							"Login failed : Unabel to identify [Your Applications]"
									+ " header element", true);
					m_assert.fail("Login failed : Unabel to identify [Your Applications]"
							+ " header element");
				}
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
		} else {
			logger.info("User is landed on [" + InitialHeaderText + "] page");
			logger.info("Navigate to [" + ExpInitialHeaderText + "] page");
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
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Switch Button]");
				ReportUtils.setStepDescription(
						"Unable to click on [Switch Button]", true);
				m_assert.fail("Unable to click on [Switch Button]");
			}
			logger.info("Click operation on [Switch Button] is successful");
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
				logger.error("Switch button Organization textbox is not present. "
						+ "So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
				ReportUtils.setStepDescription(
						"Switch button Organization textbox is not present. "
								+ "So unable to move to ["
								+ testcaseArgs.get("organization")
								+ "] Organization", true);
				m_assert.fail("Switch button Organization textbox is not present. "
						+ "So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
			}
			logger.info("Enter the Organization ["
					+ testcaseArgs.get("organization")
					+ "] name in the Switch btn Organization text box");
			// Enter the organization name in text box
			SeleniumUtils.type(element, testcaseArgs.get("organization"));
			SeleniumUtils.sleepThread(3);
			// Switch to organization as per the browser
			if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
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
					ReportUtils.setStepDescription(
							"Switch button Organization dropdown is not present. "
									+ "So unable to move to Organization",
							false);
					m_assert.fail("Switch button Organization dropdown is not present. "
							+ "So unable to move to Organization");
				}
				// Click on Organization dropdown
				isClicked = SeleniumUtils.clickOnElement(orgListbox);
				if (!isClicked) {
					logger.error("Unable to select on organization in a list");
					ReportUtils.setStepDescription(
							"Unable to select on organization in a list", true);
					m_assert.fail("Unable to select on organization in a list");

				}
			} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
					|| configproperties.get(0).equalsIgnoreCase("SAFARI")
					|| configproperties.get(0).equalsIgnoreCase("IE")) {
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
				logger.error("Login failed : Unabel to identify [Your Applications]"
						+ " header element");
				ReportUtils.setStepDescription(
						"Login failed : Unabel to identify [Your Applications]"
								+ " header element", true);
				m_assert.fail("Login failed : Unabel to identify [Your Applications]"
						+ " header element");
			}
			String landingPageText = SeleniumUtils.getText(element);
			// Get the actual text
			String ExpLandingPageText = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare the both texts
			isTextMatching = SeleniumUtils.assertEqual(landingPageText,
					ExpLandingPageText);
			if (!isTextMatching) {
				logger.error("Login failed. User is not landed on "
						+ "[Your Applications] page");
				ReportUtils.setStepDescription(
						"Login failed. User is not landed on "
								+ "[Your Applications] page", true);
				m_assert.fail("Login failed. User is not landed on "
						+ "[Your Applications] page");
			}
		}
		m_assert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods = "loginAs")
	public void verify_ForGotPassword_Functionality() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("verify_ForGotPassword_Functionality")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verify_ForGotPassword_Functionality]"
					+ " is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verify_ForGotPassword_Functionality]"
							+ " is not added for execution", false);
			throw new SkipException(
					"Test case [verify_ForGotPassword_Functionality]"
							+ " is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("verify_ForGotPassword_Functionality");
		logger.info("Starting [verify_ForGotPassword_Functionality] execution");
		// Verify if user is on [Add User] page
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		// Get the text
		String UsersAndRolestabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpUsersAndRolestabText = Suite.objectRepositoryMap.get(
				"ClientTabUsersAndRoles").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(UsersAndRolestabText,
				ExpUsersAndRolestabText);
		if (!isTextMatching) {
			logger.error("[Users & Roles] tab text matching failed. Expected text is ["
					+ ExpUsersAndRolestabText
					+ "] and the return text is ["
					+ UsersAndRolestabText + "]");
			ReportUtils.setStepDescription(
					"[Users & Roles] tab text matching failed", "",
					ExpUsersAndRolestabText, UsersAndRolestabText, true);
			m_assert.fail("[Users & Roles] tab text matching failed. Expected text is ["
					+ ExpUsersAndRolestabText
					+ "] and the return text is ["
					+ UsersAndRolestabText + "]");
		}
		// Click on [Users & Roles]
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// click on New User Button
		logger.info("Click on New User to create new user account");
		// Identify New User button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatorvalue());
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on New User button");
			ReportUtils.setStepDescription(
					"Unable to click on New User button", false);
			m_assert.fail("Unable to identify New User button");
		}
		SeleniumUtils.sleepThread(5);
		// First Name text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		// Enter first name
		SeleniumUtils.type(element, testcaseArgs.get("firstName"));
		// Last Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatorvalue());
		// Enter Last name
		SeleniumUtils.type(element, testcaseArgs.get("lastName"));
		// Identify Email
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		// Enter Email
		SeleniumUtils.type(element, testcaseArgs.get("email"));
		// Identify Password
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		// Enter password
		SeleniumUtils.type(element, testcaseArgs.get("password"));
		// Identify Time zone link
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserCurrentLocationTimeZoneLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserCurrentLocationTimeZoneLink")
						.getLocatorvalue());
		SeleniumUtils.scrollDown();
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on Save button");
			ReportUtils.setStepDescription("Unable to click on Save button",
					true);
			m_assert.fail("Unable to click on Save button");
		}
		logger.info("Validate the success message");
		SeleniumUtils.sleepThread(3);
		// Identify Success Message or Email Already taken error message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"SaveUserMsgOrEmailAlreadyTakenMsg").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SaveUserMsgOrEmailAlreadyTakenMsg").getLocatorvalue());
		// Get the text
		String Message = SeleniumUtils.getText(element);
		// Get the exp error message text
		String ErrorMessage = Suite.objectRepositoryMap.get(
				"ClientEmailAlreadyTakenMSG").getExptext();
		// First compare the return text with error text
		if (!Message.equalsIgnoreCase(ErrorMessage)) {
			String SuccessMessage = Suite.objectRepositoryMap.get(
					"ClientCreateNewUserSuccessMSG").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(Message, SuccessMessage);
			if (!isTextMatching) {
				logger.error("New User success message text matching failed."
						+ " The Expected text is [" + SuccessMessage
						+ "] and the return text is [" + Message + "]");
				ReportUtils.setStepDescription(
						"New User success message text matching failed", "",
						SuccessMessage, Message, true);
				m_assert.fail("New User success message text matching failed."
						+ " The Expected text is [" + SuccessMessage
						+ "] and the return text is [" + Message + "]");
			}
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			// Click on Users & Roles
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Users List table
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ClientUserRolesUsersListTable").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientUserRolesUsersListTable").getLocatorvalue());
			// Identify the just created role
			boolean isUserPresent = SeleniumCustomUtils.verifyUserInTable(
					element,
					testcaseArgs.get("firstName") + " "
							+ testcaseArgs.get("lastName"));
			if (!isUserPresent) {
				logger.error("Created new User ["
						+ testcaseArgs.get("firstName") + " "
						+ testcaseArgs.get("lastName")
						+ "] is not present in Users list");
				ReportUtils.setStepDescription(
						"Created new User [" + testcaseArgs.get("firstName")
								+ " " + testcaseArgs.get("lastName")
								+ "] is not present in Users list", true);
				m_assert.fail("Created new User ["
						+ testcaseArgs.get("firstName") + " "
						+ testcaseArgs.get("lastName")
						+ "] is not present in Users list");
			}
		} else {
			logger.error("The given Email address is already taken. "
					+ "Please use another Email");
			ReportUtils.setStepDescription(
					"The given Email address is already taken. "
							+ "Please use another Email", true);
			m_assert.fail("The given Email address is already taken. "
					+ "Please use another Email");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("New User creation is successful");
		// Sign Out from the application
		logger.info("Signing Out from the application -----");
		// Identify Clients tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("ClientTablogo")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTablogo")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts] tab");
			ReportUtils.setStepDescription("Unable to identify [Accounts] tab",
					true);
			m_assert.fail("Unable to identify [Accounts] tab");
		}
		// Click on Clients tab
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [Accounts] tab");
			ReportUtils.setStepDescription("Unable to click on [Accounts] tab",
					true);
			m_assert.fail("Unable to click on [Accounts] tab");
		}
		SeleniumUtils.sleepThread(5);
		// Identify Sign Out button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts tab - Sign Out]"
					+ " button");
			ReportUtils.setStepDescription(
					"Unable to identify [Accounts tab - Sign Out]" + " button",
					true);
			m_assert.fail("Unable to identify [Accounts tab - Sign Out]"
					+ " button");
		}
		logger.info("Click on Sign Out button");
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click [Accounts tab - Sign Out] button");
			ReportUtils.setStepDescription(
					"Unable to click [Accounts tab - Sign Out] button", true);
			m_assert.fail("Unable to click [Accounts tab - Sign Out] button");
		}
		SeleniumUtils.sleepThread(4);
		// Identify Login page text
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("LoginPageText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("LoginPageText").getLocatorvalue());
		// Get the text
		String LoginText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpLoginText = Suite.objectRepositoryMap.get("LoginPageText")
				.getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(LoginText, ExpLoginText);
		if (!isTextMatching) {
			logger.error("Sign Out fails : The expected text in Login page is ["
					+ ExpLoginText
					+ "] and the actual return text is ["
					+ LoginText + "]");
			ReportUtils.setStepDescription("Sign Out fails", "", ExpLoginText,
					LoginText, true);
			m_assert.assertEquals(LoginText, ExpLoginText);
		}
		logger.info("Sign Out is successful");
		logger.info("Reset the password ----");
		// Identify Forgot Password link
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordLink")
						.getLocatorvalue());
		// Get the text
		String ForgotPasswordText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpForgotPasswordText = Suite.objectRepositoryMap.get(
				"ForgotPasswordLink").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(ForgotPasswordText,
				ExpForgotPasswordText);
		if (!isTextMatching) {
			logger.error("[Forgot Password] link text fails in Login page:"
					+ " The expected text in Login page is ["
					+ ExpForgotPasswordText
					+ "] and the actual return text is [" + ForgotPasswordText
					+ "]");
			ReportUtils.setStepDescription("[Forgot Password] link text fails"
					+ " in Login page", "", ExpForgotPasswordText,
					ForgotPasswordText, true);
			m_assert.fail("[Forgot Password] link text fails in Login page:"
					+ " The expected text in Login page is ["
					+ ExpForgotPasswordText
					+ "] and the actual return text is [" + ForgotPasswordText
					+ "]");
		}
		// Click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify the URL
		String ForgotPasswordURL = SeleniumUtils.getURL();
		isTextMatching = SeleniumUtils.assertEqual(ForgotPasswordURL,
				testcaseArgs.get("forgotpassword_url"));
		if (!isTextMatching) {
			logger.error("URL matching failed after user clicks on"
					+ " [ForGot Password] link " + "The expected URL ["
					+ testcaseArgs.get("forgotpassword_url")
					+ "] and the actual URL is [" + ForgotPasswordURL + "]");
			ReportUtils.setStepDescription(
					"URL matching failed after user clicks on "
							+ "[ForGot Password] link", "",
					testcaseArgs.get("forgotpassword_url"), ForgotPasswordURL,
					true);
			m_assert.fail("URL matching failed after user clicks on"
					+ " [ForGot Password] link " + "The expected URL ["
					+ testcaseArgs.get("forgotpassword_url")
					+ "] and the actual URL is [" + ForgotPasswordURL + "]");
		}
		// Identify Don't remember your password Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("DontRememberYourPasswordHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("DontRememberYourPasswordHeader")
						.getLocatorvalue());
		// Get the text
		String DontRememberPasswordHeaderText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpDontRememberPasswordHeaderText = Suite.objectRepositoryMap
				.get("DontRememberYourPasswordHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(
				DontRememberPasswordHeaderText,
				ExpDontRememberPasswordHeaderText);
		if (!isTextMatching) {
			logger.error("[Dont Remember Password] text matching failed:"
					+ " The expected text is ["
					+ ExpDontRememberPasswordHeaderText
					+ "] and the actual return text is ["
					+ DontRememberPasswordHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Dont Remember Password] text matching failed"
							+ " in Login page", "",
					ExpDontRememberPasswordHeaderText,
					DontRememberPasswordHeaderText, true);
			m_assert.fail("[Dont Remember Password] text matching failed:"
					+ " The expected text is ["
					+ ExpDontRememberPasswordHeaderText
					+ "] and the actual return text is ["
					+ DontRememberPasswordHeaderText + "]");
		}
		// Identify Don't remember your password body element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("DontRememberYourPasswordBody")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("DontRememberYourPasswordBody")
						.getLocatorvalue());
		// Get the text
		String DontRememberPasswordBodyText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpDontRememberPasswordBodyText = Suite.objectRepositoryMap.get(
				"DontRememberYourPasswordBody").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(
				DontRememberPasswordBodyText, ExpDontRememberPasswordBodyText);
		if (!isTextMatching) {
			logger.error("[Dont Remember Password] paragraph text matching failed:"
					+ " The expected text is ["
					+ ExpDontRememberPasswordBodyText
					+ "] and the actual return text is ["
					+ DontRememberPasswordBodyText + "]");
			ReportUtils.setStepDescription(
					"[Dont Remember Password] paragraph text matching failed"
							+ " in Login page", "",
					ExpDontRememberPasswordBodyText,
					DontRememberPasswordBodyText, true);
			m_assert.fail("[Dont Remember Password] paragraph text matching failed:"
					+ " The expected text is ["
					+ ExpDontRememberPasswordBodyText
					+ "] and the actual return text is ["
					+ DontRememberPasswordBodyText + "]");
		}
		// Identify Email text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordEmail")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordEmail")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Email text box after user"
					+ " clicks on Forgot Password link");
			ReportUtils.setStepDescription(
					"Unable to identify Email text box after user"
							+ " clicks on Forgot Password link", true);
			m_assert.fail("Unable to identify Email text box after user"
					+ " clicks on Forgot Password link");
		}
		// Identify Submit button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordSubmitButton")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordSubmitButton")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Submit button after user"
					+ " clicks on Forgot Password link");
			ReportUtils.setStepDescription(
					"Unable to identify Submit button after user"
							+ " clicks on Forgot Password link", true);
			m_assert.fail("Unable to identify Submit button after user"
					+ " clicks on Forgot Password link");
		}
		// Click on Submit button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify the URL
		ForgotPasswordURL = SeleniumUtils.getURL();
		isTextMatching = SeleniumUtils.assertEqual(ForgotPasswordURL,
				testcaseArgs.get("forgotpassword_url"));
		if (!isTextMatching) {
			logger.error("Validation fails at Email field. Application "
					+ "accepts blank email when user reset the password");
			ReportUtils.setStepDescription("Validation fails at Email field. "
					+ "Application "
					+ "accepts blank email when user reset the password", true);
			m_assert.fail("Validation fails at Email field. Application "
					+ "accepts blank email when user reset the password");
		}
		// Identify Email text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordEmail")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordEmail")
						.getLocatorvalue());
		// Enter invalid Email
		SeleniumUtils.type(element, "abc");
		// Identify Submit button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordSubmitButton")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordSubmitButton")
						.getLocatorvalue());
		// Click on Submit button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify the URL
		ForgotPasswordURL = SeleniumUtils.getURL();
		isTextMatching = SeleniumUtils.assertEqual(ForgotPasswordURL,
				testcaseArgs.get("forgotpassword_url"));
		if (!isTextMatching) {
			logger.error("Validation fails at Email field. Application "
					+ "accepts invalid email when user reset the password");
			ReportUtils.setStepDescription("Validation fails at Email field. "
					+ "Application "
					+ "accepts invalid email when user reset the password",
					true);
			m_assert.fail("Validation fails at Email field. Application "
					+ "accepts invalid email when user reset the password");
		}
		// Identify Email text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordEmail")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordEmail")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		// Enter invalid Email
		SeleniumUtils.type(element, testcaseArgs.get("email"));
		// Identify Submit button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ForgotPasswordSubmitButton")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ForgotPasswordSubmitButton")
						.getLocatorvalue());
		// Click on Submit button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify Check On Your Email Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("CheckOnYourEmailHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CheckOnYourEmailHeader")
						.getLocatorvalue());
		// Get the text
		String CheckOnYourEmailText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpCheckOnYourEmailText = Suite.objectRepositoryMap.get(
				"CheckOnYourEmailHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(CheckOnYourEmailText,
				ExpCheckOnYourEmailText);
		if (!isTextMatching) {
			logger.error("[Check On Your Email] text matching failed:"
					+ " The expected text is [" + ExpCheckOnYourEmailText
					+ "] and the actual return text is ["
					+ CheckOnYourEmailText + "]");
			ReportUtils.setStepDescription(
					"[Check On Your Email] text matching failed", "",
					ExpCheckOnYourEmailText, CheckOnYourEmailText, true);
			m_assert.fail("[Check On Your Email] text matching failed:"
					+ " The expected text is [" + ExpCheckOnYourEmailText
					+ "] and the actual return text is ["
					+ CheckOnYourEmailText + "]");
		}
		// Identify Check On Your Email Paragraph element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("CheckOnYourEmailParagraph")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CheckOnYourEmailParagraph")
						.getLocatorvalue());
		// Get the text
		String CheckOnYourEmailParagraphText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpCheckOnYourEmailParagraphText = Suite.objectRepositoryMap
				.get("CheckOnYourEmailParagraph").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils
				.assertEqual(CheckOnYourEmailParagraphText,
						ExpCheckOnYourEmailParagraphText);
		if (!isTextMatching) {
			logger.error("[Check On Your Email] paragraph text matching failed:"
					+ " The expected text is ["
					+ ExpCheckOnYourEmailParagraphText
					+ "] and the actual return text is ["
					+ CheckOnYourEmailParagraphText + "]");
			ReportUtils.setStepDescription(
					"[Check On Your Email] paragraph text matching failed", "",
					ExpCheckOnYourEmailParagraphText,
					CheckOnYourEmailParagraphText, true);
			m_assert.fail("[Check On Your Email] paragraph text matching failed:"
					+ " The expected text is ["
					+ ExpCheckOnYourEmailParagraphText
					+ "] and the actual return text is ["
					+ CheckOnYourEmailParagraphText + "]");
		}
		logger.info("Navigate to Gmail account -----");
		SeleniumUtils.navigateToUrl("http://www.gmail.com");
		// Identify Gmail Home page Sign in Link
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("GoogleHomePageSignIn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("GoogleHomePageSignIn")
						.getLocatorvalue());
		if (element != null) {
			// Click on Sign In link
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		}
		// Identify Gmail Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("GmailHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("GmailHeaderElement")
						.getLocatorvalue());
		// Get the text
		String GmailHeaderText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpGmailHeaderText = Suite.objectRepositoryMap.get(
				"GmailHeaderElement").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(GmailHeaderText,
				ExpGmailHeaderText);
		if (!isTextMatching) {
			logger.error("[Gmail header] text matching failed:"
					+ " The expected text is [" + ExpGmailHeaderText
					+ "] and the actual return text is [" + GmailHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"[Gmail header] text matching failed", "",
					ExpGmailHeaderText, GmailHeaderText, true);
			m_assert.fail("[Gmail header] text matching failed:"
					+ " The expected text is [" + ExpGmailHeaderText
					+ "] and the actual return text is [" + GmailHeaderText
					+ "]");
		}
		// Identify Current Email id Element
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("CurrentEmail")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CurrentEmail").getLocatorvalue());
		if (element == null) {
			// Identify Gmail UserName
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("GmailUserName")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("GmailUserName")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Gmail - UserName text box]");
				ReportUtils.setStepDescription("Unable to identify "
						+ "[Gmail - UserName text box]", true);
				m_assert.fail("Unable to identify [Gmail - UserName text box]");
			}
			// Type UserName
			SeleniumUtils.type(element, testcaseArgs.get("gmailusername"));
			// Identify Password
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("GmailPassword")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("GmailPassword")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to Identify [Password text box]"
						+ " link in Gmail");
				ReportUtils.setStepDescription(
						"Unable to Identify [Password text box]"
								+ " link in Gmail", true);
				m_assert.fail("Unable to Identify [Password text box]"
						+ " link in Gmail");
			}
			// Type password
			SeleniumUtils.type(element, testcaseArgs.get("gmailpassword"));
			// Identify Sign In button
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("GmailSignin")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("GmailSignin")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to Identify [Sign In button]"
						+ " in Gmail");
				ReportUtils.setStepDescription(
						"Unable to Identify [Sign In button]" + " in Gmail",
						true);
				m_assert.fail("Unable to Identify [Sign In button]"
						+ " in Gmail");
			}
			// Click on Sign in
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		} else {
			// Get the text
			String CurrentEmailText = SeleniumUtils.getText(element);
			// Get the Exp text
			String ExpCurrentEmailText = Suite.objectRepositoryMap.get(
					"CurrentEmail").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(GmailHeaderText,
					ExpCurrentEmailText);
			if (!isTextMatching) {
				logger.info("The current Email id is [" + CurrentEmailText
						+ "]");
				logger.info("Navigate to [" + ExpGmailHeaderText + "]");
				// Identify Sign in with a different account
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("SignInWithDifferentAct")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("SignInWithDifferentAct")
								.getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Sign in with different user]"
							+ " link in Gmail");
					ReportUtils.setStepDescription("Unable to Identify"
							+ " [Sign in with different user] link in Gmail",
							true);
					m_assert.fail("Unable to Identify [Sign in with different user]"
							+ " link in Gmail");
				}
				// Click
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(3);
				// Identify Add Account link
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("AddAccountLink")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AddAccountLink").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Add Account]"
							+ " link in Gmail");
					ReportUtils.setStepDescription("Unable to Identify"
							+ " [Add Account] link in Gmail", true);
					m_assert.fail("Unable to Identify [Add Account]"
							+ " link in Gmail");
				}
				// Click
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(4);
				// Identify Gmail UserName
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailUserName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailUserName").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to identify [Gmail - UserName text box]");
					ReportUtils.setStepDescription("Unable to identify "
							+ "[Gmail - UserName text box]", true);
					m_assert.fail("Unable to identify [Gmail - UserName text box]");
				}
				// Type UserName
				SeleniumUtils.type(element, testcaseArgs.get("gmailusername"));
				// Identify Password
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailPassword")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailPassword").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Password text box]"
							+ " link in Gmail");
					ReportUtils.setStepDescription(
							"Unable to Identify [Password text box]"
									+ " link in Gmail", true);
					m_assert.fail("Unable to Identify [Password text box]"
							+ " link in Gmail");
				}
				// Type password
				SeleniumUtils.type(element, testcaseArgs.get("gmailpassword"));
				// Identify Sign In button
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailSignin")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailSignin").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Sign In button]"
							+ " in Gmail");
					ReportUtils
							.setStepDescription(
									"Unable to Identify [Sign In button]"
											+ " in Gmail", true);
					m_assert.fail("Unable to Identify [Sign In button]"
							+ " in Gmail");
				}
				// Click on Sign in
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(4);
			} else {
				// Identify Password input field
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailPassword")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailPassword").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Password text box]"
							+ " link in Gmail");
					ReportUtils.setStepDescription(
							"Unable to Identify [Password text box]"
									+ " link in Gmail", true);
					m_assert.fail("Unable to Identify [Password text box]"
							+ " link in Gmail");
				}
				// Type password
				SeleniumUtils.type(element, testcaseArgs.get("gmailpassword"));
				// Identify Submit button
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("GmailSignin")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("GmailSignin").getLocatorvalue());
				if (element == null) {
					logger.error("Unable to Identify [Sign In button]"
							+ " in Gmail");
					ReportUtils
							.setStepDescription(
									"Unable to Identify [Sign In button]"
											+ " in Gmail", true);
					m_assert.fail("Unable to Identify [Sign In button]"
							+ " in Gmail");
				}
				// Click on Sign in
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(4);
			}
		}
		// Identify Inbox table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("GmailInboxTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("GmailInboxTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to Identify [Gmail - Inbox -mail list]"
					+ " in Gmail");
			ReportUtils.setStepDescription(
					"Unable to Identify [Gmail - Inbox -mail list]"
							+ " in Gmail", true);
			m_assert.fail("Unable to Identify [Gmail - Inbox -mail list]"
					+ " in Gmail");
		}
		// Get the number of Rows in the inbox
		int numberOfMails = SeleniumUtils.getCountFromTable(element);
		if (numberOfMails == 0) {
			logger.error("No mail received from Phunware for "
					+ "Reset password");
			ReportUtils.setStepDescription(
					"No mail received from Phunware for Reset password", true);
			m_assert.fail("No mail received from Phunware for "
					+ "Reset password");
		} else {
			// Click on Phunware - Reset Password mail
			isClicked = SeleniumCustomUtils.clickOnMailInGmailInbox(element,
					"Reset Your Password");
			if (!isClicked) {
				logger.error("Unable to click on Phunware mail in Gmail Inbox");
				ReportUtils
						.setStepDescription(
								"Unable to click on Phunware mail in Gmail Inbox",
								true);
				m_assert.fail("Unable to click on Phunware mail in Gmail Inbox");
			}
			// Idetntify Reset Your Password link
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get(
									"GmailInboxResetYourPasswordLink")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"GmailInboxResetYourPasswordLink")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Reset Your Password] body"
						+ " link in Gmail Inbox");
				ReportUtils.setStepDescription(
						"Unable to identify [Reset Your Password] body"
								+ " link in Gmail Inbox", true);
				m_assert.fail("Unable to identify [Reset Your Password] body"
						+ " link in Gmail Inbox");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Navigate to New Window
			SeleniumUtils.switchToChildWindow();
			// Identify Enter New Password Element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("EnterNewPasswordHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("EnterNewPasswordHeader")
							.getLocatorvalue());
			// Get the text
			String EnterNewPasswordText = SeleniumUtils.getText(element);
			// Get the Exp text
			String ExpEnterNewPasswordText = Suite.objectRepositoryMap.get(
					"EnterNewPasswordHeader").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(EnterNewPasswordText,
					ExpEnterNewPasswordText);
			if (!isTextMatching) {
				logger.error("[Enter New Password] header text matching failed:"
						+ " The expected text is ["
						+ ExpEnterNewPasswordText
						+ "] and the actual return text is ["
						+ EnterNewPasswordText + "]");
				ReportUtils.setStepDescription(
						"[Enter New Password] header text matching failed", "",
						ExpEnterNewPasswordText, EnterNewPasswordText, true);
				m_assert.fail("[Enter New Password] header text matching failed:"
						+ " The expected text is ["
						+ ExpEnterNewPasswordText
						+ "] and the actual return text is ["
						+ EnterNewPasswordText + "]");
			}
			// Identify Password1 field
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("EnterNewPassword_password1")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("EnterNewPassword_password1")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Password1 field text box in"
						+ " [Enter New Password] page");
				ReportUtils.setStepDescription("Unable to identify Password1"
						+ " field text box in [Enter New Password] page", true);
				m_assert.fail("Unable to identify Password1 field text box in"
						+ " [Enter New Password] page");
			}
			// Type password value
			SeleniumUtils.type(element, testcaseArgs.get("password"));
			// Identify Password2 field
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("EnterNewPassword_password2")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("EnterNewPassword_password2")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Password2 field text box in"
						+ " [Enter New Password] page");
				ReportUtils.setStepDescription("Unable to identify Password2"
						+ " field text box in [Enter New Password] page", true);
				m_assert.fail("Unable to identify Password2 field text box in"
						+ " [Enter New Password] page");
			}
			// Type password value
			SeleniumUtils.type(element, testcaseArgs.get("newpassword"));
			// Identify SignIn button
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("EnterNewPassword_submit")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("EnterNewPassword_submit")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Submit button in"
						+ " [Enter New Password] page");
				ReportUtils.setStepDescription(
						"Unable to identify Submit button in"
								+ " [Enter New Password] page", true);
				m_assert.fail("Unable to identify Submit button in"
						+ " [Enter New Password] page");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Success Message
		}
		// Close the browser
		SeleniumUtils.closeBrowser();
		m_assert.assertAll();
	}

	/**
	 * @param testcase
	 * @return Method return the params list based on the input testcase
	 */
	public Map<String, String> getTestData(String testcase) {
		if (commonscenariosSuite != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			testcases = commonscenariosSuite.getCase();
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

}
