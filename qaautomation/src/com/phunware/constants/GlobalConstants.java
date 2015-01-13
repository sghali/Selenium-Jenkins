package com.phunware.constants;

/**
 * @author bhargavas
 * since 07/29/2013
 * This class contains all the path variables  throughout the project
 */
public class GlobalConstants {
	
	// Config location and file
	public static final String CONFIG_LOCATION_PATH =System.getProperty("user.dir")+"/config/";
	public static final String CONFIG_FILE="config.xml";		
	
	// OR Properties file location and files	
	public static final String OR_LOCATION_PATH=System.getProperty("user.dir")+"/objectRepository/";
	public static final String OR_LOGIN_FILE="loginpage.xml";
	public static final String OR_ANALYTICS_FILE="analyticspage.xml";
	public static final String OR_PHUNWARECLIENTS_FILE="phunwareclientspage.xml";
	public static final String OR_ALERTSNOTIFICATIONS_FILE="alertsandnotificationspage.xml";
		
	//All Input XML files
	public static final String INPUT_XML_PATH=System.getProperty("user.dir")+"/inputXML/";
	public static final String SUITE_FILE="parentsuite.xml";
	public static final String LOGIN_FILE="login.xml";
	public static final String PHUNWARECLIENTS_FILE="phunwareclients.xml";
	public static final String ANALYTICS_FILE="analytics.xml";
	public static final String ALERTSANDNOTIFICATIONS_FILE="alertsandnotifications.xml";
	public static final String CONTENEMANAGEMENT_FILE="contentmanagement.xml";
	public static final String ADVERTISING_FILE="advertising.xml";
	public static final String LOCATION_FILE="location.xml";
	public static final String COMMONSCENARIOS_FILE="commonscenarios.xml";
	
	
	
	//IE and Chrome drivers...	
	public static final String chromebrowser=System.getProperty("user.dir")+"/drivers/chromedriver.exe";
	public static final String iebrowser=System.getProperty("user.dir")+"/drivers/IEDriverServer.exe";
	public static final String CHROME_DRIVER_FOR_MAC=System.getProperty("user.dir")+"/drivers/chromedriver";
	
	//Screenshot path	
	public static final String SCREENSHOT_PATH=System.getProperty("user.dir")+"/reports/screenshots/";
	
	//Auto IT script & Image path
	public static final String IMAGE_PATH=System.getProperty("user.dir")+"\\autoitScript\\Desert.jpg";
	public static final String AUTOIT_SCRIPT=System.getProperty("user.dir")+"\\autoitScript\\sample2.exe";
	public static final String Handle_Alert_Window_Using_AutoIt=System.getProperty("user.dir")+"\\autoitScript\\click_OK_on_AlertWindow.exe";
	
	//Window Names
	public static final String FIREFOX_BROWSER="File Upload";
	public static final String CHROME_BROWSER="Open";
	public static final String IE_BROWSER="Choose File to Upload";
	public static final String SAFARI_BROWSER="Upload file";
	public static final String IE_Alert_Handle_Dialog_Title="Message from webpage";

}
	
	
	
	
	
	
	
	
