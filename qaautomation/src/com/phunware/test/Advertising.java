package com.phunware.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class Advertising extends Suite {
	private static Logger logger = Logger.getLogger(PhunwareClients.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static boolean isTextMatching;
	private static WebElement element = null;
	private static String childSuite = "advertising";
	private static boolean suiteExecution = false;
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private SoftAssert m_assert;
	private static boolean isClicked;
	private Testcase advertising = null;
	private List<Case> testcases = null;

	/**
	 * This method reads the input xml file and add the methods in the test case
	 * list
	 */
	@BeforeClass
	public void setUp() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Index Page Description for Results
		ReportUtils.setIndexPageDescription();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if [Advertising] test suite is added for execution
		for (String testSuite : scenarioslist) {
			if (childSuite.equalsIgnoreCase(testSuite)) {
				suiteExecution = true;
				break;
			}
		}
		// if suiteExecution is false then skip the Advertising test suite
		if (!suiteExecution) {
			logger.info("Test suite [Advertising] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [Advertising] is not added for execution",
					false);
			throw new SkipException(
					"Test suite [Advertising] is not added for execution");
		}
		logger.info("reading [Advertising] Input file");
		// reading Advertising input file
		advertising = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.ADVERTISING_FILE, Testcase.class);
		if (advertising != null) {
			// Add the test cases into testcaseList
			testcases = advertising.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());
				}
			}
		}
		// If testcaseList size is zero skip the execution
		if (testcaseList.size() == 0) {
			logger.warn("No testCases added for execution in [Advertising] "
					+ "test suite");
			ReportUtils.setStepDescription(
					"No testCases added for execution in [Advertising] "
							+ "test suite", false);
			throw new SkipException(
					"No testCases added for execution in [Advertising] "
							+ "test suite");
		}
		logger.info("reading [Advertising] Input file successful");
		logger.info(" {" + testcaseList
				+ "} for execution in [Advertising] suite");
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
			logger.warn("Test case [loginAs] is not added for execution"
					+ " in [Advertising]");
			ReportUtils.setStepDescription(
					"Test case [loginAs] is not added for execution"
							+ " in [Advertising]", false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution"
							+ " in [Advertising]");
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

	@Test(priority = 1, dependsOnMethods = "loginAs")
	public void verifyAdvertisingInitialLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyAdvertisingInitialLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyAdvertisingInitialLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyAdvertisingInitialLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyAdvertisingInitialLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyAdvertisingInitialLayout] is not added for execution");
		}
		logger.info("Starting [verifyAdvertisingInitialLayout] execution");
		logger.info("Verify if [ADVERTISING] tab is present");
		// Identify Advertising tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdvertisingTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdvertisingTab")
						.getLocatorvalue());
		// If Advertising tab element is null then throw the
		if (element == null) {
			logger.error("Unable to identify [ADVERTISING] tab element");
			ReportUtils.setStepDescription(
					"Unable to identify [ADVERTISING] tab element", true);
			m_assert.fail("Unable to identify [ADVERTISING] tab element");
		}
		// Get the text of the Advertising tab
		String AdvertisingTabText = SeleniumUtils.getText(element);
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(AdvertisingTabText,
				Suite.objectRepositoryMap.get("AdvertisingTab").getExptext());
		if (!isTextMatching) {
			logger.error("[ADVERTISING] tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdvertisingTab")
							.getExptext()
					+ "] and the return text is ["
					+ AdvertisingTabText + "]");
			ReportUtils.setStepDescription(
					"[ADVERTISING] tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdvertisingTab")
							.getExptext(), AdvertisingTabText, true);
			m_assert.fail("[ADVERTISING] tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdvertisingTab")
							.getExptext()
					+ "] and the return text is ["
					+ AdvertisingTabText + "]");
		}
		logger.info("[ADVERTISING] tab is present");
		logger.info("Navigate to [ADVERTISING] tab");
		// Click on Advertising tab
		SeleniumUtils.clickOnElement(element);
		logger.info("Navigation to [ADVERTISING] tab is successful");
		logger.info("Verify all the sub tabs present in [ADVERTISING] tab");
		SeleniumUtils.sleepThread(6);
		// Identify Dashboard tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdDashboardTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabText")
						.getLocatorvalue());
		// Get the text of the Dashboard tab
		String DashboardSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(DashboardSubTabText,
				Suite.objectRepositoryMap.get("AdDashboardTabText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Dashboard] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getExptext()
					+ "] and the return text is ["
					+ DashboardSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Dashboard] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getExptext(), DashboardSubTabText, true);
			m_assert.fail("[Dashboard] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getExptext()
					+ "] and the return text is ["
					+ DashboardSubTabText + "]");
		}
		// Identify Sites tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdSitesTab").getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTab").getLocatorvalue());
		String SitesSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabText,
				Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
		if (!isTextMatching) {
			logger.error("[Sites] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdSitesTab").getExptext()
					+ "] and the return text is [" + SitesSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Sites] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext(),
					SitesSubTabText, true);
			m_assert.fail("[Sites] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdSitesTab").getExptext()
					+ "] and the return text is [" + SitesSubTabText + "]");
		}
		// verify Reports tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("AdReportsTab")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("AdReportsTab")
								.getLocatorvalue());
		String ReportsSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(ReportsSubTabText,
				Suite.objectRepositoryMap.get("AdReportsTab").getExptext());
		if (!isTextMatching) {
			logger.error("[Reports] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdReportsTab")
							.getExptext()
					+ "] and the return text is ["
					+ ReportsSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Reports] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdReportsTab").getExptext(),
					ReportsSubTabText, true);
			m_assert.fail("[Reports] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdReportsTab")
							.getExptext()
					+ "] and the return text is ["
					+ ReportsSubTabText + "]");
		}
		// Identify Campaign Builder tab element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatorvalue());
		// Get the text of the Campaign Builder tab
		String CampaignBuilderSubTabText = SeleniumUtils.getText(element);
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(CampaignBuilderSubTabText,
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getExptext());
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Campaign Builder] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the return text is ["
					+ CampaignBuilderSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext(), CampaignBuilderSubTabText, true);
			m_assert.fail("[Campaign Builder] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the return text is ["
					+ CampaignBuilderSubTabText + "]");
		}
		// Verify Campaigns tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignsTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabText")
						.getLocatorvalue());
		String CampaignsSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(CampaignsSubTabText,
				Suite.objectRepositoryMap.get("AdCampaignsTabText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Campaigns] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext()
					+ "] and the return text is ["
					+ CampaignsSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Campaigns] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext(), CampaignsSubTabText, true);
			m_assert.fail("[Campaigns] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext()
					+ "] and the return text is ["
					+ CampaignsSubTabText + "]");
		}
		// verify Payments tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("AdPaymentsTab")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getLocatorvalue());
		String PaymentsSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(PaymentsSubTabText,
				Suite.objectRepositoryMap.get("AdPaymentsTab").getExptext());
		if (!isTextMatching) {
			logger.error("[Payments] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getExptext()
					+ "] and the return text is ["
					+ PaymentsSubTabText + "]");
			ReportUtils
					.setStepDescription(
							"[Payments] sub tab text matching failed", "",
							Suite.objectRepositoryMap.get("AdPaymentsTab")
									.getExptext(), PaymentsSubTabText, true);
			m_assert.fail("[Payments] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getExptext()
					+ "] and the return text is ["
					+ PaymentsSubTabText + "]");
		}
		// verify Account tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("AdAccountTab")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("AdAccountTab")
								.getLocatorvalue());
		String AccountSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(AccountSubTabText,
				Suite.objectRepositoryMap.get("AdAccountTab").getExptext());
		if (!isTextMatching) {
			logger.error("[Account] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdAccountTab")
							.getExptext()
					+ "] and the return text is ["
					+ AccountSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Account] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdAccountTab").getExptext(),
					AccountSubTabText, true);
			m_assert.fail("[Account] sub tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdAccountTab")
							.getExptext()
					+ "] and the return text is ["
					+ AccountSubTabText + "]");
		}
		logger.info("Verification of all sub tabs in [ADVERTISING] tab are successful");
		logger.info("Verify if user is on [Dashboard] tab");
		// Identify Dashboard tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getLocatorvalue());
		String DashboardSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(DashboardSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Dashboard] sub tab header text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
							.getExptext()
					+ "] and the return text is ["
					+ DashboardSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Dashboard] sub tab header text matching failed", "",
					Suite.objectRepositoryMap.get("AdAccountTab").getExptext(),
					DashboardSubTabHeaderText, true);
			m_assert.fail("[Dashboard] sub tab header text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
							.getExptext()
					+ "] and the return text is ["
					+ DashboardSubTabHeaderText + "]");
		}
		logger.info("User is on [Dashboard] tab");
		m_assert.assertAll();
	}

	@Test(priority = 2, dependsOnMethods = "loginAs")
	public void verifyAdvertisingDashboardLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyAdvertisingDashboardLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyAdvertisingDashboardLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyAdvertisingDashboardLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyAdvertisingDashboardLayout] is not added for execution");
		}
		logger.info("Starting [verifyAdvertisingDashboardLayout] execution");
		logger.info("Verify if user is on [Dashboard] tab");
		// Identify Dashboard header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getLocatorvalue());
		// Get the text of Dashboard header element
		String DashboardSubTabHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpDashboardSubTabHeaderText = Suite.objectRepositoryMap.get(
				"AdDashboardTabHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(DashboardSubTabHeaderText,
				ExpDashboardSubTabHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Publisher Dashboard] page");
			logger.info("Navigate to [Dashboard] tab");
			// Identify Dashboard tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getLocatorvalue());
			// Get the text of the Dashboard tab
			String DashboardSubTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpDashboardSubTabText = Suite.objectRepositoryMap.get(
					"AdDashboardTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(DashboardSubTabText,
					ExpDashboardSubTabText);
			if (!isTextMatching) {
				logger.error("[Dashboard] sub tab text matching failed. The Expected text is ["
						+ ExpDashboardSubTabText
						+ "] and the return text is ["
						+ DashboardSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Dashboard] sub tab text matching failed", "",
						ExpDashboardSubTabText, DashboardSubTabText, true);
				m_assert.fail("[Dashboard] sub tab text matching failed. The Expected text is ["
						+ ExpDashboardSubTabText
						+ "] and the return text is ["
						+ DashboardSubTabText + "]");
			}
			// Click on Dashboard tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Dashboard header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
							.getLocatorvalue());
			// Get the text of Dashboard header element
			DashboardSubTabHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpDashboardSubTabHeaderText = Suite.objectRepositoryMap.get(
					"AdDashboardTabHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(
					DashboardSubTabHeaderText, ExpDashboardSubTabHeaderText);
			if (!isTextMatching) {
				logger.error("[Publisher Dashboard] page header text matching failed. The Expected text is ["
						+ ExpDashboardSubTabHeaderText
						+ "] and the return text is ["
						+ DashboardSubTabHeaderText + "]");
				ReportUtils
						.setStepDescription(
								"[Publisher Dashboard] page header text matching failed",
								"", ExpDashboardSubTabHeaderText,
								DashboardSubTabHeaderText, true);
				m_assert.fail("[Publisher Dashboard] page header text matching failed. The Expected text is ["
						+ ExpDashboardSubTabHeaderText
						+ "] and the return text is ["
						+ DashboardSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Publisher Dashboard] page");
		logger.info("Verify Application & time dropdowns in [Dashboard] tab");
		// Identify Application dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabAppDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Application' dropdown in [Publisher Dashboard] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Application' dropdown in [Publisher Dashboard] page",
							true);
			m_assert.fail("Unable to identify 'Application' dropdown in [Publisher Dashboard] page");
		}
		// Identify Time dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabTimeDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabTimeDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Time' dropdown in [Publisher Dashboard] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Time' dropdown in [Publisher Dashboard] page",
							true);
			m_assert.fail("Unable to identify 'Time' dropdown in [Publisher Dashboard] page");
		}
		m_assert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods = "loginAs")
	public void verifyDashboardData() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyDashboardData")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyDashboardData] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyDashboardData] is not added for execution",
							true);
			throw new SkipException(
					"Test case [verifyDashboardData] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("verifyDashboardData");
		logger.info("Starting [verifyDashboardData] execution");
		logger.info("Verify if user is on [Dashboard] tab");
		logger.info("Verify if user is on [Dashboard] tab");
		// Identify Dashboard header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
						.getLocatorvalue());
		// Get the text of Dashboard header element
		String DashboardSubTabHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpDashboardSubTabHeaderText = Suite.objectRepositoryMap.get(
				"AdDashboardTabHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(DashboardSubTabHeaderText,
				ExpDashboardSubTabHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Publisher Dashboard] page");
			logger.info("Navigate to [Dashboard] tab");
			// Identify Dashboard tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdDashboardTabText")
							.getLocatorvalue());
			// Get the text of the Dashboard tab
			String DashboardSubTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpDashboardSubTabText = Suite.objectRepositoryMap.get(
					"AdDashboardTabText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(DashboardSubTabText,
					ExpDashboardSubTabText);
			if (!isTextMatching) {
				logger.error("[Dashboard] sub tab text matching failed. The Expected text is ["
						+ ExpDashboardSubTabText
						+ "] and the return text is ["
						+ DashboardSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Dashboard] sub tab text matching failed", "",
						ExpDashboardSubTabText, DashboardSubTabText, true);
				m_assert.fail("[Dashboard] sub tab text matching failed. The Expected text is ["
						+ ExpDashboardSubTabText
						+ "] and the return text is ["
						+ DashboardSubTabText + "]");
			}
			// Click on Dashboard tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Dashboard header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdDashboardTabHeaderText")
							.getLocatorvalue());
			// Get the text of Dashboard header element
			DashboardSubTabHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpDashboardSubTabHeaderText = Suite.objectRepositoryMap.get(
					"AdDashboardTabHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(
					DashboardSubTabHeaderText, ExpDashboardSubTabHeaderText);
			if (!isTextMatching) {
				logger.error("[Publisher Dashboard] page header text matching failed. The Expected text is ["
						+ ExpDashboardSubTabHeaderText
						+ "] and the return text is ["
						+ DashboardSubTabHeaderText + "]");
				ReportUtils
						.setStepDescription(
								"[Publisher Dashboard] page header text matching failed",
								"", ExpDashboardSubTabHeaderText,
								DashboardSubTabHeaderText, true);
				m_assert.fail("[Publisher Dashboard] page header text matching failed. The Expected text is ["
						+ ExpDashboardSubTabHeaderText
						+ "] and the return text is ["
						+ DashboardSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Publisher Dashboard] page");
		logger.info("Verify Application & time dropdowns in [Dashboard] tab");
		// Select specific Application from application drop down
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("AdDashboardTabAppDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabAppDropdown")
						.getLocatorvalue(), testcaseArgs.get("application"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("application")
					+ "] from the application dropdown in [Publisher Dashboard] tab");
			ReportUtils
					.setStepDescription(
							"Unable to select ["
									+ testcaseArgs.get("application")
									+ "] from the application dropdown in [Publisher Dashboard] tab",
							true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("application")
					+ "] from the application dropdown in [Publisher Dashboard] tab");
		}
		SeleniumUtils.sleepThread(3);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdDashboardTabTimeDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabTimeDropdown")
						.getLocatorvalue());
		// Select Time from the drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("AdDashboardTabTimeDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabTimeDropdown")
						.getLocatorvalue(), testcaseArgs.get("time"));
		if (!isSelected) {
			logger.error("Unable to select the time ["
					+ testcaseArgs.get("time")
					+ "] from the time dropdown in [Publisher Dashboard] tab");
			ReportUtils.setStepDescription("Unable to select the time ["
					+ testcaseArgs.get("time")
					+ "] from the time dropdown in [Publisher Dashboard] tab",
					true);
			m_assert.fail("Unable to select the time ["
					+ testcaseArgs.get("time")
					+ "] from the time dropdown in [Publisher Dashboard] tab");
		}
		SeleniumUtils.sleepThread(3);
		logger.info("Validate retrieved data for application ["
				+ testcaseArgs.get("application") + "] and time ["
				+ testcaseArgs.get("time") + "]");
		// Identify Impressions field & value
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"AdDashboardTabImpressionsFieldText")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdDashboardTabImpressionsFieldText")
								.getLocatorvalue());
		// Get the Impressions field text
		String ImpressionsText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpImpressionsText = Suite.objectRepositoryMap.get(
				"AdDashboardTabImpressionsFieldText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(ImpressionsText,
				ExpImpressionsText);
		if (!isTextMatching) {
			logger.error("[Publisher Dashboard - Impressions] field text matching failed."
					+ " The expected text is ["
					+ ExpImpressionsText
					+ "] and the return text is [" + ImpressionsText + "]");
			ReportUtils
					.setStepDescription(
							"[Publisher Dashboard - Impressions] field text matching failed",
							"", ExpImpressionsText, ImpressionsText, true);
			m_assert.fail("[Publisher Dashboard - Impressions] field text matching failed."
					+ " The expected text is ["
					+ ExpImpressionsText
					+ "] and the return text is [" + ImpressionsText + "]");
		}
		// Impressions field data
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdDashboardTabImpressionsFieldData")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdDashboardTabImpressionsFieldData")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Impressions' data in [Publisher Dashboard] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Impressions' data in [Publisher Dashboard] page",
							true);
			m_assert.fail("Unable to identify 'Impressions' data in [Publisher Dashboard] page");
		}
		// clicks
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabClicksFieldText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabClicksFieldText")
						.getLocatorvalue());
		// Get the text
		String ClicksText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpClicksText = Suite.objectRepositoryMap.get(
				"AdDashboardTabClicksFieldText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(ClicksText, ExpClicksText);
		if (!isTextMatching) {
			logger.error("[Publisher Dashboard - Clicks] field text matching failed."
					+ " The expected text is ["
					+ ExpClicksText
					+ "] and the return text is [" + ClicksText + "]");
			ReportUtils
					.setStepDescription(
							"[Publisher Dashboard - Clicks] field text matching failed",
							"", ExpClicksText, ClicksText, true);

			m_assert.fail("[Publisher Dashboard - Clicks] field text matching failed."
					+ " The expected text is ["
					+ ExpClicksText
					+ "] and the return text is [" + ClicksText + "]");
		}
		// Clicks field data
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabClicksFieldData")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabClicksFieldData")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Clicks' data in [Publisher Dashboard] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Clicks' data in [Publisher Dashboard] page",
							true);
			m_assert.fail("Unable to identify 'Clicks' data in [Publisher Dashboard] page");
		}
		// Fill Rate
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdDashboardTabFillRatesFieldText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdDashboardTabFillRatesFieldText").getLocatorvalue());
		// Get the text
		String FillRateText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpFillRateText = Suite.objectRepositoryMap.get(
				"AdDashboardTabFillRatesFieldText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(FillRateText,
				ExpFillRateText);
		if (!isTextMatching) {
			logger.error("[Publisher Dashboard - Fill Rates] field text matching failed."
					+ " The expected text is ["
					+ ExpFillRateText
					+ "] and the return text is [" + FillRateText + "]");
			ReportUtils
					.setStepDescription(
							"[Publisher Dashboard - Fill Rates] field text matching failed",
							"", ExpFillRateText, FillRateText, true);
			m_assert.fail("[Publisher Dashboard - Fill Rates] field text matching failed."
					+ " The expected text is ["
					+ ExpFillRateText
					+ "] and the return text is [" + FillRateText + "]");
		}
		// Fill Rates data
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdDashboardTabFillRatesFieldData").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdDashboardTabFillRatesFieldData").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Fill Rate' data in [Publisher Dashboard] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Fill Rate' data in [Publisher Dashboard] page",
							true);
			m_assert.fail("Unable to identify 'Fill Rate' data in [Publisher Dashboard] page");
		}
		// eCPM
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabeCPMFieldText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabeCPMFieldText")
						.getLocatorvalue());
		// Get the text
		String eCPMText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpeCPMText = Suite.objectRepositoryMap.get(
				"AdDashboardTabeCPMFieldText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(eCPMText, ExpeCPMText);
		if (!isTextMatching) {
			logger.error("[Publisher Dashboard - eCPM] field text matching failed."
					+ " The expected text is ["
					+ ExpeCPMText
					+ "] and the return text is [" + eCPMText + "]");
			ReportUtils.setStepDescription(
					"[Publisher Dashboard - eCPM] field text matching failed",
					"", ExpeCPMText, eCPMText, true);
			m_assert.fail("[Publisher Dashboard - eCPM] field text matching failed."
					+ " The expected text is ["
					+ ExpeCPMText
					+ "] and the return text is [" + eCPMText + "]");
		}
		// eCPM data
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdDashboardTabeCPMFieldData")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdDashboardTabeCPMFieldData")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'eCPM' data in [Publisher Dashboard] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'eCPM' data in [Publisher Dashboard] page",
							true);
			m_assert.fail("Unable to identify 'eCPM' data in [Dashboard] page");
		}
		// Earnings
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdDashboardTabEarningsFieldText")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdDashboardTabEarningsFieldText")
						.getLocatorvalue());
		// Get the text
		String EarningsText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpEarningsText = Suite.objectRepositoryMap.get(
				"AdDashboardTabEarningsFieldText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(EarningsText,
				ExpEarningsText);
		if (!isTextMatching) {
			logger.error("[Publisher Dashboard - Earnings] field text matching failed."
					+ " The expected text is ["
					+ ExpEarningsText
					+ "] and the return text is [" + EarningsText + "]");
			ReportUtils
					.setStepDescription(
							"[Publisher Dashboard - Earnings] field text matching failed",
							"", ExpEarningsText, EarningsText, true);
			m_assert.fail("[Publisher Dashboard - Earnings] field text matching failed."
					+ " The expected text is ["
					+ ExpEarningsText
					+ "] and the return text is [" + EarningsText + "]");
		}
		// Earnings data
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdDashboardTabeEarningsFieldData").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdDashboardTabeEarningsFieldData").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Earnings' data in [Publisher Dashboard] sub tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Earnings' data in [Publisher Dashboard] sub tab",
							true);
			m_assert.fail("Unable to identify 'Earnings' data in [Publisher Dashboard] sub tab");
		}
		m_assert.assertAll();

	}

	@Test(priority = 4, dependsOnMethods = "loginAs")
	public void verifySitesLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifySitesLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifySitesLayout] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifySitesLayout] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifySitesLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifySitesLayout");
		logger.info("Starting [verifySitesLayout] execution");
		logger.info("Verify if user is on [Sites] tab");
		// Identify header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpSitesSubTabHeaderText = Suite.objectRepositoryMap.get(
				"AdSitesTabHeaderText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				ExpSitesSubTabHeaderText);
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Identify Sites sub-tab
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			// Get the exp text
			String ExpSitesTabText = Suite.objectRepositoryMap
					.get("AdSitesTab").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					ExpSitesTabText);
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. The Expected text is ["
						+ ExpSitesTabText
						+ "] and the return text is ["
						+ SitesTabText + "]");
				ReportUtils.setStepDescription(
						"[Sites] tab text matching failed", "",
						ExpSitesTabText, SitesTabText, true);
				m_assert.fail("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
			}
			// Click on Sites tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Wait For Sites tab to be displayed
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			// Identify Header text
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			// Get the exp text
			ExpSitesSubTabHeaderText = Suite.objectRepositoryMap.get(
					"AdSitesTabHeaderText").getExptext();
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					ExpSitesSubTabHeaderText);
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] tab header text matching failed. The Expected text is ["
						+ ExpSitesSubTabHeaderText
						+ "] and the return text is ["
						+ SitesSubTabHeaderText
						+ "]");
				ReportUtils.setStepDescription(
						"[Sites] tab header text matching failed", "",
						ExpSitesSubTabHeaderText, SitesSubTabHeaderText, true);
				m_assert.fail("[Sites] tab header text matching failed. The Expected text is ["
						+ ExpSitesSubTabHeaderText
						+ "] and the return text is ["
						+ SitesSubTabHeaderText
						+ "]");
			}
		}
		logger.info("User is on [Sites] tab");
		logger.info("Verify 'Configuration', 'Mediation', 'Ad Placements', "
				+ "links are available for the application ["
				+ testcaseArgs.get("application") + "]");
		// Identify Applications list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		// Verify the specific application is available in application list
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Verify Configuration,Ad Placements & View Stats
		boolean isVerified = SeleniumCustomUtils
				.checkApplicationActionsInAdSites(element,
						testcaseArgs.get("application"));
		if (!isVerified) {
			logger.error("Unable to identify application action links for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application action links for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application action links for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		m_assert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = "loginAs")
	public void verifyAdvertisingCampaignBuilderLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// verify if the test is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("verifyAdvertisingCampaignBuilderLayout")) {
				forExecution = true;
				break;
			}
		}
		// If the method is not present then skip the method
		if (!forExecution) {
			logger.info("Testcase [verifyAdvertisingCampaignBuilderLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Testcase [verifyAdvertisingCampaignBuilderLayout] is not added for execution",
							false);
			throw new SkipException(
					"Testcase [verifyAdvertisingCampaignBuilderLayout] is not added for execution");
		}
		logger.info("Starting [verifyAdvertisingCampaignBuilderLayout] execution");
		logger.info("Verify if user is on [Campaign Builder] tab");
		// Identify Campaign Builder tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatorvalue());
		// If Campaign Builder tab is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Campaign Builder] sub-tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Campaign Builder] sub-tab", true);
			m_assert.fail("Unable to identify [Campaign Builder] sub-tab");
		}
		// Get the Campaign Builder tab text
		String CampaignBuilderSubTabText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CampaignBuilderSubTabText,
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error(" [Campaign Builder] sub-tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] sub-tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext(), CampaignBuilderSubTabText, true);
			m_assert.fail("[Campaign Builder] sub-tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabText + "]");
		}
		logger.info("Navigate to [Campaign Builder] tab");
		// Click on Campaign Builder sub tab
		SeleniumUtils.clickOnElement(element);
		// Wait for the page to load
		SeleniumUtils.sleepThread(4);
		// Identify Campaign Builder tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatorvalue());
		// Get the Campaign Builder sub tab header element text
		String CampaignBuilderSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils
				.assertEqual(CampaignBuilderSubTabHeaderText,
						Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error(" [Campaign Builder] page text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] page text matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext(), CampaignBuilderSubTabHeaderText,
					true);
			m_assert.fail("[Campaign Builder] page text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabHeaderText + "]");
		}
		logger.info("User is on [Campaign Builder] page");
		// Identify Configure Your Campaign zone
		logger.info("Identify [Configure Your Campaign] zone");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignZone").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignZone").getLocatorvalue());
		// If Configure Your Campaign element is null then throw the error and
		// exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Configure Your Campaign] zone");
			ReportUtils.setStepDescription(
					"Unable to identify [Configure Your Campaign] zone", true);
			m_assert.fail("Unable to identify [Configure Your Campaign] zone");
		}
		logger.info("Identification of [Configure Your Campaign]zone is successful");
		// Identify Targeting zone
		logger.info("Identify [Targeting] zone");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabTargetingZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabTargetingZone")
						.getLocatorvalue());
		// If Targeting zone element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Targeting] zone");
			ReportUtils.setStepDescription(
					"Unable to identify [Targeting] zone", true);
			m_assert.fail("Unable to identify [Targeting] zone");
		}
		logger.info("Identification of [Targeting] zone is successful");
		// Identify DeliveryOptions zone
		logger.info("Identify of [Delivery OPtions]zone");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDeliveryOptionsZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDeliveryOptionsZone")
						.getLocatorvalue());
		// If Delivery Options zone element is null then throw the error and
		// exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Delivery Options] zone");
			ReportUtils.setStepDescription(
					"Unable to identify [Delivery Options] zone", true);
			m_assert.fail("Unable to identify [Delivery Options] zone");
		}
		logger.info("Identification of [Delivery Options] zone is successful");
		// Identify Creative Builder zone
		logger.info("Identify [Creative Builder] zone");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderZone")
						.getLocatorvalue());
		// If Creative Builder zone element is null then throw the error and
		// exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder] zone");
			ReportUtils.setStepDescription(
					"Unable to identify [Creative Builder] zone", true);
			m_assert.fail("Unable to identify [Creative Builder] zone");
		}
		logger.info("Identification of [Creative Builder] zone is successful");
		// Identify Upload Creative zone
		logger.info("Identify [Upload Creatives] zone");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabUploadCreativesZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabUploadCreativesZone")
						.getLocatorvalue());
		// If Upload Creative zone element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Upload Creative] zone");
			ReportUtils.setStepDescription(
					"Unable to identify [Upload Creative] zone", true);
			m_assert.fail("Unable to identify [Upload Creative] zone");
		}
		logger.info("Identification of [Upload Creatives] is successful");
		// Identify Reset button
		logger.info("Identify [Reset] button");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabResetBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabResetBtn")
						.getLocatorvalue());
		// If Reset button element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Reset] button");
			ReportUtils.setStepDescription("Unable to identify [Reset] button",
					true);
			m_assert.fail("Unable to identify [Reset] button");
		}
		logger.info("Identification of [Reset] button is successful");
		// Identify Done button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		// If Done button element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Done] button");
			ReportUtils.setStepDescription("Unable to identify [Done] button",
					true);
			m_assert.fail("Unable to identify [Done] button");
		}
		m_assert.assertAll();
	}

	@Test(priority = 6, dependsOnMethods = "loginAs")
	public void validationsInCreateCampaignBuilder() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the validationsInCreateCampaignBuilder method present in
		// testcaseList
		// If the method present then start the execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationsInCreateCampaignBuilder")) {
				forExecution = true;
				break;
			}
		}
		// If the method is not present then skip the method
		if (!forExecution) {
			logger.info("Testcase [validationsInCreateCampaignBuilder] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Testcase [validationsInCreateCampaignBuilder] is not added for execution",
							false);
			throw new SkipException(
					"Testcase [validationsInCreateCampaignBuilder] is not added for execution");
		}
		// Get the arguments from the input xml file
		testcaseArgs = getTestData("validationsInCreateCampaignBuilder");
		// Identify Campaign Builder tab header element
		logger.info("Identify [Campaign Builder] page header element");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatorvalue());
		// Get the Campaign Builder sub tab header element text
		String CampaignBuilderSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils
				.assertEqual(CampaignBuilderSubTabHeaderText,
						Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext());
		// If both texts are not same then navigate to Campaign Builder tab
		if (!isTextMatching) {
			// Identify Campaign Builder tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getLocatorvalue());
			// Get the Campaign Builder tab text
			String CampaignBuilderSubTabText = SeleniumUtils.getText(element);
			// Compare the return text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(
					CampaignBuilderSubTabText,
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext());
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				// Report in log and console
				logger.error(" [Campaign Builder] sub tab text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdCampaignBuilderTabText").getExptext()
						+ "] and the actual return text is["
						+ CampaignBuilderSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Campaign Builder] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AdCampaignBuilderTabText").getExptext(),
						CampaignBuilderSubTabText, true);
				m_assert.fail("[Campaign Builder] sub tab text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdCampaignBuilderTabText").getExptext()
						+ "] and the actual return text is["
						+ CampaignBuilderSubTabText + "]");
			}
			logger.info("Navigate to [Campaign Builder] tab");
			// Click on Campaign Builder sub tab
			SeleniumUtils.clickOnElement(element);
			// Identify Campaign Builder tab header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getLocatorvalue());
			// Get the Campaign Builder sub tab header element text
			CampaignBuilderSubTabHeaderText = SeleniumUtils.getText(element);
			// Compare the return text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(
					CampaignBuilderSubTabHeaderText, Suite.objectRepositoryMap
							.get("AdCBTabHeaderText").getExptext());
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				// Report in log and console
				logger.error(" [Campaign Builder] sub tab text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext()
						+ "] and the actual return text is["
						+ CampaignBuilderSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Campaign Builder] sub tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext(), CampaignBuilderSubTabHeaderText,
						true);
				m_assert.fail("[Campaign Builder] sub tab text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext()
						+ "] and the actual return text is["
						+ CampaignBuilderSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Campaign Builder] tab");
		logger.info("Verify the validations while creating Campaign");
		logger.info("Click on [DONE] button without entering data in any fields");
		// Scroll down
		SeleniumUtils.scrollDown();
		SeleniumUtils.scrollDown();
		// Identify Done button
		logger.info("Identify [DONE] button");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		// If Done button is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [DONE] button in [CAMPAIGN BUILDER] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify [DONE] button in [CAMPAIGN BUILDER] page",
							true);
			m_assert.fail("Unable to identify [DONE] button in [CAMPAIGN BUILDER] page");
		}
		logger.info("Identification of [DONE] button is successful");
		// Click on Done button
		logger.info("Click on [DONE] button without any data");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Verify the Error message at Name field
		logger.info("Verify if error message displayed at Name field");
		// Identify Error message element
		logger.info("Identify error message displayed at Name field");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabNameErrorMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabNameErrorMSG")
						.getLocatorvalue());
		// Get the text of the error element
		String ErrorMessageText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(ErrorMessageText,
				Suite.objectRepositoryMap.get("AdCBTabNameErrorMSG")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Error Message at Campaign Name] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabNameErrorMSG")
							.getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText + "]");
			ReportUtils.setStepDescription(
					"[Error Message at Campaign Name] text matching failed",
					"", Suite.objectRepositoryMap.get("AdCBTabNameErrorMSG")
							.getExptext(), ErrorMessageText, true);
			m_assert.fail("[Error Message at Campaign Name] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabNameErrorMSG")
							.getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText + "");
		}
		logger.info("Error message verification at Campaign Name field is successful");
		// Identify Campaign name text box field
		logger.info("Identification of Campaign Nmae text box field");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatorvalue());
		// If Campaign Name text box field is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Campaign Name text box field in "
					+ "[Create a Campaign Builder] page");
			ReportUtils.setStepDescription(
					"Unable to identify Campaign Name text box field in "
							+ "[Create a Campaign Builder] page", true);
			m_assert.fail("Unable to identify Campaign Name text box field");
		}
		logger.info("Identification of Campaign Name tex box field is successful");
		// Enter text in Campaign name field
		logger.info("Enter campaign name in Campaign name field");
		SeleniumUtils.type(element, testcaseArgs.get("campaignName"));
		logger.info("Entering campaign name in Campaign name text box is successful");
		// Select 'Specific Platforms' radio button
		logger.info("Identify Platforms field & radio buttons");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Targeting - Platforms field");
			ReportUtils.setStepDescription(
					"Unable to identify Targeting - Platforms field", true);
			m_assert.fail("Unable to identify Targeting - Platforms field");
		}
		logger.info("Identification of Targeting-Platforms field is successful");
		// Identify Platforms radio buttons
		// Identify All Platforms radio button
		logger.info("Identify 'All Platforms' radio button");
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"ADCBTargetingPlatformAllPlatformsRB")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ADCBTargetingPlatformAllPlatformsRB")
								.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Targeting - Platforms - All Platforms radio button");
			ReportUtils
					.setStepDescription(
							"Unable to identify Targeting - Platforms - All Platforms radio button",
							true);
			m_assert.fail("Unable to identify Targeting - Platforms - All Platforms radio button");
		}
		logger.info("Identification of Targeting - Platforms - All Platforms field is successful");
		logger.info("Identify of Targeting - Platforms - Specific Platforms field");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRB")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRB")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Targeting - Platforms - "
					+ "Specific Platforms radio button");
			ReportUtils
					.setStepDescription(
							"Unable to identify Targeting - Platforms - Specific Platforms radio button",
							true);
			m_assert.fail("Unable to identify Targeting - Platforms - "
					+ "Specific Platforms radio button");
		}
		logger.info("Identification of Targeting - Platforms - "
				+ "Specific Platforms radio button is successful");
		// Select 'Specific radio buttons'
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRBSelection")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRBSelection")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Targeting - Platforms - "
					+ "Specific Platforms radio button selection");
			ReportUtils
					.setStepDescription(
							"Unable to identify Targeting - Platforms - "
									+ "Specific Platforms radio button selection",
							true);
			m_assert.fail("Unable to identify Targeting - Platforms - "
					+ "Specific Platforms radio button selection");
		}
		// Click at Specific Platforms radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Click operation successful on 'Specific Platforms' radio button");
		// Wait for the Platform window opens
		SeleniumUtils.sleepThread(5);
		// Identify Platform Window
		logger.info("Identify [Targeting-Platform window]");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatorvalue());
		// If Platform selection window is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window]", true);
			m_assert.fail("Unable to identify [Platform selection window]");
		}
		logger.info("Identification of [Targeting Platform selection window] is successful");
		// Identify Cancel button
		logger.info("Identify 'Cancel' button in [Targeting Platform selection winodw]");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowCancelBtn")
						.getLocatorvalue());
		// If Cancel button is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Targeting - Platform window - Cancel] button");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Targeting - Platform window - Cancel] button",
							true);
			m_assert.fail("Unable to identify [Targeting - Platform window - Cancel] button");
		}
		logger.info("Identification of 'Cancel' button is successful");
		// Click on Cancel button on Platform window
		SeleniumUtils.clickOnElement(element);
		// Wait for page to load
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		// Click on Done button
		logger.info("Click on [DONE] button");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		logger.info("Click operation on [DONE] button is successful");
		// Verify the Error message at Platform field
		logger.info("Verify if error message displayed at Platform field");
		// Identify Error message element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabPlatformErrorMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabPlatformErrorMSG")
						.getLocatorvalue());
		// Get the text of the error element
		ErrorMessageText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(ErrorMessageText,
				Suite.objectRepositoryMap.get("AdCBTabPlatformErrorMSG")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Error Message at Platform] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabPlatformErrorMSG")
							.getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText + "]");
			ReportUtils.setStepDescription(
					"[Error Message at Platform] text matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabPlatformErrorMSG")
							.getExptext(), ErrorMessageText, true);
			m_assert.fail("[Error Message at Platform] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabPlatformErrorMSG")
							.getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText + "]");
		}
		logger.info("Error message verification at Platform is successful");
		// Identify Platform link and select any of the platform
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformLink").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformLink").getLocatorvalue());
		// If Platform link is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Platform link in [CAMPAIGN BUILDER] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify Platform link in [CAMPAIGN BUILDER] tab",
							true);
			m_assert.fail("Unable to identify Platform link in [CAMPAIGN BUILDER] tab");
		}
		// Click on Platform link
		SeleniumUtils.clickOnElement(element);
		// Wait for the Platform selection window displayed
		SeleniumUtils.sleepThread(3);
		// Identify Platform selection window
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatorvalue());
		// If Platform selection window is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window]", true);
			m_assert.fail("Unable to identify [Platform selection window]");
		}
		// Identify Platform selection window header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowHeader")
						.getLocatorvalue());
		// Get the text of the Platform selection window header element
		String platformHeaderElement = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				platformHeaderElement,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowHeader")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Platform selection window header text] matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap
							.get("AdCBTabTargetingZonePlatformSelectionWindowHeader")
							.getExptext()
					+ "] and the actual return text is["
					+ platformHeaderElement + "]");
			ReportUtils
					.setStepDescription(
							"[Platform selection window header text] matching failed",
							"",
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZonePlatformSelectionWindowHeader")
									.getExptext(), platformHeaderElement, true);
			m_assert.fail("[Platform selection window header text] matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap
							.get("AdCBTabTargetingZonePlatformSelectionWindowHeader")
							.getExptext()
					+ "] and the actual return text is["
					+ platformHeaderElement + "]");
		}
		// Identify Platform selection Radio buttons
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZonePlatformSelectionWindowRadioButtons")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZonePlatformSelectionWindowRadioButtons")
								.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window radio buttons]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window radio buttons]",
							true);
			m_assert.fail("Unable to identify [Platform selection window radio buttons]");
		}
		// Select the Plaatform radio button as per the input xml file
		boolean isClicked = SeleniumCustomUtils
				.selectRadioButtonInPlatformSelectionInCampaign(element,
						testcaseArgs.get("platformDeviceType"));
		// If Radio button is not clicked then throw the error and exit
		if (!isClicked) {
			// Report in log and console
			logger.error("Unable to click on [Platform selection window radio buttons]");
			ReportUtils
					.setStepDescription(
							"Unable to click on [Platform selection window radio buttons]",
							true);
			m_assert.fail("Unable to click on  [Platform selection window radio buttons]");
		}
		// Wait for the page to load
		SeleniumUtils.sleepThread(3);
		// Identify OS lists
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowOSType")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowOSType")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window OS types]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window OS types]",
					true);
			m_assert.fail("Unable to identify [Platform selection window OS types]");
		}
		// Select specific OS from OS list as per the input file
		boolean isSelected = SeleniumCustomUtils.selectOSTypeFromList(element,
				testcaseArgs.get("OSType"));
		// Check for the seclection happened
		// if not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window");
			ReportUtils.setStepDescription("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window", true);
			m_assert.fail("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window");
		}
		// Identify Done button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowDoneBtn")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window Done button]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window Done button]",
							true);
			m_assert.fail("Unable to identify [Platform selection window Done button]");
		}
		// Click on Done button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Selection of Platform is successful");
		logger.info("Click on [DONE] button");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Click operation on [DONE] button is successful");
		// Verify the Error message at Platform field for Country dropdown
		logger.info("Verify if error message displayed at Platform field for Country");
		// Identify Error message element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabPlatformCountryErrorMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabPlatformCountryErrorMSG")
						.getLocatorvalue());
		// Get the text of the error element
		ErrorMessageText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(ErrorMessageText,
				Suite.objectRepositoryMap.get("AdCBTabPlatformCountryErrorMSG")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Error Message at Platform for country] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabPlatformCountryErrorMSG").getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText
					+ "]");
			ReportUtils
					.setStepDescription(
							"[Error Message at Platform for country] text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"AdCBTabPlatformCountryErrorMSG")
									.getExptext(), ErrorMessageText, true);
			m_assert.fail("[Error Message at Platform for country] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabPlatformCountryErrorMSG").getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText
					+ "]");
		}
		logger.info("Error message verification at Platform for country is successful");
		// Identify Countries selection box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Countries text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Countries text box]", true);
			m_assert.fail("Unable to identify [Countries text box]");
		}
		// Click on the Countries Selection box
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify Countries selection list
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
								.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Countries list element]");
			ReportUtils.setStepDescription(
					"Unable to identify [Countries list element]", true);
			m_assert.fail("Unable to identify [Countries list element]");
		}
		// Select country from country list
		isSelected = SeleniumCustomUtils
				.selectCountryFromCountryListInTargeting(element,
						testcaseArgs.get("country"));
		// If it is not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select country from country list");
			ReportUtils.setStepDescription(
					"Unable to select country from country list", true);
			m_assert.fail("Unable to select country from country list");
		}
		logger.info("Selection of Country is successful");
		logger.info("Click on [DONE] button");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Click operation on [DONE] button is successful");
		// Verify the Error message at Upload Creative
		logger.info("Verify if error message displayed at Platform field for Country");
		// Identify Error message element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabUploadCreativesErrorMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabUploadCreativesErrorMSG")
						.getLocatorvalue());
		// If Error Message is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify error message at "
					+ "Upload Creatives in [CAMPAIGN BUILDER] tab");
			ReportUtils.setStepDescription(
					"Unable to identify error message at "
							+ "Upload Creatives in [CAMPAIGN BUILDER] tab",
					true);
			m_assert.fail("Unable to identify error message at "
					+ "Upload Creatives in [CAMPAIGN BUILDER] tab");
		}
		// Get the text of the error element
		ErrorMessageText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(ErrorMessageText,
				Suite.objectRepositoryMap.get("AdCBTabUploadCreativesErrorMSG")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Error Message at Upload Creatives] text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesErrorMSG").getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText
					+ "]");
			ReportUtils.setStepDescription(
					"[Error Message at Upload Creatives] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesErrorMSG").getExptext(),
					ErrorMessageText, true);
			m_assert.fail("[Error Message at Upload Creatives] text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesErrorMSG").getExptext()
					+ "] and the actual return text is["
					+ ErrorMessageText
					+ "]");
		}
		// Identify Reset button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabResetBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabResetBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Reset button [CAMPAIGN BUILDER] page");
			ReportUtils.setStepDescription(
					"Unable to identify Reset button [CAMPAIGN BUILDER] page",
					true);
			m_assert.fail("Unable to identify Reset button [CAMPAIGN BUILDER] page");
		}
		// Click on Reset button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		m_assert.assertAll();
	}

	@SuppressWarnings("unused")
	@Test(priority = 7, dependsOnMethods = "loginAs")
	public void validateCampaignBuilderWithWhiteSpaces() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("validateCampaignBuilderWithWhiteSpaces")) {
				forExecution = true;
				break;
			}
		}
		// If the method is not present then skip the method
		if (!forExecution) {
			logger.info("Test case [validateCampaignBuilderWithWhiteSpaces]"
					+ " is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [validateCampaignBuilderWithWhiteSpaces"
							+ "] is not added for execution", false);
			throw new SkipException(
					"Test case [validateCampaignBuilderWithWhiteSpaces]"
							+ " is not added for execution");
		}
		// Get the arguments from the input xml file
		testcaseArgs = getTestData("validateCampaignBuilderWithWhiteSpaces");
		// Identify Campaign Builder tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatorvalue());
		// If Campaign Builder tab is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Campaign Builder] sub tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Campaign Builder] sub tab", true);
			m_assert.fail("Unable to identify [Campaign Builder] sub tab");
		}
		// Get the Campaign Builder tab text
		String CampaignBuilderSubTabText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CampaignBuilderSubTabText,
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error(" [Campaign Builder] sub tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext(), CampaignBuilderSubTabText, true);
			m_assert.fail("[Campaign Builder] sub tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabText + "]");
		}
		logger.info("Navigate to [Campaign Builder] tab");
		// Click on Campaign Builder sub tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		// Identify Campaign Builder tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatorvalue());
		// Get the Campaign Builder sub tab header element text
		String CampaignBuilderSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils
				.assertEqual(CampaignBuilderSubTabHeaderText,
						Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext());
		// If both texts are not same then navigate to Campaign Builder tab
		if (!isTextMatching) {
			// Report in log and console
			logger.error(" [Campaign Builder] sub tab header text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] sub tab header text matching failed",
					"", Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext(), CampaignBuilderSubTabHeaderText,
					true);
			m_assert.fail("[Campaign Builder] sub tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabHeaderText + "]");

		}
		logger.info("User is on [Campaign Builder] tab");
		// Identify Configure Your Campaign text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getLocatorvalue());
		// Get the text of Configure Your Campaign text
		String ConfigureYourCampaignText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureYourCampaignText,
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Configure Your Campaign text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext()
					+ "] and the actual return text is["
					+ ConfigureYourCampaignText + "]");
			ReportUtils.setStepDescription(
					"[Configure Your Campaign text] matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext(),
					ConfigureYourCampaignText, true);
			m_assert.fail("[Configure Your Campaign text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext()
					+ "] and the actual return text is["
					+ ConfigureYourCampaignText + "]");
		}
		logger.info("[Configure Your Campaign] text verification is successful");
		logger.info("Verify the [Campaign Builder] form");
		// Identification of campaign Name field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getLocatorvalue());
		// Get the text of campaign name field
		String campaignNameFieldText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(campaignNameFieldText,
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[campaign name field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext()
					+ "] and the actual return text is["
					+ campaignNameFieldText + "]");
			ReportUtils.setStepDescription(
					"[campaign name field text] matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext(), campaignNameFieldText, true);
			m_assert.fail("[campaign name field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext()
					+ "] and the actual return text is["
					+ campaignNameFieldText + "]");
		}
		// Identify Campaign name field textbox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatorvalue());
		// If campaign name field textbox is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [campaign name field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [campaign name field text box]", true);
			m_assert.fail("Unable to identify [campaign name field text box]");
		}
		SeleniumUtils.clearText(element);
		// Enter White spaces
		SeleniumUtils.typeKeys(element, Keys.SPACE);
		// Identify Accuracy element and radio buttons
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getLocatorvalue());
		// Get the text of the Accruacy element
		String AccuracyText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				AccuracyText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext()
					+ "] and the actual return text is[" + AccuracyText + "]");
			ReportUtils.setStepDescription(
					"[Accuracy] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext(),
					AccuracyText, true);
			m_assert.fail("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext()
					+ "] and the actual return text is[" + AccuracyText + "]");
		}
		logger.info("[Accuracy] field text matching successful");
		// Identify Countries text element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesText")
						.getLocatorvalue());
		// Get the text of the Countries element
		String CountriesText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				CountriesText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Countries] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyCountriesText")
							.getExptext()
					+ "] and the actual return text is["
					+ CountriesText + "]");
			ReportUtils.setStepDescription(
					"[Countries] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyCountriesText")
							.getExptext(), CountriesText, true);
			m_assert.fail("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyCountriesText")
							.getExptext()
					+ "] and the actual return text is["
					+ CountriesText + "]");
		}
		// Identify Countries selection box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Countries text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Countries text box]", true);
			m_assert.fail("Unable to identify [Countries text box]");
		}
		// Click on the Countries Selection box
		SeleniumUtils.clickOnElement(element);
		// Identfiy Countries selection list
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
								.getLocatorvalue());
		// If Countries list is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Countries list element]");
			ReportUtils.setStepDescription(
					"Unable to identify [Countries list element]", true);
			m_assert.fail("Unable to identify [Countries list element]");
		}
		// Select country from country list
		boolean isSelected = SeleniumCustomUtils
				.selectCountryFromCountryListInTargeting(element,
						testcaseArgs.get("country"));
		// If it is not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select country from country list");
			ReportUtils.setStepDescription(
					"Unable to select country from country list", true);
			m_assert.fail("Unable to select country from country list");
		}
		// Identify Click URL text area
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderClickURLTextArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderClickURLTextArea")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder - Click URL text area] ");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Creative Builder - Click URL text area]",
							true);
			m_assert.fail("Unable to identify [Creative Builder - Click URL text area] ");
		}
		// Enter url in Click URL text area
		SeleniumUtils.type(element, testcaseArgs.get("clickurl"));
		// Idetify Creative Builder - Image
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderImage")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderImage")
						.getLocatorvalue());
		// If Creative Builder - Image is null then throw
		// the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder - Image] ");
			ReportUtils.setStepDescription(
					"Unable to identify [Creative Builder - Image]", true);
			m_assert.fail("Unable to identify [Creative Builder - Image] ");
		}
		// Run the process as per the browser selection
		try {
			if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
				SeleniumUtils.sleepThread(5);
				Process process = new ProcessBuilder(
						GlobalConstants.AUTOIT_SCRIPT,
						GlobalConstants.IMAGE_PATH,
						GlobalConstants.FIREFOX_BROWSER).start();
			} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
				SeleniumUtils.sleepThread(5);
				Process process = new ProcessBuilder(
						GlobalConstants.AUTOIT_SCRIPT,
						GlobalConstants.IMAGE_PATH,
						GlobalConstants.CHROME_BROWSER).start();
			} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
				SeleniumUtils.sleepThread(5);
				Process process = new ProcessBuilder(
						GlobalConstants.AUTOIT_SCRIPT,
						GlobalConstants.IMAGE_PATH, GlobalConstants.IE_BROWSER)
						.start();
			} else if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
				SeleniumUtils.sleepThread(5);
				Process process = new ProcessBuilder(
						GlobalConstants.AUTOIT_SCRIPT,
						GlobalConstants.IMAGE_PATH,
						GlobalConstants.SAFARI_BROWSER).start();
			}
		} catch (IOException e) {
			logger.error(e);
		}
		// Click on Image to select image
		SeleniumUtils.clickOnElement(element);
		// Wait for the page to load
		SeleniumUtils.sleepThread(8);
		// Identify the Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderImageUploadSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderImageUploadSuccessMsg")
						.getLocatorvalue());
		// Get the text of the success message
		String ImageSuccessMsg = SeleniumUtils.getText(element);
		// Compare the text wiht expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				ImageSuccessMsg,
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderImageUploadSuccessMsg")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Success message after uploading Image] text matching failed."
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderImageUploadSuccessMsg")
							.getExptext()
					+ "] and the actual return text is["
					+ ImageSuccessMsg + "]");
			ReportUtils
					.setStepDescription(
							"[Success message after uploading Image] text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("AdCBTabCreativeBuilderImageUploadSuccessMsg")
									.getExptext(), ImageSuccessMsg, true);
			m_assert.fail("[Success message after uploading Image] text matching failed."
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderImageUploadSuccessMsg")
							.getExptext()
					+ "] and the actual return text is["
					+ ImageSuccessMsg + "]");
		}
		// Use this creative for creating campaign
		SeleniumUtils.sleepThread(3);
		// Identify Use this creative link
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderUseThisCreative")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderUseThisCreative")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Use Creative] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Use Creative] button", true);
			m_assert.fail("Unable to identify  [Use Creative] button ");
		}
		// Click on Use Creative link
		SeleniumUtils.clickOnElement(element);
		// Verify the creative added in UPloaded creative zone
		// Identify Uploaded Creatives header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabUploadCreativesZoneHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabUploadCreativesZoneHeaderText")
						.getLocatorvalue());
		// Get the text of the element
		String UploadedCreativesText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				UploadedCreativesText,
				Suite.objectRepositoryMap.get(
						"AdCBTabUploadCreativesZoneHeaderText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Uploaded Creatives header] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesZoneHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ UploadedCreativesText + "]");
			ReportUtils.setStepDescription(
					"[Uploaded Creatives header] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesZoneHeaderText")
							.getExptext(), UploadedCreativesText, true);
			m_assert.fail("[Uploaded Creatives header] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesZoneHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ UploadedCreativesText + "]");
		}
		logger.info("verification of [Uploaded Creatives] element is successful");
		SeleniumUtils.sleepThread(3);
		// Identify Done button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Done] button ");
			ReportUtils.setStepDescription("Unable to identify [Done] button",
					true);
			m_assert.fail("Unable to identify  [Done] button ");
		}
		// Click on Done button
		SeleniumUtils.clickOnElement(element);
		logger.info("Creation of Campaign Builder is successful");
		SeleniumUtils.sleepThread(6);
		// Identify Campaigns tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatorvalue());
		// Get the text of the element
		String CampaignsHeaderText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils
				.assertEqual(CampaignsHeaderText, Suite.objectRepositoryMap
						.get("AdCBTabHeaderText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("Validation failed at Campaign Name field in"
					+ " [Campaign Builder] page. "
					+ "User is able to create new campaign"
					+ " with white spaces in Name field");
			ReportUtils.setStepDescription(
					"Validation failed at Campaign Name field in"
							+ " [Campaign Builder] page. "
							+ "User is able to create new campaign"
							+ " with white spaces in Name field", true);
			m_assert.fail("Validation failed at Campaign Name field in"
					+ " [Campaign Builder] page. "
					+ "User is able to create new campaign"
					+ " with white spaces in Name field");
		}
		m_assert.assertAll();
	}

	@SuppressWarnings("unused")
	@Test(priority = 8, dependsOnMethods = "loginAs")
	public void createCampaignBuilder() throws IOException {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createCampaignBuilder")) {
				forExecution = true;
				break;
			}
		}
		// If the method is not present then skip the method
		if (!forExecution) {
			logger.info("Test case [createCampaignBuilder] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [createCampaignBuilder] is not added for execution",
							false);
			throw new SkipException(
					"Test case [createCampaignBuilder] is not added for execution");
		}
		// Get the arguments from the input xml file
		testcaseArgs = getTestData("createCampaignBuilder");
		// Identify Campaign Builder tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getLocatorvalue());
		// If Campaign Builder tab is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Campaign Builder] sub tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Campaign Builder] sub tab", true);
			m_assert.fail("Unable to identify [Campaign Builder] sub tab");
		}
		// Get the Campaign Builder tab text
		String CampaignBuilderSubTabText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CampaignBuilderSubTabText,
				Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error(" [Campaign Builder] sub tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] sub tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext(), CampaignBuilderSubTabText, true);
			m_assert.fail("[Campaign Builder] sub tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignBuilderTabText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabText + "]");
		}
		logger.info("Navigate to [Campaign Builder] tab");
		// Click on Campaign Builder sub tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		// Identify Campaign Builder tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabHeaderText")
						.getLocatorvalue());
		// Get the Campaign Builder sub tab header element text
		String CampaignBuilderSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils
				.assertEqual(CampaignBuilderSubTabHeaderText,
						Suite.objectRepositoryMap.get("AdCBTabHeaderText")
								.getExptext());
		// If both texts are not same then navigate to Campaign Builder tab
		if (!isTextMatching) {
			// Report in log and console
			logger.error(" [Campaign Builder] sub tab header text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Campaign Builder] sub tab header text matching failed",
					"", Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext(), CampaignBuilderSubTabHeaderText,
					true);
			m_assert.fail("[Campaign Builder] sub tab text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignBuilderSubTabHeaderText + "]");

		}
		logger.info("User is on [Campaign Builder] tab");
		// Identify Configure Your Campaign text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getLocatorvalue());
		// Get the text of Configure Your Campaign text
		String ConfigureYourCampaignText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureYourCampaignText,
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Configure Your Campaign text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext()
					+ "] and the actual return text is["
					+ ConfigureYourCampaignText + "]");
			ReportUtils.setStepDescription(
					"[Configure Your Campaign text] matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext(),
					ConfigureYourCampaignText, true);
			m_assert.fail("[Configure Your Campaign text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext()
					+ "] and the actual return text is["
					+ ConfigureYourCampaignText + "]");
		}
		logger.info("[Configure Your Campaign] text verification is successful");
		logger.info("Verify the [Campaign Builder] form");
		// Identification of campaign Name field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getLocatorvalue());
		// Get the text of campaign name field
		String campaignNameFieldText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(campaignNameFieldText,
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[campaign name field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext()
					+ "] and the actual return text is["
					+ campaignNameFieldText + "]");
			ReportUtils.setStepDescription(
					"[campaign name field text] matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext(), campaignNameFieldText, true);
			m_assert.fail("[campaign name field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext()
					+ "] and the actual return text is["
					+ campaignNameFieldText + "]");
		}
		// Identify Campaign name field textbox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatorvalue());
		// If campaign name field textbox is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [campaign name field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [campaign name field text box]", true);
			m_assert.fail("Unable to identify [campaign name field text box]");
		}
		// Enter Campaign name from the input advertising sheet in
		// Campaign name text box
		SeleniumUtils.clearText(element);
		SeleniumUtils.type(element, testcaseArgs.get("campaignName"));
		// Identify Bid field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabBidField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabBidField")
						.getLocatorvalue());
		// If bid field is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Bid field]");
			ReportUtils.setStepDescription("Unable to identify [Bid field]",
					true);
			m_assert.fail("Unable to identify [Bid field]");
		}
		// Get the text of the field
		String BidText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(BidText,
				Suite.objectRepositoryMap.get("AdCBTabBidField").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Bid field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabBidField")
							.getExptext()
					+ "] and the actual return text is["
					+ BidText + "]");
			ReportUtils.setStepDescription("[Bid field text] matching failed",
					"", Suite.objectRepositoryMap.get("AdCBTabBidField")
							.getExptext(), BidText, true);
			m_assert.fail("[Bid field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabBidField")
							.getExptext()
					+ "] and the actual return text is["
					+ BidText + "]");
		}
		// Identify Bid field text box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabBidFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabBidFieldTextbox")
						.getLocatorvalue());
		// If bid field text box is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Bid field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Bid field text box]", true);
			m_assert.fail("Unable to identify [Bid field text box]");
		}
		// Identify Max. Daily Spend field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabMaxDailySpendField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabMaxDailySpendField")
						.getLocatorvalue());
		// Get the text of the field
		String MaxDailySpendText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(MaxDailySpendText,
				Suite.objectRepositoryMap.get("AdCBTabMaxDailySpendField")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Max. Daily Spend text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap
							.get("AdCBTabMaxDailySpendField").getExptext()
					+ "] and the actual return text is["
					+ MaxDailySpendText
					+ "]");
			ReportUtils.setStepDescription(
					"[Max. Daily Spend text] matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabMaxDailySpendField")
							.getExptext(), MaxDailySpendText, true);
			m_assert.fail("[Max. Daily Spend field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap
							.get("AdCBTabMaxDailySpendField").getExptext()
					+ "] and the actual return text is["
					+ MaxDailySpendText
					+ "]");
		}
		// Identify Max. Daily Spend field text box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabMaxDailySpendFieldTextbox").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabMaxDailySpendFieldTextbox").getLocatorvalue());
		// If Max. Daily Spend field text box is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Max. Daily Spend field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Max. Daily Spend field text box]",
					true);
			m_assert.fail("Unable to identify [Max. Daily Spend field text box]");
		}
		// Identify Category field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCategoryField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCategoryField")
						.getLocatorvalue());
		// Get the text of the field
		String CategoryText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CategoryText,
				Suite.objectRepositoryMap.get("AdCBTabCategoryField")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Category field] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCategoryField")
							.getExptext()
					+ "] and the actual return text is["
					+ CategoryText + "]");
			ReportUtils.setStepDescription("[Category field] matching failed",
					"", Suite.objectRepositoryMap.get("AdCBTabCategoryField")
							.getExptext(), CategoryText, true);
			m_assert.fail("[Category field] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCategoryField")
							.getExptext()
					+ "] and the actual return text is["
					+ CategoryText + "]");
		}
		// Identify element Dropdown element
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdCBTabCategoryFieldDropdownElement")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdCBTabCategoryFieldDropdownElement")
								.getLocatorvalue());
		// If Category field dropdown element is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Category dropdown element]");
			ReportUtils.setStepDescription(
					"Unable to identify [Category dropdown element]", true);
			m_assert.fail("Unable to identify [Category dropdown element]");
		}
		// Click on Dropdown element
		SeleniumUtils.clickOnElement(element);
		// Select input Category value from category dropdown
		// Identify Category field Dropdown element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabCategoryFieldDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCategoryFieldDropdown")
						.getLocatorvalue());
		// If Category field dropdown is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Category dropdown]");
			ReportUtils.setStepDescription(
					"Unable to identify [Category dropdown]", true);
			m_assert.fail("Unable to identify [Category dropdown]");
		}
		// Select the Category value as persent in input xml file
		boolean isSelected = SeleniumUtils.selectDropdownByTextFromList(
				element, testcaseArgs.get("category"));
		// Check whether the category value selected or not
		// If not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select Category ["
					+ testcaseArgs.get("category") + "] from category dropdown");
			ReportUtils.setStepDescription(
					"Unable to select Category ["
							+ testcaseArgs.get("category")
							+ "] from category dropdown", true);
			m_assert.fail("Unable to select Category ["
					+ testcaseArgs.get("category") + "] from category dropdown");
		}
		// Identify Targeting Zone targeting header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneTargetingText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneTargetingText").getLocatorvalue());
		// Get the text of the field
		String TargetingText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				TargetingText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneTargetingText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Targeting header text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneTargetingText").getExptext()
					+ "] and the actual return text is[" + TargetingText + "]");
			ReportUtils.setStepDescription(
					"[Targeting header text] matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneTargetingText").getExptext(),
					TargetingText, true);
			m_assert.fail("[Targeting header text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneTargetingText").getExptext()
					+ "] and the actual return text is[" + TargetingText + "]");
		}
		// Identify Platforms field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getLocatorvalue());
		// Get the text of the Platform field
		String PlatformText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				PlatformText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Platform field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZonePlatformField").getExptext()
					+ "] and the actual return text is[" + PlatformText + "]");
			ReportUtils.setStepDescription(
					"[Platform field text] matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZonePlatformField").getExptext(),
					PlatformText, true);
			m_assert.fail("[Platform field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZonePlatformField").getExptext()
					+ "] and the actual return text is[" + PlatformText + "]");
		}
		// Select 'Specific radio buttons'
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRBSelection")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRBSelection")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Targeting - Platforms - "
					+ "Specific Platforms radio button selection");
			ReportUtils
					.setStepDescription(
							"Unable to identify Targeting - Platforms - "
									+ "Specific Platforms radio button selection",
							true);
			m_assert.fail("Unable to identify Targeting - Platforms - Specific Platforms radio button selection");
		}
		// Click at Specific Platforms radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Click operation successful on 'Specific Platforms' radio button");
		// Wait for the Platform selection window displayed
		SeleniumUtils.sleepThread(3);
		// Identify Platform selection window
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatorvalue());
		// If Platform selection window is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window]", true);
			m_assert.fail("Unable to identify [Platform selection window]");
		}
		// Identify Platform selection window header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowHeader")
						.getLocatorvalue());
		// Get the text of the Platform selection window header element
		String platformHeaderElement = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				platformHeaderElement,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowHeader")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Platform selection window header text] matching failed."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap
							.get("AdCBTabTargetingZonePlatformSelectionWindowHeader")
							.getExptext()
					+ "] and the actual return text is["
					+ platformHeaderElement + "]");
			ReportUtils
					.setStepDescription(
							"[Platform selection window header text] matching failed",
							"",
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZonePlatformSelectionWindowHeader")
									.getExptext(), platformHeaderElement, true);
			m_assert.fail("[Platform selection window header text] matching failed."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap
							.get("AdCBTabTargetingZonePlatformSelectionWindowHeader")
							.getExptext()
					+ "] and the actual return text is["
					+ platformHeaderElement + "]");
		}
		// Identify Platform selection Radio buttons
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZonePlatformSelectionWindowRadioButtons")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZonePlatformSelectionWindowRadioButtons")
								.getLocatorvalue());
		// If Platform selection window radio buttons are null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window radio buttons]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window radio buttons]",
							true);
			m_assert.fail("Unable to identify [Platform selection window radio buttons]");
		}
		// Select the Plaatform radio button as per the input xml file
		boolean isClicked = SeleniumCustomUtils
				.selectRadioButtonInPlatformSelectionInCampaign(element,
						testcaseArgs.get("platformDeviceType"));
		// If Radio button is not clicked then throw the error and exit
		if (!isClicked) {
			// Report in log and console
			logger.error("Unable to click on [Platform selection window radio buttons]");
			ReportUtils
					.setStepDescription(
							"Unable to click on [Platform selection window radio buttons]",
							true);
			m_assert.fail("Unable to click on  [Platform selection window radio buttons]");
		}
		// Wait for the page to load
		SeleniumUtils.sleepThread(3);
		// Identify OS lists
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowOSType")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowOSType")
						.getLocatorvalue());
		// If Platform selection window OS types are null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window OS types]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window OS types]",
					true);
			m_assert.fail("Unable to identify [Platform selection window OS types]");
		}
		// Select specific OS from OS list as per the input file
		isSelected = SeleniumCustomUtils.selectOSTypeFromList(element,
				testcaseArgs.get("OSType"));
		// Check for the selection happened
		// if not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window");
			ReportUtils.setStepDescription("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window", true);
			m_assert.fail("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window");

		}
		// Identify Done, Cancel, Reset buttons
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowCancelBtn")
						.getLocatorvalue());
		// If Cancel button is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window Cancel button]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window Cancel button]",
							true);
			m_assert.fail("Unable to identify [Platform selection window Cancel button]");
		}
		// Identify Reset button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowResetBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowResetBtn")
						.getLocatorvalue());
		// If Reset button is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window Reset button]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window Reset button]",
							true);
			m_assert.fail("Unable to identify [Platform selection window Reset button]");
		}
		// Identify Done button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowDoneBtn")
						.getLocatorvalue());
		// If Done button is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window Done button]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window Done button]",
							true);
			m_assert.fail("Unable to identify [Platform selection window Done button]");
		}
		// Click on Done button
		SeleniumUtils.clickOnElement(element);
		// Identify Accuracy element and radio buttons
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getLocatorvalue());
		// Get the text of the Accruacy element
		String AccuracyText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				AccuracyText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext()
					+ "] and the actual return text is[" + AccuracyText + "]");
			ReportUtils.setStepDescription(
					"[Accuracy] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext(),
					AccuracyText, true);
			m_assert.fail("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext()
					+ "] and the actual return text is[" + AccuracyText + "]");
		}
		logger.info("[Accuracy] field text matching successful");
		// Select Accuracy itemls(country, state, Zipcode,Hyperlocal) from the
		// input xml file
		logger.info("Select [Accuracy] radio buttons as per the input..");
		logger.info("Select accuracy [" + testcaseArgs.get("accuracy")
				+ "] from the radio buttons");
		// Check the validations of Accuracy
		if (testcaseArgs.get("accuracy").equalsIgnoreCase("Country")) {
			// Identify Countries text element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyCountriesText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyCountriesText")
							.getLocatorvalue());
			// Get the text of the Countries element
			String CountriesText = SeleniumUtils.getText(element);
			// Compare the text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(
					CountriesText,
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyCountriesText")
							.getExptext());
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				// Report in log and console
				logger.error("[Countries] text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdCBTabTargetingZoneAccuracyCountriesText")
								.getExptext()
						+ "] and the actual return text is["
						+ CountriesText
						+ "]");
				ReportUtils.setStepDescription(
						"[Countries] text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AdCBTabTargetingZoneAccuracyCountriesText")
								.getExptext(), CountriesText, true);
				m_assert.fail("[Accuracy] text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdCBTabTargetingZoneAccuracyCountriesText")
								.getExptext()
						+ "] and the actual return text is["
						+ CountriesText
						+ "]");
			}
			// Identify Countries selection box
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
									.getLocatorvalue());
			// If Accuracy Countries text box is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Countries text box]");
				ReportUtils.setStepDescription(
						"Unable to identify [Countries text box]", true);
				m_assert.fail("Unable to identify [Countries text box]");
			}
			// Click on the Countries Selection box
			SeleniumUtils.clickOnElement(element);
			// Identfiy Countries selection list
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
									.getLocatorvalue());
			// If Countries list is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Countries list element]");
				ReportUtils.setStepDescription(
						"Unable to identify [Countries list element]", true);
				m_assert.fail("Unable to identify [Countries list element]");
			}
			// Select country from country list
			isSelected = SeleniumCustomUtils
					.selectCountryFromCountryListInTargeting(element,
							testcaseArgs.get("country"));
			// If it is not selected then throw the error and exit
			if (!isSelected) {
				// Report in log and console
				logger.error("Unable to select country from country list");
				ReportUtils.setStepDescription(
						"Unable to select country from country list", true);
				m_assert.fail("Unable to select country from country list");
			}
			logger.info("Selection of Country is successful");
			// If user selects State as accuracy option
		}
		if (testcaseArgs.get("accuracy").equalsIgnoreCase("State")) {
			// Identify States element and text box
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStates")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStates")
							.getLocatorvalue());
			// If States element is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] element");
				ReportUtils.setStepDescription(
						"Unable to identify [States] element", true);
				m_assert.fail("Unable to identify [States] element");
			}
			// Identify States element text box
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelection")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelection")
							.getLocatorvalue());
			// If States element text box is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] element text box");
				ReportUtils.setStepDescription(
						"Unable to identify [States] element text box", true);
				m_assert.fail("Unable to identify [States] element text box");
			}
			// Click on States element selection
			SeleniumUtils.clickOnElement(element);
			// Identify States selection list
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelectionList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelectionList")
							.getLocatorvalue());
			// If States element selection list is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] selections list");
				ReportUtils.setStepDescription(
						"Unable to identify [States] selections list", true);
				m_assert.fail("Unable to identify [States] selections list");
			}
			// Select State from States list
			isSelected = SeleniumCustomUtils
					.selectCountryFromCountryListInTargeting(element,
							testcaseArgs.get("states"));
			// It it is not selected then throw the error and exit
			if (!isSelected) {
				// Report in log and console
				logger.error("Unable to select states ["
						+ testcaseArgs.get("states") + "] from states list");
				ReportUtils.setStepDescription("Unable to select states ["
						+ testcaseArgs.get("states") + "] from states list",
						true);
				m_assert.fail("Unable to select states ["
						+ testcaseArgs.get("states") + "] from states list");
			}
			logger.info("Selection of State from States list is successful");
		} else if (testcaseArgs.get("accuracy").equalsIgnoreCase("Zipcode")) {
			// have issue
			// do nothing
		} else if (testcaseArgs.get("accuracy").equalsIgnoreCase("Hyperlocal")) {
			// Identify Targeting-Hyperlocal popup window text
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getLocatorvalue());
			// Get the text of the Targeting-Hyperlocal popup header element
			String HyperlocalPopUpText = SeleniumUtils.getText(element);
			// Compare the text with expected text from OR
			isTextMatching = SeleniumUtils
					.assertEqual(
							HyperlocalPopUpText,
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getExptext());
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				// Report in log and console
				logger.error("[Targeting-Hyperlocal popup header element] text matching failed. "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
								.getExptext()
						+ "] and the actual return text is["
						+ HyperlocalPopUpText + "]");
				ReportUtils
						.setStepDescription(
								"[Targeting-Hyperlocal popup header element] text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
										.getExptext(), HyperlocalPopUpText,
								true);
				m_assert.fail("[Targeting-Hyperlocal popup header element] text matching failed."
						+ " The expected text is ["
						+ Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
								.getExptext()
						+ "] and the actual return text is["
						+ HyperlocalPopUpText + "]");
			}
			logger.info("Identification of [Targeting-Hyperlocal] popup header element is successful");
			// /need to handle hyperlocal popuupppppppppppp
			//
		}
		logger.info("Selection of accuracy [" + testcaseArgs.get("accuracy")
				+ "] & country [" + testcaseArgs.get("country")
				+ "] from accuracy is successful");
		// Select source radio button as per the input xml file
		// Identify source text box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabTargetingZoneSourceText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabTargetingZoneSourceText")
						.getLocatorvalue());
		// Get the text of the element
		String SourceText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(SourceText,
				Suite.objectRepositoryMap.get("AdCBTabTargetingZoneSourceText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Targeting-Source element] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneSourceText").getExptext()
					+ "] and the actual return text is[" + SourceText + "]");
			ReportUtils.setStepDescription(
					"[Targeting-Source element] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneSourceText").getExptext(),
					SourceText, true);
			m_assert.fail("[Targeting-Source element] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneSourceText").getExptext()
					+ "] and the actual return text is[" + SourceText + "]");
		}
		logger.info("Verification of [Targeting-Source] element is successful");
		// Identify Source radio buttons
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneSourceRadioButtons")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneSourceRadioButtons")
						.getLocatorvalue());
		// If source radio buttons list is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Targeting - Source radio buttons list] element");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Targeting - Source radio buttons list] element",
							true);
			m_assert.fail("Unable to identify [Targeting - Source radio buttons list] element");
		}
		// Select the source option from input xml
		isSelected = SeleniumCustomUtils.selectSourceRadioButtonInTargeting(
				element, testcaseArgs.get("source"));
		// If source radio button is not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select source ["
					+ testcaseArgs.get("source") + "] in source radio list");
			ReportUtils.setStepDescription("Unable to select source ["
					+ testcaseArgs.get("source") + "] in source radio list",
					true);
			m_assert.fail("Unable to select source ["
					+ testcaseArgs.get("source") + "] in source radio list");
		}
		logger.info("Targeting selection is successful");
		// Need to handle source type and audience in targeting
		// Need to handle Delivery Options
		// Handling Creative Builder
		logger.info("Selection in Creative Builder");
		// Identify Creative Builder header element text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderText")
						.getLocatorvalue());
		// Get the text of the element
		String CreativeBuilderText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CreativeBuilderText,
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Creative Builder header] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderText").getExptext()
					+ "] and the actual return text is["
					+ CreativeBuilderText
					+ "]");
			ReportUtils.setStepDescription(
					"[Creative Builder header] text matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderText")
							.getExptext(), CreativeBuilderText, true);
			m_assert.fail("[Creative Builder header] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderText").getExptext()
					+ "] and the actual return text is["
					+ CreativeBuilderText
					+ "]");
		}
		logger.info("Identification of [Creative Builder header] element is successful");
		// Identify Phone Ad field element and dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdElement")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdElement")
						.getLocatorvalue());
		// Get the text of the element
		String PhoneAdText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				PhoneAdText,
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdElement").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Creative Builder - Phone Ad] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderPhoneAdElement")
							.getExptext()
					+ "] and the actual return text is["
					+ PhoneAdText + "]");
			ReportUtils.setStepDescription(
					"[Creative Builder - Phone Ad] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderPhoneAdElement")
							.getExptext(), PhoneAdText, true);
			m_assert.fail("[Creative Builder - Phone Ad] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderPhoneAdElement")
							.getExptext()
					+ "] and the actual return text is["
					+ PhoneAdText + "]");
		}
		// Identify Phone Ad dropdown element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdDropdown")
						.getLocatorvalue());
		// If Creative Builder - Phone Ad dropdown element is null then throw
		// the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder - Phone Ad dropdown] element");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Creative Builder - Phone Ad dropdown] element",
							true);
			m_assert.fail("Unable to identify [Creative Builder - Phone Ad dropdown] element");
		}
		// Click on Dropdown
		SeleniumUtils.clickOnElement(element);
		// Select specific Ad type from the list
		// Identify Ad type list items
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdDropdownList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderPhoneAdDropdownList")
						.getLocatorvalue());
		// If Creative Builder - Phone Ad list is null then throw
		// the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder - Phone Ad list] ");
			ReportUtils.setStepDescription(
					"Unable to identify [Creative Builder - Phone Ad list]",
					true);
			m_assert.fail("Unable to identify [Creative Builder - Phone Ad list] ");
		}
		/*
		 * isClicked =
		 * SeleniumCustomUtils.selectPhoneAdInCreativeBuilder(element,
		 * testcaseArgs.get("phonead")); // If phone ad is not selected from the
		 * dropdown then throw the error // and exit if (!isClicked) { // Report
		 * in log and console logger.error("Unable to select phone ad  [" +
		 * testcaseArgs.get("phonead") + "] from the Phone Ad dropdown"); //
		 * Capture the screen shot
		 * SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() + "_"
		 * + "createCampaignBuilder" + "_" + count++); // throw testng exception
		 * m_assert.fail("Unable to select phone ad  [" +
		 * testcaseArgs.get("phonead") + "] from the Phone Ad dropdown"); }
		 */
		// Identify Click URL text area
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderClickURLTextArea")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderClickURLTextArea")
						.getLocatorvalue());
		// If Click URL text area is null then throw
		// the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder - Click URL text area] ");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Creative Builder - Click URL text area]",
							true);
			m_assert.fail("Unable to identify [Creative Builder - Click URL text area] ");
		}
		// Enter url in Click URL text area
		SeleniumUtils.type(element, testcaseArgs.get("clickurl"));
		// Idetify Creative Builder - Image
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderImage")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCreativeBuilderImage")
						.getLocatorvalue());
		// If Creative Builder - Image is null then throw
		// the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Creative Builder - Image] ");
			ReportUtils.setStepDescription(
					"Unable to identify [Creative Builder - Image]", true);
			m_assert.fail("Unable to identify [Creative Builder - Image] ");
		}
		// Run the process as per the browser selection
		if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
			Process process = new ProcessBuilder(GlobalConstants.AUTOIT_SCRIPT,
					GlobalConstants.IMAGE_PATH, GlobalConstants.FIREFOX_BROWSER)
					.start();
		} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
			Process process = new ProcessBuilder(GlobalConstants.AUTOIT_SCRIPT,
					GlobalConstants.IMAGE_PATH, GlobalConstants.CHROME_BROWSER)
					.start();
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			Process process = new ProcessBuilder(GlobalConstants.AUTOIT_SCRIPT,
					GlobalConstants.IMAGE_PATH, GlobalConstants.IE_BROWSER)
					.start();
		}
		// Click on Image to select image
		SeleniumUtils.clickOnElement(element);
		// Wait for the page to load
		SeleniumUtils.sleepThread(8);
		// Identify the Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderImageUploadSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderImageUploadSuccessMsg")
						.getLocatorvalue());
		// Get the text of the success message
		String ImageSuccessMsg = SeleniumUtils.getText(element);
		// Compare the text wiht expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				ImageSuccessMsg,
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderImageUploadSuccessMsg")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Success message after uploading Image] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderImageUploadSuccessMsg")
							.getExptext()
					+ "] and the actual return text is["
					+ ImageSuccessMsg + "]");
			ReportUtils
					.setStepDescription(
							"[Success message after uploading Image] text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("AdCBTabCreativeBuilderImageUploadSuccessMsg")
									.getExptext(), ImageSuccessMsg, true);
			m_assert.fail("[Success message after uploading Image] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabCreativeBuilderImageUploadSuccessMsg")
							.getExptext()
					+ "] and the actual return text is["
					+ ImageSuccessMsg + "]");
		}
		logger.info("Verification of Success message after uploading image is successful");
		// Use this creative for creating campaign
		SeleniumUtils.sleepThread(3);
		// Identify Use this creative link
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderUseThisCreative")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabCreativeBuilderUseThisCreative")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Use Creative] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Use Creative] button", true);
			m_assert.fail("Unable to identify  [Use Creative] button ");
		}
		// Click on Use Creative link
		SeleniumUtils.clickOnElement(element);
		// Verify the creative added in UPloaded creative zone
		// Identify Uploaded Creatives header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabUploadCreativesZoneHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabUploadCreativesZoneHeaderText")
						.getLocatorvalue());
		// Get the text of the element
		String UploadedCreativesText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				UploadedCreativesText,
				Suite.objectRepositoryMap.get(
						"AdCBTabUploadCreativesZoneHeaderText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Uploaded Creatives header] text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesZoneHeaderText")
							.getExptext() + "] and the actual return text is["
					+ UploadedCreativesText + "]");
			ReportUtils.setStepDescription(
					"[Uploaded Creatives header] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesZoneHeaderText")
							.getExptext(), UploadedCreativesText, true);
			m_assert.fail("[Uploaded Creatives header] text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabUploadCreativesZoneHeaderText")
							.getExptext() + "] and the actual return text is["
					+ UploadedCreativesText + "]");
		}
		logger.info("verification of [Uploaded Creatives] element is successful");
		SeleniumUtils.sleepThread(3);
		// Identify Done button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		// If Done button is not displayed then throw the error and exit
		// the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Done] button ");
			ReportUtils.setStepDescription("Unable to identify [Done] button",
					true);
			m_assert.fail("Unable to identify  [Done] button ");
		}
		// Click on Done button
		SeleniumUtils.clickOnElement(element);
		logger.info("Creation of Campaign Builder is successful");
		SeleniumUtils.sleepThread(8);
		logger.info("Check the created campaign builder in [CAMPAIGNS] tab");
		logger.info("Verify if user is on [CAMPAIGNS] tab");
		// Wait for Campaigns tab to be displayed
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getExptext());
		// Verify user is landed on Campaigns tab
		// Identify Campaigns tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getLocatorvalue());
		// Get the text of the element
		String CampaignsHeaderText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CampaignsHeaderText,
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Campaigns tab header] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignsHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Campaigns tab header] text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
							.getExptext(), CampaignsHeaderText, true);
			m_assert.fail("[Campaigns tab header] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
							.getExptext()
					+ "] and the actual return text is["
					+ CampaignsHeaderText + "]");
		}
		logger.info("User is landed on [CAMPAIGNS] tab");
		// Verify the created campaign displayed
		// Identify the application list
		logger.info("Verify the application list");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignsTabCampaignsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabCampaignsList")
						.getLocatorvalue());
		// If Campaigns tab header element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify campaigns list in [Campaigns] tab");
			ReportUtils.setStepDescription(
					"Unable to identify campaigns list in [Campaigns] tab",
					true);
			m_assert.fail("Unable to identify campaigns list in [Campaigns] tab");
		}
		logger.info("Verification of campaigns applications list is successful");
		// Check the created campaign is exist in campaigns list
		boolean isPresent = SeleniumCustomUtils.checkCampaignInCampaignsList(
				element, testcaseArgs.get("campaignName"));
		// If campaign is not present in campaigns list then throw the error and
		// exit
		if (!isPresent) {
			// Report in log and console
			logger.error("Unable to identify campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab");
			ReportUtils.setStepDescription("Unable to identify campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab", true);
			m_assert.fail("Unable to identify campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab");
		}
		m_assert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = "loginAs")
	public void verifyAdvertisingCampaignsLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyAdvertisingCampaignsLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyAdvertisingCampaignsLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyAdvertisingCampaignsLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyAdvertisingCampaignsLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyAdvertisingCampaignsLayout");
		logger.info("Starting [verifyAdvertisingCampaignsLayout] execution");
		logger.info("Navigating to [Campaigns] tab");
		SeleniumUtils.sleepThread(3);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCampaignsTabText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabText")
						.getLocatorvalue());
		// Get the text
		String CampaignsSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(CampaignsSubTabText,
				Suite.objectRepositoryMap.get("AdCampaignsTabText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Campaigns] tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext()
					+ "] and the return text is ["
					+ CampaignsSubTabText + "]");
			ReportUtils.setStepDescription(
					"[Campaigns] tab text matching failed", "",
					Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext(), CampaignsSubTabText, true);
			m_assert.fail("[Campaigns] tab text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext()
					+ "] and the return text is ["
					+ CampaignsSubTabText + "]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify Campaigns list
		logger.info("Identify Campaigns list in [Campaigns] page");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCampaignsTabCampaignsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabCampaignsList")
						.getLocatorvalue());
		// If Campaigns tab header element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify campaigns list in [Campaigns] tab");
			ReportUtils.setStepDescription(
					"Unable to identify campaigns list in [Campaigns] tab",
					true);
			m_assert.fail("Unable to identify campaigns list in [Campaigns] tab");
		}
		m_assert.assertAll();
	}

	@Test(priority = 10, dependsOnMethods = "loginAs")
	public void editCampaign() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		// Check if editCampaign is added for execution
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editCampaign")) {
				forExecution = true;
				break;
			}
		}
		// If editCampaign is not added for execution then skip the testcase
		if (!forExecution) {
			logger.info("Test case [editCampaign] is not added for execution.");
			ReportUtils
					.setStepDescription(
							"Test case [editCampaign] is not added for execution",
							true);
			throw new SkipException(
					"Test case [editCampaign] is not added for execution.");
		}
		// Get the arguments for editCampaign
		testcaseArgs = getTestData("editCampaign");
		logger.info("Starting [editCampaign] execution");
		logger.info("Verify if user is on [Campaigns] page");
		SeleniumUtils.sleepThread(1);
		// Identify Campaigns page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getLocatorvalue());
		// Get the text of the Campaigns page header element
		String CampaignsPageHeaderText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(CampaignsPageHeaderText,
				Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
						.getExptext());
		// If both texts are not same then
		// Navigate to Campaigns page
		if (!isTextMatching) {
			logger.info("User is not on [Campaigns] page");
			logger.info("Navigate to [Campaigns] page");
			// Identify Campaigns sub-tab
			logger.info("Identify [Campaigns] sub-tab");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getLocatorvalue());
			// Ge the Campaign sub-tab text
			String CampaignsSubTabText = SeleniumUtils.getText(element);
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(CampaignsSubTabText,
					Suite.objectRepositoryMap.get("AdCampaignsTabText")
							.getExptext());
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				logger.error("[Campaigns] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdCampaignsTabText")
								.getExptext()
						+ "] and the return text is ["
						+ CampaignsSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Campaigns] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdCampaignsTabText")
								.getExptext(), CampaignsSubTabText, true);
				m_assert.fail("[Campaigns] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdCampaignsTabText")
								.getExptext()
						+ "] and the return text is ["
						+ CampaignsSubTabText + "]");
			}
			logger.info("Identification of [Campaigns] sub-tab is successful");
			// Click on Campaigns sub-tab
			logger.info("Click on [Campaigns] sub-tab");
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Campaigns page header element after clicking on
			// Campaigns sub-tab
			// Identify Campaigns page header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
							.getLocatorvalue());
			// Get the text of the Campaigns page header element
			CampaignsPageHeaderText = SeleniumUtils.getText(element);
			// Compare the text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(CampaignsPageHeaderText,
					Suite.objectRepositoryMap.get("AdCampaignsTabHeaderText")
							.getExptext());
			// If both texts are not same then
			// throw the error and exit
			if (!isTextMatching) {
				logger.error("[Campaigns] page header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdCampaignsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ CampaignsPageHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Campaigns] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"AdCampaignsTabHeaderText").getExptext(),
						CampaignsPageHeaderText, true);
				m_assert.fail("[Campaigns] page header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdCampaignsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ CampaignsPageHeaderText + "]");
			}
			logger.info("Click operation on [Campaigns] sub-tab is successful");
		}
		logger.info("User is on [Campaigns]");
		// Verify the Campaign
		logger.info("Verify if campaign [" + testcaseArgs.get("campaignName")
				+ "] is present in campaigns list in [Campaigns] page");
		// Identify Campaigns list
		logger.info("Identify Campaigns list in [Campaigns] page");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCampaignsTabCampaignsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCampaignsTabCampaignsList")
						.getLocatorvalue());
		// If Campaigns tab header element is null then throw the error and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify campaigns list in [Campaigns] tab");
			ReportUtils.setStepDescription(
					"Unable to identify campaigns list in [Campaigns] tab",
					true);
			m_assert.fail("Unable to identify campaigns list in [Campaigns] tab");
		}
		logger.info("Verification of campaigns applications list is successful");
		// Check the created campaign is exist in campaigns list
		boolean isCampaignPresent = SeleniumCustomUtils
				.checkCampaignInCampaignsList(element,
						testcaseArgs.get("campaignName"));
		// If campaign is not present in campaigns list then throw the error and
		// exit
		if (!isCampaignPresent) {
			// Report in log and console
			logger.error("Unable to identify campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab");
			ReportUtils.setStepDescription("Unable to identify campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab", true);
			m_assert.fail("Unable to identify campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab");
		}
		logger.info("Campaign [" + testcaseArgs.get("campaignName")
				+ "] is present in campaigns list");
		// Edit the Campaign
		boolean isClickOnCampaign = SeleniumCustomUtils
				.clickCampaignInCampaignsList(element,
						testcaseArgs.get("campaignName"));
		// If Edit button is not clicked then throw the error and exit
		if (!isClickOnCampaign) {
			// Report in log and console
			logger.error("Unable to click campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab");
			ReportUtils.setStepDescription("Unable to click campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab", true);
			m_assert.fail("Unable to click campaign ["
					+ testcaseArgs.get("campaignName")
					+ "]in Campaigns list in [Campaigns] tab");
		}
		logger.info("Click on campaign [" + testcaseArgs.get("campaignName")
				+ "] in campaigns list is successful");
		// Wait for the page to load
		SeleniumUtils.sleepThread(6);
		// Verify if user is on Edit Campaign page
		// Identify Edit Campaign header page element
		logger.info("Identify [Edit Campaign] page header element");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBEditCampaignPageHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBEditCampaignPageHeaderText")
						.getLocatorvalue());
		// Get the text of Edit Campaign page header element
		String EditCampaignPageHeaderText = SeleniumUtils.getText(element);
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(EditCampaignPageHeaderText,
				Suite.objectRepositoryMap.get("AdCBEditCampaignPageHeaderText")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Edit Campaign] page header text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBEditCampaignPageHeaderText").getExptext()
					+ "] and the return text is ["
					+ EditCampaignPageHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"[Edit Campaign] page header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBEditCampaignPageHeaderText").getExptext(),
					EditCampaignPageHeaderText, true);
			m_assert.fail("[Edit Campaign] page header text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBEditCampaignPageHeaderText").getExptext()
					+ "] and the return text is ["
					+ EditCampaignPageHeaderText
					+ "]");
		}
		logger.info("User is on [Edit Campaign] page");
		// Edit the campaign
		// Identify 'Configure Your Campaign' Zone & elments
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getLocatorvalue());
		// Get the text
		String ConfigureYourText = SeleniumUtils.getText(element);
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(
				ConfigureYourText,
				Suite.objectRepositoryMap.get(
						"AdCBTabConfigureYourCampaignText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error(" 'Configure Your Campaign' text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext()
					+ "] and the return text is [" + ConfigureYourText + "]");
			ReportUtils.setStepDescription(
					"'Configure Your Campaign' text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext(),
					ConfigureYourText, true);
			m_assert.fail(" 'Configure Your Campaign' text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabConfigureYourCampaignText").getExptext()
					+ "] and the return text is [" + ConfigureYourText + "]");
		}
		// Identification of campaign Name field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getLocatorvalue());
		// Get the text of campaign name field
		String campaignNameFieldText = SeleniumUtils.getText(element);
		// Compare the return text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(campaignNameFieldText,
				Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
						.getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[campaign name field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext()
					+ "] and the actual return text is["
					+ campaignNameFieldText + "]");
			ReportUtils.setStepDescription(
					"[campaign name field text] matching failed", "",
					Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext(), campaignNameFieldText, true);
			m_assert.fail("[campaign name field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get("AdCBTabCampaignNameField")
							.getExptext()
					+ "] and the actual return text is["
					+ campaignNameFieldText + "]");
		}
		// Identify Campaign name field textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdCBTabCampaignNameFieldTextbox")
						.getLocatorvalue());
		// If campaign name field textbox is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [campaign name field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [campaign name field text box]", true);
			m_assert.fail("Unable to identify [campaign name field text box]");
		}
		// Clear the previous enterd campaing text
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		// Enter Campaign name from the input advertising sheet in
		// Campaign name text box
		SeleniumUtils.type(element, testcaseArgs.get("campaignNameEdit"));
		// Identify Bid field text box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabBidFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabBidFieldTextbox")
						.getLocatorvalue());
		// If bid field text box is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Bid field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Bid field text box]", true);
			m_assert.fail("Unable to identify [Bid field text box]");
		}
		// Identify Max. Daily Spend field text box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabMaxDailySpendFieldTextbox").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabMaxDailySpendFieldTextbox").getLocatorvalue());
		// If Max. Daily Spend field text box is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Max. Daily Spend field text box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Max. Daily Spend field text box]",
					true);
			m_assert.fail("Unable to identify [Max. Daily Spend field text box]");
		}
		// Identify Category Dropdown element
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdCBTabCategoryFieldDropdownElement")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdCBTabCategoryFieldDropdownElement")
								.getLocatorvalue());
		// If Category field dropdown element is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Category dropdown element]");
			ReportUtils.setStepDescription(
					"Unable to identify [Category dropdown element]", true);
			m_assert.fail("Unable to identify [Category dropdown element]");
		}
		// Click on Dropdown element
		SeleniumUtils.clickOnElement(element);
		// Select input Category value from category dropdown
		// Identify Category field Dropdown element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdCBTabCategoryFieldDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabCategoryFieldDropdown")
						.getLocatorvalue());
		// If Category field dropdown is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Category dropdown]");
			ReportUtils.setStepDescription(
					"Unable to identify [Category dropdown]", true);
			m_assert.fail("Unable to identify [Category dropdown]");
		}
		// Select the Category value as persent in input xml file
		boolean isSelected = SeleniumUtils.selectDropdownByTextFromList(
				element, testcaseArgs.get("category"));
		// Check whether the category value selected or not
		// If not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select Category ["
					+ testcaseArgs.get("category") + "] from category dropdown");
			ReportUtils.setStepDescription(
					"Unable to select Category ["
							+ testcaseArgs.get("category")
							+ "] from category dropdown", true);
			m_assert.fail("Unable to select Category ["
					+ testcaseArgs.get("category") + "] from category dropdown");
		}
		// Identify Targeting Zone targeting header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneTargetingText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneTargetingText").getLocatorvalue());
		// Get the text of the field
		String TargetingText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				TargetingText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneTargetingText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Targeting header text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneTargetingText").getExptext()
					+ "] and the actual return text is[" + TargetingText + "]");
			ReportUtils.setStepDescription(
					"[Targeting header text] matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneTargetingText").getExptext(),
					TargetingText, true);
			m_assert.fail("[Targeting header text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneTargetingText").getExptext()
					+ "] and the actual return text is[" + TargetingText + "]");
		}
		// Identify Platforms field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getLocatorvalue());
		// Get the text of the Platform field
		String PlatformText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				PlatformText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformField").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Platform field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZonePlatformField").getExptext()
					+ "] and the actual return text is[" + PlatformText + "]");
			ReportUtils.setStepDescription(
					"[Platform field text] matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZonePlatformField").getExptext(),
					PlatformText, true);
			m_assert.fail("[Platform field text] matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZonePlatformField").getExptext()
					+ "] and the actual return text is[" + PlatformText + "]");
		}
		// Select 'Specific radio buttons'
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRBSelection")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ADCBTargetingPlatformSpecificPlatformsRBSelection")
						.getLocatorvalue());
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify Targeting - Platforms - "
					+ "Specific Platforms radio button selection");
			ReportUtils
					.setStepDescription(
							"Unable to identify Targeting - Platforms - "
									+ "Specific Platforms radio button selection",
							true);
			m_assert.fail("Unable to identify Targeting - Platforms - Specific Platforms radio button selection");
		}
		// Click at Specific Platforms radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Click operation successful on 'Specific Platforms' radio button");
		// Wait for the Platform selection window displayed
		SeleniumUtils.sleepThread(5);
		// Identify Platform selection window
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindow")
						.getLocatorvalue());
		// If Platform selection window is null then throw the error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window]", true);
			m_assert.fail("Unable to identify [Platform selection window]");
		}
		// Identify Platform selection Radio buttons
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZonePlatformSelectionWindowRadioButtons")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("AdCBTabTargetingZonePlatformSelectionWindowRadioButtons")
								.getLocatorvalue());
		// If Platform selection window radio buttons are null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window radio buttons]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window radio buttons]",
							true);
			m_assert.fail("Unable to identify [Platform selection window radio buttons]");
		}
		// Select the Plaatform radio button as per the input xml file
		boolean isClicked = SeleniumCustomUtils
				.selectRadioButtonInPlatformSelectionInCampaign(element,
						testcaseArgs.get("platformDeviceType"));
		// If Radio button is not clicked then throw the error and exit
		if (!isClicked) {
			// Report in log and console
			logger.error("Unable to click on [Platform selection window radio buttons]");
			ReportUtils
					.setStepDescription(
							"Unable to click on [Platform selection window radio buttons]",
							true);
			m_assert.fail("Unable to click on  [Platform selection window radio buttons]");
		}
		// Wait for the page to load
		SeleniumUtils.sleepThread(3);
		// Identify OS lists
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowOSType")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowOSType")
						.getLocatorvalue());
		// If Platform selection window OS types are null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window OS types]");
			ReportUtils.setStepDescription(
					"Unable to identify [Platform selection window OS types]",
					true);
			m_assert.fail("Unable to identify [Platform selection window OS types]");
		}
		// Select specific OS from OS list as per the input file
		isSelected = SeleniumCustomUtils.selectOSTypeFromList(element,
				testcaseArgs.get("OSType"));
		// Check for the seclection happened
		// if not selected then throw the error and exit
		if (!isSelected) {
			// Report in log and console
			logger.error("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window");
			ReportUtils.setStepDescription("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window", true);
			m_assert.fail("Unable to select OS type ["
					+ testcaseArgs.get("OSType")
					+ "] from the OS list in Platfor Selection window");
		}
		// Identify Done button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZonePlatformSelectionWindowDoneBtn")
						.getLocatorvalue());
		// If Done button is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Platform selection window Done button]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Platform selection window Done button]",
							true);
			m_assert.fail("Unable to identify [Platform selection window Done button]");
		}
		// Click on Done button
		SeleniumUtils.clickOnElement(element);
		// Identify Accuracy element and radio buttons
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getLocatorvalue());
		// Get the text of the Accruacy element
		String AccuracyText = SeleniumUtils.getText(element);
		// Compare the text with expected text from OR
		isTextMatching = SeleniumUtils.assertEqual(
				AccuracyText,
				Suite.objectRepositoryMap.get(
						"AdCBTabTargetingZoneAccuracyText").getExptext());
		// If both texts are not same then throw the error and exit
		if (!isTextMatching) {
			// Report in log and console
			logger.error("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext()
					+ "] and the actual return text is[" + AccuracyText + "]");
			ReportUtils.setStepDescription(
					"[Accuracy] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext(),
					AccuracyText, true);
			m_assert.fail("[Accuracy] text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyText").getExptext()
					+ "] and the actual return text is[" + AccuracyText + "]");
		}
		logger.info("[Accuracy] field text matching successful");
		// Select Accuracy itemls(country, state, Zipcode,Hyperlocal) from the
		// input xml file
		logger.info("Select [Accuracy] radio buttons as per the input..");
		logger.info("Select accuracy [" + testcaseArgs.get("accuracy")
				+ "] from the radio buttons");
		// Check the validations of Accuracy
		if (testcaseArgs.get("accuracy").equalsIgnoreCase("Country")) {
			// Identify Countries selection box
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBox")
									.getLocatorvalue());
			// If Accuracy Countries text box is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Countries text box]");
				ReportUtils.setStepDescription(
						"Unable to identify [Countries text box]", true);
				m_assert.fail("Unable to identify [Countries text box]");
			}
			// Click on the Countries Selection box
			SeleniumUtils.clickOnElement(element);
			// Identfiy Countries selection list
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyCountriesSelectionBoxList")
									.getLocatorvalue());
			// If Countries list is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Countries list element]");
				ReportUtils.setStepDescription(
						"Unable to identify [Countries list element]", true);
				m_assert.fail("Unable to identify [Countries list element]");
			}
			// Select country from country list
			isSelected = SeleniumCustomUtils
					.selectCountryFromCountryListInTargeting(element,
							testcaseArgs.get("country"));
			// If it is not selected then throw the error and exit
			if (!isSelected) {
				// Report in log and console
				logger.error("Unable to select country from country list");
				ReportUtils.setStepDescription(
						"Unable to select country from country list", true);
				m_assert.fail("Unable to select country from country list");
			}
			logger.info("Selection of Country is successful");
			// If user selects State as accuracy option
		} else if (testcaseArgs.get("accuracy").equalsIgnoreCase("State")) {
			// Identify States radio button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesRadioBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesRadioBtn")
							.getLocatorvalue());
			// If States Radio button is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] Radio button");
				ReportUtils.setStepDescription(
						"Unable to identify [States] Radio button", true);
				m_assert.fail("Unable to identify [States] Radio button");
			}
			// Click on Radio button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			// Identify States element and text box
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStates")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStates")
							.getLocatorvalue());
			// If States element is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] element");
				ReportUtils.setStepDescription(
						"Unable to identify [States] element", true);
				m_assert.fail("Unable to identify [States] element");
			}
			// Identify States element text box
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelection")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelection")
							.getLocatorvalue());
			// If States element text box is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] element text box");
				ReportUtils.setStepDescription(
						"Unable to identify [States] element text box", true);
				m_assert.fail("Unable to identify [States] element text box");
			}
			// Click on States element selection
			SeleniumUtils.clickOnElement(element);
			// Identify States selection list
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelectionList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyStatesSelectionList")
							.getLocatorvalue());
			// If States element selection list is null then throw the
			// error
			// and exit
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [States] selections list");
				ReportUtils.setStepDescription(
						"Unable to identify [States] selections list", true);
				m_assert.fail("Unable to identify [States] selections list");
			}
			// Select State from States list
			isSelected = SeleniumCustomUtils
					.selectCountryFromCountryListInTargeting(element,
							testcaseArgs.get("states"));
			// It it is not selected then throw the error and exit
			if (!isSelected) {
				// Report in log and console
				logger.error("Unable to select states ["
						+ testcaseArgs.get("states") + "] from states list");
				ReportUtils.setStepDescription("Unable to select states ["
						+ testcaseArgs.get("states") + "] from states list",
						true);
				m_assert.fail("Unable to select states ["
						+ testcaseArgs.get("states") + "] from states list");
			}
			logger.info("Selection of State from States list is successful");
		} else if (testcaseArgs.get("accuracy").equalsIgnoreCase("Zipcode")) {
			// / Identify Targeting - Hyperlocal Popup
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyHyperlocalWindow")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyHyperlocalWindow")
							.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Pop-up window");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Pop-up window",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Pop-up window");
			}
			// Identify Pop-up window header element
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Pop-up window header element");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Pop-up window header element",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Pop-up window header element");
			}
			logger.info("Identification of Hyperlocal-Pop up window is successful");
			// Get the text of the window
			String WindowText = SeleniumUtils.getText(element);
			logger.info("The Window text is .." + WindowText);
			// Identify Text box element
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowTextbox")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowTextbox")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] textbox");
				ReportUtils.setStepDescription(
						"Unable to identify [Hyperlocal - Targeting ] textbox",
						true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] textbox");
			}
			// Enter Area/Pincode
			SeleniumUtils.type(element, testcaseArgs.get("zipcode"));
			// Identify Search button
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowSearch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowSearch")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Search button");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Search button",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Search button");
			}
			// Click on Search button
			SeleniumUtils.clickOnElement(element);
			// Identify Zip code location information element
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowInfoElement")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowInfoElement")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Information");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Information",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Information");
			}
			// Identify the Graph
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowGraph")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowGraph")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Graph");
				ReportUtils.setStepDescription(
						"Unable to identify [Hyperlocal - Targeting ] Graph",
						true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Graph");
			}
			// Click on Save button
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowDoneBtn")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowDoneBtn")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Done Btn");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Done Btn",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Done Btn");
			}
			// Click on Done button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Verify if the Selected Zip code present in Zip codes text box
			// Click on Save button
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneZipcodetextboxSelectionItem")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneZipcodetextboxSelectionItem")
							.getLocatorvalue());
			// Get the text of the Zip code text box selection item
			String ZipCodeText = SeleniumUtils.getText(element);
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(ZipCodeText,
					testcaseArgs.get("zipcode"));
			if (!isTextMatching) {
				logger.error("Selected Zip code not displayed in Zipcode text box");
				ReportUtils.setStepDescription(
						"Selected Zip code not displayed in Zipcode text box",
						true);
				m_assert.fail("Selected Zip code not displayed in Zipcode text box");
			}
			logger.info("Selected Zip code present in Zip code text box");
			logger.info("Select multiple locations in Zip code");
			// Click on ZipCode Link to open Hyperlocal window
			// Identify Zip code link
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap.get(
									"AdCBTabTargetingZoneZipcodeLink")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"AdCBTabTargetingZoneZipcodeLink")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify Zip code link");
				ReportUtils.setStepDescription(
						"Unable to identify Zip code link", true);
				m_assert.fail("Unable to identify Zip code link");
			}
			// Click on Zip code link
			SeleniumUtils.clickOnElement(element);
			// Wait for the Hyperlocal-window to open
			SeleniumUtils.sleepThread(4);
			// Identify Hypler-local window opens
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyHyperlocalWindow")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdCBTabTargetingZoneAccuracyHyperlocalWindow")
							.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Pop-up window");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Pop-up window",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Pop-up window");
			}
			// Click on Reset button
			// Identify Reset button
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowResetBtn")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowResetBtn")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Reset button");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Reset button",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Reset button");
			}
			// Click on Reset button
			SeleniumUtils.clickOnElement(element);
			// Wait for the data to be reset
			SeleniumUtils.sleepThread(2);
			// Select Multiple locations radio button
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZipcodeHyperlocalWindowMultipleLocationsRadio")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZipcodeHyperlocalWindowMultipleLocationsRadio")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Multiple Locations Radio button");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Multiple Locations Radio button",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Multiple Locations Radio button");
			}
			// Click on Multiple Radio button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			// Idenitfy Multiple Text Area
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZipcodeHyperlocalWindowMultipleLocationstext")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZipcodeHyperlocalWindowMultipleLocationstext")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Multiple Locations Text Area");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Multiple Locations Text Area",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Multiple Locations Text Area");
			}
			// Type first Zipcode
			SeleniumUtils.type(element, testcaseArgs.get("zipcodeMulti1"));
			boolean isEntered = SeleniumUtils.PressEnterKey(element);
			if (!isEntered) {
				logger.warn("Enter Key is not performed");
			}
			SeleniumUtils.type(element, testcaseArgs.get("zipcodeMulti2"));
			// Identify Search button
			// Idenitfy Multiple Text Area
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZipcodeHyperlocalWindowMultipleSearchBtn")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZipcodeHyperlocalWindowMultipleSearchBtn")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Multiple Locations Search Button");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Multiple Locations Search Button",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Multiple Locations Search Button");
			}
			// Click on Search button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Click on Done button
			// Click on Save button
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowDoneBtn")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowDoneBtn")
									.getLocatorvalue());
			// If window is not present throw the error
			if (element == null) {
				// Report in log and console
				logger.error("Unable to identify [Hyperlocal - Targeting ] Done Btn");
				ReportUtils
						.setStepDescription(
								"Unable to identify [Hyperlocal - Targeting ] Done Btn",
								true);
				m_assert.fail("Unable to identify [Hyperlocal - Targeting ] Done Btn");
			}
			// Click on Done button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		} else if (testcaseArgs.get("accuracy").equalsIgnoreCase("Hyperlocal")) {
			// Identify Targeting-Hyperlocal popup window text
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getLocatorvalue());
			// Get the text of the Targeting-Hyperlocal popup header element
			String HyperlocalPopUpText = SeleniumUtils.getText(element);
			// Compare the text with expected text from OR
			isTextMatching = SeleniumUtils
					.assertEqual(
							HyperlocalPopUpText,
							Suite.objectRepositoryMap
									.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
									.getExptext());
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				// Report in log and console
				logger.error("[Targeting-Hyperlocal popup header element] text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
								.getExptext()
						+ "] and the actual return text is["
						+ HyperlocalPopUpText + "]");
				ReportUtils
						.setStepDescription(
								"[Targeting-Hyperlocal popup header element] text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
										.getExptext(), HyperlocalPopUpText,
								true);
				m_assert.fail("[Targeting-Hyperlocal popup header element] text matching failed. The expected text is ["
						+ Suite.objectRepositoryMap
								.get("AdCBTabTargetingZoneAccuracyHyperlocalWindowPopupHeaderText")
								.getExptext()
						+ "] and the actual return text is["
						+ HyperlocalPopUpText + "]");
			}
			logger.info("Identification of [Targeting-Hyperlocal] popup header element is successful");
		}
		// Click on Done button
		logger.info("Identify Done button");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdCBTabDoneBtn")
						.getLocatorvalue());
		// If Hyperlocal popup header element is null then throw the
		// error
		// and exit
		if (element == null) {
			// Report in log and console
			logger.error("Unable to identify [Done] button in [Edit Campaign] page");
			ReportUtils.setStepDescription(
					"Unable to identify [Done] button in [Edit Campaign] page",
					true);
			m_assert.fail("Unable to identify [Done] button in [Edit Campaign] page");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		m_assert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = "loginAs")
	public void verifyConfiguration() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyConfiguration")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyConfiguration] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyConfiguration] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyConfiguration] is not added for execution");
		}
		// read param args
		testcaseArgs = getTestData("verifyConfiguration");
		logger.info("Starting [verifyConfiguration] execution");
		logger.info("Verify if user is on [Sites] tab");
		// Identify Sites tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Idetnfiy Sites tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");

				m_assert.fail("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Sites tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] sub tab header text matching failed. "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext() + "] and the return text is ["
						+ SitesSubTabHeaderText + "]");

				m_assert.fail("[Sites] sub tab header text matching failed. "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext() + "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Sites] tab");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(6);
		// Identify Application list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list in [Sites] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify applications list in [Sites] tab",
							true);
			m_assert.fail("Unable to identify applications list in [Sites] tab");
		}
		// Identify specific application
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Click on Configuration link
		boolean isClicked = SeleniumCustomUtils.clickAtConfigurationInAdSites(
				element, testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Configuration' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application actions 'Configuration' "
							+ "link for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Configuration' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		logger.info("Clicked on Configuration link of application ["
				+ testcaseArgs.get("application") + "]");
		// Identify Allow Delivery of Ads element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowDeliveryAdsText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowDeliveryAdsText")
						.getLocatorvalue());
		// Get the text
		String AllowDeleveryAdsText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAllowDeleveryAdsText = Suite.objectRepositoryMap.get(
				"SitesConfigurationAllowDeliveryAdsText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(AllowDeleveryAdsText,
				ExpAllowDeleveryAdsText);
		if (!isTextMatching) {
			logger.error("[Configuration - Allow Delivery Ads] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpAllowDeleveryAdsText + "] and "
					+ "the return text is [" + AllowDeleveryAdsText + "]");
			ReportUtils.setStepDescription(
					"[Configuration - Allow Delivery Ads] element "
							+ "text matching failed", "",
					ExpAllowDeleveryAdsText, AllowDeleveryAdsText, true);
			m_assert.fail("[Configuration - Allow Delivery Ads] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpAllowDeleveryAdsText + "] and "
					+ "the return text is [" + AllowDeleveryAdsText + "]");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowDeliveryAdsStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowDeliveryAdsStatus")
						.getLocatorvalue());
		// Identify if Allow Delivery Ads is enabled or disabled
		String isAllowDeliveryAds_Enabled = SeleniumUtils.getAttributeValue(
				element, "checked");
		if (isAllowDeliveryAds_Enabled == null) {
			logger.info("[Configuration - Allow Delivery Ads] are disabled");
			logger.info("Enable [Configuration - Allow Delivery Ads]");
			// Identify Allow Delivery of Ads element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"SitesConfigurationAllowDeliveryAdsOnOFFSwitch")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"SitesConfigurationAllowDeliveryAdsOnOFFSwitch")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Configuration - Allow Delivery Ads] "
						+ "On-Off Slider");
				ReportUtils.setStepDescription(
						"Unable to identify [Configuration - Allow Delivery Ads] "
								+ "On-Off Slider", true);
				m_assert.fail("Unable to identify [Configuration - Allow Delivery Ads] "
						+ "On-Off Slider");
			}
			// Click on Allow Delivery Ads element
			SeleniumUtils.clickOnElement(element);
		}
		// Identify Allow Third Party Ads
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowThirdPartyAdsText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowThirdPartyAdsText")
						.getLocatorvalue());
		// Get the text
		String AllowThirdPartyAdsText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpAllowThirdPartyAdsText = Suite.objectRepositoryMap.get(
				"SitesConfigurationAllowThirdPartyAdsText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(AllowThirdPartyAdsText,
				ExpAllowThirdPartyAdsText);
		if (!isTextMatching) {
			logger.error("[Configuration - Allow ThirdParty Ads] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpAllowThirdPartyAdsText + "] and "
					+ "the return text is [" + AllowThirdPartyAdsText + "]");
			ReportUtils.setStepDescription(
					"[Configuration - Allow ThirdParty Ads] element "
							+ "text matching failed", "",
					ExpAllowThirdPartyAdsText, AllowThirdPartyAdsText, true);
			m_assert.fail("[Configuration - Allow ThirdParty Ads] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpAllowThirdPartyAdsText + "] and "
					+ "the return text is [" + AllowThirdPartyAdsText + "]");
		}
		// Identify Allow Third Party Ads Slider
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowThirdPartyAdsStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"SitesConfigurationAllowThirdPartyAdsStatus")
						.getLocatorvalue());
		// Identify if Allow Delivery Ads is enabled or disabled
		String isAllowThirdPartyAds_Enabled = SeleniumUtils.getAttributeValue(
				element, "checked");
		if (isAllowThirdPartyAds_Enabled == null) {
			logger.info("[Configuration - Allow ThirdParty Ads] are disabled");
			logger.info("Enable [Configuration - Allow ThirdParty Ads]");
			// Identify Allow Third Party Ads Slider
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"SitesConfigurationAllowThirdPartyAdsSlider")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"SitesConfigurationAllowThirdPartyAdsSlider")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [Configuration - Allow ThirdParty Ads] "
						+ "On-Off Slider");
				ReportUtils.setStepDescription(
						"Unable to identify [Configuration - Allow ThirdParty Ads] "
								+ "On-Off Slider", true);
				m_assert.fail("Unable to identify [Configuration - Allow ThirdParty Ads] "
						+ "On-Off Slider");
			}
			// Click on Allow Delivery Ads element
			SeleniumUtils.clickOnElement(element);
		}
		// Identify Channel element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormChannelText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormChannelText")
						.getLocatorvalue());
		// Get the text
		String ChannelsText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpChannelsText = Suite.objectRepositoryMap.get(
				"AdSitesTabConfigurationFormChannelText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(ChannelsText,
				ExpChannelsText);
		if (!isTextMatching) {
			logger.error("[Configuration - Channel/Category] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpChannelsText + "] and " + "the return text is ["
					+ ChannelsText + "]");
			ReportUtils.setStepDescription(
					"[Configuration - Channel/Category] element "
							+ "text matching failed", "", ExpChannelsText,
					ChannelsText, true);
			m_assert.fail("[Configuration - Channel/Category] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpChannelsText + "] and " + "the return text is ["
					+ ChannelsText + "]");
		}
		// Identify Channel Select box
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormChannelSelectbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormChannelSelectbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify '[Configuration - "
					+ "Channel/Category]' select box");
			ReportUtils.setStepDescription(
					"Unable to identify '[Configuration - "
							+ "Channel/Category]' select box", true);
			m_assert.fail("Unable to identify '[Configuration - "
					+ "Channel/Category]' select box");
		}
		// select spcific channel as per the input
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormChannelSelectbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormChannelSelectbox")
						.getLocatorvalue(), testcaseArgs.get("channel"));
		if (!isSelected) {
			logger.error("Unable to identify select channel ["
					+ testcaseArgs.get("channel")
					+ "] from Channel select box under Configuration link");

			m_assert.fail("Unable to identify select channel ["
					+ testcaseArgs.get("channel")
					+ "] from Channel select box under Configuration link");
		}
		// Identify Estimated Audience Gender text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormGender").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormGender").getLocatorvalue());
		// Get the text
		String EstimatedGenderText = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpEstimatedGenderText = Suite.objectRepositoryMap.get(
				"AdSitesTabConfigurationFormGender").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(EstimatedGenderText,
				ExpEstimatedGenderText);
		if (!isTextMatching) {
			logger.error("[Configuration - Estimated Audience Gender] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpEstimatedGenderText + "] and "
					+ "the return text is [" + EstimatedGenderText + "]");
			ReportUtils.setStepDescription(
					"[Configuration - Estimated Audience Gender] element "
							+ "text matching failed", "",
					ExpEstimatedGenderText, EstimatedGenderText, true);
			m_assert.fail("[Configuration - Estimated Audience Gender] element "
					+ "text matching failed. "
					+ "The expected text is ["
					+ ExpEstimatedGenderText
					+ "] and "
					+ "the return text is [" + EstimatedGenderText + "]");
		}
		// Identify Gender Slider
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormGenderSlider")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormGenderSlider")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Gender Selection Slider' "
					+ "under 'Configuration' link");
			ReportUtils.setStepDescription(
					"Unable to identify 'Gender Selection Slider' "
							+ "under 'Configuration' link", true);
			m_assert.fail("Unable to identify 'Gender Selection Slider' "
					+ "under 'Configuration' link");
		}
		// Scroll the Slider as per the input
		boolean isScrolled = SeleniumCustomUtils.scrollSlider(element,
				testcaseArgs.get("maleGenderSelect"));
		if (!isScrolled) {
			logger.error("Unable to identify scroll 'Gender Selection Slider' "
					+ "under 'Configuration' link");
			ReportUtils.setStepDescription(
					"Unable to identify scroll 'Gender Selection Slider' "
							+ "under 'Configuration' link", true);
			m_assert.fail("Unable to identify scroll 'Gender Selection Slider' "
					+ "under 'Configuration' link");
		}
		// Identify Estimated Age text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormEstimatedAgeText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormEstimatedAgeText")
						.getLocatorvalue());
		// Get the text
		String EstimatedAge = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpEstimatedAge = Suite.objectRepositoryMap.get(
				"AdSitesTabConfigurationFormEstimatedAgeText").getExptext();
		isTextMatching = SeleniumUtils.assertEqual(EstimatedAge,
				ExpEstimatedAge);
		if (!isTextMatching) {
			logger.error("[Configuration - Estimated Age Range] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpEstimatedAge + "] and " + "the return text is ["
					+ EstimatedAge + "]");
			ReportUtils.setStepDescription(
					"[Configuration - Estimated Age Range] element "
							+ "text matching failed", "", ExpEstimatedAge,
					EstimatedAge, true);
			m_assert.fail("[Configuration - Estimated Age Range] element "
					+ "text matching failed. " + "The expected text is ["
					+ ExpEstimatedAge + "] and " + "the return text is ["
					+ EstimatedAge + "]");
		}
		// Identify Age Lower dropdown
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabConfigurationFormAgeLower")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabConfigurationFormAgeLower")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Target Age Group Lower' under"
					+ " 'Configuration' link");
			ReportUtils.setStepDescription(
					"Unable to identify 'Target Age Group Lower'"
							+ " under 'Configuration' link", true);
			m_assert.fail("Unable to identify 'Target Age Group Lower'"
					+ " under 'Configuration' link");
		}
		// Select Age as per the input
		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get(
								"AdSitesTabConfigurationFormAgeLower")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabConfigurationFormAgeLower")
								.getLocatorvalue(), testcaseArgs
								.get("ageLower"));
		if (!isSelected) {
			logger.error("Unable to select 'Target Age Group Lower'"
					+ " under 'Configuration' link");
			ReportUtils.setStepDescription(
					"Unable to select 'Target Age Group Lower'"
							+ " under 'Configuration' link", true);

			m_assert.fail("Unable to select 'Target Age Group Lower'"
					+ " under 'Configuration' link");
		}
		// Identify Age Uppper drop down
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabConfigurationFormAgeUpper")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabConfigurationFormAgeUpper")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Target Age Group Upper'"
					+ " under 'Configuration' link");
			ReportUtils.setStepDescription(
					"Unable to identify 'Target Age Group Upper'"
							+ " under 'Configuration' link", true);

			m_assert.fail("Unable to identify 'Target Age Group Upper'"
					+ " under 'Configuration' link");
		}
		// Select Age upper as per the input
		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get(
								"AdSitesTabConfigurationFormAgeUpper")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabConfigurationFormAgeUpper")
								.getLocatorvalue(), testcaseArgs
								.get("ageUpper"));
		if (!isSelected) {
			logger.error("Unable to select 'Target Age Group Upper'"
					+ " under 'Configuration' link");
			ReportUtils.setStepDescription(
					"Unable to select 'Target Age Group Upper'"
							+ " under 'Configuration' link", true);
			m_assert.fail("Unable to select 'Target Age Group Upper'"
					+ " under 'Configuration' link");
		}
		// Allow HTML Ads
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowHTMLText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowHTMLText")
						.getLocatorvalue());
		// Get the text
		String allowHtmlText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				allowHtmlText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowHTMLText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Allow HTML Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowHTMLText")
							.getExptext()
					+ "] and the actual return text is["
					+ allowHtmlText + "]");
			ReportUtils.setStepDescription(
					"'Allow HTML Ads' label text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowHTMLText")
							.getExptext(), allowHtmlText, true);

			m_assert.fail("'Allow HTML Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowHTMLText")
							.getExptext()
					+ "] and the actual return text is["
					+ allowHtmlText + "]");
		}
		// Identify If Allow Html is Enabled or disabled
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ConfigurationAllowHTMLStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ConfigurationAllowHTMLStatus")
						.getLocatorvalue());
		// Get the checked property
		String isAllowHtml_Enabled = SeleniumUtils.getText(element);
		if (isAllowHtml_Enabled == null) {
			// Identify Allow Html OnOff Switch button
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowHTMLTextOnOFFSwitch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowHTMLTextOnOFFSwitch")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Allow HTML Ads' Switch element");
				ReportUtils.setStepDescription(
						"Unable to identify 'Allow HTML Ads'"
								+ " On/OFF Switch element", true);
				m_assert.fail("Unable to identify 'Allow HTML Ads'"
						+ " On/OFF Switch element");
			}
			// Enable Allow HTML Ads
			SeleniumUtils.clickOnElement(element);
		}
		// Allow Java Script Ads
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowJavaScriptText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowJavaScriptText")
						.getLocatorvalue());
		// Get the text
		String allowJavaScriptText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				allowJavaScriptText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowJavaScriptText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Allow JavaScript Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowJavaScriptText")
							.getExptext()
					+ "] and the actual return text is["
					+ allowJavaScriptText + "]");
			ReportUtils.setStepDescription(
					"'Allow JavaScript Ads' label text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowJavaScriptText")
							.getExptext(), allowJavaScriptText, true);
			m_assert.fail("'Allow JavaScript Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowJavaScriptText")
							.getExptext()
					+ "] and the actual return text is["
					+ allowJavaScriptText + "]");
		}
		// Identify JavaScripts Element Enabled or Disabled
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowJavaScriptStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowJavaScriptStatus")
						.getLocatorvalue());
		// Get the checked property
		String isAllowJavaScripts_Enabled = SeleniumUtils.getText(element);
		if (isAllowJavaScripts_Enabled == null) {
			// Identify Allow JavaScripts On/Off Switch
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowJavaScriptOnOFFSwitch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowJavaScriptOnOFFSwitch")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Allow JavaScript Ads' Switch element");
				ReportUtils.setStepDescription(
						"Unable to identify 'Allow JavaScript Ads' "
								+ "Switch element", true);
				m_assert.fail("Unable to identify 'Allow JavaScript Ads' Switch element");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
		}
		// Allow Rich Media Ads
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowRichMediaText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowRichMediaText")
						.getLocatorvalue());
		// Get the text
		String allowRichMediaText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				allowRichMediaText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowRichMediaText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Allow Rich Media Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowRichMediaText")
							.getExptext()
					+ "] and the actual return text is["
					+ allowRichMediaText + "]");
			ReportUtils.setStepDescription(
					"'Allow Rich Media Ads' label text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowRichMediaText")
							.getExptext(), allowRichMediaText, true);

			m_assert.fail("'Allow Rich Media Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormAllowRichMediaText")
							.getExptext()
					+ "] and the actual return text is["
					+ allowRichMediaText + "]");
		}
		// Identify Allow Rich Media Ads Enabled or Disabled
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowRichMediaStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowRichMediaStatus")
						.getLocatorvalue());
		// Get the checked property
		String isAllowRichMediaAds_Enabled = SeleniumUtils.getText(element);
		if (isAllowRichMediaAds_Enabled == null) {
			// Identify Allow Rich Media On/Off switch
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowRichMediaOnOFFSwitch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowRichMediaOnOFFSwitch")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Allow Rich Media Ads'"
						+ " Switch element");
				ReportUtils.setStepDescription(
						"Unable to identify 'Allow Rich Media Ads'"
								+ " Switch element", true);
				m_assert.fail("Unable to identify 'Allow Rich Media Ads'"
						+ " Switch element");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
		}
		// Location Information
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormLocationInfoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormLocationInfoText")
						.getLocatorvalue());
		// Get the text
		String locationInformationText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				locationInformationText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormLocationInfoText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Location Information' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormLocationInfoText")
							.getExptext()
					+ "] and the actual return text is["
					+ locationInformationText + "]");
			ReportUtils.setStepDescription(
					"'Location Information' label text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormLocationInfoText")
							.getExptext(), locationInformationText, true);
			m_assert.fail("'Location Information' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormLocationInfoText")
							.getExptext()
					+ "] and the actual return text is["
					+ locationInformationText + "]");
		}
		// Identify Location Information status Enabled or Disabled
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormLocationInfoStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormLocationInfoStatus")
						.getLocatorvalue());
		// Get the checked property
		String isLocationInformation_Enabled = SeleniumUtils.getText(element);
		if (isLocationInformation_Enabled == null) {
			// Identify Location On/Off switch
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormLocationInfoOnOFFSwitch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormLocationInfoOnOFFSwitch")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Location Information' Switch element");
				ReportUtils
						.setStepDescription(
								"Unable to identify 'Location Information' Switch element",
								true);
				m_assert.fail("Unable to identify 'Local Information' Switch element");
			}
			// Click
			SeleniumUtils.clickOnElement(element);
		}
		// MRAID Complaint Ads
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormMRAIDComplaintText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormMRAIDComplaintText")
						.getLocatorvalue());
		// Get the text
		String mraidComplaintsText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				mraidComplaintsText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormMRAIDComplaintText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'MRAID Complaint Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormMRAIDComplaintText")
							.getExptext()
					+ "] and the actual return text is["
					+ mraidComplaintsText + "]");
			ReportUtils.setStepDescription(
					"'MRAID Complaint Ads' label text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormMRAIDComplaintText")
							.getExptext(), mraidComplaintsText, true);
			m_assert.fail("'MRAID Complaint Ads' label text matching failed. The expected text ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormMRAIDComplaintText")
							.getExptext()
					+ "] and the actual return text is["
					+ mraidComplaintsText + "]");
		}
		// Identify MRAID Complaints Ads status
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormMRAIDComplaintStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormMRAIDComplaintStatus")
						.getLocatorvalue());
		// Get the checked property
		String isMRAIDComplaints_Enabled = SeleniumUtils.getText(element);
		if (isMRAIDComplaints_Enabled == null) {
			// MRAID Complaint On/Off Switch
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormMRAIDComplaintOnOFFSwitch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormMRAIDComplaintOnOFFSwitch")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'MRAID Complaint Ads'"
						+ " Switch element");
				ReportUtils.setStepDescription(
						"Unable to identify 'MRAID Complaint Ads'"
								+ " Switch element", true);
				m_assert.fail("Unable to identify 'MRAID Complaint Ads' Switch element");
			}
			SeleniumUtils.clickOnElement(element);
		}
		// Allow CPI Campaigns
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowCPICampaingsText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowCPICampaingsText")
						.getLocatorvalue());
		// Identify Allow CPI Campaigns status
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowCPICampaingsStatus")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormAllowCPICampaingsStatus")
						.getLocatorvalue());
		// Get the checked property
		String isAllowCPICampaigns_Enabled = SeleniumUtils.getText(element);
		if (isAllowCPICampaigns_Enabled == null) {
			// MRAID Complaint On/Off Switch
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowCPICampaingsOnOffSwitch")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowCPICampaingsOnOffSwitch")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'Allow CPI Campaigns'"
						+ " Switch element");
				ReportUtils.setStepDescription(
						"Unable to identify 'Allow CPI Campaigns'"
								+ " Switch element", true);
				m_assert.fail("Unable to identify 'Allow CPI Campaigns'"
						+ " Switch element");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify Text box
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowCPICampaingsTextboxInput")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabConfigurationFormAllowCPICampaingsTextboxInput")
									.getLocatorvalue());
			// Clear the text
			SeleniumUtils.clearText(element);
			// Enter CPI value
			SeleniumUtils.type(element, testcaseArgs.get("MinimumCPI"));
		}
		// CPC Floor
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPCFloorTextboxIsExist")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPCFloorTextboxIsExist")
						.getLocatorvalue());
		String CPCFloorclassText = SeleniumUtils.getAttributeValue(element,
				"class").trim();
		if (!CPCFloorclassText.equalsIgnoreCase("cpc checkbox-input")) {
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormCPCFloorOnOffSwitch")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormCPCFloorOnOffSwitch")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPCFloorTextboxInput")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPCFloorTextboxInput")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		SeleniumUtils.type(element, testcaseArgs.get("MinimumCPC"));
		// CPM Floor
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPMFloorTextboxIsExist")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPMFloorTextboxIsExist")
						.getLocatorvalue());
		String CPMFloorclassText = SeleniumUtils.getAttributeValue(element,
				"class").trim();
		if (!CPMFloorclassText.equalsIgnoreCase("cpm checkbox-input")) {
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormCPMFloorOnOffSwitch")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdSitesTabConfigurationFormCPMFloorOnOffSwitch")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'CPM Floor' Switch element");
				ReportUtils.setStepDescription(
						"Unable to identify 'CPM Floor' Switch element", true);
				m_assert.fail("Unable to identify 'CPM Floor' Switch element");
			}
			SeleniumUtils.clickOnElement(element);
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPMFloorTextboxInput")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCPMFloorTextboxInput")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Minimum CPM' textbox element");
			ReportUtils.setStepDescription(
					"Unable to identify 'Minimum CPM' textbox element", true);
			m_assert.fail("Unable to identify 'Minimum CPM' textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.type(element, testcaseArgs.get("MinimumCPM"));
		// Verify Save & Cancel buttons
		logger.info("Verify 'SAVE' & 'CANCEL' buttons");
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabConfigurationFormCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Cancel' button");
			ReportUtils.setStepDescription(
					"Unable to identify 'Cancel' button", true);
			m_assert.fail("Unable to identify 'Cancel' button");
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabConfigurationFormSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabConfigurationFormSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button");
			ReportUtils.setStepDescription("Unable to identify 'Save' button",
					true);
			m_assert.fail("Unable to identify 'Save' button");
		}
		logger.info("Verification of 'SAVE' & 'CANCEL' buttons successful");
		// click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(4);
		m_assert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "loginAs")
	public void verifyAdPlacements() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyAdPlacements")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyAdPlacements] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyAdPlacements] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyAdPlacements] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("verifyAdPlacements");
		logger.info("Starting [verifyAdPlacements] execution");
		logger.info("Verify if user is on [Sites] tab");
		SeleniumUtils.sleepThread(3);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Identify Sites tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext() + "] and the return text is ["
						+ SitesTabText + "]");
				ReportUtils.setStepDescription(
						"[Sites] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext(), SitesTabText, true);
				m_assert.fail("[Sites] tab text matching failed. "
						+ "The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext() + "] and the return text is ["
						+ SitesTabText + "]");
			}
			// Click on Sites tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Sites] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext(), SitesSubTabHeaderText, true);

				m_assert.fail("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Sites] tab");
		logger.info("Identify application list in [Sites] tab");
		// Identify Application list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list "
					+ "in [Sites] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify applications list in [Sites] tab",
							true);
			m_assert.fail("Unable to identify applications list in [Sites] tab");
		}
		logger.info("Identification of application list in "
				+ "[Sites] tab is successful");
		logger.info("Verify if application present in application list");
		// Verify the application
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		logger.info("application is present in application list");
		logger.info("Click on [Ad Placements] of application ["
				+ testcaseArgs.get("application") + "]");
		// Click on Ad Placements link
		boolean isClicked = SeleniumCustomUtils.clickAtAdPlacementsInAdSites(
				element, testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Ad Placements'"
					+ " link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application actions 'Ad Placements'"
							+ " link for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Ad Placements'"
					+ " link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		logger.info("Click on [Ad Placements] of application ["
				+ testcaseArgs.get("application") + "] is successful");
		// Verify Add Zone Form
		logger.info("Identification of Length dropdown");
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormLengthDD")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormLengthDD")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Ad Placements Length Dropdown]");
			ReportUtils.setStepDescription(
					"Unable to identify [Ad Placements Length Dropdown]", true);
			m_assert.fail("Unable to identify [Ad Placements Length Dropdown]");
		}
		logger.info("Identificaion of Length dropdown is successful");
		logger.info("Select length from lenght dropdown");
		// Select Number of Ads per page
		boolean isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormLengthDD")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormLengthDD")
								.getLocatorvalue(), "25");
		if (!isSelected) {
			logger.error("Unable to select [25] entries in Ad Placements "
					+ "Length dropdown");
			ReportUtils.setStepDescription("Unable to select [25] entries "
					+ "in Ad Placements Length dropdown", true);
			m_assert.fail("Unable to select [25] entries in Ad Placements "
					+ "Length dropdown");
		}
		SeleniumUtils.sleepThread(2);
		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormLengthDD")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormLengthDD")
								.getLocatorvalue(), "50");
		SeleniumUtils.sleepThread(2);
		if (!isSelected) {
			logger.error("Unable to select [50] entries in "
					+ "Ad Placements Length dropdown");
			ReportUtils.setStepDescription("Unable to select [50] entries "
					+ "in Ad Placements Length dropdown", true);
			m_assert.fail("Unable to select [50] entries in "
					+ "Ad Placements Length dropdown");
		}
		SeleniumUtils.sleepThread(2);
		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormLengthDD")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormLengthDD")
								.getLocatorvalue(), "10");
		if (!isSelected) {
			logger.error("Unable to select [10] entries in "
					+ "Ad Placements Length dropdown");
			ReportUtils.setStepDescription("Unable to select [10] entries "
					+ "in Ad Placements Length dropdown", true);
			m_assert.fail("Unable to select [10] entries in "
					+ "Ad Placements Length dropdown");
		}
		logger.info("Identify Search text box");
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormSearchBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormSearchBox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Ad Placements Search box]");
			ReportUtils.setStepDescription(
					"Unable to identify [Ad Placements Search box]", true);
			m_assert.fail("Unable to identify [Ad Placements Search box]");
		}
		logger.info("Identification of Search text box is successful");
		// Clear the text
		logger.info("Clear the text in Search text box");
		SeleniumUtils.clearText(element);
		logger.info("Verify Search box functionlaity");
		SeleniumUtils.type(element, testcaseArgs.get("AdPlacementsSearchText"));
		// Identify Ads table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify " + "[Ad Placements table]");
			ReportUtils.setStepDescription("Unable to identify "
					+ "[Ad Placements table]", true);
			m_assert.fail("Unable to identify [Ad Placements table]");
		}
		// Return First row Placement Id from the table
		logger.info("Retrieving First row Placement Id from the Ad Placement table");
		String Placement_Id = SeleniumCustomUtils
				.get_PlacementId_From_AdPlacement_Table(element);
		logger.info("Verify Search functionality against Search text box");
		// Verify the PlacementId is displayed in the First row of Ad Placement
		// Table
		boolean isVerified = SeleniumCustomUtils
				.check_Search_Functionality_Against_SearchBox_In_AdPlacement_Table(
						element, Placement_Id);
		if (!isVerified) {
			logger.warn("Search functionality fails at Search texbox in "
					+ "Ad Placement Table. " + "The Placement Id ["
					+ Placement_Id
					+ "] is not displayed at First row in Ad Placement Table");
			ReportUtils.setStepDescription(
					"Search functionality fails at Search texbox in "
							+ "Ad Placement Table. " + "The Placement Id ["
							+ Placement_Id
							+ "] is not displayed at First row in "
							+ "Ad Placement Table", true);
			m_assert.fail("Search functionality fails at Search texbox in "
					+ "Ad Placement Table. " + "The Placement Id ["
					+ Placement_Id
					+ "] is not displayed at First row in Ad Placement Table");
		}
		logger.info("Searching functionality is working");
		// Identify Form search box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormSearchBox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormSearchBox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify " + "[Ad Placements Search box]");
			ReportUtils.setStepDescription("Unable to identify "
					+ "[Ad Placements Search box]", true);
			m_assert.fail("Unable to identify " + "[Ad Placements Search box]");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(2);
		// Identify Application list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list in [Sites] tab");
			ReportUtils.setStepDescription("Unable to identify applications "
					+ "list in [Sites] tab", true);
			m_assert.fail("Unable to identify applications list in [Sites] tab");
		}
		// Verify if application present in the list
		isAppPresent = SeleniumCustomUtils.checkApplicationNameInAdSites(
				element, testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Click at Ad Placement link
		isClicked = SeleniumCustomUtils.clickAtAdPlacementsInAdSites(element,
				testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Ad Placements'"
					+ " link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application actions 'Ad Placements'"
							+ " link for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Ad Placements'"
					+ " link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Verify the Table
		logger.info("Verify the table header elements");
		// Identify Header Zone
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getLocatorvalue());
		// Get the text
		String ZoneText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				ZoneText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Zone Id] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderZone")
							.getExptext() + "] and the return text is ["
					+ ZoneText + "]");
			ReportUtils.setStepDescription(
					"table header [Zone Id] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderZone")
							.getExptext(), ZoneText, true);
			m_assert.fail("table header [Zone Id] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderZone")
							.getExptext() + "] and the return text is ["
					+ ZoneText + "]");
		}
		// Name header
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getLocatorvalue());
		// Get the text
		String NameText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NameText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Name] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderName")
							.getExptext() + "] and the return text is ["
					+ NameText + "]");
			ReportUtils.setStepDescription(
					"table header [Name] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderName")
							.getExptext(), NameText, true);
			m_assert.fail("table header [Name] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderName")
							.getExptext() + "] and the return text is ["
					+ NameText + "]");
		}
		// Media
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderMedia")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderMedia")
						.getLocatorvalue());
		// Get the text
		String MediaText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				MediaText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderMedia")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Media] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderMedia")
							.getExptext() + "] and the return text is ["
					+ MediaText + "]");
			ReportUtils.setStepDescription(
					"table header [Media] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderMedia")
							.getExptext(), MediaText, true);
			m_assert.fail("table header [Media] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderMedia")
							.getExptext() + "] and the return text is ["
					+ MediaText + "]");
		}
		// Type/size
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderTypeSize")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderTypeSize")
						.getLocatorvalue());
		// Get the text
		String TypesizeText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				TypesizeText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderTypeSize")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Type/Size] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderTypeSize")
							.getExptext() + "] and the return text is ["
					+ TypesizeText + "]");
			ReportUtils.setStepDescription(
					"table header [Type/Size] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderTypeSize")
							.getExptext(), TypesizeText, true);

			m_assert.fail("table header [Type/Size] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderTypeSize")
							.getExptext() + "] and the return text is ["
					+ TypesizeText + "]");
		}
		// Delivery
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderDelivery")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderDelivery")
						.getLocatorvalue());
		// Get the text
		String DeliveryText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				DeliveryText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderDelivery")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Delivery] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderDelivery")
							.getExptext() + "] and the return text is ["
					+ DeliveryText + "]");
			ReportUtils.setStepDescription(
					"table header [Delivery] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderDelivery")
							.getExptext(), DeliveryText, true);

			m_assert.fail("table header [Delivery] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderDelivery")
							.getExptext() + "] and the return text is ["
					+ DeliveryText + "]");
		}
		// Edit
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderEdit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderEdit")
						.getLocatorvalue());
		// Get the text
		String EditText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				EditText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderEdit")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Edit] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderEdit")
							.getExptext() + "] and the return text is ["
					+ EditText + "]");
			ReportUtils.setStepDescription(
					"table header [Edit] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderEdit")
							.getExptext(), EditText, true);

			m_assert.fail("table header [Edit] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderEdit")
							.getExptext() + "] and the return text is ["
					+ EditText + "]");
		}
		// Remove
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderRemove")
						.getLocatorvalue());
		// Get the text
		String RemoveText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				RemoveText,
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderRemove")
						.getExptext());
		if (!isTextMatching) {
			logger.error("table header [Remove] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderRemove")
							.getExptext() + "] and the return text is ["
					+ RemoveText + "]");
			ReportUtils.setStepDescription(
					"table header [Remove] text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderRemove")
							.getExptext(), RemoveText, true);

			m_assert.fail("table header [Remove] text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormtableHeaderRemove")
							.getExptext() + "] and the return text is ["
					+ RemoveText + "]");
		}
		logger.info("Verify Sorting functionality on [Zone Id] header");
		// sorting functionality for Zone Id header
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Ad Placements table]");
			ReportUtils.setStepDescription("Unable to identify "
					+ "[Ad Placements table]", true);
			m_assert.fail("Unable to identify [Ad Placements table]");
		}
		// Get number of Rows
		int numberOfRows = SeleniumUtils.getCountFromTable(element);
		Random randomGenerator = new Random();
		int randomInt = 0;
		for (int i = 1; i <= 4; i++) {
			randomInt = randomGenerator.nextInt(numberOfRows);
			if (randomInt > 1 && randomInt < 5) {
				break;
			}
		}
		int Number_RandomGenerated = SeleniumUtils.getTextFromTable(element,
				randomInt, 1);
		int Number_RandomGenBefore = SeleniumUtils.getTextFromTable(element,
				randomInt - 1, 1);
		int Number_RandomGenAfter = SeleniumUtils.getTextFromTable(element,
				randomInt + 1, 1);
		if (Number_RandomGenerated < Number_RandomGenBefore
				|| Number_RandomGenerated > Number_RandomGenAfter) {
			logger.error("Sorting down functionaltiy fails at Zone header");
			ReportUtils.setStepDescription(
					"Sorting down functionaltiy fails at Zone header", true);
			m_assert.fail("Sorting down functionaltiy fails at Zone header");
		}
		// sorting up
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify table header [Zone ID]");
			ReportUtils.setStepDescription(
					"Unable to identify table header [Zone ID]", true);
			m_assert.fail("Unable to identify table header [Zone ID]");
		}
		// Click on Header Zone
		SeleniumUtils.clickOnElement(element);
		randomInt = 0;
		for (int i = 1; i <= 4; i++) {
			randomInt = randomGenerator.nextInt(numberOfRows);
			if (randomInt > 1 && randomInt < 5) {
				break;
			}
		}
		// Identify Form table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Ad Placements table]");
			ReportUtils.setStepDescription("Unable to identify"
					+ " [Ad Placements table]", true);
			m_assert.fail("Unable to identify [Ad Placements table]");
		}
		Number_RandomGenerated = SeleniumUtils.getTextFromTable(element,
				randomInt, 1);
		Number_RandomGenBefore = SeleniumUtils.getTextFromTable(element,
				randomInt - 1, 1);
		Number_RandomGenAfter = SeleniumUtils.getTextFromTable(element,
				randomInt + 1, 1);
		if (Number_RandomGenerated > Number_RandomGenBefore
				|| Number_RandomGenerated < Number_RandomGenAfter) {
			logger.error("Sorting Up functionaltiy fails at Zone header");
			ReportUtils.setStepDescription("Sorting Up functionaltiy fails at "
					+ "Zone header", true);
			m_assert.fail("Sorting Up functionaltiy fails at Zone header");
		}
		// Identify Header zone
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderZone")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify table header [Zone ID]");
			ReportUtils.setStepDescription("Unable to identify table "
					+ "header [Zone ID]", true);
			m_assert.fail("Unable to identify table header [Zone ID]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Sorting Up functionality on Name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify header [Name]");
			ReportUtils.setStepDescription("Unable to identify "
					+ "header [Name]", true);
			m_assert.fail("Unable to identify header [Name]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Ad Placements table]");
			ReportUtils.setStepDescription("Unable to identify "
					+ "[Ad Placements table]", true);
			m_assert.fail("Unable to identify [Ad Placements table]");
		}
		boolean isSortingUpVerified = SeleniumCustomUtils
				.sortingUpOnAdPlacementsSites(element);
		if (!isSortingUpVerified) {
			logger.error("Sorting UP fails for Name header field");
			ReportUtils.setStepDescription("Sorting UP fails for Name "
					+ "header field", true);
			m_assert.fail("Sorting UP fails for Name header field");
		}
		// Identify header name
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormtableHeaderName")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify header [Name]");
			ReportUtils.setStepDescription("Unable to identify header [Name]",
					true);
			m_assert.fail("Unable to identify header [Name]");
		}
		// Click
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		// Identify Form table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Ad Placements table]");
			ReportUtils.setStepDescription("Unable to identify "
					+ "[Ad Placements table]", true);
			m_assert.fail("Unable to identify [Ad Placements table]");
		}
		// Sorting down functionality
		boolean isSortingDownVerified = SeleniumCustomUtils
				.sortingDownOnAdPlacementsSites(element);
		if (!isSortingDownVerified) {
			logger.error("Sorting Down fails for Name header field");
			ReportUtils.setStepDescription("Sorting Down fails for Name "
					+ "header field", true);
			m_assert.fail("Sorting Down fails for Name header field");
		}
		// Click on Cancel button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Cancel] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Cancel] button", true);
			m_assert.fail("Unable to identify [Cancel] button");
		}
		SeleniumUtils.clickOnElement(element);
		m_assert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods = "loginAs")
	public void createNewAdInAdPlacements() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewAdInAdPlacements")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [createNewAdInAdPlacements] is not "
					+ "added for execution");
			ReportUtils.setStepDescription(
					"Test case [createNewAdInAdPlacements] "
							+ "is not added for execution", true);
			throw new SkipException(
					"Test case [createNewAdInAdPlacements] is not "
							+ "added for execution");
		}
		// read data
		testcaseArgs = getTestData("createNewAdInAdPlacements");
		logger.info("Starting [createNewAdInAdPlacements] execution");
		logger.info("Verify if user is on [Sites] tab");
		SeleniumUtils.sleepThread(3);
		// Identify Sites tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Identify Sites tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
				ReportUtils.setStepDescription(
						"[Sites] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext(), SitesTabText, true);
				m_assert.fail("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
			}
			// Click on Sites tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Sites tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Sites] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext(), SitesSubTabHeaderText, true);
				m_assert.fail("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Sites] tab");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(6);
		// Identify Application list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list "
					+ "in [Sites] tab");
			ReportUtils.setStepDescription(
					"Unable to identify applications list " + "in [Sites] tab",
					true);
			m_assert.fail("Unable to identify applications list "
					+ "in [Sites] tab");
		}
		// verify the specific application
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Click on Ad Placements link
		boolean isClicked = SeleniumCustomUtils.clickAtAdPlacementsInAdSites(
				element, testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Ad Placements' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application actions 'Ad Placements' "
							+ "link for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Ad Placements' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Identify Create New Placement button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCrateNewPlacementBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCrateNewPlacementBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Create New Placement] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Create New Placement] " + "button",
					true);
			m_assert.fail("Unable to identify [Create New Placement] button");
		}
		// Click on Create New Placement button
		SeleniumUtils.clickOnElement(element);
		logger.info("Verify the Device section opened");
		// Identify Create Ad Placement form
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewForm")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewForm")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Create New Ad] form");
			ReportUtils.setStepDescription(
					"Unable to identify [Create New Ad] form", true);
			m_assert.fail("Unable to identify [Create New Ad] form");
		}
		// Identify Device Type
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewFormDeviceType")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewFormDeviceType")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Device type list in [Create New Ad] form");
			ReportUtils.setStepDescription(
					"Unable to identify Device type list "
							+ "in [Create New Ad] form", true);
			m_assert.fail("Unable to identify Device type list in [Create New Ad] form");
		}
		// Select Device
		boolean isSelected = SeleniumCustomUtils
				.selectDeviceTypeFromDeviceList(element,
						testcaseArgs.get("DeviceSelection"));
		if (!isSelected) {
			logger.error("Unable to select the device type ["
					+ testcaseArgs.get("DeviceSelection") + "]");
			ReportUtils.setStepDescription("Unable to select the device type ["
					+ testcaseArgs.get("DeviceSelection") + "]", true);
			m_assert.fail("Unable to select the device type ["
					+ testcaseArgs.get("DeviceSelection") + "]");
		}
		// Device type
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewFormAdType")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewFormAdType")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Ad type list in [Create New Ad] form");
			ReportUtils.setStepDescription(
					"Unable to identify Ad type list in [Create New Ad] form",
					true);
			m_assert.fail("Unable to identify Ad type list in [Create New Ad] form");
		}
		// Select Device list
		isSelected = SeleniumCustomUtils.selectDeviceTypeFromDeviceList(
				element, testcaseArgs.get("DeviceType"));
		if (!isSelected) {
			logger.error("Unable to select the device type ["
					+ testcaseArgs.get("DeviceSelection") + "]");
			ReportUtils.setStepDescription("Unable to select the device type ["
					+ testcaseArgs.get("DeviceSelection") + "]", true);
			m_assert.fail("Unable to select the device type ["
					+ testcaseArgs.get("DeviceSelection") + "]");
		}
		logger.info("Click on Save button");
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewFormSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"AdSitesTabAdPlacementsFormCreateNewFormSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save Button in [Create New Ad] form");
			ReportUtils.setStepDescription("Unable to identify Save Button in "
					+ "[Create New Ad] form", true);
			m_assert.fail("Unable to identify Save Button in [Create New Ad] form");
		}
		SeleniumUtils.clickOnElement(element);
		// Identify Cancel button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Cancel] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Ad Placements - Cancel] button", true);
			m_assert.fail("Unable to identify [Cancel] button");
		}
		SeleniumUtils.clickOnElement(element);
		m_assert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = "loginAs")
	public void editAdInAdPlacements() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editAdInAdPlacements")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [editAdInAdPlacements] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [editAdInAdPlacements] is not added for execution",
							false);
			throw new SkipException(
					"Test case [editAdInAdPlacements] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("editAdInAdPlacements");
		logger.info("Starting [editAdInAdPlacements] execution");
		logger.info("Verify if user is on [Sites] tab");
		SeleniumUtils.sleepThread(3);
		// Identify Sites tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Identify Sites tab
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
				ReportUtils.setStepDescription(
						"[Sites] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext(), SitesTabText, true);
				m_assert.fail("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
			}
			// Click on Sites tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Sites tab header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Sites] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext(), SitesSubTabHeaderText, true);
				m_assert.fail("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Sites] tab");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(6);
		// Identify Applications list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list in [Sites] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify applications list in [Sites] tab",
							true);
			m_assert.fail("Unable to identify applications list in [Sites] tab");
		}
		// Verify the specific application in application list
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Click at Ad Placements link
		boolean isClicked = SeleniumCustomUtils.clickAtAdPlacementsInAdSites(
				element, testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Ad Placements' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application actions 'Ad Placements' "
							+ "link for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Ad Placements' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Identify Ad Placements table
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify table in Ad sites tab");
			ReportUtils.setStepDescription(
					"Unable to identify table in Ad sites tab", true);
			m_assert.fail("Unable to identify table in Ad sites tab");
		}
		// Retrieve Number of Rows available in Ad Placement table
		logger.info("Retrieve number of rows in Ad Placement table");
		int rows_Count = SeleniumCustomUtils
				.get_Count_Of_Rows_In_AdPlacement_Table(element);
		if (rows_Count != 0) {
			logger.info("Click on Ad in Ad Placement table");
			// Click on Edit link
			isClicked = SeleniumCustomUtils
					.click_At_Edit_Link_In_AdPlacement_Table(element);
			if (!isClicked) {
				logger.error("Unable to click on Edit link of First Ad in "
						+ "Ad Placement table");
				ReportUtils.setStepDescription(
						"Unable to click on Edit link of First Ad in "
								+ "Ad Placement table", true);
				m_assert.fail("Unable to click on Edit link of First Ad in "
						+ "Ad Placement table");
			}
			// Identify Ad Placements Edit form
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormCreateNewForm")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormCreateNewForm")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Ad sites edit form");
				ReportUtils.setStepDescription(
						"Unable to identify Ad sites edit form", true);
				m_assert.fail("Unable to identify Ad sites edit form");
			}
			// Identify Device type buttons
			element = SeleniumUtils
					.findobject(
							Suite.objectRepositoryMap
									.get("AdSitesTabAdPlacementsFormCreateNewFormDeviceType")
									.getLocatortype(),
							Suite.objectRepositoryMap
									.get("AdSitesTabAdPlacementsFormCreateNewFormDeviceType")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Device type list in "
						+ "[Create New Ad] form");
				ReportUtils.setStepDescription(
						"Unable to identify Device type list in "
								+ "[Create New Ad] form", true);
				m_assert.fail("Unable to identify Device type list in "
						+ "[Create New Ad] form");
			}
			// Select specific Device based on the input
			boolean isSelected = SeleniumCustomUtils
					.selectDeviceTypeFromDeviceList(element,
							testcaseArgs.get("DeviceSelection"));
			if (!isSelected) {
				logger.error("Unable to select the device type ["
						+ testcaseArgs.get("DeviceSelection") + "]");
				ReportUtils.setStepDescription(
						"Unable to select the device type ["
								+ testcaseArgs.get("DeviceSelection") + "]",
						true);
				m_assert.fail("Unable to select the device type ["
						+ testcaseArgs.get("DeviceSelection") + "]");
			}
			logger.info("Click on Save button");
			// Identify Save button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormCreateNewFormSaveBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"AdSitesTabAdPlacementsFormCreateNewFormSaveBtn")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify Save Button in [Create New Ad] form");
				ReportUtils
						.setStepDescription(
								"Unable to identify Save Button in [Create New Ad] form",
								true);
				m_assert.fail("Unable to identify Save Button in [Create New Ad] form");
			}
			// Click on Save
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);

		} else {
			logger.warn("No Ads are available in Ad Placement Table. "
					+ "Please create Ad");
			ReportUtils.setStepDescription(
					"No Ads are available in Ad Placement Table. "
							+ "Please create Ad", true);
			m_assert.fail("No Ads are available in Ad Placement Table. "
					+ "Please create Ad");
		}
		// Identify Cancel button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Cancel] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Ad Placements- Cancel] " + "button",
					true);
			m_assert.fail("Unable to identify [Cancel] button");
		}
		// Click on Cancel
		SeleniumUtils.clickOnElement(element);
		m_assert.assertAll();
	}

	@Test(priority = 15, dependsOnMethods = "loginAs")
	public void deleteAdInAdPlacements() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteAdInAdPlacements")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [deleteAdInAdPlacements] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [deleteAdInAdPlacements] is not added for execution",
							false);
			throw new SkipException(
					"Test case [deleteAdInAdPlacements] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("deleteAdInAdPlacements");
		logger.info("Starting [deleteAdInAdPlacements] execution");
		logger.info("Verify if user is on [Sites] tab");
		SeleniumUtils.sleepThread(3);
		// Identify Sites tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Identify Sites tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
				ReportUtils.setStepDescription(
						"[Sites] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext(), SitesTabText, true);
				m_assert.fail("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
			}
			// Click on Sites tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Ad sites tab header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Sites] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext(), SitesSubTabHeaderText, true);
				m_assert.fail("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Sites] tab");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(6);
		// Identify Applications list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list in [Sites] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify applications list in [Sites] tab",
							true);
			m_assert.fail("Unable to identify applications list in [Sites] tab");
		}
		// Verify if application present in the list
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application in the list",
					testcaseArgs.get("application"), true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Click on Ad placements
		boolean isClicked = SeleniumCustomUtils.clickAtAdPlacementsInAdSites(
				element, testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Ad Placements' link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils
					.setStepDescription(
							"Unable to identify application actions 'Ad Placements' link for the application ["
									+ testcaseArgs.get("application")
									+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Ad Placements' link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Identify Ad placements table
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("AdSitesTabAdPlacementsFormtable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify table in Ad sites tab");
			ReportUtils.setStepDescription(
					"Unable to identify table in Ad sites tab", true);
			m_assert.fail("Unable to identify table in Ad sites tab");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			logger.info("Retrieve the AdName from Ad Placement table ");
			String Ad_Name = SeleniumCustomUtils
					.get_AdName_From_AdPlacement_Table(element);
			logger.info("Delete the First Ad from the Ad Placement table");
			// Click on Delete link of specific Ad
			isClicked = SeleniumCustomUtils.delete_Ad_From_AdPlacement_Table(
					element, Ad_Name);
			if (!isClicked) {
				logger.error("Unable to click on Remove link of [" + Ad_Name
						+ "]");
				ReportUtils.setStepDescription(
						"Unable to click on Remove link of [" + Ad_Name + "]",
						true);
				m_assert.fail("Unable to click on Remove link of [" + Ad_Name
						+ "]");
			}
		} else {
			logger.info("Retrieve the AdName from Ad Placement table ");
			String Ad_Name = SeleniumCustomUtils
					.get_AdName_From_AdPlacement_Table(element);
			logger.info("Delete the First Ad from the Ad Placement table");
			// Click on Delete link of specific Ad
			isClicked = SeleniumCustomUtils.delete_Ad_From_AdPlacement_Table(
					element, Ad_Name);
			if (!isClicked) {
				logger.error("Unable to click on Remove link of [" + Ad_Name
						+ "]");
				ReportUtils.setStepDescription(
						"Unable to click on Remove link of [" + Ad_Name + "]",
						true);
				m_assert.fail("Unable to click on Remove link of [" + Ad_Name
						+ "]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.acceptAlertWindow();
		}
		SeleniumUtils.sleepThread(5);
		logger.info("Execution of deleteAdInAdPlacements is successful");
		// Identify Cancel button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AdSitesTabAdPlacementsFormCancelBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Cancel] button");
			ReportUtils.setStepDescription(
					"Unable to identify [Cancel] button", true);
			m_assert.fail("Unable to identify [Cancel] button");
		}
		// Click on Cancel button
		SeleniumUtils.clickOnElement(element);
		logger.info("Execution of editAdInAdPlacements is successful");
		m_assert.assertAll();
	}

	@Test(priority = 16, dependsOnMethods = "loginAs")
	public void verifyViewStats() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyViewStats")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyViewStats] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyViewStats] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifyViewStats] is not added for execution");
		}
		// read parameter data
		testcaseArgs = getTestData("verifyViewStats");
		logger.info("Starting [verifyViewStats] execution");
		logger.info("Verify if user is on [Sites] tab");
		SeleniumUtils.sleepThread(3);
		// Identify Sites tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SitesSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Sites] page");
			logger.info("Navigate to [Sites] tab");
			// Idetnify Sites tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTab")
							.getLocatorvalue());
			// Get the text
			String SitesTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesTabText,
					Suite.objectRepositoryMap.get("AdSitesTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
				ReportUtils.setStepDescription(
						"[Sites] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTab")
								.getExptext(), SitesTabText, true);
				m_assert.fail("[Sites] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("SitesTabText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesTabText + "]");
			}
			// Click on Sites tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Sites tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SitesSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SitesSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Sites] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext(), SitesSubTabHeaderText, true);
				m_assert.fail("[Sites] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdSitesTabHeaderText")
								.getExptext()
						+ "] and the return text is ["
						+ SitesSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Sites] tab");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(6);
		// Identify Application list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdSitesTabApplicationsList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list in [Sites] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify applications list in [Sites] tab",
							true);
			m_assert.fail("Unable to identify applications list in [Sites] tab");
		}
		// Identify the specific application in list
		boolean isAppPresent = SeleniumCustomUtils
				.checkApplicationNameInAdSites(element,
						testcaseArgs.get("application"));
		if (!isAppPresent) {
			logger.error("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application in application list",
					testcaseArgs.get("application"), true);
			m_assert.fail("Unable to identify application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		// Click on View Stats link of specific application
		boolean isClicked = SeleniumCustomUtils.clickAtViewStatsInAdSites(
				element, testcaseArgs.get("application"));
		if (!isClicked) {
			logger.error("Unable to identify application actions 'Ad Placements' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
			ReportUtils.setStepDescription(
					"Unable to identify application actions 'Ad Placements' "
							+ "link for the application ["
							+ testcaseArgs.get("application")
							+ "] in application list", true);
			m_assert.fail("Unable to identify application actions 'Ad Placements' "
					+ "link for the application ["
					+ testcaseArgs.get("application") + "] in application list");
		}
		SeleniumUtils.sleepThread(5);
		// Reports tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ReportsHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(ReportsHeaderText,
				Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Reports] tab header text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ ReportsHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Reports] tab header text matching failed", "",
					Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
							.getExptext(), ReportsHeaderText, true);
			m_assert.fail("[Reports] tab header text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ ReportsHeaderText + "]");
		}
		logger.info("Execution of verifyViewStats is successful");
		m_assert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods = "loginAs")
	public void verifyReports() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyReports")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyReports] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyReports] is not added for execution",
					true);
			throw new SkipException("The runmode for verifyReports set to No");
		}
		// read param data
		testcaseArgs = getTestData("verifyReports");
		logger.info("Starting [verifyReports] execution");
		logger.info("Verify if user is on [Reports] tab");
		// Identify Reports tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ReportsSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(ReportsSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Reports] page");
			logger.info("Navigate to [Reports] tab");
			// Identify Reports tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdReportsTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdReportsTab")
							.getLocatorvalue());
			// Get the text
			String ReportsTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(ReportsTabText,
					Suite.objectRepositoryMap.get("AdReportsTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Reports] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdReportsTab")
								.getExptext()
						+ "] and the return text is ["
						+ ReportsTabText + "]");
				ReportUtils.setStepDescription(
						"[Reports] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdReportsTab")
								.getExptext(), ReportsTabText, true);
				m_assert.fail("[Reports] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdReportsTab")
								.getExptext()
						+ "] and the return text is ["
						+ ReportsTabText + "]");
			}
			// Click on Reports tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
							.getLocatorvalue());
			// Get the text
			ReportsSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(ReportsSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
							.getExptext());
			logger.info("Navigation to [Sites] tab is successful");
			if (!isTextMatching) {
				logger.error("[Reports] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdReportsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ ReportsSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Reports] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdReportsTabHeaderText")
								.getExptext(), ReportsSubTabHeaderText, true);
				m_assert.fail("[Reports] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdReportsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ ReportsSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Reports] tab");
		// Verify Reports data
		logger.info("Verify Application & time dropdowns in [Reports] tab");
		m_assert.assertAll();
	}

	@Test(priority = 18, dependsOnMethods = "loginAs")
	public void verifyPayments() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyPayments")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [verifyPayments] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyPayments] is not added for execution",
					true);
			throw new SkipException(
					"Test case [verifyPayments] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyPayments");
		logger.info("Starting [verifyPayments] execution");
		logger.info("Verify if user is on [Payments] tab");
		SeleniumUtils.sleepThread(3);
		// Identify Payments tab header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(PaymentsSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Payments] page");
			logger.info("Navigate to [Payments] tab");
			// Identify Payments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatorvalue());
			String PaymentsTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils
					.assertEqual(PaymentsTabText, Suite.objectRepositoryMap
							.get("AdPaymentsTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
				ReportUtils.setStepDescription(
						"[Payments] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext(), PaymentsTabText, true);
				m_assert.fail("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
			}
			// Click on Payments tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue());
			// Get the theader text
			PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					PaymentsSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Payments] sub tab header text matching failed", "",
						Suite.objectRepositoryMap
								.get("AdPaymentsTabHeaderText").getExptext(),
						PaymentsSubTabHeaderText, true);
				m_assert.fail("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
			}
		}
		// Identify Payment Radio buttons
		logger.info("Verify Payment options radio buttons ");
		logger.info("Identify 'None' radio button in 'Advertising - Payments' ");
		// Identify None radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_None_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_None_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_None_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(Adv_Pay_None_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_None_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - None] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_None_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_None_RadioBtn_Text + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - None] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_None_RadioBtn").getExptext(),
							Adv_Pay_None_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - None] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_None_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_None_RadioBtn_Text + "]");
		}
		logger.info("Identify 'ACH/WIRE' radio button in 'Advertising - Payments' ");
		// Identify ACH/WIRE radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_ACH_WIRE_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_RadioBtn").getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_RadioBtn_Text + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_ACH_WIRE_RadioBtn").getExptext(),
							Adv_Pay_ACH_WIRE_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_RadioBtn").getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_RadioBtn_Text + "]");
		}
		logger.info("Identify 'Paypal' radio button in 'Advertising - Payments' ");
		// Identify Paypal radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_Paypal_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_Paypal_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - Paypal] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_Paypal_RadioBtn_Text + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - Payapl] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_PayPal_RadioBtn").getExptext(),
							Adv_Pay_ACH_WIRE_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - Paypal] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_Paypal_RadioBtn_Text + "]");
		}
		logger.info("Identify 'Check' radio button in 'Advertising - Payments' ");
		// Identify Paypal radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_Check_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(Adv_Pay_Check_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - Check] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_Check_RadioBtn_Text + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - Check] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_Check_RadioBtn").getExptext(),
							Adv_Pay_Check_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - Check] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_Check_RadioBtn_Text + "]");
		}
		logger.info("Identify Save Changes button");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save Changes' button "
					+ "in 'Advertising - Payments' page");
			ReportUtils.setStepDescription("Unable to identify "
					+ "'Save Changes' button in 'Advertising - Payments' page",
					true);
			m_assert.fail("Unable to identify 'Save Changes' button "
					+ "in 'Advertising - Payments' page");
		}
		m_assert.assertAll();
	}

	@Test(priority = 19, dependsOnMethods = "loginAs")
	public void verify_Advertising_Payments_ACHWIRE() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("verify_Advertising_Payments_ACHWIRE")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Testcase [verify_Advertising_Payments_ACHWIRE] is "
					+ "not added for execution");
			ReportUtils.setStepDescription(
					"Testcase [verify_Advertising_Payments_ACHWIRE] is "
							+ "not added for execution", true);
			throw new SkipException(
					"The runmode for verify_Advertising_Payments_ACHWIRE "
							+ "set to No");
		}
		// read the param data
		testcaseArgs = getTestData("verify_Advertising_Payments_ACHWIRE");
		logger.info("Starting [verify_Advertising_Payments_ACHWIRE] execution");
		logger.info("Verify if the user is on [Advertising - Payments] page");
		// Verify if User is on Payments - ACH/WIRE page
		// Identify Payments tab header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(PaymentsSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Payments] page");
			logger.info("Navigate to [Payments] tab");
			// Identify Payments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatorvalue());
			String PaymentsTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils
					.assertEqual(PaymentsTabText, Suite.objectRepositoryMap
							.get("AdPaymentsTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
				ReportUtils.setStepDescription(
						"[Payments] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext(), PaymentsTabText, true);
				m_assert.fail("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
			}
			// Click on Payments tab
			SeleniumUtils.clickOnElement(element);
			// Wait for the Payments - page header element to display
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue());
			// Get the theader text
			PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					PaymentsSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Payments] sub tab header text matching failed", "",
						Suite.objectRepositoryMap
								.get("AdPaymentsTabHeaderText").getExptext(),
						PaymentsSubTabHeaderText, true);
				m_assert.fail("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
			}
		}
		logger.info("Identify if the ACH/WIRE radio button is available?");
		// Identify ACH/WIRE radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_ACH_WIRE_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_RadioBtn").getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_RadioBtn_Text + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_ACH_WIRE_RadioBtn").getExptext(),
							Adv_Pay_ACH_WIRE_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_RadioBtn").getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_RadioBtn_Text + "]");
		}
		logger.info("Click on ACH/WIRE radio button");
		// Click on ACH/WIRE radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Wait for ACH/WIRE fields to be displayed");
		// Wait for the ACH/WIRE options to display
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getExptext());
		// Identify ACH/WIRE fields
		logger.info("Identify if Recipient/Company Name field is displayed ");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element,
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE - Recipient/Company Name]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element + "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - ACH/WIRE - Recipient/Company Name]  "
							+ "field text matching failed", "",
					Suite.objectRepositoryMap.get(
							"[Advertising - Payments - ACH/WIRE - Recipient/Company Name]  "
									+ "field text matching failed")
							.getExptext(),
					Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE - Recipient/Company Name]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element + "]");
		}
		// Identify Recipient/Company Name text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Recipient/Company Name text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Recipient/Company Name text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Recipient/Company Name text box",
					true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Recipient/Company Name text box");
		}
		logger.info("Identify if Bank Name field is displayed ");
		// Identify Bank Name element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element,
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE - Bank Name]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element + "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - ACH/WIRE - Bank Name]  "
							+ "field text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element")
							.getExptext(),
					Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE - Bank Name]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_BankName_Text_Element + "]");
		}
		// Identify Bank Name text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Bank Name text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Name text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Bank Name text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Name text box");
		}
		logger.info("Identify if Bank Address field is displayed ");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element,
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE - Bank Address]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE - Bank Address]  "
									+ "field text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element")
									.getExptext(),
							Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element,
							true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE - Bank Address]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_BankAddress_Text_Element + "]");
		}
		// Identify Bank Address text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Bank Address text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Address text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Bank Address text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Address text box");
		}

		// Identify Account Number/BAN
		logger.info("Identify if Account Number/BAN field is displayed ");
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element")
								.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element,
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element")
								.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE - Account Number/BAN]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element
					+ "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE - Account Number/BAN]  "
									+ "field text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element")
									.getExptext(),
							Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element,
							true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE - Account Number/BAN]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_AccountNumber_Text_Element
					+ "]");
		}
		// Identify Bank Address text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Account Number/BAN text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Account Number/BAN text box");
			ReportUtils
					.setStepDescription(
							"Unable to identify Advertising - "
									+ "Payments - ACH/WIRE - Account Number/BAN text box",
							true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Account Number/BAN text box");
		}
		// Identify ACH Routing Number

		logger.info("Identify if ACH Routing Number field is displayed ");
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element")
								.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element,
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element")
								.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE - ACH Routing Number]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element
					+ "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE - ACH Routing Number]  "
									+ "field text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element")
									.getExptext(),
							Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element,
							true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE - ACH Account Number]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_Text_Element
					+ "]");
		}
		// Identify Bank Address text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- ACH Number text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "ACH Number text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - ACH Number text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "ACH Number text box");
		}
		m_assert.assertAll();
	}

	@Test(priority = 20, dependsOnMethods = "loginAs")
	public void validate_Advertising_Payments_ACHWIRE() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("verify_Advertising_Payments_ACHWIRE")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Testcase [verify_Advertising_Payments_ACHWIRE] is "
					+ "not added for execution");
			ReportUtils.setStepDescription(
					"Testcase [verify_Advertising_Payments_ACHWIRE] is "
							+ "not added for execution", true);
			throw new SkipException(
					"The runmode for verify_Advertising_Payments_ACHWIRE "
							+ "set to No");
		}
		// read the param data
		testcaseArgs = getTestData("verify_Advertising_Payments_ACHWIRE");
		logger.info("Starting [verify_Advertising_Payments_ACHWIRE] execution");
		logger.info("Verify if the user is on [Advertising - Payments] page");
		// Verify if User is on Payments - ACH/WIRE page
		// Identify Payments tab header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(PaymentsSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Payments] page");
			logger.info("Navigate to [Payments] tab");
			// Identify Payments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatorvalue());
			String PaymentsTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils
					.assertEqual(PaymentsTabText, Suite.objectRepositoryMap
							.get("AdPaymentsTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
				ReportUtils.setStepDescription(
						"[Payments] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext(), PaymentsTabText, true);
				m_assert.fail("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
			}
			// Click on Payments tab
			SeleniumUtils.clickOnElement(element);
			// Wait for the Payments - page header element to display
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue());
			// Get the theader text
			PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					PaymentsSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Payments] sub tab header text matching failed", "",
						Suite.objectRepositoryMap
								.get("AdPaymentsTabHeaderText").getExptext(),
						PaymentsSubTabHeaderText, true);
				m_assert.fail("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
			}
		}
		logger.info("Identify if the ACH/WIRE radio button is available?");
		// Identify ACH/WIRE radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_ACH_WIRE_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_ACH_WIRE_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_ACH_WIRE_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_RadioBtn").getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_RadioBtn_Text + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_ACH_WIRE_RadioBtn").getExptext(),
							Adv_Pay_ACH_WIRE_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("Adv_Pay_ACH_WIRE_RadioBtn").getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_ACH_WIRE_RadioBtn_Text + "]");
		}
		logger.info("Click on ACH/WIRE radio button");
		// Click on ACH/WIRE radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Wait for ACH/WIRE fields to be displayed");
		// Wait for the ACH/WIRE options to display
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_Text_Element")
						.getExptext());
		logger.info("Validate ACH/WIRE functionality when click on Submit button with empty field values");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Recipient/Company Name text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Recipient/Company Name text box",
					true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Recipient/Company Name text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Identify Bank Name text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Bank Name text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Name text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Bank Name text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Name text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Identify Bank Address text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Bank Address text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Address text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Bank Address text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Bank Address text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);

		// Identify Bank Address text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- Account Number/BAN text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Account Number/BAN text box");
			ReportUtils
					.setStepDescription(
							"Unable to identify Advertising - "
									+ "Payments - ACH/WIRE - Account Number/BAN text box",
							true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Account Number/BAN text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Identify ACH Number text box
		logger.info("Identify Advertising-Payments-ACH/WIRE- ACH Number text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "ACH Number text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - ACH Number text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "ACH Number text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Save Changes button");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Save Changes button", true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Save Changes button");
		}
		// Click on Save Changes button
		SeleniumUtils.clickOnElement(element);
		logger.info("Identify if error messgae is displayed");
		// Wait for the Error Message to be displayed
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_BlankFields_ErrorMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_BlankFields_ErrorMsg")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Error alert messgae "
					+ "when user try to save blank data in "
					+ "ACH/WIRE fields");
			ReportUtils.setStepDescription(
					"Unable to identify Error alert messgae "
							+ "when user try to save blank "
							+ "data in ACH/WIRE fields", true);
			m_assert.fail("Unable to identify Error alert messgae "
					+ "when user try to save blank data in "
					+ "ACH/WIRE fields");
		}
		String EXP_ACH_WIRE_BlankFields_ErrorMsg = Suite.objectRepositoryMap
				.get("Adv_Pay_ACH_WIRE_BlankFields_ErrorMsg").getExptext();
		String ACT_ACH_WIRE_BlankFields_ErrorMsg = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				EXP_ACH_WIRE_BlankFields_ErrorMsg,
				ACT_ACH_WIRE_BlankFields_ErrorMsg);
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "blank data in ACH/WIRE fields "
					+ "The Expected text is ["
					+ EXP_ACH_WIRE_BlankFields_ErrorMsg
					+ "] and the return text is ["
					+ ACT_ACH_WIRE_BlankFields_ErrorMsg + "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - ACH/WIRE] Error alert "
							+ "message text matching failed when user Saves "
							+ "blank data in ACH/WIRE fields", "",
					EXP_ACH_WIRE_BlankFields_ErrorMsg,
					ACT_ACH_WIRE_BlankFields_ErrorMsg, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "blank data in ACH/WIRE fields "
					+ "The Expected text is ["
					+ EXP_ACH_WIRE_BlankFields_ErrorMsg
					+ "] and the return text is ["
					+ ACT_ACH_WIRE_BlankFields_ErrorMsg + "]");
		}
		logger.info("Validate Recipient/Company Name field..User Saves the data when ACH/WIRE field is empty");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Recipient/Company Name text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - ACH/WIRE - Recipient/Company Name text box",
					true);
			m_assert.fail("Unable to identify Advertising - Payments - ACH/WIRE - "
					+ "Recipient/Company Name text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Name text box
		SeleniumUtils.type(element, "Bank name");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatorvalue());

		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Address text box
		SeleniumUtils.type(element, "Bank Address");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Account Number text box
		SeleniumUtils.type(element, "BankAccountNumber123");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in ACH Number text box
		SeleniumUtils.type(element, "1245");
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatorvalue());
		// Click on Save Changes button
		SeleniumUtils.clickOnElement(element);
		// Wait for the Error Message to be displayed
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_BlankFields_ErrorMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_BlankFields_ErrorMsg")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Error alert messgae "
					+ "when user try to save blank data in "
					+ "ACH/WIRE fields");
			ReportUtils.setStepDescription(
					"Unable to identify Error alert messgae "
							+ "when user try to save blank "
							+ "data in ACH/WIRE fields", true);
			m_assert.fail("Unable to identify Error alert messgae "
					+ "when user try to save blank data in "
					+ "ACH/WIRE fields");
		}
		String EXP_Recipient_CompanyName_BlankFields_ErrorMsg = Suite.objectRepositoryMap
				.get("Adv_Pay_ACH_WIRE_BlankFields_ErrorMsg").getExptext();
		String ACT_Recipient_CompanyName_BlankFields_ErrorMsg = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				EXP_Recipient_CompanyName_BlankFields_ErrorMsg,
				ACT_Recipient_CompanyName_BlankFields_ErrorMsg);
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "blank data in Recipient/Company Name "
					+ "The Expected text is ["
					+ EXP_Recipient_CompanyName_BlankFields_ErrorMsg
					+ "] and the return text is ["
					+ ACT_Recipient_CompanyName_BlankFields_ErrorMsg + "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - ACH/WIRE] Error alert "
							+ "message text matching failed when user Saves "
							+ "blank data in Recipient/Company Name", "",
					EXP_Recipient_CompanyName_BlankFields_ErrorMsg,
					ACT_Recipient_CompanyName_BlankFields_ErrorMsg, true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "blank data in Recipient/Company Name "
					+ "The Expected text is ["
					+ EXP_Recipient_CompanyName_BlankFields_ErrorMsg
					+ "] and the return text is ["
					+ ACT_Recipient_CompanyName_BlankFields_ErrorMsg + "]");
		}
		logger.info("Validate AccoutNumber/BAN field... "
				+ "When user saves the data with Special characters in Account Number/BAN");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Type Company Name
		SeleniumUtils.type(element, "Cybage");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Name text box
		SeleniumUtils.type(element, "Bank name");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatorvalue());

		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Address text box
		SeleniumUtils.type(element, "Bank Address");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Account Number text box
		SeleniumUtils.type(element, "$%%%$$$####");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in ACH Number text box
		SeleniumUtils.type(element, "123131");
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatorvalue());
		// Click on Save Changes button
		SeleniumUtils.clickOnElement(element);
		// Wait for the Error Message to be displayed
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Error alert messgae "
					+ "when user try to save Special Characters "
					+ "data in Account Number/BAN field");
			ReportUtils.setStepDescription(
					"Unable to identify Error alert messgae "
							+ "when user try to save Special Characters "
							+ "data in Account Number/BAN field", true);
			m_assert.fail("Unable to identify Error alert messgae "
					+ "when user try to save Special Characters "
					+ "data in Account Number/BAN field");
		}
		String EXP_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg = Suite.objectRepositoryMap
				.get("Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg")
				.getExptext();
		String ACT_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				EXP_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg,
				ACT_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg);
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "Special Characters data in Account Number/BAN "
					+ "The Expected text is ["
					+ EXP_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg
					+ "] and the return text is ["
					+ ACT_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg
					+ "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - ACH/WIRE] Error alert "
							+ "message text matching failed when user Saves "
							+ "Special Characters data in Account Number/BAN",
					"",
					EXP_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg,
					ACT_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg,
					true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "Special Characters data in Account Number/BAN "
					+ "The Expected text is ["
					+ EXP_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg
					+ "] and the return text is ["
					+ ACT_Adv_Pay_ACH_WIRE_AccountNumber_SpecialChar_ErrorMsg
					+ "]");
		}
		logger.info("Validate ACH Number field... "
				+ "When user saves the data with Alphabets and Special characters in ACH Number");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_CompanyName_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Type Company Name
		SeleniumUtils.type(element, "Cybage");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankName_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Name text box
		SeleniumUtils.type(element, "Bank name");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_BankAddress_textbox")
						.getLocatorvalue());

		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Address text box
		SeleniumUtils.type(element, "Bank Address");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_AccountNumber_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in Banke Account Number text box
		SeleniumUtils.type(element, "hjkhjk45");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_ACH_WIRE_Recipient_ACHRoutingNumber_textbox")
						.getLocatorvalue());
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Enter valid data in ACH Number text box
		SeleniumUtils.type(element, "fsd^&*^");
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatorvalue());
		// Click on Save Changes button
		SeleniumUtils.clickOnElement(element);
		// Wait for the Error Message to be displayed
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Error alert messgae "
					+ "when user try to save Alpha and Special Characters "
					+ "data in ACH Number field");
			ReportUtils
					.setStepDescription(
							"Unable to identify Error alert messgae "
									+ "when user try to save Alpha and Special Characters "
									+ "data in ACH Number field", true);
			m_assert.fail("Unable to identify Error alert messgae "
					+ "when user try to save Alpha and Special Characters "
					+ "data in ACH Number field");
		}
		String EXP_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg = Suite.objectRepositoryMap
				.get("Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg")
				.getExptext();
		String ACT_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				EXP_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg,
				ACT_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg);
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "Alpha and Special Characters data in ACH Number "
					+ "The Expected text is ["
					+ EXP_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg
					+ "] and the return text is ["
					+ ACT_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg
					+ "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - ACH/WIRE] Error alert "
									+ "message text matching failed when user Saves "
									+ "Alpha and Special Characters data in ACH Number",
							"",
							EXP_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg,
							ACT_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg,
							true);
			m_assert.fail("[Advertising - Payments - ACH/WIRE] Error alert "
					+ "message text matching failed when user Saves "
					+ "Alpha and Special Characters data in ACH Number "
					+ "The Expected text is ["
					+ EXP_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg
					+ "] and the return text is ["
					+ ACT_Adv_Pay_ACH_WIRE_AchNumber_Alpha_And_Special_ErrorMsg
					+ "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 21, dependsOnMethods = "loginAs")
	public void verify_And_Validate_Advertising_Payments_PayPal() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase
					.equalsIgnoreCase("verify_And_Validate_Advertising_Payments_PayPal")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Testcase [verify_And_Validate_Advertising_Payments_PayPal]"
					+ " is not added for execution");
			ReportUtils.setStepDescription(
					"Testcase [verify_And_Validate_Advertising_Payments_PayPal]"
							+ " is not added for execution", true);
			throw new SkipException(
					"Testcase [verify_And_Validate_Advertising_Payments_PayPal]"
							+ " is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("Testcase [verify_And_Validate_Advertising_Payments_PayPal]"
				+ " is not added for execution");
		logger.info("Starting [Testcase [verify_And_Validate_Advertising_Payments_PayPal]"
				+ " is not added for execution] execution");
		logger.info("Verify if the user is on [Advertising - Payments] page");
		// Verify if User is on Payments - ACH/WIRE page
		// Identify Payments tab header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(PaymentsSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Payments] page");
			logger.info("Navigate to [Payments] tab");
			// Identify Payments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatorvalue());
			String PaymentsTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils
					.assertEqual(PaymentsTabText, Suite.objectRepositoryMap
							.get("AdPaymentsTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
				ReportUtils.setStepDescription(
						"[Payments] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext(), PaymentsTabText, true);
				m_assert.fail("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
			}
			// Click on Payments tab
			SeleniumUtils.clickOnElement(element);
			// Wait for the Payments - page header element to display
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue());
			// Get the theader text
			PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					PaymentsSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Payments] sub tab header text matching failed", "",
						Suite.objectRepositoryMap
								.get("AdPaymentsTabHeaderText").getExptext(),
						PaymentsSubTabHeaderText, true);
				m_assert.fail("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
			}
		}
		logger.info("Identify if the Paypal radio button is available?");
		// Identify Paypal radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_Paypal_RadioBtn_Text = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_Paypal_RadioBtn_Text,
				Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - Paypal] radio button "
					+ "text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_Paypal_RadioBtn_Text + "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - Paypal] radio button "
							+ "text matching failed", "",
					Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
							.getExptext(), Adv_Pay_Paypal_RadioBtn_Text, true);
			m_assert.fail("[Advertising - Payments - Paypal] radio button "
					+ "text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_PayPal_RadioBtn")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_Paypal_RadioBtn_Text + "]");
		}
		logger.info("Click on Paypal radio button");
		// Click on Paypal radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Wait for Paypal fields to be displayed");
		// Wait for the Paypal options to display
		SeleniumUtils
				.wait_For_Element_To_Display_Having_Text(
						Suite.objectRepositoryMap.get(
								"Adv_Pay_Paypal_PaypalLogin_Text_Element")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("Adv_Pay_Paypal_PaypalLogin_Text_Element")
								.getLocatorvalue(), Suite.objectRepositoryMap
								.get("Adv_Pay_Paypal_PaypalLogin_Text_Element")
								.getExptext());
		logger.info("Validate Paypal functionality when click "
				+ "on Submit button with empty field values");
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"Adv_Pay_Paypal_PaypalLogin_textbox")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("Adv_Pay_Paypal_PaypalLogin_textbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - Paypal - "
					+ "Paypal Login text box");
			ReportUtils.setStepDescription(
					"Unable to identify Advertising - Payments - Paypal - "
							+ "Paypal Login text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - Paypal - "
					+ "Paypal Login text box");
		}
		// Clear the default data
		SeleniumUtils.clearText(element);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_SaveChanges_Btn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - Paypal - "
					+ "Save Changes button");
			ReportUtils.setStepDescription(
					"Unable to identify Advertising - Payments - Paypal - "
							+ "Save Changes button", true);
			m_assert.fail("Unable to identify Advertising - Payments - Paypal - "
					+ "Save Changes button");
		}
		// Click on Save Changes button
		SeleniumUtils.clickOnElement(element);
		logger.info("Identify if error messgae is displayed");
		m_assert.assertAll();
	}

	@Test(priority = 22, dependsOnMethods = "loginAs")
	public void verify_Advertising_Payments_CHECK() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verify_Advertising_Payments_CHECK")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Testcase [verify_Advertising_Payments_CHECK] is "
					+ "not added for execution");
			ReportUtils.setStepDescription(
					"Testcase [verify_Advertising_Payments_CHECK] is "
							+ "not added for execution", true);
			throw new SkipException(
					"Testcase [verify_Advertising_Payments_CHECK] is "
							+ "not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("verify_Advertising_Payments_CHECK");
		logger.info("Starting [verify_Advertising_Payments_CHECK] execution");
		logger.info("Verify if the user is on [Advertising - Payments] page");
		// Verify if User is on Payments - ACH/WIRE page
		// Identify Payments tab header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(PaymentsSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Payments] page");
			logger.info("Navigate to [Payments] tab");
			// Identify Payments tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTab")
							.getLocatorvalue());
			String PaymentsTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils
					.assertEqual(PaymentsTabText, Suite.objectRepositoryMap
							.get("AdPaymentsTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
				ReportUtils.setStepDescription(
						"[Payments] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext(), PaymentsTabText, true);
				m_assert.fail("[Payments] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdPaymentsTab")
								.getExptext()
						+ "] and the return text is ["
						+ PaymentsTabText + "]");
			}
			// Click on Payments tab
			SeleniumUtils.clickOnElement(element);
			// Wait for the Payments - page header element to display
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			// Identify header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getLocatorvalue());
			// Get the theader text
			PaymentsSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					PaymentsSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdPaymentsTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Payments] sub tab header text matching failed", "",
						Suite.objectRepositoryMap
								.get("AdPaymentsTabHeaderText").getExptext(),
						PaymentsSubTabHeaderText, true);
				m_assert.fail("[Payments] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdPaymentsTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ PaymentsSubTabHeaderText + "]");
			}
		}
		logger.info("Identify if the Check radio button is available?");
		// Identify Check radio button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_Check_RadioBtn = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(Adv_Pay_Check_RadioBtn,
				Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - CHECK] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_Check_RadioBtn + "]");
			ReportUtils
					.setStepDescription(
							"[Advertising - Payments - CHECK] radio button text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"Adv_Pay_Check_RadioBtn").getExptext(),
							Adv_Pay_Check_RadioBtn, true);
			m_assert.fail("[Advertising - Payments - CHECK] radio button text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get("Adv_Pay_Check_RadioBtn")
							.getExptext()
					+ "] and the return text is ["
					+ Adv_Pay_Check_RadioBtn + "]");
		}
		logger.info("Click on CHECK radio button");
		// Click on Check radio button
		SeleniumUtils.clickOnElement(element);
		logger.info("Wait for CHECK fields to be displayed");
		// Wait for the ACH/WIRE options to display
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_Check_PaymentTo_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_Check_PaymentTo_Text_Element")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_Check_PaymentTo_Text_Element").getExptext());
		// Identify CHECK fields
		logger.info("Identify if Payment To field is displayed ");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"Adv_Pay_Check_PaymentTo_Text_Element")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"Adv_Pay_Check_PaymentTo_Text_Element")
						.getLocatorvalue());
		// Get the Radio button text
		String Adv_Pay_Check_PaymentTo_Text_Element = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				Adv_Pay_Check_PaymentTo_Text_Element, Suite.objectRepositoryMap
						.get("Adv_Pay_Check_PaymentTo_Text_Element")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Advertising - Payments - Check - Payment To]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"Adv_Pay_Check_PaymentTo_Text_Element")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_Check_PaymentTo_Text_Element + "]");
			ReportUtils.setStepDescription(
					"[Advertising - Payments - ACH/WIRE - Recipient/Company Name]  "
							+ "field text matching failed", "",
					Suite.objectRepositoryMap.get(
							"Adv_Pay_Check_PaymentTo_Text_Element "
									+ "field text matching failed")
							.getExptext(),
					Adv_Pay_Check_PaymentTo_Text_Element, true);
			m_assert.fail("[Advertising - Payments - Check - Payment To]  "
					+ "field text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"Adv_Pay_Check_PaymentTo_Text_Element")
							.getExptext() + "] and the return text is ["
					+ Adv_Pay_Check_PaymentTo_Text_Element + "]");
		}
		// Identify Payment To text box
		logger.info("Identify Advertising-Payments-Payment To- text box");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap
						.get("Adv_Pay_Check_PaymentTo_textbox")
						.getLocatortype(),
				Suite.objectRepositoryMap
						.get("Adv_Pay_Check_PaymentTo_textbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Advertising - Payments - Payment To - "
					+ "text box");
			ReportUtils.setStepDescription("Unable to identify Advertising - "
					+ "Payments - Payment To - " + "text box", true);
			m_assert.fail("Unable to identify Advertising - Payments - Payment To - "
					+ "text box");
		}

	}

	@Test(priority = 23, dependsOnMethods = "loginAs")
	public void verifyAccount() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyAccount")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Testcase [verifyAccount] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Testcase [verifyAccount] is not added for execution",
							true);
			throw new SkipException("The runmode for verifyAccount set to No");
		}
		// read the param data
		testcaseArgs = getTestData("verifyAccount");
		logger.info("Starting [verifyAccount] execution");
		logger.info("Verify if user is on [Account] tab");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String AccountSubTabHeaderText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(AccountSubTabHeaderText,
				Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Account] page");
			logger.info("Navigate to [Account] tab");
			// Identify Account tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("AdAccountTab")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdAccountTab")
							.getLocatorvalue());
			// Get the text
			String AccountTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(AccountTabText,
					Suite.objectRepositoryMap.get("AdAccountTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Account] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdAccountTab")
								.getExptext()
						+ "] and the return text is ["
						+ AccountTabText + "]");
				ReportUtils.setStepDescription(
						"[Account] tab text matching failed", "",
						Suite.objectRepositoryMap.get("AdAccountTab")
								.getExptext(), AccountTabText, true);
				m_assert.fail("[Account] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get("AdAccountTab")
								.getExptext()
						+ "] and the return text is ["
						+ AccountTabText + "]");
			}
			// Click on Account tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(3);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
							.getLocatorvalue());
			// Get the text
			AccountSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(AccountSubTabHeaderText,
					Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Account] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdAccountTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ AccountSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Account] sub tab header text matching failed", "",
						Suite.objectRepositoryMap.get("AdAccountTabHeaderText")
								.getExptext(), AccountSubTabHeaderText, true);
				m_assert.fail("[Account] sub tab header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"AdAccountTabHeaderText").getExptext()
						+ "] and the return text is ["
						+ AccountSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Account] tab");
		m_assert.assertAll();
	}

	@Test(priority = 24, dependsOnMethods = { "loginAs" })
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
		if (advertising != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			testcases = advertising.getCase();
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
