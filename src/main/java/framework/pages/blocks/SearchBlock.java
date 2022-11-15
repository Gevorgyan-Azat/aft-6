package framework.pages.blocks;

import framework.pages.BasePage;
import framework.pages.ResultPage;
import framework.pages.StartPage;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchBlock extends BasePage {

    @FindBy(xpath = "//nav[@id='header-search']//input[contains(@class, 'input_presearch')]")
    private WebElement searchBar;

    @FindBy(xpath = "//div[@class='catalog-products view-simple']")
    private WebElement productCatalog;

    public void search (String searchValue) {
       waitUtilElementToBeClickable(searchBar).click();
       searchBar.sendKeys(searchValue + Keys.ENTER);
       try {
           wait.until(ExpectedConditions.visibilityOf(productCatalog));
       } catch (TimeoutException ignore) {}
       Assert.assertEquals("", searchBar.getAttribute("value"), searchValue);
    }
}
