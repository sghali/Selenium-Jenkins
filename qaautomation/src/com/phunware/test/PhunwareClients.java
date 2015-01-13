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
import com.phunware.util.SeleniumCustomUtils;
import com.phunware.util.SeleniumUtils;
import com.phunware.util.SoftAssert;

/**
 * @author bhargavas
 * @since 08/15/2013 This class contains all the PhunwareClient methods
 * 
 */

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class PhunwareClients extends Suite {
	private static Logger logger = Logger.getLogger(PhunwareClients.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static boolean isTextMatching;
	private static WebElement element = null;
	private static String childSuite = "phunwareClients";
	private static boolean suiteExecution = false;
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private static boolean isClicked;
	private Testcase clientSuite = null;
	private List<Case> testcases = null;
	private SoftAssert m_assert;

	/**
	 * Read the phunwareclients input xml file and add the name of the test
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
			logger.warn("Test suite [PhunwareClients] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [PhunwareClients] is not added for execution",
					false);
			throw new SkipException(
					"Test suite [PhunwareClients] is not added for execution");
		}
		// Reading PhunwareClient input file
		logger.info("reading [PhunwareClient] Input file");
		// Unmarshal the xml file
		clientSuite = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.PHUNWARECLIENTS_FILE, Testcase.class);
		// check if nodes are present
		if (clientSuite != null) {
			// take test cases into List
			testcases = clientSuite.getCase();
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
			logger.warn("No TestCase added for execution in [PhunwareClients]");
			ReportUtils
					.setStepDescription(
							"No TestCase added for execution in [PhunwareClients] suite",
							false);
			throw new SkipException(
					"No TestCase added for execution in [PhunwareClients]");
		}
		logger.info("reading [PhunwareClients] input file successful");
		logger.info("The testcases for execution in [PhunwareClients]"
				+ testcaseList);
		m_assert.assertAll();
	}

	/**
	 * This method login into the application as per the input
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
			logger.warn("Test case [loginAs] is not added for execution in [PhunwareClients]");
			ReportUtils
					.setStepDescription(
							"Test case [loginAs] is not added for execution in [PhunwareClients]",
							false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution in [PhunwareClients]");
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

	/**
	 * Verify the Clients initial screen after login into the application
	 */
	@Test(priority = 1, dependsOnMethods = "loginAs")
	public void verifyInitialLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyInitialLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyInitialLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyInitialLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyInitialLayout] is not added for execution");
		}
		// staring the execution
		logger.info("Starting [verifyInitialLayout] execution");
		logger.info("Verify all the main tabs are present in the MaaS application");
		logger.info("Identify [Accounts] tab");
		// Identify Client tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("ClientTablogo")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTablogo")
								.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify [Phunwae Clients] tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Phunwae Clients] tab", true);
			m_assert.fail("Unable to identify [Phunwae Clients] tab");
		}
		// Identify Analytics tab
		logger.info("Identify [Analytics] tab ");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AnalyticsTablogoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AnalyticsTablogoText")
						.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify [Analytics] tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Analytics] tab", true);
			m_assert.fail("Unable to identify [Analytics]");
		}
		// Get the text
		String AnalyticsTabText = SeleniumUtils.getText(element);
		// Get the Expected Text
		String ExpAnalyticsTabText = Suite.objectRepositoryMap.get(
				"AnalyticsTablogoText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(AnalyticsTabText,
				ExpAnalyticsTabText);
		if (!isTextMatching) {
			logger.error("[Analytics] tab text matching failed: The expected text is ["
					+ ExpAnalyticsTabText
					+ "] and the actual return text is ["
					+ AnalyticsTabText + "]");
			ReportUtils.setStepDescription(
					"[Analytics] tab text matching failed", "",
					ExpAnalyticsTabText, AnalyticsTabText, true);
			m_assert.assertEquals(AnalyticsTabText, ExpAnalyticsTabText);
		}
		// Alerts & Notification
		logger.info("Identify [Alerts & Notifications] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AlertsTablogoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AlertsTablogoText")
						.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify [Alerts & Notifications] tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Alerts & Notifications] tab", true);
			m_assert.fail("Unable to identify [Alerts & Notifications] tab");
		}
		// Get the text
		String AlertTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAlertTabText = Suite.objectRepositoryMap.get(
				"AlertsTablogoText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(AlertTabText,
				ExpAlertTabText);
		if (!isTextMatching) {
			logger.error("[Alerts & Notifications] tab text matching failed: The expected text is ["
					+ ExpAlertTabText
					+ "] and the actual return text is ["
					+ AlertTabText + "]");
			ReportUtils.setStepDescription(
					"[Alerts & Notifications] tab text matching failed", "",
					ExpAlertTabText, AlertTabText, true);
			m_assert.assertEquals(AlertTabText, ExpAlertTabText);
		}
		// Content Management
		logger.info("Identify [Content Management] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentTablogoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentTablogoText")
						.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify [Content Managaement] tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Content Managaement] tab", true);
			m_assert.fail("Unable to identify [Content Managaement] tab");
		}
		// Get the text
		String ContenetManagementText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpContentManagementText = Suite.objectRepositoryMap.get(
				"ContentTablogoText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(ContenetManagementText,
				ExpContentManagementText);
		if (!isTextMatching) {
			logger.error("[Contenet Management] tab text matching failed: The expected text is ["
					+ ExpContentManagementText
					+ "] and the actual return text is ["
					+ ContenetManagementText + "]");
			ReportUtils.setStepDescription(
					"[Contenet Management] tab text matching failed", "",
					ExpContentManagementText, ContenetManagementText, true);
			m_assert.assertEquals(ContenetManagementText,
					ExpContentManagementText);
		}
		// Advertising
		logger.info("Identify [Advertising] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdvertisingTablogoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdvertisingTablogoText")
						.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify [Advertising] tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Advertising] tab", true);
			m_assert.fail("Unable to identify [Advertising] tab");
		}
		// Get the text
		String AdvertisingTabText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpAdvertisingTabText = Suite.objectRepositoryMap.get(
				"AdvertisingTablogoText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(AdvertisingTabText,
				ExpAdvertisingTabText);
		if (!isTextMatching) {
			logger.error("[Advertising] tab text matching failed: The expected text is ["
					+ ExpAdvertisingTabText
					+ "] and the actual return text is ["
					+ AdvertisingTabText
					+ "]");
			ReportUtils.setStepDescription(
					"[Advertising] tab text matching failed", "",
					ExpAdvertisingTabText, AdvertisingTabText, true);
			m_assert.assertEquals(AdvertisingTabText, ExpAdvertisingTabText);
		}
		// Location
		logger.info("Identify [Location] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationTablogoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationTablogoText")
						.getLocatorvalue());
		if (element == null) {
			logger.info("Unable to identify [Location] tab");
			ReportUtils.setStepDescription("Unable to identify [Location] tab",
					true);
			m_assert.fail("Unable to identify [Location] tab");
		}
		// Get the text
		String LocationTabText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpLocationTabText = Suite.objectRepositoryMap.get(
				"LocationTablogoText").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(LocationTabText,
				ExpLocationTabText);
		if (!isTextMatching) {
			logger.error("[Location] tab text matching failed: The expected text is ["
					+ ExpLocationTabText
					+ "] and the actual return text is ["
					+ LocationTabText + "]");
			ReportUtils.setStepDescription(
					"[Location] tab text matching failed", "",
					ExpLocationTabText, LocationTabText, true);
			m_assert.assertEquals(LocationTabText, ExpLocationTabText);
		}
		m_assert.assertAll();
	}

	/**
	 * This method verifies Application tab Layout
	 * 
	 */
	@Test(priority = 2, dependsOnMethods = "loginAs")
	public void verifyApplicationLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check verifyApplicationLayout is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyApplicationLayout")) {
				forExecution = true;
				break;
			}
		}
		// If not added then skip the test case
		if (!forExecution) {
			logger.info("Test case [verifyApplicationLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyApplicationLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyApplicationLayout] is not added for execution");
		}
		// Starting execution of this method..
		logger.info("Starting [verifyApplicationLayout] execution");
		// Read arguments
		testcaseArgs = getTestData("verifyApplicationLayout");
		// YourApplications page
		logger.info("Verify if user is on [Your Applications] page");
		// Identify the header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		// If header is not present then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Your Applications] page header element");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Your Applications] page header element",
							true);
			m_assert.fail("Unable to identify [Your Applications] page header element");
		}
		// Get the text of the header
		String appheader = SeleniumUtils.getText(element);
		// Get the expected text
		String Expappheader = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// compare the texts
		isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
		if (!isTextMatching) {
			logger.info("User is not on [Your Applications] page");
			logger.info("Click on Accounts tab");
			// Identify ClientTab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTablogo")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTablogo")
							.getLocatorvalue());
			// If it is null then throw the error and exit
			if (element == null) {
				logger.error("Unable to identify [Accounts] tab");
				ReportUtils.setStepDescription(
						"Unable to identify [Accounts] tab", true);
				m_assert.fail("Unable to identify [Accounts] tab");
			}
			// If ClientTab is present then click on the tab
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Accounts] tab");
				ReportUtils.setStepDescription(
						"Unable to click on [Accounts] tab", true);
				m_assert.fail("Unable to click on [Accounts] tab");
			}
			// Identify Applications sub tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatorvalue());
			// If it is not present then throw the error and exit
			if (element == null) {
				logger.error("Unable to identify Application tab in Accounts page");
				ReportUtils.setStepDescription(
						"Unable to identify Application tab in Accounts page",
						true);
				m_assert.fail("Unable to identify Application tab in Accounts page");
			}
			// Get the text of the application tab
			String applicationTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpapplicationTabText = Suite.objectRepositoryMap.get(
					"ClientTabApplications").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(applicationTabText,
					ExpapplicationTabText);
			if (!isTextMatching) {
				logger.error("[Application] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
				ReportUtils.setStepDescription(
						"[Application] tab text matching failed", "",
						ExpapplicationTabText, applicationTabText, true);
				m_assert.fail("[Application] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
			}
			// Click on Applications tab
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on Application tab in Accounts page");
				ReportUtils
						.setStepDescription(
								"Unable to click on Application sub-tab in [Accounts] page",
								true);
				m_assert.fail("Unable to click on Application sub-tab in [Accounts] page");
			}
			// Check the applications tab header
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Application tab header text element");
				ReportUtils
						.setStepDescription(
								"Unable to identify Application sub-tab header text element",
								true);
				m_assert.fail("Unable to identify Application sub-tab header text element");
			}
			// Get the text
			appheader = SeleniumUtils.getText(element);
			// Get the Exp text
			Expappheader = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
			if (!isTextMatching) {
				logger.error("[Your Applications] page header text matching failed :The Expected text ["
						+ Expappheader
						+ "]"
						+ "and"
						+ "The return text ["
						+ appheader + " ]are not equal");
				ReportUtils.setStepDescription(
						"[Your Applications] page header text matching failed",
						"", Expappheader, appheader, true);
				m_assert.fail("[Your Applications] page header text matching failed :The Expected text ["
						+ Expappheader
						+ "]"
						+ "and"
						+ "The return text ["
						+ appheader + " ]are not equal");
			}
		}
		logger.info("Identify Username and Organization fields in [Accounts] tab");
		// Identify Organization name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabOrganizationName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabOrganizationName")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Organization name in [Accounts] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Organization name in [Accounts] tab",
					true);
			m_assert.fail("Unable to identify Organization name in [Accounts] tab");
		}
		// Get the Organization text
		String OrgText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpOrgText = testcaseArgs.get("orgName");
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OrgText, ExpOrgText);
		if (!isTextMatching) {
			logger.error("[Organization name] matching failed :The Expected organization ["
					+ ExpOrgText
					+ "]"
					+ "and"
					+ "The return organization ["
					+ OrgText + " ]are not equal");
			ReportUtils.setStepDescription(
					"[Organization name] matching failed", "", ExpOrgText,
					OrgText, true);
			m_assert.fail("[Organization name] matching failed :The Expected organization ["
					+ ExpOrgText
					+ "]"
					+ "and"
					+ "The return organization ["
					+ OrgText + " ]are not equal");

		}
		// Identify User Name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUserName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUserName")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Username in [Accounts] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Username name in [Accounts] tab", true);
			m_assert.fail("Unable to identify Username in [Accounts] tab");
		}
		// Get the Organization text
		String UserText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpUserText = testcaseArgs.get("userName");
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(OrgText, ExpOrgText);
		if (!isTextMatching) {
			logger.error("[User name] matching failed :The Expected username ["
					+ ExpUserText + "]" + "and" + "The return username ["
					+ UserText + " ]are not equal");
			ReportUtils.setStepDescription("[User name] matching failed", "",
					ExpUserText, UserText, true);
			m_assert.fail("[User name] matching failed :The Expected username ["
					+ ExpUserText
					+ "]"
					+ "and"
					+ "The return username ["
					+ UserText + " ]are not equal");
		}
		// Verify Switch tab and Exit tab
		logger.info("Identify Switch and Exit tabs present in [Accounts] tab");
		// Identify Switch tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabSwitchTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSwitchTab")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Switch tab in [Accounts] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Switch tab in [Accounts] tab", true);
			m_assert.fail("Unable to identify Switch tab in [Accounts] tab");
		}
		// Get the Organization text
		String SwitchTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpSwitchTabText = Suite.objectRepositoryMap.get(
				"ClientTabSwitchTab").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(SwitchTabText,
				ExpSwitchTabText);
		if (!isTextMatching) {
			logger.error("[Switch tab] matching failed :The Expected text ["
					+ ExpSwitchTabText + "]" + "and" + "The return text ["
					+ SwitchTabText + " ]are not equal");
			ReportUtils.setStepDescription("[Switch tab]text matching failed",
					"", ExpSwitchTabText, SwitchTabText, true);
			m_assert.fail("[Switch tab] matching failed :The Expected text ["
					+ ExpSwitchTabText + "]" + "and" + "The return text ["
					+ SwitchTabText + " ]are not equal");
		}
		// Identify Exit tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabExitTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabExitTab")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Exit tab in [Accounts] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Exit tab in [Accounts] tab", true);
			m_assert.fail("Unable to identify Exit tab in [Accounts] tab");
		}
		// Get the Organization text
		String ExitTabText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpExitTabText = Suite.objectRepositoryMap.get(
				"ClientTabExitTab").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(ExitTabText, ExpExitTabText);
		if (!isTextMatching) {
			logger.error("[Exit tab] matching failed :The Expected text ["
					+ ExpExitTabText + "]" + "and" + "The return text ["
					+ ExitTabText + " ]are not equal");
			ReportUtils.setStepDescription("[Exit tab]text matching failed",
					"", ExpExitTabText, ExitTabText, true);
			m_assert.fail("[Exit tab] matching failed :The Expected text ["
					+ ExpExitTabText + "]" + "and" + "The return text ["
					+ ExitTabText + " ]are not equal");
		}
		// Verify all the other sub tabs in the Clients page
		logger.info("Verify all the sub tabs are present in [Accounts] tab");
		logger.info("Verify [Your Account] sub tab is present in [Accounts] tab");
		// Identify YourAccount tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabYourAccount")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabYourAccount")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Your Account] sub tab in [Accounts] main tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Your Account] sub tab in [Accounts] tab",
							true);
			m_assert.fail("Unable to identify [Your Account] sub tab in [Accounts] main tab");
		}
		// Get the text of the YourAccount tab
		String clientTabYourAccountText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpclientTabYourAccountText = Suite.objectRepositoryMap.get(
				"ClientTabYourAccount").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(clientTabYourAccountText,
				ExpclientTabYourAccountText);
		if (!isTextMatching) {
			logger.error("[Your Account] sub tab text matching failed :The Expected text is ["
					+ ExpclientTabYourAccountText
					+ "] and the actual return text is ["
					+ clientTabYourAccountText + "]");
			ReportUtils
					.setStepDescription(
							"[Your Account] sub tab text matching failed", "",
							ExpclientTabYourAccountText,
							clientTabYourAccountText, true);
			m_assert.fail("[Your Account] sub tab text matching failed :The Expected text is ["
					+ ExpclientTabYourAccountText
					+ "] and the actual return text is ["
					+ clientTabYourAccountText + "]");
		}
		// Identify the roles based on User Current Role
		if (testcaseArgs.get("userCurrentRole").equalsIgnoreCase("Admin")) {
			// Verify Users&Roles tab is available
			logger.info("Identify [Users & Roles] sub tab");
			// Identify Users tab elements
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Users & Roles] sub tab in [Accounts] tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Users & Roles] sub tab in [Accounts] tab",
								true);
				m_assert.fail("Unable to identify [Users & Roles] sub tab in [Accounts] tab");
			}
			// Get the Users tab text
			String UsersAndRolesTabText = SeleniumUtils.getText(element);
			// Get the Exp text
			String ExpUsersAndRolesTabText = Suite.objectRepositoryMap.get(
					"ClientTabUsersAndRoles").getExptext();
			// Compare the return text with Object repository expected text
			isTextMatching = SeleniumUtils.assertEqual(UsersAndRolesTabText,
					ExpUsersAndRolesTabText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] sub tab text matching failed :The Expected text is ["
						+ ExpUsersAndRolesTabText
						+ "] and the actual return text is ["
						+ UsersAndRolesTabText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] sub tab text matching failed", "",
						ExpUsersAndRolesTabText, UsersAndRolesTabText, true);
				m_assert.fail("[Users & Roles] sub tab text matching failed :The Expected text is ["
						+ ExpUsersAndRolesTabText
						+ "] and the actual return text is ["
						+ UsersAndRolesTabText + "]");
			}
		} else {
			// Verify User Roles tab is available
			logger.info("Identify [Users] sub tab");
			// Identify Users tab elements
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsers")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsers")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Users] sub tab in [Accounts] main tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Users] sub tab in [Accounts] main tab",
								true);
				m_assert.fail("Unable to identify [Users] sub tab in [Accounts] main tab");
			}
			// Get the Users tab text
			String UsersTabText = SeleniumUtils.getText(element);
			// Get the Exp text
			String ExpUsersTabText = Suite.objectRepositoryMap.get(
					"ClientTabUsers").getExptext();
			// Compare the return text with Object repository expected text
			isTextMatching = SeleniumUtils.assertEqual(UsersTabText,
					ExpUsersTabText);
			if (!isTextMatching) {
				logger.error("[Users] sub tab text matching failed :The Expected text is ["
						+ ExpUsersTabText
						+ "] and the actual return text is ["
						+ UsersTabText + "]");
				ReportUtils.setStepDescription(
						"[Users] sub tab text matching failed", "",
						ExpUsersTabText, UsersTabText, true);
				m_assert.fail("[Users] sub tab text matching failed :The Expected text is ["
						+ ExpUsersTabText
						+ "] and the actual return text is ["
						+ UsersTabText + "]");
			}
		}
		// Verify Sign Out tab is available in Clients page
		logger.info("Verify [Sign Out] sub tab is present in [Accounts] tab");
		// Identify the Sign Out tab element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Sign Out] sub tab in [Accounts] main tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Sign Out] sub tab in [Accounts] main tab",
							true);
			m_assert.fail("Unable to identify [Sign Out] sub tab in [Accounts] main tab");
		}
		// Get the text of the Sign Out tab
		String SignOutTabText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpSignOutTabText = Suite.objectRepositoryMap.get(
				"ClientTabSignOut").getExptext();
		// Compare with expected text
		isTextMatching = SeleniumUtils.assertEqual(SignOutTabText,
				ExpSignOutTabText);
		// If both are not equal then throw the error message and exit
		if (!isTextMatching) {
			logger.error("[Sign Out] sub tab text matching failed :The Expected text is ["
					+ ExpSignOutTabText
					+ "] and the actual return text is ["
					+ SignOutTabText + "]");
			ReportUtils.setStepDescription(
					"[Sign Out] sub tab text matching failed", "",
					ExpSignOutTabText, SignOutTabText, true);
			m_assert.fail("[Sign Out] sub tab text matching failed :The Expected text is ["
					+ ExpSignOutTabText
					+ "] and the actual return text is ["
					+ SignOutTabText + "]");
		}
		// Identify Downloads tab
		logger.info("Identify [Downloads] tab in [Accounts] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabDownloads")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabDownloads")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Downloads] sub tab in [Accounts] main tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Downloads] sub tab in [Accounts] main tab",
							true);
			m_assert.fail("Unable to identify [Downloads] sub tab in [Accounts] main tab");
		}
		// Get the text of the Downloads tab
		String DownloadsTabText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpDownloadsTabText = Suite.objectRepositoryMap.get(
				"ClientTabDownloads").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(DownloadsTabText,
				ExpDownloadsTabText);
		if (!isTextMatching) {
			logger.error("[Downloads] sub tab text matching failed :The Expected text is ["
					+ ExpDownloadsTabText
					+ "] and the actual return text is ["
					+ DownloadsTabText + "]");
			ReportUtils.setStepDescription(
					"[Downloads] sub tab text matching failed", "",
					ExpDownloadsTabText, DownloadsTabText, true);
			m_assert.fail("[Downloads] sub tab text matching failed :The Expected text is ["
					+ ExpDownloadsTabText
					+ "] and the actual return text is ["
					+ DownloadsTabText + "]");
		}
		// Identify Support tab
		logger.info("Identify [Support] sub-tab in [Accounts] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabSupport")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSupport")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Support] sub tab in [Accounts] main tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Support] sub tab in [Accounts] main tab",
							true);
			m_assert.fail("Unable to identify [Support] sub tab in [Accounts] main tab");
		}
		// Get the text of the Downloads tab
		String SupportTabText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpSupportTabText = Suite.objectRepositoryMap.get(
				"ClientTabSupport").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(SupportTabText,
				ExpSupportTabText);
		if (!isTextMatching) {
			logger.error("[Support] sub tab text matching failed :The Expected text is ["
					+ ExpSupportTabText
					+ "] and the actual return text is ["
					+ SupportTabText + "]");
			ReportUtils.setStepDescription(
					"[Support] sub tab text matching failed", "",
					ExpSupportTabText, SupportTabText, true);
			m_assert.fail("[Support] sub tab text matching failed :The Expected text is ["
					+ ExpSupportTabText
					+ "] and the actual return text is ["
					+ SupportTabText + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods = "loginAs")
	public void validationInCreateApplication() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check validationInCreateApplication is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationInCreateApplication")) {
				forExecution = true;
				break;
			}
		}
		// If not added then skip the test case
		if (!forExecution) {
			logger.warn("Test case [validationInCreateApplication] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [validationInCreateApplication] is not added for execution",
							false);
			throw new SkipException(
					"Test case [validationInCreateApplication] is not added for execution");
		}
		// Start execution of this method
		logger.info("Starting [validationInCreateApplication] execution");
		logger.info("Verify if user is on [Your Applications] tab");
		// Identify Client tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Your Applications] tab header element");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Your Applications] tab header element",
							true);
			m_assert.fail("Unable to identify [Your Applications] tab header element");
		}
		// Get the text of the header
		String appheader = SeleniumUtils.getText(element);
		// Get the exp text
		String Expappheader = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
		if (!isTextMatching) {
			logger.info("User is not on [Your Applications] page");
			logger.info("Click on [Applications] sub-tab");
			// Identify Applications tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Application] sub-tab in [Client] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Application] sub-tab in [Client] page",
								true);
				m_assert.fail("Unable to identify [Application] sub-tab in [Client] page");
			}
			// Get the text of Applications tab
			String applicationTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpapplicationTabText = Suite.objectRepositoryMap.get(
					"ClientTabApplications").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(applicationTabText,
					ExpapplicationTabText);
			if (!isTextMatching) {
				logger.error("[Applications] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
				ReportUtils.setStepDescription(
						"[Applications] sub-tab text matching failed", "",
						ExpapplicationTabText, applicationTabText, true);
				m_assert.fail("[Applications] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
			}
			// Click on Applications tab
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Applications] sub-tab in [Clients] tab");
				ReportUtils
						.setStepDescription(
								"Unable to click on [Applications] sub-tab in [Clients] tab",
								true);
				m_assert.fail("Unable to click on [Applications] sub-tab in [Clients] tab");
			}
			// Identify Applications tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Your Applications] tab header element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Your Applications] tab header element",
								true);
				m_assert.fail("Unable to identify [Your Applications] tab header element");
			}
			// Get the text
			appheader = SeleniumUtils.getText(element);
			// Get the Exp text
			Expappheader = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
			if (!isTextMatching) {
				logger.error("[Your Applications] page text matching failed :The Expected text is ["
						+ Expappheader
						+ "] and the actual return text is ["
						+ appheader + "]");
				ReportUtils.setStepDescription(
						"[Your Applications] page text matching failed", "",
						Expappheader, appheader, true);
				m_assert.fail("[Your Applications] page text matching failed :The Expected text is ["
						+ Expappheader
						+ "] and the actual return text is ["
						+ appheader + "]");
			}
		}
		logger.info("Identify 'Add Application' button in [Your Applications] page]");
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddApplicationBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddApplicationBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Add Application' button in [Your Applications] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Add Application' button in [Your Applications] page",
							true);
			m_assert.fail("Unable to identify 'Add Application' button in [Your Applications] page");
		}
		logger.info("Click on Add Application button");
		// click on Add Application button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on 'Add Application' button in [Your Applications] page");
			ReportUtils
					.setStepDescription(
							"Unable to click on 'Add Application' button in [Your Applications] page",
							true);
			m_assert.fail("Unable to click on 'Add Application' button in [Your Applications] page");
		}
		SeleniumUtils.sleepThread(1);
		logger.info("Identify 'Application name' field, 'Platform' and 'Category' fields "
				+ "in [Your Applications] tab");
		// Identify Application Name field
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnAPPName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnAPPName")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unabel to identify 'Application name' field after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unabel to identify 'Application name' field after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unabel to identify 'Application name' field after "
					+ "clicking on 'Add Application' button");
		}
		// Get the text
		String addApp_AppName = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpaddApp_AppName = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnAPPName").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_AppName,
				ExpaddApp_AppName);
		if (!isTextMatching) {
			logger.error("'Application Name' field text matching failed :The Expected text is ["
					+ ExpaddApp_AppName
					+ "] and the actual return text is ["
					+ addApp_AppName + "]");
			ReportUtils.setStepDescription(
					"'Application Name' field text matching failed", "",
					ExpaddApp_AppName, addApp_AppName, true);
			m_assert.fail("'Application Name' field text matching failed :The Expected text is ["
					+ ExpaddApp_AppName
					+ "] and the actual return text is ["
					+ addApp_AppName + "]");
		}
		// Identify Application Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unabel to identify 'Application name text box' field after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unabel to identify 'Application name text box' field after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unabel to identify 'Application name text box' field after "
					+ "clicking on 'Add Application' button");
		}
		// Platform field and platform drop down
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnPlatform")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnPlatform")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unabel to identify 'Platform' field after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unabel to identify 'Platform' field after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unabel to identify 'Platform' field after "
					+ "clicking on 'Add Application' button");
		}
		// Get the text
		String addApp_Platform = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpaddApp_Platform = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnPlatform").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_Platform,
				ExpaddApp_Platform);
		if (!isTextMatching) {
			logger.error("'Platform' field text matching failed :The Expected text is ["
					+ ExpaddApp_Platform
					+ "] and the actual return text is ["
					+ addApp_Platform + "]");
			ReportUtils.setStepDescription(
					"'Platform' field text matching failed", "",
					ExpaddApp_Platform, addApp_Platform, true);

			m_assert.fail("'Platform' field text matching failed :The Expected text is ["
					+ ExpaddApp_Platform
					+ "] and the actual return text is ["
					+ addApp_Platform + "]");
		}
		// Platform dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Platfrom' dropdown after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'Platfrom' dropdown after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unable to identify 'Platfrom' dropdown after "
					+ "clicking on 'Add Application' button");
		}
		// Category field and drop down
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnCategory")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnCategory")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unabel to identify 'Category' field after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'Category' field after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unabel to identify 'Category' field after "
					+ "clicking on 'Add Application' button");
		}
		// Get the text
		String addApp_Category = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpaddApp_Category = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnCategory").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_Category,
				ExpaddApp_Category);
		if (!isTextMatching) {
			logger.error("'Category' field text matching failed :The Expected text is ["
					+ ExpaddApp_Category
					+ "] and the actual return text is ["
					+ addApp_Category + "]");
			ReportUtils.setStepDescription(
					"'Category' field text matching failed", "",
					ExpaddApp_Category, addApp_Category, true);
			m_assert.fail("'Category' field text matching failed :The Expected text is ["
					+ ExpaddApp_Category
					+ "] and the actual return text is ["
					+ addApp_Category + "]");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Category' dropdown after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'Category' dropdown after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unable to identify 'Category' dropdown after "
					+ "clicking on 'Add Application' button");
		}
		logger.info("Identification of 'Save' & 'Cancel' buttons");
		// Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Cancel' button after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'Cancel' button after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unable to identify 'Cancel' button after "
					+ "clicking on 'Add Application' button");
		}
		// Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button after "
					+ "clicking on 'Add Application' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'Save' button after "
							+ "clicking on 'Add Application' button", true);
			m_assert.fail("Unable to identify 'Save' button after "
					+ "clicking on 'Add Application' button");
		}
		// keep Application name field empty and click on Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Application name' textbox in [Your Applications] tab ");
			m_assert.fail("Unable to identify 'Application name' textbox in [Your Applications] tab");
		}
		// Enter blank in Application name
		logger.info("Enter blank in 'Application name' field");
		SeleniumUtils.type(element, "");
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in [Your Applications] tab");
			m_assert.fail("Unable to identify 'Save' button in [Your Applications] tab ");
		}
		// click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on 'Save' button in [Your Applications] page");
			m_assert.fail("Unable to click on 'Save' button in [Your Applications] page");
		}
		// Identify Application Name
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnAPPName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnAPPName")
								.getLocatorvalue());
		// Get the text
		addApp_AppName = SeleniumUtils.getText(element);
		// Get the Exp text
		ExpaddApp_AppName = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnAPPName").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_AppName,
				ExpaddApp_AppName);
		if (!isTextMatching) {
			logger.error("Validation failed on mandatory [Application Name] field");
			ReportUtils
					.setStepDescription(
							"Validation failed on mandatory [Application Name] field:"
									+ "Application accepts empty value in [Application Name] field",
							true);
			m_assert.fail("Validation failed on mandatory [Application Name] field:"
					+ "Application accepts empty value in [Application Name] field");
		}
		// Application name Text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatorvalue());
		// Enter application name in input text box
		logger.info("Enter application name in Application name text box");
		SeleniumUtils.type(element, "Cybage Application Validation");
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnSaveBtn")
								.getLocatorvalue());
		// Click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on 'Save' button in [Your Applications] page");
			m_assert.fail("Unable to click on 'Save' button in [Your Applications] page");
		}
		logger.info("Verify if Your Applications page is displayed after entering text "
				+ "and keeping platform & Category blank");
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnAPPName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnAPPName")
								.getLocatorvalue());
		// Get the text
		addApp_AppName = SeleniumUtils.getText(element);
		// Get the Exp text
		ExpaddApp_AppName = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnAPPName").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_AppName,
				ExpaddApp_AppName);
		if (!isTextMatching) {
			logger.error("Validation failed on mandatory [Platform] field");
			ReportUtils
					.setStepDescription(
							"Validation failed on mandatory [Platform] field:"
									+ "Application accepts empty value in [Platform] field",
							true);
			m_assert.fail("Validation failed on mandatory [Platform] field:"
					+ "Application accepts empty value in [Platform] field");
		}
		// Identify Platform
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatorvalue());
		// Select Platform dropdown based on the input
		SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatorvalue(), "Android");
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnSaveBtn")
								.getLocatorvalue());
		// Click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on 'Save' button in [Your Applications] page");
			m_assert.fail("Unable to click on 'Save' button in [Your Applications] page");
		}
		logger.info("Verify if Your Applications page is displayed after entering text "
				+ "selecting in platform and keeping Category blank");
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnAPPName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnAPPName")
								.getLocatorvalue());
		// Get the text
		addApp_AppName = SeleniumUtils.getText(element);
		// Get the Exp text
		ExpaddApp_AppName = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnAPPName").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_AppName,
				ExpaddApp_AppName);
		if (!isTextMatching) {
			logger.error("Validation failed on mandatory [Category] field");
			ReportUtils
					.setStepDescription(
							"Validation failed on mandatory [Category] field:"
									+ "Application accepts empty value in [Category] field",
							true);
			m_assert.fail("Validation failed on mandatory [Category] field:"
					+ "Application accepts empty value in [Category] field");
		}
		// Identify Category
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatorvalue());
		// Select Platform dropdown based on the input
		SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatorvalue(), "Business");
		// Identify Application name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatorvalue());
		// Clear the text in name field
		SeleniumUtils.clearText(element);
		logger.info("Validating on Application Name field with white space");
		// Enter white space in Application name field
		boolean isTyped = SeleniumUtils.typeKeys(element, Keys.SPACE);
		if (!isTyped) {
			logger.error("Unable to type SPACE key in Application Name text box");
			ReportUtils.setStepDescription(
					"Unable to type SPACE key in Application Name text box",
					true);
			m_assert.fail("Unable to type SPACE key in Application Name text box");
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnSaveBtn")
								.getLocatorvalue());
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on 'Save' button in [Your Applications] page");
			ReportUtils
					.setStepDescription(
							"Unable to click on 'Save' button in [Your Applications] page",
							true);
			m_assert.fail("Unable to click on 'Save' button in [Your Applications] page");
		}
		SeleniumUtils.sleepThread(4);
		// Identify Application name field
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddAppBtnAPPName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddAppBtnAPPName")
								.getLocatorvalue());
		// Get the text
		addApp_AppName = SeleniumUtils.getText(element);
		// Get the Exp text
		ExpaddApp_AppName = Suite.objectRepositoryMap.get(
				"ClientApplicationsAddAppBtnAPPName").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(addApp_AppName,
				ExpaddApp_AppName);
		if (!isTextMatching) {
			logger.error("Validation failed on mandatory [Application Name] field");
			ReportUtils
					.setStepDescription(
							"Vaication failed at [Application Name] field:"
									+ "Application accepts White Spaces in [Application Name] field",
							true);
			m_assert.fail("Validation failed on mandatory [Application Name] field:"
					+ "Application accepts White Spaces in [Application Name] field");
		}
		m_assert.assertAll();
	}

	/**
	 * This method creates New Application under Application tab
	 */
	@Test(priority = 4, dependsOnMethods = "loginAs")
	public void createApplication() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if createApplication is added in execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createApplication")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createApplication] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [createApplication] is not added for execution",
					false);
			throw new SkipException(
					"Test case [createApplication] is not added for execution");
		}
		// Read the data
		testcaseArgs = getTestData("createApplication");
		logger.info("Starting [create Application] execution");
		// verify user is on Application tab of PhunwareClients page..
		SeleniumUtils.sleepThread(1);
		logger.info("Verify if user is on [Your Applications] tab");
		// Identify Client tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Your Applications] tab header element");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Your Applications] tab header element",
							true);
			m_assert.fail("Unable to identify [Your Applications] tab header element");
		}
		// Get the text of the header
		String appheader = SeleniumUtils.getText(element);
		// Get the exp text
		String Expappheader = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
		if (!isTextMatching) {
			logger.info("User is not on [Your Applications] page");
			logger.info("Click on [Applications] sub-tab");
			// Identify Applications tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Application] sub-tab in [Client] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Application] sub-tab in [Client] page",
								true);
				m_assert.fail("Unable to identify [Application] sub-tab in [Client] page");
			}
			// Get the text of Applications tab
			String applicationTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpapplicationTabText = Suite.objectRepositoryMap.get(
					"ClientTabApplications").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(applicationTabText,
					ExpapplicationTabText);
			if (!isTextMatching) {
				logger.error("[Applications] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
				ReportUtils.setStepDescription(
						"[Applications] sub-tab text matching failed", "",
						ExpapplicationTabText, applicationTabText, true);
				m_assert.fail("[Applications] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
			}
			// Click on Applications tab
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Applications] sub-tab in [Clients] tab");
				ReportUtils
						.setStepDescription(
								"Unable to click on [Applications] sub-tab in [Clients] tab",
								true);
				m_assert.fail("Unable to click on [Applications] sub-tab in [Clients] tab");
			}
			// Identify Applications tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Your Applications] tab header element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Your Applications] tab header element",
								true);
				m_assert.fail("Unable to identify [Your Applications] tab header element");
			}
			// Get the text
			appheader = SeleniumUtils.getText(element);
			// Get the Exp text
			Expappheader = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
			if (!isTextMatching) {
				logger.error("[Your Applications] page text matching failed :The Expected text is ["
						+ Expappheader
						+ "] and the actual return text is ["
						+ appheader + "]");
				ReportUtils.setStepDescription(
						"[Your Applications] page text matching failed", "",
						Expappheader, appheader, true);
				m_assert.fail("[Your Applications] page text matching failed :The Expected text is ["
						+ Expappheader
						+ "] and the actual return text is ["
						+ appheader + "]");
			}
		}
		logger.info("Identify 'Add Application' button in [Your Applications] page]");
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsAddApplicationBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsAddApplicationBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Add Application' button in [Your Applications] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Add Application' button in [Your Applications] page",
							true);
			m_assert.fail("Unable to identify 'Add Application' button in [Your Applications] page");
		}
		logger.info("Click on Add Application button");
		// click on Add Application button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on 'Add Application' button in [Your Applications] page");
			ReportUtils
					.setStepDescription(
							"Unable to click on 'Add Application' button in [Your Applications] page",
							true);
			m_assert.fail("Unable to click on 'Add Application' button in [Your Applications] page");
		}
		// Enter application name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnAPPNameTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the application name text box ");
			ReportUtils.setStepDescription(
					"Unable to identify on 'Application Name' text box", true);
			m_assert.fail("Unable to identify on 'Application Name' text box");
		}
		SeleniumUtils.type(element, testcaseArgs.get("appName"));
		// Select Platform dropdown based on the input
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the Platform dropdown ");
			ReportUtils.setStepDescription(
					"Unable to identify Platform dropdown", true);
			m_assert.fail("Unable to identify on Platform dropdown");
		}
		SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnPlatformDropDown")
						.getLocatorvalue(), testcaseArgs.get("platform"));
		SeleniumUtils.sleepThread(3);
		// Select Category dropdown based on the input
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the Category dropdown ");
			ReportUtils.setStepDescription(
					"Unable to identify Category dropdown", true);
			m_assert.fail("Unable to identify on Category dropdown");
		}
		SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsAddAppBtnCategoryDropDown")
						.getLocatorvalue(), testcaseArgs.get("category"));
		// Click on Save button
		// SeleniumUtils
		// .click(Suite.objectRepositoryMap.get(
		// "ClientApplicationsAddAppBtnSaveBtn").getLocatortype(),
		// Suite.objectRepositoryMap.get(
		// "ClientApplicationsAddAppBtnSaveBtn")
		// .getLocatorvalue());
		SeleniumUtils.sleepThread(3);
		// Identify Applications tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabApplications")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabApplications")
						.getLocatorvalue());
		// Click on Applications tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		m_assert.assertAll();
	}

	/**
	 * This method verifies the specific application
	 * 
	 */
	@Test(priority = 5, dependsOnMethods = "loginAs")
	public void verifyApplication() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isAppPresent = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyApplication")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyApplication] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyApplication] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifyApplication] is not added for execution");
		}
		logger.info("Starting [verifyApplication] execution");
		// read param data
		testcaseArgs = getTestData("verifyApplication");
		logger.info("Verify if user is on [Your Applications] tab");
		// Identify Client tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Your Applications] tab header element");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Your Applications] tab header element",
							true);
			m_assert.fail("Unable to identify [Your Applications] tab header element");
		}
		// Get the text of the header
		String appheader = SeleniumUtils.getText(element);
		// Get the exp text
		String Expappheader = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
		if (!isTextMatching) {
			logger.info("User is not on [Your Applications] page");
			logger.info("Click on [Applications] sub-tab");
			// Identify Applications tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabApplications")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Application] sub-tab in [Client] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Application] sub-tab in [Client] page",
								true);
				m_assert.fail("Unable to identify [Application] sub-tab in [Client] page");
			}
			// Get the text of Applications tab
			String applicationTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpapplicationTabText = Suite.objectRepositoryMap.get(
					"ClientTabApplications").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(applicationTabText,
					ExpapplicationTabText);
			if (!isTextMatching) {
				logger.error("[Applications] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
				ReportUtils.setStepDescription(
						"[Applications] sub-tab text matching failed", "",
						ExpapplicationTabText, applicationTabText, true);
				m_assert.fail("[Applications] sub-tab text matching failed : The expected text is["
						+ ExpapplicationTabText
						+ "] and the actual return text is ["
						+ applicationTabText + "]");
			}
			// Click on Applications tab
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Applications] sub-tab in [Clients] tab");
				ReportUtils
						.setStepDescription(
								"Unable to click on [Applications] sub-tab in [Clients] tab",
								true);
				m_assert.fail("Unable to click on [Applications] sub-tab in [Clients] tab");
			}
			// Identify Applications tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Your Applications] tab header element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Your Applications] tab header element",
								true);
				m_assert.fail("Unable to identify [Your Applications] tab header element");
			}
			// Get the text
			appheader = SeleniumUtils.getText(element);
			// Get the Exp text
			Expappheader = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare both texts
			isTextMatching = SeleniumUtils.assertEqual(appheader, Expappheader);
			if (!isTextMatching) {
				logger.error("[Your Applications] page text matching failed :The Expected text is ["
						+ Expappheader
						+ "] and the actual return text is ["
						+ appheader + "]");
				ReportUtils.setStepDescription(
						"[Your Applications] page text matching failed", "",
						Expappheader, appheader, true);
				m_assert.fail("[Your Applications] page text matching failed :The Expected text is ["
						+ Expappheader
						+ "] and the actual return text is ["
						+ appheader + "]");
			}
		}
		logger.info("Verify if the applications list is present in [Your Applications] tab");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientApplicationsAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientApplicationsAppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Applications list in [Your Applications] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify Applications list in [Your Applications] tab",
							true);
			m_assert.fail("Unable to identify Applications list in [Your Applications] tab");
		}
		/*
		 * //Identify Total number of applications available element =
		 * SeleniumUtils.waitForElementToIdentify(
		 * Suite.objectRepositoryMap.get("ClientApplicationsPaginations")
		 * .getLocatortype(),
		 * Suite.objectRepositoryMap.get("ClientApplicationsPaginations")
		 * .getLocatorvalue()); //Get text String
		 * appPaginationText=SeleniumUtils.getText(element); int
		 * totalApplications=Integer.parseInt(appPaginationText.substring(18));
		 */
		logger.info("verifing if the [" + testcaseArgs.get("appName")
				+ "] application is present ");
		for (int i = 1; i < 10; i++) {
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientApplicationsAppList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientApplicationsAppList")
							.getLocatorvalue());
			isAppPresent = SeleniumCustomUtils.checkApplicationName(element,
					testcaseArgs.get("appName"));
			if (isAppPresent) {
				break;
			}
			// Identify Next link
			WebElement NextLinkelement = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationNextLink")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationNextLink")
							.getLocatorvalue());
			if (NextLinkelement != null) {
				SeleniumCustomUtils.clickAtNextLinkOfApplications(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsPaginationNextLink")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsPaginationNextLink")
								.getLocatorvalue());
			}
		}
		if (!isAppPresent) {
			logger.error("[" + testcaseArgs.get("appName")
					+ "] Application is not present in application list");
			ReportUtils.setStepDescription("[" + testcaseArgs.get("appName")
					+ "] Application is not present in application list", true);
			m_assert.fail("[" + testcaseArgs.get("appName")
					+ "] Application is not present in application list");
		}
		// click on show keys
		logger.info("Clicking on ShowKeys of application ["
				+ testcaseArgs.get("appName") + "]");
		// Verify the Show Keys of specific application
		boolean isVerified = SeleniumCustomUtils.verifyShowKeysTableData(
				element, testcaseArgs.get("appName"));
		if (!isVerified) {
			logger.error("Error while verifying Show Keys table of application ["
					+ testcaseArgs.get("appName") + "]");
			ReportUtils.setStepDescription(
					"Error while verifying Show Keys table of application ["
							+ testcaseArgs.get("appName") + "]", true);
			m_assert.fail("Error while verifying Show Keys table of application ["
					+ testcaseArgs.get("appName") + "]");
		}
		SeleniumCustomUtils.clickAtAPPLocation(element,
				testcaseArgs.get("appName"));
		logger.info("Verify if user is on [First] page of application link");
		WebElement activeLink = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsPaginationActiveLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsPaginationActiveLink")
						.getLocatorvalue());
		if (activeLink == null) {
			logger.error("Unable to identify active link in [Your Applications] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify active link in [Your Applications] tab",
							true);
			m_assert.fail("Unable to identify active link in [Your Applications] tab");
		}
		boolean isClicked = SeleniumUtils.clickOnElement(activeLink);
		if (!isClicked) {
			logger.error("Unable to click on active link in [Your Applications] tab");
			ReportUtils
					.setStepDescription(
							"Unable to click on active link in [Your Applications] tab",
							true);
			m_assert.fail("Unable to click on active link in [Your Applications] tab");
		}
		SeleniumUtils.sleepThread(3);
		// Get the URL
		String url = SeleniumUtils.getURL();
		int pageNum = Integer.parseInt(url.substring(45, url.length()).trim());
		logger.info("User is on [" + pageNum + "] page");
		for (int i = 1; i < pageNum; i++) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationPrevLink")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationPrevLink")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify pagnation Prev link in [Your Applications] tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify pagnation Prev link in [Your Applications] tab",
								true);
				m_assert.fail("Unable to identify pagnation Prev link in [Your Applications] tab");
			}
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on Previous link in [Your Applications] tab");
				ReportUtils
						.setStepDescription(
								"Unable to click on Previous link in [Your Applications] tab",
								true);
				m_assert.fail("Unable to click on Previous link in [Your Applications] tab");
			}
		}
		m_assert.assertAll();
	}

	/**
	 * This method edit the specific application
	 */
	@Test(priority = 6, dependsOnMethods = "loginAs")
	public void editApplication() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		boolean isEdited = false;
		boolean isVerified = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editApplication")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test cae [editApplication] is not added for execution");
			ReportUtils.setStepDescription(
					"Test cae [editApplication] is not added for execution",
					false);
			throw new SkipException(
					"Test cae [editApplication] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("editApplication");
		logger.info("Starting [editApplication] execution");
		logger.info("Identifying [" + testcaseArgs.get("appName")
				+ "] application and edit the application");
		for (int i = 1; i < 10; i++) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientApplicationsAppList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientApplicationsAppList")
							.getLocatorvalue());
			// edit the application
			isEdited = SeleniumCustomUtils.editApplication(element,
					testcaseArgs.get("appName"),
					testcaseArgs.get("NewappName"),
					testcaseArgs.get("platform"), testcaseArgs.get("category"));
			if (isEdited) {
				break;
			}
			// Identify Next link
			WebElement NextLinkelement = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get(
									"ClientApplicationsPaginationNextLink")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"ClientApplicationsPaginationNextLink")
									.getLocatorvalue());
			if (NextLinkelement != null) {
				SeleniumCustomUtils.clickAtNextLinkOfApplications(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsPaginationNextLink")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsPaginationNextLink")
								.getLocatorvalue());
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.refreshPage();
			SeleniumUtils.sleepThread(5);
		}
		if (!isEdited) {
			logger.error("Edit operation fail on specified application "
					+ testcaseArgs.get("appName"));
			ReportUtils.setStepDescription(
					"Edit operation fail on specified application "
							+ testcaseArgs.get("appName"), false);
			m_assert.fail("Edit operation fail on specified application "
					+ testcaseArgs.get("appName"));
		}
		// Verify if the application is edited
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientApplicationsAppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientApplicationsAppList")
						.getLocatorvalue());
		SeleniumUtils.refreshPage();
		for (int i = 1; i < 10; i++) {
			SeleniumUtils.sleepThread(3);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientApplicationsAppList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientApplicationsAppList")
							.getLocatorvalue());
			isVerified = SeleniumCustomUtils.isApplicationEdited(element,
					testcaseArgs.get("NewappName"),
					testcaseArgs.get("platform"), testcaseArgs.get("category"));
			if (isVerified) {
				break;
			}
			WebElement NextLinkelement = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get(
									"ClientApplicationsPaginationNextLink")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"ClientApplicationsPaginationNextLink")
									.getLocatorvalue());
			if (NextLinkelement != null) {
				SeleniumCustomUtils.clickAtNextLinkOfApplications(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsPaginationNextLink")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsPaginationNextLink")
								.getLocatorvalue());
				SeleniumUtils.sleepThread(3);
			}
		}
		if (!isVerified) {
			logger.error("Edit verification fails on Application "
					+ testcaseArgs.get("NewappName"));
			ReportUtils.setStepDescription(
					"Edit verification fails on Application "
							+ testcaseArgs.get("NewappName"), false);
			m_assert.fail("Edit verification fails on Application  "
					+ testcaseArgs.get("NewappName"));
		}
		WebElement activeLink = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsPaginationActiveLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsPaginationActiveLink")
						.getLocatorvalue());
		if (activeLink == null) {
			logger.error("Unable to identify active link page");
			ReportUtils.setStepDescription(
					"Unable to identify active link page", false);
			m_assert.fail("Unable to identify active link page");
		}
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"ClientApplicationsPaginationActiveLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientApplicationsPaginationActiveLink")
						.getLocatorvalue());
		String url = SeleniumUtils.getURL();
		int pageNum = Integer.parseInt(url.substring(45, url.length()).trim());
		logger.info("User is on [" + pageNum + "] page");
		for (int i = 1; i < pageNum; i++) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationPrevLink")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationPrevLink")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify pagnation Prev link in [Your Applications] tab");
				m_assert.fail("Unable to identify pagnation Prev link in [Your Applications] tab");
			}
			SeleniumUtils.click(
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationPrevLink")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientApplicationsPaginationPrevLink")
							.getLocatorvalue());
		}
		m_assert.assertAll();
	}

	/**
	 * This method verifies Your Account tab Layout
	 */
	@Test(priority = 7, dependsOnMethods = "loginAs")
	public void verifyYourAccountLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check runmode for this method
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyYourAccountLayout")) {
				forExecution = true;
				break;
			}
		}
		// If runmode is no then skip this method
		if (!forExecution) {
			logger.warn("Test case [verifyYourAccountLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyYourAccountLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyYourAccountLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyYourAccountLayout");
		// Start execution of this method
		logger.info("Starting [verifyYourAccountLayout] execution");
		// Identify Your Account tab
		logger.info("Identify [Your Account] tab");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabYourAccount")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabYourAccount")
						.getLocatorvalue());
		// If Your Account tab is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Your Account] sub tab in [Accounts] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Your Account] sub tab in [Accounts] tab",
							true);
			m_assert.fail("Unable to identify [Your Account] sub tab in [Accounts] tab");
		}
		// click on Your Account tab
		logger.info("Click on [Your Account] tab");
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click on [Your Account] sub-tab in [Accounts] tab");
			ReportUtils
					.setStepDescription(
							"Unable to click on [Your Account] sub-tab in [Accounts] tab",
							true);
			m_assert.fail("Unable to click on [Your Account] sub-tab in [Accounts] tab");
		}
		logger.info("Verify if user is on [Your Account] page");
		// Identify Your Account tab Header element
		logger.info("Identify [Your Account] tab header element");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientYourAccountHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientYourAccountHeaderText")
						.getLocatorvalue());
		// If Your Account tab header element is null throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Your Account] sub tab header element in [Accounts] tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Your Account] sub tab header element",
					true);
			m_assert.fail("Unable to identify [Your Account] sub tab header element");
		}
		// Get the text of the header element
		String YourAccoutHeaderText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpYourAccoutHeaderText = Suite.objectRepositoryMap.get(
				"ClientYourAccountHeaderText").getExptext();
		// Compare the text
		isTextMatching = SeleniumUtils.assertEqual(YourAccoutHeaderText,
				ExpYourAccoutHeaderText);
		// If text is not matching then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Your Account] tab header text matching failed. Expected text is ["
					+ ExpYourAccoutHeaderText
					+ "] and the return text is ["
					+ YourAccoutHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Your Account] tab header text matching failed", "",
					ExpYourAccoutHeaderText, YourAccoutHeaderText, true);
			m_assert.fail("[Your Account] tab header text matching failed: Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ClientYourAccountHeaderText").getExptext()
					+ "] and the return text is [" + YourAccoutHeaderText + "]");
		}
		logger.info("Identify [Your Account] page elements");
		// First Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientYourAccountFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientYourAccountFNTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'FirstName' Textbox in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'FirstName' Textbox in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'FirstName' Textbox in [Your Account] tab");
		}
		// Last Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientYourAccountLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientYourAccountLNTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'LastName' Textbox in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'LastName' Textbox in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'LastName' Textbox in [Your Account] tab");
		}
		// Email text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientYourAccountEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientYourAccountEmailTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'Email' Textbox in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'Email' Textbox in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'Email' Textbox in [Your Account] tab");
		}
		// Password element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientYourAccountPasswordTextBox").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientYourAccountPasswordTextBox").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'Password' Textbox in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'Password' Textbox in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'Password' Textbox in [Your Account] tab");
		}
		// Location dropdowns
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientYourAccountCountryDropdown").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientYourAccountCountryDropdown").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'Country' dropdown in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'Country' dropdown in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'Country' dropdown in [Your Account] tab");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientYourAccountTimeZoneDropdown").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientYourAccountTimeZoneDropdown").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'Time Zone' dropdown in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'Time Zone' dropdown in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'Time Zone' dropdown in [Your Account] tab");
		}
		// Time zone link
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientYourAccountCurrentLocationTimeZoneLink")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientYourAccountCurrentLocationTimeZoneLink")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the 'Time Zone' link in [Your Account] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify the 'Time Zone' link in [Your Account] tab",
							true);
			m_assert.fail("Unable to identify the 'Time Zone' link in [Your Account] tab");
		}
		// Scroll Down the browser
		SeleniumUtils.scrollDown();
		if (testcaseArgs.get("userCurrentRole").equalsIgnoreCase("Admin")) {
			// Identify Role & Status element for Admin role only
			logger.info("Identify Roles & Status dropdowns for Admin user");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientYourAccountRoleDropdown").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientYourAccountRoleDropdown").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Role' dropdown for the Admin user in [Your Account] tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify 'Role' dropdown for the Admin user in [Your Account] tab",
								true);
				m_assert.fail("Unable to identify 'Role' dropdown for the Admin user in [Your Account] tab");
			}
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"ClientYourAccountStatusDropdown")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"ClientYourAccountStatusDropdown")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Status' dropdown for the Admin user in [Your Account] tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify 'Status' dropdown for the Admin user in [Your Account] tab",
								true);
				m_assert.fail("Unable to identify 'Status' dropdown for the Admin user in [Your Account] tab");
			}
		}
		// Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientYourAccountBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientYourAccountBtnSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in [Your Account] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Save' button in [Your Account] tab",
					true);
			m_assert.fail("Unable to identify 'Save' button in [Your Account] tab");
		}
		m_assert.assertAll();
	}

	/**
	 * This method verifies User & Roles tab Layout
	 */
	@Test(priority = 8, dependsOnMethods = "loginAs")
	public void verifyUsersAndRolesLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check verifyUsersLayout method in testcaseList
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyUsersAndRolesLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Testcase [verifyUsersAndRolesLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Testcase [verifyUsersAndRolesLayout] is not added for execution",
							false);
			throw new SkipException(
					"Testcase [verifyUsersAndRolesLayout] is not added for execution.");
		}
		logger.info("Starting [verifyUsersAndRolesLayout] execution");
		// read test data
		testcaseArgs = getTestData("verifyUsersAndRolesLayout");
		if (testcaseArgs.get("userCurrentRole").equalsIgnoreCase("Admin")) {
			// Check if users & Roles tab is present for Admin user
			logger.info("Identify [Users & Roles] tab for Admin user");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Users & Roles] sub tab in [Accounts] tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Users & Roles] sub tab in [Accounts] tab",
								true);
				m_assert.fail("Unable to identify [Users & Roles] sub tab in [Accounts] tab");
			}
			// Get the text of the users & Role tab
			String UsersAndRolestabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpUsersAndRolestabText = Suite.objectRepositoryMap.get(
					"ClientTabUsersAndRoles").getExptext();
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(UsersAndRolestabText,
					ExpUsersAndRolestabText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] tab text matching failed. Expected text is ["
						+ ExpUsersAndRolestabText
						+ "] and the actual return text is ["
						+ UsersAndRolestabText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] tab text matching failed", "",
						ExpUsersAndRolestabText, UsersAndRolestabText, true);
				m_assert.fail("[Users & Roles] tab text matching failed. Expected text is ["
						+ ExpUsersAndRolestabText
						+ "] and the actual return text is ["
						+ UsersAndRolestabText + "]");
			}
			// clicking on user & roles tab
			logger.info("Click on [Users & Roles] sub tab in [Accounts] tab");
			boolean isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.info("Unable to click on [Users & Roles] sub-tab");
				ReportUtils.setStepDescription(
						"Unable to click on [Users & Roles] sub-tab", true);
				m_assert.fail("Unable to click on [Users & Roles] sub-tab");
			}
			SeleniumUtils.sleepThread(5);
			logger.info("Verify if User is on [Users & Roles] tab");
			// Identify Users & Roles page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Users & Roles] page header element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Users & Roles] page header element",
								true);
				m_assert.fail("Unable to identify [Users & Roles] tab header element");
			}
			// Get the text
			String UsersAndRolesHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpUsersAndRolesHeaderText = Suite.objectRepositoryMap.get(
					"ClientUserRolesHeaderText").getExptext();
			// Compare the text
			isTextMatching = SeleniumUtils.assertEqual(UsersAndRolesHeaderText,
					ExpUsersAndRolesHeaderText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] page header text matching failed. Expected text is ["
						+ ExpUsersAndRolesHeaderText
						+ "] and the return text is ["
						+ UsersAndRolesHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] page header text matching failed", "",
						ExpUsersAndRolesHeaderText, UsersAndRolesHeaderText,
						true);
				m_assert.fail("[Users & Roles] page header text matching failed. Expected text is ["
						+ ExpUsersAndRolesHeaderText
						+ "] and the return text is ["
						+ UsersAndRolesHeaderText + "]");
			}
			// Identify Roles
			logger.info("Identify Roles header");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientRolesText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientRolesText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Roles header in [Users & Roles] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Roles header in [Users & Roles] page",
								true);
				m_assert.fail("Unable to identify Roles header in [Users & Roles] page");
			}
			// get the text
			String Rolesheader = SeleniumUtils.getText(element);
			// get the exp text
			String ExpRolesheader = Suite.objectRepositoryMap.get(
					"ClientRolesText").getExptext();
			// compare both texts
			isTextMatching = SeleniumUtils.assertEqual(Rolesheader,
					ExpRolesheader);
			if (!isTextMatching) {
				logger.error("[Roles] header text matching failed. Expected text is ["
						+ ExpRolesheader
						+ "] and the return text is ["
						+ Rolesheader + "]");
				ReportUtils.setStepDescription(
						"[Roles] header text matching failed", "",
						ExpRolesheader, Rolesheader, true);
				m_assert.fail("[Roles] header text matching failed. Expected text is ["
						+ ExpRolesheader
						+ "] and the return text is ["
						+ Rolesheader + "]");
			}
			// Identify Users
			logger.info("Identify Users header");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientUsersText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUsersText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Users header in [Users & Roles] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Users header in [Users & Roles] page",
								true);
				m_assert.fail("Unable to identify Users header in [Users & Roles] page");
			}
			// get the text
			String Usersheader = SeleniumUtils.getText(element);
			// get the exp text
			String ExpUsersheader = Suite.objectRepositoryMap.get(
					"ClientUsersText").getExptext();
			// compare both texts
			isTextMatching = SeleniumUtils.assertEqual(Usersheader,
					ExpUsersheader);
			if (!isTextMatching) {
				logger.error("[Users] header text matching failed. Expected text is ["
						+ ExpUsersheader
						+ "] and the return text is ["
						+ Usersheader + "]");
				ReportUtils.setStepDescription(
						"[Users] header text matching failed", "",
						ExpUsersheader, Usersheader, true);
				m_assert.fail("[Users] header text matching failed. Expected text is ["
						+ ExpUsersheader
						+ "] and the return text is ["
						+ Usersheader + "]");
			}
			// Identify New Role button
			logger.info("Identify New Role button");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify New Role Button in [Users & Roles] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify New Role Button in [Users & Roles] page",
								true);
				m_assert.fail("Unable to identify New Role Button in [Users & Roles] page");
			}
			// Click on New Role
			logger.info("Click on 'New Role' button");
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on New Role Button in [Users & Roles] page");
				ReportUtils
						.setStepDescription(
								"Unable to click on identify New Role Button in [Users & Roles] page",
								true);
				m_assert.fail("Unable to click on identify New Role Button in [Users & Roles] page");
			}
			logger.info("verifying Create New Role Header text");
			SeleniumUtils.sleepThread(5);
			// Get the text of the Add Role page
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewRoleHeaderText").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewRoleHeaderText").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify New Role Header element");
				ReportUtils.setStepDescription(
						"Unable to identify New Role Header element", true);
				m_assert.fail("Unable to identify New Role Header element");
			}
			// Get text
			String AddRoleText = SeleniumUtils.getText(element);
			// get exp text
			String ExpAddRoleText = Suite.objectRepositoryMap.get(
					"ClientCreateNewRoleHeaderText").getExptext();
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(AddRoleText,
					ExpAddRoleText);
			if (!isTextMatching) {
				logger.error("[Add Role] header text matching failed. Expected text is ["
						+ ExpAddRoleText
						+ "] and the return text is ["
						+ AddRoleText + "]");
				ReportUtils.setStepDescription(
						"[Add Role] header text matching failed", "",
						ExpAddRoleText, AddRoleText, true);
				m_assert.fail("[Add Role] header text matching failed. Expected text is ["
						+ ExpAddRoleText
						+ "] and the return text is ["
						+ AddRoleText + "]");
			}
			// Identify Alerts & Notifications check box
			logger.info("Identify Alerts & Notifications header in [Add Role] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewRoleAlertsNotificationsText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewRoleAlertsNotificationsText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Alerts & Notifications header in [Add Role] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Alerts & Notifications header in [Add Role] page",
								true);
				m_assert.fail("Unable to identify Alerts & Notifications header in [Add Role] page");
			}
			// get text
			String AlertNotificationText = SeleniumUtils.getText(element);
			// get exp text
			String ExpAlertNotificationText = Suite.objectRepositoryMap.get(
					"ClientCreateNewRoleAlertsNotificationsText").getExptext();
			// compare
			isTextMatching = SeleniumUtils.assertEqual(AlertNotificationText,
					ExpAlertNotificationText);
			if (!isTextMatching) {
				logger.error("Alerts & Notifications header text matching failed in [Add Role] page"
						+ "Expected text is ["
						+ ExpAlertNotificationText
						+ "] and the return text is ["
						+ AlertNotificationText
						+ "]");
				ReportUtils
						.setStepDescription(
								"Alerts & Notifications header text matching failed in [Add Role] page",
								"", ExpAlertNotificationText,
								AlertNotificationText, true);
				m_assert.fail("Alerts & Notifications header text matching failed in [Add Role] page"
						+ "Expected text is ["
						+ ExpAlertNotificationText
						+ "] and the return text is ["
						+ AlertNotificationText
						+ "]");
			}
			// Identify Save button
			logger.info("Identify Save button");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Save button in [Add Role] page");
				ReportUtils.setStepDescription(
						"Unable to identify Save button in [Add Role] page",
						true);
				m_assert.fail("Unable to identify Save button in [Add Role] page");
			}
			// Identify Users & Roles tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			// Click on Users & Roles tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify New User button
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify New User Button in [Users & Roles] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify New User Button in [Users & Roles] page",
								true);
				m_assert.fail("Unable to identify New Role Button in [Users & Roles] page");
			}
			// Click on New User
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on New User Button in [Users & Roles] page");
				ReportUtils
						.setStepDescription(
								"Unable to click on New User Button in [Users & Roles] page",
								true);
				m_assert.fail("Unable to click oon New User Button in [Users & Roles] page");
			}
			SeleniumUtils.sleepThread(5);
			// Identify ADD USER elements
			logger.info("Identify Add User page elements");
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserHeaderText").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserHeaderText").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Add User] header element");
				ReportUtils.setStepDescription(
						"Unable to identify [Add User] header element", true);
				m_assert.fail("Unable to identify [Add User] header element");
			}
			// get text
			String AddUserText = SeleniumUtils.getText(element);
			// get exp text
			String ExpAddUserText = Suite.objectRepositoryMap.get(
					"ClientCreateNewUserHeaderText").getExptext();
			// compare
			isTextMatching = SeleniumUtils.assertEqual(AddUserText,
					ExpAddUserText);
			if (!isTextMatching) {
				logger.error("[Add User] header element text matching failed"
						+ "Expected text is [" + ExpAddUserText
						+ "] and the return text is [" + AddUserText + "]");
				ReportUtils.setStepDescription(
						"[Add User] header element text matching failed", "",
						ExpAddUserText, AddUserText, true);
				m_assert.fail("[Add User] header element text matching failed"
						+ "Expected text is [" + ExpAddUserText
						+ "] and the return text is [" + AddUserText + "]");
			}
			// Identify First name
			logger.info("Identify First Name Text box in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserFNTextBox").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserFNTextBox").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify First Name text box in [Add User] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify First Name text box in [Add User] page",
								true);
				m_assert.fail("Unable to identify First Name text box in [Add User] page");
			}
			// Identify Last name
			logger.info("Identify Last Name Text box in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserLNTextBox").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserLNTextBox").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Last Name text box in [Add User] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Last Name text box in [Add User] page",
								true);
				m_assert.fail("Unable to identify Last Name text box in [Add User] page");
			}
			// Identify Email
			logger.info("Identify Email Text box in [Add User] page");
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"ClientCreateNewUserEmailTextBox")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"ClientCreateNewUserEmailTextBox")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Email text box in [Add User] page");
				ReportUtils.setStepDescription(
						"Unable to identify Email text box in [Add User] page",
						true);
				m_assert.fail("Unable to identify Email text box in [Add User] page");
			}
			// Identify Password
			logger.info("Identify Password Text box in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserPasswordTextBox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserPasswordTextBox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Password text box in [Add User] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Password text box in [Add User] page",
								true);
				m_assert.fail("Unable to identify Password text box in [Add User] page");
			}
			// Identify Country dropdown
			logger.info("Identify Country dropdown in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserCountryDropdown")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserCountryDropdown")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Country dropdown in [Add User] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Country dropdown in [Add User] page",
								true);
				m_assert.fail("Unable to identify Country dropdown in [Add User] page");
			}
			// Identify Time zone dropdown
			logger.info("Identify Time Zone dropdown in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserTimeZoneDropdown")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserTimeZoneDropdown")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Time zone dropdown in [Add User] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Time zone dropdown in [Add User] page",
								true);
				m_assert.fail("Unable to identify Time zone dropdown in [Add User] page");
			}
			// Identify Time zone Link
			logger.info("Identify Time Zone Link in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserCurrentLocationTimeZoneLink")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserCurrentLocationTimeZoneLink")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Time zone Link in [Add User] page");
				ReportUtils.setStepDescription(
						"Unable to identify Time zone Link in [Add User] page",
						true);
				m_assert.fail("Unable to identify Time zone Link in [Add User] page");
			}
			// Identify Role dropdown Link
			logger.info("Identify Role dropdown in [Add User] page");
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"ClientCreateNewUserRoleDropdown")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"ClientCreateNewUserRoleDropdown")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Role dropdown in [Add User] page");
				ReportUtils.setStepDescription(
						"Unable to identify Role dropdown in [Add User] page",
						true);
				m_assert.fail("Unable to identify Role dropdown in [Add User] page");
			}
			// Identify Status dropdown
			logger.info("Identify Status dropdown in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserStatusDropdown")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientCreateNewUserStatusDropdown")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Status dropdown in [Add User] page");
				ReportUtils
						.setStepDescription(
								"Unable to identify Status dropdown in [Add User] page",
								true);
				m_assert.fail("Unable to identify Status dropdown in [Add User] page");
			}
			// Identify Save button
			logger.info("Identify Save button in [Add User] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Save button in [Add User] page");
				ReportUtils.setStepDescription(
						"Unable to identify Save button in [Add User] page",
						true);
				m_assert.fail("Unable to identify Save button in [Add User] page");
			}
			// Identify Users & Roles
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
		} else {
			// If user is not Admin......
		}
		m_assert.assertAll();
	}

	/**
	 * This method verifies negative validation while creating new user
	 */
	@Test(priority = 9, dependsOnMethods = "loginAs")
	public void validationInCreateUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationInCreateUser")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [validationInCreateUser] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [validationInCreateUser] is not added for execution",
							false);
			throw new SkipException(
					"Test case [validationInCreateUser] is not added for execution");
		}
		// Get the test data
		testcaseArgs = getTestData("validationInCreateUser");
		logger.info("Starting [validationInCreateUser] execution");
		// Identify Users & Roles page header element
		logger.info("Identify User is on [Users & Roles] tab");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Users & Roles] page header element");
			ReportUtils.setStepDescription(
					"Unable to identify [Users & Roles] page header element",
					true);
			m_assert.fail("Unable to identify [Users & Roles] tab header element");
		}
		// Get the text
		String UsersAndRolesHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpUsersAndRolesHeaderText = Suite.objectRepositoryMap.get(
				"ClientUserRolesHeaderText").getExptext();
		// Compare the text
		isTextMatching = SeleniumUtils.assertEqual(UsersAndRolesHeaderText,
				ExpUsersAndRolesHeaderText);
		if (!isTextMatching) {
			// Identify the Users & Roles tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Users & Roles] tab element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Users & Roles] tab element",
								false);
				m_assert.fail("Unable to identify [Users & Roles] tab element");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Users & Roles] page header element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Users & Roles] page header element",
								true);
				m_assert.fail("Unable to identify [Users & Roles] tab header element");
			}
			// Get the text
			UsersAndRolesHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpUsersAndRolesHeaderText = Suite.objectRepositoryMap.get(
					"ClientUserRolesHeaderText").getExptext();
			// Compare the text
			isTextMatching = SeleniumUtils.assertEqual(UsersAndRolesHeaderText,
					ExpUsersAndRolesHeaderText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] page header text matching failed. Expected text is ["
						+ ExpUsersAndRolesHeaderText
						+ "] and the return text is ["
						+ UsersAndRolesHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] page header text matching failed", "",
						ExpUsersAndRolesHeaderText, UsersAndRolesHeaderText,
						true);
				m_assert.fail("[Users & Roles] page header text matching failed. Expected text is ["
						+ ExpUsersAndRolesHeaderText
						+ "] and the return text is ["
						+ UsersAndRolesHeaderText + "]");
			}
		}
		// Identify & click on New User Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify New User button");
			ReportUtils.setStepDescription(
					"Unable to identify New User button", true);
			m_assert.fail("Unable to identify New User button");
		}
		// click on New User
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// Identify First Name
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		logger.info("Validate all the error messages while creating New User");
		// Keep all the fields empty and click on Save button
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify the Add User Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		String headerText = SeleniumUtils.getText(element);
		// get the exp text
		String ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at First Name field in [Add User]: User can"
					+ "create new user with First Name field as empty");
			ReportUtils
					.setStepDescription(
							"Validation failed at First Name field in [Add User]: User can"
									+ " create new user with First Name field as empty",
							true);
			m_assert.fail("Validation failed at First Name field in [Add User]: User can"
					+ "create new user with First Name field as empty");
		}
		// Enter text in first name field and keep all other fields empty
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		// Type First Name
		SeleniumUtils.type(element, testcaseArgs.get("firstName"));
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		// Identify the Add User Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		headerText = SeleniumUtils.getText(element);
		// get the exp text
		ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at Last Name field in [Add User]: User can"
					+ " create new user with Last Name field as empty");
			ReportUtils.setStepDescription(
					"Validation failed at Last Name field in [Add User]: User can"
							+ " create new user with Last Name field as empty",
					true);
			m_assert.fail("Validation failed at Last Name field in [Add User]: User can"
					+ "create new user with Last Name field as empty");
		}
		// Enter text in firstname field, last name field and keep all other
		// fields empty
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatorvalue());
		// Type Last Name field
		SeleniumUtils.type(element, testcaseArgs.get("lastName"));
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		// Identify the Add User Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		headerText = SeleniumUtils.getText(element);
		// get the exp text
		ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at Email field in [Add User]: User can"
					+ " create new user with Email field as empty");
			ReportUtils.setStepDescription(
					"Validation failed at Email field in [Add User]: User can"
							+ " create new user with Email field as empty",
					true);
			m_assert.fail("Validation failed at Email field in [Add User]: User can"
					+ " create new user with Email field as empty");
		}
		// Enter text in first name field, last name,email(invalid format) field
		// and keep all other fields empty
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		// Type in Email
		SeleniumUtils.type(element, testcaseArgs.get("invalidEmail"));
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		// Identify the Add User Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		headerText = SeleniumUtils.getText(element);
		// get the exp text
		ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at Password field in [Add User]: User can"
					+ " create new user with Password field as empty");
			ReportUtils.setStepDescription(
					"Validation failed at Password field in [Add User]: User can"
							+ " create new user with Password field as empty",
					true);
			m_assert.fail("Validation failed at Password field in [Add User]: User can"
					+ "create new user with Password field as empty");
		}
		// Enter text in first name field, last name,email(invalid format)
		// field,password and keep all other fields empty
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		// Type in Password field
		SeleniumUtils.type(element, testcaseArgs.get("password"));
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		// Identify Invalid Email error message text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserInvalidEmailPopup")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserInvalidEmailPopup")
						.getLocatorvalue());
		// Get the text
		String invalidEmailMsg = SeleniumUtils
				.getTextFromAlertPopup(Suite.objectRepositoryMap.get(
						"ClientCreateNewUserInvalidEmailMSG").getLocatorvalue());
		// Get the exp text
		String ExpinvalidEmailMsg = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserInvalidEmailMSG").getExptext();
		boolean isMessageMatching = SeleniumUtils.assertEqual(invalidEmailMsg,
				ExpinvalidEmailMsg);
		if (!isMessageMatching) {
			logger.error("Validation failed at Email field in [Add User]: No Error"
					+ "Message displayed for Invalid Email");
			ReportUtils.setStepDescription(
					"Validation failed at Email field in [Add User]: No Error"
							+ "Message displayed for Invalid Email", true);
			m_assert.fail("Validation failed at Email field in [Add User]: No Error"
					+ "Message displayed for Invalid Email");
		}
		// Identify the Add User Header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		headerText = SeleniumUtils.getText(element);
		// get the exp text
		ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at Email field in [Add User]: User can"
					+ "create new user with Invalid Email");
			ReportUtils.setStepDescription(
					"Validation failed at Email field in [Add User]: User can"
							+ "create new user with Invalid Email", true);
			m_assert.fail("Validation failed at Email field in [Add User]: User can"
					+ "create new user with Invalid Email");
		}
		// Validation on Password field
		logger.info("Validation on Password field");
		// Enter text in first name field, last name,email field,password (less
		// than 6 characters)
		// Identify Email
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		// Type Correct Email
		SeleniumUtils.type(element, testcaseArgs.get("email"));
		// Identify Password
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		// Type Invalid Password
		SeleniumUtils.type(element, testcaseArgs.get("invalidPassword"));
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		// wait for the page to display email error message
		SeleniumUtils.sleepThread(2);
		// Identify Invalid Password Element pop up
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserInvalidPasswordMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientCreateNewUserInvalidPasswordMSG")
						.getLocatorvalue());
		// Get the text
		String invalidPasswordMsg = SeleniumUtils
				.getTextFromAlertPopup(Suite.objectRepositoryMap.get(
						"ClientCreateNewUserInvalidPasswordMSG")
						.getLocatorvalue());
		// Get the exp text
		String ExpinvalidPasswordMsg = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserInvalidPasswordMSG").getExptext();
		isMessageMatching = SeleniumUtils.assertEqual(invalidPasswordMsg,
				ExpinvalidPasswordMsg);
		if (!isMessageMatching) {
			logger.error("Validation failed at Password field in [Add User]: No Error"
					+ "Message displayed for Invalid Password (less than 6 characters)");
			ReportUtils
					.setStepDescription(
							"Validation failed at Password field in [Add User]: No Error"
									+ "Message displayed for Invalid Password (less than 6 characters)",
							true);
			m_assert.fail("Validation failed at Password field in [Add User]: No Error"
					+ "Message displayed for Invalid Password (less than 6 characters)");
		}
		// Identify the Add User Header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		headerText = SeleniumUtils.getText(element);
		// get the exp text
		ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at Password field in [Add User]: User can"
					+ "create new user with Invalid Password (less than 6 characters)");
			ReportUtils
					.setStepDescription(
							"Validation failed at Password field in [Add User]: User can"
									+ "create new user with Invalid Password (less than 6 characters)",
							true);
			m_assert.fail("Validation failed at Password field in [Add User]: User can"
					+ "create new user with Invalid Password (less than 6 characters)");
		}
		// Validations on First Name and Last Name with white spaces
		// Identify First Name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		// Clear the text
		SeleniumUtils.clearText(element);
		// Type white space in First Name field
		SeleniumUtils.typeKeys(element, Keys.SPACE);
		// Identify Last Name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatorvalue());
		// Clear the text
		SeleniumUtils.clearText(element);
		// Type white space in Last Name field
		SeleniumUtils.typeKeys(element, Keys.SPACE);
		// Identify Email
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		// Type Correct Email
		SeleniumUtils.type(element, testcaseArgs.get("email"));
		// Identify Password
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		// Clear the text
		SeleniumUtils.clearText(element);
		// Type Password
		SeleniumUtils.type(element, testcaseArgs.get("password"));
		SeleniumUtils.scrollDown();
		// Identify Save
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save
		SeleniumUtils.clickOnElement(element);
		// wait for the page to display email error message
		SeleniumUtils.sleepThread(3);
		// Identify the Add User Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserHeaderText")
						.getLocatorvalue());
		// get the text
		headerText = SeleniumUtils.getText(element);
		// get the exp text
		ExpheaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserHeaderText").getExptext();
		// compare both texts
		isTextMatching = SeleniumUtils.assertEqual(headerText, ExpheaderText);
		if (!isTextMatching) {
			logger.error("Validation failed at First and Last Name fields "
					+ "in [Add User]: User can"
					+ " create new user account when user enters white "
					+ "space in First and Last Name fields");
			ReportUtils.setStepDescription(
					"Validation failed at First and Last Name "
							+ "fields in [Add User]: User can"
							+ " create new user account when user "
							+ "enters white space in First and "
							+ "Last Name fields", true);
			m_assert.fail("Validation failed at First and Last Name "
					+ "fields in [Add User]: User can"
					+ " create new user account when user enters "
					+ "white space in First and Last Name fields");
			// Delete the created user account
			// Identify Users & Roles tab
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
					element, testcaseArgs.get("email"));
			if (!isUserPresent) {
				logger.error("Created new User with email id ["
						+ testcaseArgs.get("email")
						+ "] is not present in Users list");
				ReportUtils.setStepDescription(
						"Created new User with email id ["
								+ testcaseArgs.get("email")
								+ "] is not present in Users list", true);
				m_assert.fail("Created new User with email id ["
						+ testcaseArgs.get("email")
						+ "] is not present in Users list");
			}
			// Identify Users list table
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ClientUserRolesUsersListTable").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ClientUserRolesUsersListTable").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify the users table list");
				ReportUtils.setStepDescription(
						"Unable to identify the users table list", true);
				m_assert.fail("Unable to identify the users table list");
			}
			if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
				SeleniumUtils.acceptAlertWindowInSafariBrowser();
				SeleniumUtils.sleepThread(3);
				// Click on Delete link of specific user
				isClicked = SeleniumCustomUtils.deleteUserAccountViaEmail(
						element, testcaseArgs.get("email"));
				if (!isClicked) {
					logger.error("Click operation fails at the specified email  "
							+ testcaseArgs.get("email"));
					ReportUtils.setStepDescription(
							"Click operation fails at the specified email  "
									+ testcaseArgs.get("email"), true);
					m_assert.fail("Click operation fails at the specified email  "
							+ testcaseArgs.get("email"));
				}
			} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
				// Click on Delete link of specific user
				isClicked = SeleniumCustomUtils
						.delete_UserAccount_By_Email_In_IE(element,
								testcaseArgs.get("email"));
				if (!isClicked) {
					logger.error("Click operation fails at the specified email  "
							+ testcaseArgs.get("email"));
					ReportUtils.setStepDescription(
							"Click operation fails at the specified email  "
									+ testcaseArgs.get("email"), true);
					m_assert.fail("Click operation fails at the specified email  "
							+ testcaseArgs.get("email"));
				}
			} else {
				isClicked = SeleniumCustomUtils.deleteUserAccountViaEmail(
						element, testcaseArgs.get("email"));
				if (!isClicked) {
					logger.error("Click operation fails at the specified email  "
							+ testcaseArgs.get("email"));
					ReportUtils.setStepDescription(
							"Click operation fails at the specified email  "
									+ testcaseArgs.get("email"), true);
					m_assert.fail("Click operation fails at the specified user  "
							+ testcaseArgs.get("email"));
				}
				SeleniumUtils.sleepThread(2);
				// Click on Ok message of Alert window
				SeleniumUtils.acceptAlertWindow();
			}
			SeleniumUtils.sleepThread(2);
			// Identify Alert success message element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientDeleteUserSuccessMSG")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientDeleteUserSuccessMSG")
							.getLocatorvalue());
			// Get the text
			String delUserSuccessMsg = SeleniumUtils
					.perform_SubString_And_Trim(SeleniumUtils.getText(element),
							1);
			// Get the exp text
			String ExpdelUserSuccessMsg = Suite.objectRepositoryMap.get(
					"ClientDeleteUserSuccessMSG").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(delUserSuccessMsg,
					ExpdelUserSuccessMsg);
			if (!isTextMatching) {
				logger.error("Delete User success message text matching failed. "
						+ "The Expected text is ["
						+ ExpdelUserSuccessMsg
						+ "] and the return text is ["
						+ delUserSuccessMsg
						+ "]");
				ReportUtils.setStepDescription(
						"Delete User success message text matching failed", "",
						ExpdelUserSuccessMsg, delUserSuccessMsg, true);
				m_assert.fail("Delete User success message text matching failed. "
						+ "The Expected text is ["
						+ ExpdelUserSuccessMsg
						+ "] and the return text is ["
						+ delUserSuccessMsg
						+ "]");
			}
		}
		SeleniumUtils.sleepThread(4);
		m_assert.assertAll();
	}

	@Test(priority = 10, dependsOnMethods = "loginAs")
	public void createUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createUser")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createUser] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [createUser] is not added for execution", false);
			throw new SkipException(
					"Test case [createUser] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("createUser");
		logger.info("Starting [CreateUser] execution");
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
		if (element != null) {
			// Get the text
			String actual_Return_Text = SeleniumUtils.getText(element)
					.substring(1).trim();
			// Get exp return text
			String exp_New_User_Success_Msg = Suite.objectRepositoryMap.get(
					"ClientCreateNewUserSuccessMSG").getExptext();
			// Compare the actual text with exp text
			if (actual_Return_Text.equalsIgnoreCase(exp_New_User_Success_Msg)) {
				logger.info("New User success message is displayed");
				// Identify Users & Roles tab
				element = SeleniumUtils.findobject(Suite.objectRepositoryMap
						.get("ClientTabUsersAndRoles").getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
								.getLocatorvalue());
				// Click on Users & Roles
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(3);
				// Identify Users List table
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientUserRolesUsersListTable")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientUserRolesUsersListTable")
								.getLocatorvalue());
				// Identify the just created role
				boolean isUserPresent = SeleniumCustomUtils.verifyUserInTable(
						element, testcaseArgs.get("firstName") + " "
								+ testcaseArgs.get("lastName"));
				if (!isUserPresent) {
					logger.error("Created new User ["
							+ testcaseArgs.get("firstName") + " "
							+ testcaseArgs.get("lastName")
							+ "] is not present in Users list");
					ReportUtils.setStepDescription("Created new User ["
							+ testcaseArgs.get("firstName") + " "
							+ testcaseArgs.get("lastName")
							+ "] is not present in Users list", true);
					m_assert.fail("Created new User ["
							+ testcaseArgs.get("firstName") + " "
							+ testcaseArgs.get("lastName")
							+ "] is not present in Users list");
				}

			} else {
				logger.info("New User success message is not displayed");
				// Get exp return text
				String exp_Email_Address_Taken_Msg = Suite.objectRepositoryMap
						.get("ClientEmailAlreadyTakenMSG").getExptext();
				if (actual_Return_Text
						.equalsIgnoreCase(exp_Email_Address_Taken_Msg)) {
					logger.warn("Email Address already taken message is displayed");
					logger.warn("The given Email address is already taken. "
							+ "Please use another Email");
					ReportUtils.setStepDescription(
							"The given Email address is already taken. "
									+ "Please use another Email", true);
					m_assert.fail("The given Email address is already taken. "
							+ "Please use another Email");
				} else {
					logger.error("Unexpected error text displayed. The text is ["
							+ actual_Return_Text + "]");
					ReportUtils.setStepDescription(
							"Unexpected error text displayed. The text is ["
									+ actual_Return_Text + "]", true);
					m_assert.fail("Unexpected error text displayed. The text is ["
							+ actual_Return_Text + "]");
				}
			}
		} else {
			logger.error("Unable to identify either Successful message or "
					+ "Error message for new User creation");
			ReportUtils.setStepDescription(
					"Unable to identify either Successful message or "
							+ "Error message for new User creation", true);
			m_assert.fail("Unable to identify either Successful message or "
					+ "Error message for new User creation");
		}
		// Identify Users & Roles tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		m_assert.assertAll();
	}

	/**
	 * method to edit the user in Users&Roles
	 */
	@Test(priority = 11, dependsOnMethods = "loginAs")
	public void editUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editUser")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [editUser] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [editUser] is not added for execution", false);
			throw new SkipException(
					"Test case [editUser] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("editUser");
		logger.info("Starting [editUser]execution");
		logger.info("executing click operation on edit link for the user "
				+ testcaseArgs.get("username"));
		// Identify List table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the users table list");
			ReportUtils.setStepDescription(
					"Unable to identify the users table list", true);
			m_assert.fail("Unable to identify the users table list");
		}
		// Click on Edit link of specific user
		boolean isClicked = SeleniumCustomUtils
				.click_At_EditLink_At_Users_In_UsersAndRoles(element,
						testcaseArgs.get("username"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Click operation fails at the specified user  "
					+ testcaseArgs.get("username"));
			ReportUtils.setStepDescription(
					"Click operation fails at the specified user  "
							+ testcaseArgs.get("username"), true);
			m_assert.fail("Click operation fails at the specified user  "
					+ testcaseArgs.get("username"));
		}
		logger.info("Validate the header text in edit user page");
		// Identify Edit User header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientEditUserHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientEditUserHeaderText")
						.getLocatorvalue());
		// Get the header text
		String editUserHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpEditUserHeaderText = Suite.objectRepositoryMap.get(
				"ClientEditUserHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(editUserHeaderText,
				ExpEditUserHeaderText);
		if (!isTextMatching) {
			logger.error("[Edit User] page header text matching failed. The Expected text is ["
					+ ExpEditUserHeaderText
					+ "] and the return text is ["
					+ editUserHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Edit User] page header text matching failed", "",
					ExpEditUserHeaderText, editUserHeaderText, true);
			m_assert.fail("[Edit User] page header text matching failed. The Expected text is ["
					+ ExpEditUserHeaderText
					+ "] and the return text is ["
					+ editUserHeaderText + "]");
		}
		// First Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		// Clear the first name text which was entered previously
		SeleniumUtils.clearText(element);
		// Enter new first name text
		SeleniumUtils.type(element, testcaseArgs.get("firstName"));
		// Last Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatorvalue());
		// Clear the last name text which was entered previously
		SeleniumUtils.clearText(element);
		// enter new last name text
		SeleniumUtils.type(element, testcaseArgs.get("lastName"));
		// Email text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		// Clear the email text
		SeleniumUtils.clearText(element);
		// Enter new email text
		SeleniumUtils.type(element, testcaseArgs.get("email"));
		// Password text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		// clear password text
		SeleniumUtils.clearText(element);
		// enter new password text
		SeleniumUtils.type(element, testcaseArgs.get("password"));
		SeleniumUtils.scrollDown();
		// Identify SAve button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Validate the success message after edit the User");
		// Identify success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserSuccessMSG")
						.getLocatorvalue());
		// Get text
		String userSuccessMsg = SeleniumUtils.getText(element).substring(1)
				.trim();
		// Get exp text
		String ExpuserSuccessMsg = Suite.objectRepositoryMap.get(
				"ClientCreateNewUserSuccessMSG").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(userSuccessMsg,
				ExpuserSuccessMsg);
		if (!isTextMatching) {
			logger.error("Edit User success message text matching failed. "
					+ "The Expected text is [" + ExpuserSuccessMsg
					+ "] and the return text is [" + userSuccessMsg + "]");
			ReportUtils.setStepDescription(
					"Edit User success message text matching failed", "",
					ExpuserSuccessMsg, userSuccessMsg, true);
			m_assert.fail("Edit User success message text matching failed. "
					+ "The Expected text is [" + ExpuserSuccessMsg
					+ "] and the return text is [" + userSuccessMsg + "]");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		// Click on Users & Roles
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify Users list table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatorvalue());
		// Identify the just created role
		boolean isUserPresent = SeleniumCustomUtils.verifyUserInTable(
				element,
				testcaseArgs.get("firstName") + " "
						+ testcaseArgs.get("lastName"));
		if (!isUserPresent) {
			logger.error("Edited existing User ["
					+ testcaseArgs.get("firstName") + " "
					+ testcaseArgs.get("lastName")
					+ "] is not present in Users list");
			ReportUtils.setStepDescription(
					"Edited existing User [" + testcaseArgs.get("firstName")
							+ " " + testcaseArgs.get("lastName")
							+ "] is not present in Users list", true);
			m_assert.fail("Edited existing User ["
					+ testcaseArgs.get("firstName") + " "
					+ testcaseArgs.get("lastName")
					+ "] is not present in Users list");
		}
		m_assert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "loginAs")
	public void deleteUser() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteUser")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [deleteUser] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [deleteUser] is not added for execution", false);
			throw new SkipException(
					"Test case [deleteUser] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("deleteUser");
		logger.info("Starting [deleteUser] execution");
		logger.info("executing click operation on delete link for the user "
				+ testcaseArgs.get("username"));
		// Identify Users list table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify the users table list");
			ReportUtils.setStepDescription(
					"Unable to identify the users table list", true);
			m_assert.fail("Unable to identify the users table list");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete link of specific user
			isClicked = SeleniumCustomUtils.clickAtDeleteForUsers(element,
					testcaseArgs.get("username"));
			if (!isClicked) {
				logger.error("Click operation fails at the specified user  "
						+ testcaseArgs.get("username"));
				ReportUtils.setStepDescription(
						"Click operation fails at the specified user  "
								+ testcaseArgs.get("username"), true);
				m_assert.fail("Click operation fails at the specified user  "
						+ testcaseArgs.get("username"));
			}
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			// Click on Delete link of specific user
			isClicked = SeleniumCustomUtils
					.delete_User_By_UserName_for_IE_in_UsersAndRoles(element,
							testcaseArgs.get("username"));
			if (!isClicked) {
				logger.error("Click operation fails at the specified email  "
						+ testcaseArgs.get("email"));
				ReportUtils.setStepDescription(
						"Click operation fails at the specified email  "
								+ testcaseArgs.get("email"), true);
				m_assert.fail("Click operation fails at the specified email  "
						+ testcaseArgs.get("email"));
			}

		} else {
			isClicked = SeleniumCustomUtils.clickAtDeleteForUsers(element,
					testcaseArgs.get("username"));
			if (!isClicked) {
				logger.error("Click operation fails at the specified user  "
						+ testcaseArgs.get("username"));
				ReportUtils.setStepDescription(
						"Click operation fails at the specified user  "
								+ testcaseArgs.get("username"), true);
				m_assert.fail("Click operation fails at the specified user  "
						+ testcaseArgs.get("username"));
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		}
		SeleniumUtils.sleepThread(2);
		// Identify Alert success message element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientDeleteUserSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientDeleteUserSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String delUserSuccessMsg = SeleniumUtils.getText(element).substring(1)
				.trim();
		// Get the exp text
		String ExpdelUserSuccessMsg = Suite.objectRepositoryMap.get(
				"ClientDeleteUserSuccessMSG").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(delUserSuccessMsg,
				ExpdelUserSuccessMsg);
		if (!isTextMatching) {
			logger.error("Delete User success message text matching failed."
					+ " The Expected text is [" + ExpdelUserSuccessMsg
					+ "] and the return text is [" + delUserSuccessMsg + "]");
			ReportUtils.setStepDescription(
					"Delete User success message text matching failed", "",
					ExpdelUserSuccessMsg, delUserSuccessMsg, true);
			m_assert.fail("Delete User success message text matching failed. "
					+ "The Expected text is [" + ExpdelUserSuccessMsg
					+ "] and the return text is [" + delUserSuccessMsg + "]");
		}
		// Identify Users List table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesUsersListTable")
						.getLocatorvalue());
		// Validate if the deleted user still available in Users list
		boolean isNamePresent = SeleniumCustomUtils.verifyUserInTable(element,
				testcaseArgs.get("username"));
		if (isNamePresent) {
			logger.error("Deleted user still available in Users list");
			ReportUtils.setStepDescription(
					"Deleted user still available in Users list", true);
			m_assert.fail("Deleted user still available in Users list");
		}
		m_assert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods = "loginAs")
	public void validationInCreateRole() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationInCreateRole")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [validationInCreateRole] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [validationInCreateRole] is not added for execution",
							false);
			throw new SkipException(
					"Test case [validationInCreateRole] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("validationInCreateRole");
		logger.info("Verify if user is on [Users & Roles] page");
		// Identify Users & Roles tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatorvalue());
		// Get the text
		String UsersAndRolestabHeaderText = SeleniumUtils.getText(element);
		// Get the return text
		String ExpUsersAndRolestabHeaderText = Suite.objectRepositoryMap.get(
				"ClientUserRolesHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(UsersAndRolestabHeaderText,
				ExpUsersAndRolestabHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Users & Roles] tab");
			// Identify Users & Roles tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Users & Roles tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatorvalue());
			// Get the text
			UsersAndRolestabHeaderText = SeleniumUtils.getText(element);
			// Get the return text
			ExpUsersAndRolestabHeaderText = Suite.objectRepositoryMap.get(
					"ClientUserRolesHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(
					UsersAndRolestabHeaderText, ExpUsersAndRolestabHeaderText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] tab header text matching failed. Expected text is ["
						+ ExpUsersAndRolestabHeaderText
						+ "] and the return text is ["
						+ UsersAndRolestabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] tab header text matching failed", "",
						ExpUsersAndRolestabHeaderText,
						UsersAndRolestabHeaderText, true);
				m_assert.fail("[Users & Roles] tab header text matching failed. Expected text is ["
						+ ExpUsersAndRolestabHeaderText
						+ "] and the return text is ["
						+ UsersAndRolestabHeaderText + "]");
			}
		}
		logger.info("Starting [validationInCreateRole] execution");
		SeleniumUtils.sleepThread(3);
		// Identify New Role button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
						.getLocatorvalue());
		// click on New Role
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		if (!isClicked) {
			logger.error("Unable to click on New Role button");
			ReportUtils.setStepDescription(
					"Unabel to click on New Role button", true);
			m_assert.fail("Unable to click on New Role button");
		}
		// Input text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatorvalue());
		// Enter blank
		SeleniumUtils.type(element, null);
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button in [Create Role] page");
			ReportUtils.setStepDescription(
					"Unable to identify Save button in [Create Role] page",
					true);
			m_assert.fail("Unable to identify Save button in [Create Role] page");
		}
		// click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click on Save button in [Create Role] page");
			ReportUtils.setStepDescription(
					"Unable to click on Save button in [Create Role] page",
					true);
			m_assert.fail("Unable to click on Save button in [Create Role] page");
		}
		// Identify Add Role header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleHeaderText")
						.getLocatorvalue());
		// Get the text
		String createRoleHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpcreateRoleHeaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewRoleHeaderText").getExptext();
		// Compare the both texts
		isTextMatching = SeleniumUtils.assertEqual(createRoleHeaderText,
				ExpcreateRoleHeaderText);
		if (!isTextMatching) {
			logger.error("Validation fails at Role Name text box. User able to "
					+ "create New Role with empty value in Role name text box");
			ReportUtils
					.setStepDescription(
							"Validation fails at Role Name text box. User able to "
									+ "create New Role with empty value in Role name text box",
							true);
			m_assert.fail("Validation fails at Role Name text box. User able to "
					+ "create New Role with empty value in Role name text box");
		}
		// Input text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatorvalue());
		// Enter white spaces
		SeleniumUtils.typeKeys(element, Keys.SPACE);
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatorvalue());
		// click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify Error message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ClientCreateNewRoleErrorMsgForWhiteSpaces")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientCreateNewRoleErrorMsgForWhiteSpaces")
						.getLocatorvalue());
		// Get the text
		String NewRoleErrorMsg = SeleniumUtils.getText(element).substring(1)
				.trim();
		// Get the exp text
		String ExpNewRoleErrorMsg = Suite.objectRepositoryMap.get(
				"ClientCreateNewRoleErrorMsgForWhiteSpaces").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(NewRoleErrorMsg,
				ExpNewRoleErrorMsg);
		if (!isTextMatching) {
			logger.error("Error message while creating new Role, text matching failed. "
					+ "Expected text is ["
					+ ExpNewRoleErrorMsg
					+ "] and the return text is [" + NewRoleErrorMsg + "]");
			ReportUtils
					.setStepDescription(
							"Error message while creating new Role, text matching failed",
							"", ExpNewRoleErrorMsg, NewRoleErrorMsg, true);
			m_assert.fail("Error message while creating new Role, text matching failed. "
					+ "Expected text is ["
					+ ExpNewRoleErrorMsg
					+ "] and the return text is [" + NewRoleErrorMsg + "]");
		}
		// Identify Add Role header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleHeaderText")
						.getLocatorvalue());
		// Get the text
		createRoleHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		ExpcreateRoleHeaderText = Suite.objectRepositoryMap.get(
				"ClientCreateNewRoleHeaderText").getExptext();
		// Compare the both texts
		isTextMatching = SeleniumUtils.assertEqual(createRoleHeaderText,
				ExpcreateRoleHeaderText);
		if (!isTextMatching) {
			// Identify if user is on Edit Role page
			String expEditRolePageHeaderText = Suite.objectRepositoryMap.get(
					"ClientEditRoleHeaderText").getExptext();
			// Compare the actual return text with editRolePageHeaderText
			isTextMatching = SeleniumUtils.assertEqual(createRoleHeaderText,
					expEditRolePageHeaderText);
			if (isTextMatching) {
				logger.error("Validation fails at Role Name text box. User able to "
						+ "create New Role with white space in Role name text box");
				ReportUtils
						.setStepDescription(
								"Validation fails at Role Name text box. User able to "
										+ "create New Role with empty value in Role name text box",
								true);
				m_assert.fail("Validation fails at Role Name text box. User able to "
						+ "create New Role with white space in Role name text box");
			} else {
				logger.error("Validation fails at Role Name text box. Unexpected error happens while "
						+ "creating New Role with white space in Role name text box");
				ReportUtils
						.setStepDescription(
								"Validation fails at Role Name text box. Unexpected error happens while "
										+ "creating New Role with white space in Role name text box",
								true);
				m_assert.fail("Validation fails at Role Name text box. Unexpected error happens while "
						+ "creating New Role with white space in Role name text box");
				// navigate to backward
				SeleniumUtils.navigateToBackWard();
				// Wait for the page to load
				SeleniumUtils.sleepThread(3);
			}
		}
		m_assert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = "loginAs")
	public void createRole() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createRole")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createRole] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [createRole] is not added for execution", false);
			throw new SkipException(
					"Test case [createRole] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("createRole");
		logger.info("Starting [createRole] execution");
		// Identify Users & Roles tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatorvalue());
		// Get the text
		String UsersAndRolestabHeaderText = SeleniumUtils.getText(element);
		// Get the return text
		String ExpUsersAndRolestabHeaderText = Suite.objectRepositoryMap.get(
				"ClientUserRolesHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(UsersAndRolestabHeaderText,
				ExpUsersAndRolestabHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Users & Roles] tab");
			// Identify Users & Roles tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			// Identify Users & Roles tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatorvalue());
			// Get the text
			UsersAndRolestabHeaderText = SeleniumUtils.getText(element);
			// Get the return text
			ExpUsersAndRolestabHeaderText = Suite.objectRepositoryMap.get(
					"ClientUserRolesHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(
					UsersAndRolestabHeaderText, ExpUsersAndRolestabHeaderText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] tab header text matching failed. Expected text is ["
						+ ExpUsersAndRolestabHeaderText
						+ "] and the return text is ["
						+ UsersAndRolestabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] tab header text matching failed", "",
						ExpUsersAndRolestabHeaderText,
						UsersAndRolestabHeaderText, true);
				m_assert.fail("[Users & Roles] tab header text matching failed. Expected text is ["
						+ ExpUsersAndRolestabHeaderText
						+ "] and the return text is ["
						+ UsersAndRolestabHeaderText + "]");
			}
		}
		// Identify New Role button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
						.getLocatorvalue());
		// click on New Role
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		if (!isClicked) {
			logger.error("Unable to click on New Role button");
			ReportUtils.setStepDescription(
					"Unabel to click on New Role button", true);
			m_assert.fail("Unable to click on New Role button");
		}
		// Input text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatorvalue());
		// Enter Role name
		SeleniumUtils.type(element, testcaseArgs.get("name"));
		// Select the permissions on Alerts, Organizations, CME
		logger.info("Select checkboxes based on the input");
		isClicked = SeleniumCustomUtils.checkCheckboxesAsPerInput(testcaseArgs
				.get("alertsPermissions1"));
		if (!isClicked) {
			logger.error("Unable to click on check box of " + "["
					+ testcaseArgs.get("alertsPermissions1") + "]");
			ReportUtils.setStepDescription("Unable to click on check box of "
					+ "[" + testcaseArgs.get("alertsPermissions1") + "]", true);
			m_assert.fail("Unable to click on check box of " + "["
					+ testcaseArgs.get("alertsPermissions1") + "]");
		}
		isClicked = SeleniumCustomUtils.checkCheckboxesAsPerInput(testcaseArgs
				.get("orgPermissions1"));
		if (!isClicked) {
			logger.error("Unable to click on check box of " + "["
					+ testcaseArgs.get("orgPermissions1") + "]");
			ReportUtils.setStepDescription("Unable to click on check box of "
					+ "[" + testcaseArgs.get("orgPermissions1") + "]", true);
			m_assert.fail("Unable to click on check box of " + "["
					+ testcaseArgs.get("orgPermissions1") + "]");
		}
		isClicked = SeleniumCustomUtils.checkCheckboxesAsPerInput(testcaseArgs
				.get("CMEPermissions1"));
		if (!isClicked) {
			logger.error("Unable to click on check box of " + "["
					+ testcaseArgs.get("CMEPermissions1") + "]");
			ReportUtils.setStepDescription("Unable to click on check box of "
					+ "[" + testcaseArgs.get("CMEPermissions1") + "]", true);
			m_assert.fail("Unable to click on check box of " + "["
					+ testcaseArgs.get("CMEPermissions1") + "]");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button");
			ReportUtils.setStepDescription("Unable to identify Save button",
					true);
			m_assert.fail("Unable to identify Save button");
		}
		// click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on Save button");
			ReportUtils.setStepDescription("Unable to click on Save button",
					true);
			m_assert.fail("Unable to click on Save button");
		}
		SeleniumUtils.sleepThread(4);
		// Identify Success message message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateRoleSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateRoleSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String NewRoleSuccessMsg = SeleniumUtils.getText(element).substring(1)
				.trim();
		// Get the exp text
		String ExpNewRoleSuccessMsg = Suite.objectRepositoryMap.get(
				"ClientCreateRoleSuccessMSG").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(NewRoleSuccessMsg,
				ExpNewRoleSuccessMsg);
		if (!isTextMatching) {
			logger.error("New Role success message text matching failed. Expected text is ["
					+ ExpNewRoleSuccessMsg
					+ "] and the return text is ["
					+ NewRoleSuccessMsg + "]");
			ReportUtils.setStepDescription(
					"New Role success message text matching failed", "",
					ExpNewRoleSuccessMsg, NewRoleSuccessMsg, true);
			m_assert.fail("New Role success message text matching failed. Expected text is ["
					+ ExpNewRoleSuccessMsg
					+ "] and the return text is ["
					+ NewRoleSuccessMsg + "]");
		}
		// Identify Users & Role tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Validate the just created role
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Role table list");
			ReportUtils.setStepDescription(
					"Unable to identify Role table list", true);
			m_assert.fail("Unable to identify Role table list");
		}
		// Identify just created role in Role table
		boolean isRolePresent = SeleniumCustomUtils.verifyRoleNameInRoleList(
				element, testcaseArgs.get("name"));
		if (!isRolePresent) {
			logger.error("Created Role is not available in Role table list");
			ReportUtils.setStepDescription(
					"Created Role is not available in Role table list", true);
			m_assert.fail("Created Role is not available in Role table list");
		}
		m_assert.assertAll();
	}

	@Test(priority = 15, dependsOnMethods = "loginAs")
	public void editRole() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editRole")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [editRole] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [editRole] is not added for execution", false);
			throw new SkipException(
					"Test case [editRole] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("editRole");
		logger.info("Starting [editRole] execution");
		// Identify Users & Roles tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
						.getLocatorvalue());
		// Get the text
		String UsersAndRolestabHeaderText = SeleniumUtils.getText(element);
		// Get the return text
		String ExpUsersAndRolestabHeaderText = Suite.objectRepositoryMap.get(
				"ClientUserRolesHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(UsersAndRolestabHeaderText,
				ExpUsersAndRolestabHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Users & Roles] tab");
			// Identify Users & Roles tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			// Identify Users & Roles tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientUserRolesHeaderText")
							.getLocatorvalue());
			// Get the text
			UsersAndRolestabHeaderText = SeleniumUtils.getText(element);
			// Get the return text
			ExpUsersAndRolestabHeaderText = Suite.objectRepositoryMap.get(
					"ClientUserRolesHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(
					UsersAndRolestabHeaderText, ExpUsersAndRolestabHeaderText);
			if (!isTextMatching) {
				logger.error("[Users & Roles] tab header text matching failed. Expected text is ["
						+ ExpUsersAndRolestabHeaderText
						+ "] and the return text is ["
						+ UsersAndRolestabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Users & Roles] tab header text matching failed", "",
						ExpUsersAndRolestabHeaderText,
						UsersAndRolestabHeaderText, true);
				m_assert.fail("[Users & Roles] tab header text matching failed. Expected text is ["
						+ ExpUsersAndRolestabHeaderText
						+ "] and the return text is ["
						+ UsersAndRolestabHeaderText + "]");
			}
		}
		// Identify Role table list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatorvalue());
		// Edit the Role
		boolean isRoleEdited = SeleniumCustomUtils
				.click_At_EditLink_At_Roles_In_UsersAndRoles(element,
						testcaseArgs.get("name"));
		SeleniumUtils.sleepThread(3);
		if (!isRoleEdited) {
			logger.error("Unable to edit the [" + testcaseArgs.get("name")
					+ "] role");
			ReportUtils.setStepDescription("Unable to edit the ["
					+ testcaseArgs.get("name") + "] role", true);
			m_assert.fail("Unable to edit the [" + testcaseArgs.get("name")
					+ "] role");
		}
		// Identify the Edit Role header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientEditRoleHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientEditRoleHeaderText")
						.getLocatorvalue());
		// Get the text
		String editRoleHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpeditRoleHeaderText = Suite.objectRepositoryMap.get(
				"ClientEditRoleHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(editRoleHeaderText,
				ExpeditRoleHeaderText);
		if (!isTextMatching) {
			logger.error("[Edit Role] page header text matching failed. Expected text is ["
					+ ExpeditRoleHeaderText
					+ "] and the return text is ["
					+ editRoleHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Edit Role] page header text matching failed", "",
					ExpeditRoleHeaderText, editRoleHeaderText, true);
			m_assert.fail("[Edit Role] page header text matching failed. Expected text is ["
					+ ExpeditRoleHeaderText
					+ "] and the return text is ["
					+ editRoleHeaderText + "]");
		}
		// Identify Role name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatorvalue());
		// Clear the text
		SeleniumUtils.clearText(element);
		// Enter new text
		SeleniumUtils.type(element, testcaseArgs.get("newName"));
		SeleniumUtils.scrollDown();
		// Select the permissions on Alerts, Organizations, CME
		logger.info("Select checkboxes based on the input");
		isClicked = SeleniumCustomUtils.checkCheckboxesAsPerInput(testcaseArgs
				.get("alertsPermissions1"));
		if (!isClicked) {
			logger.error("Unable to click on check box of " + "["
					+ testcaseArgs.get("alertsPermissions1") + "]");
			ReportUtils.setStepDescription("Unable to click on check box of "
					+ "[" + testcaseArgs.get("alertsPermissions1") + "]", true);
			m_assert.fail("Unable to click on check box of " + "["
					+ testcaseArgs.get("alertsPermissions1") + "]");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button");
			ReportUtils.setStepDescription("Unable to identify Save button",
					true);
			m_assert.fail("Unable to identify Save button");
		}
		// click on Save button
		isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		if (!isClicked) {
			logger.error("Unable to click on Save button");
			ReportUtils.setStepDescription("Unable to click on Save button",
					true);
			m_assert.fail("Unable to click on Save button");
		}
		SeleniumUtils.sleepThread(4);
		// Identify Success message message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateRoleSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateRoleSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String NewRoleSuccessMsg = SeleniumUtils.getText(element).substring(1)
				.trim();
		// Get the exp text
		String ExpNewRoleSuccessMsg = Suite.objectRepositoryMap.get(
				"ClientCreateRoleSuccessMSG").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(NewRoleSuccessMsg,
				ExpNewRoleSuccessMsg);
		if (!isTextMatching) {
			logger.error("New Role success message text matching failed. "
					+ "Expected text is [" + ExpNewRoleSuccessMsg
					+ "] and the return text is [" + NewRoleSuccessMsg + "]");
			ReportUtils.setStepDescription(
					"New Role success message text matching failed", "",
					ExpNewRoleSuccessMsg, NewRoleSuccessMsg, true);
			m_assert.fail("New Role success message text matching failed. "
					+ "Expected text is [" + ExpNewRoleSuccessMsg
					+ "] and the return text is [" + NewRoleSuccessMsg + "]");
		}
		// Identify Users & Role tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Validate the just created role
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Role table list");
			ReportUtils.setStepDescription(
					"Unable to identify Role table list", true);
			m_assert.fail("Unable to identify Role table list");
		}
		// Identify just created role in Role table
		boolean isRolePresent = SeleniumCustomUtils.verifyRoleNameInRoleList(
				element, testcaseArgs.get("name"));
		if (!isRolePresent) {
			logger.error("Created Role is not available in Role table list");
			ReportUtils.setStepDescription(
					"Created Role is not available in Role table list", true);
			m_assert.fail("Created Role is not available in Role table list");
		}
		m_assert.assertAll();
	}

	@Test(priority = 16, dependsOnMethods = "loginAs")
	public void deleteRole() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteRole")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [deleteRole] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [deleteRole] is not added for execution", false);
			throw new SkipException(
					"Test case [deleteRole] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("deleteRole");
		logger.info("Starting [deleteRole] execution");
		// Identify Roles table list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatorvalue());
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete link of specific user
			isClicked = SeleniumCustomUtils.deleteRole(element,
					testcaseArgs.get("name"));
			if (!isClicked) {
				logger.error("Click operation fails at Delete link of specific role  "
						+ testcaseArgs.get("username"));
				ReportUtils.setStepDescription(
						"Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("username"), true);
				m_assert.fail("Click operation fails at Delete link of specific role  "
						+ testcaseArgs.get("username"));
			}
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			boolean isClicked = SeleniumCustomUtils
					.delete_Role_for_IE_In_UsersAndRoles(element,
							testcaseArgs.get("name"));
			if (!isClicked) {
				logger.error("Click operation fails at Delete link of specific role  "
						+ testcaseArgs.get("username"));
				ReportUtils.setStepDescription(
						"Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("username"), true);
				m_assert.fail("Click operation fails at Delete link of specific role  "
						+ testcaseArgs.get("username"));
			}
		} else {
			boolean isClicked = SeleniumCustomUtils.deleteRole(element,
					testcaseArgs.get("name"));
			if (!isClicked) {
				logger.error("Click operation fails at Delete link of specific role  "
						+ testcaseArgs.get("username"));
				ReportUtils.setStepDescription(
						"Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("username"), true);
				m_assert.fail("Click operation fails at Delete link of specific role  "
						+ testcaseArgs.get("username"));
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
		}
		SeleniumUtils.sleepThread(5);
		// Identify Error message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientDeleteRoleSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientDeleteRoleSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String delRoleSuccessMsg = SeleniumUtils.getText(element).substring(1)
				.trim();
		// Get the exp text
		String ExpdelRoleSuccessMsg = Suite.objectRepositoryMap.get(
				"ClientDeleteRoleSuccessMSG").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(delRoleSuccessMsg,
				ExpdelRoleSuccessMsg);
		if (!isTextMatching) {
			logger.error("Role deletion message text matching failed. "
					+ "Expected text is [" + ExpdelRoleSuccessMsg
					+ "] and the return text is [" + delRoleSuccessMsg + "]");
			ReportUtils.setStepDescription(
					"Role deletion message text matching failed", "",
					ExpdelRoleSuccessMsg, delRoleSuccessMsg, true);
			m_assert.fail("Role deletion message text matching failed. "
					+ "Expected text is [" + ExpdelRoleSuccessMsg
					+ "] and the return text is [" + delRoleSuccessMsg + "]");
		}
		// Identify Role list table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesRolesListTable")
						.getLocatorvalue());
		// Validate the delete Role is available in the Role table list
		boolean isRolePresent = SeleniumCustomUtils.verifyRoleNameInRoleList(
				element, testcaseArgs.get("name"));
		if (isRolePresent) {
			logger.error("Deleted Role still available in Role table list");
			ReportUtils.setStepDescription(
					"Deleted Role still available in Role table list", true);
			m_assert.fail("Deleted Role still available in Role table list");
		}
		m_assert.assertAll();

	}

	@Test(priority = 17, dependsOnMethods = "loginAs")
	public void verifyLowLevelUserAccessPermissions() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("verifyLowLevelUserAccessPermissions")) {
				forExecution = true;
				break;
			}
		}
		// If not added then skip the test case
		if (!forExecution) {
			logger.warn("Test case [verifyLowLevelUserAccessPermissions] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyLowLevelUserAccessPermissions] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyLowLevelUserAccessPermissions] is not added for execution");
		}
		// Read the data
		testcaseArgs = getTestData("verifyLowLevelUserAccessPermissions");
		logger.info("Starting [verifyLowLevelUserAccessPermissions] execution");
		// Identify Users & Roles tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		// Click on Users & Roles tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify New Role button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewRole")
						.getLocatorvalue());
		// Click on New Role
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		logger.info("creating new role-- Restriction on Alerts & Notification"
				+ " and Organization checkboxes");
		// Identify Role name text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewRoleInputTextBox")
						.getLocatorvalue());
		// Enter Role
		SeleniumUtils.type(element, testcaseArgs.get("RestrictedRoleName"));
		isClicked = SeleniumCustomUtils.checkCheckboxesAsPerInput(testcaseArgs
				.get("orgPermissions1"));
		if (!isClicked) {
			logger.error("Unable to click on check box of " + "["
					+ testcaseArgs.get("orgPermissions1") + "]");
			ReportUtils.setStepDescription("Unable to click on check box of "
					+ "[" + testcaseArgs.get("orgPermissions1") + "]", true);
			m_assert.fail("Unable to click on check box of " + "["
					+ testcaseArgs.get("orgPermissions1") + "]");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewRoleBtnSave")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		// Identify Users & Roles tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
						.getLocatorvalue());
		// Click on Users & Roles tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Creating new user-- with restricted role");
		// Identify New User button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientUserRolesBtnNewUser")
						.getLocatorvalue());
		// Click on New User
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// First Name text box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserFNTextBox")
						.getLocatorvalue());
		// Enter first name
		SeleniumUtils.type(element, testcaseArgs.get("fn1"));
		// Last Name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserLNTextBox")
						.getLocatorvalue());
		// Enter last name
		SeleniumUtils.type(element, testcaseArgs.get("ln1"));
		// Email
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserEmailTextBox")
						.getLocatorvalue());
		// Enter Email
		SeleniumUtils.type(element, testcaseArgs.get("email1"));
		// Password
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientCreateNewUserPasswordTextBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientCreateNewUserPasswordTextBox")
								.getLocatorvalue());
		SeleniumUtils.type(element, testcaseArgs.get("password1"));
		// Select Restricted role value from Role dropdown
		SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserRoleDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientCreateNewUserRoleDropdown")
						.getLocatorvalue(), testcaseArgs.get("userDropdown"));
		SeleniumUtils.scrollDown();
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientCreateNewUserBtnSave")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Validate the success message");
		// Identify Success Message or Email Already taken error message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"SaveUserMsgOrEmailAlreadyTakenMsg").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SaveUserMsgOrEmailAlreadyTakenMsg").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify either Successful message or "
					+ "Error message for new User creation");
			ReportUtils.setStepDescription(
					"Unable to identify either Successful message or "
							+ "Error message for new User creation", true);
			m_assert.fail("Unable to identify either Successful message or "
					+ "Error message for new User creation");
		}
		// Get the text
		String Message = SeleniumUtils.getText(element).substring(1).trim();
		// Get the exp error message text
		String ErrorMessage = Suite.objectRepositoryMap.get(
				"ClientEmailAlreadyTakenMSG").getExptext();
		// First compare the return text with error text
		if (!Message.equalsIgnoreCase(ErrorMessage)) {
			String SuccessMessage = Suite.objectRepositoryMap.get(
					"ClientCreateNewUserSuccessMSG").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(Message, SuccessMessage);
			if (!isTextMatching) {
				logger.error("New User success message text matching failed. "
						+ "The Expected text is [" + SuccessMessage
						+ "] and the return text is [" + Message + "]");
				ReportUtils.setStepDescription(
						"New User success message text matching failed", "",
						SuccessMessage, Message, true);
				m_assert.fail("New User success message text matching failed. "
						+ "The Expected text is [" + SuccessMessage
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
			logger.info("Sign out and Sign in with the new restricted user");
			// Identify Sign Out button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTabSignOut")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabSignOut")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
			// verify if the user is on login page
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("LoginPageText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LoginPageText")
							.getLocatorvalue());
			// Get text
			String loginText = SeleniumUtils.getText(element);
			// Get exp text
			String ExploginText = Suite.objectRepositoryMap
					.get("LoginPageText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(loginText, ExploginText);
			if (!isTextMatching) {
				logger.error("Sign Out is unsuccessful. User is not in Login Page");
				ReportUtils.setStepDescription(
						"Sign Out is unsuccessful. User is not in Login Page",
						true);
				m_assert.fail("Sign Out is unsuccessful. User is not in Login Page");
			}
			// Identify username element
			WebElement userelement = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("UserNameTextbox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("UserNameTextbox")
							.getLocatorvalue());
			// Identify password element
			WebElement passwordelement = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("PasswordTextbox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("PasswordTextbox")
							.getLocatorvalue());
			// Identify Sign in button
			WebElement buttonelement = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("SigninButton")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("SigninButton")
							.getLocatorvalue());
			// Login into application
			SeleniumUtils.login(userelement, passwordelement, buttonelement,
					testcaseArgs.get("loginUserName1"),
					testcaseArgs.get("loginPassword1"));
			// Identify Your Account page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap
							.get("ClientYourAccountHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ClientYourAccountHeaderText")
							.getLocatorvalue());
			// Get the landing page header text
			String LandingPageYourAccountText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpLandingPageYourAccountText = Suite.objectRepositoryMap
					.get("ClientYourAccountHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(
					LandingPageYourAccountText, ExpLandingPageYourAccountText);
			if (!isTextMatching) {
				logger.error("Login unsuccessful: User is not landed on Your Accout page");
				ReportUtils
						.setStepDescription(
								"Login unsuccessful: User is not landed on Your Accout page",
								true);
				m_assert.fail("Login unsuccessful: User is not landed on Your Accout page");
			}
			// verify applications list in Analytics tab
			logger.info("Validate User privileges in Analytics page");
			// Identify Analytics tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AnalyticsTablogoText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AnalyticsTablogoText")
							.getLocatorvalue());
			// Click on Analytics tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			// Identify Application dropdown in Analytics tab
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AnalyticsOverviewAppDropdown").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AnalyticsOverviewAppDropdown").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Application selection dropdown"
						+ " in Analytics - Overview page");
				ReportUtils.setStepDescription(
						"Unable to identify Application selection dropdown"
								+ " in Analytics - Overview page", true);
				m_assert.fail("Unable to identify Application selection dropdown"
						+ " in Analytics - Overview page");
			}
			// Get the number of items available in application droopdown
			int numberOfValues = SeleniumUtils
					.getCountOfDropDownValues(element);
			if (numberOfValues == 0 || numberOfValues > 1) {
				logger.error("Restricted Low level User is having Privileges to select "
						+ "applications from application dropdowin in Analytics - Overview page ");
				ReportUtils
						.setStepDescription(
								"Restricted Low level User is having Privileges to select "
										+ "applications from application dropdowin in Analytics - Overview page ",
								true);
				m_assert.fail("Restricted Low level User is having Privileges to select "
						+ "applications from application dropdowin in Analytics - Overview page ");
			}
			logger.info("Validate User privilege in Alerts & Notification page");
			String currentPageURL = SeleniumUtils.getURL();
			// Identify Alerts & Notifications tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AlertsTablogoText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AlertsTablogoText")
							.getLocatorvalue());
			// click on Alerts & Notification tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Get the title of the page
			String title = SeleniumUtils.getTitle();
			if (!title
					.equalsIgnoreCase("Access denied. (500 Internal Server Error)")) {
				logger.error("Restricted Low level User is having Privileges to access "
						+ "Alerts & Notifications page");
				ReportUtils.setStepDescription(
						"Restricted Low level User is having Privileges to access "
								+ "Alerts & Notifications page", true);
				m_assert.fail("Restricted Low level User is having Privileges to access "
						+ "Alerts & Notifications page");
			}
			if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
				SeleniumUtils.navigateToUrl(currentPageURL);
				SeleniumUtils.sleepThread(2);
			} else {
				// Navigate to back wards
				SeleniumUtils.navigateToBackWard();
			}
			SeleniumUtils.sleepThread(2);
			// Idetnify Accounts tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("ClientTablogo")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTablogo")
							.getLocatorvalue());
			// Click on Accounts tab
			SeleniumUtils.clickOnElement(element);
			// Identify Sign Out button
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("SignoutButton")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("SignoutButton")
							.getLocatorvalue());
			// Click on Sign Out button
			SeleniumUtils.clickOnElement(element);
			// Identify Username
			userelement = SeleniumUtils.findobject(Suite.objectRepositoryMap
					.get("UserNameTextbox").getLocatortype(),
					Suite.objectRepositoryMap.get("UserNameTextbox")
							.getLocatorvalue());
			// Identify password
			passwordelement = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("PasswordTextbox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("PasswordTextbox")
							.getLocatorvalue());
			// Identify Sign in
			buttonelement = SeleniumUtils.findobject(Suite.objectRepositoryMap
					.get("SigninButton").getLocatortype(),
					Suite.objectRepositoryMap.get("SigninButton")
							.getLocatorvalue());
			// Login into application
			SeleniumUtils.login(userelement, passwordelement, buttonelement,
					testcaseArgs.get("loginUserNamevalid"),
					testcaseArgs.get("loginPasswordvalid"));
			// Wait for page to load
			SeleniumUtils.sleepThread(3);
			// Identify Initial Page header text
			logger.info("Identification of Initial Page Header element");
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			// If Initial page header element is null then throw the error and
			// exit
			if (element == null) {
				logger.error("Unable to identify [Initial Page] Header Element");
				ReportUtils.setStepDescription(
						"Unable to identify [Initial Page] Header Element",
						true);
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
				logger.info("User is landed on [" + InitialHeaderText
						+ "] page");
				logger.info("Navigate to [" + ExpectedInitialHeaderText
						+ "] page");
				logger.info("Verify if User has Switch button ");
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
							"Switch button Organization textbox is not present."
									+ " So unable to move to ["
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
				SeleniumUtils.sleepThread(2);
				// Verify the landing Page
				logger.info("Verify if user is landed on [Your Applications] page");
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatortype(),
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatorvalue());
				if (element == null) {
					logger.error("Login failed : Unabel to identify "
							+ "[Your Applications] header element");
					ReportUtils.setStepDescription(
							"Login failed : Unabel to identify "
									+ "[Your Applications] header element",
							true);
					m_assert.fail("Login failed : Unabel to identify "
							+ "[Your Applications] header element");
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
					logger.error("Login failed. User is not landed on "
							+ "[Your Applications] page");
					ReportUtils.setStepDescription(
							"Login failed. User is not landed on "
									+ "[Your Applications] page", true);
					m_assert.fail("Login failed. User is not landed on "
							+ "[Your Applications] page");
				}
				// Delete created Role and User
				// Identify Users & Roles tab
				// Verify if user is on [Add User] page
				element = SeleniumUtils.findobject(Suite.objectRepositoryMap
						.get("ClientTabUsersAndRoles").getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTabUsersAndRoles")
								.getLocatorvalue());
				// Get the text
				String UsersAndRolestabText = SeleniumUtils.getText(element);
				// Get the exp text
				String ExpUsersAndRolestabText = Suite.objectRepositoryMap.get(
						"ClientTabUsersAndRoles").getExptext();
				isTextMatching = SeleniumUtils.assertEqual(
						UsersAndRolestabText, ExpUsersAndRolestabText);
				if (!isTextMatching) {
					logger.error("[Users & Roles] tab text matching failed."
							+ "Expected text is [" + ExpUsersAndRolestabText
							+ "] and the return text is ["
							+ UsersAndRolestabText + "]");
					ReportUtils
							.setStepDescription(
									"[Users & Roles] tab text matching failed",
									"", ExpUsersAndRolestabText,
									UsersAndRolestabText, true);
					m_assert.fail("[Users & Roles] tab text matching failed."
							+ " Expected text is [" + ExpUsersAndRolestabText
							+ "] and the return text is ["
							+ UsersAndRolestabText + "]");
				}
				// Click on [Users & Roles]
				SeleniumUtils.clickOnElement(element);
				SeleniumUtils.sleepThread(5);
				// Identify Roles table list
				element = SeleniumUtils.findobject(
						Suite.objectRepositoryMap.get(
								"ClientUserRolesRolesListTable")
								.getLocatortype(),
						Suite.objectRepositoryMap.get(
								"ClientUserRolesRolesListTable")
								.getLocatorvalue());
				if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
					SeleniumUtils.acceptAlertWindowInSafariBrowser();
					SeleniumUtils.sleepThread(3);
					// Click on Delete link of specific user
					isClicked = SeleniumCustomUtils.deleteRole(element,
							testcaseArgs.get("RestrictedRoleName"));
					if (!isClicked) {
						logger.error("Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("RestrictedRoleName"));
						ReportUtils.setStepDescription(
								"Click operation fails at Delete link of specific role  "
										+ testcaseArgs
												.get("RestrictedRoleName"),
								true);
						m_assert.fail("Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("RestrictedRoleName"));
					}
				} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
					isClicked = SeleniumCustomUtils
							.delete_Role_for_IE_In_UsersAndRoles(element,
									testcaseArgs.get("RestrictedRoleName"));
					if (!isClicked) {
						logger.error("Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("RestrictedRoleName"));
						ReportUtils.setStepDescription(
								"Click operation fails at Delete link of specific role  "
										+ testcaseArgs
												.get("RestrictedRoleName"),
								true);
						m_assert.fail("Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("RestrictedRoleName"));
					}
				} else {
					isClicked = SeleniumCustomUtils.deleteRole(element,
							testcaseArgs.get("RestrictedRoleName"));
					if (!isClicked) {
						logger.error("Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("RestrictedRoleName"));
						ReportUtils.setStepDescription(
								"Click operation fails at Delete link of specific role  "
										+ testcaseArgs
												.get("RestrictedRoleName"),
								true);
						m_assert.fail("Click operation fails at Delete link of specific role  "
								+ testcaseArgs.get("RestrictedRoleName"));
					}
					SeleniumUtils.sleepThread(2);
					// Click on Ok message of Alert window
					SeleniumUtils.acceptAlertWindow();
				}
				SeleniumUtils.sleepThread(5);
				// Identify Error message
				element = SeleniumUtils
						.waitForElementToIdentify(Suite.objectRepositoryMap
								.get("ClientDeleteRoleSuccessMSG")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientDeleteRoleSuccessMSG")
								.getLocatorvalue());
				// Get the text
				String delRoleSuccessMsg = SeleniumUtils
						.perform_SubString_And_Trim(
								SeleniumUtils.getText(element), 1);
				// Get the exp text
				String ExpdelRoleSuccessMsg = Suite.objectRepositoryMap.get(
						"ClientDeleteRoleSuccessMSG").getExptext();
				isTextMatching = SeleniumUtils.assertEqual(delRoleSuccessMsg,
						ExpdelRoleSuccessMsg);
				if (!isTextMatching) {
					logger.error("Role deletion message text matching failed. "
							+ "Expected text is [" + ExpdelRoleSuccessMsg
							+ "] and the return text is [" + delRoleSuccessMsg
							+ "]");
					ReportUtils.setStepDescription(
							"Role deletion message text matching failed", "",
							ExpdelRoleSuccessMsg, delRoleSuccessMsg, true);
					m_assert.fail("Role deletion message text matching failed. "
							+ "Expected text is ["
							+ ExpdelRoleSuccessMsg
							+ "] and the return text is ["
							+ delRoleSuccessMsg
							+ "]");
				}
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
		m_assert.assertAll();
	}

	@Test(priority = 19, dependsOnMethods = { "loginAs" })
	public void verifyDownloadsLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyDownloadsLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyDownloadsLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyDownloadsLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyDownloadsLayout] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("verifyDownloadsLayout");
		logger.info("Starting [verifyDownloadsLayout] execution");
		// Identify Downloads tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabDownloads")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabDownloads")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Downloads] sub-tab in Accounts tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Downloads] sub-tab in Accounts tab",
					true);
			m_assert.fail("Unable to identify [Downloads] sub-tab in Accounts tab");
		}
		logger.info("Click on Downloads tab");
		// Click on Downloads tab
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [Downloads] sub-tab in Accounts tab");
			ReportUtils.setStepDescription(
					"Unable to click on [Downloads] sub-tab in Accounts tab",
					true);
			m_assert.fail("Unable to click on [Downloads] sub-tab in Accounts tab");
		}
		logger.info("Verify if the Download page is opened");
		SeleniumUtils.sleepThread(3);
		// Identify Header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("ClientTabDownloadsHeaderElement")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("ClientTabDownloadsHeaderElement")
						.getLocatorvalue());
		// Get the text
		String DownloadHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpDownloadHeaderText = Suite.objectRepositoryMap.get(
				"ClientTabDownloadsHeaderElement").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(DownloadHeaderText,
				ExpDownloadHeaderText);
		if (!isTextMatching) {
			logger.error("[Downloads] sub-tab header text matching failed. The Expected text ["
					+ ExpDownloadHeaderText
					+ "] and the return text ["
					+ DownloadHeaderText + "] not equal");
			ReportUtils.setStepDescription(
					"[Downloads] sub-tab header text matching failed", "",
					ExpDownloadHeaderText, DownloadHeaderText, true);
			m_assert.fail("[Downloads] sub-tab header text matching failed. The Expected text ["
					+ ExpDownloadHeaderText
					+ "] and the return text ["
					+ DownloadHeaderText + "] not equal");
		}
		logger.info("Verify  MaaS Core SDK, MaaS Core Documentation elements");
		// Identify MaaS Core element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabDownloadsMaaSCoreArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabDownloadsMaaSCoreArea")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Core elements area");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS Core elements area", true);
			m_assert.fail("Unable to identify MaaS Core elements area");
		}
		// Identify MaaS Analytics element
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientTabDownloadsMaaSAnalyticsArea")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientTabDownloadsMaaSAnalyticsArea")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Analytics elements area");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS Analytics elements area", true);
			m_assert.fail("Unable to identify MaaS Analytics elements area");
		}
		// Identify MaaS Alerts element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSAlertsArea").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSAlertsArea").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Alerts elements area");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS Alerts elements area", true);
			m_assert.fail("Unable to identify MaaS Alerts elements area");
		}
		// Identify MaaS CMS element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabDownloadsMaaSCMSArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabDownloadsMaaSCMSArea")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS CMS elements area");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS CMS elements area", true);
			m_assert.fail("Unable to identify MaaS CMS elements area");
		}
		// Identify MaaS Location element
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ClientTabDownloadsMaaSLocationArea")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientTabDownloadsMaaSLocationArea")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Location elements area");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS Location elements area", true);
			m_assert.fail("Unable to identify MaaS Location elements area");
		}
		// Identify MaaS Advertising element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSAdvertisingArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSAdvertisingArea")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Advertising elements area");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS Advertising elements area", true);
			m_assert.fail("Unable to identify MaaS Advertising elements area");
		}
		m_assert.assertAll();
	}

	@Test(priority = 20, dependsOnMethods = { "loginAs" })
	public void verifyMaaSCoreSDK() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author for Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMaaSCoreSDK")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyMaaSCoreSDK] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyMaaSCoreSDK] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifyMaaSCoreSDK] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyMaaSCoreSDK");
		logger.info("Starting [verifyMaaSCoreSDK] execution");
		logger.info("Click on MaaS Core iOS - Download link");
		// MaaS Core - iOS
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSCoreIOSDownload")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSCoreIOSDownload")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Core iOS - Download element");
			ReportUtils
					.setStepDescription(
							"Unable to identify MaaS Core iOS - Download element",
							true);
			m_assert.fail("Unable to identify MaaS Core iOS - Download element");
		}
		// Get the url
		String url = SeleniumUtils.getAttributeValue(element, "href");
		// Get the exp text
		String Expurl = Suite.objectRepositoryMap.get(
				"ClientTabDownloadsMaaSCoreIOSDownload").getExptext();
		// Compare both urls
		boolean isURLMatching = SeleniumUtils.assertEqual(url, Expurl);
		if (!isURLMatching) {
			logger.error("[MaaS Core SDK - iOS] Download URL matching failed. The Expected URL ["
					+ Expurl + "] and the return URL [" + url + "] not equal");
			ReportUtils.setStepDescription(
					"[MaaS Core SDK - iOS] Download URL matching failed", "",
					Expurl, url, true);
			m_assert.fail("[MaaS Core SDK - iOS] Download URL matching failed. The Expected URL ["
					+ Expurl + "] and the return URL [" + url + "] not equal");
		}
		// MaaS Core - Android
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSCoreAndroidDownload")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSCoreAndroidDownload")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Core Android - Download element");
			ReportUtils.setStepDescription(
					"Unable to identify MaaS Core Android - Download element",
					true);
			m_assert.fail("Unable to identify MaaS Core Android - Download element");
		}
		// Get the url
		url = SeleniumUtils.getAttributeValue(element, "href");
		// Get the exp text
		Expurl = Suite.objectRepositoryMap.get(
				"ClientTabDownloadsMaaSCoreAndroidDownload").getExptext();
		// Compare both urls
		isURLMatching = SeleniumUtils.assertEqual(url, Expurl);
		if (!isURLMatching) {
			logger.error("[MaaS Core SDK - Android] Download URL matching failed. The Expected URL ["
					+ Expurl + "] and the return URL [" + url + "] not equal");
			ReportUtils.setStepDescription(
					"[MaaS Core SDK - Android] Download URL matching failed",
					"", Expurl, url, true);
			m_assert.fail("[MaaS Core SDK - Android] Download URL matching failed. The Expected URL ["
					+ Expurl + "] and the return URL [" + url + "] not equal");
		}
		// MaaS Core - iOS - Documentation
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSCoreIOSDocumentation")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ClientTabDownloadsMaaSCoreIOSDocumentation")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify MaaS Core iOS - Document element");
			ReportUtils
					.setStepDescription(
							"Unable to identify MaaS Core iOS - Document element",
							true);
			m_assert.fail("Unable to identify MaaS Core iOS - Document element");
		}
		String currentPageURL = SeleniumUtils.getURL();
		// Click
		boolean isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [MaaS Core SDK - iOS] Document link");
			ReportUtils.setStepDescription(
					"Unable to click on [MaaS Core SDK - iOS] Document link",
					true);
			m_assert.fail("Unable to click on [MaaS Core SDK - iOS] Document link");
		}
		SeleniumUtils.sleepThread(5);
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			// Navigate to previous page
			SeleniumUtils.navigateToUrl(currentPageURL);
			SeleniumUtils.sleepThread(2);
		} else {
			// Navigate to backward
			SeleniumUtils.navigateToBackWard();
		}
		SeleniumUtils.sleepThread(2);
		m_assert.assertAll();
	}

	@Test(priority = 21, dependsOnMethods = { "loginAs" })
	public void verifyS3Layout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyS3Layout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyS3Layout] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyS3Layout] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifyS3Layout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyS3Layout");
		logger.info("Starting [verifyS3Layout] execution");
		logger.info("Navigate to [S3] tab");
		// Identify S3 tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentManagementS3Tab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementS3Tab")
						.getLocatorvalue());
		// Get the text
		String S3SubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(S3SubTabText,
				Suite.objectRepositoryMap.get("ContentManagementS3Tab")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[S3] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get("ContentManagementS3Tab")
							.getExptext()
					+ "] and the actual return text is ["
					+ S3SubTabText + "]");
			ReportUtils.setStepDescription("[S3] sub tab text matching failed",
					"", Suite.objectRepositoryMap.get("ContentManagementS3Tab")
							.getExptext(), S3SubTabText, true);
			m_assert.fail("[S3] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get("ContentManagementS3Tab")
							.getExptext()
					+ "] and the actual return text is ["
					+ S3SubTabText + "]");
		}
		logger.info("Verification of [S3] tab text is successful");
		// Click on S3 tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if user is on [S3] tab");
		// Identify S3 tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementS3TabHeaderText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementS3TabHeaderText").getLocatorvalue());
		// Get the text
		String S3SubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				S3SubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementS3TabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Structure] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementS3TabHeaderText").getExptext()
					+ "] and the actual return text is ["
					+ S3SubTabHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"[Structure] sub tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementS3TabHeaderText").getExptext(),
					S3SubTabHeaderText, true);
			m_assert.fail("[Structure] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementS3TabHeaderText").getExptext()
					+ "] and the actual return text is ["
					+ S3SubTabHeaderText
					+ "]");
		}
		m_assert.assertAll();

	}

	@Test(priority = 22, dependsOnMethods = { "loginAs" })
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
		if (clientSuite != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			testcases = clientSuite.getCase();
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
