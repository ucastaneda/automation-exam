package com.veelo.exam;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class StagingPageTest {
	
	// Location of gecko driver in the file system
	public static final String GECKO_DRIVER_WIN64 = "src\\test\\resources\\geckodriver.exe";
	public static final String GECKO_DRIVER_MAC = "src/test/resources/geckodriver";

	// RGB code for green color of main sections
	public static final String GREEN_COLOR = "rgb(0, 167, 80)";

	WebDriver driver;
	String url = "https://staging.veeloapp.com";
	StagingPageFactory pageFactory;
	
	/**
	 * Common setup for web driver initial configuration.
	 * @param platform
	 * @throws Exception
	 */
	@BeforeClass
	@Parameters({"platform"})
	public void setup(String platform) throws Exception {
		
		// Choosing the correct web driver according to the platform otherwise it will throw an exception
		if (platform.equalsIgnoreCase("mac"))
			System.setProperty("webdriver.gecko.driver", GECKO_DRIVER_MAC);
		else if (platform.equalsIgnoreCase("windows"))
			System.setProperty("webdriver.gecko.driver", GECKO_DRIVER_WIN64);
		else
			throw new Exception(String.format("Invalid platform: %s. Please use either 'windows' or 'mac' "
					+ "in testng-parameters.xml", platform));
		
		// General web driver setup
		driver = new FirefoxDriver();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		pageFactory = new StagingPageFactory(driver);
	}
	
	/**
	 * Test case for basic verifications over Veelo stage site.
	 * @param username
	 * @param password
	 */
	@Test
	@Parameters({"username", "password"})
	public void examTest(String username, String password) {
		
		String user = username;
		String pass = password;
		
		// Time for explicit waits.
		WebDriverWait wait = new WebDriverWait(driver, 15);
		// Object for mouse actions.
		Actions action = new Actions(driver);
		
		// Getting initial favorites count
		int favoritesCount;
		
		// STEP 1: Login.
		login(user, pass);
		
		// Explicit wait needed to start sending actions over the elements
		WebElement allContent = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), \"All Content\")]")));
		
		// - ASSERTIONS
		// a) The discovery page should load in the "All Content" view (verifying selection with the text color
		// green as selected and gray as unselected)
		Assert.assertEquals(allContent.getCssValue("color"), GREEN_COLOR);
		
		// b) There should be 34 total pieces of content available.
		// UPDATE: 35
		Assert.assertEquals(getContentCount(), 35);
		
		// STEP 2. Close and reopen the filters sidebar.
		pageFactory.btnFiltersSideBar.click();
		pageFactory.btnFiltersSideBar.click();
		
		// STEP 3: In the Filters sidebar, select the "Finance" filter. 
		pageFactory.chkboxFinanceFilter.click();
		
		// - ASSERTIONS
		// a) The content should filter down to the 6 pieces of content.
		Assert.assertEquals(getContentCount(), 6);
		
		// b) The “Collection 1” item should be marked as Late and contain 3 items.
		Assert.assertTrue(pageFactory.firstCardTitle.getText().contains("Collection 1") && 
				pageFactory.firstCardLate.isDisplayed());
		Assert.assertEquals(pageFactory.firstCardItemCount.getText(), "3");
		
		// STEP 4: Click clear button
		pageFactory.btnClear.click();
		
		// - ASSERTIONS
		// a) The selected filters should be cleared. (Finance)
		Assert.assertFalse(pageFactory.chkboxFinanceFilter.isSelected());
		// b) There should be 34 total pieces of content available. 
		// UPDATE: 35
		Assert.assertEquals(getContentCount(), 35);
		// c) The first piece of content should be the "Collection 10" collection which should contain 8 items. 
		// UPDATE: First piece "Add Me! Assign Me! Collection - Til..." with 2 items
		Assert.assertTrue(pageFactory.firstCardTitle.getText().contains("Add Me! Assign Me! Collection - Til..."));
		Assert.assertEquals(pageFactory.firstCardItemCount.getText(), "2");
		
		// STEP 5: Type in the search box: "direct"
		pageFactory.txtSearch.sendKeys("direct");

		// - ASSERTIONS
		// a) A dropdown should appear with two suggestions for "Swagelock Interactive PDF.pdf" and "PDF File 1 Shareable”. 
		// UPDATE: Second suggestion changed
		Assert.assertTrue(pageFactory.drpdwnFirstSuggestion.getAttribute("title").equals("Swagelok Interactive PDF.pdf") &&
				pageFactory.drpdwnSecondSuggestion.getAttribute("title").equals("Collection PDF File 3 - Shareable"));
		
		// STEP 6: Press the "enter" key to search for content
		pageFactory.txtSearch.sendKeys(Keys.ENTER);
		
		// ASSERTIONS
		// a) The view label should change to "Search Results".
		Assert.assertEquals(pageFactory.viewLabel.getText(), "Search Results");
		// b) The Swagelok and PDF File1 content tiles should be the first 2 items displayed.
		Assert.assertEquals(pageFactory.firstCardTitle.getText(), "Swagelok Interactive PDF.pdf");
		Assert.assertEquals(pageFactory.secondCardTitle.getText(), "Collection PDF File 3 - Shareable");
		
		// STEP 7: Hover mouse over the "Swagelok"  item and then click the heart button to favorite the piece of content.
		action.moveToElement(pageFactory.firstCardTitle).perform();
		favoritesCount = getFavoritesCount();	// Initializing favoritesCount by first time
		pageFactory.firstCardFavoriteBtn.click();
		
		// ASSERTIONS
		// a) The heart should turn orange.
		Assert.assertTrue(pageFactory.firstCardFavoriteBtnSelected.isDisplayed());
		// b) The Favorites tab’s indicator number should increment.
		Assert.assertEquals(getFavoritesCount(), favoritesCount + 1);
		favoritesCount++;

		// STEP 8: Navigate to the Favorites view.
		pageFactory.btnFavoritesSection.click();
		
		// ASSERTIONS
		// a) The only piece of content should be the "Swagelok" item.
		Assert.assertEquals(pageFactory.firstCardTitle.getText(), "Swagelok Interactive PDF.pdf");
		Assert.assertEquals(getContentCount(), 1);
		
		// STEP 9: Hover mouse over the "Swagelok" item and then click the heart button to unfavorite the piece of content.
		action.moveToElement(pageFactory.firstCardTitle);
		pageFactory.firstCardFavoriteBtn.click();

		// ASSERTIONS
		// b) The Favorites tab’s indicator number should decrement.
		Assert.assertEquals(getFavoritesCount(), favoritesCount - 1);
		favoritesCount--;
		// a) The heart should turn black and the content should disappear.
		
		// Making a search to have the correct content displayed and be able to verify heart icon.
		pageFactory.txtSearch.sendKeys("Swagelok Interactive PDF.pdf");
		pageFactory.txtSearch.sendKeys(Keys.ENTER);
		action.moveToElement(pageFactory.firstCardTitle).perform();
		Assert.assertTrue(pageFactory.firstCardFavoriteBtnUnselected.isDisplayed());
		
		// c) No content should be visible and the following message should be displayed: "You don’t have any favorites at this time."
		pageFactory.btnFavoritesSection.click();
		Assert.assertEquals(pageFactory.lblNoFavorites.getText(), "You don’t have any favorites at this time.");
		
		// STEP 10: Logout by clicking the profile button flyout and clicking on "Sign Out".
		pageFactory.btnProfile.click();
		pageFactory.btnLogout.click();
		
		// ASSERTIONS
		// a) The user should be navigated to the login page.
		Assert.assertEquals(driver.getCurrentUrl(), "https://staging.veeloapp.com/users/signout/");
	}
	
	@AfterClass
	public void teardown() {
		driver.quit();
	}
	
	/** 
	 * SPECIFIC ACTIONS/METHODS 
	 **/
	
	/**
	 * Login to the web page with the test credentials.
	 */
	public void login(String username, String password) {
		pageFactory.txtUsername.sendKeys(username);
		pageFactory.txtPassword.sendKeys(password);
		pageFactory.btnLogin.click();
	}
	
	/**
	 * Returns the total number of pieces of content.
	 */
	public int getContentCount() {
		// Regex to extract only digits from count text
		return Integer.valueOf(pageFactory.lblContentCount.getText().replaceAll("\\D+", ""));
	}
	
	/**
	 * Returns the total number of content saved as favorite.
	 */
	public int getFavoritesCount() {
		// Regex to extract only digits from count text
		return Integer.valueOf(pageFactory.lblFavoritesCount.getText().replaceAll("\\D+", ""));
	}
}
