package com.phunware.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.phunware.constants.GlobalConstants;
import com.phunware.jaxb.entity.Config;
import com.phunware.jaxb.entity.Config.Environment;
import com.phunware.jaxb.entity.OR;
import com.phunware.jaxb.entity.OR.Element;
import com.phunware.jaxb.entity.Parentsuite;
import com.phunware.jaxb.entity.Parentsuite.Testsuite;
import com.phunware.util.JaxbUtil;
import com.phunware.util.ReportUtils;

/**
 * @author bhargavas
 * @since 07/29/2013 This class is starting and ending point of framework.
 * 
 */
public class Suite {
	private static final Logger logger = Logger.getLogger(Suite.class);
	public static List<String> configproperties = new ArrayList<String>();
	public static List<String> scenarioslist = new ArrayList<String>();
	public static Map<String, Element> objectRepositoryMap = new HashMap<String, OR.Element>();

	/**
	 * @since 07/29/2013 This Method is the entry point for Test Suite
	 * 
	 */
	@BeforeSuite
	//@Parameters({ "browser", "systemType" })
	// public void FrameworkEntryPoint(String browser, String systemType) {
	public void FrameworkEntryPoint() {
		// Set Index page for Reports
		ReportUtils.setIndexPageDescription();
		// Set Author Details for Reports
		ReportUtils.setAuthorInfoForReports();
		logger.info("Starting the execution");
		logger.info("reading [config] file");
		Config config = (Config) JaxbUtil.unMarshal(
				GlobalConstants.CONFIG_LOCATION_PATH
						+ GlobalConstants.CONFIG_FILE, Config.class);
		if (config != null) {
			List<Environment> environments = config.getEnvironment();
			for (Environment thisEnvironment : environments) {
				String runMode = thisEnvironment.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					 configproperties.add(thisEnvironment.getBrowser());
					 configproperties.add(thisEnvironment.getUrl());
					//configproperties.add(System.getProperty("browser"));
					//configproperties.add(thisEnvironment.getUrl());
				}
			}
		}
		if (configproperties.size() == 0) {
			logger.error("Config properties are empty. Exiting program");
			ReportUtils.setStepDescription(
					"Config properties are empty. Exiting program", false);
			throw new SkipException(
					"Config properties are empty. Exiting program");
		}
		logger.info("reading [config] file successful");
		// Parse all the OR information
		File orDir = new File(GlobalConstants.OR_LOCATION_PATH);
		if (orDir.exists() && orDir.isDirectory()) {
			File[] orFiles = orDir.listFiles();
			if (orFiles != null && orFiles.length > 0) {
				for (File orFile : orFiles) {
					OR or = (OR) JaxbUtil.unMarshal(orFile.getAbsolutePath(),
							OR.class);
					if (or != null) {
						List<Element> orElements = or.getElement();
						if (orElements != null && orElements.size() > 0) {
							for (Element orElement : orElements) {
								objectRepositoryMap.put(orElement.getName(),
										orElement);
							}
						}
					}
				}
			}
		}
		logger.info("reading [ParentSuite] input file");
		Parentsuite parentsuite = (Parentsuite) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH + GlobalConstants.SUITE_FILE,
				Parentsuite.class);
		if (parentsuite != null) {
			List<Testsuite> testsuites = parentsuite.getTestsuite();
			for (Testsuite thissuite : testsuites) {
				String runMode = thissuite.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					scenarioslist.add(thissuite.getName());

				}
			}
		}
		if (scenarioslist.size() == 0) {
			logger.warn("No Test suites added for execution. Exiting program");
			ReportUtils.setStepDescription(
					"No Test suites added for execution. Exiting program",
					false);
			throw new SkipException(
					"No Test suites added for execution. Exiting program");
		}
		logger.info("reading [ParentSuite] input file is successful");
		logger.info("The Test suites for execution.." + scenarioslist);
	}

}
