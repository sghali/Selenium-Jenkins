package com.phunware.util;

import org.openqa.selenium.WebDriver;

import atu.testng.reports.ATUReports;
import atu.testng.reports.utils.Utils;

/**
 * This class is used to reporting of results
 * 
 * @author bhargavas
 * 
 */
public class ReportUtils {

	/**
	 * 
	 */
	public static void setIndexPageDescription() {
		ATUReports.indexPageDescription = "Phunware - MaaS application Reports";
	}

	/**
	 * 
	 */
	public static void setAuthorInfoForReports() {
		ATUReports.setAuthorInfo("Automation Tester", Utils.getCurrentTime(),
				"1.0");
	}

	/**
	 * @param driver
	 */
	public static void setWebDriverReports(WebDriver driver) {
		ATUReports.setWebDriver(driver);
	}

	/**
	 * @param text
	 * @param status
	 */
	public static void setStepDescription(String text, boolean status) {
		ATUReports.add(text, status);
	}

	/**
	 * @param text
	 * @param inputvalue
	 * @param status
	 */
	public static void setStepDescription(String text, String inputvalue,
			boolean status) {
		ATUReports.add(text, inputvalue, status);

	}

	/**
	 * @param text
	 * @param inputValue
	 * @param expValue
	 * @param status
	 */
	public static void setStepDescription(String text, String inputValue,
			String expValue, boolean status) {
		ATUReports.add(text, inputValue, expValue, status);
	}

	/**
	 * @param text
	 * @param inputValue
	 * @param expValue
	 * @param actValue
	 * @param status
	 */
	public static void setStepDescription(String text, String inputValue,
			String expValue, String actValue, boolean status) {
		ATUReports.add(text, inputValue, expValue, actValue, status);
	}

}
