package framework.pages.blocks;

import framework.pages.BasePage;
import framework.pages.StartPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchBlock extends BasePage {

    @FindBy(xpath = "//input[@placeholder]")
    private WebElement searchBar;

    public <T extends BasePage> T search (String searchValue) {
//       waitUtilElementToBeClickable(searchBar).click();
//       sendKeyArray(searchBar, searchValue);
//       searchBar.sendKeys(Keys.ENTER);
       return (T) pageManager.getPage(StartPage.class);
    }
}
