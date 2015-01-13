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

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class Location extends Suite {
	private static Logger logger = Logger.getLogger(Location.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static boolean isTextMatching;
	private static WebElement element = null;
	private static String childSuite = "location";
	private static boolean suiteExecution = false;
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private Testcase locationSuite;
	private List<Case> testcases = null;
	private boolean isClicked;
	private SoftAssert m_assert;

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
		// if suiteExecution is false then skip the location test suite
		if (!suiteExecution) {
			logger.info("Test suite [Location] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [Location] is not added for execution", false);
			throw new SkipException(
					"Test suite [Location] is not added for execution");
		}
		logger.info("reading [Location] Input file");
		// reading Advertising input file
		locationSuite = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH + GlobalConstants.LOCATION_FILE,
				Testcase.class);
		if (locationSuite != null) {
			// Add the test cases into testcaseList
			testcases = locationSuite.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());
				}
			}
		}
		// If testcaseList size is zero skip the execution
		if (testcaseList.size() == 0) {
			logger.warn("No testCase added for execution "
					+ "in [Location] test suite");
			ReportUtils.setStepDescription("No TestCase added "
					+ "for execution " + "in [Location] suite", false);
			throw new SkipException("No testCases added for execution "
					+ "in [Location] test suite");
		}
		logger.info("reading [Location] Input file successful");
		ReportUtils.setStepDescription(
				"reading [Location] input file successful", false);
		logger.info(" {" + testcaseList + "} for execution in [Location] suite");
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
			logger.warn("Test case [loginAs] is not added for "
					+ "execution in [PhunwareClients]");
			ReportUtils.setStepDescription(
					"Test case [loginAs] is not added for "
							+ "execution in [PhunwareClients]", false);
			throw new SkipException("Test case [loginAs] is not added for "
					+ "execution in [PhunwareClients]");
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
	public void clickAndVerifyLocationTab() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("clickAndVerifyLocationTab")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the test case
		if (!forExecution) {
			logger.info("Test case [clickAndVerifyLocationTab] is not "
					+ "added for execution");
			ReportUtils.setStepDescription(
					"Test case [clickAndVerifyLocationTab] is not "
							+ "added for execution", false);
			throw new SkipException(
					"Test case [clickAndVerifyLocationTab] is not "
							+ "added for execution");
		}
		logger.info("Starting [clickAndVerifyLocationTab] execution");
		logger.info("Verify if [LOCATION] tab is present");
		// Identify Location tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationTab").getLocatortype(),
				Suite.objectRepositoryMap.get("LocationTab").getLocatorvalue());
		// Get the text of the LOCATION tab
		String LocationTabText = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpLocationTabText = Suite.objectRepositoryMap
				.get("LocationTab").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(LocationTabText,
				ExpLocationTabText);
		if (!isTextMatching) {
			logger.error("[LOCATION] tab text matching failed."
					+ " The Expected text is [" + ExpLocationTabText
					+ "] and the return text is [" + LocationTabText + "]");
			ReportUtils.setStepDescription(
					"[LOCATION] tab text matching failed", "",
					ExpLocationTabText, LocationTabText, true);
			m_assert.fail("[LOCATION] tab text matching failed."
					+ " The Expected text is [" + ExpLocationTabText
					+ "] and the return text is [" + LocationTabText + "]");
		}
		logger.info("[LOCATION] tab is present");
		logger.info("Navigate to [LOCATION] tab");
		// Click on lOCATION tab
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [LOCATION] tab");
			ReportUtils.setStepDescription("Unable to click on [LOCATION] tab",
					true);
			m_assert.fail("Unable to click on [LOCATION] tab");
		}
		// Verify the landing Page
		SeleniumUtils.sleepThread(4);
		logger.info("Verify if user is landed on [LOCATION] page");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatorvalue());
		// Get the text of the header element
		String ConfigureTabHeaderElement = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
				"LocationHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabHeaderElement,
				ExpConfigureTabHeaderElement);
		if (!isTextMatching) {
			logger.error("[Configure MSE] page header text matching failed:"
					+ " The expected text is [" + ExpConfigureTabHeaderElement
					+ "] and the actual return text is ["
					+ ConfigureTabHeaderElement + "]");
			ReportUtils.setStepDescription(
					"[Analytics] tab text matching failed", "",
					ExpConfigureTabHeaderElement, ConfigureTabHeaderElement,
					true);
			m_assert.fail("[Configure MSE] page header text matching failed:"
					+ " The expected text is [" + ExpConfigureTabHeaderElement
					+ "] and the actual return text is ["
					+ ConfigureTabHeaderElement + "]");
		}
		logger.info("Click operation on [LOCATION] tab successful");
		logger.info("Test case [clickAndVerifyLocationTab] execution"
				+ " is successful");
		m_assert.assertAll();
	}

	@Test(priority = 2, dependsOnMethods = "loginAs")
	public void verifyLocationTabLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyLocationTabLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyLocationTabLayout] is not added"
					+ " for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyLocationTabLayout] is not added"
							+ " for execution", false);
			throw new SkipException(
					"Test case [verifyLocationTabLayout] is not added"
							+ " for execution");
		}
		logger.info("Verify all the sub tabs present in [LOCATION] tab");
		// Identify Configure sub tab element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationConfigure")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationConfigure")
						.getLocatorvalue());
		// Get the text of the header element
		String LocationConfigure = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpLocationConfigure = Suite.objectRepositoryMap.get(
				"LocationConfigure").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(LocationConfigure,
				ExpLocationConfigure);
		if (!isTextMatching) {
			logger.error("[Configure] tab text matching failed:"
					+ " The expected text is [" + ExpLocationConfigure
					+ "] and the actual return text is [" + LocationConfigure
					+ "]");
			ReportUtils.setStepDescription(
					"[Analytics] tab text matching failed", "",
					ExpLocationConfigure, LocationConfigure, true);
			m_assert.fail("[Configure] tab text matching failed:"
					+ " The expected text is [" + ExpLocationConfigure
					+ "] and the actual return text is [" + LocationConfigure
					+ "]");
		}
		logger.info("Identification of [Configure] sub-tab is Successful");
		// Identify Map Editor Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditor]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditor]", true);
			m_assert.fail("Unable to identify [LocationMapEditor]");
		}
		// Get the text of the New Venue
		String mapEditor = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpmapEditor = Suite.objectRepositoryMap
				.get("LocationMapEditor").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditor, ExpmapEditor);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[MapEditor] tab text matching failed:"
					+ " The expected text is [" + ExpmapEditor
					+ "] and the actual return text is [" + mapEditor + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEditor] tab text matching failed", "",
					ExpmapEditor, mapEditor, true);
			m_assert.fail("[MapEditor] tab text matching failed:"
					+ " The expected text is [" + ExpmapEditor
					+ "] and the actual return text is [" + mapEditor + "]");
		}
		logger.info("Identificatio of [Map Editor] sub-tab is Successful");
		// Identify Support tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationSupportTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationSupportTab")
						.getLocatorvalue());
		// Get the text of the New Venue
		String LocationSupportTab = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpLocationSupportTab = Suite.objectRepositoryMap.get(
				"LocationSupportTab").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditor, ExpmapEditor);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Support] tab text matching failed:"
					+ " The expected text is [" + ExpLocationSupportTab
					+ "] and the actual return text is [" + LocationSupportTab
					+ "]");
			ReportUtils.setStepDescription(
					"[LocationSupportTab] tab text matching failed", "",
					ExpLocationSupportTab, LocationSupportTab, true);
			m_assert.fail("[Support] tab text matching failed:"
					+ " The expected text is [" + ExpLocationSupportTab
					+ "] and the actual return text is [" + LocationSupportTab
					+ "]");
		}
		logger.info("Identificatio of [Support] sub-tab is Successful");
		logger.info("Identification of all the sub tabs in [LOCATION] tab successful");
		m_assert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods = "loginAs")
	public void verifyConfigureTabLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyConfigureTabLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyConfigureTabLayout] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyConfigureTabLayout] is not added"
							+ " for execution", false);
			throw new SkipException(
					"Test case [verifyConfigureTabLayout] is not added for execution");
		}
		logger.info("Starting [verifyConfigureTabLayout] execution");
		logger.info("Verify if user is on [Configure] tab");
		// Identify Configure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatorvalue());
		// Get the text of the header element
		String ConfigureTabHeaderElement = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
				"LocationHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabHeaderElement,
				ExpConfigureTabHeaderElement);
		if (!isTextMatching) {
			logger.info("User is not on [Configure MSE] page");
			logger.info("Navigate to [Configure MSE] page");
			// Identify Configure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatorvalue());
			// Get the text
			String ConfigureText = SeleniumUtils.getText(element);
			String ExpConfigureText = Suite.objectRepositoryMap.get(
					"LocationConfigure").getExptext();
			// Compare the return text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(ConfigureText,
					ExpConfigureText);
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				logger.error("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
				ReportUtils.setStepDescription(
						"[Configure] tab text matching failed", "",
						ExpConfigureText, ConfigureText, true);
				m_assert.fail("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
			}
			// Click on Configure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Configure tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatorvalue());
			// Get the text of the header element
			ConfigureTabHeaderElement = SeleniumUtils.getText(element);
			// Get the expected text
			ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
					"LocationHeader").getExptext();
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(
					ConfigureTabHeaderElement, ExpConfigureTabHeaderElement);
			if (!isTextMatching) {
				logger.error("[Configure MSE] page header text matching failed:"
						+ " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
				ReportUtils.setStepDescription(
						"[Analytics] tab text matching failed", "",
						ExpConfigureTabHeaderElement,
						ConfigureTabHeaderElement, true);
				m_assert.fail("[Configure MSE] page header text matching "
						+ "failed:" + " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
			}
		}
		// Identify New Venue Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatorvalue());
		// Get the text of the New Venue
		String ActTextnewVenue = SeleniumUtils.getText(element);
		String ExpTextnewVenue = Suite.objectRepositoryMap.get(
				"LocationNewVenueBtn").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ActTextnewVenue,
				ExpTextnewVenue);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[New Venue] button in Configure tab text "
					+ "matching failed: " + "The expected text is ["
					+ ExpTextnewVenue + "] and the actual return text is ["
					+ ActTextnewVenue + "]");
			ReportUtils.setStepDescription(
					"[New Venue] button in Configure tab text matching failed",
					"", ExpTextnewVenue, ActTextnewVenue, true);
			m_assert.fail("[New Venue] button in Configure tab text "
					+ "matching failed: " + "The expected text is ["
					+ ExpTextnewVenue + "] and the actual return text is ["
					+ ActTextnewVenue + "]");
		}
		logger.info("Verify [New Venue Button]  is Successful");
		// Identify Configure Data
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ConfigureTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ConfigureTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Venue list] in Configure tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Venue list] in Configure tab", true);
			m_assert.fail("Unable to identify [Venue list] in Configure tab");
		}
		logger.info("Identification  of Configure data is successful");
		m_assert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods = "loginAs")
	public void verifyNewVenueTabLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyNewVenueTabLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyNewVenueTabLayout] is not added"
					+ " for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyNewVenueTabLayout] is not"
							+ " added for execution", false);
			throw new SkipException(
					"Test case [verifyNewVenueTabLayout] is not added"
							+ " for execution");
		}
		logger.info("Starting [verifyNewVenueTabLayout] execution");
		// Identify Configure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatorvalue());
		// Get the text of the header element
		String ConfigureTabHeaderElement = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
				"LocationHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabHeaderElement,
				ExpConfigureTabHeaderElement);
		if (!isTextMatching) {
			logger.info("User is not on [Configure MSE] page");
			logger.info("Navigate to [Configure MSE] page");
			// Identify Configure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatorvalue());
			// Get the text
			String ConfigureText = SeleniumUtils.getText(element);
			String ExpConfigureText = Suite.objectRepositoryMap.get(
					"LocationConfigure").getExptext();
			// Compare the return text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(ConfigureText,
					ExpConfigureText);
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				logger.error("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
				ReportUtils.setStepDescription(
						"[Configure] tab text matching failed", "",
						ExpConfigureText, ConfigureText, true);
				m_assert.fail("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
			}
			// Click on Configure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Configure tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatorvalue());
			// Get the text of the header element
			ConfigureTabHeaderElement = SeleniumUtils.getText(element);
			// Get the expected text
			ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
					"LocationHeader").getExptext();
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(
					ConfigureTabHeaderElement, ExpConfigureTabHeaderElement);
			if (!isTextMatching) {
				logger.error("[Configure MSE] page header text matching failed:"
						+ " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
				ReportUtils.setStepDescription(
						"[Analytics] tab text matching failed", "",
						ExpConfigureTabHeaderElement,
						ConfigureTabHeaderElement, true);
				m_assert.fail("[Configure MSE] page header text matching "
						+ "failed:" + " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
			}
		}
		// Identify New Venue Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationNewVenueBtn]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationNewVenueBtn]", true);
			m_assert.fail("Unable to identify [LocationNewVenueBtn]");
		}
		// Click on New Venue Button
		isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify subHeader Venue Information Text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderVenueInformation")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderVenueInformation")
						.getLocatorvalue());
		// Get the text of the newVenueSubHeader
		String newVenueSubHeader1 = SeleniumUtils.getText(element);
		String ExpTextSubHeader1 = Suite.objectRepositoryMap.get(
				"LocationAddVenueSubHeaderVenueInformation").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSubHeader1,
				ExpTextSubHeader1);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Add New Venue] page header text matching failed:"
					+ " The expected text is [" + ExpTextSubHeader1
					+ "] and the actual return text is [" + newVenueSubHeader1
					+ "]");
			ReportUtils.setStepDescription(
					"[Add New Venue] page header text matching failed", "",
					ExpTextSubHeader1, newVenueSubHeader1, true);
			m_assert.fail("[Add New Venue] page header text matching failed:"
					+ " The expected text is [" + ExpTextSubHeader1
					+ "] and the actual return text is [" + newVenueSubHeader1
					+ "]");
		}
		// Identify Venue Name Text Box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueNameInput]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueNameInput]", true);
			m_assert.fail("Unable to identify [LocationAddVenueNameInput]");
		}
		// Identify subHeader Perimeter NotificationsText
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderPerimeterNotification")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderPerimeterNotification")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueSubHeaderPerimeterNotification]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationAddVenueSubHeaderPerimeterNotification]",
							true);
			m_assert.fail("Unable to identify [LocationAddVenueSubHeaderPerimeterNotification]");
		}
		// Get the text of the Perimeter Notification Text tab
		String newVenueSubHeader2 = SeleniumUtils.getText(element);
		String ExpTextSubHeader2 = Suite.objectRepositoryMap.get(
				"LocationAddVenueSubHeaderPerimeterNotification").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSubHeader2,
				ExpTextSubHeader2);

		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueSubHeaderPerimeterNotification] "
					+ "text matching failed: The expected text is ["
					+ ExpTextSubHeader2 + "] and the actual return text is ["
					+ newVenueSubHeader2 + "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenueSubHeaderPerimeterNotification] "
							+ "tab text matching failed", "",
					ExpTextSubHeader2, newVenueSubHeader2, true);
			m_assert.assertEquals(ExpTextSubHeader2, newVenueSubHeader2);
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueHours]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueHours]", true);
			m_assert.fail("Unable to identify [LocationAddVenueHours]");
		}
		logger.info("Verify [cool Down Hours] Drop Down is Successful");
		// verify cool Down Minutes Drop Down
		// Identify cool Down Minutes Drop Down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueHoursMin]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueHoursMin]", true);
			m_assert.fail("Unable to identify [LocationAddVenueHoursMin]");
		}
		logger.info("Verify [cool Down Minutes] Drop Down is Successful");

		// verify location add venue check box pane
		// Identify location add venue check box pane
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBoxPane")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBoxPane")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueCheckBoxPane]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueCheckBoxPane]", true);
			m_assert.fail("Unable to identify [LocationAddVenueCheckBoxPane]");
		}
		logger.info("Verify [location add venue]check box pane is Successful");
		// verify subHeader Application Availability Text
		// Identify subHeader Application Availability Text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderApplicationAvailability")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderApplicationAvailability")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueSubHeaderApplicationAvailability]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationAddVenueSubHeaderApplicationAvailability]",
							true);
			m_assert.fail("Unable to identify [LocationAddVenueSubHeaderApplicationAvailability]");
		}
		// Get the text of the Application Availability Text tab
		String newVenueSubHeader3 = SeleniumUtils.getText(element);
		String ExpnewVenueSubHeader3 = Suite.objectRepositoryMap.get(
				"LocationAddVenueSubHeaderApplicationAvailability")
				.getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSubHeader3,
				ExpnewVenueSubHeader3);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueSubHeaderApplicationAvailability] "
					+ "text matching failed: The expected text is ["
					+ ExpnewVenueSubHeader3
					+ "] and the actual return text is [" + newVenueSubHeader3
					+ "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenueSubHeaderApplicationAvailability] "
							+ "tab text matching failed", "",
					ExpTextSubHeader2, newVenueSubHeader3, true);
			m_assert.assertEquals(ExpnewVenueSubHeader3, newVenueSubHeader3);
		}
		logger.info("Verify [Application Availability] Text is Successful");
		// verify cancel button is present or not
		// Identify cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueCancel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueCancel")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueCancel]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueCancel]", true);
			m_assert.fail("Unable to identify [LocationAddVenueCancel]");
		}
		// Get the text of the cancel button tab
		String newVenueCancel = SeleniumUtils.getText(element);
		String ExpnewVenueCancel = Suite.objectRepositoryMap.get(
				"LocationAddVenueCancel").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueCancel,
				ExpnewVenueCancel);

		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueCancel]  text matching failed: The expected text is ["
					+ ExpnewVenueCancel
					+ "] and the actual return text is ["
					+ newVenueCancel + "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenueCancel] tab text matching failed", "",
					ExpnewVenueCancel, newVenueCancel, true);
			m_assert.assertEquals(ExpnewVenueCancel, newVenueCancel);
		}
		logger.info("Verify [Cancel Button] Text is Successful");
		// verify Save button is present or not
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueSave]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueSave]", true);
			m_assert.fail("Unable to identify [LocationAddVenueSave]");
		}
		// Get the text of the Application Availability Text tab
		String newVenueSave = SeleniumUtils.getText(element);
		String ExpnewVenueSave = Suite.objectRepositoryMap.get(
				"LocationAddVenueSave").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSave,
				ExpnewVenueSave);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueSave]  text matching failed:"
					+ " The expected text is [" + newVenueSave
					+ "] and the actual return text is [" + ExpnewVenueSave
					+ "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenueSave] tab text matching failed", "",
					ExpnewVenueSave, newVenueSave, true);
			m_assert.assertEquals(ExpnewVenueSave, newVenueSave);
		}
		logger.info("Verify [Save Button] Text is Successful");
		logger.info("Test case [verifyNewVenueLayout] is successful");
		ReportUtils.setStepDescription(
				"Test case [verifyNewVenueLayout] is successful", false);
		m_assert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = "loginAs")
	public void validatingAddingNewVenue() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validatingAddingNewVenue")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [validatingAddingNewVenue] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [validatingAddingNewVenue] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [validatingAddingNewVenue] is not added for execution in [Location]");
		}
		// Click On Save Button without giving any input and check whether it is
		// in the same page or not
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatorvalue());
		// check the header of the page
		// Identify Header Text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenuesHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenuesHeader")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenuesHeader]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenuesHeader]", true);
			m_assert.fail("Unable to identify [LocationAddVenuesHeader]");
		}
		String ValidatingHeader = SeleniumUtils.getText(element);
		String ExpnewValidatingHeader = Suite.objectRepositoryMap.get(
				"LocationAddVenuesHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ValidatingHeader,
				ExpnewValidatingHeader);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenuesHeader]  text matching failed: The expected text is ["
					+ ValidatingHeader
					+ "] and the actual return text is ["
					+ ExpnewValidatingHeader + "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenuesHeader] tab text matching failed", "",
					ExpnewValidatingHeader, ValidatingHeader, true);
			m_assert.assertEquals(ExpnewValidatingHeader, ValidatingHeader);
		}
		m_assert.assertAll();
	}

	@Test(priority = 6, dependsOnMethods = "loginAs")
	public void createNewVenue() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewVenue")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createNewVenue] is not added "
					+ "for execution in [Location]");
			ReportUtils.setStepDescription("Test case [createNewVenue] is not "
					+ "added for execution in [Location]", false);
			throw new SkipException("Test case [createNewVenue] is not added "
					+ "for execution in [Location]");
		}
		testcaseArgs = getTestData("createNewVenue");
		WebElement venuename = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatorvalue());
		SeleniumUtils.type(venuename, testcaseArgs.get("venueName"));
		logger.info("inserted name successfully");
		/*
		 * SeleniumUtils.type(onentry, testcaseArgs.get("onEntry"));
		 * logger.info("inserted on entry succesfully");
		 */
		logger.info("Selecting hours and minutes from newvenue");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatorvalue(), testcaseArgs.get("hours"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("hours")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("hours")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("hours")
					+ "] from dropdown");
		}
		SeleniumUtils.sleepThread(4);
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatorvalue(), testcaseArgs.get("minutes"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("minutes")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("minutes")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("calandar")
					+ "] from dropdown");
		}
		// Identify Application list
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("L_AddVenue_AppList")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("L_AddVenue_AppList")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify applications list "
					+ "in [ADD NEW VENUE] page");
			ReportUtils.setStepDescription(
					"Unable to identify applications list "
							+ "in [ADD NEW VENUE] page", true);
			m_assert.fail("Unable to identify applications list "
					+ "in [ADD NEW VENUE] page");
		}
		// Select Applications
		isSelected = SeleniumCustomUtils.checkAppInAddVenue(element,
				testcaseArgs.get("appSelection1"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("appSelection1") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("appSelection1")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("appSelection1") + "] from dropdown");
		}
		isSelected = SeleniumCustomUtils.checkAppInAddVenue(element,
				testcaseArgs.get("appSelection2"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("appSelection2") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("appSelection2")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("appSelection2") + "] from dropdown");
		}
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify SAVE button "
					+ "in [ADD NEW VENUE] page");
			ReportUtils.setStepDescription("Unable to identify SAVE button "
					+ "in [ADD NEW VENUE] page", true);
			m_assert.fail("Unable to identify SAVE button "
					+ "in [ADD NEW VENUE] page");
		}
		// Click on Save Button
		SeleniumUtils.clickOnElement(element);
		m_assert.assertAll();
	}

	@Test(priority = 7, dependsOnMethods = "loginAs")
	public void verifyMappingLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMappingLayout")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [verifyMappingLayout] is not "
					+ "added for execution in [Location]");
			ReportUtils.setStepDescription(
					"Test case [verifyMappingLayout] is "
							+ "not added for execution in [Location]", false);
			throw new SkipException("Test case [verifyMappingLayout] is "
					+ "not added for execution in [Location]");
		}
		// Identify Map Editor Button
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditor]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditor]", true);
			m_assert.fail("Unable to identify [LocationMapEditor]");
		}
		// Get the text of the New Venue
		String mapEditor = SeleniumUtils.getText(element);
		String ExpmapEditor = Suite.objectRepositoryMap
				.get("LocationMapEditor").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditor, ExpmapEditor);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEditor]  text matching failed: "
					+ "The expected text is [" + mapEditor
					+ "] and the actual return text is [" + ExpmapEditor + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEditor] tab text matching failed", "",
					mapEditor, ExpmapEditor, true);
			m_assert.assertEquals(mapEditor, ExpmapEditor);
		}
		logger.info("Verify [Map Editor] is Successful");
		// Click on Map Editor Button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify Venue Selection drop down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorVenueSelectDD")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorVenueSelectDD")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Venue Dropdown] in [MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify [Venue Dropdown] in [MAP EDITOR] page",
					true);
			m_assert.fail("Unable to identify [Venue Dropdown] in [MAP EDITOR] page");
		}
		// Identify Building Select DD
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationMapEditorBuildingSelectDD").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationMapEditorBuildingSelectDD").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Building Dropdown] in "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify [Building Dropdown] in "
							+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify [Building Dropdown] in "
					+ "[MAP EDITOR] page");
		}
		// Identify Floor/Level Select DD
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorLevelSelectDD")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorLevelSelectDD")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Floor/Level Dropdown] in "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify [Floor/Level Dropdown] in "
							+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify [Floor/Level Dropdown] in "
					+ "[MAP EDITOR] page");
		}
		// Identify Draw-Draw-Marker
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDMarker]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDMarker]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDMarker]");
		}
		// Identify Draw-Draw-Point
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPoint]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPoint]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPoint]");
		}
		// Identify Draw-Draw-Point
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPoint]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPoint]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPoint]");
		}
		// Identify Draw-Draw-Point
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPolyline")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPolyline")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPolyline]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPolyline]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPolyline]");
		}
		// Identify Draw-Draw-Circle
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDCircle]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDCircle]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDCircle]");
		}
		// Identify Edit-Edit Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditEdit]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDEditEdit]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditEdit]");
		}
		// Identify Edit-Remove Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditRemove]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDEditRemove]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditRemove]");
		}
		// Identify Toggle Segments checkBox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleSegment")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleSegment")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEdiorToggleSegment]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEdiorToggleSegment]", true);
			m_assert.fail("Unable to identify [LocationMapEdiorToggleSegment]");
		}
		// Get the text of the Toggle Segments checkBox
		String mapEditorToggleSegment = SeleniumUtils.getText(element);
		String ExpmapEditorToggleSegment = Suite.objectRepositoryMap.get(
				"LocationMapEdiorToggleSegment").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditorToggleSegment,
				ExpmapEditorToggleSegment);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEdiorToggleSegment]  text matching failed: The expected text is ["
					+ mapEditorToggleSegment
					+ "] and the actual return text is ["
					+ ExpmapEditorToggleSegment + "]");
			ReportUtils
					.setStepDescription(
							"[LocationMapEdiorToggleSegment] tab text matching failed",
							"", mapEditorToggleSegment,
							ExpmapEditorToggleSegment, true);
			m_assert.assertEquals(mapEditorToggleSegment,
					ExpmapEditorToggleSegment);
		}
		// Identify Toggle Zones checkBox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleZones")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleZones")
						.getLocatorvalue());
		// Get the text of the Toggle Segments checkBox
		String mapEditorToggleZones = SeleniumUtils.getText(element);
		String ExpmapEditorToggleZones = Suite.objectRepositoryMap.get(
				"LocationMapEdiorToggleZones").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditorToggleZones,
				ExpmapEditorToggleZones);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEdiorToggleZones]  text matching failed: The expected text is ["
					+ mapEditorToggleZones
					+ "] and the actual return text is ["
					+ ExpmapEditorToggleZones + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEdiorToggleZones] tab text matching failed",
					"", mapEditorToggleZones, ExpmapEditorToggleZones, true);
			m_assert.assertEquals(mapEditorToggleZones, ExpmapEditorToggleZones);
		}
		m_assert.assertAll();
	}

	@Test(priority = 8, dependsOnMethods = "loginAs")
	public void creatingZone_One() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingZone_One")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingZone_One] is not "
					+ "added for execution in [Location]");
			ReportUtils.setStepDescription(
					"Test case [creatingZone_One] is not "
							+ "added for execution in [Location]", false);
			throw new SkipException("Test case [creatingZone_One] is not "
					+ "added for execution in [Location]");
		}
		testcaseArgs = getTestData("creatingZone_One");
		// Identify Venue Selection drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorVenueSelectDD")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorVenueSelectDD")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Venue Dropdown] in [MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify [Venue Dropdown] in [MAP EDITOR] page",
					true);
			m_assert.fail("Unable to identify [Venue Dropdown] in [MAP EDITOR] page");
		}
		// Select the Venue from the dropdown
		boolean isSelected = SeleniumUtils.selectOptionFromDropdown(element,
				testcaseArgs.get("venueName"));
		if (!isSelected) {
			logger.error("Unable to select [ " + testcaseArgs.get("venueName")
					+ " ] from Venue dropdown in [MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to select [ " + testcaseArgs.get("venueName")
							+ " ] from Venue dropdown in [MAP EDITOR] page",
					true);
			m_assert.fail("Unable to select [ " + testcaseArgs.get("venueName")
					+ " ] from Venue dropdown in [MAP EDITOR] page");
		}
		// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.sleepThread(5);
		// Identify and click on Zone creation
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDCircle]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDCircle]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDCircle]");
		}
		// clicks the zone element and holds the element and drags the mouse
		// w.r.t offset values
		SeleniumUtils.createZone_In_MapEditor(element, 200, 250);
		// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.sleepThread(6);
		// Identify ADD ZONE TRIGGER FORM
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneName").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneName").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Zone Name text box in "
					+ "[ADD ZONE TRIGGER] Form");
			ReportUtils.setStepDescription(
					"Unable to identify Zone Name text box in "
							+ "[ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to identify Zone Name text box in "
					+ "[ADD ZONE TRIGGER] Form");
		}
		// Click on Element
		SeleniumUtils.clickOnElement(element);
		// Type Zone Name
		SeleniumUtils.type(element, testcaseArgs.get("Name"));
		// Identify Message Textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Zone Message text box in "
					+ "[ADD ZONE TRIGGER] Form");
			ReportUtils.setStepDescription(
					"Unable to identify Zone Message text box in "
							+ "[ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to identify Zone Message text box in "
					+ "[ADD ZONE TRIGGER] Form");
		}
		// Click on Messages texbox
		SeleniumUtils.clickOnElement(element);
		// Type Zone Message
		SeleniumUtils.type(element, testcaseArgs.get("Message"));
		// Identify Cool Down Intervals Hours drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Cool Down Intervals Hours "
					+ "drop down in [ADD ZONE TRIGGER] Form ");
			ReportUtils.setStepDescription(
					"Unable to identify Cool Down Intervals Hours "
							+ "drop down in [ADD ZONE TRIGGER] Form ", true);
			m_assert.fail("Unable to identify Cool Down Intervals Hours "
					+ "drop down in [ADD ZONE TRIGGER] Form ");
		}
		// SeleniumUtils.clickOnElement(Hours_One);
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue(), testcaseArgs.get("zoneHours"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from  Hours dropdown in [ADD ZONE TRIGGER] Form ");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoneHours")
							+ "] from  Hours "
							+ "dropdown in [ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from  Hours dropdown in [ADD ZONE TRIGGER] Form");
		}
		// Identify Mins dropdown
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue());
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue(), testcaseArgs.get("zoneMin"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from  Mins dropdown in [ADD ZONE TRIGGER] Form ");
			ReportUtils
					.setStepDescription(
							"Unable to select  ["
									+ testcaseArgs.get("zoneMin")
									+ "] from  Mins dropdown in [ADD ZONE TRIGGER] Form ",
							true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from  Mins dropdown in [ADD ZONE TRIGGER] Form ");
		}
		SeleniumUtils.sleepThread(1);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button in "
					+ "[ADD ZONE TRIGGER] Form ");
			ReportUtils.setStepDescription("Unable to identify Save button in "
					+ "[ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to identify Save button in "
					+ "[ADD ZONE TRIGGER] Form");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.sleepThread(1);
		m_assert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = "loginAs")
	public void creatingPointOfInterest_One() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingPointOfInterest_One")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingPointOfInterest_One] is "
					+ "not added for execution in [Location]");
			ReportUtils.setStepDescription(
					"Test case [creatingPointOfInterest_One] is "
							+ "not added for execution in [Location]", false);
			throw new SkipException(
					"Test case [creatingPointOfInterest_One] is "
							+ "not added for execution in [Location]");
		}
		testcaseArgs = getTestData("creatingPointOfInterest_One");
		// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.sleepThread(2);
		// Identify POI element in Map Editor page
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI element in "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription("Unable to identify POI element in "
					+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify POI element in "
					+ "[MAP EDITOR] page");
		}
		// Create POI point
		SeleniumUtils.createPOI(element, 200, 300);
		// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.sleepThread(6);
		// Identify POI Name
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIName").getLocatortype(),
				Suite.objectRepositoryMap.get("POIName").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Name text box in [ADD POI] Form");
			ReportUtils.setStepDescription(
					"Unable to identify POI Name text box in [ADD POI] Form",
					true);
			m_assert.fail("Unable to identify POI Name text box in [ADD POI] Form");
		}
		// Click on POI name text box
		SeleniumUtils.clickOnElement(element);
		// Type the POI Name
		SeleniumUtils.type(element, testcaseArgs.get("Name"));
		// Identify POI type dropdown
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIType").getLocatortype(),
				Suite.objectRepositoryMap.get("POIType").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Type Drop down in [ADD POI] Form");
			ReportUtils
					.setStepDescription(
							"Unable to identify POI POI Type Drop down in [ADD POI] Form",
							true);
			m_assert.fail("Unable to identify POI POI Type Drop down in [ADD POI] Form");
		}
		// Select POI Type
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIType").getLocatortype(),
				Suite.objectRepositoryMap.get("POIType").getLocatorvalue(),
				testcaseArgs.get("Type"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from POI dropdown in [ADD POI] form");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("Type")
							+ "] from POI dropdown in [ADD POI] form", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from POI dropdown in [ADD POI] form");
		}
		// Identify POI Portal Id
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIPortalId").getLocatortype(),
				Suite.objectRepositoryMap.get("POIPortalId").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Portal Id in [ADD POI] form");
			ReportUtils.setStepDescription(
					"Unable to identify POI Portal Id in [ADD POI] form", true);
			m_assert.fail("Unable to identify POI Portal Id in [ADD POI] form");
		}
		// Click on POI Portal Id
		SeleniumUtils.clickOnElement(element);
		// Type POI Portal Id
		SeleniumUtils.type(element, testcaseArgs.get("portalId"));
		// Select Zoon Level from dorpdown
		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get("POIZoomLevel")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("POIZoomLevel").getLocatorvalue(),
						testcaseArgs.get("zoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
		}
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatorvalue(), testcaseArgs.get("maxZoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("maxZoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
		}
		// Identify POI Descripttion
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Description "
					+ "text box in [ADD POI] form");
			ReportUtils.setStepDescription(
					"Unable to identify POI Description "
							+ "text box in [ADD POI] form", true);
			m_assert.fail("Unable to identify POI Description "
					+ "text box in [ADD POI] form");
		}
		// Click
		SeleniumUtils.clickOnElement(element);
		// Type POI Description
		SeleniumUtils.type(element, testcaseArgs.get("description"));
		// Identify IsActive check box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIIsActive").getLocatortype(),
				Suite.objectRepositoryMap.get("POIIsActive").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify isActive checkbox element "
					+ "in [ADD POI] form");
			ReportUtils.setStepDescription("Unable to identify isActive "
					+ "checkbox element in [ADD POI] form", true);
			m_assert.fail("Unable to identify isActive checkbox element "
					+ "in [ADD POI] form");
		}
		// Click on Check box
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POISave").getLocatortype(),
				Suite.objectRepositoryMap.get("POISave").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button "
					+ "in [ADD POI] form");
			ReportUtils.setStepDescription("Unable to identify "
					+ "Save button in [ADD POI] form", true);
			m_assert.fail("Unable to identify Save button "
					+ "in [ADD POI] form");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		/*// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.clickElementThroughActions(element);*/
		// Wait for Page till loading completed
		SeleniumUtils.waitTillPageLoads(Suite.objectRepositoryMap.get(
				"LBS_Map_PageLoading").getLocatorvalue());
		SeleniumUtils.sleepThread(2);
		m_assert.assertAll();
	}

	@Test(priority = 10, dependsOnMethods = "loginAs")
	public void creatingZone_two() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingZone_two")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingZone_two] is not "
					+ "added for execution in [Location]");
			ReportUtils.setStepDescription(
					"Test case [creatingZone_two] is not "
							+ "added for execution in [Location]", false);
			throw new SkipException("Test case [creatingZone_two] is not "
					+ "added for execution in [Location]");
		}
		testcaseArgs = getTestData("creatingZone_two");
		if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
			// Refresh the browser
			SeleniumUtils.refreshPage();
			SeleniumUtils.sleepThread(4);
		}
		// Identify and click on Zone creation
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDCircle]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDCircle]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDCircle]");
		}
		// clicks the zone element and holds the element and drags the mouse
		// w.r.t offset values
		SeleniumUtils.createZone_In_MapEditor(element, 400, 200);
		SeleniumUtils.sleepThread(4);
		// Identify ADD ZONE TRIGGER FORM
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneName").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneName").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Zone Name text box in "
					+ "[ADD ZONE TRIGGER] Form");
			ReportUtils.setStepDescription(
					"Unable to identify Zone Name text box in "
							+ "[ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to identify Zone Name text box in "
					+ "[ADD ZONE TRIGGER] Form");
		}
		// Click on Element
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		// Type Zone Name
		SeleniumUtils.type(element, testcaseArgs.get("Name"));
		// Identify Message Textbox
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Zone Message text box in "
					+ "[ADD ZONE TRIGGER] Form");
			ReportUtils.setStepDescription(
					"Unable to identify Zone Message text box in "
							+ "[ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to identify Zone Message text box in "
					+ "[ADD ZONE TRIGGER] Form");
		}
		// Click on Messages texbox
		SeleniumUtils.clickOnElement(element);
		// Type Zone Message
		SeleniumUtils.type(element, testcaseArgs.get("Message"));
		// Identify Cool Down Intervals Hours drop down
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Cool Down Intervals Hours "
					+ "drop down in [ADD ZONE TRIGGER] Form ");
			ReportUtils.setStepDescription(
					"Unable to identify Cool Down Intervals Hours "
							+ "drop down in [ADD ZONE TRIGGER] Form ", true);
			m_assert.fail("Unable to identify Cool Down Intervals Hours "
					+ "drop down in [ADD ZONE TRIGGER] Form ");
		}
		// SeleniumUtils.clickOnElement(Hours_One);
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue(), testcaseArgs.get("zoneHours"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from  Hours dropdown in [ADD ZONE TRIGGER] Form ");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoneHours")
							+ "] from  Hours "
							+ "dropdown in [ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from  Hours dropdown in [ADD ZONE TRIGGER] Form");
		}
		// Identify Mins dropdown
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue());
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue(), testcaseArgs.get("zoneMin"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from  Mins dropdown in [ADD ZONE TRIGGER] Form ");
			ReportUtils
					.setStepDescription(
							"Unable to select  ["
									+ testcaseArgs.get("zoneMin")
									+ "] from  Mins dropdown in [ADD ZONE TRIGGER] Form ",
							true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from  Mins dropdown in [ADD ZONE TRIGGER] Form ");
		}
		SeleniumUtils.sleepThread(2);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button in "
					+ "[ADD ZONE TRIGGER] Form ");
			ReportUtils.setStepDescription("Unable to identify Save button in "
					+ "[ADD ZONE TRIGGER] Form", true);
			m_assert.fail("Unable to identify Save button in "
					+ "[ADD ZONE TRIGGER] Form");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		m_assert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = "loginAs")
	public void creatingPointOfInterest_Two() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingPointOfInterest_Two")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingPointOfInterest_Two] is "
					+ "not added for execution in [Location]");
			ReportUtils.setStepDescription(
					"Test case [creatingPointOfInterest_Two] is "
							+ "not added for execution in [Location]", false);
			throw new SkipException(
					"Test case [creatingPointOfInterest_Two] is "
							+ "not added for execution in [Location]");
		}
		testcaseArgs = getTestData("creatingPointOfInterest_Two");
		SeleniumUtils.sleepThread(2);
		// Identify POI element in Map Editor page
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI element in "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription("Unable to identify POI element in "
					+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify POI element in "
					+ "[MAP EDITOR] page");
		}
		SeleniumUtils.createPOI(element, 400, 200);
		SeleniumUtils.sleepThread(2);
		// Identify POI Name
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIName").getLocatortype(),
				Suite.objectRepositoryMap.get("POIName").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Name text box in [ADD POI] Form");
			ReportUtils.setStepDescription(
					"Unable to identify POI Name text box in [ADD POI] Form",
					true);
			m_assert.fail("Unable to identify POI Name text box in [ADD POI] Form");
		}
		// Click on POI name text box
		SeleniumUtils.clickOnElement(element);
		// Type the POI Name
		SeleniumUtils.type(element, testcaseArgs.get("Name"));
		// Identify POI type dropdown
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIType").getLocatortype(),
				Suite.objectRepositoryMap.get("POIType").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Type Drop down in [ADD POI] Form");
			ReportUtils
					.setStepDescription(
							"Unable to identify POI POI Type Drop down in [ADD POI] Form",
							true);
			m_assert.fail("Unable to identify POI POI Type Drop down in [ADD POI] Form");
		}
		// Select POI Type
		boolean isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIType").getLocatortype(),
				Suite.objectRepositoryMap.get("POIType").getLocatorvalue(),
				testcaseArgs.get("Type"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from POI dropdown in [ADD POI] form");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("Type")
							+ "] from POI dropdown in [ADD POI] form", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from POI dropdown in [ADD POI] form");
		}
		// Identify POI Portal Id
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIPortalId").getLocatortype(),
				Suite.objectRepositoryMap.get("POIPortalId").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Portal Id in [ADD POI] form");
			ReportUtils.setStepDescription(
					"Unable to identify POI Portal Id in [ADD POI] form", true);
			m_assert.fail("Unable to identify POI Portal Id in [ADD POI] form");
		}
		// Click on POI Portal Id
		SeleniumUtils.clickOnElement(element);
		// Type POI Portal Id
		SeleniumUtils.type(element, testcaseArgs.get("portalId"));
		// Select Zoon Level from dorpdown
		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get("POIZoomLevel")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("POIZoomLevel").getLocatorvalue(),
						testcaseArgs.get("zoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
		}
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatorvalue(), testcaseArgs.get("maxZoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("maxZoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
		}
		// Identify POI Descripttion
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify POI Description "
					+ "text box in [ADD POI] form");
			ReportUtils.setStepDescription(
					"Unable to identify POI Description "
							+ "text box in [ADD POI] form", true);
			m_assert.fail("Unable to identify POI Description "
					+ "text box in [ADD POI] form");
		}
		// Click
		SeleniumUtils.clickOnElement(element);
		// Type POI Description
		SeleniumUtils.type(element, testcaseArgs.get("description"));
		// Identify IsActive check box element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIIsActive").getLocatortype(),
				Suite.objectRepositoryMap.get("POIIsActive").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify isActive checkbox element "
					+ "in [ADD POI] form");
			ReportUtils.setStepDescription("Unable to identify isActive "
					+ "checkbox element in [ADD POI] form", true);
			m_assert.fail("Unable to identify isActive checkbox element "
					+ "in [ADD POI] form");
		}
		// Click on Check box
		SeleniumUtils.clickOnElement(element);
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POISave").getLocatortype(),
				Suite.objectRepositoryMap.get("POISave").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save button "
					+ "in [ADD POI] form");
			ReportUtils.setStepDescription("Unable to identify "
					+ "Save button in [ADD POI] form", true);
			m_assert.fail("Unable to identify Save button "
					+ "in [ADD POI] form");
		}
		// Click on Save button
		SeleniumUtils.clickOnElement(element);
		m_assert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "loginAs")
	public void creatingSegments() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingSegments")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingSegments] is "
					+ "not added for execution in [Location]");
			ReportUtils.setStepDescription("Test case [creatingSegments] is "
					+ "not added for execution in [Location]", false);
			throw new SkipException("Test case [creatingSegments] is "
					+ "not added for execution in [Location]");
		}
		// Identify WayPoint element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify WayPoint element in [MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify WayPoint element in [MAP EDITOR] page",
					true);
			m_assert.fail("Unable to identify WayPoint element in [MAP EDITOR] page");
		}
		// Create First Way Point
		SeleniumUtils.createWayPoint(element, 303, 279);
		SeleniumUtils.sleepThread(2);
		// Create Second Way Point
		SeleniumUtils.createWayPoint(element, 364, 279);
		SeleniumUtils.sleepThread(1);
		// Identify Line to connect Way Points
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPolyline")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPolyline")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPolyline]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPolyline]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPolyline]");
		}
		// Click on Element
		SeleniumUtils.clickOnElement(element);
		// Identify First POI
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("poiOne_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("poiOne_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify First created POI");
			ReportUtils.setStepDescription(
					"Unable to identify First created POI", true);
			m_assert.fail("Unable to identify First created POI");
		}
		// Click on First POI
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify First Way Point
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("wayPoint1_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("wayPoint1_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify First created WayPoint");
			ReportUtils.setStepDescription(
					"Unable to identify First created WayPoint", true);
			m_assert.fail("Unable to identify First created WayPoint");
		}
		// Click on First WayPoint
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Again Click on First Created WayPoint
		SeleniumUtils.clickOnElement(element);
		// Identify Second WayPoint
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("wayPoint2_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("wayPoint2_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Second created WayPoint");
			ReportUtils.setStepDescription(
					"Unable to identify Second created WayPoint", true);
			m_assert.fail("Unable to identify Second created WayPoint");
		}
		// Click on Second WayPoint
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Again Click on Second Created WayPoint
		SeleniumUtils.clickOnElement(element);
		// Identify Second POI
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("poiTwo_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("poiTwo_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Second created POI");
			ReportUtils.setStepDescription(
					"Unable to identify Second created POI", true);
			m_assert.fail("Unable to identify Second created POI");
		}
		// Click on Second POI
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Again Click on Second POI
		SeleniumUtils.clickOnElement(element);
		// Identify First POI
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("poiOne_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("poiOne_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify First created POI");
			ReportUtils.setStepDescription(
					"Unable to identify First created POI", true);
			m_assert.fail("Unable to identify First created POI");
		}
		// Click on First POI
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(3);
		m_assert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods = "loginAs")
	public void editingElementsOnMapEditor() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editingElementsOnMapEditor")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [editingElements] is"
					+ " not added for execution in [Location]");
			ReportUtils.setStepDescription("Test case [editingElements] is"
					+ " not added for execution in [Location]", false);
			throw new SkipException("Test case [editingElements] is"
					+ " not added for execution in [Location]");
		}
		// Identify Edit button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Edit button"
					+ " in [MAP EDITOR] page");
			ReportUtils.setStepDescription("Unable to identify"
					+ " Edit button" + " in [MAP EDITOR] page", true);
			m_assert.fail("Unable to identify Edit button"
					+ " in [MAP EDITOR] page");
		}
		// Click on Edit button
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify First POI
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("poiOne_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("poiOne_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify First created POI");
			ReportUtils.setStepDescription(
					"Unable to identify First created POI", true);
			m_assert.fail("Unable to identify First created POI");
		}
		// Move the First POI element
		SeleniumUtils.moveElementByCoordinates(element, 0, -100);
		SeleniumUtils.sleepThread(1);
		// Identify Save changes button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("saveChanges").getLocatortype(),
				Suite.objectRepositoryMap.get("saveChanges").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Save buttton in "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription("Unable to identify "
					+ "Save buttton in [MAP EDITOR] page", true);
			m_assert.fail("Unable to identify Save buttton in"
					+ " [MAP EDITOR] page");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		SeleniumUtils.acceptAlertWindow();
		SeleniumUtils.sleepThread(2);
		m_assert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = "loginAs")
	public void removingElementsOnMapEditor() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("removingElementsOnMapEditor")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [removingElementsOnMapEditor] is "
					+ "not added for execution in [Location]");
			ReportUtils.setStepDescription(
					"Test case [removingElementsOnMapEditor] is "
							+ "not added for execution in [Location]", false);
			throw new SkipException(
					"Test case [removingElementsOnMapEditor] is "
							+ "not added for execution in [Location]");
		}
		SeleniumUtils.sleepThread(2);
		// Identify Edit/Remove button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Edit/Remove button "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify Edit/Remove button "
							+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify Edit/Remove button "
					+ "[MAP EDITOR] page");
		}
		// Click on Element
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify POI1, POI2,WayPoint1 and Waypoint2,Zone1
		// POI1
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POI_One_After_Edit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POI_One_After_Edit")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify first POI elemet "
					+ "[MAP EDITOR] page after editing");
			ReportUtils.setStepDescription(
					"Unable to identify first POI elemet "
							+ "[MAP EDITOR] page after editing", true);
			m_assert.fail("Unable to identify first POI elemet "
					+ "[MAP EDITOR] page after editing");
		}
		// Click on First POI
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// POI2
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("poiTwo_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("poiTwo_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify second POI elemet "
					+ "[MAP EDITOR] page after editing");
			ReportUtils.setStepDescription(
					"Unable to identify second POI elemet "
							+ "[MAP EDITOR] page after editing", true);
			m_assert.fail("Unable to identify second POI elemet "
					+ "[MAP EDITOR] page after editing");
		}
		// Click on Second POI
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify First Way Point
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("wayPoint1_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("wayPoint1_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify First created WayPoint");
			ReportUtils.setStepDescription(
					"Unable to identify First created WayPoint", true);
			m_assert.fail("Unable to identify First created WayPoint");
		}
		// Click on First WayPoint
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify Second WayPoint
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("wayPoint2_firefox_old")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("wayPoint2_firefox_old")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Second created WayPoint");
			ReportUtils.setStepDescription(
					"Unable to identify Second created WayPoint", true);
			m_assert.fail("Unable to identify Second created WayPoint");
		}
		// Click on Second WayPoint
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("saveChanges").getLocatortype(),
				Suite.objectRepositoryMap.get("saveChanges").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Save] lanel after clicking"
					+ " on Edit/Delete button in [MAP EDITOR] page ");
			ReportUtils.setStepDescription("Unable to identify [Save]"
					+ " lanel after clicking on "
					+ "Edit/Delete button in [MAP EDITOR] page ", true);
			m_assert.fail("Unable to identify [Save] lanel after clicking"
					+ " on Edit/Delete button in [MAP EDITOR] page ");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify Edit/Remove button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Edit/Remove button "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify Edit/Remove button "
							+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify Edit/Remove button "
					+ "[MAP EDITOR] page");
		}
		// Click on Element
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Remove first zone
		SeleniumUtils.deleteZone_In_MapEditor(150, 150);
		SeleniumUtils.sleepThread(2);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("saveChanges").getLocatortype(),
				Suite.objectRepositoryMap.get("saveChanges").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Save] lanel after clicking"
					+ " on Edit/Delete button in [MAP EDITOR] page ");
			ReportUtils.setStepDescription("Unable to identify [Save]"
					+ " lanel after clicking on "
					+ "Edit/Delete button in [MAP EDITOR] page ", true);
			m_assert.fail("Unable to identify [Save] lanel after clicking"
					+ " on Edit/Delete button in [MAP EDITOR] page ");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Identify Edit/Remove button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify Edit/Remove button "
					+ "[MAP EDITOR] page");
			ReportUtils.setStepDescription(
					"Unable to identify Edit/Remove button "
							+ "[MAP EDITOR] page", true);
			m_assert.fail("Unable to identify Edit/Remove button "
					+ "[MAP EDITOR] page");
		}
		// Click on Element
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		// Remove second zone
		SeleniumUtils.deleteZone_In_MapEditor(350, 150);
		SeleniumUtils.sleepThread(2);
		// Identify Save button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("saveChanges").getLocatortype(),
				Suite.objectRepositoryMap.get("saveChanges").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Save] lanel after clicking"
					+ " on Edit/Delete button in [MAP EDITOR] page ");
			ReportUtils.setStepDescription("Unable to identify [Save]"
					+ " lanel after clicking on "
					+ "Edit/Delete button in [MAP EDITOR] page ", true);
			m_assert.fail("Unable to identify [Save] lanel after clicking"
					+ " on Edit/Delete button in [MAP EDITOR] page ");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(2);
		m_assert.assertAll();
	}

	@Test(priority = 15, dependsOnMethods = { "loginAs" })
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
		// Identify Sign Out button
		element = SeleniumUtils.findobject(
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
		// Identify Login page text
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("LoginPageText")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("LoginPageText")
								.getLocatorvalue());
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
		Testcase locationTestCase = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH + GlobalConstants.LOCATION_FILE,
				Testcase.class);
		if (locationTestCase != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			List<Case> cases = locationTestCase.getCase();
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