package com.veelo.exam;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class StagingPageFactory {

	WebDriver driver;
	
	/**
	 * Login Page elements
	 */
	@FindBy(how=How.ID, using="id_identification")
	WebElement txtUsername;
	
	@FindBy(how=How.ID, using="id_password")
	WebElement txtPassword;
	
	@FindBy(how=How.XPATH, using="/html/body/div/div[1]/form[2]/div/button")
	WebElement btnLogin;
	
	/**
	 * Main Page elements
	 */
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div[1]/header/nav/div[1]/content-tabs/ul/li[1]/a/span")
	WebElement btnAllContentSection;
	
	@FindBy(how=How.XPATH, using="//div[@id='webux-content-navigator-container']/div/div/div/content-tabs/ul/li[2]/a/span[1]")
	WebElement btnFavoritesSection;
	
	// Label containing the total number of favorites
	@FindBy(how=How.XPATH, using="//div[@id='webux-content-navigator-container']/div/div/div/content-tabs/ul/li[2]/a/span[2]")
	WebElement lblFavoritesCount;
	
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div/div/filters-drawer/aside/div[1]/md-icon")
	WebElement btnFiltersSideBar;
		
	// Label containing the total number of content items
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div[1]/div/div/div[2]/div/div[2]/div/div/content-portal/pagination/div/span[2]/span")
	WebElement lblContentCount;
	
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div/div/filters-drawer/aside/div[2]/content-filter/div[1]/div[2]/label/span/span")
	WebElement chkboxFinanceFilter;
	
	@FindBy(how=How.XPATH, using="//div[@style='order: 1;']//h1[@class='Card2-contentArea-title']")
	WebElement firstCardTitle;
	
	@FindBy(how=How.XPATH, using="//div[@style='order: 1;']//span[@class='CardFlag-label']")
	WebElement firstCardLate;
	
	@FindBy(how=How.XPATH, using="//div[@style='order: 1;']//span[@class='Card2-itemCount-number']")
	WebElement firstCardItemCount;
	
	@FindBy(how=How.XPATH, using="//div[@style='order: 1;']//button[@title='Favorite']")
	WebElement firstCardFavoriteBtn;
	
	// Border icon for no favorite content
	@FindBy(how=How.XPATH, using="//div[@style='order: 1;']//md-icon[contains(@md-svg-src, 'icon-favoriteBorder.svg')]")
	WebElement firstCardFavoriteBtnUnselected;
	
	// Orange icon for favorite content
	@FindBy(how=How.XPATH, using="//div[@style='order: 1;']//md-icon[contains(@md-svg-src, 'icon-favoriteActive.svg')]")
	WebElement firstCardFavoriteBtnSelected;
	
	@FindBy(how=How.XPATH, using="//div[@style='order: 2;']//h1[@class='Card2-contentArea-title']")
	WebElement secondCardTitle;
	
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div/div/filters-drawer/aside/div[2]/content-filter/div[1]/div[1]/button")
	WebElement btnClear;
	
	@FindBy(how=How.XPATH, using="//input[@placeholder='Search...']")
	WebElement txtSearch;
	
	@FindBy(how=How.XPATH, using="//div[@class='Searchbox-suggestions']//ul/li[1]")
	WebElement drpdwnFirstSuggestion;
	
	@FindBy(how=How.XPATH, using="//div[@class='Searchbox-suggestions']//ul/li[2]")
	WebElement drpdwnSecondSuggestion;
	
	// Label with the title changing dynamically in each section
	@FindBy(how=How.XPATH, using="//div[@id='webux-content-navigator-container']//h1[@class='webux_content_title text-s2']")
	WebElement viewLabel;
	
	// Label stating there are no favorite content
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div[1]/div/div/div[2]/div/div[2]/div/div/content-portal/div[2]/h2")
	WebElement lblNoFavorites;
	
	@FindBy(how=How.XPATH, using="/html/body/div[2]/div/div/div[1]/header/nav/div[2]/user-dropdown/div/div[1]")
	WebElement btnProfile;
	
	@FindBy(how=How.XPATH, using="//a[@href='/users/signout/']")
	WebElement btnLogout;
	
	
	/**
	 * Constructor with web driver
	 */
	StagingPageFactory(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
