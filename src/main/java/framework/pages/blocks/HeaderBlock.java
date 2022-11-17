package framework.pages.blocks;

import framework.pages.BasePage;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HeaderBlock extends BasePage {

    @FindBy(xpath = "//nav[@id='header-search']//input[contains(@class, 'input_presearch')]")
    private WebElement searchBar;

    @FindBy(xpath = "//div[@class='catalog-products view-simple']")
    private WebElement productCatalog;

    @FindBy(xpath = "//nav[@id='header-search']//a[@class='ui-link cart-link']")
    private WebElement basketButton;

    private static final String BASKET_URL = "https://www.dns-shop.ru/cart/";

    public void search (String searchValue) {
       waitUtilElementToBeClickable(searchBar).click();
       searchBar.sendKeys(searchValue + Keys.ENTER);
       try {
           wait.until(ExpectedConditions.visibilityOf(productCatalog));
       } catch (NoSuchElementException ignore) {
           Assert.fail("По результатам поиска ничего не найдено");
       } catch (TimeoutException ignore) {}
       Assert.assertEquals("Фактический результат поиска отличается от ожидаемого",
               searchValue, searchBar.getAttribute("value"));
    }

    public void switchBasket() {
        waitUtilElementToBeClickable(basketButton).click();
        wait.until(ExpectedConditions.urlContains(BASKET_URL));
        Assert.assertTrue("",
                driverManager.getDriver().getCurrentUrl().contains(BASKET_URL));
    }
}
