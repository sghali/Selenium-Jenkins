package com.phunware.util;

import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.phunware.constants.GlobalConstants;

/**
 * @author bhargavas
 * @param <HttpWebRequest>
 * @since 08/20/13 This class contains all selenium methods specific to Phunware
 *        Application
 * 
 */
public class SeleniumCustomUtils<HttpWebRequest> {
	private static Logger logger = Logger.getLogger(SeleniumUtils.class);

	// private static WebDriver driver = null;

	/**
	 * @param element
	 * @param application
	 * @return Verify the application name
	 */
	public static boolean checkApplicationName(WebElement element,
			String application) {
		List<WebElement> elementsList = null;
		String appName = null;
		try {
			elementsList = element.findElements(By
					.xpath(".//div[@class='app-name']"));
			for (WebElement elements : elementsList) {
				appName = elements.findElement(
						By.xpath(".//span[@class='app-name']")).getText();
				if (application.equalsIgnoreCase(appName)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param element
	 * @param application
	 * @return Click at Show Keys link of a specific application
	 */
	public static boolean clickAtAPPLocation(WebElement element,
			String application) {
		List<WebElement> elementsList = null;
		try {
			elementsList = element.findElements(By
					.xpath(".//div[@class='app']"));
			for (WebElement elements : elementsList) {
				String app = elements.findElement(
						By.xpath(".//span[@class='app-name']")).getText();
				if (app.equalsIgnoreCase(application)) {
					List<WebElement> links = elements.findElements(By
							.xpath(".//div[@class='app-actions']"));
					for (WebElement showkeyslink : links) {
						showkeyslink.findElement(
								By.xpath(".//a[@class='app-keys']")).click();
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param element
	 * @param application
	 * @return Click at Edit link of application
	 */
	public static boolean clickAtEditLocation(WebElement element,
			String application) {
		List<WebElement> elementsList = null;
		try {
			elementsList = element.findElements(By
					.xpath(".//div[@class='app']"));
			for (WebElement elements : elementsList) {
				String app = elements.findElement(
						By.xpath(".//span[@class='app-name']")).getText();
				if (app.equalsIgnoreCase(application)) {
					List<WebElement> links = elements.findElements(By
							.xpath(".//div[@class='app-actions']"));
					for (WebElement editLink : links) {
						editLink.findElement(
								By.xpath(".//a[@class='app-edit']")).click();
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * This method verifies show keys table under specific application
	 * 
	 * @param element
	 *            - show keys table
	 * @param appName
	 */
	public static boolean verifyShowKeysTableData(WebElement element,
			String application) {
		List<WebElement> appList = null;
		List<WebElement> linkList = null;
		WebElement tableElement = null;
		List<WebElement> tableRows = null;
		String field = null;
		String value = null;
		int flag = 0;
		try {
			appList = element.findElements(By.xpath(".//div[@class='app']"));
			for (WebElement appl : appList) {
				String app = appl.findElement(
						By.xpath(".//span[@class='app-name']")).getText();
				if (app.equalsIgnoreCase(application)) {
					linkList = appl.findElements(By
							.xpath(".//div[@class='app-actions']"));
					for (WebElement childlink : linkList) {
						childlink.findElement(
								By.xpath(".//a[@class='app-keys']")).click();
						break;
					}
					tableElement = appl
							.findElement(By
									.xpath("..//div[@class='app-keys-display']/table/tbody"));
					tableRows = tableElement.findElements(By.tagName("tr"));
					for (WebElement row : tableRows) {
						field = row.findElement(
								By.xpath(".//td[@class='app-key-cell']"))
								.getText();
						if (field.equalsIgnoreCase("App Id:")) {
							value = row.findElement(By.xpath(".//td[2]"))
									.getText();
							if (value != null) {
								logger.info("The field is  " + field
										+ "and the value is  " + value);
							} else {
								flag++;
								logger.error("The field is  " + field
										+ "and the value is  " + value);
							}
						} else if (field.equalsIgnoreCase("Access Key:")) {
							value = row.findElement(By.xpath(".//td[2]"))
									.getText();
							if (value != null) {
								logger.info("The field is  " + field
										+ "and the value is  " + value);
							} else {
								flag++;
								logger.error("The field is  " + field
										+ "and the value is  " + value);
							}
						} else if (field.equalsIgnoreCase("Signature Key:")) {
							value = row.findElement(By.xpath(".//td[2]"))
									.getText();
							if (value != null) {
								logger.info("The field is  " + field
										+ "and the value is  " + value);
							} else {
								flag++;
								logger.error("The field is  " + field
										+ "and the value is  " + value);
							}
						} else {
							flag++;
							logger.error("The specified " + field + " "
									+ "is not present");
						}
					}
					if (flag == 0) {
						return true;
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @param string
	 * @return true if specified name/email/role present Users List table
	 * 
	 */
	public static boolean verifyUserInTable(WebElement element, String username) {
		List<WebElement> rows = null;
		List<WebElement> cols = null;
		boolean flag = false;
		try {
			if (element != null) {
				rows = element.findElements(By.tagName("tr"));
				for (WebElement row : rows) {
					cols = row.findElements(By.tagName("td"));
					for (WebElement col : cols) {
						if (username.equalsIgnoreCase(col.getText())) {
							flag = true;
							return flag;
						}
					}
				}
			} else {
				logger.error("Users List table is null");
				return flag;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * This method is used to click on Edit link of a specific user in User
	 * table of Users & Roles tab
	 * 
	 * @param element
	 * @param username
	 * @return - true if clicks on Edit link else false
	 */
	public static boolean click_At_EditLink_At_Users_In_UsersAndRoles(
			WebElement element, String username) {
		List<WebElement> rows = null;
		String ActualUserText = null;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				ActualUserText = row.findElement(By.xpath("./td[1]")).getText();
				if (username.equalsIgnoreCase(ActualUserText)) {
					WebElement editLink = row
							.findElement(By.xpath("./td[4]/a"));
					SeleniumUtils.click_Using_JavaScript(editLink);
					WebElement sub_Edit_Link = row.findElement(By
							.xpath("./td[4]/div[@class='tools']/a[2]"));
					SeleniumUtils.click_Using_JavaScript(sub_Edit_Link);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * This method is used to click on Delete link of a specific user in User
	 * table of Users & Roles tab
	 * 
	 * @param element
	 * @param username
	 * @return - true if clicks on Edit link else false
	 */
	public static boolean delete_User_By_UserName_for_IE_in_UsersAndRoles(
			WebElement element, String username) {
		List<WebElement> rows = null;
		String ActualUserText = null;
		try {
			// Get number of rows
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				// Get the actual user name
				ActualUserText = row.findElement(By.xpath("./td[1]")).getText();
				if (username.equalsIgnoreCase(ActualUserText)) {
					WebElement EditLink = row
							.findElement(By.xpath("./td[4]/a"));
					// Click on Parent Edit link using java script
					SeleniumUtils.click_Using_JavaScript(EditLink);
					// Get the child - delete link after clicking on Parent Edit
					// Link
					WebElement deleteButton = row.findElement(By
							.xpath("./td[4]/div[@class='tools']/a[1]"));
					// Run the process to click on OK button on Alert window
					Process process = new ProcessBuilder(
							GlobalConstants.Handle_Alert_Window_Using_AutoIt,
							GlobalConstants.IE_Alert_Handle_Dialog_Title)
							.start();
					// Click on Child - Delete window
					SeleniumUtils.click_Using_JavaScript(deleteButton);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while doing delete User in Users & Roles in IE browser "
					+ e.toString());
		}
		return false;
	}

	/**
	 * This method is used to click on Delete link of a specific user in User
	 * table of Users & Roles tab
	 * 
	 * @param element
	 * @param username
	 * @return - true if clicks on Edit link else false
	 */
	public static boolean clickAtDeleteForUsers(WebElement element,
			String username) {
		List<WebElement> rows = null;
		String ActualUserText = null;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				ActualUserText = row.findElement(By.xpath("./td[1]")).getText();
				if (username.equalsIgnoreCase(ActualUserText)) {
					row.findElement(By.xpath("./td[4]/a")).click();
					row.findElement(
							By.xpath("./td[4]/a/preceding-sibling::div[@class='tools']/a[1]/i"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * This method is used to click on Delete link of a specific user in User
	 * table of Users & Roles tab
	 * 
	 * @param element
	 * @param email
	 * @return - true if clicks on delete link else false
	 */
	public static boolean deleteUserAccountViaEmail(WebElement element,
			String email) {
		List<WebElement> rows = null;
		String ActualUserText = null;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				ActualUserText = row.findElement(By.xpath("./td[2]")).getText();
				if (email.equalsIgnoreCase(ActualUserText)) {
					row.findElement(By.xpath("./td[4]/a")).click();
					/*
					 * WebElement
					 * Delete_Element=SeleniumUtils.waitForElementToIdentify
					 * ("xpath",
					 * "./td[4]/a/preceding-sibling::div[@class='tools']/a[1]/i"
					 * ); Delete_Element.click();
					 */
					row.findElement(
							By.xpath("./td[4]/a/preceding-sibling::div[@class='tools']/a[1]/i"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean editApplication(WebElement element,
			String application, String newApplication, String platform,
			String category) {
		List<WebElement> appList = null;
		List<WebElement> linkList = null;
		WebElement saveBtn = null;
		try {
			appList = element.findElements(By.xpath(".//div[@class='app']"));
			for (WebElement appl : appList) {
				String app = appl.findElement(
						By.xpath(".//span[@class='app-name']")).getText();
				if (app.equalsIgnoreCase(application)) {
					linkList = appl.findElements(By
							.xpath(".//div[@class='app-actions']"));
					for (WebElement editLink : linkList) {
						editLink.findElement(
								By.xpath(".//a[@class='app-edit']")).click();
						break;
					}
					WebElement formAppNameElement = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[1]/div/div/input"));
					formAppNameElement.clear();
					formAppNameElement.sendKeys(newApplication);

					WebElement platformDropdown = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[2]/div[1]/div/select"));
					Select select = new Select(platformDropdown);
					select.selectByVisibleText(platform);

					WebElement categoryDropdown = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[2]/div[2]/div/select"));
					select = new Select(categoryDropdown);
					select.selectByVisibleText(category);
					saveBtn = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[3]/div/button[2]"));
					saveBtn.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	public static boolean isApplicationEdited(WebElement element,
			String application, String platform, String category) {

		List<WebElement> appList = null;
		List<WebElement> linkList = null;
		WebElement cancelBtn = null;
		try {
			appList = element.findElements(By.xpath(".//div[@class='app']"));
			for (WebElement appl : appList) {
				String app = appl.findElement(
						By.xpath(".//span[@class='app-name']")).getText();
				if (app.equalsIgnoreCase(application)) {
					linkList = appl.findElements(By
							.xpath(".//div[@class='app-actions']"));
					for (WebElement editLink : linkList) {
						editLink.findElement(
								By.xpath(".//a[@class='app-edit']")).click();
						break;
					}
					WebElement formAppNameElement = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[1]/div/div/input"));
					String newAppValue = formAppNameElement
							.getAttribute("value");

					WebElement platformDropdown = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[2]/div[1]/div/select"));
					Select select = new Select(platformDropdown);
					String newPlatformValue = select.getFirstSelectedOption()
							.getText();

					WebElement categoryDropdown = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[2]/div[2]/div/select"));
					select = new Select(categoryDropdown);
					String newCategoryValue = select.getFirstSelectedOption()
							.getText();
					if (!newAppValue.equalsIgnoreCase(application)
							|| !newPlatformValue.equalsIgnoreCase(platform)
							|| !newCategoryValue.equalsIgnoreCase(category)) {
						logger.error("Error while editing the application");
						cancelBtn = appl
								.findElement(By
										.xpath("..//div[@class='app-form']/form/div[3]/div/button[1]"));
						cancelBtn.click();
						return false;
					}
					cancelBtn = appl
							.findElement(By
									.xpath("..//div[@class='app-form']/form/div[3]/div/button[1]"));
					cancelBtn.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	/**
	 * This method is used to identify specific Role is available in Role table
	 * list
	 * 
	 * @param element
	 * @param rolename
	 * @return
	 */
	public static boolean verifyRoleNameInRoleList(WebElement element,
			String rolename) {
		List<WebElement> rowsList = null;
		List<WebElement> colsList = null;
		boolean flag = false;
		try {
			rowsList = element.findElements(By.tagName("tr"));
			if (rowsList.size() > 0) {
				for (WebElement row : rowsList) {
					colsList = row.findElements(By.tagName("td"));
					for (WebElement col : colsList) {
						if (rolename.equalsIgnoreCase(col.getText())) {
							flag = true;
							return flag;
						}
					}
				}
			} else {
				logger.error("No roles found in Roles list table");
				return flag;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * This method is used to edit the specific Role
	 * 
	 * @param element
	 * @param rolename
	 * @return - true if click on Edit link of specific Role else false
	 */
	public static boolean click_At_EditLink_At_Roles_In_UsersAndRoles(
			WebElement element, String rolename) {
		List<WebElement> rowsList = null;
		String Actual_Role_Text = null;
		try {
			// Get the number of rows
			rowsList = element.findElements(By.tagName("tr"));
			for (WebElement row : rowsList) {
				// Get the actual Role Name
				Actual_Role_Text = row.findElement(By.xpath("./td[1]"))
						.getText();
				if (rolename.equalsIgnoreCase(Actual_Role_Text)) {
					// Get the Parent Edit link
					WebElement editLink = row
							.findElement(By.xpath("./td[2]/a"));
					// Click on Parent Edit link using java script
					SeleniumUtils.click_Using_JavaScript(editLink);
					// Get the child Edit link after clicking on Parent Edit
					// link
					WebElement sub_Edit_Link = row.findElement(By
							.xpath("./td[2]/div[@class='tools']/a[2]"));
					// Click on child Edit link
					SeleniumUtils.click_Using_JavaScript(sub_Edit_Link);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * This method is used to delete the specific role
	 * 
	 * @param element
	 * @param rolename
	 * @return - true if clicks on delete link else false
	 */
	public static boolean delete_Role_for_IE_In_UsersAndRoles(
			WebElement element, String rolename) {
		List<WebElement> rowsList = null;
		try {
			// Get the number of rows
			rowsList = element.findElements(By.tagName("tr"));
			for (WebElement row : rowsList) {
				// Get the Parent Edit link
				WebElement editLink = row.findElement(By.xpath("./td[2]/a"));
				// Click on Parent Edit link using java script
				SeleniumUtils.click_Using_JavaScript(editLink);
				// Get the child Edit link after clicking on Parent Edit link
				WebElement sub_Delete_Link = row.findElement(By
						.xpath("./td[2]/div[@class='tools']/a[1]"));
				// Click on child Edit link
				// Run the process to click on OK button on Alert window
				Process process = new ProcessBuilder(
						GlobalConstants.Handle_Alert_Window_Using_AutoIt,
						GlobalConstants.IE_Alert_Handle_Dialog_Title).start();
				// Click on Child - Delete window
				SeleniumUtils.click_Using_JavaScript(sub_Delete_Link);
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * This method is used to delete the specific role
	 * 
	 * @param element
	 * @param rolename
	 * @return - true if clicks on delete link else false
	 */
	public static boolean deleteRole(WebElement element, String rolename) {
		List<WebElement> rowsList = null;
		List<WebElement> colsList = null;
		boolean flag = false;
		try {
			rowsList = element.findElements(By.tagName("tr"));
			for (WebElement row : rowsList) {
				colsList = row.findElements(By.tagName("td"));
				for (WebElement col : colsList) {
					if (rolename.equalsIgnoreCase(col.getText())) {
						row.findElement(By.xpath("./td[2]/a")).click();
						row.findElement(
								By.xpath("./td[2]/a/preceding-sibling::div[@class='tools']/a[1]"))
								.click();
						flag = true;
						return flag;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean selectCheckbox(WebElement element,
			String checkboxOption) {
		List<WebElement> divList = null;
		try {
			divList = element.findElements(By.tagName("div"));
			for (WebElement div : divList) {
				if (checkboxOption.equalsIgnoreCase(div.findElement(
						By.xpath(".//label")).getText())) {
					div.findElement(By.xpath(".//input")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean checkMessageInMessageOverview(WebElement element,
			String message) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				if (message.equalsIgnoreCase(row.findElement(
						By.xpath(".//td[3]/a")).getText())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param element
	 *            - Messages List WebElement
	 * @param message
	 *            - message hyperlink o
	 * @return
	 */
	public static boolean click_At_Message_Hyperlink_In_Message(
			WebElement element) {
		List<WebElement> rows = null;
		try {
			// Get the number of rows
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				// Get the Message Hyperlink Element
				WebElement Message_Hyperlink = row.findElement(By
						.xpath("./td[3]/a"));
				Message_Hyperlink.click();
				return true;
			}
		} catch (Exception e) {
			logger.error("Error while doing click on Message hyperlink in Messages "
					+ e.toString());
		}
		return false;
	}

	public static boolean checkScheduledMessageInMessageList(
			WebElement element, String message) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				if (message.equalsIgnoreCase(row.findElement(
						By.xpath(".//td[2]/a")).getText())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickAtRadioLocation(WebElement element, String time) {
		List<WebElement> radioButtons = null;
		try {
			radioButtons = element.findElements(By.tagName("label"));
			for (WebElement radiobtn : radioButtons) {
				if (time.equalsIgnoreCase(radiobtn.getText().replaceAll("\\s+",
						""))) {
					radiobtn.findElement(By.xpath(".//input")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectDateFromCalandar(WebElement element, String date) {
		List<WebElement> rows = null;
		List<WebElement> cols = null;
		try {
			rows = element.findElements(By.xpath(".//tbody"));
			for (WebElement row : rows) {
				cols = row.findElements(By.tagName("td"));
				for (WebElement col : cols) {
					if (date.equalsIgnoreCase(col.getText())) {
						col.click();
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean verifySegmentInSegmentList(WebElement element,
			String segment) {
		List<WebElement> rows = null;
		try {
			rows = element
					.findElements(By
							.xpath(".//div[@class='group-row parent-without-children']"));
			for (WebElement row : rows) {
				String actualSegment = row.findElement(
						By.xpath("./table/tbody/tr/td[1]")).getText();
				if (segment.equalsIgnoreCase(actualSegment)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	/**
	 * This method is used to edit the Segment
	 * 
	 * @param element
	 *            - Segment Table Element
	 * @param segment
	 *            - Present Segment Name
	 * @param newSegment
	 *            - New Segment Name
	 * @return
	 */
	public static boolean edit_The_Segment_In_Segments(WebElement element,
			String Present_Segment, String New_Segment) {
		List<WebElement> rows = null;
		try {
			// Get the rows in Segment list
			rows = element
					.findElements(By
							.xpath(".//div[@class='group-row parent-without-children']"));
			for (WebElement row : rows) {
				// Get the Present Segment Name
				String actual_Return_Segment_Name = row.findElement(
						By.xpath("./table/tbody/tr/td[1]")).getText();
				if (Present_Segment
						.equalsIgnoreCase(actual_Return_Segment_Name)) {
					// Get the Parent Edit link in Segment Table
					WebElement parent_Edit_Link = row.findElement(By
							.xpath("./table/tbody/tr/td[3]/a"));
					// Click on Edit Link
					SeleniumUtils.click_Using_JavaScript(parent_Edit_Link);
					// Get the Child Edit Link after clicking on Parent Edit
					// Link
					WebElement child_Edit_Link = row
							.findElement(By
									.xpath("./table/tbody/tr/td[3]/div/a[@class='edit']"));
					// Click on Child Edit link
					SeleniumUtils.click_Using_JavaScript(child_Edit_Link);
					// Identify the text box after clicking on Child Edit link
					WebElement rowElementTextbox = row
							.findElement(By
									.xpath("./preceding-sibling::*[1]/form/input[@class='group-name']"));
					// Clear the text
					rowElementTextbox.clear();
					// Pass the new Segment name
					rowElementTextbox.sendKeys(New_Segment);
					// Click on Submit button in Edit Segment Form
					row.findElement(
							By.xpath("./preceding-sibling::*[1]/form/button[@id='add-group-submit']"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while doing edit on Segments " + e.toString());
		}
		return false;
	}

	/**
	 * @param element
	 *            - Segment Table Element
	 * @param segment
	 *            - Segment name to delete
	 * @return
	 */
	public static boolean delete_The_Segment_In_Segments(WebElement element,
			String segment) {
		List<WebElement> rows = null;
		try {
			// Get the rows
			rows = element
					.findElements(By
							.xpath(".//div[@class='group-row parent-without-children']"));
			for (WebElement row : rows) {
				// Get the Present Segment Name
				String actual_Return_Segment_Name = row.findElement(
						By.xpath("./table/tbody/tr/td[1]")).getText();
				if (segment.equalsIgnoreCase(actual_Return_Segment_Name)) {
					// Get the Parent Edit link in Segment Table
					WebElement parent_Edit_Link = row.findElement(By
							.xpath("./table/tbody/tr/td[3]/a"));
					// Click on Edit Link
					SeleniumUtils.click_Using_JavaScript(parent_Edit_Link);
					// Get the Child Delete Link after clicking on Parent Edit
					// Link
					WebElement child_Delete_Link = row
							.findElement(By
									.xpath("./table/tbody/tr/td[3]/div/a[@class='delete']"));
					// Click on Child Delete link
					child_Delete_Link.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while doing delete on Segments " + e.toString());
		}
		return false;
	}

	/**
	 * @param element
	 *            - Segment Table Element
	 * @param segment
	 *            - Segment name to delete
	 * @return
	 */
	public static boolean delete_The_Segment_In_Segments_for_IE(
			WebElement element, String segment) {
		List<WebElement> rows = null;
		try {
			// Get the rows
			rows = element
					.findElements(By
							.xpath(".//div[@class='group-row parent-without-children']"));
			for (WebElement row : rows) {
				// Get the Present Segment Name
				String actual_Return_Segment_Name = row.findElement(
						By.xpath("./table/tbody/tr/td[1]")).getText();
				if (segment.equalsIgnoreCase(actual_Return_Segment_Name)) {
					// Get the Parent Edit link in Segment Table
					WebElement parent_Edit_Link = row.findElement(By
							.xpath("./table/tbody/tr/td[3]/a"));
					// Click on Edit Link
					SeleniumUtils.click_Using_JavaScript(parent_Edit_Link);
					// Get the Child Delete Link after clicking on Parent Edit
					// Link
					WebElement child_Delete_Link = row
							.findElement(By
									.xpath("./table/tbody/tr/td[3]/div/a[@class='delete']"));
					Process process = new ProcessBuilder(
							GlobalConstants.Handle_Alert_Window_Using_AutoIt,
							GlobalConstants.IE_Alert_Handle_Dialog_Title)
							.start();
					// Click on Child Delete link
					SeleniumUtils.click_Using_JavaScript(child_Delete_Link);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while doing delete on Segments " + e.toString());
		}
		return false;
	}

	public static boolean clickOnUpdateLinkInConfigure(WebElement element,
			String ExpApplication) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.tagName("li"));
			for (WebElement row : rows) {
				if (ExpApplication
						.equalsIgnoreCase(row
								.findElement(
										By.xpath(".//div[@class='app']/div[@class='app-name']"))
								.getText().trim())) {

					row.findElement(
							By.xpath(".//div[@class='app']/div[@class='app-actions']/a"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	public static boolean verifyUpdateCertificateForm(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.tagName("li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath(".//div[@class='app']/div[@class='app-name']"))
								.getText().trim())) {
					// row.findElement(By.xpath(".//div[@class='app']/div[@class='app-actions']/a")).click();
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell certificate']"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell password']"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell environment']"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell buttons']/button[1]"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell buttons']/button[2]"));
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean verifyUpdateAPIeForm(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.tagName("li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath(".//div[@class='app']/div[@class='app-name']"))
								.getText().trim())) {
					// row.findElement(By.xpath(".//div[@class='app']/div[@class='app-actions']/a")).click();
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell token']/label[1]"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell token']/input[1]"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell token']/label[2]"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell token']/input[2]"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell buttons']/button[1]"));
					row.findElement(By
							.xpath(".//div[@class='app-form']/form/div[@class='cell buttons']/button[2]"));
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickOnDownloadLink(WebElement element, String sdk) {
		try {
			if (sdk.equalsIgnoreCase("ios")) {
				element.findElement(
						By.xpath(".//a[@class='button-download-ios']")).click();
				return true;
			} else if (sdk.equalsIgnoreCase("android")) {
				element.findElement(
						By.xpath(".//a[@class='button-download-android']"))
						.click();
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickOnDocument(String document) {
		try {
			if (document.equalsIgnoreCase(" MaaSCore (iOS)")) {
				SeleniumUtils.click("xpath", ".//a[text()=' MaaSCore (iOS)']");
				return true;
			} else if (document.equalsIgnoreCase(" MaaSAlerts (iOS)")) {
				SeleniumUtils
						.click("xpath", ".//a[text()=' MaaSAlerts (iOS)']");
				return true;
			} else if (document.equalsIgnoreCase(" MaaSCore (Android)")) {
				SeleniumUtils.click("xpath",
						".//a[text()=' MaaSCore (Android)']");
				return true;
			} else if (document.equalsIgnoreCase(" MaaSAlerts (Android)")) {
				SeleniumUtils.click("xpath",
						".//a[text()=' MaaSAlerts (Android)']");
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public static boolean checkSortingDownOnMessages(WebElement element) {
		int numberOfRows = 0;
		Random randomGenerator = new Random();
		int randomInt = 0;

		String randomGeneratedText = null;
		char randomGeneratedCharacter;
		int asciirandomInt = 0;

		String randomGeneratedPreviousText = null;
		char randomGeneratedPreviousChar;
		int asciirandomPreviousInt = 0;

		String randomGeneratedAfterText = null;
		char randomGeneratedAfterChar;
		int asciirandomAfterInt = 0;
		try {
			numberOfRows = element.findElements(By.tagName("tr")).size();
			if (numberOfRows == 2) {
				// second one
				randomGeneratedText = element
						.findElement(
								By.xpath(".//tr[" + numberOfRows + "]/td[3]/a"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;
				// first one
				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (numberOfRows - 1)
										+ "]/td[3]/a")).getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;
				// condition
				if (asciirandomInt >= asciirandomPreviousInt) {
					return true;
				}
			} else {
				for (int i = 1; i <= 8; i++) {
					randomInt = randomGenerator.nextInt(numberOfRows);
					if (randomInt > 1) {
						break;
					}
				}
				randomGeneratedText = element
						.findElement(
								By.xpath(".//tr[" + randomInt + "]/td[3]/a"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;

				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt - 1)
										+ "]/td[3]/a")).getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;

				randomGeneratedAfterText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt + 1)
										+ "]/td[3]/a")).getText().toLowerCase();
				randomGeneratedAfterChar = randomGeneratedAfterText.charAt(0);
				asciirandomAfterInt = (int) randomGeneratedAfterChar;

				if (asciirandomInt >= asciirandomPreviousInt
						&& asciirandomInt <= asciirandomAfterInt) {
					return true;
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean checkSortingUpOnMessages(WebElement element) {
		int numberOfRows = 0;
		Random randomGenerator = new Random();
		int randomInt = 0;

		String randomGeneratedText = null;
		char randomGeneratedCharacter;
		int asciirandomInt = 0;

		String randomGeneratedPreviousText = null;
		char randomGeneratedPreviousChar;
		int asciirandomPreviousInt = 0;

		String randomGeneratedAfterText = null;
		char randomGeneratedAfterChar;
		int asciirandomAfterInt = 0;
		try {
			numberOfRows = element.findElements(By.tagName("tr")).size();
			if (numberOfRows == 2) {
				// second one
				randomGeneratedText = element
						.findElement(
								By.xpath(".//tr[" + numberOfRows + "]/td[3]/a"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;
				// first one
				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (numberOfRows - 1)
										+ "]/td[3]/a")).getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;
				if (asciirandomInt <= asciirandomPreviousInt) {
					return true;
				}
			} else {
				for (int i = 1; i <= 8; i++) {
					randomInt = randomGenerator.nextInt(numberOfRows);
					if (randomInt > 1) {
						break;
					}
				}

				randomGeneratedText = element
						.findElement(
								By.xpath(".//tr[" + randomInt + "]/td[3]/a"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;

				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt - 1)
										+ "]/td[3]/a")).getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;

				randomGeneratedAfterText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt + 1)
										+ "]/td[3]/a")).getText().toLowerCase();
				randomGeneratedAfterChar = randomGeneratedAfterText.charAt(0);
				asciirandomAfterInt = (int) randomGeneratedAfterChar;

				if (asciirandomInt <= asciirandomPreviousInt
						&& asciirandomInt >= asciirandomAfterInt) {
					return true;
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static int getCountOfApplications(WebElement element) {
		String textOfPagination = null;
		int count = 0;
		try {
			textOfPagination = element.getText().trim();
			count = Integer.parseInt(textOfPagination.substring(18, 20));
			return count;
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	public static int getCurrentCountOfApplications(WebElement element) {
		String textOfPagination = null;
		int count = 0;
		try {
			textOfPagination = element.getText().trim();
			count = Integer.parseInt(textOfPagination.substring(12, 14).trim());
			return count;
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	public static void clickAtNextLinkOfApplications(String locatortype,
			String locatorvalue) {
		WebElement UlElement = null;
		List<WebElement> countOfLi = null;
		int count = 0;
		String NextLink = null;
		try {
			UlElement = SeleniumUtils.findobject(locatortype, locatorvalue);
			countOfLi = UlElement.findElements(By.tagName("li"));
			count = countOfLi.size();
			NextLink = locatorvalue + "/li[" + (count - 1) + "]/a";
			SeleniumUtils.click(locatortype, NextLink);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static boolean checkSchemaInSchemaList(WebElement element,
			String schema) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By
					.xpath(".//div[contains(@id,'itemId')]"));
			for (WebElement row : rows) {
				if (schema.equalsIgnoreCase(row
						.findElement(By.xpath("./div/div/div/div[1]/div"))
						.getText().trim())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	public static boolean clickAtSchemaInSchemaList(WebElement element,
			String schema) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By
					.xpath(".//div[contains(@id,'itemId')]"));
			for (WebElement row : rows) {
				if (schema.equalsIgnoreCase(row
						.findElement(By.xpath("./div/div/div/div[1]/div"))
						.getText().trim())) {
					row.findElement(By.xpath("./div/div/div/div[2]/a")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	public static boolean checkContainerInContainersList(WebElement element,
			String container) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./div[@class='list-item']"));
			for (WebElement row : rows) {
				if (container.equalsIgnoreCase(row
						.findElement(By.xpath("./div/div/div/div[1]/div/a"))
						.getText().trim())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param element
	 * @param container
	 * @return Click on Container link in Structure tab of Content Management
	 */
	public static boolean clickContainerInContainersList(WebElement element,
			String container) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./div[@class='list-item']"));
			for (WebElement row : rows) {
				if (container.equalsIgnoreCase(row
						.findElement(By.xpath("./div/div/div/div[1]/div/a"))
						.getText().trim())) {
					row.findElement(By.xpath("./div/div/div/div[1]/div/a"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickAtEditLinkOfContainersInStructure(
			WebElement element, String container) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./div[@class='list-item']"));
			for (WebElement row : rows) {
				if (container.equalsIgnoreCase(row
						.findElement(By.xpath("./div/div/div/div[1]/div/a"))
						.getText().trim())) {
					row.findElement(By.xpath("./div/div/div/div[2]/a")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static int getNumberOfChildElements(WebElement element) {
		try {
			return element
					.findElements(
							By.xpath("./div[@class='dashboard-form-style schema-block-container']"))
					.size();
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	public static boolean clickContainerInContentTab(WebElement element,
			String container) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./div[@class='list-item']"));
			for (WebElement row : rows) {
				if (container.equalsIgnoreCase(row
						.findElement(By.xpath("./div/div/div/div/div/a"))
						.getText().trim())) {
					row.findElement(By.xpath("./div/div/div/div/div/a"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean checkApplicationNameInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/"
												+ "span[@class='app-name']"))
								.getText().trim())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean checkApplicationActionsInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		String configText = null;
		// String mediationText = null;
		String adPlaceText = null;
		String viewStatsText = null;
		// String getTheSDK = null;
		boolean flag = false;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/span[@class='app-name']"))
								.getText().trim())) {
					configText = row
							.findElement(
									By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[1]/a"))
							.getText().trim();
					/*
					 * mediationText = row .findElement( By.xpath(
					 * "./div[@class='app']/div[@class='app-actions']/ul/li[2]/a"
					 * )) .getText().trim();
					 */
					adPlaceText = row
							.findElement(
									By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[2]/a"))
							.getText().trim();
					viewStatsText = row
							.findElement(
									By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[3]/a"))
							.getText().trim();
					/*
					 * getTheSDK = row .findElement( By.xpath(
					 * "./div[@class='app']/div[@class='app-actions']/ul/li[5]/a"
					 * )) .getText().trim();
					 */
					if (configText.equalsIgnoreCase("Configuration")
							&& adPlaceText.equalsIgnoreCase("Ad Placements")
							&& viewStatsText.equalsIgnoreCase("View Stats")) {
						flag = true;
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean clickAtConfigurationInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/span[@class='app-name']"))
								.getText().trim())) {
					row.findElement(
							By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[1]/a"))
							.click();
					return true;

				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean scrollSlider(WebElement element, String percentOfMale) {
		int malePercentage = Integer.parseInt(percentOfMale);
		try {
			if (malePercentage == 0) {
				element.findElement(By.xpath("./span[1]")).click();
				return true;
			} else {
				int countNumber = (malePercentage / 10) + 1;
				element.findElement(By.xpath("./span[" + countNumber + "]"))
						.click();
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickAtMediationInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/span[@class='app-name']"))
								.getText().trim())) {
					row.findElement(
							By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[2]/a"))
							.click();
					return true;

				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickAtAdPlacementsInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/span[@class='app-name']"))
								.getText().trim())) {
					row.findElement(
							By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[2]/a"))
							.click();
					return true;

				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean sortingDownOnAdPlacementsSites(WebElement element) {
		int numberOfRows = 0;
		Random randomGenerator = new Random();
		int randomInt = 0;

		String randomGeneratedText = null;
		char randomGeneratedCharacter;
		int asciirandomInt = 0;

		String randomGeneratedPreviousText = null;
		char randomGeneratedPreviousChar;
		int asciirandomPreviousInt = 0;

		String randomGeneratedAfterText = null;
		char randomGeneratedAfterChar;
		int asciirandomAfterInt = 0;
		try {
			numberOfRows = element.findElements(By.tagName("tr")).size();
			if (numberOfRows == 2) {
				// second one
				randomGeneratedText = element
						.findElement(
								By.xpath(".//tr[" + numberOfRows + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;
				// first one
				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (numberOfRows - 1)
										+ "]/td[2]")).getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;
				// condition
				if (asciirandomInt >= asciirandomPreviousInt) {
					return true;
				}
			} else {
				for (int i = 1; i <= 8; i++) {
					randomInt = randomGenerator.nextInt(numberOfRows);
					if (randomInt > 1) {
						break;
					}
				}
				randomGeneratedText = element
						.findElement(By.xpath(".//tr[" + randomInt + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;

				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt - 1) + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;

				randomGeneratedAfterText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt + 1) + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedAfterChar = randomGeneratedAfterText.charAt(0);
				asciirandomAfterInt = (int) randomGeneratedAfterChar;

				if (asciirandomInt <= asciirandomPreviousInt
						&& asciirandomInt >= asciirandomAfterInt) {
					return true;
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean sortingUpOnAdPlacementsSites(WebElement element) {
		int numberOfRows = 0;
		Random randomGenerator = new Random();
		int randomInt = 0;

		String randomGeneratedText = null;
		char randomGeneratedCharacter;
		int asciirandomInt = 0;

		String randomGeneratedPreviousText = null;
		char randomGeneratedPreviousChar;
		int asciirandomPreviousInt = 0;

		String randomGeneratedAfterText = null;
		char randomGeneratedAfterChar;
		int asciirandomAfterInt = 0;
		try {
			numberOfRows = element.findElements(By.tagName("tr")).size();
			if (numberOfRows == 2) {
				// second one
				randomGeneratedText = element
						.findElement(
								By.xpath(".//tr[" + numberOfRows + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;
				// first one
				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (numberOfRows - 1)
										+ "]/td[2]")).getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;
				if (asciirandomInt <= asciirandomPreviousInt) {
					return true;
				}
			} else {
				for (int i = 1; i <= 8; i++) {
					randomInt = randomGenerator.nextInt(numberOfRows);
					if (randomInt > 1 && randomInt < 5) {
						break;
					}
				}

				randomGeneratedText = element
						.findElement(By.xpath(".//tr[" + randomInt + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedCharacter = randomGeneratedText.charAt(0);
				asciirandomInt = (int) randomGeneratedCharacter;

				randomGeneratedPreviousText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt - 1) + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedPreviousChar = randomGeneratedPreviousText
						.charAt(0);
				asciirandomPreviousInt = (int) randomGeneratedPreviousChar;

				randomGeneratedAfterText = element
						.findElement(
								By.xpath(".//tr[" + (randomInt + 1) + "]/td[2]"))
						.getText().toLowerCase();
				randomGeneratedAfterChar = randomGeneratedAfterText.charAt(0);
				asciirandomAfterInt = (int) randomGeneratedAfterChar;

				if (asciirandomInt >= asciirandomPreviousInt
						&& asciirandomInt <= asciirandomAfterInt) {
					return true;
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectDeviceTypeFromDeviceList(WebElement element,
			String device) {
		List<WebElement> numberOfLists = null;
		try {
			numberOfLists = element.findElements(By.tagName("li"));
			for (WebElement elementList : numberOfLists) {
				if (device.equalsIgnoreCase(elementList
						.findElement(By.xpath("./label")).getText().trim())) {
					elementList.findElement(By.xpath("./label")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickAtViewStatsInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/span[@class='app-name']"))
								.getText().trim())) {
					row.findElement(
							By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[3]/a"))
							.click();
					return true;

				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickAtGetTheSDKInAdSites(WebElement element,
			String application) {
		List<WebElement> rows = null;
		try {
			rows = element.findElements(By.xpath("./li"));
			for (WebElement row : rows) {
				if (application
						.equalsIgnoreCase(row
								.findElement(
										By.xpath("./div[@class='app']/div[@class='app-name']/span[@class='app-name']"))
								.getText().trim())) {
					row.findElement(
							By.xpath("./div[@class='app']/div[@class='app-actions']/ul/li[5]/a"))
							.click();
					return true;

				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean checkCheckboxFiltersInReports(WebElement element,
			String filterOption) {
		List<WebElement> lists = null;
		try {
			lists = element.findElements(By.tagName("li"));
			for (WebElement list : lists) {
				if (filterOption.equalsIgnoreCase(list
						.findElement(By.xpath("./label")).getText().trim())) {
					list.findElement(By.xpath("./span/input")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectRadioButtonInPlatformSelectionInCampaign(
			WebElement element, String value) {
		List<WebElement> spanItems = null;
		try {
			spanItems = element.findElements(By
					.xpath("./span[@class='widget_input']"));
			for (WebElement span : spanItems) {
				if (value.equalsIgnoreCase(span
						.findElement(By.xpath("./label")).getText().trim())) {
					span.findElement(By.xpath("./span/input")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectOSTypeFromList(WebElement element, String value) {
		List<WebElement> listItems = null;
		try {
			listItems = element.findElements(By.tagName("li"));
			for (WebElement list : listItems) {
				if (value.equalsIgnoreCase(list
						.findElement(By.xpath("./label")).getText().trim())) {
					list.findElement(By.xpath("./label")).click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectCountryFromCountryListInTargeting(
			WebElement element, String value) {
		List<WebElement> listItems = null;
		try {
			listItems = element.findElements(By.tagName("li"));
			for (WebElement list : listItems) {
				if (value.equalsIgnoreCase(list.getText().trim())) {
					list.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectSourceRadioButtonInTargeting(
			WebElement element, String value) {
		List<WebElement> numberOfItems = null;
		try {
			numberOfItems = element.findElements(By.xpath("./li"));
			for (WebElement item : numberOfItems) {
				if (value.equalsIgnoreCase(item.getText().trim())) {
					item.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean selectPhoneAdInCreativeBuilder(WebElement element,
			String value) {
		List<WebElement> numberOfItems = null;
		try {
			numberOfItems = element.findElements(By.xpath("./li"));
			for (WebElement item : numberOfItems) {
				if (value.equalsIgnoreCase(item.getText().trim())) {
					item.click();
					return true;
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return false;
	}

	public static boolean checkCampaignInCampaignsList(WebElement element,
			String value) {
		List<WebElement> numberOfItems = null;
		try {
			numberOfItems = element.findElements(By.xpath("./li"));
			for (WebElement item : numberOfItems) {
				if (value.equalsIgnoreCase(item.findElement(
						By.xpath("./div[1]/div[@class='app-name']/span"))
						.getText())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickCampaignInCampaignsList(WebElement element,
			String value) {
		List<WebElement> numberOfItems = null;
		try {
			numberOfItems = element.findElements(By.xpath("./li"));
			for (WebElement item : numberOfItems) {
				if (value.equalsIgnoreCase(item.findElement(
						By.xpath("./div[1]/div[@class='app-name']/span"))
						.getText())) {
					item.findElement(
							By.xpath("./div[1]/div[@class='app-actions']/ul/li[@class='campaign-edit']/a"))
							.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean checkCheckboxesAsPerInput(String inputtext) {
		WebElement element = null;
		boolean flag = false;
		try {
			element = SeleniumUtils.findobject("xpath",
					"//label[contains(text(),'" + inputtext + "')]");
			/*
			 * element = SeleniumUtils.findobject("xpath",
			 * "//label[contains(text(),'" + inputtext +
			 * "')]/following-sibling::*[1]");
			 */
			element.click();
			flag = true;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean removeGraph(WebElement element) {
		List<WebElement> list = null;
		boolean flag = false;
		try {
			list = element.findElements(By.xpath("/g"));
			for (WebElement eachone : list) {
				eachone.findElement(By.xpath("/path")).click();
			}
			flag = true;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean selectOrganization(WebElement element,
			String organization) {
		boolean flag = false;
		try {
			List<WebElement> list = element.findElements(By.xpath("./li"));
			for (WebElement item : list) {
				if (organization.equalsIgnoreCase(item
						.getAttribute("data-value"))) {
					item.click();
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean clickOnMailInGmailInbox(WebElement element,
			String Subject) {
		boolean flag = false;
		try {
			List<WebElement> tableRows = element.findElements(By.xpath("./tr"));
			for (WebElement row : tableRows) {
				if (Subject.equalsIgnoreCase(row
						.findElement(By.xpath("./td[6]/div/div/div/span[1]/b"))
						.getText().trim())) {
					row.click();
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean checkAppInAddVenue(WebElement element,
			String application) {
		boolean flag = false;
		try {
			List<WebElement> divList = element.findElements(By
					.xpath("./div[@class='control-group']"));
			for (WebElement div : divList) {
				WebElement labelElement = div.findElement(By.tagName("label"));
				String expApp = labelElement.getText();
				if (application.equalsIgnoreCase(expApp)) {
					labelElement.click();
					flag = true;
					return flag;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * @param element
	 *            - Users Table
	 * @param email
	 *            - Email address
	 * @return
	 */
	public static boolean delete_UserAccount_By_Email_In_IE(WebElement element,
			String email) {
		List<WebElement> rows = null;
		String ActualUserText = null;
		try {
			// Get the number of rows
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				// Get the actual text
				ActualUserText = row.findElement(By.xpath("./td[2]")).getText();
				if (email.equalsIgnoreCase(ActualUserText)) {
					// Get the Parent Edit Link
					WebElement EditLink = row
							.findElement(By.xpath("./td[4]/a"));
					// Click on Parent Edit link using java script
					SeleniumUtils.click_Using_JavaScript(EditLink);
					// Get the child - delete link after clicking on Parent Edit
					// Link
					WebElement deleteButton = row.findElement(By
							.xpath("./td[4]/div[@class='tools']/a[1]"));
					// Run the process to click on OK button on Alert window
					Process process = new ProcessBuilder(
							GlobalConstants.Handle_Alert_Window_Using_AutoIt,
							GlobalConstants.IE_Alert_Handle_Dialog_Title)
							.start();
					// Click on Child - Delete window
					SeleniumUtils.click_Using_JavaScript(deleteButton);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while doing Delete on Users in IE browser "
					+ e.toString());
		}
		return false;
	}

	/**
	 * @param element
	 *            - Ad Placement Table
	 * @return
	 * 
	 *         - Retrieve Placement_Id value from AdPlacement table
	 */
	public static String get_PlacementId_From_AdPlacement_Table(
			WebElement element) {
		String Placement_Id = null;
		try {
			List<WebElement> rows = element.findElements(By.tagName("tr"));
			if (rows.size() != 0) {
				Placement_Id = element.findElement(By.xpath("./tr[1]/td[1]"))
						.getText();
				return Placement_Id;
			}
		} catch (Exception e) {
			logger.error("Unexpected Error while retrieving PlacementId from Ad Placement Table"
					+ e.toString());
		}
		return Placement_Id;
	}

	/**
	 * @param element
	 *            - Ad Placement Table
	 * @param Act_PlacementId
	 * @return
	 * 
	 *         - Check the Act_PlacementId is displayed at first row in the Ad
	 *         Placement table
	 */
	public static boolean check_Search_Functionality_Against_SearchBox_In_AdPlacement_Table(
			WebElement element, String Act_PlacementId) {
		List<WebElement> rows = null;
		List<WebElement> cols = null;
		boolean flag = false;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				cols = row.findElements(By.tagName("td"));
				for (WebElement col : cols) {
					if (Act_PlacementId.equalsIgnoreCase(col.getText())) {
						flag = true;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error while checking the first "
					+ "row placement id in AdPlacement table " + e.toString());
		}
		return flag;
	}

	/**
	 * @param element
	 *            - Ad Placement Table
	 * @return
	 * 
	 *         - Retrieve AdName value from AdPlacement table
	 */
	public static String get_AdName_From_AdPlacement_Table(WebElement element) {
		String Ad_Name = null;
		try {
			List<WebElement> rows = element.findElements(By.tagName("tr"));
			if (rows.size() != 0) {
				Ad_Name = element.findElement(By.xpath("./tr[1]/td[2]"))
						.getText().trim();
				return Ad_Name;
			}
		} catch (Exception e) {
			logger.error("Unexpected Error while retrieving AdName from Ad Placement table "
					+ e.toString());
		}
		return Ad_Name;
	}

	/**
	 * @param element
	 *            - Ad Placement Table
	 * @param AdName
	 * @return
	 */
	public static boolean delete_Ad_From_AdPlacement_Table(WebElement element,
			String AdName) {
		List<WebElement> rows = null;
		boolean flag = false;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				if (AdName.equalsIgnoreCase(row
						.findElement(By.xpath("./td[2]")).getText().trim())) {
					row.findElement(By.xpath("./td[7]/a")).click();
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error while deleting the Ad from the AdPlacement table "
					+ e.toString());
		}
		return flag;
	}

	/**
	 * @param element
	 *            - Ad Placement Table
	 * @return
	 * 
	 *         - retrieve number of rows present in Ad Placement table
	 */
	public static int get_Count_Of_Rows_In_AdPlacement_Table(WebElement element) {
		List<WebElement> rows = null;
		int count = 0;
		try {
			rows = element.findElements(By.tagName("tr"));
			count = rows.size();
			return count;
		} catch (Exception e) {
			logger.error("Un expected error while click on edit link in Ad Placement Table "
					+ e);
		}
		return count;
	}

	/**
	 * @param element
	 *            - Ad Placement Table
	 * @return
	 */
	public static boolean click_At_Edit_Link_In_AdPlacement_Table(
			WebElement element) {
		List<WebElement> rows = null;
		boolean flag = false;
		try {
			rows = element.findElements(By.tagName("tr"));
			if (rows.size() != 0) {
				element.findElement(By.xpath("./tr[1]/td[6]/a")).click();
				flag = true;
			} else {
				logger.warn("No Ads are present under Ad Placements..");
			}
			/*
			 * for (WebElement row : rows) { if
			 * (devicename.equalsIgnoreCase(row.findElement(
			 * By.xpath("./td[2]")).getText())) {
			 * row.findElement(By.xpath("./td[6]/a")).click(); return true; } }
			 */
		} catch (Exception e) {
			logger.error("Un expected error while click on edit link in Ad Placement Table "
					+ e);
		}
		return flag;
	}

	/**
	 * @param element
	 *            - Segment list
	 * @param segment
	 *            - Segment Name
	 * @return
	 */
	public static boolean clone_The_Segment_In_Segments(WebElement element,
			String segment) {
		List<WebElement> rows = null;
		try {
			// Get the rows in Segment list
			rows = element
					.findElements(By
							.xpath(".//div[@class='group-row parent-without-children']"));
			for (WebElement row : rows) {
				// Get the Present Segment Name
				String actual_Segment_Name = row.findElement(
						By.xpath("./table/tbody/tr/td[1]")).getText();
				if (segment.equalsIgnoreCase(actual_Segment_Name)) {
					// Get the Parent Edit link in Segment Table
					WebElement parent_Edit_Link = row.findElement(By
							.xpath("./table/tbody/tr/td[3]/a"));
					// Click on Edit Link
					SeleniumUtils.click_Using_JavaScript(parent_Edit_Link);
					// Get the Child Clone Link after clicking on Parent Edit
					// Link
					WebElement child_Clone_Link = row
							.findElement(By
									.xpath("./table/tbody/tr/td[3]/div/a[@class='clone']"));
					// Click on Child Clone link
					child_Clone_Link.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error while Cloning a Segment "
					+ e.toString());
		}
		return false;
	}

	/**
	 * @param element
	 *            - Feed table element
	 * @param feedname
	 *            - feed Name
	 */
	public static boolean click_On_Edit_Link_At_Feeds_Table_In_Feeds_Page(
			WebElement element, String feedname) {
		List<WebElement> no_Of_Rows = null;
		try {
			// Get the number of rows in Feeds table
			no_Of_Rows = element.findElements(By.xpath("./tr"));
			for (WebElement row : no_Of_Rows) {
				// Get the actual Feed anme
				String actual_Feed_Name = row.findElement(By.xpath("./td[1]"))
						.getText();
				if (feedname.equalsIgnoreCase(actual_Feed_Name)) {
					// Get the Edit link
					WebElement parent_Edit_Link = row.findElement(By
							.xpath("./td[3]/a"));
					// Click on Edit link
					SeleniumUtils.click_Using_JavaScript(parent_Edit_Link);
					return true;
				}
			}

		} catch (Exception e) {
			logger.error("Error while doing click on Edit link at Feeds table in Feeds page "
					+ e.toString());
		}
		return false;
	}

}
