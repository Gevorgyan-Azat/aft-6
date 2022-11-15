package framework.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ResultPage extends BasePage {

    @FindBy(xpath = "//div[@class='left-filters']//label[@class='ui-checkbox']")
    private List<WebElement> checkBoxList;

    @FindBy(xpath = "//div[@class='apply-filters-float-btn']")
    private WebElement displayBtn;

    @FindBy(xpath = "//span[@class='ui-collapse__link-text']")
    private List<WebElement> filterBlockList;

    @FindBy(xpath = "//div[@class='left-filters__list']/div")
    private List<WebElement> filterBlocksForScrollList;

    @FindBy(css = ".ui-checkbox__input:checked")
    private List<WebElement> checkBoxChecked;

    {
        loadingFilterBlocks();
    }

    public ResultPage() {
    }

    public void selectCheckBox (String checkBoxName) {
        for (WebElement checkBox: checkBoxList) {
            if (checkBox.getAttribute("textContent").contains(checkBoxName)) {
                waitForJavascript();
                scrollElementInCenter(checkBox);
                waitUtilElementToBeClickable(checkBox).click();
                waitUtilElementToBeVisible(displayBtn);
                waitUtilElementToBeClickable(displayBtn).click();
            }
        }
    }

    public void findBlockAndSetValue(String filterBlockName, String fieldType, String blockFieldName, String expectedValue){
        waitForJavascript();
        switch (fieldType) {
            case "checkBox":
                changeCheckBoxValue(Objects.requireNonNull(selectFilterBlock(filterBlockName)), blockFieldName, Boolean.parseBoolean(expectedValue));
                checkPickedFilterValueForCheckbox(blockFieldName);
                break;
            case "radioButton":

                break;
            case "поля":
                changeFieldValue(Objects.requireNonNull(selectFilterBlock(filterBlockName)), blockFieldName, expectedValue);
                break;
        }
    }

    private WebElement selectFilterBlock(String filterBlockName){
        for (WebElement filterBlock: filterBlockList) {
            if(filterBlock.getAttribute("textContent").contains(filterBlockName)){
                if(!filterBlock.findElement(By.xpath("./..")).getAttribute("class").contains("link_in")) {
                    scrollElementInCenter(filterBlock.findElement(By.xpath("./..")));
                    filterBlock.findElement(By.xpath("./..")).click();
                    wait.until(ExpectedConditions.attributeContains(filterBlock.findElement(By.xpath("./..")), "class", "link_in"));
                    Assert.assertTrue("Не удалось раскрыть блок фильтра под названием '" + filterBlockName + "'",
                            filterBlock.findElement(By.xpath("./..")).getAttribute("class").contains("link_in"));
                }
                return filterBlock;
            }
        }
        Assert.fail("Не найден блок фильтра под названием '" + filterBlockName + "'");
        return null;
    }


    private void changeCheckBoxValue(WebElement element, String checkBoxName, boolean expectedStatus) {
        List<WebElement> filterBlockList = element.findElements(By.xpath("./../..//label"));
        for(WebElement filterBlock: filterBlockList) {
            if (filterBlock.getAttribute("textContent").contains(checkBoxName)) {
                List<WebElement> checkBox = filterBlock.findElements(By.cssSelector(".ui-checkbox__input:checked"));
                if(checkBox.size()<1 && expectedStatus) {
                    scrollAndClickCheckBox(filterBlock);
                    return;
                } else if (checkBox.size()>0 && !expectedStatus){
                    scrollAndClickCheckBox(filterBlock);
                    return;
                }
                return;
            }
        }
    }

    private void checkPickedFilterValueForCheckbox(String checkBoxName){
        WebElement pickedFilter = driverManager.getDriver().findElement(By.xpath(String.format("//div[@class='top-filters']//div[@class='picked-filter' and contains(., '%s')]", checkBoxName)));
        wait.until(ExpectedConditions.attributeContains(pickedFilter, "textContent", checkBoxName));
        Assert.assertTrue("",
                pickedFilter.getAttribute("textContent").contains(checkBoxName));
    }

    private void scrollAndClickCheckBox(WebElement element) {
        scrollElementInCenter(element);
        waitUtilElementToBeClickable(element).click();
        waitUtilElementToBeVisible(displayBtn);
        waitUtilElementToBeClickable(displayBtn).click();
    }

    private void changeFieldValue(WebElement element, String fieldName, String value) {
        List<WebElement> fieldsList = element.findElements(By.xpath("./../..//input[@placeholder]"));
        for(WebElement field: fieldsList) {
            if (field.getAttribute("placeholder").contains(fieldName)) {
                scrollElementInCenter(field);
                waitUtilElementToBeClickable(field).click();
                field.clear();
                field.sendKeys(value);
                waitUtilElementToBeVisible(displayBtn);
                waitUtilElementToBeClickable(displayBtn).click();
                break;
            }
        }
        WebElement pickedFilter = driverManager.getDriver().findElement(By.xpath(String.format("//div[@class='top-filters']//div[@class='picked-filter' and contains(., '%s')]", value)));
        wait.until(ExpectedConditions.attributeContains(pickedFilter, "textContent", value));
        Assert.assertTrue("",
                pickedFilter.getAttribute("textContent").contains(value));
    }

    private void loadingFilterBlocks() {
        for (int i = 0; i < filterBlocksForScrollList.size()-1; i+=2){
            List<WebElement> filterBlocks = new ArrayList<>(driverManager.getDriver().findElements(By.xpath("//div[@class='left-filters__list']/div")));
            scrollElementInCenter(filterBlocks.get(i));
        }
        scrollElementInCenter(driverManager.getDriver().findElement(By.xpath("//div[@class='products-page__top-block']")));
    }
}
