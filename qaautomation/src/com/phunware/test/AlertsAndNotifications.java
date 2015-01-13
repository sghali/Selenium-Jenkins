package com.phunware.test;

import java.io.IOException;
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
import com.phunware.util.SeleniumCustomUtils;
import com.phunware.util.SeleniumUtils;
import com.phunware.util.SoftAssert;

/**
 * @author bhargavas
 * @since 03/10/2013 Executes Alerts & Notifications functionality
 * 
 */
@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class AlertsAndNotifications extends Suite {
	private static final Logger logger = Logger.getLogger(Analytics.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private String childSuite = "AlertsAndNotifications";
	private boolean suiteExecution = false;
	private static WebElement element = null;
	private static boolean isTextMatching;
	private static boolean isClicked;
	private SoftAssert m_assert;
	private Testcase alertsnotifications = null;
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
		// Check AlertsAndNotifications is added for execution
		for (String testSuite : scenarioslist) {
			if (childSuite.equalsIgnoreCase(testSuite)) {
				suiteExecution = true;
				break;
			}
		}
		// If not added then skip the suite
		if (!suiteExecution) {
			logger.warn("Test suite [AlertsAndNotifications] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test suite [AlertsAndNotifications] is not added for execution",
							false);
			throw new SkipException(
					"Test suite [AlertsAndNotifications] is not added for execution");
		}
		logger.info("reading AlertsNotifications Input file");
		// Read the AlertsNotifications Input file
		alertsnotifications = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.ALERTSANDNOTIFICATIONS_FILE,
				Testcase.class);
		if (alertsnotifications != null) {
			testcases = alertsnotifications.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					// Add the AlertsNotifications testcases in testcaseList
					testcaseList.add(testcase.getName());
				}
			}
		}
		// If testcaseList size is zero then skip the Test suite
		if (testcaseList.size() == 0) {
			logger.warn("No TestCase added for execution in [AlertsAndNotifications]");
			ReportUtils
					.setStepDescription(
							"No TestCase added for execution in [AlertsAndNotifications]",
							false);
			throw new SkipException(
					"No TestCase added for execution in [AlertsAndNotifications]");
		}
		logger.info("The testcases for execution in [Analytics]" + testcaseList);
		m_assert.assertAll();
	}

	/**
	 * Login into application
	 */
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
					+ " in [Alerts & Notifications]");
			ReportUtils.setStepDescription(
					"Test case [loginAs] is not added for execution"
							+ " in [Alerts & Notifications]", false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution"
							+ " in [Alerts & Notifications]");
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
	public void verifyInitialLayoutInAlerts() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check verifyAlertsOverviewLayout is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyInitialLayoutInAlerts")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyInitialLayoutInAlerts] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyInitialLayoutInAlerts] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyInitialLayoutInAlerts] is not added for execution");
		}
		logger.info("Starting [verifyInitialLayoutInAlerts] execution ");
		logger.info("Identify 'Alerts & Notifications' tab");
		// Identify Alerts & Notifications tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsNotificationsTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsNotificationsTabText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Alerts & Notifications' tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Alerts & Notifications' tab", true);
			m_assert.fail("Unable to identify 'Alerts & Notifications' tab");
		}
		// Get the text of the tab
		String AlertNotificationsTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAlertNotificationsTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsTabText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(AlertNotificationsTabText,
				ExpAlertNotificationsTabText);
		if (!isTextMatching) {
			logger.error("[Alerts & Notifications] tab text matching failed. "
					+ "The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsTabText").getExptext()
					+ "]  and the return text [" + AlertNotificationsTabText
					+ "] not equal");
			ReportUtils.setStepDescription(
					"[Alerts & Notifications] tab text matching failed", "",
					ExpAlertNotificationsTabText, AlertNotificationsTabText,
					true);
			m_assert.fail("[Alerts & Notifications] tab text matching failed. "
					+ "The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsTabText").getExptext()
					+ "]  and the return text [" + AlertNotificationsTabText
					+ "] not equal");
		}
		logger.info("Click on [Alerts & Notifications] tab");
		// Click
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [Alerts & Notifications] tab");
			ReportUtils.setStepDescription(
					"Unable to click on [Alerts & Notifications] tab", true);
			m_assert.fail("Unable to click on [Alerts & Notifications] tab");
		}
		SeleniumUtils.sleepThread(4);
		logger.info("Identify Overview tab");
		// Identify Overview tab
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsOverviewTabText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AlertsNotificationsOverviewTabText")
								.getLocatorvalue());
		// Get the text of the tab
		String OverviewTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpOverviewTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsOverviewTabText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Overview] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpOverviewTabText
					+ "]  and the return text ["
					+ OverviewTabText
					+ "] not equal");
			ReportUtils
					.setStepDescription(
							"[Overview] tab text matching failed in Alerts & Notifications",
							"", ExpOverviewTabText, OverviewTabText, true);
			m_assert.fail("[Overview] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpOverviewTabText
					+ "]  and the return text ["
					+ OverviewTabText
					+ "] not equal");
		}
		logger.info("Identify Compose tab");
		// Identify Compose tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatorvalue());
		// Get the text of the tab
		String ComposeTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpComposeTabText = Suite.objectRepositoryMap.get(
				"ComposeButtonTab").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Compose] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpComposeTabText
					+ "]  and the return text ["
					+ ComposeTabText
					+ "] not equal");
			ReportUtils
					.setStepDescription(
							"[Compose] tab text matching failed in Alerts & Notifications",
							"", ExpComposeTabText, ComposeTabText, true);
			m_assert.fail("[Compose] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpComposeTabText
					+ "]  and the return text ["
					+ ComposeTabText
					+ "] not equal");
		}
		logger.info("Identify Messages tab");
		// Identify Messages tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsNotificationsMessagesTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsNotificationsMessagesTab")
						.getLocatorvalue());
		// Get the text of the tab
		String MessagesTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpMessagesTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsMessagesTab").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Messages] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpMessagesTabText
					+ "]  and the return text ["
					+ MessagesTabText
					+ "] not equal");
			ReportUtils
					.setStepDescription(
							"[Messages] tab text matching failed in Alerts & Notifications",
							"", ExpMessagesTabText, MessagesTabText, true);
			m_assert.fail("[Messages] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpMessagesTabText
					+ "]  and the return text ["
					+ MessagesTabText
					+ "] not equal");
		}
		logger.info("Identify Segments tab");
		// Identify Segments tab
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AlertsNotificationsSegmentsTabText")
								.getLocatorvalue());
		// Get the text of the tab
		String SegementsTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpSegementsTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsSegmentsTabText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Segments] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpSegementsTabText
					+ "]  and the return text ["
					+ SegementsTabText
					+ "] not equal");
			ReportUtils
					.setStepDescription(
							"[Segments] tab text matching failed in Alerts & Notifications",
							"", ExpSegementsTabText, SegementsTabText, true);
			m_assert.fail("[Segments] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpSegementsTabText
					+ "]  and the return text ["
					+ SegementsTabText
					+ "] not equal");
		}
		logger.info("Identify Feeds tab");
		// Identify Feeds tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("AlertsNotificationsFeedsTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AlertsNotificationsFeedsTabText")
						.getLocatorvalue());
		// Get the text of the tab
		String FeedsTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFeedsTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsFeedsTabText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Feeds] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpFeedsTabText
					+ "]  and the return text [" + FeedsTabText + "] not equal");
			ReportUtils
					.setStepDescription(
							"[Feeds] tab text matching failed in Alerts & Notifications",
							"", ExpFeedsTabText, FeedsTabText, true);
			m_assert.fail("[Feeds] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpFeedsTabText
					+ "]  and the return text [" + FeedsTabText + "] not equal");
		}
		logger.info("Identify Configure tab");
		// Identify Configure tab
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsConfigureTabText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AlertsNotificationsConfigureTabText")
								.getLocatorvalue());
		// Get the text of the tab
		String ConfigureTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpConfigureTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsConfigureTabText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Configure] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpConfigureTabText
					+ "]  and the return text ["
					+ ConfigureTabText
					+ "] not equal");
			ReportUtils
					.setStepDescription(
							"[Configure] tab text matching failed in Alerts & Notifications",
							"", ExpConfigureTabText, ConfigureTabText, true);
			m_assert.fail("[Configure] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpConfigureTabText
					+ "]  and the return text ["
					+ ConfigureTabText
					+ "] not equal");
		}
		logger.info("Identify Support tab");
		// Identify Support tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSupportTabText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSupportTabText").getLocatorvalue());
		// Get the text of the tab
		String SupportTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpSupportTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsSupportTabText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OverviewTabText,
				ExpOverviewTabText);
		if (!isTextMatching) {
			logger.error("[Support] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpSupportTabText
					+ "]  and the return text ["
					+ SupportTabText
					+ "] not equal");
			ReportUtils
					.setStepDescription(
							"[Support] tab text matching failed in Alerts & Notifications",
							"", ExpSupportTabText, SupportTabText, true);
			m_assert.fail("[Support] tab text matching failed in Alerts & Notifications. "
					+ "The Expected text  ["
					+ ExpSupportTabText
					+ "]  and the return text ["
					+ SupportTabText
					+ "] not equal");
		}
		logger.info("Verify the landing page in [Alerts & Notifications]tab");
		// Identify Landing Page header element
		logger.info("Identify Landing Page header element in [Alerts & Notifications] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String OverviewPageHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpOverviewPageHeaderText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsOverviewTabHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(OverviewPageHeaderText,
				ExpOverviewPageHeaderText);
		if (!isTextMatching) {
			logger.error("Landing Page error in [Alerts & Notifications] tab. The Expected page is ["
					+ ExpOverviewPageHeaderText
					+ "]  and the return page is ["
					+ OverviewPageHeaderText + "]");
			ReportUtils.setStepDescription(
					"Landing Page error in [Alerts & Notifications] tab", "",
					ExpOverviewPageHeaderText, OverviewPageHeaderText, true);
			m_assert.fail("Landing Page error in [Alerts & Notifications] tab. The Expected page is ["
					+ ExpOverviewPageHeaderText
					+ "]  and the return page is ["
					+ OverviewPageHeaderText + "]");
		}
		logger.info("User is on [Alerts & Notifications] page");
		m_assert.assertAll();
	}

	/**
	 * Executes Overview Layout functionality
	 */
	@Test(priority = 2, dependsOnMethods = { "loginAs" })
	public void verifyAlertsOverviewLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check verifyAlertsOverviewLayout is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyAlertsOverviewLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyAlertsOverviewLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyAlertsOverviewLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyAlertsOverviewLayout] is not added for execution");
		}
		logger.info("Starting [verifyAlertsOverviewLayout] execution ");
		logger.info("Verify if User is on [Overview] page");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String OverviewPageHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpOverviewPageHeaderText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsOverviewTabHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(OverviewPageHeaderText,
				ExpOverviewPageHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Overview] page");
			// Identify Alerts & Notifications tab
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AlertsNotificationsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AlertsNotificationsTabText")
							.getLocatorvalue());
			// Get the text of the tab
			String AlertNotificationsTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpAlertNotificationsTabText = Suite.objectRepositoryMap
					.get("AlertsNotificationsTabText").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(
					AlertNotificationsTabText, ExpAlertNotificationsTabText);
			if (!isTextMatching) {
				logger.error("[Alerts & Notifications] tab text matching failed. "
						+ "The Expected text  ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsTabText").getExptext()
						+ "]  and the return text ["
						+ AlertNotificationsTabText + "] not equal");
				ReportUtils.setStepDescription(
						"[Alerts & Notifications] tab text matching failed",
						"", ExpAlertNotificationsTabText,
						AlertNotificationsTabText, true);
				m_assert.fail("[Alerts & Notifications] tab text matching failed. "
						+ "The Expected text  ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsTabText").getExptext()
						+ "]  and the return text ["
						+ AlertNotificationsTabText + "] not equal");
			}
			logger.info("Click on [Alerts & Notifications] tab");
			// Click
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Alerts & Notifications] tab");
				ReportUtils
						.setStepDescription(
								"Unable to click on [Alerts & Notifications] tab",
								true);
				m_assert.fail("Unable to click on [Alerts & Notifications] tab");
			}
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabHeaderText")
							.getLocatorvalue());
			// Get the text
			OverviewPageHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpOverviewPageHeaderText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsOverviewTabHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(OverviewPageHeaderText,
					ExpOverviewPageHeaderText);
			if (!isTextMatching) {
				logger.error("Landing Page error in [Alerts & Notifications] tab. The Expected page is ["
						+ ExpOverviewPageHeaderText
						+ "]  and the return page is ["
						+ OverviewPageHeaderText + "]");
				ReportUtils.setStepDescription(
						"Landing Page error in [Alerts & Notifications] tab",
						"", ExpOverviewPageHeaderText, OverviewPageHeaderText,
						true);
				m_assert.fail("Landing Page error in [Alerts & Notifications] tab. The Expected page is ["
						+ ExpOverviewPageHeaderText
						+ "]  and the return page is ["
						+ OverviewPageHeaderText + "]");
			}
		}
		logger.info("User is on [Overview] page");
		logger.info("Identify application and calandar dropdowns");
		// Identify Application drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown in [Overview] "
					+ "sub-tab of [Alerts & Notifications]");
			ReportUtils.setStepDescription(
					"Unable to identify application dropdown in [Overview] "
							+ "sub-tab of [Alerts & Notifications]", true);
			m_assert.fail("Unable to identify application dropdown in [Overview] "
					+ "sub-tab of [Alerts & Notifications]");
		}
		// Identify Calandar drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabCalandarDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabCalandarDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify calandar dropdown in [Overview] "
					+ "sub-tab of [Alerts & Notifications]");
			ReportUtils.setStepDescription(
					"Unable to identify calandar dropdown in [Overview] "
							+ "sub-tab of [Alerts & Notifications]", true);
			m_assert.fail("Unable to identify calandar dropdown in [Overview] "
					+ "sub-tab of [Alerts & Notifications]");
		}
		m_assert.assertAll();
	}

	/**
	 * Executes functional flow of Overview tab
	 */
	@Test(priority = 3, dependsOnMethods = { "loginAs" })
	public void verifyOverviewData() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check verifyOverviewData is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
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
							true);
			throw new SkipException(
					"Test case [verifyOverviewData] is not added for execution");
		}
		// Get the parameters
		testcaseArgs = getTestData("verifyOverviewData");
		logger.info("Starting [verifyOverviewData] execution");
		logger.info("Verify if User is on [Overview] page in [Alerts & Notifications]");
		// Identify header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String OverviewTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				OverviewTabHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Overview] page in [Alerts & Notifications]");
			logger.info("Navigate to [Overview] page");
			logger.info("Identify [Overview] sub-tab");
			// Identify Overview tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabText")
							.getLocatorvalue());
			// Get the text
			String OverviewTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					OverviewTabText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabText").getExptext());
			if (!isTextMatching) {
				logger.error("[Alerts & Notifications- Overview] tab text matching failed. The Expected text  ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsOverviewTabText")
								.getExptext()
						+ "]  and the return text ["
						+ OverviewTabText + "] not equal");
				ReportUtils
						.setStepDescription(
								"[Alerts & Notifications- Overview] tab text matching failed",
								"",
								Suite.objectRepositoryMap.get(
										"AlertsNotificationsOverviewTabText")
										.getExptext(), OverviewTabText, true);
				m_assert.fail("The Expected text  ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsOverviewTabText")
								.getExptext() + "]  and the return text ["
						+ OverviewTabText + "] not equal");
			}
			logger.info("Click on [Overview] tab in [Alerts & Notifications]");
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabHeaderText")
							.getLocatorvalue());
			// Get the text
			OverviewTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					OverviewTabHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsOverviewTabHeaderText")
							.getExptext());

			if (!isTextMatching) {
				logger.error("[Alerts & Notifications- Overview] tab header text matching failed");
				logger.error("The Expected text  ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsOverviewTabHeaderText")
								.getExptext() + "]  and the return text ["
						+ OverviewTabHeaderText + "] not equal");
				ReportUtils
						.setStepDescription(
								"[Alerts & Notifications- Overview] tab header text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("AlertsNotificationsOverviewTabHeaderText")
										.getExptext(), OverviewTabHeaderText,
								true);
				m_assert.fail("The Expected text  ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsOverviewTabHeaderText")
								.getExptext() + "]  and the return text ["
						+ OverviewTabHeaderText + "] not equal");
			}
		}
		logger.info("Selecting applicaton and calandar from application and calandar dropdowns resp.");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsOverviewTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		SeleniumUtils.sleepThread(4);
		/*
		 * logger.info("Verify Push Enabled field"); element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabPushEnabledField").getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabPushEnabledField").getLocatorvalue()); if (element
		 * == null) { logger.error("Alerts & Notifications - Overview : " +
		 * "unable to identify Push Enabled element ");
		 * SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() + "_"
		 * + "verifyOverviewData" + "_" + count++);
		 * m_assert.fail("Alerts & Notifications - Overview : " +
		 * "unable to identify Push Enabled element "); } String PushEnabledText
		 * = SeleniumUtils.getText(element); isTextMatching =
		 * SeleniumUtils.containsText(Suite.objectRepositoryMap
		 * .get("AlertsOverviewTabPushEnabledField").getExptext(),
		 * PushEnabledText); if (!isTextMatching) { logger.error(
		 * "Alerts & Notifications - Overview : Push Enabled element text matching failed"
		 * ); logger.error("The Expected text  [" +
		 * Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabPushEnabledField").getExptext() +
		 * "]  and the return text [" + PushEnabledText + "] not equal");
		 * SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() + "_"
		 * + "verifyAlertsOverviewLayout" + "_" + count++);
		 * m_assert.fail("The Expected text  [" + Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabPushEnabledField").getExptext() +
		 * "]  and the return text [" + PushEnabledText + "] not equal"); } int
		 * PushEnabledData = Integer.parseInt(PushEnabledText
		 * .split("(?=\\d+)")[1]); if (PushEnabledData == 0) {
		 * logger.error("Alerts & Notifications - Overview : Push Enabled data is 0"
		 * ); SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() +
		 * "_" + "verifyAlertsOverviewLayout" + "_" + count++); //
		 * m_assert.fail( //
		 * "Alerts & Notifications - Overview : Push Enabled data is 0"); }
		 * logger.info("Verification of Push Enabled field is successful");
		 * logger.info("Verify Total Pushes Sent field"); element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesSentField") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesSentField") .getLocatorvalue()); if
		 * (element == null) { logger.error(
		 * "Alerts & Notifications - Overview : unable to identify Total Pushes Sent element "
		 * ); SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() +
		 * "_" + "verifyOverviewData" + "_" + count++); m_assert.fail(
		 * "Alerts & Notifications - Overview : unable to identify Total Pushes Sent element "
		 * ); } String PushesSentText = SeleniumUtils.getText(element);
		 * isTextMatching = SeleniumUtils.containsText(Suite.objectRepositoryMap
		 * .get("AlertsOverviewTabTotalPushesSentField").getExptext(),
		 * PushesSentText); if (!isTextMatching) { logger.error(
		 * "Alerts & Notifications - Overview : Total Pushes Sent element text matching failed"
		 * ); logger.error("The Expected text  [" +
		 * Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesSentField") .getExptext() +
		 * "]  and the return text [" + PushesSentText + "] not equal");
		 * SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() + "_"
		 * + "verifyAlertsOverviewLayout" + "_" + count++);
		 * m_assert.fail("The Expected text  [" + Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesSentField") .getExptext() +
		 * "]  and the return text [" + PushesSentText + "] not equal"); } int
		 * PushesSentData = Integer
		 * .parseInt(PushesSentText.split("(?=\\d+)")[1]); if (PushesSentData ==
		 * 0) {
		 * logger.error("Alerts & Notifications - Overview : Pushes Sent data is 0"
		 * ); SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() +
		 * "_" + "verifyAlertsOverviewLayout" + "_" + count++);
		 * m_assert.fail("Alerts & Notifications - Overview : Pushes Sent data is 0"
		 * ); }
		 * logger.info("Verification of Total Pushes Sent field is successful");
		 * logger.info("Verify Total Pushes Opened field"); element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesOpenedField") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesOpenedField") .getLocatorvalue()); if
		 * (element == null) { logger.error(
		 * "Alerts & Notifications - Overview : unable to identify Total Pushes Opened element "
		 * ); SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() +
		 * "_" + "verifyOverviewData" + "_" + count++); m_assert.fail(
		 * "Alerts & Notifications - Overview : unable to identify Total Pushes Opened element "
		 * ); } String PushesOpenedText = SeleniumUtils.getText(element);
		 * isTextMatching = SeleniumUtils.containsText(Suite.objectRepositoryMap
		 * .get("AlertsOverviewTabTotalPushesOpenedField").getExptext(),
		 * PushesOpenedText); if (!isTextMatching) { logger.error(
		 * "Alerts & Notifications - Overview : Total Pushes Sent element text matching failed"
		 * ); logger.error("The Expected text  [" +
		 * Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesOpenedField") .getExptext() +
		 * "]  and the return text [" + PushesOpenedText + "] not equal");
		 * SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() + "_"
		 * + "verifyAlertsOverviewLayout" + "_" + count++);
		 * m_assert.fail("The Expected text  [" + Suite.objectRepositoryMap.get(
		 * "AlertsOverviewTabTotalPushesOpenedField") .getExptext() +
		 * "]  and the return text [" + PushesOpenedText + "] not equal"); } //
		 * int //
		 * PushesOpenedData=Integer.parseInt(PushesOpenedText.split("(?=\\d+)"
		 * )[1]);
		 * logger.info("Verification of Total Pushes Opened field is successful"
		 * ); logger.info("Test case [verifyOverviewData] is successful");
		 */
		m_assert.assertAll();
	}

	/**
	 * Executes Compose Message Layout functionality
	 */
	@Test(priority = 4, dependsOnMethods = { "loginAs" })
	public void verifyComposeMessageLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyComposeMessageLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyComposeMessageLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyComposeMessageLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyComposeMessageLayout] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("verifyComposeMessageLayout");
		logger.info("Starting [verifyComposeMessageLayout] execution");
		// Identify Compose button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Compose button in Alerts & Notifications");
			ReportUtils
					.setStepDescription(
							"Unable to identify Compose button in Alerts & Notifications",
							true);
			m_assert.fail("Unable to identify Compose button in Alerts & Notifications");
		}
		// Click on Compose
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Verify if Create New Message page is displayed");
		// Identify Compose button header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ComposeHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("[Create a New Message] page header element text matching failed. "
					+ "The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext()
					+ "]  and the return text ["
					+ ComposeHeaderText + "] not equal");
			ReportUtils
					.setStepDescription(
							"[Create a New Message] page header element text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsComposeTabHeaderText")
									.getExptext(), ComposeHeaderText, true);
			m_assert.fail("[Create a New Message] page header element text matching failed. "
					+ "The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext()
					+ "]  and the return text ["
					+ ComposeHeaderText + "] not equal");
		}
		// Identify Select an application header
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectAppFieldText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectAppFieldText")
						.getLocatorvalue());
		// Get the text
		String SelectApplicationText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SelectApplicationText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectAppFieldText")
						.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Select an application' header text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabSelectAppFieldText")
							.getExptext()
					+ "]  and the return text ["
					+ SelectApplicationText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Select an application' header text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabSelectAppFieldText")
									.getExptext(), SelectApplicationText, true);
			m_assert.fail("'Select an application' header text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabSelectAppFieldText")
							.getExptext()
					+ "]  and the return text ["
					+ SelectApplicationText + "] not equal");
		}
		// Identify application dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown in [Create a New Message] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify application dropdown in [Create a New Message] page",
							true);
			m_assert.fail("Unable to identify application dropdown in [Create a New Message] page");
		}
		// Select Message Type
		logger.info("Verification of Select Message Type field");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectMsgTypeFieldText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectMsgTypeFieldText")
						.getLocatorvalue());
		// Get the text
		String SelectMessageText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SelectMessageText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectMsgTypeFieldText")
						.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Select Message Type' header text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsComposeTabSelectMsgTypeFieldText")
							.getExptext()
					+ "]  and the return text ["
					+ SelectMessageText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Select Message Type' header text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabSelectMsgTypeFieldText")
									.getExptext(), SelectMessageText, true);
			m_assert.fail("'Select Message Type' header text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsComposeTabSelectMsgTypeFieldText")
							.getExptext()
					+ "]  and the return text ["
					+ SelectMessageText + "] not equal");
		}
		// Now radio button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeManualField")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeManualField")
								.getLocatorvalue());
		// Get the text
		String SelectMessageManualText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						SelectMessageManualText,
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeManualField")
								.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Now' radio button text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsComposeTabSelectMsgTypeManualField")
							.getExptext()
					+ "]  and the return text ["
					+ SelectMessageManualText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Now' radio button text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabSelectMsgTypeManualField")
									.getExptext(), SelectMessageManualText,
							true);
			m_assert.fail("'Now' radio button text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsComposeTabSelectMsgTypeManualField")
							.getExptext()
					+ "]  and the return text ["
					+ SelectMessageManualText + "] not equal");
		}
		// Scheduled radio button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledField")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledField")
								.getLocatorvalue());
		// Get the text
		String SelectMessageScheduledText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						SelectMessageScheduledText,
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledField")
								.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Scheduled' radio button text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledField")
							.getExptext()
					+ "]  and the return text ["
					+ SelectMessageScheduledText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Scheduled' radio button text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledField")
									.getExptext(), SelectMessageScheduledText,
							true);
			m_assert.fail("'Scheduled' radio button text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledField")
							.getExptext()
					+ "]  and the return text ["
					+ SelectMessageScheduledText + "] not equal");
		}
		// Select Segments
		logger.info("Identification  of Select Segments");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsField")
						.getLocatorvalue());
		// Get the text
		String SelectSegmentsText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SelectSegmentsText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsField")
						.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Select Segments' header element text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabSelectSegmentsField")
							.getExptext()
					+ "]  and the return text ["
					+ SelectSegmentsText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Select Segments' header element text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabSelectSegmentsField")
									.getExptext(), SelectSegmentsText, true);
			m_assert.fail("'Select Segments' header element text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabSelectSegmentsField")
							.getExptext()
					+ "]  and the return text ["
					+ SelectSegmentsText + "] not equal");
		}
		// Segments dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Select Segments dropdown in [Create a New Message] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Select Segments dropdown in [Create a New Message] page",
							true);
			m_assert.fail("Unable to identify Select Segments dropdown in [Create a New Message] page");
		}
		// Enter Message
		logger.info("Identification of Enter Message field");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabEnterMessageField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabEnterMessageField")
						.getLocatorvalue());
		// Get the text
		String EnterMessageText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				EnterMessageText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabEnterMessageField")
						.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Enter Message' header element text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabEnterMessageField")
							.getExptext()
					+ "]  and the return text ["
					+ EnterMessageText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Enter Message' header element text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabEnterMessageField")
									.getExptext(), EnterMessageText, true);
			m_assert.fail("'Enter Message' header element text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabEnterMessageField")
							.getExptext()
					+ "]  and the return text ["
					+ EnterMessageText + "] not equal");
		}
		// Enter Message Text Area
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabMsgTextArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabMsgTextArea")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Enter Message Text Area in [Create a New Message] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Enter Message Text Area in [Create a New Message] page",
							true);
			m_assert.fail("Unable to identify Enter Message Text Area in [Create a New Message] page");
		}
		logger.info("Verification of SendNow and Cancel buttons");
		// Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Cancel button in [Create a New Message] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Cancel button in [Create a New Message] page",
							true);
			m_assert.fail("Unable to identify Cancel button in [Create a New Message] page");
		}
		// Send Now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Send Now button in [Create a New Message] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Send Now button in [Create a New Message] page",
							true);
			m_assert.fail("Unable to identify Send Now button in [Create a New Message] page");
		}
		// Optional Data
		logger.info("Identification of Optional Data");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabOptionalDataText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabOptionalDataText")
						.getLocatorvalue());
		// Get the text
		String OptionalDataText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				OptionalDataText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabOptionalDataText")
						.getExptext());
		if (!isTextMatching) {
			logger.error(" 'Optional Data' header element text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabOptionalDataText")
							.getExptext()
					+ "]  and the return text ["
					+ OptionalDataText + "] not equal");
			ReportUtils
					.setStepDescription(
							"'Optional Data' header element text matching failed in [Create a New Message]",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsComposeTabOptionalDataText")
									.getExptext(), OptionalDataText, true);
			m_assert.fail("'Optional Data' header element text matching failed in [Create a New Message]. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabOptionalDataText")
							.getExptext()
					+ "]  and the return text ["
					+ OptionalDataText + "] not equal");
		}
		// Optional Data Key/Value Pair
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabOptionalDataKeyValuePair")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabOptionalDataKeyValuePair")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Optional Data Key/Value Pair in [Create a New Message] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Optional Data Key/Value Pair in [Create a New Message] page",
							true);
			m_assert.fail("Unable to identify Optional Data Key/Value Pair in [Create a New Message] page");
		}
		m_assert.assertAll();
	}

	/**
	 * Executes validations in Compose Message tab
	 */
	@Test(priority = 5, dependsOnMethods = { "loginAs" })
	public void validationsInCreateManualMessage() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationsInCreateManualMessage")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [validationsInCreateManualMessage] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [validationsInCreateManualMessage] is not added for execution",
							true);
			throw new SkipException(
					"Test case [validationsInCreateManualMessage] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("validationsInCreateManualMessage");
		logger.info("Starting [validationsInCreateManualMessage] execution");
		logger.info("Verify if user is on Create New Message page");
		// Identify Create a New Message header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ComposeHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.warn("User is not in Create a new Message page");
			logger.info("Performing click operation on Compose button");
			// Identify Compose button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ComposeButtonTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ComposeButtonTab")
							.getLocatorvalue());
			// Click on Compose button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Create a New Message header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getLocatorvalue());
			// Get the text
			ComposeHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					ComposeHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Create a New Message] header element text matching failed : Expected text is["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ComposeHeaderText + "]");
				ReportUtils
						.setStepDescription(
								"[Create a New Message] header element text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("AlertsNotificationsComposeTabHeaderText")
										.getExptext(), ComposeHeaderText, true);
				m_assert.fail("[Create a New Message] header element text matching failed : Expected text is["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ComposeHeaderText + "]");
			}
		}
		logger.info("Keep all fields blank and click on Send Now button");
		// Identify Send Now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify Create a New Message header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ComposeHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Validation failed at application dropdown in [Create a New Message] page. "
					+ "User able to create new message without selection of application");
			ReportUtils
					.setStepDescription(
							"Validation failed at application dropdown in [Create a New Message] page."
									+ " User able to create new message without selection of application",
							true);
			m_assert.fail("Validation failed at application dropdown in [Create a New Message] page."
					+ " User able to create new message without selection of application");
		}
		// Select application in dropdown
		logger.info("Select application from application dropdown");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		logger.info("Click on Send Now button");
		// Identify Send Now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify Create a New Message header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ComposeHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Validation failed at Enter Message text area in [Create a New Message] page. "
					+ "User able to create new message without any message");
			ReportUtils
					.setStepDescription(
							"Validation failed at Enter Message text area in [Create a New Message] page. "
									+ "User able to create new message without any message",
							true);
			m_assert.fail("Validation failed at Enter Message text area in [Create a New Message] page. "
					+ "User able to create new message without any message");
		}
		m_assert.assertAll();
	}

	/**
	 * Creation of New Manual Message
	 */
	@Test(priority = 6, dependsOnMethods = { "loginAs" })
	public void createNewManualMessage() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewManualMessage")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [createNewManualMessage] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [createNewManualMessage] is not added for execution",
							false);
			throw new SkipException(
					"Test case [createNewManualMessage] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("createNewManualMessage");
		logger.info("Starting [createNewManualMessage] execution");
		logger.info("Verify if user is on Create New Message page");
		// Identify Create a New Message header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ComposeHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.warn("User is not in Create a new Message page");
			logger.info("Performing click operation on Compose button");
			// Identify Compose button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ComposeButtonTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ComposeButtonTab")
							.getLocatorvalue());
			// Click on Compose button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Create a New Message header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getLocatorvalue());
			// Get the text
			ComposeHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					ComposeHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Create a New Message] header element text matching failed : "
						+ "Expected text is["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ComposeHeaderText + "]");
				ReportUtils
						.setStepDescription(
								"[Create a New Message] header element text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("AlertsNotificationsComposeTabHeaderText")
										.getExptext(), ComposeHeaderText, true);
				m_assert.fail("[Create a New Message] header element text matching failed : "
						+ "Expected text is["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ComposeHeaderText + "]");
			}
		}
		logger.info("User is on Create a New Message page");
		logger.info("Select application from application dropdown");
		// Select application from application dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from application dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		logger.info("Select segment from Segment dropdown");
		SeleniumUtils.sleepThread(2);
		// Select Segment
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsDropdown")
						.getLocatorvalue(), testcaseArgs.get("segment"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("segment")
					+ "] from Segment dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("segment")
							+ "] from Segment dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("segment")
					+ "] from Segment dropdown");
		}
		// Enter Message text area
		logger.info("Enter message in text area box");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabMsgTextArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabMsgTextArea")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unabe to identify Compose tab message field text box");
			ReportUtils.setStepDescription(
					"Unabe to identify Compose tab message field text box",
					true);
			m_assert.fail("Unabe to identify Compose tab message field text box");
		}
		// Enter message text
		SeleniumUtils.type(element, testcaseArgs.get("messageText"));
		SeleniumUtils.sleepThread(3);
		logger.info("Verify the entered text is dipslyaed in Notification");
		// Verify the entered text in Notifications
		// Identify Notifications
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabNotificationText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabNotificationText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unabe to identify Compose tab Notifications message field in "
					+ "[Create a New Message] page");
			ReportUtils.setStepDescription(
					"Unabe to identify Compose tab Notifications message field in "
							+ "[Create a New Message] page", true);
			m_assert.fail("Unabe to identify Compose tab Notifications message field in "
					+ "[Create a New Message] page");
		}
		// Get the text
		String NotificationMessageText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(NotificationMessageText,
				testcaseArgs.get("messageText"));
		if (!isTextMatching) {
			logger.error("Typed message text in Enter Message text area is not matched with "
					+ "displayed text in Notification message text area. "
					+ "The Expected text is ["
					+ testcaseArgs.get("messageText")
					+ "] and the return text is ["
					+ NotificationMessageText
					+ "]");
			ReportUtils
					.setStepDescription(
							"Typed message text in Enter Message text area is not matched with "
									+ "displayed text in Notification message text area",
							"", testcaseArgs.get("messageText"),
							NotificationMessageText, true);
			m_assert.fail("Typed message text in Enter Message text area is not matched with "
					+ "displayed text in Notification message text area. "
					+ "The Expected text is ["
					+ testcaseArgs.get("messageText")
					+ "] and the return text is ["
					+ NotificationMessageText
					+ "]");
		}
		logger.info("Click on Send Now button");
		// Identify Send Now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		// Click on Send Now
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// Identify the successage mesage
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANComposeSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANComposeSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String successfulmessagetext = SeleniumUtils.getText(element)
				.substring(1).trim();
		// Get the exp text
		String expsuccessfulmessagetext = Suite.objectRepositoryMap.get(
				"ANComposeSuccessMsg").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(successfulmessagetext,
				expsuccessfulmessagetext);
		if (!isTextMatching) {
			logger.error("New Message success text matching failed."
					+ "The Expected text is [" + expsuccessfulmessagetext
					+ "] and the return text is [" + successfulmessagetext
					+ "]");
			ReportUtils.setStepDescription(
					"New Message success text matching failed", "",
					expsuccessfulmessagetext, successfulmessagetext, true);
			m_assert.fail("New Message success text matching failed."
					+ "The Expected text is [" + expsuccessfulmessagetext
					+ "] and the return text is [" + successfulmessagetext
					+ "]");

		}
		logger.info("Verify the landing page after creating of new message");
		// Identify Message page header element
		// Wait till the element is visible
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String MessagesTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MessagesTabHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Messages] page header element text matching failed "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getExptext() + "] and the return text is ["
					+ MessagesTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Messages] page header element text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getExptext(), MessagesTabHeaderText, true);
			m_assert.fail("[Messages] page header element text matching failed "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getExptext()
					+ "] and the return text is ["
					+ MessagesTabHeaderText + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 7, dependsOnMethods = { "loginAs" })
	public void validationOfSortingOnDateField() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationOfSortingOnDateField")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [validationOfSortingOnDateField] is not "
					+ "added for execution.");
			ReportUtils.setStepDescription(
					"Test case [validationOfSortingOnDateField] is not "
							+ "added for execution", true);
			throw new SkipException(
					"Test case [validationOfSortingOnDateField] is not "
							+ "added for execution");
		}
		// read param data
		testcaseArgs = getTestData("validationOfSortingOnDateField");
		logger.info("Starting [validationOfSortingOnDateField] execution");
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(4);
		logger.info("Verify if the user is on Messages page");
		// Identify Message tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String MessagesTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MessagesTabHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on Messages page");
			logger.info("Click on Messages tab");
			// Identify Messages tab
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsMessagesTab")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsMessagesTab")
									.getLocatorvalue());
			// Click on Messages tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Messages tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			MessagesTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					MessagesTabHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Messages] page header element text matching failed "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ MessagesTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Messages] page header element text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext(), MessagesTabHeaderText, true);
				m_assert.fail("[Messages] page header element text matching failed "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ MessagesTabHeaderText + "]");
			}
		}
		logger.info("Select application from application dropdown");
		// Select application from application dropdown
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		m_assert.assertAll();
	}

	@Test(priority = 8, dependsOnMethods = { "loginAs" })
	public void validationOfSortingOnMessagesField() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationOfSortingOnMessagesField")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [validationOfSortingOnMessagesField] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [validationOfSortingOnMessagesField] is not added for execution",
							true);
			throw new SkipException(
					"Test case [validationOfSortingOnMessagesField] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("validationOfSortingOnMessagesField");
		logger.info("Starting [validationOfSortingOnMessagesField] execution");
		logger.info("Verify if the user is on Messages page");
		// Identify Message tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String MessagesTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MessagesTabHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on Messages page");
			logger.info("Click on Messages tab");
			// Identify Messages tab
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsMessagesTab")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsMessagesTab")
									.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Messages tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			MessagesTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					MessagesTabHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Messages] page header element text matching failed "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ MessagesTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Messages] page header element text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext(), MessagesTabHeaderText, true);
				m_assert.fail("[Messages] page header element text matching failed "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ MessagesTabHeaderText + "]");
			}
		}
		SeleniumUtils.sleepThread(3);
		logger.info("Select application from application dropdown");
		// Select application from application drop down
		/*
		 * isSelected = SeleniumUtils.selectDropdownByText(
		 * Suite.objectRepositoryMap.get(
		 * "AlertsNotificationsMessagesTabAppDropdown") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "AlertsNotificationsMessagesTabAppDropdown") .getLocatorvalue(),
		 * testcaseArgs.get("application")); if (!isSelected) {
		 * logger.error("Unable to select  [" + testcaseArgs.get("application")
		 * + "] from dropdown"); ReportUtils.setStepDescription(
		 * "Unable to select  [" + testcaseArgs.get("application") +
		 * "] from dropdown", true); m_assert.fail("Unable to select  [" +
		 * testcaseArgs.get("application") + "] from dropdown"); }
		 */
		m_assert.assertAll();
	}

	/**
	 * validations in Create schedule message
	 */
	@Test(priority = 9, dependsOnMethods = { "loginAs" })
	public void validationsInCreateScheduleMessage() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationsInCreateScheduleMessage")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [validationsInCreateScheduleMessage] is not "
					+ "added for execution.");
			ReportUtils.setStepDescription(
					"Test case [validationsInCreateScheduleMessage] is not "
							+ "added for execution", false);
			throw new SkipException(
					"Test case [validationsInCreateScheduleMessage] is not "
							+ "added for execution");
		}
		// read param data
		testcaseArgs = getTestData("validationsInCreateScheduleMessage");
		logger.info("Starting [validationsInCreateScheduleMessage] execution");
		// Identify Compose button tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatorvalue());
		logger.info("Click on Compose button");
		// click on compose button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if user is on Create a New Message page");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ComposeHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Alerts & Notifications - Compose tab : Header text is not matched");
			logger.error("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext() + "]  and the return text ["
					+ ComposeHeaderText + "] not equal");
			ReportUtils
					.setStepDescription(
							"Alerts & Notifications - Compose tab : Header text is not matched",
							"",
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsComposeTabHeaderText")
									.getExptext(), ComposeHeaderText, true);
			m_assert.fail("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext() + "]  and the return text ["
					+ ComposeHeaderText + "] not equal");
		}
		logger.info("Verificatiion successful : user is on Create a New Message page");
		logger.info("Click operation on Compose button is successful");
		// Identify Schedule radio button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledradioBtn")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledradioBtn")
								.getLocatorvalue());
		// Click on radio button
		SeleniumUtils.clickOnElement(element);
		// Identify Calandar icon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledCalandarIcon")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledCalandarIcon")
						.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify Calandar Icon after selection of Scheduled "
					+ "radio button in [Craete a New Message] page");
			ReportUtils.setStepDescription(
					"Unable to identify Calandar Icon after selection of Scheduled "
							+ "radio button in [Craete a New Message] page",
					true);
			m_assert.fail("Unable to identify Calandar Icon after selection of Scheduled "
					+ "radio button in [Craete a New Message] page");
		}
		// Identify Send Now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		logger.info("Click on Send Now button");
		// click on Send Now button
		SeleniumUtils.clickOnElement(element);
		logger.info("Validate the mandatory field at Selection of application");
		// Identify Create a New Message header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ComposeHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Validation failed at Select an application in Create a New Message page. "
					+ "Users can craete a schedule message without selection of application");
			ReportUtils
					.setStepDescription(
							"Validation failed at Select an application in Create a New Message page. "
									+ "Users can craete a schedule message without selection of application",
							true);
			m_assert.fail("Validation failed at Select an application in Create a New Message page. "
					+ "Users can craete a schedule message without selection of application");
		}
		logger.info("Select application from application dropdown");
		// Select application from application dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		logger.info("Selection of application from application dropdown is successful");
		// Identify Send Now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		logger.info("Click on Send Now button");
		// click on Send Now button
		SeleniumUtils.clickOnElement(element);
		// Identify Create a New Message header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ComposeHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Validation failed at Enter Message in Create a New Message page. "
					+ "Users can craete a schedule message without text in Enter Message field");
			ReportUtils
					.setStepDescription(
							"Validation failed at Enter Message in Create a New Message page. "
									+ "Users can craete a schedule message without text in Enter Message field",
							true);
			m_assert.fail("Validation failed at Enter Message in Create a New Message page. "
					+ "Users can craete a schedule message without text in Enter Message field");
		}
		logger.info("Verification is successful : user is on Create a New Message page");
		m_assert.assertAll();
	}

	/**
	 * Creation of Scheduled message
	 */
	@Test(priority = 10, dependsOnMethods = { "loginAs" })
	public void createScheduleMessage() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case is added for execution
		boolean forExecution = false;
		boolean isMessagePresent = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createScheduleMessage")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [createScheduleMessage] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [createScheduleMessage] is not added for execution",
							false);
			throw new SkipException(
					"Test case [createScheduleMessage] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("createScheduleMessage");
		logger.info("Starting [createScheduleMessage] execution");
		SeleniumUtils.sleepThread(2);
		// Identify Compose button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ComposeButtonTab")
						.getLocatorvalue());
		logger.info("Click on Compose button");
		// Click on Compose button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Verify if user is on Create a New Message page");
		// Identify Create a New Message header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabHeaderText")
						.getLocatorvalue());
		// Get the t ext
		String ComposeHeaderText = SeleniumUtils.getText(element);
		// Compare the texts
		isTextMatching = SeleniumUtils
				.assertEqual(
						ComposeHeaderText,
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsComposeTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Alerts & Notifications - Compose tab : Header text is not matched. The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext()
					+ "]  and the return text ["
					+ ComposeHeaderText + "] not equal");
			ReportUtils
					.setStepDescription(
							"Alerts & Notifications - Compose tab : Header text is not matched",
							"",
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsComposeTabHeaderText")
									.getExptext(), ComposeHeaderText, true);
			m_assert.fail("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsComposeTabHeaderText")
							.getExptext() + "]  and the return text ["
					+ ComposeHeaderText + "] not equal");
		}
		// Select application from application dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdwon");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from dropdwon", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdwon");
		}
		logger.info("Selection of application from application dropdown is successful");
		logger.info("Select Schedule radio button");
		// Identify Schedule radio button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledradioBtn")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabSelectMsgTypeScheduledradioBtn")
								.getLocatorvalue());
		// Click on Schedule radio button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		logger.info("Select date and time");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledCalandarIcon")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledCalandarIcon")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Caladar icon");
			ReportUtils.setStepDescription("Unable to identify Caladar icon",
					true);
			m_assert.fail("Unable to identify Caladar icon");
		}
		SeleniumUtils.clickOnElement(element);
		// Identify Today's date
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledTodayDate")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledTodayDate")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Select hrs from Hrs drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledHrsDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledHrsDropdown")
						.getLocatorvalue(), testcaseArgs.get("timeHrs"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("timeHrs")
					+ "] from dropdwon");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("timeHrs")
							+ "] from dropdwon", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("timeHrs")
					+ "] from dropdwon");
		}
		// Select mins from Mins dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledMinsDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabScheduledMinsDropdown")
						.getLocatorvalue(), testcaseArgs.get("timeMins"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("timeMins")
					+ "] from dropdwon");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("timeMins")
							+ "] from dropdwon", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("timeMins")
					+ "] from dropdwon");
		}
		// Identify AM /PM radio buttons
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabScheduledTotalDateContainer")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AlertsNotificationsComposeTabScheduledTotalDateContainer")
								.getLocatorvalue());
		// Click on AM/PM
		boolean isClicked = SeleniumCustomUtils.clickAtRadioLocation(element,
				testcaseArgs.get("Meridian"));
		if (!isClicked) {
			logger.error("Unable to click on AM/PM radio buttons");
			ReportUtils.setStepDescription(
					"Unable to click on AM/PM radio buttons", true);
			m_assert.fail("Unable to click on AM/PM radio buttons");
		}
		logger.info("Select segment from Segment dropdown");
		// Select segment from segment dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSelectSegmentsDropdown")
						.getLocatorvalue(), testcaseArgs.get("segment"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("segment")
					+ "] from dropdwon");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("segment")
							+ "] from dropdwon", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("segment")
					+ "] from dropdwon");
		}
		logger.info("Enter message in text area box");
		// Identify Message text area
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabMsgTextArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabMsgTextArea")
						.getLocatorvalue());
		// Enter message text
		SeleniumUtils.type(element, testcaseArgs.get("messageText"));
		SeleniumUtils.sleepThread(2);
		// Validate the entered text displayed in Notifications text box
		logger.info("Verify the entered text is dipslyaed in Notification");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabNotificationText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabNotificationText")
						.getLocatorvalue());
		String NotificationMessageText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(NotificationMessageText,
				testcaseArgs.get("messageText"));
		if (!isTextMatching) {
			logger.error("Entered text in text area is not displayed in notification message");
			ReportUtils
					.setStepDescription(
							"Entered text in text area is not displayed in notification message",
							true);
			m_assert.fail("Entered text in text area is not displayed in notification message");
		}
		// Identify Send now button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsComposeTabSendNowBtn")
						.getLocatorvalue());
		logger.info("Click on Send Now button");
		// Click on Send now button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Verify the landing page after creating of new message");
		// Identify Messages tab header element
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatorvalue());
		// refresh page
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(3);
		// Identify Messages tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String MessagesTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MessagesTabHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("User is not landed on Messages page");
			ReportUtils.setStepDescription(
					"User is not landed on Messages page", true);
			m_assert.fail("User is not landed on Messages page");
		}
		logger.info("The landing page is Messages page");
		logger.info("Verification of the created scheduled message");
		// Select the application from application dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdwon");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("application")
							+ "] from dropdwon", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("application") + "] from dropdwon");
		}
		// Identify Message table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledMsgTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledMsgTable")
						.getLocatorvalue());
		// Validate the created schedule message is displayed
		isMessagePresent = SeleniumCustomUtils
				.checkScheduledMessageInMessageList(element,
						testcaseArgs.get("messageText"));
		if (!isMessagePresent) {
			logger.error("Scheduled Message is not displayed in Message list");
			ReportUtils.setStepDescription(
					"Scheduled Message is not displayed in Message list", true);
			m_assert.fail("Message is not displayed in Message overview table");
		}
		m_assert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = { "loginAs" })
	public void verifyMessagesLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMessagesLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyMessagesLayout] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [verifyMessagesLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyMessagesLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyMessagesLayout");
		logger.info("Starting [verifyMessagesLayout] execution");
		// Identify Messages tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String MessagesTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MessagesTabHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			// Identify Messages tab
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsMessagesTab")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsMessagesTab")
									.getLocatorvalue());
			// Click on Messages tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			MessagesTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					MessagesTabHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Messages] page header element text matching failed "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ MessagesTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Messages] page header element text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext(), MessagesTabHeaderText, true);
				m_assert.fail("[Messages] page header element text matching failed "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsMessagesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ MessagesTabHeaderText + "]");
			}
		}
		// Identify Application drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify application dropdown", true);
			m_assert.fail("Unable to identify application dropdown");
		}
		// Identify scheudled text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Scheduled text element");
			ReportUtils.setStepDescription(
					"Unable to identify Scheduled text element", true);
			m_assert.fail("Unable to identify Scheduled text element");
		}
		// Get the text
		String ScheduledText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ScheduledText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Scheduled text is not matched");
			logger.error("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabScheduledText")
							.getExptext() + "]  and the return text ["
					+ ScheduledText + "] not equal");
			ReportUtils.setStepDescription(
					"Scheduled text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabScheduledText")
							.getExptext(), ScheduledText, true);
			m_assert.fail("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabScheduledText")
							.getExptext() + "]  and the return text ["
					+ ScheduledText + "] not equal");
		}
		// Identify History text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHistoryText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHistoryText")
						.getLocatorvalue());
		// Get the text
		String HistoryText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				HistoryText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabHistoryText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("History text is not matched");
			logger.error("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHistoryText")
							.getExptext() + "]  and the return text ["
					+ HistoryText + "] not equal");
			ReportUtils.setStepDescription(
					"History text is not matched",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHistoryText")
							.getExptext(), HistoryText, true);
			m_assert.fail("The Expected text  ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabHistoryText")
							.getExptext() + "]  and the return text ["
					+ HistoryText + "] not equal");
		}
		/*
		 * // Identify Messages length dropdown element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "AlertsNotificationsMessagesTabMessagesLengthDropdown")
		 * .getLocatortype(), Suite.objectRepositoryMap.get(
		 * "AlertsNotificationsMessagesTabMessagesLengthDropdown")
		 * .getLocatorvalue()); if (element == null) {
		 * logger.error("Unable to identify Message length dropdown");
		 * ReportUtils.setStepDescription(
		 * "Unable to identify Message length dropdown", true);
		 * m_assert.fail("Unable to identify Message length dropdown"); } //
		 * Identify Search box element = SeleniumUtils.findobject(
		 * Suite.objectRepositoryMap.get(
		 * "AlertsNotificationsMessagesTabSearchTextbox") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "AlertsNotificationsMessagesTabSearchTextbox") .getLocatorvalue());
		 * if (element == null) {
		 * logger.error("Unable to identify Search filter textbox");
		 * ReportUtils.setStepDescription(
		 * "Unable to identify Search filter textbox", true);
		 * m_assert.fail("Unable to identify Search filter textbox"); }
		 */
		// Schedule messages table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledMsgTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabScheduledMsgTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Scheduled messages table");
			ReportUtils.setStepDescription(
					"Unable to identify Scheduled messages table", true);
			m_assert.fail("Unable to identify Scheduled messages table");
		}
		// Identify manual messages table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify messages table");
			ReportUtils.setStepDescription("Unable to identify messages table",
					true);
			m_assert.fail("Unable to identify messages table");
		}
		m_assert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = { "loginAs" })
	public void verifyMessagesLength() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMessagesLength")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyMessagesLength] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [verifyMessagesLength] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyMessagesLength] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyMessagesLength");
		logger.info("Starting [verifyMessagesLength] execution");
		// Identify Messages tab application drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify application dropdown", true);
			m_assert.fail("Unable to identify application dropdown");
		}
		logger.info("Select application from application dropdown");
		// Select application from drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to Select [" + testcaseArgs.get("application")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to Select [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to Select ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		// Identify Messages table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Messages table");
			ReportUtils.setStepDescription("Unable to identify Messages table",
					true);
			m_assert.fail("Unable to identify Messages table");
		}
		// Identify Length filter drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Message length dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify Message length dropdown", true);
			m_assert.fail("Unable to identify Message length dropdown");
		}
		// Select length filter from drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatorvalue(), testcaseArgs.get("entriesFirst"));
		if (!isSelected) {
			logger.error("Unable to Select ["
					+ testcaseArgs.get("entriesFirst") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to Select [" + testcaseArgs.get("entriesFirst")
							+ "] from dropdown", true);
			m_assert.fail("Unable to Select ["
					+ testcaseArgs.get("entriesFirst") + "] from dropdown");
		}
		logger.info("Verifying if " + testcaseArgs.get("entriesFirst")
				+ "  are displayed in message list");
		// Identify Messages table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Messages table");
			ReportUtils.setStepDescription("Unable to identify Messages table",
					true);
			m_assert.fail("Unable to identify Messages table");
		}
		// Get the count of messages in a table
		int numberOfMessages = SeleniumUtils.getCountFromTable(element);
		if (numberOfMessages != Integer.parseInt(testcaseArgs
				.get("entriesFirst"))) {
			logger.error("Expected number of messages to be displayed is ["
					+ testcaseArgs.get("entriesFirst")
					+ " ] but the actual number of messages displayed is ["
					+ numberOfMessages + "]");
			ReportUtils
					.setStepDescription(
							"Expected number of messages to be displayed is ["
									+ testcaseArgs.get("entriesFirst")
									+ " ] but the actual number of messages displayed is ["
									+ numberOfMessages + "]", true);
			m_assert.fail("Expected number of messages to be displayed is ["
					+ testcaseArgs.get("entriesFirst")
					+ " ] but the actual number of messages displayed is ["
					+ numberOfMessages + "]");
		}
		// Identify Messages length drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Message length dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify Message length dropdown", true);
			m_assert.fail("Unable to identify Message length dropdown");
		}
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatorvalue(), testcaseArgs.get("entriesSecond"));
		if (!isSelected) {
			logger.error("Unable to Select ["
					+ testcaseArgs.get("entriesSecond") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to Select [" + testcaseArgs.get("entriesSecond")
							+ "] from dropdown", true);
			m_assert.fail("Unable to Select ["
					+ testcaseArgs.get("entriesSecond") + "] from dropdown");
		}
		logger.info("Verifying if " + testcaseArgs.get("entriesSecond")
				+ "  are displayed in message list");

		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Messages table");
			ReportUtils.setStepDescription("Unable to identify Messages table",
					true);
			m_assert.fail("Unable to identify Messages table");
		}
		numberOfMessages = SeleniumUtils.getCountFromTable(element);
		if (numberOfMessages != Integer.parseInt(testcaseArgs
				.get("entriesSecond"))) {
			logger.error("Expected number of messages to be displayed is ["
					+ testcaseArgs.get("entriesSecond")
					+ " ] but the actual number of messages displayed is ["
					+ numberOfMessages + "]");
			ReportUtils
					.setStepDescription(
							"Expected number of messages to be displayed is ["
									+ testcaseArgs.get("entriesSecond")
									+ " ] but the actual number of messages displayed is ["
									+ numberOfMessages + "]", true);
			m_assert.fail("Expected number of messages to be displayed is ["
					+ testcaseArgs.get("entriesSecond")
					+ " ] but the actual number of messages displayed is ["
					+ numberOfMessages + "]");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Message length dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify Message length dropdown", true);
			m_assert.fail("Unable to identify Message length dropdown");
		}
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesLengthDropdown")
						.getLocatorvalue(), testcaseArgs.get("defaultvalue"));

		logger.info("Executing verifyMessagesLength is successful");
		m_assert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods = { "loginAs" })
	public void verifyMessagesSearchBox() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		boolean isMessagePresent = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMessagesSearchBox")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyMessagesSearchBox] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyMessagesSearchBox] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyMessagesSearchBox] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyMessagesSearchBox");
		logger.info("Starting [verifyMessagesSearchBox] execution");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify application dropdown", true);
			m_assert.fail("Unable to identify application dropdown");
		}
		logger.info("Select application from application dropdown");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to Select [" + testcaseArgs.get("application")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to Select [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to Select ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		logger.info("Selection of application from dropdown is successful");
		logger.info("Search an application by entering the applicaion in search box");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabSearchTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabSearchTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Search input box");
			ReportUtils.setStepDescription(
					"Unable to identify Search input box", true);
			m_assert.fail("Unable to identify Search input box");
		}
		SeleniumUtils.type(element, testcaseArgs.get("message"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatorvalue());
		for (int i = 1; i <= 20; i++) {
			isMessagePresent = SeleniumCustomUtils
					.checkMessageInMessageOverview(element,
							testcaseArgs.get("message"));
			if (isMessagePresent) {
				break;
			}
			SeleniumUtils.click(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabMessagesNextLink")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsMessagesTabMessagesNextLink")
							.getLocatorvalue());

		}
		if (!isMessagePresent) {
			logger.error("Unable to identify the ["
					+ testcaseArgs.get("message") + "] in message list");
			ReportUtils.setStepDescription("Unable to identify the ["
					+ testcaseArgs.get("message") + "] in message list", true);
			m_assert.fail("Unable to identify the ["
					+ testcaseArgs.get("message") + "] in message list");
		}
		logger.info("Searching an application is successful");
		logger.info("Executing verifyMessagesSearchBox is successful");
		m_assert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = { "loginAs" })
	public void clickOnMessage() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		boolean isClicked = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("clickOnMessage")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [clickOnMessage] is not added for execution.");
			ReportUtils.setStepDescription(
					"Test case [clickOnMessage] is not added for execution",
					false);
			throw new SkipException(
					"Test case [clickOnMessage] is not added for execution");
		}
		// read arguments
		testcaseArgs = getTestData("clickOnMessage");
		logger.info("Starting [clickOnMessage] execution");
		// Identify application dropdown
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown");
			ReportUtils.setStepDescription(
					"Unable to identify application dropdown", true);
			m_assert.fail("Unable to identify application dropdown");
		}
		logger.info("Select application from application dropdown");
		// Select specific application from the drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to Select [" + testcaseArgs.get("application")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to Select [" + testcaseArgs.get("application")
							+ "] from dropdown", true);
			m_assert.fail("Unable to Select ["
					+ testcaseArgs.get("application") + "] from dropdown");
		}
		logger.info("Selection of application from dropdown is successful");
		// Perform Refresh to load Messages table
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(5);
		// Identify Message table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMessagesTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Messages list table in Messages page");
			ReportUtils.setStepDescription(
					"Unable to identify Messages list table in Messages page",
					true);
			m_assert.fail("Unable to identify Messages list table in Messages page");
		}
		// Click on first Message hyper link
		isClicked = SeleniumCustomUtils
				.click_At_Message_Hyperlink_In_Message(element);
		if (!isClicked) {
			logger.error("Unable to identify the ["
					+ testcaseArgs.get("message") + "] in message list");
			ReportUtils.setStepDescription("Unable to identify the ["
					+ testcaseArgs.get("message") + "] in message list", true);
			m_assert.fail("Unable to identify the ["
					+ testcaseArgs.get("message") + "] in message list");
		}
		SeleniumUtils.sleepThread(6);
		// Identify Message details page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMsgDetailText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMsgDetailText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the Message Details header element");
			ReportUtils.setStepDescription(
					"Unable to identify the Message Details header element",
					true);
			m_assert.fail("Unable to identify the Message Details header element");
		}
		logger.info("Click on Message is successful");
		logger.info("Verify the Message components");
		String MessageDetailText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MessageDetailText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMsgDetailText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Unable to identify the Message Details header element");
			ReportUtils.setStepDescription(
					"Unable to identify the Message Details header element",
					true);
			m_assert.fail("Unable to identify the Message Details header element");
		}
		logger.info("Verification of Message components is successful");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMsgDetailMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsMessagesTabMsgDetailMsg")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify message text element in Message Details page");
			ReportUtils
					.setStepDescription(
							"Unable to identify message text element in Message Details page",
							true);
			m_assert.fail("Unable to identify message text element in Message Details page");
		}
		String messageText = SeleniumUtils.getText(element);
		if (!messageText.equalsIgnoreCase(testcaseArgs.get("message"))) {
			logger.error("The expected text is [" + testcaseArgs.get("message")
					+ "] and the return text is [" + messageText + "]");
			ReportUtils.setStepDescription("Text matching failed", "",
					testcaseArgs.get("message"), messageText, true);
			m_assert.fail("The expected text is ["
					+ testcaseArgs.get("message")
					+ "] and the return text is [" + messageText + "]");
		}
		logger.info("Executing clickOnMessage is successful");
		m_assert.assertAll();
	}

	@Test(priority = 15, dependsOnMethods = { "loginAs" })
	public void verifySegmentsLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifySegmentsLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifySegmentsLayout] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [verifySegmentsLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifySegmentsLayout] is not added for execution");
		}
		testcaseArgs = getTestData("verifySegmentsLayout");
		logger.info("Executing verifySegmentsLayout");
		logger.info("Click on Segment button");
		// Identify Segment tab
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AlertsNotificationsSegmentsTabText")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments tab");
			ReportUtils.setStepDescription("Unable to identify Segments tab",
					true);
			m_assert.fail("Unable to identify Segments tab");
		}
		// Click on Segment tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if the Segments page is opened");
		// Identify Segment tab - header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments tab- header text element");
			ReportUtils.setStepDescription(
					"Unable to identify Segments tab- header text element",
					true);
			m_assert.fail("Unable to identify Segments tab- header text element");
		}
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			logger.error("Segment tab - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext()
					+ "] and the return text [" + SegmentHeaderText
					+ "] not equal");
			ReportUtils.setStepDescription(
					"Segment tab - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext(),
					SegmentHeaderText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext()
					+ "] and the return text [" + SegmentHeaderText
					+ "] not equal");
		}
		logger.info("Segments page is opened successfully");
		logger.info("Click operation on Segment button is successful");
		logger.info("Verify the application dropdown");
		// Identify Segment Application dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		logger.info("Verification of application dropdown is successful");
		logger.info("Executing verifySegmentsLayout is successful");
		m_assert.assertAll();
	}

	@Test(priority = 16, dependsOnMethods = { "loginAs" })
	public void verifySegmentsData() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifySegmentsData")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifySegmentsData] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [verifySegmentsData] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifySegmentsData] is not added for execution");
		}
		testcaseArgs = getTestData("verifySegmentsData");
		logger.info("Executing verifySegmentsData");
		logger.info("Verify if the User is on Segments page");
		// Identify Segment tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments tab- header text element");
			ReportUtils.setStepDescription(
					"Unable to identify Segments tab- header text element",
					true);
			m_assert.fail("Unable to identify Segments tab- header text element");
		}
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			// Identify Segment tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Segments tab");
				ReportUtils.setStepDescription(
						"Unable to identify Segments tab", true);
				m_assert.fail("Unable to identify Segments tab");
			}
			// Click on Segment tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		}
		logger.info("User is on Segments page");
		logger.info("Select application from application dropdown");
		// Identify Application dropdown
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		// Select the application
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown", true);
			m_assert.fail("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		SeleniumUtils.sleepThread(3);
		logger.info("Selection of application from application dropdown is successful");
		logger.info("Verify the New Segment button is displayed");
		// Identify New Segment button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment button");
			ReportUtils.setStepDescription(
					"Unable to identify New Segment button", true);
			m_assert.fail("Unable to identify New Segment button");
		}
		logger.info("The New Segment button is displayed successfully");
		logger.info("Verify the Segments list displayed");
		// Identify Segment table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments list");
			ReportUtils.setStepDescription("Unable to identify Segments list",
					true);
			m_assert.fail("Unable to identify Segments list");
		}
		logger.info("The segments list displayed successfully");
		logger.info("Executing verifySegmentsData is successful");
		m_assert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods = { "loginAs" })
	public void createNewSegment() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewSegment")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [createNewSegment] is not added for execution.");
			ReportUtils.setStepDescription(
					"Test case [createNewSegment] is not added for execution",
					false);
			throw new SkipException(
					"Test case [createNewSegment] is not added for execution");
		}
		// read arguments
		testcaseArgs = getTestData("createNewSegment");
		logger.info("Starting [createNewSegment] execution");
		logger.info("Verify if the User is on [Segments] page");
		// Identify Segments page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			// Identify Segment tab
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatorvalue());
			// Click on Segments tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Segments page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatorvalue());
			// get the text
			SegmentHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SegmentHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext());
			if (!isTextMatching) {
				logger.error("[Segments] tab header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Segments] tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext(), SegmentHeaderText, true);
				m_assert.fail("[Segments] tab header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
			}
		}
		logger.info("User is on [Segments] page");
		logger.info("Select application [" + testcaseArgs.get("application")
				+ "] from application dropdown");
		// Identify Application drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		// Select specific application
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown", true);
			m_assert.fail("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		logger.info("Selection of application ["
				+ testcaseArgs.get("application")
				+ "] from application dropdown is successful");
		logger.info("Verify the 'New Segment' button is displayed");
		// Identify New Segment button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Segment' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'New Segment' button", true);
			m_assert.fail("Unable to identify 'New Segment' button");
		}
		logger.info("The 'New Segment' button is displayed successfully");
		if (configproperties.get(0).equalsIgnoreCase("IE")) {
			SeleniumUtils.click_Using_JavaScript(element);
			SeleniumUtils.sleepThread(4);
		} else {
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		}
		logger.info("Verify Segment textbox, Done and Cancel buttons ");
		// Identify Segment text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Segment' Textbox");
			ReportUtils.setStepDescription(
					"Unable to identify 'New Segment' Textbox", true);
			m_assert.fail("Unable to identify 'New Segment' Textbox");
		}
		// Identify Done button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment Done Button");
			ReportUtils.setStepDescription(
					"Unable to identify New Segment Done Button", true);
			m_assert.fail("Unable to identify New Segment Done Button");
		}
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment Cancel Button");
			ReportUtils.setStepDescription(
					"Unable to identify New Segment Cancel Button", true);
			m_assert.fail("Unable to identify New Segment Cancel Button");
		}
		logger.info("Verification of Segment textbox, Done and Cancel buttons is successful ");
		logger.info("Create New Segment [" + testcaseArgs.get("segment") + "]");
		// Identify Segment text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatorvalue());
		// Enter new Segment
		SeleniumUtils.type(element, testcaseArgs.get("segment"));
		// Click on Done button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(5);
		logger.info("Veiry the success Segment message");
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment Successful creation message");
			ReportUtils
					.setStepDescription(
							"Unable to identify New Segment Successful creation message",
							true);
			m_assert.fail("Unable to identify New Segment Successful creation message");
		}
		String SegementCreationSuccessMsg = SeleniumUtils.getText(element)
				.substring(1).trim();
		isTextMatching = SeleniumUtils.assertEqual(
				SegementCreationSuccessMsg,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getExptext());
		if (!isTextMatching) {
			logger.error("New Segment successful creation message text matching failed."
					+ " The Expected text ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
							.getExptext()
					+ "] and the return text ["
					+ SegementCreationSuccessMsg + "] not equal");
			ReportUtils
					.setStepDescription(
							"New Segment creation success text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
									.getExptext(), SegementCreationSuccessMsg,
							true);
			m_assert.fail("New Segment successful creation message text matching failed. "
					+ "The Expected text ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
							.getExptext()
					+ "] and the return text ["
					+ SegementCreationSuccessMsg + "] not equal");
		}
		logger.info("Verification of success Segment message is successful");
		logger.info("Verify the created Segment displayed in Segments list");
		SeleniumUtils.sleepThread(3);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		boolean isSegmentPresent = SeleniumCustomUtils
				.verifySegmentInSegmentList(element,
						testcaseArgs.get("segment"));
		if (!isSegmentPresent) {
			logger.error("Segment creation fails : "
					+ "Segment is not present in Segment List");
			ReportUtils.setStepDescription("Segment creation fails : "
					+ "Segment is not present in Segment List", true);
			m_assert.fail("Segment creation fails : "
					+ "Segment is not present in Segment List");
		}
		logger.info("Segment creation is successful : "
				+ "Created Segment is displayed in Segments list");
		logger.info("Execution of createNewSegment is successful");
		m_assert.assertAll();
	}

	@Test(priority = 18, dependsOnMethods = { "loginAs" })
	public void editSegment() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editSegment")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [editSegment] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [editSegment] is not added for execution",
							false);
			throw new SkipException(
					"Test case [editSegment] is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("editSegment");
		logger.info("Starting [editSegment] execution");
		logger.info("Verify if the User is on [Segments] page");
		// Identify Segments page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			// Identify segments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatorvalue());
			// Get the text
			String SegmentsTabText = SeleniumUtils.getText(element);
			String ExpSegmentsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsSegmentsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(SegmentsTabText,
					ExpSegmentsTabText);
			if (!isTextMatching) {
				logger.error("[Segments] tab text matching failed: The expected text is ["
						+ ExpSegmentsTabText
						+ "] and the actual return text is ["
						+ SegmentsTabText
						+ "]");
				ReportUtils.setStepDescription(
						"[Segments] tab text matching failed", "",
						ExpSegmentsTabText, SegmentsTabText, true);
				m_assert.fail("[Segments] tab text matching failed: The expected text is ["
						+ ExpSegmentsTabText
						+ "] and the actual return text is ["
						+ SegmentsTabText
						+ "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SegmentHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SegmentHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext());
			if (!isTextMatching) {
				logger.error("[Segments] page header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Segments] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext(), SegmentHeaderText, true);
				m_assert.fail("[Segments] page header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
			}
		}
		logger.info("User is on [Segments] page");
		logger.info("Select application from application dropdown");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		// Select application from application dropdown
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown", true);
			m_assert.fail("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		logger.info("Selection of application from application dropdown is successful");
		logger.info("Edit the Segment [" + testcaseArgs.get("segment") + "]");
		// Identify Segments table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		// Edit the specific Segment
		boolean isEdited = SeleniumCustomUtils.edit_The_Segment_In_Segments(
				element, testcaseArgs.get("segment"),
				testcaseArgs.get("segmentNewName"));
		if (!isEdited) {
			logger.error("Unable to Edit Segment  ["
					+ testcaseArgs.get("segment") + "]");
			ReportUtils.setStepDescription("Unable to Edit Segment  ["
					+ testcaseArgs.get("segment") + "]", true);
			m_assert.fail("Unable to Edit Segment  ["
					+ testcaseArgs.get("segment") + "]");
		}
		SeleniumUtils.sleepThread(3);
		logger.info("Veiry the success Segment message");
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getLocatorvalue());
		// Get the text
		String SegementCreationSuccessMsg = SeleniumUtils.getText(element)
				.substring(1).trim();
		isTextMatching = SeleniumUtils.assertEqual(
				SegementCreationSuccessMsg,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Success Message after editing Segment text matching failed."
					+ "The Expected text ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
							.getExptext()
					+ "] and the return text ["
					+ SegementCreationSuccessMsg + "] not equal");
			ReportUtils
					.setStepDescription(
							"Success Message after editing Segment text matching failed. ",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
									.getExptext(), SegementCreationSuccessMsg,
							true);
			m_assert.fail("Success Message after editing Segment text matching failed."
					+ " The Expected text ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
							.getExptext()
					+ "] and the return text ["
					+ SegementCreationSuccessMsg + "] not equal");
		}
		logger.info("Verification of success Segment message is successful");
		logger.info("Verify the edited Segment displayed in Segment list");
		// Identify Segments table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		boolean isSegmentPresent = SeleniumCustomUtils
				.verifySegmentInSegmentList(element,
						testcaseArgs.get("segmentNewName"));
		if (!isSegmentPresent) {
			logger.error("Segment edition fails : "
					+ "Segment is not present in Segment List");
			ReportUtils.setStepDescription("Segment edition fails : "
					+ "Segment is not present in Segment List", true);
			m_assert.fail("Segment edition fails : "
					+ "Segment is not present in Segment List");
		}
		logger.info("Edition of Segment is successful");
		logger.info("Execution of editSegment is successful");
		m_assert.assertAll();
	}

	@Test(priority = 19, dependsOnMethods = { "loginAs" })
	public void deleteSegment() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteSegment")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [deleteSegment] is not added for execution.");
			ReportUtils.setStepDescription(
					"Test case [deleteSegment] is not added for execution",
					false);
			throw new SkipException(
					"Test case [deleteSegment] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("deleteSegment");
		logger.info("Starting [deleteSegment] execution");
		logger.info("Verify if the User is on Segments page");
		// Identify Segments page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			// Identify segments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatorvalue());
			// Get the text
			String SegmentsTabText = SeleniumUtils.getText(element);
			String ExpSegmentsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsSegmentsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(SegmentsTabText,
					ExpSegmentsTabText);
			if (!isTextMatching) {
				logger.error("[Segments] tab text matching failed: "
						+ "The expected text is [" + ExpSegmentsTabText
						+ "] and the actual return text is [" + SegmentsTabText
						+ "]");
				ReportUtils.setStepDescription(
						"[Segments] tab text matching failed", "",
						ExpSegmentsTabText, SegmentsTabText, true);
				m_assert.fail("[Segments] tab text matching failed: "
						+ "The expected text is [" + ExpSegmentsTabText
						+ "] and the actual return text is [" + SegmentsTabText
						+ "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SegmentHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SegmentHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext());
			if (!isTextMatching) {
				logger.error("[Segments] page header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Segments] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext(), SegmentHeaderText, true);
				m_assert.fail("[Segments] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
			}
		}
		logger.info("User is on Segments page");
		// Identify Application drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		logger.info("Select application from application dropdown");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown", true);
			m_assert.fail("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		logger.info("Selection of application from application dropdown is successful");
		SeleniumUtils.sleepThread(5);
		// Identify Segments table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete link of specific user
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, testcaseArgs.get("segment"));
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment ["
						+ testcaseArgs.get("segment") + "]");
			}
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, testcaseArgs.get("segment"));
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment ["
						+ testcaseArgs.get("segment") + "]");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		} else {
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, testcaseArgs.get("segment"));
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment ["
						+ testcaseArgs.get("segment") + "]");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		}
		m_assert.assertAll();
	}

	@Test(priority = 20, dependsOnMethods = { "loginAs" })
	public void clone_The_Segment() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("clone_The_Segment")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [clone_The_Segment] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [clone_The_Segment] is not added for execution",
					false);
			throw new SkipException(
					"Test case [clone_The_Segment] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("clone_The_Segment");
		logger.info("Starting [clone_The_Segment] execution");
		// Identify Segments page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			// Identify segments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatorvalue());
			// Get the text
			String SegmentsTabText = SeleniumUtils.getText(element);
			String ExpSegmentsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsSegmentsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(SegmentsTabText,
					ExpSegmentsTabText);
			if (!isTextMatching) {
				logger.error("[Segments] tab text matching failed: The expected text is ["
						+ ExpSegmentsTabText
						+ "] and the actual return text is ["
						+ SegmentsTabText
						+ "]");
				ReportUtils.setStepDescription(
						"[Segments] tab text matching failed", "",
						ExpSegmentsTabText, SegmentsTabText, true);
				m_assert.fail("[Segments] tab text matching failed: The expected text is ["
						+ ExpSegmentsTabText
						+ "] and the actual return text is ["
						+ SegmentsTabText
						+ "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SegmentHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SegmentHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext());
			if (!isTextMatching) {
				logger.error("[Segments] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Segments] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext(), SegmentHeaderText, true);
				m_assert.fail("[Segments] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
			}
		}
		logger.info("User is on Segments page");
		// Identify Application drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		logger.info("Select application from application dropdown");
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown", true);
			m_assert.fail("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		logger.info("Selection of application from application dropdown is successful");
		SeleniumUtils.sleepThread(4);
		// Identify New Segment button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Segment' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'New Segment' button", true);
			m_assert.fail("Unable to identify 'New Segment' button");
		}
		logger.info("The 'New Segment' button is displayed successfully");
		if (configproperties.get(0).equalsIgnoreCase("IE")) {
			SeleniumUtils.click_Using_JavaScript(element);
			SeleniumUtils.sleepThread(4);
		} else {
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		}
		logger.info("Verify Segment textbox, Done and Cancel buttons ");
		// Identify Segment text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Segment' Textbox");
			ReportUtils.setStepDescription(
					"Unable to identify 'New Segment' Textbox", true);
			m_assert.fail("Unable to identify 'New Segment' Textbox");
		}
		// Identify Done button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment Done Button");
			ReportUtils.setStepDescription(
					"Unable to identify New Segment Done Button", true);
			m_assert.fail("Unable to identify New Segment Done Button");
		}
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment Cancel Button");
			ReportUtils.setStepDescription(
					"Unable to identify New Segment Cancel Button", true);
			m_assert.fail("Unable to identify New Segment Cancel Button");
		}
		logger.info("Verification of Segment textbox, Done and Cancel buttons is successful ");
		logger.info("Create New Segment [" + testcaseArgs.get("segment") + "]");
		// Identify Segment text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentTextbox")
						.getLocatorvalue());
		// Enter new Segment
		SeleniumUtils.type(element, testcaseArgs.get("segment"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabNewSegmentDoneBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segment Done button");
			ReportUtils.setStepDescription(
					"Unable to identify Segment Done button", true);
			m_assert.fail("Unable to identify Segment Done button");
		}
		// Click on Done button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Veiry the success Segment message");
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Segment Successful creation message");
			ReportUtils
					.setStepDescription(
							"Unable to identify New Segment Successful creation message",
							true);
			m_assert.fail("Unable to identify New Segment Successful creation message");
		}
		String SegementCreationSuccessMsg = SeleniumUtils.getText(element)
				.substring(1).trim();
		isTextMatching = SeleniumUtils.assertEqual(
				SegementCreationSuccessMsg,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
						.getExptext());
		if (!isTextMatching) {
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
							.getExptext() + "] and the return text ["
					+ SegementCreationSuccessMsg + "] not equal");
			ReportUtils
					.setStepDescription(
							"New Segment successful creation message text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
									.getExptext(), SegementCreationSuccessMsg,
							true);
			m_assert.fail("New Segment successful creation message text matching failed. The Expected text ["
					+ Suite.objectRepositoryMap
							.get("AlertsNotificationsSegmentsTabSegmentCreatedSuccess")
							.getExptext()
					+ "] and the return text ["
					+ SegementCreationSuccessMsg + "] not equal");
		}
		logger.info("Verification of success Segment message is successful");
		logger.info("Verify the created Segment displayed in Segments list");
		SeleniumUtils.sleepThread(4);
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(4);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		boolean isSegmentPresent = SeleniumCustomUtils
				.verifySegmentInSegmentList(element,
						testcaseArgs.get("segment"));
		if (!isSegmentPresent) {
			logger.error("Segment creation fails : Segment is not present in Segment List");
			ReportUtils
					.setStepDescription(
							"Segment creation fails : Segment is not present in Segment List",
							true);
			m_assert.fail("Segment creation fails : Segment is not present in Segment List");
		}
		// Create Clone for the just Created Segment
		logger.info("Cloning of created Segment");
		boolean is_Segment_Cloned = SeleniumCustomUtils
				.clone_The_Segment_In_Segments(element,
						testcaseArgs.get("segment"));
		if (!is_Segment_Cloned) {
			logger.error("Error while Cloning of Segment  ["
					+ testcaseArgs.get("segment") + "]");
			ReportUtils.setStepDescription("Error while Cloning of Segment  ["
					+ testcaseArgs.get("segment") + "]", true);
			m_assert.fail("Error while Cloning of Segment  ["
					+ testcaseArgs.get("segment") + "]");
		}
		logger.info("Successfully Cloned the new Segment");
		logger.info("Identify whether Alert window is being displayed?");
		// Identify if Alert window is displayed
		boolean is_Alert_Window_Displayed = SeleniumUtils
				.isAlertWindowDialogPresent();
		if (!is_Alert_Window_Displayed) {
			logger.error("Alert Window is not displayed after doing click on "
					+ "Clone button");
			ReportUtils.setStepDescription(
					"Alert Window is not displayed after "
							+ "doing click on Clone button", true);
			m_assert.fail("Alert Window is not displayed after doing click on "
					+ "Clone button");
		}
		logger.info("Alert Dialog Winodw is displayed");
		logger.info("Click on OK button on Alert Window to Clone the Segment");
		SeleniumUtils.acceptAlertWindow();
		SeleniumUtils.sleepThread(5);
		logger.info("Identify if the Cloned Segment is displayed in Segments list?");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		isSegmentPresent = SeleniumCustomUtils.verifySegmentInSegmentList(
				element, "Cloned - [" + testcaseArgs.get("segment") + "]");
		if (!isSegmentPresent) {
			logger.error("Cloned Segment " + testcaseArgs.get("segment") + " "
					+ "is not displayed in Segments list");
			ReportUtils.setStepDescription(
					"Cloned Segment " + testcaseArgs.get("segment") + " "
							+ "is not displayed in Segments list", true);
			m_assert.fail("Cloned Segment " + testcaseArgs.get("segment") + " "
					+ "is not displayed in Segments list");
		}
		logger.info("Cloned Segment is displyaed in Segment list");
		logger.info("Deleting the Segment and Cloned Segment");
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete link of specific user
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, testcaseArgs.get("segment"));
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment ["
						+ testcaseArgs.get("segment") + "]");
			}
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			isClicked = SeleniumCustomUtils
					.delete_The_Segment_In_Segments_for_IE(element,
							testcaseArgs.get("segment"));
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment ["
						+ testcaseArgs.get("segment") + "]");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		} else {
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, testcaseArgs.get("segment"));
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment ["
						+ testcaseArgs.get("segment") + "]");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		}
		m_assert.assertAll();
	}

	@Test(priority = 21, dependsOnMethods = { "loginAs" })
	public void delete_Cloned_Segment() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("delete_Cloned_Segment")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [delete_Cloned_Segment] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [delete_Cloned_Segment] is not added for execution",
							false);
			throw new SkipException(
					"Test case [delete_Cloned_Segment] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("delete_Cloned_Segment");
		logger.info("Starting [delete_Cloned_Segment] execution");
		logger.info("Verify if the User is on Segments page");
		// Identify Segments page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SegmentHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SegmentHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabText").getExptext());
		if (!isTextMatching) {
			// Identify segments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText")
							.getLocatorvalue());
			// Get the text
			String SegmentsTabText = SeleniumUtils.getText(element);
			String ExpSegmentsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsSegmentsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(SegmentsTabText,
					ExpSegmentsTabText);
			if (!isTextMatching) {
				logger.error("[Segments] tab text matching failed: "
						+ "The expected text is [" + ExpSegmentsTabText
						+ "] and the actual return text is [" + SegmentsTabText
						+ "]");
				ReportUtils.setStepDescription(
						"[Segments] tab text matching failed", "",
						ExpSegmentsTabText, SegmentsTabText, true);
				m_assert.fail("[Segments] tab text matching failed: "
						+ "The expected text is [" + ExpSegmentsTabText
						+ "] and the actual return text is [" + SegmentsTabText
						+ "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SegmentHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SegmentHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsSegmentsTabText").getExptext());
			if (!isTextMatching) {
				logger.error("[Segments] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Segments] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext(), SegmentHeaderText, true);
				m_assert.fail("[Segments] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsSegmentsTabText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SegmentHeaderText + "]");
			}
		}
		logger.info("User is on Segments page");
		// Identify Application drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application dropdown in Segments page");
			ReportUtils.setStepDescription(
					"Unable to identify Application dropdown in Segments page",
					true);
			m_assert.fail("Unable to identify Application dropdown in Segments page");
		}
		logger.info("Select application from application dropdown");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabApplicationDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
			ReportUtils.setStepDescription("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown", true);
			m_assert.fail("Unable to select the application ["
					+ testcaseArgs.get("application")
					+ "] from application dropdown");
		}
		logger.info("Selection of application from application dropdown is successful");
		SeleniumUtils.sleepThread(5);
		// Identify Segments table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSegmentsTabSegmentsTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments List");
			ReportUtils.setStepDescription("Unable to identify Segments List",
					true);
			m_assert.fail("Unable to identify Segments List");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete link of specific user
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, "Cloned - [" + testcaseArgs.get("segment") + "]");
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  Cloned - ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  Cloned - ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment  Cloned - ["
						+ testcaseArgs.get("segment") + "]");
			}
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			isClicked = SeleniumCustomUtils
					.delete_The_Segment_In_Segments_for_IE(element,
							"Cloned - [" + testcaseArgs.get("segment") + "]");
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  Cloned - ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  Cloned - ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment  Cloned - ["
						+ testcaseArgs.get("segment") + "]");
			}
		} else {
			isClicked = SeleniumCustomUtils.delete_The_Segment_In_Segments(
					element, "Cloned - [" + testcaseArgs.get("segment") + "]");
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Segment  Cloned - ["
						+ testcaseArgs.get("segment") + "]");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Segment  Cloned - ["
								+ testcaseArgs.get("segment") + "]", true);
				m_assert.fail("Unable to click on Delete button of Segment  Cloned - ["
						+ testcaseArgs.get("segment") + "]");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		}
		m_assert.assertAll();
	}

	@Test(priority = 22, dependsOnMethods = { "loginAs" })
	public void verifyFeedsLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyFeedsLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyFeedsLayout] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyFeedsLayout] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifyFeedsLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyFeedsLayout");
		logger.info("Starting [verifyFeedsLayout] execution");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(5);
		// Identify Feeds tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("AlertsNotificationsFeedsTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AlertsNotificationsFeedsTabText")
						.getLocatorvalue());
		// Get the text
		String FeedsTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFeedsTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsFeedsTabText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(FeedsTabText,
				ExpFeedsTabText);
		if (!isTextMatching) {
			logger.error("[Feeds] tab text matching failed: The expected text is ["
					+ ExpFeedsTabText
					+ "] and the actual return text is ["
					+ FeedsTabText + "]");
			ReportUtils.setStepDescription("[Feeds] tab text matching failed",
					"", ExpFeedsTabText, FeedsTabText, true);
			m_assert.fail("[Feeds] tab text matching failed: The expected text is ["
					+ ExpFeedsTabText
					+ "] and the actual return text is ["
					+ FeedsTabText + "]");
		}
		// Click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// Identify Feeds tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue());
		// Get the text
		String FeedsPageHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
				"AlertsFeedsPageHeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
				ExpFeedsPageHeaderText);
		if (!isTextMatching) {
			logger.error("[Feeds] page header text matching failed: The expected text is ["
					+ ExpFeedsPageHeaderText
					+ "] and the actual return text is ["
					+ FeedsPageHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"[Feeds] page header text matching failed", "",
					ExpFeedsPageHeaderText, FeedsPageHeaderText, true);
			m_assert.fail("[Feeds] page header text matching failed: The expected text is ["
					+ ExpFeedsPageHeaderText
					+ "] and the actual return text is ["
					+ FeedsPageHeaderText
					+ "]");
		}
		// Identify New Feed button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsFeedsPageNewFeedBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageNewFeedBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Feeds button in [Feeds] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify New Feeds button in [Feeds] page",
							true);
			m_assert.fail("Unable to identify New Feeds button in [Feeds] page");
		}
		m_assert.assertAll();
	}

	@Test(priority = 23, dependsOnMethods = { "loginAs" })
	public void create_New_Feeds() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("create_New_Feeds")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [create_New_Feeds] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [create_New_Feeds] is not added for execution",
					false);
			throw new SkipException(
					"Test case [create_New_Feeds] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("create_New_Feeds");
		logger.info("Starting [create_New_Feeds] execution");
		logger.info("Verify if Feeds page is displayed?");
		// Identify Feeds tab header element
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getExptext());
		// Identify Feeds tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue());
		// Get the text
		String FeedsPageHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
				"AlertsFeedsPageHeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
				ExpFeedsPageHeaderText);
		if (!isTextMatching) {
			logger.info("User is not in Feeds page");
			// Identify Feeds tab
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsFeedsTabText")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsFeedsTabText")
									.getLocatorvalue());
			// Get the text
			String FeedsTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpFeedsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsFeedsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(FeedsTabText,
					ExpFeedsTabText);
			if (!isTextMatching) {
				logger.error("[Feeds] tab text matching failed: The expected text is ["
						+ ExpFeedsTabText
						+ "] and the actual return text is ["
						+ FeedsTabText + "]");
				ReportUtils.setStepDescription(
						"[Feeds] tab text matching failed", "",
						ExpFeedsTabText, FeedsTabText, true);
				m_assert.fail("[Feeds] tab text matching failed: The expected text is ["
						+ ExpFeedsTabText
						+ "] and the actual return text is ["
						+ FeedsTabText + "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Feeds tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsFeedsPageHeaderElement").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsFeedsPageHeaderElement").getLocatorvalue());
			// Get the text
			FeedsPageHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
					"AlertsFeedsPageHeaderElement").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
					ExpFeedsPageHeaderText);
			if (!isTextMatching) {
				logger.error("[Feeds] page header text matching failed: The expected text is ["
						+ ExpFeedsPageHeaderText
						+ "] and the actual return text is ["
						+ FeedsPageHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Feeds] page header text matching failed", "",
						ExpFeedsPageHeaderText, FeedsPageHeaderText, true);
				m_assert.fail("[Feeds] page header text matching failed: The expected text is ["
						+ ExpFeedsPageHeaderText
						+ "] and the actual return text is ["
						+ FeedsPageHeaderText + "]");
			}
		}
		logger.info("User is on Feeds tab");
		logger.info("Identify if New Feed button is displayed?");
		// Identify New Feed button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsFeedsPageNewFeedBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageNewFeedBtn")
						.getLocatorvalue());

		if (element == null) {
			logger.error("Unable to identify New Feed button in [Feeds] page");
			ReportUtils.setStepDescription(
					"Unable to identify New Feed button in [Feeds] page", true);
			m_assert.fail("Unable to identify New Feed button in [Feeds] page");
		}
		logger.info("New Feed button is displayed in [Feeds] page");
		logger.info("Create New Feed");
		// Click on New Feed button
		SeleniumUtils.click_Using_JavaScript(element);
		SeleniumUtils.sleepThread(2);
		// Wait for New Feed page is being displayed
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap
						.get("ANS_Feeds_NewFeed_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ANS_Feeds_NewFeed_HeaderElement")
						.getLocatorvalue(),
				Suite.objectRepositoryMap
						.get("ANS_Feeds_NewFeed_HeaderElement").getExptext());
		// Identify New Feeds page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("ANS_Feeds_NewFeed_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ANS_Feeds_NewFeed_HeaderElement")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New Feed page header element");
			ReportUtils.setStepDescription(
					"Unable to identify New Feed page header element", true);
			m_assert.fail("Unable to identify New Feed page header element");
		}
		// Get the text
		String ANS_Feeds_NewFeed_HeaderElement = SeleniumUtils.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_HeaderElement,
				Exp_ANS_Feeds_NewFeed_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed] page header text matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed] page header text matching failed", "",
					Exp_ANS_Feeds_NewFeed_HeaderElement,
					ANS_Feeds_NewFeed_HeaderElement, true);
			m_assert.fail("[New Feed] page header text matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_HeaderElement + "]");
		}
		logger.info("User is landed on New Feed page successfully");
		logger.info("Identify if the same application is being selected in New Feed page?");
		// Identify Select Application header element text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_SelectApp_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_SelectApp_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_SelectApp_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_SelectApp_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_SelectApp_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_SelectApp_HeaderElement,
				Exp_ANS_Feeds_NewFeed_SelectApp_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Select an application] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_SelectApp_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_SelectApp_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Select an application] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_SelectApp_HeaderElement,
					ANS_Feeds_NewFeed_SelectApp_HeaderElement, true);
			m_assert.fail("[New Feed - Select an application] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_SelectApp_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_SelectApp_HeaderElement + "]");
		}
		// Identify Application drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_SelectApp_dropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_SelectApp_dropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify application dropdown in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify application dropdown in New Feed page",
					true);
			m_assert.fail("Unable to identify application dropdown in New Feed page");
		}
		logger.info("Application dropdown is present in New Feed page");
		logger.info("Identify if the same application is being selected in New Feed page?");
		String act_Selected_Application = SeleniumUtils
				.get_Default_Item_In_Dropdown(element);
		// Compare both applications
		if (!act_Selected_Application.equalsIgnoreCase(testcaseArgs
				.get("application"))) {
			logger.error("New Feeds page application dropdown is not being "
					+ "selected the correct application "
					+ "The expected application is ["
					+ testcaseArgs.get("application")
					+ "] and the selected application is "
					+ act_Selected_Application + "]");
			ReportUtils.setStepDescription(
					"New Feeds page application dropdown is not being "
							+ "selected the correct application", "",
					testcaseArgs.get("application"), act_Selected_Application,
					true);
			m_assert.fail("New Feeds page application dropdown is not being "
					+ "selected the correct application "
					+ "The expected application is ["
					+ testcaseArgs.get("application")
					+ "] and the selected application is "
					+ act_Selected_Application + "]");
		}
		// Identify Start Date & End Date Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement,
				Exp_ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Start Date & End Date] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Start Date & End Date] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement,
					ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement, true);
			m_assert.fail("[New Feed - Start Date & End Date] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_StartAndEndDate_HeaderElement + "]");
		}
		// Identify Start Date
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_StartDate")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_StartDate")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Start Date in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Start Date in New Feed page", true);
			m_assert.fail("Unable to identify Start Date in New Feed page");
		}
		// Identify End Date
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_EndDate")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_EndDate")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify End Date in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify End Date in New Feed page", true);
			m_assert.fail("Unable to identify End Date in New Feed page");
		}
		// Identify Daily Time Range Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement,
				Exp_ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Daily Limit Range] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Daily Limit Range] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement,
					ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement, true);
			m_assert.fail("[New Feed - Daily Limit Range] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_DailyTimeRange_HeaderElement + "]");
		}
		// Identify Daily time Range - Start - AM radio
		// button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyTimeRange_Start_AM_radioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyTimeRange_Start_AM_radioBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Daily time Range - Start - AM"
					+ " radio button in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Daily time Range - Start - AM"
							+ " radio button in New Feed page", true);
			m_assert.fail("Unable to identify Daily time Range - Start - AM"
					+ " radio button in New Feed page");
		}
		// Click on Am radio button
		SeleniumUtils.clickOnElement(element);
		// Identify Daily time Range - End - AM radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyTimeRange_End_AM_radioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyTimeRange_End_AM_radioBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Daily time Range - End - AM"
					+ " radio button in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Daily time Range - End - AM"
							+ " radio button in New Feed page", true);
			m_assert.fail("Unable to identify Daily time Range - End - AM"
					+ " radio button in New Feed page");
		}
		// Click on Am radio button
		SeleniumUtils.clickOnElement(element);
		// Identify Interval (in minutes) header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_Interval_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_Interval_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_Interval_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_Interval_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_Interval_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_Interval_HeaderElement,
				Exp_ANS_Feeds_NewFeed_Interval_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Interval (in minutes)] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_Interval_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_Interval_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Interval (in minutes)] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_Interval_HeaderElement,
					ANS_Feeds_NewFeed_Interval_HeaderElement, true);
			m_assert.fail("[New Feed - Interval (in minutes)] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_Interval_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_Interval_HeaderElement + "]");
		}
		// Identify Interval text box
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ANS_Feeds_NewFeed_Interval_textbox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ANS_Feeds_NewFeed_Interval_textbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Interval (in minutes) "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Interval (in minutes) "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Interval (in minutes) "
					+ "textbox in New Feed page");
		}
		// Type Interval in textbox
		SeleniumUtils.type(element, testcaseArgs.get("interval"));
		// Identify Daily Limit header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_DailyLimit_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_DailyLimit_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_DailyLimit_HeaderElement,
				Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Daily Limit] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_DailyLimit_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Daily Limit] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement,
					ANS_Feeds_NewFeed_DailyLimit_HeaderElement, true);
			m_assert.fail("[New Feed - Daily Limit] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_DailyLimit_HeaderElement + "]");
		}
		// Identify Daily Limit textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Daily Limit "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription("Unable to identify Daily Limit "
					+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Daily Limit "
					+ "textbox in New Feed page");
		}
		// Type Interval in textbox
		SeleniumUtils.type(element, testcaseArgs.get("daily"));
		// Identify Feed Url header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedURL_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedURL_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedURL_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed URL] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedURL_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed URL] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement,
					ANS_Feeds_NewFeed_FeedURL_HeaderElement, true);
			m_assert.fail("[New Feed - Feed URL] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedURL_HeaderElement + "]");
		}
		// Identify Feed URL textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_textbox").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_textbox").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed URL "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription("Unable to identify Feed URL "
					+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed URL "
					+ "textbox in New Feed page");
		}
		// Type feedurl in textbox
		SeleniumUtils.type(element, testcaseArgs.get("feedurl"));
		// Identify Feed Item Xpath
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed Item XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed Item XPATH] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement,
					ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement, true);
			m_assert.fail("[New Feed - Feed Item XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemXPATH_HeaderElement + "]");
		}
		// Identify Feed Item XPATH textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemXPATH_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemXPATH_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed Item XPATH "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Feed Item XPATH "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed Item XPATH "
					+ "textbox in New Feed page");
		}
		// Type feed item xpath in textbox
		SeleniumUtils.type(element, testcaseArgs.get("feeditemxpath"));
		// Identify Feed Item Title xPATH
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed Item Title XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed Item Title XPATH] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement,
					ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement, true);
			m_assert.fail("[New Feed - Feed Item Title XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemTitleXPATH_HeaderElement + "]");
		}
		// Identify Feed Item Title XPATH textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemTitleXPATH_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemTitleXPATH_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed Item  Title XPATH "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Feed Item Title XPATH "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed Item Title XPATH "
					+ "textbox in New Feed page");
		}
		// Type feed item Title xpath in textbox
		SeleniumUtils.type(element, testcaseArgs.get("feeditemtitlexpath"));
		// Feed Item Desc XPATH
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed Item Desc XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed Item Desc XPATH] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement,
					ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement, true);
			m_assert.fail("[New Feed - Feed Item Desc XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemDescXPATH_HeaderElement + "]");
		}
		// Identify Feed Item Desc XPATH textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemDescXPATH_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemDescXPATH_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed Item  Desc XPATH "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Feed Item Desc XPATH "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed Desc Title XPATH "
					+ "textbox in New Feed page");
		}
		// Type feed item Desc xpath in textbox
		SeleniumUtils.type(element, testcaseArgs.get("feeditemdescxpath"));
		// Identify Feed Item Link Xpath
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed Item Link XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed Item Link XPATH] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement,
					ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement, true);
			m_assert.fail("[New Feed - Feed Item Link XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemLinkXPATH_HeaderElement + "]");
		}
		// Identify Feed Item Link XPATH textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemLinkXPATH_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemLinkXPATH_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed Item  Link XPATH "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Feed Item Link XPATH "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed Link Title XPATH "
					+ "textbox in New Feed page");
		}
		// Type feed item Link xpath in textbox
		SeleniumUtils.type(element, testcaseArgs.get("feeditemlinkxpath"));
		// Identify Feed Item Timestamp Xpath
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap
								.get("ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement")
								.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement")
				.getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed Item TimeStamp XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement
					+ "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed Item TimeStamp XPATH] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement,
					ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement,
					true);
			m_assert.fail("[New Feed - Feed Item TimeStamp XPATH] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_HeaderElement
					+ "]");
		}
		// Identify Feed Item TimeStamp XPATH textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedItemTimeStampXPATH_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed Item  Timestamp XPATH "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Feed Item Timestamp XPATH "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed Timestamp Title XPATH "
					+ "textbox in New Feed page");
		}
		// Type feed item Timestamp xpath in textbox
		SeleniumUtils.type(element, testcaseArgs.get("feeditemtimestampxpath"));
		// Identify Enter Message TextArea
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_EnterMessage_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_EnterMessage_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Enter Message text area in New Feed page");
			ReportUtils
					.setStepDescription(
							"Unable to identify Enter Message text area in New Feed page",
							true);
			m_assert.fail("Unable to identify Enter Message text area in New Feed page");
		}
		// Type feed item Timestamp xpath in textbox
		SeleniumUtils.type(element, testcaseArgs.get("textarea"));
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_Save_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_Save_Btn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify ANS_Feeds_NewFeed_Save_Btn"
					+ " in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify ANS_Feeds_NewFeed_Save_Btn"
							+ " in New Feed pagee", true);
			m_assert.fail("Unable to identify ANS_Feeds_NewFeed_Save_Btn"
					+ " in New Feed page");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		m_assert.assertAll();
	}

	@Test(priority = 24, dependsOnMethods = { "loginAs" })
	public void edit_Feeds() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("edit_Feeds")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [edit_Feeds] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [edit_Feeds] is not added for execution", false);
			throw new SkipException(
					"Test case [edit_Feeds] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("edit_Feeds");
		logger.info("Starting [edit_Feeds] execution");
		logger.info("Verify if Feeds page is displayed?");
		// Identify Feeds tab header element
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getExptext());
		// Identify Feeds tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue());
		// Get the text
		String FeedsPageHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
				"AlertsFeedsPageHeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
				ExpFeedsPageHeaderText);
		if (!isTextMatching) {
			logger.info("User is not in Feeds page");
			// Identify Feeds tab
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsFeedsTabText")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsFeedsTabText")
									.getLocatorvalue());
			// Get the text
			String FeedsTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpFeedsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsFeedsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(FeedsTabText,
					ExpFeedsTabText);
			if (!isTextMatching) {
				logger.error("[Feeds] tab text matching failed: The expected text is ["
						+ ExpFeedsTabText
						+ "] and the actual return text is ["
						+ FeedsTabText + "]");
				ReportUtils.setStepDescription(
						"[Feeds] tab text matching failed", "",
						ExpFeedsTabText, FeedsTabText, true);
				m_assert.fail("[Feeds] tab text matching failed: The expected text is ["
						+ ExpFeedsTabText
						+ "] and the actual return text is ["
						+ FeedsTabText + "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Feeds tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsFeedsPageHeaderElement").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsFeedsPageHeaderElement").getLocatorvalue());
			// Get the text
			FeedsPageHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
					"AlertsFeedsPageHeaderElement").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
					ExpFeedsPageHeaderText);
			if (!isTextMatching) {
				logger.error("[Feeds] page header text matching failed: The expected text is ["
						+ ExpFeedsPageHeaderText
						+ "] and the actual return text is ["
						+ FeedsPageHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Feeds] page header text matching failed", "",
						ExpFeedsPageHeaderText, FeedsPageHeaderText, true);
				m_assert.fail("[Feeds] page header text matching failed: The expected text is ["
						+ ExpFeedsPageHeaderText
						+ "] and the actual return text is ["
						+ FeedsPageHeaderText + "]");
			}
		}
		logger.info("User is on Feeds tab");
		// Identify Feeds table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_List_Table")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_List_Table")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feeds list in Feeds tab");
			ReportUtils.setStepDescription(
					"Unable to identify Feeds list in Feeds tab", true);
			m_assert.fail("Unable to identify Feeds list in Feeds tab");
		}
		// Click on Edit link of Feeds table
		boolean isClicked = SeleniumCustomUtils
				.click_On_Edit_Link_At_Feeds_Table_In_Feeds_Page(element,
						testcaseArgs.get("message"));
		if (!isClicked) {
			logger.error("Unable to click on Edit link of Feed [ "
					+ testcaseArgs.get("message") + " ] ");
			ReportUtils.setStepDescription(
					"Unable to click on Edit link of Feed [ "
							+ testcaseArgs.get("message") + " ] ", true);
			m_assert.fail("Unable to click on Edit link of Feed [ "
					+ testcaseArgs.get("message") + " ] ");
		}
		// Edit the Feed description
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ANS_Feeds_NewFeed_Interval_textbox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ANS_Feeds_NewFeed_Interval_textbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Interval (in minutes) "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify Interval (in minutes) "
							+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Interval (in minutes) "
					+ "textbox in New Feed page");
		}
		// Type Interval in textbox
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("interval"));
		// Identify Daily Limit header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_DailyLimit_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_DailyLimit_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_DailyLimit_HeaderElement,
				Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Daily Limit] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_DailyLimit_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Daily Limit] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement,
					ANS_Feeds_NewFeed_DailyLimit_HeaderElement, true);
			m_assert.fail("[New Feed - Daily Limit] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_DailyLimit_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_DailyLimit_HeaderElement + "]");
		}
		// Identify Daily Limit textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_DailyLimit_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Daily Limit "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription("Unable to identify Daily Limit "
					+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Daily Limit "
					+ "textbox in New Feed page");
		}
		// Type Interval in textbox
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("daily"));
		// Identify Feed Url header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_HeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_HeaderElement")
						.getLocatorvalue());
		// Get the text
		String ANS_Feeds_NewFeed_FeedURL_HeaderElement = SeleniumUtils
				.getText(element);
		// Get the exp text
		String Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement = Suite.objectRepositoryMap
				.get("ANS_Feeds_NewFeed_FeedURL_HeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(
				ANS_Feeds_NewFeed_FeedURL_HeaderElement,
				Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement);
		if (!isTextMatching) {
			logger.error("[New Feed - Feed URL] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedURL_HeaderElement + "]");
			ReportUtils.setStepDescription(
					"[New Feed - Feed URL] element header text "
							+ "matching failed", "",
					Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement,
					ANS_Feeds_NewFeed_FeedURL_HeaderElement, true);
			m_assert.fail("[New Feed - Feed URL] element header text "
					+ "matching failed: The expected text is ["
					+ Exp_ANS_Feeds_NewFeed_FeedURL_HeaderElement
					+ "] and the actual return text is ["
					+ ANS_Feeds_NewFeed_FeedURL_HeaderElement + "]");
		}
		// Identify Feed URL textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_textbox").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANS_Feeds_NewFeed_FeedURL_textbox").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feed URL "
					+ "textbox in New Feed page");
			ReportUtils.setStepDescription("Unable to identify Feed URL "
					+ "textbox in New Feed page", true);
			m_assert.fail("Unable to identify Feed URL "
					+ "textbox in New Feed page");
		}
		// Type feedurl in textbox
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("feedurl"));
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_Save_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_NewFeed_Save_Btn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify ANS_Feeds_NewFeed_Save_Btn"
					+ " in New Feed page");
			ReportUtils.setStepDescription(
					"Unable to identify ANS_Feeds_NewFeed_Save_Btn"
							+ " in New Feed pagee", true);
			m_assert.fail("Unable to identify ANS_Feeds_NewFeed_Save_Btn"
					+ " in New Feed page");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		m_assert.assertAll();
	}

	@Test(priority = 25, dependsOnMethods = { "loginAs" })
	public void delete_Feeds() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("delete_Feeds")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [delete_Feeds] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [delete_Feeds] is not added for execution",
					false);
			throw new SkipException(
					"Test case [delete_Feeds] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("edit_Feeds");
		logger.info("Starting [edit_Feeds] execution");
		logger.info("Verify if Feeds page is displayed?");
		// Identify Feeds tab header element
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getExptext());
		// Identify Feeds tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsFeedsPageHeaderElement")
						.getLocatorvalue());
		// Get the text
		String FeedsPageHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
				"AlertsFeedsPageHeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
				ExpFeedsPageHeaderText);
		if (!isTextMatching) {
			logger.info("User is not in Feeds page");
			// Identify Feeds tab
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsFeedsTabText")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AlertsNotificationsFeedsTabText")
									.getLocatorvalue());
			// Get the text
			String FeedsTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpFeedsTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsFeedsTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(FeedsTabText,
					ExpFeedsTabText);
			if (!isTextMatching) {
				logger.error("[Feeds] tab text matching failed: The expected text is ["
						+ ExpFeedsTabText
						+ "] and the actual return text is ["
						+ FeedsTabText + "]");
				ReportUtils.setStepDescription(
						"[Feeds] tab text matching failed", "",
						ExpFeedsTabText, FeedsTabText, true);
				m_assert.fail("[Feeds] tab text matching failed: The expected text is ["
						+ ExpFeedsTabText
						+ "] and the actual return text is ["
						+ FeedsTabText + "]");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Feeds tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsFeedsPageHeaderElement").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsFeedsPageHeaderElement").getLocatorvalue());
			// Get the text
			FeedsPageHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpFeedsPageHeaderText = Suite.objectRepositoryMap.get(
					"AlertsFeedsPageHeaderElement").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(FeedsPageHeaderText,
					ExpFeedsPageHeaderText);
			if (!isTextMatching) {
				logger.error("[Feeds] page header text matching failed: The expected text is ["
						+ ExpFeedsPageHeaderText
						+ "] and the actual return text is ["
						+ FeedsPageHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Feeds] page header text matching failed", "",
						ExpFeedsPageHeaderText, FeedsPageHeaderText, true);
				m_assert.fail("[Feeds] page header text matching failed: The expected text is ["
						+ ExpFeedsPageHeaderText
						+ "] and the actual return text is ["
						+ FeedsPageHeaderText + "]");
			}
		}
		logger.info("User is on Feeds tab");
		// Identify Feeds table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_List_Table")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_List_Table")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Feeds list in Feeds tab");
			ReportUtils.setStepDescription(
					"Unable to identify Feeds list in Feeds tab", true);
			m_assert.fail("Unable to identify Feeds list in Feeds tab");
		}
		// Click on Edit link of Feeds table
		boolean isClicked = SeleniumCustomUtils
				.click_On_Edit_Link_At_Feeds_Table_In_Feeds_Page(element,
						testcaseArgs.get("message"));
		if (!isClicked) {
			logger.error("Unable to click on Edit link of Feed [ "
					+ testcaseArgs.get("message") + " ] ");
			ReportUtils.setStepDescription(
					"Unable to click on Edit link of Feed [ "
							+ testcaseArgs.get("message") + " ] ", true);
			m_assert.fail("Unable to click on Edit link of Feed [ "
					+ testcaseArgs.get("message") + " ] ");
		}
		// Identify Feeds Delete button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ANS_Feeds_Delete_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANS_Feeds_Delete_Btn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Delete button Feeds tab");
			ReportUtils.setStepDescription(
					"Unable to identify Delete button Feeds tab", true);
			m_assert.fail("Unable to identify Delete button Feeds tab");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete link of specific user
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Feeds page");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Feeds page", true);
				m_assert.fail("Unable to click on Delete button of Feeds page");
			}
			SeleniumUtils.sleepThread(5);
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			// Run the process to click on OK button on Alert window
			try {
				Process process = new ProcessBuilder(
						GlobalConstants.Handle_Alert_Window_Using_AutoIt,
						GlobalConstants.IE_Alert_Handle_Dialog_Title).start();
				SeleniumUtils.click_Using_JavaScript(element);
				SeleniumUtils.sleepThread(5);
			} catch (IOException e) {
				logger.error("Error while doing click on Delete button in Feeds ");
			}
			SeleniumUtils.click_Using_JavaScript(element);
		} else {
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on Delete button of Feeds page");
				ReportUtils.setStepDescription(
						"Unable to click on Delete button of Feeds page", true);
				m_assert.fail("Unable to click on Delete button of Feeds page");
			}
			// Accept alert window
			SeleniumUtils.acceptAlertWindow();
			SeleniumUtils.sleepThread(5);
		}
		m_assert.assertAll();

	}

	@Test(priority = 26, dependsOnMethods = { "loginAs" })
	public void verifyBackgroundLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyBackgroundLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyBackgroundLayout] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [verifyBackgroundLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyBackgroundLayout] is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("verifyBackgroundLayout");
		logger.info("Starting [verifyBackgroundLayout] execution");
		logger.info("Click on Background tab");
		// Identify Background tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabText")
						.getLocatorvalue());
		// Get the text
		String ConfigureTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpConfigureTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsBackgroundTabText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabText,
				ExpConfigureTabText);
		if (!isTextMatching) {
			logger.error("[Background] tab text matching failed: The expected text is ["
					+ ExpConfigureTabText
					+ "] and the actual return text is ["
					+ ConfigureTabText + "]");
			ReportUtils.setStepDescription(
					"[Background] tab text matching failed", "",
					ExpConfigureTabText, ConfigureTabText, true);
			m_assert.fail("[Background] tab text matching failed: The expected text is ["
					+ ExpConfigureTabText
					+ "] and the actual return text is ["
					+ ConfigureTabText + "]");
		}
		logger.info("Click on Background tab");
		// Click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if the Background page is opened");
		// Idetnfiy Background page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ConfigureHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Background tab - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsBackgroundTabHeaderText")
							.getExptext() + "] and the return text ["
					+ ConfigureHeaderText + "] not equal");
			ReportUtils.setStepDescription(
					"Background tab - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsBackgroundTabHeaderText")
							.getExptext(), ConfigureHeaderText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsBackgroundTabHeaderText")
							.getExptext() + "] and the return text ["
					+ ConfigureHeaderText + "] not equal");
		}
		logger.info("Verify the applications in Configure tab");
		// Identify Application list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application list in Background tab");
			ReportUtils.setStepDescription(
					"Unable to identify Application list in Background tab",
					true);
			m_assert.fail("Unable to identify Application list in Background tab");
		}
		// Identify New Background Update button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabNewbutton")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application list in Background tab");
			ReportUtils.setStepDescription(
					"Unable to identify Application list in Background tab",
					true);
			m_assert.fail("Unable to identify Application list in Background tab");
		}
	}

	@Test(priority = 27, dependsOnMethods = { "loginAs" })
	public void createBackground() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createBackground")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [createBackground] is not added for execution.");
			ReportUtils.setStepDescription(
					"Test case [createBackground] is not added for execution",
					false);
			throw new SkipException(
					"Test case [createBackground] is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("createBackground");
		logger.info("Starting [createBackground] execution");
		logger.info("Identify Background page header element");
		// Identify Background page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ConfigureHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Background tab - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsBackgroundTabHeaderText")
							.getExptext() + "] and the return text ["
					+ ConfigureHeaderText + "] not equal");
			ReportUtils.setStepDescription(
					"Background tab - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsBackgroundTabHeaderText")
							.getExptext(), ConfigureHeaderText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsBackgroundTabHeaderText")
							.getExptext() + "] and the return text ["
					+ ConfigureHeaderText + "] not equal");
		}
		// Identify Application list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application list in Background tab");
			ReportUtils.setStepDescription(
					"Unable to identify Application list in Background tab",
					true);
			m_assert.fail("Unable to identify Application list in Background tab");
		}
		// Identify New Background Update button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabNewbutton")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsBackgroundTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application list in Background tab");
			ReportUtils.setStepDescription(
					"Unable to identify Application list in Background tab",
					true);
			m_assert.fail("Unable to identify Application list in Background tab");
		}
		// click on New Background button
		SeleniumUtils.click_Using_JavaScript(element);
		SeleniumUtils.sleepThread(3);
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageHeaderText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageHeaderText").getLocatorvalue());
		// Identify Selection Application header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectApplicationText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectApplicationText")
						.getLocatorvalue());
		// Get the text
		String selectAppText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				selectAppText,
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectApplicationText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Select an application - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectApplicationText")
							.getExptext() + "] and the return text ["
					+ selectAppText + "] not equal");
			ReportUtils.setStepDescription(
					"Select an application - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectApplicationText")
							.getExptext(), selectAppText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectApplicationText")
							.getExptext() + "] and the return text ["
					+ selectAppText + "] not equal");
		}
		// Identify Application list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ANCreateBackgroundPageAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ANCreateBackgroundPageAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application list in Create Background tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify Application list in Create Background tab",
							true);
			m_assert.fail("Unable to identify Application list in Create Background tab");
		}
		// Select application from app list
		boolean isSelected = SeleniumUtils.selectDropdownByTextFromList(
				element, testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select [ "
					+ testcaseArgs.get("application")
					+ " ] application from list");
			ReportUtils.setStepDescription(
					"Unable to select [ " + testcaseArgs.get("application")
							+ " ] application from list", true);
			m_assert.fail("Unable to select [ "
					+ testcaseArgs.get("application")
					+ " ] application from list");
		}
		// Identify Select Message Type header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectMsgTypeText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectMsgTypeText")
						.getLocatorvalue());
		// Get the text
		String selectMsgText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						selectMsgText,
						Suite.objectRepositoryMap.get(
								"ANCreateBackgroundPageSelectMsgTypeText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("Select Msg Type - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectMsgTypeText")
							.getExptext() + "] and the return text ["
					+ selectMsgText + "] not equal");
			ReportUtils.setStepDescription(
					"Select an application - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectMsgTypeText")
							.getExptext(), selectMsgText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectMsgTypeText")
							.getExptext() + "] and the return text ["
					+ selectMsgText + "] not equal");
		}
		// Identify Select Segments header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectSegmentsText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectSegmentsText")
						.getLocatorvalue());
		// Get the text
		String selectSegmentsText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				selectSegmentsText,
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSelectSegmentsText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Select Segments - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectSegmentsText")
							.getExptext() + "] and the return text ["
					+ selectSegmentsText + "] not equal");
			ReportUtils.setStepDescription(
					"Select an application - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectSegmentsText")
							.getExptext(), selectSegmentsText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageSelectSegmentsText")
							.getExptext() + "] and the return text ["
					+ selectSegmentsText + "] not equal");
		}
		// Identify Application list
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ANCreateBackgroundPageSegmentsList")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ANCreateBackgroundPageSegmentsList")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Segments list in Create Background tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify Segments list in Create Background tab",
							true);
			m_assert.fail("Unable to identify Segments list in Create Background tab");
		}
		// Select application from app list
		isSelected = SeleniumUtils.selectDropdownByTextFromList(element,
				testcaseArgs.get("segment"));
		if (!isSelected) {
			logger.error("Unable to select [ " + testcaseArgs.get("segment")
					+ " ] from list");
			ReportUtils.setStepDescription(
					"Unable to select [ " + testcaseArgs.get("segment")
							+ " ] from list", true);
			m_assert.fail("Unable to select [ " + testcaseArgs.get("segment")
					+ " ] from list");
		}
		// Identify MetaData header text
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ANCreateBackgroundPageMetaDataText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ANCreateBackgroundPageMetaDataText")
								.getLocatorvalue());
		// Get the text
		String MetadataText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MetadataText,
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetaDataText").getExptext());
		if (!isTextMatching) {
			logger.error("Select MetaData - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageMetaDataText").getExptext()
					+ "] and the return text [" + MetadataText + "] not equal");
			ReportUtils.setStepDescription(
					"Select an application - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageMetaDataText").getExptext(),
					MetadataText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"ANCreateBackgroundPageMetaDataText").getExptext()
					+ "] and the return text [" + MetadataText + "] not equal");
		}
		// Identify Metadata link
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetadataKeyValuePair")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetadataKeyValuePair")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Metadata Key/Value pair link in Create Background tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify Metadata Key/Value pair link in Create Background tab",
							true);
			m_assert.fail("Unable to identify Metadata Key/Value pair link in Create Background tab");
		}
		// Click on link
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Wait for the key text box to be displayed
		WebElement element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetadataKeyTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetadataKeyTextbox")
						.getLocatorvalue());
		// Type Key value
		SeleniumUtils.type(element, testcaseArgs.get("key"));
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetadataValueTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageMetadataValueTextbox")
						.getLocatorvalue());
		// Type Key value
		SeleniumUtils.type(element, testcaseArgs.get("value"));
		// Identify Submit button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSendNowBtn").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ANCreateBackgroundPageSendNowBtn").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Send Now button in Create Background tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify Send Now button in Create Background tab",
							true);
			m_assert.fail("Unable to identify Send Now button in Create Background tab");
		}
		// Click on Send now button
		SeleniumUtils.clickOnElement(element);
	}

	@Test(priority = 28, dependsOnMethods = { "loginAs" })
	public void verifyConfigureLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyConfigureLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyConfigureLayout] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [verifyConfigureLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyConfigureLayout] is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("verifyConfigureLayout");
		logger.info("Starting [verifyConfigureLayout] execution");
		logger.info("Click on Configure button");
		// Identify Configure tab
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsConfigureTabText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AlertsNotificationsConfigureTabText")
								.getLocatorvalue());
		// Get the text
		String ConfigureTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpConfigureTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsConfigureTabText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabText,
				ExpConfigureTabText);
		if (!isTextMatching) {
			logger.error("[Configure] tab text matching failed: The expected text is ["
					+ ExpConfigureTabText
					+ "] and the actual return text is ["
					+ ConfigureTabText + "]");
			ReportUtils.setStepDescription(
					"[Configure] tab text matching failed", "",
					ExpConfigureTabText, ConfigureTabText, true);
			m_assert.fail("[Configure] tab text matching failed: The expected text is ["
					+ ExpConfigureTabText
					+ "] and the actual return text is ["
					+ ConfigureTabText + "]");
		}
		logger.info("Click on Configure tab");
		// Click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Verify if the Configure page is opened");
		// Idetnfiy Configure page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ConfigureHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Confgire tab - header text matching failed");
			logger.error("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabHeaderText")
							.getExptext() + "] and the return text ["
					+ ConfigureHeaderText + "] not equal");
			ReportUtils.setStepDescription(
					"Confgire tab - header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabHeaderText")
							.getExptext(), ConfigureHeaderText, true);
			m_assert.fail("The Expected text ["
					+ Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabHeaderText")
							.getExptext() + "] and the return text ["
					+ ConfigureHeaderText + "] not equal");
		}
		logger.info("Verify the applications in Configure tab");
		// Identify Application list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Application list in Configure tab");
			ReportUtils.setStepDescription(
					"Unable to identify Application list in Configure tab",
					true);
			m_assert.fail("Unable to identify Application list in Configure tab");
		}
		m_assert.assertAll();
	}

	@Test(priority = 29, dependsOnMethods = { "loginAs" })
	public void verifyUpdateCertificateForIOS() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isClicked = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyUpdateCertificateForIOS")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyUpdateCertificateForIOS] "
					+ "is not added for execution.");
			ReportUtils.setStepDescription(
					"Test case [verifyUpdateCertificateForIOS] "
							+ "is not added for execution", false);
			throw new SkipException(
					"Test case [verifyUpdateCertificateForIOS] "
							+ "is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("verifyUpdateCertificateForIOS");
		logger.info("Starting [verifyUpdateCertificateForIOS] execution");
		logger.info("Verify if user is on Configure tab");
		// Identify Configure page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ConfigureHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("User is not on Configure page");
			logger.info("Click on Configure tab button");
			// Identify Configure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabText")
							.getLocatorvalue());
			// Get the text
			String ConfigureTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpConfigureTabText = Suite.objectRepositoryMap.get(
					"AlertsNotificationsConfigureTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(ConfigureTabText,
					ExpConfigureTabText);
			if (!isTextMatching) {
				logger.error("[Configure] tab text matching failed: The expected text is ["
						+ ExpConfigureTabText
						+ "] and the actual return text is ["
						+ ConfigureTabText + "]");
				ReportUtils.setStepDescription(
						"[Configure] tab text matching failed", "",
						ExpConfigureTabText, ConfigureTabText, true);
				m_assert.fail("[Configure] tab text matching failed: The expected text is ["
						+ ExpConfigureTabText
						+ "] and the actual return text is ["
						+ ConfigureTabText + "]");
			}
			// Click on Element
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Configure page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			ConfigureHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					ConfigureHeaderText,
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Configure] page header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsConfigureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ConfigureHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Configure] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AlertsNotificationsConfigureTabHeaderText")
								.getExptext(), ConfigureHeaderText, true);
				m_assert.fail("[Configure] page header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AlertsNotificationsConfigureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ConfigureHeaderText + "]");
			}
		}
		logger.info("User is on Configure tab");
		logger.info("Click on UpdateCertificate link of application");
		// Identify Applications list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Confgire tab- application list");
			ReportUtils.setStepDescription(
					"Unable to identify Confgire tab- application list", true);
			m_assert.fail("Unable to identify Confgire tab- application list");
		}
		// Click on Update link of specific application
		isClicked = SeleniumCustomUtils.clickOnUpdateLinkInConfigure(element,
				testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to click on Update Certificate link of application ["
					+ testcaseArgs.get("application") + "]");
			ReportUtils.setStepDescription(
					"Unable to click on Update Certificate link of application ["
							+ testcaseArgs.get("application") + "]", true);
			m_assert.fail("Unable to click on Update Certificate link of application ["
					+ testcaseArgs.get("application") + "]");
		}
		logger.info("Click operation is successful on UpdateCertificate link");
		logger.info("Verify the elements in UpdateCertificate form");
		boolean isVerified = SeleniumCustomUtils.verifyUpdateCertificateForm(
				element, testcaseArgs.get("application"));
		if (!isVerified) {
			logger.error("Verification failed on Update Certificate link of application ["
					+ testcaseArgs.get("application") + "]");
			ReportUtils.setStepDescription(
					"Verification failed on Update Certificate link of application ["
							+ testcaseArgs.get("application") + "]", true);
			m_assert.fail("Verification failed on Update Certificate link of application ["
					+ testcaseArgs.get("application") + "]");
		}
		logger.info("Verification of elements in UpdateCertificate form is successful");
		isClicked = SeleniumCustomUtils.clickOnUpdateLinkInConfigure(element,
				testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to click on Update Certificate link of application ["
					+ testcaseArgs.get("application") + "]");
			ReportUtils.setStepDescription(
					"Unable to click on Update Certificate link of application ["
							+ testcaseArgs.get("application") + "]", true);
			m_assert.fail("Unable to click on Update Certificate link of application ["
					+ testcaseArgs.get("application") + "]");
		}
		logger.info("Execution of verifyUpdateCertificateForIOS is successful");
		m_assert.assertAll();
	}

	@Test(priority = 30, dependsOnMethods = { "loginAs" })
	public void verifyUpdateAPIKeyForAndroid() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isClicked = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyUpdateAPIKeyForAndroid")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyUpdateAPIKeyForAndroid] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyUpdateAPIKeyForAndroid] is not added for execution",
							true);
			throw new SkipException(
					"Test case [verifyUpdateAPIKeyForAndroid] is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("verifyUpdateAPIKeyForAndroid");
		logger.info("Starting [verifyUpdateAPIKeyForAndroid] execution");
		logger.info("Verify if user is on Configure tab");
		// Identify Configure page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ConfigureHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureHeaderText,
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("User is not on Configure page");
			logger.info("Click on Configure tab button");
			// Identify Configure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AlertsNotificationsConfigureTabText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Configure tab");
				ReportUtils.setStepDescription(
						"Unable to identify Configure tab", true);
				m_assert.fail("Unable to identify Configure tab");
			}
			// Click on tab
			SeleniumUtils.clickOnElement(element);
			logger.info("Click operation is successful on Configure tab");
		}
		logger.info("User is on Configure tab");
		logger.info("Click on UpdateCertificate link of application");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsConfigureTabAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Confgire tab- application list");
			ReportUtils.setStepDescription(
					"Unable to identify Confgire tab- application list", true);
			m_assert.fail("Unable to identify Confgire tab- application list");
		}
		isClicked = SeleniumCustomUtils.clickOnUpdateLinkInConfigure(element,
				testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to click on Update API Key link of application ["
					+ testcaseArgs.get("application") + "]");
			ReportUtils.setStepDescription(
					"Unable to click on Update API Key link of application ["
							+ testcaseArgs.get("application") + "]", true);
			m_assert.fail("Unable to click on Update API Key link of application ["
					+ testcaseArgs.get("application") + "]");
		}
		logger.info("Click operation is successful on UpdateAPI Key link");
		logger.info("Verify the elements in UpdateAPI Key form");
		boolean isVerified = SeleniumCustomUtils.verifyUpdateAPIeForm(element,
				testcaseArgs.get("application"));
		if (!isVerified) {
			logger.error("Verification failed on Update API Key link of application ["
					+ testcaseArgs.get("application") + "]");
			ReportUtils.setStepDescription(
					"Verification failed on Update API Key link of application ["
							+ testcaseArgs.get("application") + "]", true);
			m_assert.fail("Verification failed on Update API Key link of application ["
					+ testcaseArgs.get("application") + "]");
		}
		logger.info("Verification of elements in Update API Key form is successful");
		isClicked = SeleniumCustomUtils.clickOnUpdateLinkInConfigure(element,
				testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to click on Update API Key link of application ["
					+ testcaseArgs.get("application") + "]");
			ReportUtils.setStepDescription(
					"Unable to click on Update API Key link of application ["
							+ testcaseArgs.get("application") + "]", true);
			m_assert.fail("Unable to click on Update API Key link of application ["
					+ testcaseArgs.get("application") + "]");
		}
		logger.info("Execution of verifyUpdateAPIKeyForAndroid is successful");
		m_assert.assertAll();
	}

	@Test(priority = 31, dependsOnMethods = { "loginAs" })
	public void verifySupportTab() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifySupportTab")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifySupportTab] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifySupportTab] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifySupportTab] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifySupportTab");
		logger.info("Starting [verifySupportTab] execution");
		// Identify Support Tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSupportTabText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AlertsNotificationsSupportTabText").getLocatorvalue());
		// Get the text
		String SupportTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpSupportTabText = Suite.objectRepositoryMap.get(
				"AlertsNotificationsSupportTabText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(SupportTabText,
				ExpSupportTabText);
		if (!isTextMatching) {
			logger.error("[Support] tab text matching failed: The expected text is ["
					+ ExpSupportTabText
					+ "] and the actual return text is ["
					+ SupportTabText + "]");
			ReportUtils.setStepDescription(
					"[Support] tab text matching failed", "",
					ExpSupportTabText, SupportTabText, true);
			m_assert.fail("[Support] tab text matching failed: The expected text is ["
					+ ExpSupportTabText
					+ "] and the actual return text is ["
					+ SupportTabText + "]");
		}
		// Get the current window handle
		String windowHandle = SeleniumUtils.getWindowHandle();
		if (windowHandle.length() == 0) {
			logger.error("Unable to get the current window handle Id");
			ReportUtils.setStepDescription(
					"Unable to get the current window handle Id", true);
			m_assert.fail("Unable to get the current window handle Id");
		}
		// Click on Support tab
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on Support tab");
			ReportUtils
					.setStepDescription(
							"Unable to click on Support tab in [Alerts & Notifications]",
							true);
			m_assert.fail("Unable to click on Support tab");
		}
		SeleniumUtils.sleepThread(4);
		// Switch to Child window
		SeleniumUtils.switchToChildWindow();
		// Switch to parent window
		SeleniumUtils.switchToWindow(windowHandle);
		m_assert.assertAll();
	}

	@Test(priority = 32, dependsOnMethods = { "loginAs" })
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
		if (alertsnotifications != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			testcases = alertsnotifications.getCase();
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
