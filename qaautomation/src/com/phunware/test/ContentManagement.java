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
 * @since 09/26/13 This class is used to check Content Management
 *        functionalities
 * 
 */
@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class ContentManagement extends Suite {
	private static final Logger logger = Logger
			.getLogger(ContentManagement.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private String childSuite = "contentManagement";
	private boolean suiteExecution = false;
	private static WebElement element = null;
	private static boolean isTextMatching;
	private static boolean isVerified;
	private SoftAssert m_assert;
	private Testcase contentmanagement = null;

	@SuppressWarnings("unused")
	@BeforeClass
	private void setUp() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Index Page Description for Results
		ReportUtils.setIndexPageDescription();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if [ContentManagement] test suite is added for execution
		for (String testSuite : scenarioslist) {
			if (childSuite.equalsIgnoreCase(testSuite)) {
				suiteExecution = true;
				break;
			}
		}
		if (!suiteExecution) {
			logger.info("Test suite [ContentManagement] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test suite [ContentManagement] is not added for execution",
							false);
			throw new SkipException(
					"Test suite [ContentManagement] is not added for execution");
		}
		logger.info("reading ContentManagement Input file");
		contentmanagement = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.CONTENEMANAGEMENT_FILE,
				Testcase.class);
		if (contentmanagement != null) {
			List<Case> testcases = contentmanagement.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());
				}
			}
		}
		if (testcaseList.size() == 0) {
			logger.warn("No testCases added for execution in [ContentManagement] test suite");
			ReportUtils
					.setStepDescription(
							"No testCases added for execution in [ContentManagement] test suite",
							false);
			throw new SkipException(
					"No testCases added for execution in [Advertising] test suite");
		}
		logger.info("reading [ContentManagement] Input file successful");
		logger.info(" {" + testcaseList
				+ "} for execution in [ContentManagement] suite");
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
					+ " in [ContentManagement]");
			ReportUtils.setStepDescription(
					"Test case [loginAs] is not added for execution"
							+ " in [ContentManagement]", false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution"
							+ " in [ContentManagement]");
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
				boolean isClicked = SeleniumUtils.clickOnElement(element);
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
			boolean isClicked = SeleniumUtils.clickOnElement(element);
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
	public void verifyContentLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyContentLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyContentLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyContentLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyContentLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyContentLayout");
		logger.info("Starting [verifyContentLayout] execution");
		logger.info("Navigating to [Content Management] tab");
		// Wait for the page to load
		SeleniumUtils.sleepThread(3);
		// Identify Content Management tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ContentTablogoText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentTablogoText")
						.getLocatorvalue());

		// Get the text of the tab
		String ContentManagementTabText = SeleniumUtils.getText(element);
		// Compare the text with exp text
		isTextMatching = SeleniumUtils.assertEqual(ContentManagementTabText,
				Suite.objectRepositoryMap.get("ContentTablogoText")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Content Management] tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get("ContentTablogoText")
							.getExptext()
					+ "] and the actual return text is ["
					+ ContentManagementTabText + "]");
			ReportUtils.setStepDescription(
					"[Content Management] tab text matching failed", "",
					Suite.objectRepositoryMap.get("ContentTablogoText")
							.getExptext(), ContentManagementTabText, true);
			m_assert.fail("[Content Management] tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get("ContentTablogoText")
							.getExptext()
					+ "] and the actual return text is ["
					+ ContentManagementTabText + "]");
		}
		// Click on CMS tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Verify all the sub tabs in [Content Management] tab");
		// Identify Content tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getLocatorvalue());
		// Get the text
		String ContentSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabText,
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Content] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementContentTab").getExptext()
					+ "] and the actual return text is ["
					+ ContentSubTabText
					+ "]");
			ReportUtils.setStepDescription(
					"[Content] sub tab text matching failed", "",
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab").getExptext(),
					ContentSubTabText, true);
			m_assert.fail("[Content] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementContentTab").getExptext()
					+ "] and the actual return text is ["
					+ ContentSubTabText
					+ "]");
		}
		logger.info("Verification of [Content] tab text is successful");
		logger.info("Verify [Structure] tab");
		// Identify Structure tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentManagementStructureTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementStructureTab")
						.getLocatorvalue());
		// Get the text
		String StructureSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(StructureSubTabText,
				Suite.objectRepositoryMap.get("ContentManagementStructureTab")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Structure] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext()
					+ "] and the actual return text is ["
					+ StructureSubTabText
					+ "]");
			ReportUtils.setStepDescription(
					"[Structure] sub tab text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext(),
					StructureSubTabText, true);
			m_assert.fail("[Structure] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext()
					+ "] and the actual return text is ["
					+ StructureSubTabText
					+ "]");
		}
		logger.info("Verification of [Structure] tab text is successful");
		logger.info("Verify [Schemas] tab");
		// Identify Schema tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Schemas] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTab").getExptext()
					+ "] and the actual return text is ["
					+ SchemasSubTabText
					+ "]");
			ReportUtils.setStepDescription(
					"[Schemas] sub tab text matching failed", "",
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext(),
					SchemasSubTabText, true);
			m_assert.fail("[Schemas] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTab").getExptext()
					+ "] and the actual return text is ["
					+ SchemasSubTabText
					+ "]");
		}
		logger.info("Verification of [Schemas] tab text is successful");
		logger.info("Verify [S3] tab");
		/*
		 * // Identify S3 tab element = SeleniumUtils.findobject(
		 * Suite.objectRepositoryMap.get("ContentManagementS3Tab")
		 * .getLocatortype(),
		 * Suite.objectRepositoryMap.get("ContentManagementS3Tab")
		 * .getLocatorvalue()); // Get the text String S3SubTabText =
		 * SeleniumUtils.getText(element); isTextMatching =
		 * SeleniumUtils.assertEqual(S3SubTabText,
		 * Suite.objectRepositoryMap.get("ContentManagementS3Tab")
		 * .getExptext()); if (!isTextMatching) {
		 * logger.error("[S3] sub tab text matching failed: The expected text is ["
		 * + Suite.objectRepositoryMap.get("ContentManagementS3Tab")
		 * .getExptext() + "] and the actual return text is [" + S3SubTabText +
		 * "]");
		 * ReportUtils.setStepDescription("[S3] sub tab text matching failed",
		 * "", Suite.objectRepositoryMap.get("ContentManagementS3Tab")
		 * .getExptext(), S3SubTabText, true);
		 * m_assert.fail("[S3] sub tab text matching failed: The expected text is ["
		 * + Suite.objectRepositoryMap.get("ContentManagementS3Tab")
		 * .getExptext() + "] and the actual return text is [" + S3SubTabText +
		 * "]"); } logger.info("Verification of [S3] tab text is successful");
		 */
		logger.info("Verification of all the sub tabs in [Content Management] tab is successful");
		logger.info("Verify if user is on [Content] sub tab in [Content Management] tab");
		// Identify Content page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(
				ContentSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Content] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Content] sub tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getExptext(), ContentSubTabHeaderText, true);
			m_assert.fail("[Content] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 2, dependsOnMethods = { "loginAs" })
	public void verifyStructureLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyStructureLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyStructureLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyStructureLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyStructureLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifyStructureLayout");
		logger.info("Starting [verifyStructureLayout] execution");
		logger.info("Navigate to [Structure] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Structure tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ContentManagementStructureTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementStructureTab")
						.getLocatorvalue());
		// Get the text
		String StructureSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(StructureSubTabText,
				Suite.objectRepositoryMap.get("ContentManagementStructureTab")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Structure] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext()
					+ "] and the actual return text is ["
					+ StructureSubTabText
					+ "]");
			ReportUtils.setStepDescription(
					"[Structure] sub tab text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext(),
					StructureSubTabText, true);
			m_assert.fail("[Structure] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext()
					+ "] and the actual return text is ["
					+ StructureSubTabText
					+ "]");
		}
		logger.info("Verification of [Structure] tab text is successful");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if user is on [Structure] tab");
		// Identify Structure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(
						StructureSubTabHeaderText,
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext());
		if (!isTextMatching) {
			logger.error("[Structure] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ StructureSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Structure] sub tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext(), StructureSubTabHeaderText, true);
			m_assert.fail("[Structure] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ StructureSubTabHeaderText + "]");
		}
		logger.info("Navigation to [Structure] tab is successful");
		logger.info("Verify 'New Container' button is present in [Structure] sub tab");
		// Identify New Container Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Container' button in [Structure] sub tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'New Container' button in [Structure] sub tab",
							true);
			m_assert.fail("Unable to identify 'New Container' button in [Structure] sub tab");
		}
		m_assert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods = { "loginAs" })
	public void verifySchemasLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifySchemasLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifySchemasLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifySchemasLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifySchemasLayout] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("verifySchemasLayout");
		logger.info("Starting [verifySchemasLayout] execution");
		logger.info("Navigate to [Schemas] tab");
		// Identify Schemas tab
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[Schemas] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTab").getExptext()
					+ "] and the actual return text is ["
					+ SchemasSubTabText
					+ "]");
			ReportUtils.setStepDescription(
					"[Schemas] sub tab text matching failed", "",
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext(),
					SchemasSubTabText, true);
			m_assert.fail("[Schemas] sub tab text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTab").getExptext()
					+ "] and the actual return text is ["
					+ SchemasSubTabText
					+ "]");
		}
		logger.info("Identification of [Schemas] tab text is successful");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if user is on [Schemas] tab");
		// Identify Schemas tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemasSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Schemas] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ SchemasSubTabHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Schemas] sub tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext(), SchemasSubTabHeaderText, true);
			m_assert.fail("[Schemas] sub tab header text matching failed: The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ SchemasSubTabHeaderText + "]");
		}
		logger.info("Navigation to [Schemas] tab is successful");
		logger.info("Verify 'New Schema' button is present in [Schemas] sub tab");
		// Identify New Schema button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabNewSchemaBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabNewSchemaBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Schema' button in [Schemas] sub tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'New Schema' button in [Schemas] sub tab",
							true);
			m_assert.fail("Unable to identify 'New Schemas' button in [Schemas] sub tab");
		}
		m_assert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods = { "loginAs" })
	public void verifyCreateNewSchemaLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyCreateNewSchemaLayout")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifyCreateNewSchemaLayout] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [verifyCreateNewSchemaLayout] is not added for execution",
							false);
			throw new SkipException(
					"Test case [verifyCreateNewSchemaLayout] is not added for execution");
		}
		// read the param data
		testcaseArgs = getTestData("verifyCreateNewSchemaLayout");
		logger.info("Starting [verifyCreateNewSchemaLayout] execution");
		logger.info("Verify if User is on [Schemas] tab");
		// Identify Schemas tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemasSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Schemas] tab. User is on ["
					+ SchemasSubTabHeaderText + "]");
			logger.info("Navigate to [Schemas] tab");
			// Identify Schemas tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schema tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Schemas] tab");
		logger.info("Click on 'New Schema' button");
		// Identify New Schema button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabNewSchemaBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabNewSchemaBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'New Schema' button in [Schemas] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'New Schema' button in [Schemas] tab",
					true);
			m_assert.fail("Unable to identify 'New Schema' button in [Schemas] tab");
		}
		// Click on New Schema button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(8);
		logger.info("Click operation on 'New Schema' button is successful");
		logger.info("Verify if User is on 'Create New Schema' page");
		// Identify New Schema page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page header text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaHeaderText + "]");
			ReportUtils.setStepDescription(
					"'Create New Schema' page header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext(), CreateNewSchemaHeaderText, true);
			m_assert.fail("'Create New Schema' page header text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaHeaderText + "]");
		}
		logger.info("User is on 'Create New Schema' page");
		logger.info("Verify 'Create New Schema' page Layout");
		// Identify Create New Schema label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaNameLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaNameLabelText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel").getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page 'Name' label text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
			ReportUtils
					.setStepDescription(
							"'Create New Schema' page 'Name' label text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"CMSchemasTabCreateNewSchemaNameLabel")
									.getExptext(),
							CreateNewSchemaNameLabelText, true);
			m_assert.fail("'Create New Schema' page 'Name' label text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
		}
		// Identify New Schema text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field input textbox in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Name' field input textbox in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'Name' field input textbox in [Create New Schema] in [Schema] tab");
		}
		// Identify Description label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaDescriptionLabelText = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaDescriptionLabelText, Suite.objectRepositoryMap
						.get("CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page 'Description' label text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaDescriptionLabelText + "]");
			ReportUtils
					.setStepDescription(
							"'Create New Schema' page 'Description' label text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("CMSchemasTabCreateNewSchemaDescriptionLabel")
									.getExptext(),
							CreateNewSchemaDescriptionLabelText, true);
			m_assert.fail("'Create New Schema' page 'Description' label text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaDescriptionLabelText + "]");
		}
		// Identify Description text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description' field input textbox in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Description' field input textbox in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'Description' field input textbox in [Create New Schema] in [Schema] tab");
		}
		logger.info("Verification of 'Name' and 'Description' fields are successful");
		// Identify Attribute button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD ATTRIBUTE' button in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'ADD ATTRIBUTE' button in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'ADD ATTRIBUTE' button in [Create New Schema] in [Schema] tab");
		}
		// Identify Field button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD FIELD' button in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'ADD FIELD' button in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'ADD FIELD' button in [Create New Schema] in [Schema] tab");
		}
		logger.info("Verification of 'ADD ATTRIBUTE' & 'ADD FIELD' buttons successful");
		// Identify Attributes label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAttributesLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAttributesLabel")
						.getLocatorvalue());
		// Get the texg
		String CreateNewSchemaAttributesLabelText = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaAttributesLabelText, Suite.objectRepositoryMap
						.get("CMSchemasTabCreateNewSchemaAttributesLabel")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page 'Attributes' label text matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaAttributesLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaAttributesLabelText + "]");
			ReportUtils
					.setStepDescription(
							"'Create New Schema' page 'Attributes' label text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("CMSchemasTabCreateNewSchemaAttributesLabel")
									.getExptext(),
							CreateNewSchemaAttributesLabelText, true);
			m_assert.fail("'Create New Schema' page 'Attributes' label text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaAttributesLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaAttributesLabelText + "]");
		}
		// Fields label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaFieldsLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaFieldsLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaFieldsLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaFieldsLabelText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaFieldsLabel").getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page 'Fields' label text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaFieldsLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaFieldsLabelText + "]");
			ReportUtils
					.setStepDescription(
							"'Create New Schema' page 'Fields' label text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"CMSchemasTabCreateNewSchemaFieldsLabel")
									.getExptext(),
							CreateNewSchemaFieldsLabelText, true);
			m_assert.fail("'Create New Schema' page 'Fields' label text matching failed. "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaFieldsLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaFieldsLabelText + "]");
		}
		logger.info("Verification of Attributes & Fields labels successful");
		// Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Cancel' button in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Cancel' button in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'Cancel' button in [Create New Schema] in [Schema] tab");
		}
		// Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Save' button in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'Save' button in [Create New Schema] in [Schema] tab");
		}
		logger.info("Verification of 'CANCEL' & 'SAVE' buttons successful");
		logger.info("Click on 'ADD ATTRIBUTE' button and Verify fields");
		// Click on Add Attribute button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(5);
		// Identify Attributes fields
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeFields")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeFields")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD ATTRIBUTE' fields (Key & Val) "
					+ "button in [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD ATTRIBUTE' fields (Key & Val) "
							+ "button in [Create New Schema] in [Schema] tab",
					true);
			m_assert.fail("Unable to identify 'ADD ATTRIBUTE' fields (Key & Val) "
					+ "button in [Create New Schema] in [Schema] tab");
		}
		// Identify Attributes fields close button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaAddAttributeFieldsCloseBtn")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaAddAttributeFieldsCloseBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD ATTRIBUTE' fields (Close) button in "
					+ "[Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD ATTRIBUTE' fields (Close) button in "
							+ "[Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD ATTRIBUTE' fields (Close) button in "
					+ "[Create New Schema] in [Schema] tab");
		}
		// Click on Close button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Verification of 'ADD ATTRIBUTE' fields successful");
		logger.info("Verify 'ADD FIELDS' fields");
		// Click on Fields button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(6);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldFields")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldFields")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD FIELD' fields (Label & Field & Type)"
					+ " in [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD FIELD' fields (Label & Field & Type)"
							+ " in [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD FIELD' fields (Label & Field & Type)"
					+ " in [Create New Schema] in [Schema] tab");
		}
		// Identify Close button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldFieldsCloseBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldFieldsCloseBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD FIELD' fields (Close) button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD FIELD' fields (Close) button in"
							+ " [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD FIElD' fields (Close) button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		m_assert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = { "loginAs" })
	public void validationsOnCreateNewSchema() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		List<WebElement> elementsList = null;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validationsOnCreateNewSchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [validationsOnCreateNewSchema] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [validationsOnCreateNewSchema] is not added"
							+ " for execution", false);
			throw new SkipException(
					"Test case [validationsOnCreateNewSchema] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("validationsOnCreateNewSchema");
		logger.info("Starting [validationsOnCreateNewSchema] execution");
		logger.info("Verify if User is on [Create New Schema] page");
		// Identify Create New Schema header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Create New Schema] page");
			logger.info("Navigate to [Schemas] tab");
			// Identify Schemas tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schemas tab
			SeleniumUtils.clickOnElement(element);
			// Identify Schemas header element having expected text
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			// Identify Schemas header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
			// Identify New Schema button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'New Schema' button in"
						+ " [Schemas] tab");
				ReportUtils.setStepDescription(
						"Unable to identify 'New Schema' button in"
								+ " [Schemas] tab", true);
				m_assert.fail("Unable to identify 'New Schema' button in"
						+ " [Schemas] tab");
			}
			// Click on New Schema button
			SeleniumUtils.clickOnElement(element);
			// Identify Create New Schema header element having expected text
			SeleniumUtils.wait_For_Element_To_Display_Having_Text(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatorvalue(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext());
			// Identify Schema header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatorvalue());
			// Get the text
			CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					CreateNewSchemaPageHeaderText, Suite.objectRepositoryMap
							.get("CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Create New Schema] tab header text matching failed"
						+ ": The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ CreateNewSchemaPageHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Create New Schema] tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), CreateNewSchemaPageHeaderText,
						true);
				m_assert.fail("[Create New Schema] tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ CreateNewSchemaPageHeaderText + "]");
			}
		}
		logger.info("User is on [Create New Schema] tab");
		logger.info("Verify validations on Create new Schema ");
		// Identify Create New Schema name label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaNameLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaNameLabelText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel").getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page 'Name' label text matching failed."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
			ReportUtils
					.setStepDescription(
							"'Create New Schema' page 'Name' label text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"CMSchemasTabCreateNewSchemaNameLabel")
									.getExptext(),
							CreateNewSchemaNameLabelText, true);
			m_assert.fail("'Create New Schema' page 'Name' label text matching failed."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
		}
		// Identify Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Name' field input textbox in"
							+ " [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'Name' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in [Create New Schema] in"
					+ " [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Save' button in [Create New Schema] in"
							+ " [Schema] tab", true);
			m_assert.fail("Unable to identify 'Save' button in [Create New Schema] in"
					+ " [Schema] tab");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Click operation is successful on 'SAVE' button. "
				+ "Verify 'Name' field is mandatory for creating container");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("Validation failed at Schema Name field. User able to"
					+ " create Schema without any data in Name field");
			ReportUtils.setStepDescription(
					"Validation failed at Schema Name field. User able to"
							+ " create Schema without any data in Name field",
					true);
			m_assert.fail("Validation failed at Schema Name field. User able to"
					+ " create Schema without any data in Name field");
		}
		logger.info("Validation on 'Name' field is successful. User is on [CREATE NEW SCHEMA] page");
		// Identify Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatorvalue());
		logger.info("Enter the schema [" + testcaseArgs.get("schemaName")
				+ "] in Name textbox");
		// Enter Schema name
		SeleniumUtils.type(element, testcaseArgs.get("schemaName"));
		logger.info("Entering Schema in Name textbox is successful");
		logger.info("Click on 'SAVE' button");
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in [Create New Schema]"
					+ " in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Save' button in [Create New Schema]"
							+ " in [Schema] tab", true);
			m_assert.fail("Unable to identify 'Save' button in [Create New Schema]"
					+ " in [Schema] tab");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		logger.info("Click operation is successful on 'SAVE' button. "
				+ "Verify 'Add Field' is mandatory for creating container");
		// Identify Error message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaFieldsErrorMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaFieldsErrorMSG")
						.getLocatorvalue());
		// Get the text
		String FieldsErrorMessage = SeleniumUtils.perform_SubString_And_Trim(
				SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				FieldsErrorMessage,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaFieldsErrorMSG")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Error Messgae' at 'Field' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaFieldsErrorMSG")
							.getExptext() + "] and the return text is ["
					+ FieldsErrorMessage + "]");
			ReportUtils.setStepDescription(
					"'Error Messgae' at 'Field' text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaFieldsErrorMSG")
							.getExptext(), FieldsErrorMessage, true);
			m_assert.fail("'Error Messgae' at 'Field' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaFieldsErrorMSG")
							.getExptext() + "] and the return text is ["
					+ FieldsErrorMessage + "]");
		}
		logger.info("Validation success at Fields in [CREATE NEW SCHEMA]");
		// Identify Add Fields button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD Field' button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD Field' button in"
							+ " [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD Field' button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// Click on Add Attributes button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Save' button in"
							+ " [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'Save' button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Click operation is successful on 'SAVE' button. "
				+ "Verify 'Name' field is mandatory for creating container");
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		// Identify header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext() + "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Create New Schema] tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext(), CreateNewSchemaPageHeaderText, true);
			m_assert.fail("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
		}
		// Identify Attributes field sets
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatorvalue());
		for (int i = 0; i < 1; i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, "FieldsValue" + (i + 1));
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Click operation is successful on 'SAVE' button. "
				+ "Verify 'Name' field is mandatory for creating container");
		// Identify the header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext() + "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Create New Schema] tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext(), CreateNewSchemaPageHeaderText, true);
			m_assert.fail("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
		}
		for (int i = 1; i < 2; i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, "FieldsValue" + (i + 1));
		}
		// Attribute button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatorvalue());
		// Click on Attribute button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Click operation on ADD ATTRIBUTE is successful");
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Click operation is successful on 'SAVE' button. "
				+ "Verify 'Name' field is mandatory for creating container");
		// Identify the header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext() + "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Create New Schema] tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext(), CreateNewSchemaPageHeaderText, true);
			m_assert.fail("[Create New Schema] tab header text matching failed: "
					+ "The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
		}
		// Identify Attributes key list
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatorvalue());
		for (int i = 0; i < 1; i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, "AttributeValue" + (i + 1));
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Click operation is successful on 'SAVE' button. "
				+ "Verify 'Name' field is mandatory for creating container");
		// Identify the header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.error("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext() + "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
			ReportUtils.setStepDescription(
					"[Create New Schema] tab header text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext(), CreateNewSchemaPageHeaderText, true);
			m_assert.fail("[Create New Schema] tab header text matching failed:"
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaPageHeaderText + "]");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementSchemasTab")
						.getLocatorvalue());
		// Click on Schema tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		m_assert.assertAll();
	}

	@Test(priority = 6, dependsOnMethods = { "loginAs" })
	public void createNewSchema() {
		boolean forExecution = false;
		List<WebElement> elementsList = null;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewSchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createNewSchema] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [createNewSchema] is not added for execution",
					false);
			throw new SkipException(
					"Test case [createNewSchema] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("createNewSchema");
		logger.info("Starting [createNewSchema] execution");
		logger.info("Verify if User is on [Create New Schema] page");
		SeleniumUtils.sleepThread(6);
		// Identify Create New Schema header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Create New Schema] page");
			logger.info("Navigate to [Schemas] tab");
			// Identify Schema tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						SchemasSubTabText,
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						true);
				m_assert.fail("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schemas tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(6);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
			// Identify New Schema button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'New Schema' button in [Schemas] tab");
				ReportUtils
						.setStepDescription(
								"Unable to identify 'New Schema' button in [Schemas] tab",
								true);
				m_assert.fail("Unable to identify 'New Schema' button in [Schemas] tab");
			}
			// Click on New Schema button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatorvalue());
			// Get the text
			CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					CreateNewSchemaPageHeaderText, Suite.objectRepositoryMap
							.get("CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Create New Schema] tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Create New Schema] tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), CreateNewSchemaPageHeaderText,
						true);
				m_assert.fail("[Create New Schema] tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Create New Schema] tab");
		logger.info("Create new Schema ");
		// Identify Create New Schema label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaNameLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaNameLabelText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel").getExptext());
		if (!isTextMatching) {
			logger.error("'Create New Schema' page 'Name' label text matching failed."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
			ReportUtils
					.setStepDescription(
							"'Create New Schema' page 'Name' label text matching failed",
							"",
							Suite.objectRepositoryMap.get(
									"CMSchemasTabCreateNewSchemaNameLabel")
									.getExptext(),
							CreateNewSchemaNameLabelText, true);
			m_assert.fail("'Create New Schema' page 'Name' label text matching failed."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
		}
		// Identify Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field input textbox in "
					+ "[Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Name' field input "
							+ "textbox in [Create New Schema] in [Schema] tab",
					true);
			m_assert.fail("Unable to identify 'Name' field input textbox in "
					+ "[Create New Schema] in [Schema] tab");
		}
		// enter the schema name in Name textbox
		SeleniumUtils.type(element, testcaseArgs.get("schemaName"));
		// Identify Description text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description' field input "
					+ "textbox in [Create New Schema] in [Schema] tab");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Description' "
									+ "field input textbox in [Create New Schema] in [Schema] tab",
							true);
			m_assert.fail("Unable to identify 'Description' field input "
					+ "textbox in [Create New Schema] in [Schema] tab");
		}
		// enter schema description in Schema description textbox
		SeleniumUtils.type(element, testcaseArgs.get("description"));
		// Identify Attribute button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD ATTRIBUTE' button in "
					+ "[Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD ATTRIBUTE' button in "
							+ "[Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD ATTRIBUTE' button in "
					+ "[Create New Schema] in [Schema] tab");
		}
		// click on ADD ATTRIBUTE button
		logger.info("Click on ADD ATTRIBUTE button");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Click operation on 'ADD ATTRIBUTE' button is successful. "
				+ "Enter data in 'Key' and 'Val' fields");
		// enter data in Key and val fields
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatorvalue());
		if (elementsList == null) {
			logger.error("Unable to identify 'Key' & 'Val' fields "
					+ "textbox in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Key' & 'Val' fields "
							+ "textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Key' & 'Val' fields "
					+ "textbox in [Create New Schema] page");
		}
		for (int i = 0; i < elementsList.size(); i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, "AttributeValue" + (i + 1));
		}
		// Identify Add Field button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD FIELD' button "
					+ "in [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription("Unable to identify 'ADD FIELD' "
					+ "button in [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD FIELD' "
					+ "button in [Create New Schema] in [Schema] tab");
		}
		logger.info("Click on 'ADD FIELD' button");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		logger.info("Click operation on 'ADD FIELD' button is successful");
		// enter data in Label, Field and Type fields
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatorvalue());
		if (elementsList == null) {
			logger.error("Unable to identify 'Label' & 'Field' & 'Description' "
					+ "fields textbox in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Label' & 'Field' & 'Description' "
							+ "fields textbox in [Create New Schema] page",
					true);
			m_assert.fail("Unable to identify 'Label' & 'Field' & 'Description' "
					+ "fields textbox in [Create New Schema] page");
		}
		for (int i = 0; i < elementsList.size(); i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, "FieldsValue" + (i + 1));
		}
		// Identify Field type dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldTypeDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldTypeDropDown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type' dropdown in "
					+ "[Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Type' dropdown in "
							+ "[Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'Type' dropdown in "
					+ "[Create New Schema] in [Schema] tab");
		}
		// Select drop down text
		SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldTypeDropDown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldTypeDropDown")
						.getLocatorvalue(), testcaseArgs.get("fieldsType"));
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button in "
					+ "[Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Save' button in "
							+ "[Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'Save' button in "
					+ "[Create New Schema] in [Schema] tab");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(6);
		// Identify Success message
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String SchemaSuccessMessage = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemaSuccessMessage,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG").getExptext());
		if (!isTextMatching) {
			logger.error("'Schema' success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
			ReportUtils.setStepDescription(
					"'Schema' success message text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext(), SchemaSuccessMessage, true);
			m_assert.fail("'Schema' success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
		}
		logger.info("Test case [createNewSchema] is successful");
	}

	@Test(priority = 7, dependsOnMethods = { "loginAs" })
	public void verifySchema() {
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifySchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [verifySchema] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifySchema] is not added for execution",
					false);
			throw new SkipException(
					"Test case [verifySchema] is not added for execution");
		}
		// reading param args
		testcaseArgs = getTestData("verifySchema");
		logger.info("Starting [verifySchema] execution");
		logger.info("Verify if User is on [Schemas] page");
		// Identify Scehmas page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemasSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Schemas] tab");
			// Identify Schemas tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schemas tab
			SeleniumUtils.clickOnElement(element);
			// Identify Schemas page header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Schemas] tab");
		logger.info("Verify if Schemas List present in [Schemas] tab");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(4);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Schemas List in [Schemas] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Schemas List in [Schemas] tab", true);
			m_assert.fail("Unable to identify Schemas List in [Schemas] tab");
		}
		logger.info("Verification of Schemas List is successful. "
				+ "Schemas List is present in [Schemas] tab");
		logger.info("Verify [" + testcaseArgs.get("schemaName")
				+ "] schema present in the Schemas List");
		boolean isVerified = SeleniumCustomUtils.checkSchemaInSchemaList(
				element, testcaseArgs.get("schemaName"));
		if (!isVerified) {
			logger.error("Unable to identify ["
					+ testcaseArgs.get("schemaName")
					+ "] schema in Schemas List");
			ReportUtils.setStepDescription("Unable to identify ["
					+ testcaseArgs.get("schemaName")
					+ "] schema in Schemas List", true);
			m_assert.fail("Unable to identify ["
					+ testcaseArgs.get("schemaName")
					+ "] schema in Schemas List");
		}
		logger.info("[" + testcaseArgs.get("schemaName")
				+ "] schema present in the Schemas List");
		logger.info("Execution of verifySchema is successful");
	}

	@Test(priority = 8, dependsOnMethods = { "loginAs" })
	public void editSchema() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editSchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [editSchema] is not added for execution");
			ReportUtils.setStepDescription("Test case [editSchema] is not"
					+ " added for execution", false);
			throw new SkipException(
					"Test case [editSchema] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("editSchema");
		logger.info("Starting [editSchema] execution");
		logger.info("Verify if User is on [Schemas] page");
		// Identify Schemas page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemasSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Schemas] tab");
			// Identify Schemas tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schemas tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Schemas page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Schemas] tab");
		logger.info("Verify if Schemas List present in [Schemas] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Schemas list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Schemas List in [Schemas] tab");
			ReportUtils.setStepDescription("Unable to identify Schemas List"
					+ " in [Schemas] tab", true);
			m_assert.fail("Unable to identify Schemas List in [Schemas] tab");
		}
		logger.info("Verification of Schemas List is successful");
		logger.info("Edit the [" + testcaseArgs.get("schemaName") + "] schema");
		for (int i = 1; i < 10; i++) {
			// Identify Schemas list
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabSchemasList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabSchemasList")
							.getLocatorvalue());
			// Validate the specific Schema present in list
			isVerified = SeleniumCustomUtils.clickAtSchemaInSchemaList(element,
					testcaseArgs.get("schemaName"));
			if (isVerified) {
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
			// If Next link is not null then Click
			if (NextLinkelement != null) {
				SeleniumCustomUtils.clickAtNextLinkOfApplications(
						Suite.objectRepositoryMap.get(
								"ClientApplicationsPaginationNextLink")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("ClientApplicationsPaginationNextLink")
								.getLocatorvalue());
			}
			SeleniumUtils.sleepThread(3);
		}
		if (!isVerified) {
			logger.error("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema");
			ReportUtils.setStepDescription("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema", true);
			m_assert.fail("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema");
		}
		// Identify New Schema Text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameTextBoxEdit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameTextBoxEdit")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' textbox in "
					+ "[Edit Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Name'"
					+ " textbox in [Edit Schema] page", true);
			m_assert.fail("Unable to identify 'Name' textbox in"
					+ " [Edit Schema] page");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("schemaNewName"));
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Save' button"
					+ " in [Edit Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Save' button in [Edit Schema] page", true);
			m_assert.fail("Unable to identify 'Save' button"
					+ " in [Edit Schema] page");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// Identify Success Message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String SchemaSuccessMessage = SeleniumUtils.perform_SubString_And_Trim(
				SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemaSuccessMessage,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG").getExptext());
		if (!isTextMatching) {
			logger.error("'Schema' success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
			ReportUtils.setStepDescription(
					"'Schema' success message text" + " matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext(), SchemaSuccessMessage, true);
			m_assert.fail("'Schema' success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
		}
		/*
		 * SeleniumUtils.click( Suite.objectRepositoryMap.get(
		 * "ClientApplicationsPaginationActiveLink") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "ClientApplicationsPaginationActiveLink") .getLocatorvalue());
		 * 
		 * String url = SeleniumUtils.getURL(); int pageNum =
		 * Integer.parseInt(url.substring(45, url.length()).trim());
		 * if(pageNum!=1){ for (int i = 1; i < pageNum; i++) { element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "ClientApplicationsPaginationPrevLink") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "ClientApplicationsPaginationPrevLink") .getLocatorvalue()); if
		 * (element == null) { logger.error(
		 * "Unable to identify pagnation Prev link in [Your Applications] tab");
		 * SeleniumUtils.captureScreenShot(this.getClass().getSimpleName() + "_"
		 * + "verifyApplication" + "_" + count++); m_assert.fail(
		 * "Unable to identify pagnation Prev link in [Your Applications] tab");
		 * } SeleniumUtils.click( Suite.objectRepositoryMap.get(
		 * "ClientApplicationsPaginationPrevLink") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "ClientApplicationsPaginationPrevLink") .getLocatorvalue()); } url =
		 * SeleniumUtils.getURL(); pageNum = Integer.parseInt(url.substring(45,
		 * url.length()).trim()); } logger.info("User is on [" + pageNum +
		 * "] page");
		 */
		logger.info("Test case [editSchema] is successful");
		m_assert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = { "loginAs" })
	public void deleteSchema() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteSchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [deleteSchema] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [deleteSchema] is not added for execution",
					false);
			throw new SkipException(
					"Test case [deleteSchema] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("deleteSchema");
		logger.info("Starting [deleteSchema] execution");
		logger.info("Verify if User is on [Schemas] page");
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.wait_For_Element_To_Display_Having_Text(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		// Identify Schemas header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemasSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Schemas] tab");
			// Identify Schemas tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Schemas] tab");
		logger.info("Verify if Schemas List present in [Schemas] tab");
		// Identify Schemas list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Schemas List in [Schemas] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Schemas List in [Schemas] tab", true);
			m_assert.fail("Unable to identify Schemas List in [Schemas] tab");
		}
		logger.info("Verification of Schemas List is successful. Schemas List"
				+ " is present in [Schemas] tab");
		logger.info("Verify [" + testcaseArgs.get("schemaName")
				+ "] schema present in the Schemas List");
		for (int i = 1; i <= 10; i++) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabSchemasList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabSchemasList")
							.getLocatorvalue());
			isVerified = SeleniumCustomUtils.clickAtSchemaInSchemaList(element,
					testcaseArgs.get("schemaName"));
			if (isVerified) {
				break;
			}
			// Identify Next Link
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
			SeleniumUtils.sleepThread(3);
		}
		logger.info("[" + testcaseArgs.get("schemaName")
				+ "] schema present in the Schemas List");
		if (!isVerified) {
			logger.error("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema");
			ReportUtils.setStepDescription("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema", true);
			m_assert.fail("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema");
		}
		// Identify Delete button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabEditSchemaDeleteSchemaBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabEditSchemaDeleteSchemaBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Delete Schema' button in [Edit Schema] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Delete Schema' button in [Edit Schema] page",
							true);
			m_assert.fail("Unable to identify 'Delete Schema' button in [Edit Schema] page");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete schema button
			boolean isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Click on Delete button fails at the specified Schema");
				ReportUtils.setStepDescription(
						"Click on Delete button fails at the specified Schema",
						true);
				m_assert.fail("Click on Delete button fails at the specified Schema");
			}
		} else {
			boolean isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Click on Delete button fails at the specified Schema");
				ReportUtils.setStepDescription(
						"Click on Delete button fails at the specified Schema",
						true);
				m_assert.fail("Click on Delete button fails at the specified Schema");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
			SeleniumUtils.sleepThread(3);
		}
		// Identify Delete success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDeleteSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDeleteSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String SchemaSuccessMessage = SeleniumUtils.perform_SubString_And_Trim(
				SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemaSuccessMessage,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDeleteSuccessMSG")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Schema' success message text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDeleteSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
			ReportUtils.setStepDescription(
					"'Schema' success message text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDeleteSuccessMSG")
							.getExptext(), SchemaSuccessMessage, true);
			m_assert.fail("'Schema' success message text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDeleteSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 10, dependsOnMethods = { "loginAs" })
	public void createNewComplexSchema() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		List<WebElement> elementsList = null;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewComplexSchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createNewComplexSchema] is not"
					+ " added for execution");
			ReportUtils.setStepDescription("Test case [createNewComplexSchema]"
					+ " is not added for execution", true);
			throw new SkipException(
					"Test case [createNewComplexSchema] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("createNewComplexSchema");
		logger.info("Starting [createNewComplexSchema] execution");
		logger.info("Verify if User is on [Create New Schema] page");
		// Identify Create New Schema page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Create New Schema] page");
			logger.info("Navigate to [Schemas] tab");
			// Identify Schema tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schemas tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Schemas page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] page header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
			// Identify Create New Schema button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'New Schema' button"
						+ " in [Schemas] tab");
				ReportUtils.setStepDescription(
						"Unable to identify 'New Schema'"
								+ " button in [Schemas] tab", true);
				m_assert.fail("Unable to identify 'New Schema' button"
						+ " in [Schemas] tab");
			}
			// Click on Create New Schema button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Create New Schema page
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatorvalue());
			// Get the text
			CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					CreateNewSchemaPageHeaderText, Suite.objectRepositoryMap
							.get("CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Create New Schema] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Create New Schema] page header text "
								+ "matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Create New Schema] tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Create New Schema] tab");
		logger.info("Create new Complex Schema ");
		// Identify Schema Label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaNameLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaNameLabelText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel").getExptext());
		if (!isTextMatching) {
			logger.error("Name field text matching failed in [Create New Schema] page."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
			ReportUtils.setStepDescription(
					"Name field text matching failed"
							+ " in [Create New Schema] page",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext(), CreateNewSchemaNameLabelText, true);
			m_assert.fail("Name field text matching failed in [Create New Schema] page."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
		}
		// Identify Name field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription("Unable to identify 'Name' field"
					+ " input textbox in [Create New Schema] in [Schema] tab",
					true);
			m_assert.fail("Unable to identify 'Name' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// enter the schema name in Name text box
		SeleniumUtils.type(element, testcaseArgs.get("schemaName"));
		// Identify Description Label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaDescriptionLabelText = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaDescriptionLabelText, Suite.objectRepositoryMap
						.get("CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Description' label text matching failed in [Create New Schema] page."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaDescriptionLabelText + "]");
			ReportUtils.setStepDescription(
					"'Description' label text matching failed in"
							+ " [Create New Schema] page",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext(), CreateNewSchemaDescriptionLabelText,
					true);
			m_assert.fail("'Create New Schema' page 'Description' label text"
					+ " matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext() + "] and the actual return text is ["
					+ CreateNewSchemaDescriptionLabelText + "]");
		}
		// Identify Description text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Description' field"
							+ " input textbox in [Create New Schema] in"
							+ " [Schema] tab", true);
			m_assert.fail("Unable to identify 'Description' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// enter schema description in Schema description textbox
		SeleniumUtils.type(element, testcaseArgs.get("description"));
		// Identify Add Attribute button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD ATTRIBUTE' button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD ATTRIBUTE' button"
							+ " in [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD ATTRIBUTE' button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		logger.info("Click on ADD ATTRIBUTE button");
		// Click on Add Attribute button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Click operation on 'ADD ATTRIBUTE' button is successful. "
				+ "Enter data in 'Key' and 'Val' fields");
		// enter data in Key and val fields
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatorvalue());
		if (elementsList == null) {
			logger.error("Unable to identify 'Key' & 'Val' fields"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Key' & 'Val' fields"
							+ " textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Key' & 'Val' fields"
					+ " textbox in [Create New Schema] page");
		}
		for (int i = 0; i < elementsList.size(); i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, testcaseArgs.get("attribute"));
		}
		// Identify Add Field button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD FIELD' button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription("Unable to identify 'ADD FIELD'"
					+ " button in [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD FIELD' button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		logger.info("Click on 'ADD FIELD' button");
		for (int i = 0; i < 5; i++) {
			SeleniumUtils.sleepThread(1);
			// Identify Add Field button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaAddFieldBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaAddFieldBtn")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
		}
		logger.info("Click operation on 'ADD FIELD' button is successful");
		// enter data in Label, Field and Type fields
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatorvalue());
		if (elementsList == null) {
			logger.error("Unable to identify 'Label' & 'Field' & 'Description'"
					+ " fields textbox in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Label' & 'Field'"
							+ " & 'Description' fields textbox in"
							+ " [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label' & 'Field' & 'Description'"
					+ " fields textbox in [Create New Schema] page");
		}
		// 1st field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel1")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label1' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label1'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label1' field textbox in"
					+ " [Create New Schema] page");
		}
		// Enter Fields
		SeleniumUtils.type(element, testcaseArgs.get("field1"));
		// Identify Field1
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField1")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field1' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field1'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field1' field textbox in"
					+ " [Create New Schema] page");
		}
		// Enter field value in First field
		SeleniumUtils.type(element, testcaseArgs.get("field1"));
		// Identify First field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription1")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description1' field"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Description1' field textbox"
					+ " in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description1' field"
					+ " textbox in [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field1Description"));
		// 2nd field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel2")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label2' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label2'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label2' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field2"));
		// 2nd field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField2")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field2' field textbox"
					+ " in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field2'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field2' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field2"));
		// 2nd field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription2")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description2' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Description2'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description2' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field2Description"));
		// 3rd field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label3' field textbox"
					+ " in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label3'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label3' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3"));
		// 3rd field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field3' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field3'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field3' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3"));
		// 3rd field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description3' field"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Description3' field textbox in"
					+ " [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description3' field"
					+ " textbox in [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3Description"));
		// 3rd Field drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type dropdown3' field in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Type"
					+ " dropdown3' field in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Type dropdown3' field in"
					+ " [Create New Schema] page");
		}
		// Select from drop down
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatorvalue(), testcaseArgs.get("field3Type"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("field3Type")
					+ "] from 'Type dropdown3' field in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to select [" + testcaseArgs.get("field3Type")
							+ "] from 'Type dropdown3' field in"
							+ " [Create New Schema] page", true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("field3Type")
					+ "] from 'Type dropdown3' field in [Create New Schema] page");
		}
		// 4th Field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label4' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label4'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label4' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field4"));
		// 4th Field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field4' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field4'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field4' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field4"));
		// 4th field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description4' field"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Description4'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description4' field textbox"
					+ " in [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field4Description"));
		// 4th field drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type dropdown4' field in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Type dropdown4' field in [Create New Schema] page",
					true);
			m_assert.fail("Unable to identify 'Type dropdown4' field in"
					+ " [Create New Schema] page");
		}
		// Select from drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatorvalue(), testcaseArgs.get("field4Type"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("field4Type")
					+ "] from 'Type dropdown3' field in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to select [" + testcaseArgs.get("field4Type")
							+ "] from 'Type dropdown3' field in "
							+ "[Create New Schema] page", true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("field4Type")
					+ "] from 'Type dropdown4' field in [Create New Schema] page");
		}
		// 5th Field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label5' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label5'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label5' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field5"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field5' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field5'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field5' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field5"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description5' field textbox"
					+ " in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Description5'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description5' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field5Description"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type dropdown5' field in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Type dropdown5'"
							+ " field in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Type dropdown5' field in"
					+ " [Create New Schema] page");
		}
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatorvalue(), testcaseArgs.get("field5Type"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("field5Type")
					+ "] from 'Type dropdown5' field in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to select [" + testcaseArgs.get("field5Type")
							+ "] from 'Type dropdown5' field in"
							+ " [Create New Schema] page", true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("field5Type")
					+ "] from 'Type dropdown5' field in [Create New Schema] page");
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// Identify Success Message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String SchemaSuccessMessage = SeleniumUtils.perform_SubString_And_Trim(
				SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemaSuccessMessage,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG").getExptext());
		if (!isTextMatching) {
			logger.error("'Schema' success message text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
			ReportUtils.setStepDescription(
					"'Schema' success message text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext(), SchemaSuccessMessage, true);
			m_assert.fail("'Schema' success message text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = { "loginAs" })
	public void createNewContainer() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewContainer")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createNewContainer] is not"
					+ " added for execution");
			ReportUtils.setStepDescription("Test case [createNewContainer]"
					+ " is not added for execution", true);
			throw new SkipException(
					"Test case [createNewContainer] is not added"
							+ " for execution");
		}
		// reading data
		testcaseArgs = getTestData("createNewContainer");
		logger.info("Starting [createNewContainer] execution");
		logger.info("Verify if user is on [STRUCTURE:CONTAINERS] tab");
		// Identify Structure tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(StructureTabHeaderText, Suite.objectRepositoryMap
						.get("ContentManagementStructureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [STRUCTURE:CONTAINERS] tab ");
			logger.info("Click on [Structure] sub tab");
			// Identify Structure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatorvalue());
			// Get the text
			String StructureTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE] tab text matching failed. The Expected"
						+ " text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE] tab text matching" + " failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext(),
						StructureTabText, true);
				m_assert.fail("[STRUCTURE] tab text matching failed. The Expected"
						+ " text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
			}
			// Click on Structure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			logger.info("Click operation on [STRUCTURE] tab is successful. "
					+ "Verify User is on [STRUCTURE:CONTAINERS] page");
			// Identify Structure page header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			StructureTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE:CONTAINERS]page header text matching"
						+ " failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE:CONTAINERS]page "
								+ "header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext(), StructureTabHeaderText, true);
				m_assert.fail("[STRUCTURE:CONTAINERS]page header text matching"
						+ " failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
			}
		}
		logger.info("User is on [STRUCTURE:CONTAINERS] tab");
		logger.info("Click on 'NEW CONTAINER' button");
		// Identify New Container button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'NEW CONTAINER' button in"
					+ " [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'NEW CONTAINER' button in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'NEW CONTAINER' button in"
					+ " [STRUCTURE:CONTAINERS] page");
		}
		// Click on New Container button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		logger.info("Verify 'Name' field 'SAVE' & 'CANCEL' buttons are displayed");
		// Container Name field
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getLocatorvalue());
		// Get the text
		String NewContainerNameFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerNameFieldText,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Name' field text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerNameField")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerNameFieldText + "]");
			ReportUtils
					.setStepDescription(
							"'Name' field text matching failed while"
									+ " creating new Container",
							"",
							Suite.objectRepositoryMap
									.get("ContentManagementStructureTabNewContainerNameField")
									.getExptext(), NewContainerNameFieldText,
							true);
			m_assert.fail("'Name' field text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerNameField")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerNameFieldText + "]");
		}
		// Identify Container Name text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name'field textbox for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'Name'field"
					+ " textbox for creating New Container in"
					+ " [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Name'field textbox for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'SAVE'button for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'SAVE'butto"
					+ "n for creating New Container in"
					+ " [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'SAVE'button for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
		}
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'CANCEL' button for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'CANCEL'"
					+ " button for creating New Container"
					+ " in [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'CANCEL' button for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("'Name' field 'SAVE' & 'CANCEL' buttons are displayed successfully ");
		logger.info("Create [" + testcaseArgs.get("containerName")
				+ "] container");
		// Identify Name field text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		// Clear the text
		SeleniumUtils.clearText(element);
		// Type Container Name
		SeleniumUtils.type(element, testcaseArgs.get("containerName"));
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if the Success message displyaed ");
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String NewContainerSuccessMsg = SeleniumUtils
				.perform_SubString_And_Trim(SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerSuccessMsg,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'New Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
			ReportUtils.setStepDescription("'New Container success msge' "
					+ "text matching failed", "", Suite.objectRepositoryMap
					.get("ContentManagementStructureTabNewContainerSuccessMsg")
					.getExptext(), NewContainerSuccessMsg, true);
			m_assert.fail("'New Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
		}
		logger.info("New Container Success message displayed successfully");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerName")
				+ "] displayed in Containers list");
		SeleniumUtils.sleepThread(2);
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List' in"
					+ " [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Containers List' in  [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'Containers List' in"
					+ "  [STRUCTURE:CONTAINERS] page");
		}
		// Validate the specific container present in list
		boolean isVerified = SeleniumCustomUtils
				.checkContainerInContainersList(element,
						testcaseArgs.get("containerName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerName")
				+ "] displyaed in Containers list");
		logger.info("Test case [createNewContainer] is successful");
		m_assert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = { "loginAs" })
	public void editContainer() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editContainer")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [editContainer] is not added for execution");
			ReportUtils.setStepDescription("Test case [editContainer] "
					+ "is not added for execution", false);
			throw new SkipException(
					"Test case [editContainer] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("editContainer");
		logger.info("Starting [editContainer] execution");
		logger.info("Verify if user is on [STRUCTURE:CONTAINERS] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Structure tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(StructureTabHeaderText, Suite.objectRepositoryMap
						.get("ContentManagementStructureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [STRUCTURE:CONTAINERS] tab ");
			logger.info("Click on [Structure] sub tab");
			// Identify Structure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatorvalue());
			// Get the text
			String StructureTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE] tab text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE] tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext(),
						StructureTabText, true);
				m_assert.fail("[STRUCTURE] tab text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
			}
			// Click on Structure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			logger.info("Click operation on [STRUCTURE] tab is successful. "
					+ "Verify User is on [STRUCTURE:CONTAINERS] page");
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			StructureTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE:CONTAINERS]page header text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE:CONTAINERS]page header text"
								+ " matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext(), StructureTabHeaderText, true);
				m_assert.fail("[STRUCTURE:CONTAINERS]page header text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
			}
		}
		logger.info("User is on [STRUCTURE:CONTAINERS] tab");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerName")
				+ "] displayed in Containers list");
		SeleniumUtils.sleepThread(2);
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List' in  "
					+ "[STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify "
					+ "'Containers List' in [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Containers List' "
					+ "in  [STRUCTURE:CONTAINERS] page");
		}
		SeleniumUtils.sleepThread(2);
		// Validate the specific container in list
		boolean isVerified = SeleniumCustomUtils
				.checkContainerInContainersList(element,
						testcaseArgs.get("containerName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerName")
				+ "] displyaed in Containers list");
		logger.info("Edit the container [" + testcaseArgs.get("containerName")
				+ "]");
		// Click on Edit link
		boolean isClicked = SeleniumCustomUtils
				.clickAtEditLinkOfContainersInStructure(element,
						testcaseArgs.get("containerName"));
		if (!isClicked) {
			logger.error("Error while click on Edit link of a container ["
					+ testcaseArgs.get("containerName") + "]");
			ReportUtils.setStepDescription(
					"Error while click on Edit link of a container ["
							+ testcaseArgs.get("containerName") + "]", true);
			m_assert.fail("Error while click on Edit link of a container ["
					+ testcaseArgs.get("containerName") + "]");
		}
		logger.info("Click operation successful on Edit link of a container ["
				+ testcaseArgs.get("containerName") + "] ");
		// Identify Container Name text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Name Field Textbox in"
					+ " [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify Name"
					+ " Field Textbox in [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify Name Field Textbox in"
					+ " [STRUCTURE:CONTAINERS] page");
		}
		// Clear text
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(4);
		// Type Container
		SeleniumUtils.type(element, testcaseArgs.get("containerNewName"));
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerEditSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerEditSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'SAVE' button in"
					+ " [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'SAVE' button in [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'SAVE' button in"
					+ " [STRUCTURE:CONTAINERS] page");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		logger.info("Verify if the Success message displyaed ");
		// Identify Save message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String NewContainerSuccessMsg = SeleniumUtils
				.perform_SubString_And_Trim(SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerSuccessMsg,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Edit Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
			ReportUtils.setStepDescription("'Edit Container success msge'"
					+ " text matching failed", "", Suite.objectRepositoryMap
					.get("ContentManagementStructureTabNewContainerSuccessMsg")
					.getExptext(), NewContainerSuccessMsg, true);
			m_assert.fail("'Edit Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
		}
		logger.info("Edit Container Success message displayed successfully");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerNewName")
				+ "] displayed in Containers list");
		SeleniumUtils.refreshPage();
		SeleniumUtils.sleepThread(5);
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List'"
					+ " in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Containers List' in  [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'Containers List'"
					+ " in  [STRUCTURE:CONTAINERS] page");
		}
		// Verify the edited Container in list
		isVerified = SeleniumCustomUtils.checkContainerInContainersList(
				element, testcaseArgs.get("containerNewName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerNewName")
				+ "] displyaed in Containers list");
		logger.info("Test case [editContainer] is successful ");
		m_assert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = { "loginAs" })
	public void deleteContainer() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if the test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteContainer")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [deleteContainer] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [deleteContainer] is not added for execution",
					false);
			throw new SkipException(
					"Test case [deleteContainer] is not added for execution");
		}
		// read param data
		testcaseArgs = getTestData("deleteContainer");
		logger.info("Starting [deleteContainer] execution");
		logger.info("Verify if user is on [STRUCTURE:CONTAINERS] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Structure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(StructureTabHeaderText, Suite.objectRepositoryMap
						.get("ContentManagementStructureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [STRUCTURE:CONTAINERS] tab ");
			logger.info("Click on [Structure] sub tab");
			// Identify Structure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatorvalue());
			// Get the text
			String StructureTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE] tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext(),
						StructureTabText, true);
				m_assert.fail("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			logger.info("Click operation on [STRUCTURE] tab is successful. "
					+ "Verify User is on [STRUCTURE:CONTAINERS] page");
			// Identify Structure tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			StructureTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE:CONTAINERS]page header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
				ReportUtils
						.setStepDescription(
								"[STRUCTURE:CONTAINERS]page header text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("ContentManagementStructureTabHeaderText")
										.getExptext(), StructureTabHeaderText,
								true);
				m_assert.fail("[STRUCTURE:CONTAINERS]page header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
			}
		}
		logger.info("User is on [STRUCTURE:CONTAINERS] tab");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerName")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List' in  [STRUCTURE:CONTAINERS] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Containers List' in  [STRUCTURE:CONTAINERS] page",
							true);
			m_assert.fail("Unable to identify 'Containers List' in  [STRUCTURE:CONTAINERS] page");
		}
		// Verify if the specific container is available
		boolean isVerified = SeleniumCustomUtils
				.checkContainerInContainersList(element,
						testcaseArgs.get("containerName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerName")
				+ "] displyaed in Containers list");
		logger.info("Edit the container [" + testcaseArgs.get("containerName")
				+ "]");
		// Click at specific container
		boolean isClicked = SeleniumCustomUtils
				.clickAtEditLinkOfContainersInStructure(element,
						testcaseArgs.get("containerName"));
		if (!isClicked) {
			logger.error("Error while click on Edit link of a container ["
					+ testcaseArgs.get("containerName") + "]");
			ReportUtils.setStepDescription(
					"Error while click on Edit link of a container ["
							+ testcaseArgs.get("containerName") + "]", true);
			m_assert.fail("Error while click on Edit link of a container ["
					+ testcaseArgs.get("containerName") + "]");
		}
		logger.info("Click operation successful on Edit link of a container ["
				+ testcaseArgs.get("containerName") + "] ");
		// Identify Delete button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerEditDeleteBtn")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerEditDeleteBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'DELETE' button in [STRUCTURE:CONTAINERS] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'DELETE' button in [STRUCTURE:CONTAINERS] page",
							true);
			m_assert.fail("Unable to identify 'DELETE' button in [STRUCTURE:CONTAINERS] page");
		}
		if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
			SeleniumUtils.acceptAlertWindowInSafariBrowser();
			SeleniumUtils.sleepThread(3);
			// Click on Delete schema button
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Click on Delete button fails at the specified Container");
				ReportUtils
						.setStepDescription(
								"Click on Delete button fails at the specified Container",
								true);
				m_assert.fail("Click on Delete button fails at the specified Container");
			}
		} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Click on Delete button fails at the specified Container");
				ReportUtils
						.setStepDescription(
								"Click on Delete button fails at the specified Container",
								true);
				m_assert.fail("Click on Delete button fails at the specified Container");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
			SeleniumUtils.sleepThread(3);
		} else {
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Click on Delete button fails at the specified Container");
				ReportUtils
						.setStepDescription(
								"Click on Delete button fails at the specified Container",
								true);
				m_assert.fail("Click on Delete button fails at the specified Container");
			}
			SeleniumUtils.sleepThread(2);
			// Click on Ok message of Alert window
			SeleniumUtils.acceptAlertWindow();
			SeleniumUtils.sleepThread(3);
		}
		logger.info("Verify if the Container Delte Success message displyaed ");
		// Identify delete success message
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerDeleteSuccessMsg")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerDeleteSuccessMsg")
								.getLocatorvalue());
		// Get the text
		String NewContainerSuccessMsg = SeleniumUtils
				.perform_SubString_And_Trim(SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils
				.assertEqual(
						NewContainerSuccessMsg,
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerDeleteSuccessMsg")
								.getExptext());
		if (!isTextMatching) {
			logger.error("'Delete Container success msge' text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerDeleteSuccessMsg")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
			ReportUtils
					.setStepDescription(
							"'Delete Container success msge' text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("ContentManagementStructureTabNewContainerDeleteSuccessMsg")
									.getExptext(), NewContainerSuccessMsg, true);
			m_assert.fail("'Delete Container success msge' text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerDeleteSuccessMsg")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
		}
		logger.info("Delete Container Success message displayed successfully");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerNewName")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		// Verify the deleted container available in the containers list
		isVerified = SeleniumCustomUtils.checkContainerInContainersList(
				element, testcaseArgs.get("containerNewName"));
		if (isVerified) {
			logger.error("Delted container ["
					+ testcaseArgs.get("containerName")
					+ "] present in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils
					.setStepDescription(
							"Delted container ["
									+ testcaseArgs.get("containerName")
									+ "] present in Contaienrs List in [STRUCTURE:CONTAINERS] page",
							true);
			m_assert.fail("Delted container ["
					+ testcaseArgs.get("containerName")
					+ "] present in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		m_assert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods = { "loginAs" })
	public void createContainerWithSchema() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createContainerWithSchema")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createContainerWithSchema] is not added"
					+ " for execution");
			ReportUtils.setStepDescription(
					"Test case [createContainerWithSchema]"
							+ " is not added for execution", false);
			throw new SkipException(
					"Test case [createContainerWithSchema] is not added"
							+ " for execution");
		}
		// read data
		testcaseArgs = getTestData("createContainerWithSchema");
		logger.info("Starting [createContainerWithSchema] execution");
		logger.info("Verify if user is on [STRUCTURE:CONTAINERS] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Structure tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(StructureTabHeaderText, Suite.objectRepositoryMap
						.get("ContentManagementStructureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [STRUCTURE:CONTAINERS] tab ");
			logger.info("Click on [Structure] sub tab");
			// Identify Structure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatorvalue());
			// Get the text
			String StructureTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE] tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext(),
						StructureTabText, true);
				m_assert.fail("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
			}
			// Click on Structure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			logger.info("Click operation on [STRUCTURE] tab is successful. "
					+ "Verify User is on [STRUCTURE:CONTAINERS] page");
			// Identify Structure tab header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			StructureTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE:CONTAINERS]page header text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE:CONTAINERS]page header text"
								+ " matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext(), StructureTabHeaderText, true);
				m_assert.fail("[STRUCTURE:CONTAINERS]page header text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
			}
		}
		logger.info("User is on [STRUCTURE:CONTAINERS] tab");
		logger.info("Click on 'NEW CONTAINER' button");
		SeleniumUtils.sleepThread(2);
		// Identify Container Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'NEW CONTAINER'"
					+ " button in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'NEW CONTAINER' button in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'NEW CONTAINER'"
					+ " button in [STRUCTURE:CONTAINERS] page");
		}
		// Click on Container button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Verify 'Name' field 'SAVE' & 'CANCEL' buttons are displayed");
		// Identify Name field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getLocatorvalue());
		// Get the text
		String NewContainerNameFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerNameFieldText,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Name' field text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerNameField")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerNameFieldText + "]");
			ReportUtils
					.setStepDescription(
							"'Name' field text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("ContentManagementStructureTabNewContainerNameField")
									.getExptext(), NewContainerNameFieldText,
							true);
			m_assert.fail("'Name' field text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerNameField")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerNameFieldText + "]");
		}
		// Identify Name field text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name'field textbox for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'Name'field"
					+ " textbox for creating New Container in"
					+ " [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Name'field textbox for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'SAVE'button for creating New Container"
					+ " in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'SAVE'button for creating"
							+ " New Container in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'SAVE'button for creating New Container in"
					+ " [STRUCTURE:CONTAINERS] page");
		}
		logger.info("'Name' field 'SAVE' & 'CANCEL' buttons are displayed successfully ");
		logger.info("Create [" + testcaseArgs.get("containerName")
				+ "] container");
		// Identify Name text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		// Type Container name
		SeleniumUtils.type(element, testcaseArgs.get("containerName"));
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if the Success message displyaed ");
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String NewContainerSuccessMsg = SeleniumUtils
				.perform_SubString_And_Trim(SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerSuccessMsg,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'New Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
			ReportUtils
					.setStepDescription(
							"'New Container success msge' text"
									+ " matching failed",
							"",
							Suite.objectRepositoryMap
									.get("ContentManagementStructureTabNewContainerSuccessMsg")
									.getExptext(), NewContainerSuccessMsg, true);
			m_assert.fail("'New Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
		}
		logger.info("New Container Success message displayed successfully");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerName")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List' in"
					+ "  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Containers List' in  [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'Containers List' in"
					+ "  [STRUCTURE:CONTAINERS] page");
		}
		// Validate the container displayed in list
		boolean isVerified = SeleniumCustomUtils
				.checkContainerInContainersList(element,
						testcaseArgs.get("containerName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerName")
				+ "] displyaed in Containers list");
		logger.info("Click on [" + testcaseArgs.get("containerName")
				+ "] container");
		// Click on Container
		boolean isClicked = SeleniumCustomUtils.clickContainerInContainersList(
				element, testcaseArgs.get("containerName"));
		SeleniumUtils.sleepThread(3);
		if (!isClicked) {
			logger.error("Unable to click container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to click container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to click container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		// Identify header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		String OpenContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(OpenContainerHeaderText,
				testcaseArgs.get("containerName"));
		if (!isTextMatching) {
			logger.error("["
					+ testcaseArgs.get("containerName")
					+ "] Container header text matching failed. The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"Container header text matching failed", "",
					testcaseArgs.get("containerName"), OpenContainerHeaderText,
					true);
			m_assert.fail("["
					+ testcaseArgs.get("containerName")
					+ "] Container header text matching failed. The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
		}
		logger.info("Click operation successful on container ["
				+ testcaseArgs.get("containerName") + "]");
		// Identify New Item button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'NEW ITEM' button"
					+ " in Container of [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'NEW ITEM' button in Container"
					+ " of [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'NEW ITEM' button"
					+ " in Container of [STRUCTURE:CONTAINERS] page");
		}
		logger.info("Click on 'NEW ITEM' button for container");
		// Click on New Item button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Click operation is successful on 'NEW ITEM'."
				+ " Verify 'Name', 'Field', 'Type' & 'Schema' fields");
		// Identify New Item field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameField")
						.getLocatorvalue());
		// Get the text
		String NewItemNameFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemNameFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Name' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemNameField")
							.getExptext() + "] and the return text is ["
					+ NewItemNameFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Name' field text matching" + " failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemNameField")
							.getExptext(), NewItemNameFieldText, true);
			m_assert.fail("[NEW ITEM]'Name' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemNameField")
							.getExptext() + "] and the return text is ["
					+ NewItemNameFieldText + "]");
		}
		// Identify New Item Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Name' field textbox"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Name' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		// Identify New item type field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeField")
						.getLocatorvalue());
		// get the text
		String NewItemTypeFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemTypeFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemTypeField")
							.getExptext() + "] and the return text is ["
					+ NewItemTypeFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Type' field text matching" + " failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemTypeField")
							.getExptext(), NewItemTypeFieldText, true);
			m_assert.fail("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemTypeField")
							.getExptext() + "] and the return text is ["
					+ NewItemTypeFieldText + "]");
		}
		// Identify Type drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Type' field dropdown"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Type' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		// Identify field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldField")
						.getLocatorvalue());
		// Get the text
		String NewItemFieldFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemFieldFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Field' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemFieldField")
							.getExptext() + "] and the return text is ["
					+ NewItemFieldFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Field' field text" + " matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemFieldField")
							.getExptext(), NewItemFieldFieldText, true);
			m_assert.fail("[NEW ITEM]'Field' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemFieldField")
							.getExptext() + "] and the return text is ["
					+ NewItemFieldFieldText + "]");
		}
		// Identify Field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Field' field textbox"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Field' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		// Identify Schema field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaField")
						.getLocatorvalue());
		// Get the text
		String NewItemSchemaFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemSchemaFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemSchemaField")
							.getExptext() + "] and the return text is ["
					+ NewItemSchemaFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Type' field text" + " matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemSchemaField")
							.getExptext(), NewItemSchemaFieldText, true);
			m_assert.fail("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemSchemaField")
							.getExptext() + "] and the return text is ["
					+ NewItemSchemaFieldText + "]");
		}
		// Identify Schema drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Schema' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Schema' field dropdown"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Schema' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		logger.info("Verification of 'Name', 'Type', 'Field' & 'Schema' fields are successful");
		logger.info("Verify 'CANCEL' & 'SAVE' buttons");
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'SAVE' button for"
					+ " Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'SAVE'"
					+ " button for Creating New Item in"
					+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'SAVE' button for"
					+ " Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		logger.info("Verification of 'CANCEL' & 'SAVE' buttons successful");
		logger.info("Validate the 'NEW ITEM' form");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify Header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		OpenContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(OpenContainerHeaderText,
				testcaseArgs.get("containerName"));
		if (!isTextMatching) {
			logger.error("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed. "
					+ "The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"Container header text matching failed", "",
					testcaseArgs.get("containerName"), OpenContainerHeaderText,
					true);
			m_assert.fail("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed. "
					+ "The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
		}
		// New Item text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		// Type field values
		SeleniumUtils.type(element, testcaseArgs.get("fieldValue"));
		// Click on Save button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(4);
		// Identify Header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		OpenContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(OpenContainerHeaderText,
				testcaseArgs.get("containerName"));
		if (!isTextMatching) {
			logger.error("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed."
					+ " The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
			ReportUtils.setStepDescription("Container header text"
					+ " matching failed", "",
					testcaseArgs.get("containerName"), OpenContainerHeaderText,
					true);
			m_assert.fail("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed."
					+ " The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
		}
		/*
		 * // Identify Error Message at field element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getLocatortype(), Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getLocatorvalue()); // Get the text String ErrorMsgAtFieldPosition =
		 * SeleniumUtils.getText(element); isTextMatching =
		 * SeleniumUtils.assertEqual( ErrorMsgAtFieldPosition,
		 * Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemErrorMsgAtField") .getExptext());
		 * if (!isTextMatching) {
		 * logger.error("Error text matching failed. The expected text is [" +
		 * Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getExptext() + "] and the return text is [" +
		 * ErrorMsgAtFieldPosition + "]"); ReportUtils .setStepDescription(
		 * "Error text matching failed", "", Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getExptext(), ErrorMsgAtFieldPosition, true);
		 * m_assert.fail("Error text matching failed. The expected text is [" +
		 * Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getExptext() + "] and the return text is [" +
		 * ErrorMsgAtFieldPosition + "]"); }
		 */
		// Identify Field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		// Type field value
		SeleniumUtils.type(element, testcaseArgs.get("fieldValue"));
		// Select from drop down
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatorvalue(), testcaseArgs.get("schema"));
		if (!isSelected) {
			logger.error("Unable to select schema ["
					+ testcaseArgs.get("schema") + "] from 'Schema' dropdown");
			ReportUtils.setStepDescription("Unable to select schema ["
					+ testcaseArgs.get("schema") + "] from 'Schema' dropdown",
					true);
			m_assert.fail("Unable to select schema ["
					+ testcaseArgs.get("schema") + "] from 'Schema' dropdown");
		}
		// Click on Save button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(2);
		logger.info("Verify if the container ["
				+ testcaseArgs.get("fieldValue")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerContainersList")
						.getLocatorvalue());
		// Validate the container in list
		boolean isChecked = SeleniumCustomUtils.checkContainerInContainersList(
				element, testcaseArgs.get("fieldValue"));
		if (!isChecked) {
			logger.error("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
			ReportUtils.setStepDescription("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list",
					true);
			m_assert.fail("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
		}
		logger.info("container [" + testcaseArgs.get("fieldValue")
				+ "] is present in containers list");
		// Click on Container
		isClicked = SeleniumCustomUtils.clickContainerInContainersList(element,
				testcaseArgs.get("fieldValue"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
			ReportUtils.setStepDescription("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list",
					true);
			m_assert.fail("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
		}
		// Identify header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		String ContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(ContainerHeaderText,
				testcaseArgs.get("fieldValue").toUpperCase());
		if (!isTextMatching) {
			logger.error("Container header text matching failed."
					+ " The expected text is ["
					+ testcaseArgs.get("fieldValue").toUpperCase()
					+ "] and the return text is [" + ContainerHeaderText + "]");
			ReportUtils.setStepDescription("Container header text"
					+ " matching failed", "", testcaseArgs.get("fieldValue")
					.toUpperCase(), ContainerHeaderText, true);
			m_assert.fail("Container header text matching failed. "
					+ "The expected text is ["
					+ testcaseArgs.get("fieldValue").toUpperCase()
					+ "] and the return text is [" + ContainerHeaderText + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = { "loginAs" })
	public void putContentInContainer() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Verify if test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("putContentInContainer")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [putContentInContainer] is not added for execution");
			ReportUtils.setStepDescription("Test case [putContentInContainer]"
					+ " is not added for execution", false);
			throw new SkipException(
					"Test case [putContentInContainer] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("putContentInContainer");
		logger.info("Starting [putContentInContainer] execution");
		logger.info("Verify if user is on [Content] sub tab in [Content Management] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Content page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(
				ContentSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText").getExptext());
		if (!isTextMatching) {
			// Identify Content tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab")
							.getLocatorvalue());
			// Get the text
			String ContentSubTabText = SeleniumUtils.getText(element).trim();
			isTextMatching = SeleniumUtils.assertEqual(ContentSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Content] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTab").getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabText + "]");
				ReportUtils.setStepDescription("[Content] sub tab text"
						+ " matching failed", "", Suite.objectRepositoryMap
						.get("ContentManagementContentTab").getExptext(),
						ContentSubTabText, true);
				m_assert.fail("[Content] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTab").getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabText + "]");
			}
			// Click on Content tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Contet tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getLocatorvalue());
			// Get the text
			ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
			isTextMatching = SeleniumUtils.assertEqual(
					ContentSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Content] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabHeaderText + "]");
				ReportUtils.setStepDescription("[Content] sub tab header text"
						+ " matching failed", "", Suite.objectRepositoryMap
						.get("ContentManagementContentTabHeaderText")
						.getExptext(), ContentSubTabHeaderText, true);
				m_assert.fail("[Content] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Content] sub tab in [Content Management] tab");
		logger.info("Click on container [" + testcaseArgs.get("containerName")
				+ "] in container list");
		SeleniumUtils.sleepThread(2);
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription("Unable to select Containers list"
					+ " in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		// Click on Container
		boolean isClicked = SeleniumCustomUtils.clickContainerInContentTab(
				element, testcaseArgs.get("containerName"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
			ReportUtils.setStepDescription(
					"Unable to click Container ["
							+ testcaseArgs.get("containerName")
							+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
		}
		// Identify header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("containerName").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header text"
					+ " matching failed", "", testcaseArgs.get("containerName")
					.toUpperCase(), ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription("Unable to select Containers list"
					+ " in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		// Click on Container
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("InnerContainerValue"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
			ReportUtils.setStepDescription("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
		}
		// Identify header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("InnerContainerValue").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header tex"
					+ "t matching failed", "",
					testcaseArgs.get("InnerContainerValue").toUpperCase(),
					ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		// Identify Content Form
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentForm").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentForm").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify content form for opened container");
			ReportUtils.setStepDescription(
					"Unable to identify content form for opened container",
					true);
			m_assert.fail("Unable to identify content form for opened container");
		}
		logger.info("Verify the form and enter the data in container");
		// Identify Company Name
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyName")
						.getLocatorvalue());
		// Get the text
		String FirstLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(FirstLabelText,
				testcaseArgs.get("field1"));
		if (!isTextMatching) {
			logger.error("First Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field1")
					+ "] and the return text is ["
					+ FirstLabelText + "]");
			ReportUtils.setStepDescription("First Label text matching failed",
					"", testcaseArgs.get("field1"), FirstLabelText, true);
			m_assert.fail("First Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field1")
					+ "] and the return text is ["
					+ FirstLabelText + "]");
		}
		// Company Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyNameTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyNameTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify first label texbox element");
			ReportUtils.setStepDescription("Unable to identify first label"
					+ " texbox element", true);
			m_assert.fail("Unable to identify first label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(1);
		SeleniumUtils.type(element, testcaseArgs.get("field1Data"));
		// enter data in second label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAdd")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAdd")
						.getLocatorvalue());
		// Get the text
		String SecondLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SecondLabelText,
				testcaseArgs.get("field2"));
		if (!isTextMatching) {
			logger.error("Second Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field2")
					+ "] and the return text is ["
					+ SecondLabelText + "]");
			ReportUtils.setStepDescription("Second Label text matching failed",
					"", testcaseArgs.get("field2"), SecondLabelText, true);
			m_assert.fail("Second Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field2")
					+ "] and the return text is ["
					+ SecondLabelText + "]");
		}
		// Company address text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAddTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAddTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify second label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify second label texbox element", true);
			m_assert.fail("Unable to identify second label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("field2Data"));
		// enter data in third label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyLocations")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyLocations")
						.getLocatorvalue());
		// Get the text
		String thirdLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(thirdLabelText,
				testcaseArgs.get("field3"));
		if (!isTextMatching) {
			logger.error("third Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field3")
					+ "] and the return text is [" + thirdLabelText + "]");
			ReportUtils.setStepDescription("third Label text"
					+ " matching failed", "", testcaseArgs.get("field3"),
					thirdLabelText, true);
			m_assert.fail("third Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field3")
					+ "] and the return text is [" + thirdLabelText + "]");
		}
		// Location text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyLocTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyLocTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify third label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify third label texbox element", true);
			m_assert.fail("Unable to identify third label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("field3Data"));
		// enter data in fourth label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmp")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmp")
						.getLocatorvalue());
		// Get the text
		String fourthLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(fourthLabelText,
				testcaseArgs.get("field4"));
		if (!isTextMatching) {
			logger.error("fourth Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field1")
					+ "] and the return text is [" + fourthLabelText + "]");
			ReportUtils.setStepDescription("fourth Label text matching"
					+ " failed", "", testcaseArgs.get("field1"),
					fourthLabelText, true);
			m_assert.fail("fourth Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field1")
					+ "] and the return text is ["
					+ fourthLabelText + "]");
		}
		// Company employee text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmpTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmpTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify fourth label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify fourth label texbox element", true);
			m_assert.fail("Unable to identify fourth label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("field4Data"));
		// enter data in fifth label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEstDate")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEstDate")
						.getLocatorvalue());
		// Get the text
		String fifthLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(fifthLabelText,
				testcaseArgs.get("field5"));
		if (!isTextMatching) {
			logger.error("fifth Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field5")
					+ "] and the return text is [" + fifthLabelText + "]");
			ReportUtils.setStepDescription("fifth Label text matching"
					+ " failed", "", testcaseArgs.get("field5"),
					fifthLabelText, true);
			m_assert.fail("fifth Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field5")
					+ "] and the return text is [" + fifthLabelText + "]");
		}
		// Identify Date text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("CMContentTabContainersContentFormCompanyEstDateTextBox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("CMContentTabContainersContentFormCompanyEstDateTextBox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify fifth label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify fifth label texbox element", true);
			m_assert.fail("Unable to identify fifth label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(4);
		SeleniumUtils.type(element, testcaseArgs.get("field5Data"));
		logger.info("Verification of all the form fields and Entering data is successful");
		logger.info("Click on SAVE button");
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify CANCEL element");
			ReportUtils.setStepDescription("Unable to identify CANCEL element",
					true);
			m_assert.fail("Unable to identify CANCEL element");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSavelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSavelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify SAVE element");
			ReportUtils.setStepDescription("Unable to identify SAVE element",
					true);
			m_assert.fail("Unable to identify SAVE element");
		}
		if (configproperties.get(0).equalsIgnoreCase("IE")) {
			// Click on Save button
			SeleniumUtils.click_Using_JavaScript(element);
			SeleniumUtils.sleepThread(4);
		} else {
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
		}
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String DataSucessMsg = SeleniumUtils.perform_SubString_And_Trim(
				SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				DataSucessMsg,
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSuccessMsg")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Container data success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMContentTabContainersContentFormSuccessMsg")
							.getExptext() + "] and the return text is ["
					+ DataSucessMsg + "]");
			ReportUtils.setStepDescription(
					"Container data success message text " + "matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMContentTabContainersContentFormSuccessMsg")
							.getExptext(), DataSucessMsg, true);
			m_assert.fail("Container data success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMContentTabContainersContentFormSuccessMsg")
							.getExptext()
					+ "] and the return text is ["
					+ DataSucessMsg + "]");
		}
		logger.info("Content data saved successfully");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify content sub tab in Content Management");
			ReportUtils.setStepDescription(
					"Unable to identify content sub tab in Content Management",
					true);
			m_assert.fail("Unable to identify content sub tab in Content Management");
		}
		// Click on Content tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription(
					"Unable to select Containers list in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("containerName"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
			ReportUtils.setStepDescription(
					"Unable to click Container ["
							+ testcaseArgs.get("containerName")
							+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
		}
		// Identify Header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("containerName").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header text"
					+ " matching failed", "", testcaseArgs.get("containerName")
					.toUpperCase(), ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription(
					"Unable to select Containers list in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("InnerContainerValue"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
			ReportUtils.setStepDescription("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
		}
		// Headers text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("InnerContainerValue").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header text"
					+ " matching failed", "",
					testcaseArgs.get("InnerContainerValue").toUpperCase(),
					ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 16, dependsOnMethods = { "loginAs" })
	public void createSchemaWithMultiLineHtml() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createSchemaWithMultiLineHtml")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [createSchemaWithMultiLineHtml] is not added for execution");
			ReportUtils
					.setStepDescription(
							"Test case [createSchemaWithMultiLineHtml] is not added for execution",
							false);
			throw new SkipException(
					"Test case [createSchemaWithMultiLineHtml] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("createSchemaWithMultiLineHtml");
		logger.info("Verify if User is on [Create New Schema] page");
		// Identify Create New Schmea page header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaPageHeaderText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Create New Schema] page");
			logger.info("Navigate to [Schemas] tab");
			// Identify Schema tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			// Click on Schemas tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Schemas page header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] page header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] page header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed: The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
			// Identify Create New Schema button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabNewSchemaBtn")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify 'New Schema' button"
						+ " in [Schemas] tab");
				ReportUtils.setStepDescription(
						"Unable to identify 'New Schema'"
								+ " button in [Schemas] tab", true);
				m_assert.fail("Unable to identify 'New Schema' button"
						+ " in [Schemas] tab");
			}
			// Click on Create New Schema button
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Identify Create New Schema page
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaHeaderText")
							.getLocatorvalue());
			// Get the text
			CreateNewSchemaPageHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					CreateNewSchemaPageHeaderText, Suite.objectRepositoryMap
							.get("CMSchemasTabCreateNewSchemaHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Create New Schema] page header text matching failed: "
						+ "The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Create New Schema] page header text "
								+ "matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Create New Schema] tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Create New Schema] tab");
		logger.info("Create new Complex Schema ");
		// Identify Schema Label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaNameLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaNameLabelText,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameLabel").getExptext());
		if (!isTextMatching) {
			logger.error("Name field text matching failed in [Create New Schema] page."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
			ReportUtils.setStepDescription(
					"Name field text matching failed"
							+ " in [Create New Schema] page",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext(), CreateNewSchemaNameLabelText, true);
			m_assert.fail("Name field text matching failed in [Create New Schema] page."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaNameLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaNameLabelText + "]");
		}
		// Identify Name field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaNameValue")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription("Unable to identify 'Name' field"
					+ " input textbox in [Create New Schema] in [Schema] tab",
					true);
			m_assert.fail("Unable to identify 'Name' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// enter the schema name in Name text box
		SeleniumUtils.type(element, testcaseArgs.get("schemaName"));
		// Identify Description Label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getLocatorvalue());
		// Get the text
		String CreateNewSchemaDescriptionLabelText = SeleniumUtils
				.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				CreateNewSchemaDescriptionLabelText, Suite.objectRepositoryMap
						.get("CMSchemasTabCreateNewSchemaDescriptionLabel")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Description' label text matching failed in [Create New Schema] page."
					+ " The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext()
					+ "] and the actual return text is ["
					+ CreateNewSchemaDescriptionLabelText + "]");
			ReportUtils.setStepDescription(
					"'Description' label text matching failed in"
							+ " [Create New Schema] page",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext(), CreateNewSchemaDescriptionLabelText,
					true);
			m_assert.fail("'Create New Schema' page 'Description' label text"
					+ " matching failed. The expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaDescriptionLabel")
							.getExptext() + "] and the actual return text is ["
					+ CreateNewSchemaDescriptionLabelText + "]");
		}
		// Identify Description text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaDescriptionTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'Description' field"
							+ " input textbox in [Create New Schema] in"
							+ " [Schema] tab", true);
			m_assert.fail("Unable to identify 'Description' field input textbox in"
					+ " [Create New Schema] in [Schema] tab");
		}
		// enter schema description in Schema description text box
		SeleniumUtils.type(element, testcaseArgs.get("description"));
		// Identify Add Attribute button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributeBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD ATTRIBUTE' button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription(
					"Unable to identify 'ADD ATTRIBUTE' button"
							+ " in [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD ATTRIBUTE' button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		logger.info("Click on ADD ATTRIBUTE button");
		// Click on Add Attribute button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Click operation on 'ADD ATTRIBUTE' button is successful. "
				+ "Enter data in 'Key' and 'Val' fields");
		// enter data in Key and val fields
		List<WebElement> elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddAttributesKeyTextbox")
						.getLocatorvalue());
		if (elementsList == null) {
			logger.error("Unable to identify 'Key' & 'Val' fields"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Key' & 'Val' fields"
							+ " textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Key' & 'Val' fields"
					+ " textbox in [Create New Schema] page");
		}
		for (int i = 0; i < elementsList.size(); i++) {
			WebElement singleElement = elementsList.get(i);
			SeleniumUtils.type(singleElement, testcaseArgs.get("attribute"));
		}
		// Identify Add Field button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'ADD FIELD' button in"
					+ " [Create New Schema] in [Schema] tab");
			ReportUtils.setStepDescription("Unable to identify 'ADD FIELD'"
					+ " button in [Create New Schema] in [Schema] tab", true);
			m_assert.fail("Unable to identify 'ADD FIELD' button in"
					+ " [Create New Schema] in [Schema] tab");
		}
		logger.info("Click on 'ADD FIELD' button");
		for (int i = 0; i < 5; i++) {
			SeleniumUtils.sleepThread(1);
			// Identify Add Field button
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaAddFieldBtn")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaAddFieldBtn")
							.getLocatorvalue());
			SeleniumUtils.clickOnElement(element);
		}
		logger.info("Click operation on 'ADD FIELD' button is successful");
		// enter data in Label, Field and Type fields
		elementsList = SeleniumUtils.findobjects(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldLabelTextbox")
						.getLocatorvalue());
		if (elementsList == null) {
			logger.error("Unable to identify 'Label' & 'Field' & 'Description'"
					+ " fields textbox in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Label' & 'Field'"
							+ " & 'Description' fields textbox in"
							+ " [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label' & 'Field' & 'Description'"
					+ " fields textbox in [Create New Schema] page");
		}
		// 1st field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel1")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label1' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label1'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label1' field textbox in"
					+ " [Create New Schema] page");
		}
		// Enter Fields
		SeleniumUtils.type(element, testcaseArgs.get("field1"));
		// Identify Field1
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField1")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field1' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field1'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field1' field textbox in"
					+ " [Create New Schema] page");
		}
		// Enter field value in First field
		SeleniumUtils.type(element, testcaseArgs.get("field1"));
		// Identify First field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription1")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description1' field"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Description1' field textbox"
					+ " in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description1' field"
					+ " textbox in [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field1Description"));
		// 2nd field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel2")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label2' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label2'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label2' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field2"));
		// 2nd field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField2")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field2' field textbox"
					+ " in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field2'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field2' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field2"));
		// 2nd field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription2")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description2' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Description2'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description2' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field2Description"));
		// 3rd field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label3' field textbox"
					+ " in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label3'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label3' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3"));
		// 3rd field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field3' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field3'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field3' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3"));
		// 3rd field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description3' field"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Description3' field textbox in"
					+ " [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description3' field"
					+ " textbox in [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3Description"));
		// 3rd Field drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type dropdown3' field in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Type"
					+ " dropdown3' field in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Type dropdown3' field in"
					+ " [Create New Schema] page");
		}
		// Select from drop down
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown3")
						.getLocatorvalue(), testcaseArgs.get("field3Type"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("field3Type")
					+ "] from 'Type dropdown3' field in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to select [" + testcaseArgs.get("field3Type")
							+ "] from 'Type dropdown3' field in"
							+ " [Create New Schema] page", true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("field3Type")
					+ "] from 'Type dropdown3' field in [Create New Schema] page");
		}
		// 4th Field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label4' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label4'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label4' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field4"));
		// 4th Field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field4' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field4'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field4' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field4"));
		// 4th field Description
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description4' field"
					+ " textbox in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Description4'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description4' field textbox"
					+ " in [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field4Description"));
		// 4th field drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type dropdown4' field in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Type dropdown4' field in [Create New Schema] page",
					true);
			m_assert.fail("Unable to identify 'Type dropdown4' field in"
					+ " [Create New Schema] page");
		}
		// Select from drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown4")
						.getLocatorvalue(), testcaseArgs.get("field4Type"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("field4Type")
					+ "] from 'Type dropdown3' field in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to select [" + testcaseArgs.get("field4Type")
							+ "] from 'Type dropdown3' field in "
							+ "[Create New Schema] page", true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("field4Type")
					+ "] from 'Type dropdown4' field in [Create New Schema] page");
		}
		// 5th Field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetLabel5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Label5' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Label5'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Label5' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field5"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetField5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field5' field textbox in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Field5'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Field5' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field5"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetDescription5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Description5' field textbox"
					+ " in [Create New Schema] page");
			ReportUtils.setStepDescription("Unable to identify 'Description5'"
					+ " field textbox in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Description5' field textbox in"
					+ " [Create New Schema] page");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field5Description"));
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type dropdown5' field in"
					+ " [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Type dropdown5'"
							+ " field in [Create New Schema] page", true);
			m_assert.fail("Unable to identify 'Type dropdown5' field in"
					+ " [Create New Schema] page");
		}
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaAddFieldSetTypeDropdown5")
						.getLocatorvalue(), testcaseArgs.get("field5Type"));
		if (!isSelected) {
			logger.error("Unable to select ["
					+ testcaseArgs.get("field5Type")
					+ "] from 'Type dropdown5' field in [Create New Schema] page");
			ReportUtils.setStepDescription(
					"Unable to select [" + testcaseArgs.get("field5Type")
							+ "] from 'Type dropdown5' field in"
							+ " [Create New Schema] page", true);
			m_assert.fail("Unable to select ["
					+ testcaseArgs.get("field5Type")
					+ "] from 'Type dropdown5' field in [Create New Schema] page");
		}
		// Identify Save button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap.get(
								"CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("CMSchemasTabCreateNewSchemaSaveBtn")
								.getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		// Identify Success Message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG")
						.getLocatorvalue());
		// Get the text
		String SchemaSuccessMessage = SeleniumUtils.perform_SubString_And_Trim(
				SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemaSuccessMessage,
				Suite.objectRepositoryMap.get(
						"CMSchemasTabCreateNewSchemaSuccessMSG").getExptext());
		if (!isTextMatching) {
			logger.error("'Schema' success message text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
			ReportUtils.setStepDescription(
					"'Schema' success message text matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext(), SchemaSuccessMessage, true);
			m_assert.fail("'Schema' success message text matching failed. "
					+ "The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMSchemasTabCreateNewSchemaSuccessMSG")
							.getExptext() + "] and the actual return text is ["
					+ SchemaSuccessMessage + "]");
		}
		// Create Container
		logger.info("Verify if user is on [STRUCTURE:CONTAINERS] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Structure tab header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(StructureTabHeaderText, Suite.objectRepositoryMap
						.get("ContentManagementStructureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [STRUCTURE:CONTAINERS] tab ");
			logger.info("Click on [Structure] sub tab");
			// Identify Structure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatorvalue());
			// Get the text
			String StructureTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE] tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext(),
						StructureTabText, true);
				m_assert.fail("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
			}
			// Click on Structure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			logger.info("Click operation on [STRUCTURE] tab is successful. "
					+ "Verify User is on [STRUCTURE:CONTAINERS] page");
			// Identify Structure tab header element
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			StructureTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE:CONTAINERS]page header text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE:CONTAINERS]page header text"
								+ " matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext(), StructureTabHeaderText, true);
				m_assert.fail("[STRUCTURE:CONTAINERS]page header text matching failed."
						+ " The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
			}
		}
		logger.info("User is on [STRUCTURE:CONTAINERS] tab");
		logger.info("Click on 'NEW CONTAINER' button");
		SeleniumUtils.sleepThread(2);
		// Identify Container Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'NEW CONTAINER'"
					+ " button in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'NEW CONTAINER' button in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'NEW CONTAINER'"
					+ " button in [STRUCTURE:CONTAINERS] page");
		}
		// Click on Container button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Verify 'Name' field 'SAVE' & 'CANCEL' buttons are displayed");
		// Identify Name field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getLocatorvalue());
		// Get the text
		String NewContainerNameFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerNameFieldText,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerNameField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'Name' field text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerNameField")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerNameFieldText + "]");
			ReportUtils
					.setStepDescription(
							"'Name' field text matching failed",
							"",
							Suite.objectRepositoryMap
									.get("ContentManagementStructureTabNewContainerNameField")
									.getExptext(), NewContainerNameFieldText,
							true);
			m_assert.fail("'Name' field text matching failed. The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerNameField")
							.getExptext()
					+ "] and the actual return text is ["
					+ NewContainerNameFieldText + "]");
		}
		// Identify Name field text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name'field textbox for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'Name'field"
					+ " textbox for creating New Container in"
					+ " [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Name'field textbox for creating"
					+ " New Container in [STRUCTURE:CONTAINERS] page");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'SAVE'button for creating New Container"
					+ " in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'SAVE'button for creating"
							+ " New Container in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'SAVE'button for creating New Container in"
					+ " [STRUCTURE:CONTAINERS] page");
		}
		logger.info("'Name' field 'SAVE' & 'CANCEL' buttons are displayed successfully ");
		logger.info("Create [" + testcaseArgs.get("containerName")
				+ "] container");
		// Identify Name text box
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerNameFieldTextbox")
								.getLocatorvalue());
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		// Type Container name
		SeleniumUtils.type(element, testcaseArgs.get("containerName"));
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSaveBtn")
						.getLocatorvalue());
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		logger.info("Verify if the Success message displyaed ");
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String NewContainerSuccessMsg = SeleniumUtils
				.perform_SubString_And_Trim(SeleniumUtils.getText(element), 1);
		isTextMatching = SeleniumUtils.assertEqual(
				NewContainerSuccessMsg,
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabNewContainerSuccessMsg")
						.getExptext());
		if (!isTextMatching) {
			logger.error("'New Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
			ReportUtils
					.setStepDescription(
							"'New Container success msge' text"
									+ " matching failed",
							"",
							Suite.objectRepositoryMap
									.get("ContentManagementStructureTabNewContainerSuccessMsg")
									.getExptext(), NewContainerSuccessMsg, true);
			m_assert.fail("'New Container success msge' text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap
							.get("ContentManagementStructureTabNewContainerSuccessMsg")
							.getExptext() + "] and the actual return text is ["
					+ NewContainerSuccessMsg + "]");
		}
		logger.info("New Container Success message displayed successfully");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerName")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List' in"
					+ "  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'Containers List' in  [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to identify 'Containers List' in"
					+ "  [STRUCTURE:CONTAINERS] page");
		}
		// Validate the container displayed in list
		boolean isVerified = SeleniumCustomUtils
				.checkContainerInContainersList(element,
						testcaseArgs.get("containerName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerName")
				+ "] displyaed in Containers list");
		logger.info("Click on [" + testcaseArgs.get("containerName")
				+ "] container");
		// Click on Container
		boolean isClicked = SeleniumCustomUtils.clickContainerInContainersList(
				element, testcaseArgs.get("containerName"));
		SeleniumUtils.sleepThread(3);
		if (!isClicked) {
			logger.error("Unable to click container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to click container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to click container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		// Identify header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		String OpenContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(OpenContainerHeaderText,
				testcaseArgs.get("containerName"));
		if (!isTextMatching) {
			logger.error("["
					+ testcaseArgs.get("containerName")
					+ "] Container header text matching failed. The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"Container header text matching failed", "",
					testcaseArgs.get("containerName"), OpenContainerHeaderText,
					true);
			m_assert.fail("["
					+ testcaseArgs.get("containerName")
					+ "] Container header text matching failed. The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
		}
		logger.info("Click operation successful on container ["
				+ testcaseArgs.get("containerName") + "]");
		// Identify New Item button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'NEW ITEM' button"
					+ " in Container of [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " 'NEW ITEM' button in Container"
					+ " of [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'NEW ITEM' button"
					+ " in Container of [STRUCTURE:CONTAINERS] page");
		}
		logger.info("Click on 'NEW ITEM' button for container");
		// Click on New Item button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		logger.info("Click operation is successful on 'NEW ITEM'."
				+ " Verify 'Name', 'Field', 'Type' & 'Schema' fields");
		// Identify New Item field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameField")
						.getLocatorvalue());
		// Get the text
		String NewItemNameFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemNameFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Name' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemNameField")
							.getExptext() + "] and the return text is ["
					+ NewItemNameFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Name' field text matching" + " failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemNameField")
							.getExptext(), NewItemNameFieldText, true);
			m_assert.fail("[NEW ITEM]'Name' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemNameField")
							.getExptext() + "] and the return text is ["
					+ NewItemNameFieldText + "]");
		}
		// Identify New Item Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Name' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Name' field textbox"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Name' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		// Identify New item type field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeField")
						.getLocatorvalue());
		// get the text
		String NewItemTypeFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemTypeFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemTypeField")
							.getExptext() + "] and the return text is ["
					+ NewItemTypeFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Type' field text matching" + " failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemTypeField")
							.getExptext(), NewItemTypeFieldText, true);
			m_assert.fail("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemTypeField")
							.getExptext() + "] and the return text is ["
					+ NewItemTypeFieldText + "]");
		}
		// Identify Type drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemTypeDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Type' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Type' field dropdown"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Type' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		// Identify field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldField")
						.getLocatorvalue());
		// Get the text
		String NewItemFieldFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemFieldFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Field' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemFieldField")
							.getExptext() + "] and the return text is ["
					+ NewItemFieldFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Field' field text" + " matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemFieldField")
							.getExptext(), NewItemFieldFieldText, true);
			m_assert.fail("[NEW ITEM]'Field' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemFieldField")
							.getExptext() + "] and the return text is ["
					+ NewItemFieldFieldText + "]");
		}
		// Identify Field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Field' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Field' field textbox"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Field' field textbox"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		// Identify Schema field
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaField")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaField")
						.getLocatorvalue());
		// Get the text
		String NewItemSchemaFieldText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				NewItemSchemaFieldText,
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaField")
						.getExptext());
		if (!isTextMatching) {
			logger.error("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemSchemaField")
							.getExptext() + "] and the return text is ["
					+ NewItemSchemaFieldText + "]");
			ReportUtils.setStepDescription(
					"[NEW ITEM]'Type' field text" + " matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemSchemaField")
							.getExptext(), NewItemSchemaFieldText, true);
			m_assert.fail("[NEW ITEM]'Type' field text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMStructureTabOpenContainerNewItemSchemaField")
							.getExptext() + "] and the return text is ["
					+ NewItemSchemaFieldText + "]");
		}
		// Identify Schema drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Schema' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription(
					"Unable to identify 'Schema' field dropdown"
							+ " element for Creating New Item in"
							+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'Schema' field dropdown"
					+ " element for Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		logger.info("Verification of 'Name', 'Type', 'Field' & 'Schema' fields are successful");
		logger.info("Verify 'CANCEL' & 'SAVE' buttons");
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'SAVE' button for"
					+ " Creating New Item in  [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to identify 'SAVE'"
					+ " button for Creating New Item in"
					+ "  [STRUCTURE:CONTAINERS] page", true);
			m_assert.fail("Unable to identify 'SAVE' button for"
					+ " Creating New Item in  [STRUCTURE:CONTAINERS] page");
		}
		logger.info("Verification of 'CANCEL' & 'SAVE' buttons successful");
		logger.info("Validate the 'NEW ITEM' form");
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify Header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		OpenContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(OpenContainerHeaderText,
				testcaseArgs.get("containerName"));
		if (!isTextMatching) {
			logger.error("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed. "
					+ "The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
			ReportUtils.setStepDescription(
					"Container header text matching failed", "",
					testcaseArgs.get("containerName"), OpenContainerHeaderText,
					true);
			m_assert.fail("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed. "
					+ "The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
		}
		// New Item text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemNameTextbox")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		// Type field values
		SeleniumUtils.type(element, testcaseArgs.get("fieldValue"));
		// Click on Save button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(4);
		// Identify Header text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		OpenContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(OpenContainerHeaderText,
				testcaseArgs.get("containerName"));
		if (!isTextMatching) {
			logger.error("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed."
					+ " The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
			ReportUtils.setStepDescription("Container header text"
					+ " matching failed", "",
					testcaseArgs.get("containerName"), OpenContainerHeaderText,
					true);
			m_assert.fail("[" + testcaseArgs.get("containerName")
					+ "] Container header text matching failed."
					+ " The exepcted text is ["
					+ testcaseArgs.get("containerName")
					+ "] and the return text is [" + OpenContainerHeaderText
					+ "]");
		}
		/*
		 * // Identify Error Message at field element =
		 * SeleniumUtils.findobject( Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getLocatortype(), Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getLocatorvalue()); // Get the text String ErrorMsgAtFieldPosition =
		 * SeleniumUtils.getText(element); isTextMatching =
		 * SeleniumUtils.assertEqual( ErrorMsgAtFieldPosition,
		 * Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemErrorMsgAtField") .getExptext());
		 * if (!isTextMatching) {
		 * logger.error("Error text matching failed. The expected text is [" +
		 * Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getExptext() + "] and the return text is [" +
		 * ErrorMsgAtFieldPosition + "]"); ReportUtils .setStepDescription(
		 * "Error text matching failed", "", Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getExptext(), ErrorMsgAtFieldPosition, true);
		 * m_assert.fail("Error text matching failed. The expected text is [" +
		 * Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemErrorMsgAtField")
		 * .getExptext() + "] and the return text is [" +
		 * ErrorMsgAtFieldPosition + "]"); }
		 */
		// Identify Field text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemFieldTextbox")
						.getLocatorvalue());
		SeleniumUtils.clearText(element);
		// Type field value
		SeleniumUtils.type(element, testcaseArgs.get("fieldValue"));
		// Select from drop down
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSchemaDropdown")
						.getLocatorvalue(), testcaseArgs.get("schema"));
		if (!isSelected) {
			logger.error("Unable to select schema ["
					+ testcaseArgs.get("schema") + "] from 'Schema' dropdown");
			ReportUtils.setStepDescription("Unable to select schema ["
					+ testcaseArgs.get("schema") + "] from 'Schema' dropdown",
					true);
			m_assert.fail("Unable to select schema ["
					+ testcaseArgs.get("schema") + "] from 'Schema' dropdown");
		}
		// Click on Save button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerNewItemSaveBtn")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(2);
		// Identify Success Message
		/*
		 * element = SeleniumUtils.waitForElementToIdentify(
		 * Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemSuccessMsg") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemSuccessMsg") .getLocatorvalue());
		 * // Get the text String NewItemSuccessMsg =
		 * SeleniumUtils.getText(element); isTextMatching =
		 * SeleniumUtils.assertEqual( NewItemSuccessMsg,
		 * Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemSuccessMsg") .getExptext()); if
		 * (!isTextMatching) {
		 * logger.error("New Item creation success messsage text matching failed."
		 * + " The Expected text is [" + Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemSuccessMsg") .getExptext() +
		 * "] and the return text is [" + NewItemSuccessMsg + "]");
		 * ReportUtils.setStepDescription("New Item creation success messsage" +
		 * " text matching failed", "", Suite.objectRepositoryMap
		 * .get("CMStructureTabOpenContainerNewItemSuccessMsg") .getExptext(),
		 * NewItemSuccessMsg, true);
		 * m_assert.fail("New Item creation success messsage text matching failed."
		 * + " The Expected text is [" + Suite.objectRepositoryMap.get(
		 * "CMStructureTabOpenContainerNewItemSuccessMsg") .getExptext() +
		 * "] and the return text is [" + NewItemSuccessMsg + "]"); }
		 */
		logger.info("Verify if the container ["
				+ testcaseArgs.get("fieldValue")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerContainersList")
						.getLocatorvalue());
		// Validate the container in list
		boolean isChecked = SeleniumCustomUtils.checkContainerInContainersList(
				element, testcaseArgs.get("fieldValue"));
		if (!isChecked) {
			logger.error("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
			ReportUtils.setStepDescription("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list",
					true);
			m_assert.fail("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
		}
		logger.info("container [" + testcaseArgs.get("fieldValue")
				+ "] is present in containers list");
		// Click on Container
		isClicked = SeleniumCustomUtils.clickContainerInContainersList(element,
				testcaseArgs.get("fieldValue"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
			ReportUtils.setStepDescription("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list",
					true);
			m_assert.fail("Unable to identify container ["
					+ testcaseArgs.get("fieldValue") + "] in containers list");
		}
		// Identify header element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMStructureTabOpenContainerHeaderText")
						.getLocatorvalue());
		// Get the text
		String ContainerHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(ContainerHeaderText,
				testcaseArgs.get("fieldValue").toUpperCase());
		if (!isTextMatching) {
			logger.error("Container header text matching failed."
					+ " The expected text is ["
					+ testcaseArgs.get("fieldValue").toUpperCase()
					+ "] and the return text is [" + ContainerHeaderText + "]");
			ReportUtils.setStepDescription("Container header text"
					+ " matching failed", "", testcaseArgs.get("fieldValue")
					.toUpperCase(), ContainerHeaderText, true);
			m_assert.fail("Container header text matching failed. "
					+ "The expected text is ["
					+ testcaseArgs.get("fieldValue").toUpperCase()
					+ "] and the return text is [" + ContainerHeaderText + "]");
		}
		logger.info("Verify if user is on [Content] sub tab in [Content Management] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Content page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(
				ContentSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText").getExptext());
		if (!isTextMatching) {
			// Identify Content tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab")
							.getLocatorvalue());
			// Get the text
			String ContentSubTabText = SeleniumUtils.getText(element).trim();
			isTextMatching = SeleniumUtils.assertEqual(ContentSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementContentTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Content] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTab").getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabText + "]");
				ReportUtils.setStepDescription("[Content] sub tab text"
						+ " matching failed", "", Suite.objectRepositoryMap
						.get("ContentManagementContentTab").getExptext(),
						ContentSubTabText, true);
				m_assert.fail("[Content] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTab").getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabText + "]");
			}
			// Click on Content tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			// Contet tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getLocatorvalue());
			// Get the text
			ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
			isTextMatching = SeleniumUtils.assertEqual(
					ContentSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementContentTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Content] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabHeaderText + "]");
				ReportUtils.setStepDescription("[Content] sub tab header text"
						+ " matching failed", "", Suite.objectRepositoryMap
						.get("ContentManagementContentTabHeaderText")
						.getExptext(), ContentSubTabHeaderText, true);
				m_assert.fail("[Content] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementContentTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ ContentSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Content] sub tab in [Content Management] tab");
		logger.info("Click on container [" + testcaseArgs.get("containerName")
				+ "] in container list");
		SeleniumUtils.sleepThread(2);
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription("Unable to select Containers list"
					+ " in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		// Click on Container
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("containerName"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
			ReportUtils.setStepDescription(
					"Unable to click Container ["
							+ testcaseArgs.get("containerName")
							+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
		}
		// Identify header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("containerName").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header text"
					+ " matching failed", "", testcaseArgs.get("containerName")
					.toUpperCase(), ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription("Unable to select Containers list"
					+ " in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		// Click on Container
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("InnerContainerValue"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
			ReportUtils.setStepDescription("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
		}
		// Identify header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("InnerContainerValue").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header tex"
					+ "t matching failed", "",
					testcaseArgs.get("InnerContainerValue").toUpperCase(),
					ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		// Identify Content Form
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentForm").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentForm").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify content form for opened container");
			ReportUtils.setStepDescription(
					"Unable to identify content form for opened container",
					true);
			m_assert.fail("Unable to identify content form for opened container");
		}
		logger.info("Verify the form and enter the data in container");
		// Identify Company Name
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyName")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyName")
						.getLocatorvalue());
		// Get the text
		String FirstLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(FirstLabelText,
				testcaseArgs.get("field1"));
		if (!isTextMatching) {
			logger.error("First Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field1")
					+ "] and the return text is ["
					+ FirstLabelText + "]");
			ReportUtils.setStepDescription("First Label text matching failed",
					"", testcaseArgs.get("field1"), FirstLabelText, true);
			m_assert.fail("First Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field1")
					+ "] and the return text is ["
					+ FirstLabelText + "]");
		}
		// Company Name text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyNameTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyNameTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify first label texbox element");
			ReportUtils.setStepDescription("Unable to identify first label"
					+ " texbox element", true);
			m_assert.fail("Unable to identify first label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(1);
		SeleniumUtils.type(element, testcaseArgs.get("field1Data"));
		// enter data in second label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAdd")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAdd")
						.getLocatorvalue());
		// Get the text
		String SecondLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(SecondLabelText,
				testcaseArgs.get("field2"));
		if (!isTextMatching) {
			logger.error("Second Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field2")
					+ "] and the return text is ["
					+ SecondLabelText + "]");
			ReportUtils.setStepDescription("Second Label text matching failed",
					"", testcaseArgs.get("field2"), SecondLabelText, true);
			m_assert.fail("Second Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field2")
					+ "] and the return text is ["
					+ SecondLabelText + "]");
		}
		// Company address text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAddTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyAddTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify second label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify second label texbox element", true);
			m_assert.fail("Unable to identify second label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("field2Data"));
		// enter data in third label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyLocations")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyLocations")
						.getLocatorvalue());
		// Get the text
		String thirdLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(thirdLabelText,
				testcaseArgs.get("field3"));
		if (!isTextMatching) {
			logger.error("third Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field3")
					+ "] and the return text is [" + thirdLabelText + "]");
			ReportUtils.setStepDescription("third Label text"
					+ " matching failed", "", testcaseArgs.get("field3"),
					thirdLabelText, true);
			m_assert.fail("third Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field3")
					+ "] and the return text is [" + thirdLabelText + "]");
		}
		// Identify Multi Line Html Rich Text Area
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextbox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Multi Line HTML Rich Text Area Location");
			ReportUtils
					.setStepDescription(
							"Unable to identify Multi Line HTML Rich Text Area Location",
							true);
			m_assert.fail("Unable to identify Multi Line HTML Rich Text Area Location");
		}
		// Identify MultiLine Html Rich Text Area Header
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxHeader")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Multi Line HTML Rich Text Area Header");
			ReportUtils.setStepDescription(
					"Unable to identify Multi Line HTML Rich Text Area Header",
					true);
			m_assert.fail("Unable to identify Multi Line HTML Rich Text Area Header");
		}
		// Identify Rich Text area frame
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomFrame")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomFrame")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Multi Line HTML Rich Text Area Frame");
			ReportUtils.setStepDescription(
					"Unable to identify Multi Line HTML Rich Text Area Frame",
					true);
			m_assert.fail("Unable to identify Multi Line HTML Rich Text Area Frame");
		}
		// Navigate to Frame
		SeleniumUtils.switchToFrame(element);
		// Locations MultiLine text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomBody")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomBody")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify third label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify third label texbox element", true);
			m_assert.fail("Unable to identify third label textbox element");
		}
		SeleniumUtils.type(element, testcaseArgs.get("field3Data"));
		// Locations MultiLine text box
		/*
		 * element = SeleniumUtils .findobject( Suite.objectRepositoryMap
		 * .get("CMContentTabMultiLineHtmlRichTextboxBottomBodyParagraph")
		 * .getLocatortype(), Suite.objectRepositoryMap
		 * .get("CMContentTabMultiLineHtmlRichTextboxBottomBodyParagraph")
		 * .getLocatorvalue()); if (element == null) {
		 * logger.error("Unable to identify third label texbox element");
		 * ReportUtils.setStepDescription(
		 * "Unable to identify third label texbox element", true);
		 * m_assert.fail("Unable to identify third label textbox element"); }
		 * SeleniumUtils.type(element, testcaseArgs.get("field3Data"));
		 */
		// Move back to Default content
		SeleniumUtils.switchToDefaultWindow();
		// Identify Bold button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBold")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBold")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Bold button in Rich Text");
			ReportUtils.setStepDescription(
					"Unable to identify Bold button in Rich Text", true);
			m_assert.fail("Unable to identify Bold button in Rich Text");
		}
		// Get the class property of the bold button
		String Bold_Initial_Class_Actual_Value = SeleniumUtils
				.getAttributeValue(element, "class");
		isTextMatching = SeleniumUtils.assertEqual(
				Bold_Initial_Class_Actual_Value,
				testcaseArgs.get("ExpectedInitialBoldClassValue"));
		if (!isTextMatching) {
			logger.error("Validation failed at Bold functionality");
			ReportUtils.setStepDescription(
					"Validation failed at Bold functionality", true);
			m_assert.fail("Validation failed at Bold functionality");
		}
		// Identify Rich Text area frame
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomFrame")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomFrame")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Multi Line HTML Rich Text Area Frame");
			ReportUtils.setStepDescription(
					"Unable to identify Multi Line HTML Rich Text Area Frame",
					true);
			m_assert.fail("Unable to identify Multi Line HTML Rich Text Area Frame");
		}
		// Navigate to Frame
		SeleniumUtils.switchToFrame(element);
		// Locations MultiLine text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomBody")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBottomBody")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify third label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify third label texbox element", true);
			m_assert.fail("Unable to identify third label textbox element");
		}
		// Select the entire text
		SeleniumUtils.selectTextUsingKeys(element);
		// Move back to Default content
		SeleniumUtils.switchToDefaultWindow();
		// Identify Bold button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBold")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabMultiLineHtmlRichTextboxBold")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Bold button in Rich Text");
			ReportUtils.setStepDescription(
					"Unable to identify Bold button in Rich Text", true);
			m_assert.fail("Unable to identify Bold button in Rich Text");
		}
		// Click on Bold button
		SeleniumUtils.clickOnElement(element);
		// Get the class property of the bold button
		String Bold_After_Class_Actual_Value = SeleniumUtils.getAttributeValue(
				element, "class");
		isTextMatching = SeleniumUtils.assertEqual(
				Bold_After_Class_Actual_Value,
				testcaseArgs.get("ExpectedAfterBoldClassValue"));
		if (!isTextMatching) {
			logger.error("Validation failed at Bold functionality");
			ReportUtils.setStepDescription(
					"Validation failed at Bold functionality", true);
			m_assert.fail("Validation failed at Bold functionality");
		}
		// enter data in fourth label
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmp")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmp")
						.getLocatorvalue());
		// Get the text
		String fourthLabelText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(fourthLabelText,
				testcaseArgs.get("field4"));
		if (!isTextMatching) {
			logger.error("fourth Label text matching failed."
					+ " The expected text is [" + testcaseArgs.get("field1")
					+ "] and the return text is [" + fourthLabelText + "]");
			ReportUtils.setStepDescription("fourth Label text matching"
					+ " failed", "", testcaseArgs.get("field1"),
					fourthLabelText, true);
			m_assert.fail("fourth Label text matching failed. The expected text is ["
					+ testcaseArgs.get("field1")
					+ "] and the return text is ["
					+ fourthLabelText + "]");
		}
		// Company employee text box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmpTextBox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCompanyEmpTextBox")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify fourth label texbox element");
			ReportUtils.setStepDescription(
					"Unable to identify fourth label texbox element", true);
			m_assert.fail("Unable to identify fourth label textbox element");
		}
		SeleniumUtils.clearText(element);
		SeleniumUtils.sleepThread(2);
		SeleniumUtils.type(element, testcaseArgs.get("field4Data"));
		/*
		 * // enter data in fifth label element = SeleniumUtils.findobject(
		 * Suite.objectRepositoryMap.get(
		 * "CMContentTabContainersContentFormCompanyEstDate") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "CMContentTabContainersContentFormCompanyEstDate")
		 * .getLocatorvalue()); // Get the text String fifthLabelText =
		 * SeleniumUtils.getText(element); isTextMatching =
		 * SeleniumUtils.assertEqual(fifthLabelText,
		 * testcaseArgs.get("field5")); if (!isTextMatching) {
		 * logger.error("fifth Label text matching failed." +
		 * " The expected text is [" + testcaseArgs.get("field5") +
		 * "] and the return text is [" + fifthLabelText + "]");
		 * ReportUtils.setStepDescription("fifth Label text matching" +
		 * " failed", "", testcaseArgs.get("field5"), fifthLabelText, true);
		 * m_assert.fail("fifth Label text matching failed." +
		 * " The expected text is [" + testcaseArgs.get("field5") +
		 * "] and the return text is [" + fifthLabelText + "]"); } // Identify
		 * Date text box element = SeleniumUtils .findobject(
		 * Suite.objectRepositoryMap
		 * .get("CMContentTabContainersContentFormCompanyEstDateTextBox")
		 * .getLocatortype(), Suite.objectRepositoryMap
		 * .get("CMContentTabContainersContentFormCompanyEstDateTextBox")
		 * .getLocatorvalue()); if (element == null) {
		 * logger.error("Unable to identify fifth label texbox element");
		 * ReportUtils.setStepDescription(
		 * "Unable to identify fifth label texbox element", true);
		 * m_assert.fail("Unable to identify fifth label textbox element"); }
		 * SeleniumUtils.clickOnElement(element); SeleniumUtils.sleepThread(3);
		 * // Identify Date Picker and Select current date element =
		 * SeleniumUtils.waitForElementToIdentify(
		 * Suite.objectRepositoryMap.get(
		 * "CMSContentFormCompanyEstDateCalanderCurrentDate") .getLocatortype(),
		 * Suite.objectRepositoryMap.get(
		 * "CMSContentFormCompanyEstDateCalanderCurrentDate")
		 * .getLocatorvalue()); if (element == null) {
		 * logger.error("Unable to identify CANCEL element");
		 * ReportUtils.setStepDescription("Unable to identify CANCEL element",
		 * true); m_assert.fail("Unable to identify CANCEL element"); }
		 * SeleniumUtils.clickOnElement(element);
		 */
		logger.info("Click on SAVE button");
		// Identify Cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCancelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormCancelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify CANCEL element");
			ReportUtils.setStepDescription("Unable to identify CANCEL element",
					true);
			m_assert.fail("Unable to identify CANCEL element");
		}
		// Identify Save buttton
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSavelBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSavelBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify SAVE element");
			ReportUtils.setStepDescription("Unable to identify SAVE element",
					true);
			m_assert.fail("Unable to identify SAVE element");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify Success message
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSuccessMsg")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSuccessMsg")
						.getLocatorvalue());
		// Get the text
		String DataSucessMsg = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				DataSucessMsg,
				Suite.objectRepositoryMap.get(
						"CMContentTabContainersContentFormSuccessMsg")
						.getExptext());
		if (!isTextMatching) {
			logger.error("Container data success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMContentTabContainersContentFormSuccessMsg")
							.getExptext() + "] and the return text is ["
					+ DataSucessMsg + "]");
			ReportUtils.setStepDescription(
					"Container data success message text " + "matching failed",
					"",
					Suite.objectRepositoryMap.get(
							"CMContentTabContainersContentFormSuccessMsg")
							.getExptext(), DataSucessMsg, true);
			m_assert.fail("Container data success message text matching failed."
					+ " The Expected text is ["
					+ Suite.objectRepositoryMap.get(
							"CMContentTabContainersContentFormSuccessMsg")
							.getExptext()
					+ "] and the return text is ["
					+ DataSucessMsg + "]");
		}
		logger.info("Content data saved successfully");
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ContentManagementContentTab")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify content sub tab in Content Management");
			ReportUtils.setStepDescription(
					"Unable to identify content sub tab in Content Management",
					true);
			m_assert.fail("Unable to identify content sub tab in Content Management");
		}
		// Click on Content tab
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription(
					"Unable to select Containers list in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("containerName"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
			ReportUtils.setStepDescription(
					"Unable to click Container ["
							+ testcaseArgs.get("containerName")
							+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("containerName") + "]in Containers list");
		}
		// Identify Header text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("containerName").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header text"
					+ " matching failed", "", testcaseArgs.get("containerName")
					.toUpperCase(), ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("containerName").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		// Identify Containers list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("CMContentTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to select Containers list in [Contet] tab");
			ReportUtils.setStepDescription(
					"Unable to select Containers list in [Contet] tab", true);
			m_assert.fail("Unable to select Containers list in [Contet] tab");
		}
		isClicked = SeleniumCustomUtils.clickContainerInContentTab(element,
				testcaseArgs.get("InnerContainerValue"));
		SeleniumUtils.sleepThread(4);
		if (!isClicked) {
			logger.error("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
			ReportUtils.setStepDescription("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list", true);
			m_assert.fail("Unable to click Container ["
					+ testcaseArgs.get("InnerContainerValue")
					+ "]in Containers list");
		}
		// Headers text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementContentTabHeaderText")
						.getLocatorvalue());
		// Get the text
		ContentSubTabHeaderText = SeleniumUtils.getText(element).trim();
		isTextMatching = SeleniumUtils.assertEqual(ContentSubTabHeaderText,
				testcaseArgs.get("InnerContainerValue").toUpperCase());
		if (!isTextMatching) {
			logger.error("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
			ReportUtils.setStepDescription("opened container header text"
					+ " matching failed", "",
					testcaseArgs.get("InnerContainerValue").toUpperCase(),
					ContentSubTabHeaderText, true);
			m_assert.fail("opened container header text matching failed:"
					+ " The expected text is ["
					+ testcaseArgs.get("InnerContainerValue").toUpperCase()
					+ "] and the actual return text is ["
					+ ContentSubTabHeaderText + "]");
		}
		m_assert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods = { "loginAs" })
	public void deleteTestData() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("deleteTestData")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [deleteTestData] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [deleteTestData] is not added for execution",
					false);
			throw new SkipException(
					"Test case [deleteTestData] is not added for execution");
		}
		// read data
		testcaseArgs = getTestData("deleteTestData");
		logger.info("Starting [deleteTestData] execution");
		// Delete Schema
		logger.info("Verify if User is on [Schemas] page");
		// Identify Schemas header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String SchemasSubTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils.assertEqual(
				SchemasSubTabHeaderText,
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabHeaderText").getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [Schemas] tab");
			// Identify Schemas tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab")
							.getLocatorvalue());
			// Get the text
			String SchemasSubTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(SchemasSubTabText,
					Suite.objectRepositoryMap
							.get("ContentManagementSchemasTab").getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext(),
						SchemasSubTabText, true);
				m_assert.fail("[Schemas] sub tab text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTab").getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabText + "]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getLocatorvalue());
			// Get the text
			SchemasSubTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					SchemasSubTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
				ReportUtils.setStepDescription(
						"[Schemas] sub tab header text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext(), SchemasSubTabHeaderText, true);
				m_assert.fail("[Schemas] sub tab header text matching failed:"
						+ " The expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementSchemasTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ SchemasSubTabHeaderText + "]");
			}
		}
		logger.info("User is on [Schemas] tab");
		logger.info("Verify if Schemas List present in [Schemas] tab");
		// Identify Schemas list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementSchemasTabSchemasList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Schemas List in [Schemas] tab");
			ReportUtils.setStepDescription(
					"Unable to identify Schemas List in [Schemas] tab", true);
			m_assert.fail("Unable to identify Schemas List in [Schemas] tab");
		}
		logger.info("Verification of Schemas List is successful. Schemas List"
				+ " is present in [Schemas] tab");
		logger.info("Verify [" + testcaseArgs.get("schemaName")
				+ "] schema present in the Schemas List");
		for (int i = 1; i <= 10; i++) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabSchemasList")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementSchemasTabSchemasList")
							.getLocatorvalue());
			isVerified = SeleniumCustomUtils.clickAtSchemaInSchemaList(element,
					testcaseArgs.get("schemaName"));
			if (isVerified) {
				break;
			}
			// Identify Next Link
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
			SeleniumUtils.sleepThread(3);
		}
		logger.info("[" + testcaseArgs.get("schemaName")
				+ "] schema present in the Schemas List");
		if (!isVerified) {
			logger.error("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema");
			ReportUtils.setStepDescription("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema", true);
			m_assert.fail("Unable to Click on Edit link of ["
					+ testcaseArgs.get("schemaName") + "] schema");
		}
		// Identify Delete button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"CMSchemasTabEditSchemaDeleteSchemaBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"CMSchemasTabEditSchemaDeleteSchemaBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Delete Schema' button in [Edit Schema] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Delete Schema' button in [Edit Schema] page",
							true);
			m_assert.fail("Unable to identify 'Delete Schema' button in [Edit Schema] page");
		}
		// Click on Delete schema button
		SeleniumUtils.clickOnElement(element);
		// Wait for Alert window displays
		SeleniumUtils.sleepThread(6);
		// Check for Alert window
		SeleniumUtils.acceptAlertWindow();
		SeleniumUtils.sleepThread(5);
		// Delete Container
		logger.info("Verify if user is on [STRUCTURE:CONTAINERS] tab");
		SeleniumUtils.sleepThread(2);
		// Identify Structure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabHeaderText")
						.getLocatorvalue());
		// Get the text
		String StructureTabHeaderText = SeleniumUtils.getText(element);
		isTextMatching = SeleniumUtils
				.assertEqual(StructureTabHeaderText, Suite.objectRepositoryMap
						.get("ContentManagementStructureTabHeaderText")
						.getExptext());
		if (!isTextMatching) {
			logger.info("User is not on [STRUCTURE:CONTAINERS] tab ");
			logger.info("Click on [Structure] sub tab");
			// Identify Structure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getLocatorvalue());
			// Get the text
			String StructureTabText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTab").getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
				ReportUtils.setStepDescription(
						"[STRUCTURE] tab text matching failed",
						"",
						Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext(),
						StructureTabText, true);
				m_assert.fail("[STRUCTURE] tab text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTab").getExptext()
						+ "] and the actual return text is ["
						+ StructureTabText + "]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(5);
			logger.info("Click operation on [STRUCTURE] tab is successful. "
					+ "Verify User is on [STRUCTURE:CONTAINERS] page");
			// Identify Structure tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getLocatorvalue());
			// Get the text
			StructureTabHeaderText = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(
					StructureTabHeaderText,
					Suite.objectRepositoryMap.get(
							"ContentManagementStructureTabHeaderText")
							.getExptext());
			if (!isTextMatching) {
				logger.error("[STRUCTURE:CONTAINERS]page header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
				ReportUtils
						.setStepDescription(
								"[STRUCTURE:CONTAINERS]page header text matching failed",
								"",
								Suite.objectRepositoryMap
										.get("ContentManagementStructureTabHeaderText")
										.getExptext(), StructureTabHeaderText,
								true);
				m_assert.fail("[STRUCTURE:CONTAINERS]page header text matching failed. The Expected text is ["
						+ Suite.objectRepositoryMap.get(
								"ContentManagementStructureTabHeaderText")
								.getExptext()
						+ "] and the actual return text is ["
						+ StructureTabHeaderText + "]");
			}
		}
		logger.info("User is on [STRUCTURE:CONTAINERS] tab");
		logger.info("Verify if the container ["
				+ testcaseArgs.get("containerName")
				+ "] displayed in Containers list");
		// Identify Containers list
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"ContentManagementStructureTabContainersList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'Containers List' in  [STRUCTURE:CONTAINERS] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'Containers List' in  [STRUCTURE:CONTAINERS] page",
							true);
			m_assert.fail("Unable to identify 'Containers List' in  [STRUCTURE:CONTAINERS] page");
		}
		// Verify if the specific container is available
		boolean isVerified = SeleniumCustomUtils
				.checkContainerInContainersList(element,
						testcaseArgs.get("containerName"));
		if (!isVerified) {
			logger.error("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
			ReportUtils.setStepDescription("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page",
					true);
			m_assert.fail("Unable to find container ["
					+ testcaseArgs.get("containerName")
					+ "] in Contaienrs List in [STRUCTURE:CONTAINERS] page");
		}
		logger.info("[" + testcaseArgs.get("containerName")
				+ "] displyaed in Containers list");
		logger.info("Edit the container [" + testcaseArgs.get("containerName")
				+ "]");
		// Click at specific container
		boolean isClicked = SeleniumCustomUtils
				.clickAtEditLinkOfContainersInStructure(element,
						testcaseArgs.get("containerName"));
		if (!isClicked) {
			logger.error("Error while click on Edit link of a container ["
					+ testcaseArgs.get("containerName") + "]");
			ReportUtils.setStepDescription(
					"Error while click on Edit link of a container ["
							+ testcaseArgs.get("containerName") + "]", true);
			m_assert.fail("Error while click on Edit link of a container ["
					+ testcaseArgs.get("containerName") + "]");
		}
		logger.info("Click operation successful on Edit link of a container ["
				+ testcaseArgs.get("containerName") + "] ");
		// Identify Delete button
		element = SeleniumUtils
				.findobject(
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerEditDeleteBtn")
								.getLocatortype(),
						Suite.objectRepositoryMap
								.get("ContentManagementStructureTabNewContainerEditDeleteBtn")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify 'DELETE' button in [STRUCTURE:CONTAINERS] page");
			ReportUtils
					.setStepDescription(
							"Unable to identify 'DELETE' button in [STRUCTURE:CONTAINERS] page",
							true);
			m_assert.fail("Unable to identify 'DELETE' button in [STRUCTURE:CONTAINERS] page");
		}
		// Click on Delete button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		SeleniumUtils.acceptAlertWindow();
		m_assert.assertAll();
	}

	@Test(priority = 18, dependsOnMethods = { "loginAs" })
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
		boolean isClicked = SeleniumUtils.clickOnElement(element);
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
		try {
			logger.info("Closing the browser");
			SeleniumUtils.closeBrowser();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param testcase
	 * @return Method return the params list based on the input testcase
	 */
	public Map<String, String> getTestData(String testcase) {
		Testcase contentmanagement = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH
						+ GlobalConstants.CONTENEMANAGEMENT_FILE,
				Testcase.class);
		if (contentmanagement != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			List<Case> cases = contentmanagement.getCase();
			for (Case thisCase : cases) {
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
