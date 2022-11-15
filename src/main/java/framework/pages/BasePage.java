package framework.pages;

import framework.managers.DriverManager;
import framework.managers.PageManager;
import framework.pages.blocks.SearchBlock;
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

    protected BasePage() {
        PageFactory.initElements(driverManager.getDriver(), this);
    }

    public SearchBlock getSearching() {
        return pageManager.getPage(SearchBlock.class);
    }

    protected void scrollElementInCenter(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
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
