package framework.pages;

import framework.classes.Basket;
import framework.classes.Product;
import framework.managers.DriverManager;
import framework.managers.PageManager;
import framework.pages.blocks.HeaderBlock;
import framework.utils.GsonSerial;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected DriverManager driverManager = DriverManager.getINSTANCE();
    protected WebDriverWait wait = new WebDriverWait(driverManager.getDriver(), 10, 500);
    protected PageManager pageManager = PageManager.getINSTANCE();
    protected JavascriptExecutor js = (JavascriptExecutor) driverManager.getDriver();
    protected Basket basket = Basket.getINSTANCE();
    protected GsonSerial gson = new GsonSerial();

    protected BasePage() {
        PageFactory.initElements(driverManager.getDriver(), this);
    }

    public HeaderBlock getSearching() {
        return pageManager.getPage(HeaderBlock.class);
    }

    protected void scrollElementInCenter(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void scrollAndClick(WebElement element) {
        scrollElementInCenter(element);
        waitUtilElementToBeClickable(element).click();
    }

    protected void clickOnElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected WebElement waitUtilElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitUtilElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForJavascript() {
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + 5000) {
            String prevState = driverManager.getDriver().getPageSource();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignore) { }
            if (prevState.equals(driverManager.getDriver().getPageSource())) {
                return;
            }
        }
    }

    protected void sendKeyArray(WebElement element, String value) {
        String[] strValue = value.split("");
        for (String s: strValue) {
            element.sendKeys(s);
        }
    }

}
