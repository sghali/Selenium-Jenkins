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
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import com.phunware.constants.GlobalConstants;
import com.phunware.jaxb.entity.Testcase;
import com.phunware.jaxb.entity.Testcase.Case;
import com.phunware.jaxb.entity.Testcase.Case.Param;
import com.phunware.util.JaxbUtil;
import com.phunware.util.ReportUtils;
import com.phunware.util.SeleniumUtils;
import com.phunware.util.SoftAssert;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class Analytics extends Suite {
	private static final Logger logger = Logger.getLogger(Analytics.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private static String childSuite = "analytics";
	private static boolean suiteExecution = false;
	private static WebElement element = null;
	private static boolean isTextMatching;
	private static boolean isClicked;
	private SoftAssert m_assert;
	private Testcase analytics = null;
	private List<Case> testcases = null;

	@SuppressWarnings("unused")
	@BeforeClass
	private void setUp() {
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
			logger.warn("Test suite [Analytics] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [Analytics] is not added for execution", false);
			throw new SkipException(
					"Test suite [Analytics] is not added for execution");
		}
		logger.info("reading Analytic Input file");
		analytics = (Testcase) JaxbUtil
				.unMarshal(GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.ANALYTICS_FILE, Testcase.class);
		if (analytics != null) {
			testcases = analytics.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());

				}
			}
		}
		if (testcaseList.size() == 0) {
			logger.warn("No TestCase added for execution in [Analytics]");
			throw new SkipException(
					"No TestCase added for execution in [Analytics]");
		}
		logger.info("The testcases for execution in [Analytics]" + testcaseList);
		m_assert.assertAll();
	}

	@Test(priority = 0)
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
					+ " in [Analytics]");
			ReportUtils.setStepDescription(
					"Test case [loginAs] is not added for execution"
							+ " in [Analytics]", false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution"
							+ " in [Analytics]");
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

	@Test(priority = 1, dependsOnMethods = { "loginAs" })
	public void verifyOverviewLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyOverviewLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyOverviewLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyOverviewLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyOverviewLayout] is not added for execution");
		}
		logger.info("Starting [verifyOverviewLayout] execution");
		logger.info("verify if Analytics Tab is present");
		// Identify Analytics tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("AnalyticsTab")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("AnalyticsTab")
								.getLocatorvalue());
		// Get the text
		String AnalyticsTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAnalyticsTabText = Suite.objectRepositoryMap.get(
				"AnalyticsTab").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(AnalyticsTabText,
				ExpAnalyticsTabText);
		if (!isTextMatching) {
			logger.error("[Analytics] tab text matching failed. The expected text is ["
					+ ExpAnalyticsTabText
					+ "] and the actual return text is ["
					+ AnalyticsTabText + "]");
			ReportUtils.setStepDescription(
					"[Analytics] tab text matching failed", "",
					ExpAnalyticsTabText, AnalyticsTabText, true);
			m_assert.fail("[Analytics] tab text matching failed. The expected text is ["
					+ ExpAnalyticsTabText
					+ "] and the actual return text is ["
					+ AnalyticsTabText + "]");
		}
		logger.info("Performing click operation on Analytics Tab");
		// click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("verify if Overview Tab is the initial landing tab of Analytics page");
		// Identify Overview tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AnalyticsOverviewTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AnalyticsOverviewTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String AnalyticsOverviewHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAnalyticsOverviewHeaderText = Suite.objectRepositoryMap.get(
				"AnalyticsOverviewTabHeaderText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(AnalyticsOverviewHeaderText,
				ExpAnalyticsOverviewHeaderText);
		if (!isTextMatching) {
			logger.error("[Overview] page header text matching failed. The expected text is ["
					+ ExpAnalyticsOverviewHeaderText
					+ "] and the actual return text is ["
					+ AnalyticsOverviewHeaderText + "]");
			ReportUtils.setStepDescription(
					"[[Overview] page header text matching failed", "",
					ExpAnalyticsOverviewHeaderText,
					AnalyticsOverviewHeaderText, true);
			m_assert.fail("[Overview] page header text matching failed. The expected text is ["
					+ ExpAnalyticsOverviewHeaderText
					+ "] and the actual return text is ["
					+ AnalyticsOverviewHeaderText + "]");
		}
		logger.info("Verify if Application and Calandar dropdowns are present in Overview tab");
		// Identify Application dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AnalyticsOverviewTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AnalyticsOverviewTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in [Overview] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Application dropdown in [Overview] page",
							true);
			m_assert.fail("Unable to identify Application dropdown in [Overview] page");
		}
		// Calandar dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AnalyticsOverviewTabCalandarDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AnalyticsOverviewTabCalandarDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Calandar dropdown in [Overview] page");
			ReportUtils.setStepDescription(
					"Unable to identify Calandar dropdown in [Overview] page",
					true);
			m_assert.fail("Unable to identify Calandar dropdown in [Overview] page");
		}
		m_assert.assertAll();
	}

	@Test(priority = 2, dependsOnMethods = { "loginAs" })
	public void verifyOverviewData() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyOverviewData")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyOverviewData] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyOverviewData] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyOverviewData] is not added for execution");
		}
		logger.info("Starting [verifyOverviewData] execution");
		// read param data
		testcaseArgs = getTestData("verifyOverviewData");
		// Select application from application dropdown
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AnalyticsOverviewTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AnalyticsOverviewTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.info("Unable to select application ["
					+ testcaseArgs.get("application")
					+ "] from the  application dropdown in [Overview] page");
			ReportUtils.setStepDescription("Unable to select application ["
					+ testcaseArgs.get("application")
					+ "] from the  application dropdown in [Overview] page",
					false);
			m_assert.fail("Unable to select application ["
					+ testcaseArgs.get("application")
					+ "] from the  application dropdown in [Overview] page");
		}
		SeleniumUtils.sleepThread(4);
		m_assert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods = { "loginAs" })
	public void verifyEventsLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyEventsLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyEventsLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyEventsLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyEventsLayout] is not added for execution");
		}
		logger.info("Starting [verifyEventsLayout] execution");
		logger.info("Verify if Custom Events Tab is present");
		// Identify Custom Events tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AnalyticsEventsTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AnalyticsEventsTab")
						.getLocatorvalue());
		// Get the text
		String AnalyticsEventsText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAnalyticsEventsText = Suite.objectRepositoryMap.get(
				"AnalyticsEventsTab").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(AnalyticsEventsText,
				ExpAnalyticsEventsText);
		if (!isTextMatching) {
			logger.error("[Custom Events] tab text matching failed. The expected text is ["
					+ ExpAnalyticsEventsText
					+ "] and the actual return text is ["
					+ AnalyticsEventsText
					+ "]");
			ReportUtils.setStepDescription(
					"[Custom Events] tab text matching failed", "",
					ExpAnalyticsEventsText, AnalyticsEventsText, true);
			m_assert.fail("[Custom Events] tab text matching failed. The expected text is ["
					+ ExpAnalyticsEventsText
					+ "] and the actual return text is ["
					+ AnalyticsEventsText
					+ "]");
		}
		logger.info("Performing click operation on Events tab");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		logger.info("Verify if Application and Calandar dropdowns are present in Events tab");
		// Identify Application dropdown
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AnalyticsEventsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AnalyticsEventsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in [Custom Events] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Application dropdown in [Custom Events] page",
							true);
			m_assert.fail("Unable to identify Application dropdown in [Custom Events] page");
		}
		// Calandar dropdown
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AnalyticsEventsTabCalandarDropdown")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AnalyticsEventsTabCalandarDropdown")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Calandar dropdown in [Custom Events] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Calandar dropdown in [Custom Events] page",
							true);
			m_assert.fail("Unable to identify Calandar dropdown in [Custom Events] page");
		}
		m_assert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods = { "loginAs" })
	public void verifyEventsData() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyEventsData")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyEventsData] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyEventsData] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifyEventsData] is not added for execution");
		}
		logger.info("Starting [verifyEventsData] execution");
		// read param data
		testcaseArgs = getTestData("verifyEventsData");
		// Select application from application dropdown
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AnalyticsEventsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AnalyticsEventsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.info("Unable to select application ["
					+ testcaseArgs.get("application")
					+ "] from the  application dropdown in [Overview] page");
			ReportUtils.setStepDescription("Unable to select application ["
					+ testcaseArgs.get("application")
					+ "] from the  application dropdown in [Overview] page",
					false);
			m_assert.fail("Unable to select application ["
					+ testcaseArgs.get("application")
					+ "] from the  application dropdown in [Overview] page");
		}
		SeleniumUtils.sleepThread(4);
		m_assert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = { "loginAs" })
	public void logOut() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check logOut is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("logOut")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [logOut] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [logOut] is not added for execution", false);
			throw new SkipException(
					"Test case [logOut] is not added for execution");
		}
		logger.info("Starting [logOut] execution");
		logger.info("Navigating to [Accounts] page");
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
		m_assert.assertAll();
	}

	/**
	 * This method sign out from the application
	 */
	@AfterClass
	public void tearDown() {
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		SeleniumUtils.closeBrowser();
	}

	/**
	 * @param testcase
	 * @return Method return the params list based on the input testcase
	 */
	public Map<String, String> getTestData(String testcase) {
		if (analytics != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			testcases = analytics.getCase();
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
