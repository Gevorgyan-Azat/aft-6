package framework.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ResultPage extends BasePage {

    @FindBy(xpath = "//div[@class='apply-filters-float-btn']")
    private WebElement displayBtn;

    @FindBy(xpath = "//div[@class='left-filters__list']/div")
    private List<WebElement> filterBlocksList;

    @FindBy(xpath = "//div[@class='left-filters__list']")
    private WebElement leftFilters;

    @FindBy(xpath = "//div[@class='products-page__top-block']")
    private WebElement topBlock;

    @FindBy(xpath = "//div[@class='left-filters__buttons-main']")
    private WebElement leftFiltersButtonsBlock;

    {
        loadingFilterBlocks();
    }

    public void findBlockAndSetValue(String filterBlockName, String fieldType, String blockFieldName, String expectedValue) {
        switch (fieldType) {
            case "checkBox":
                changeCheckBoxValue(Objects.requireNonNull(selectFilterBlock(filterBlockName)), blockFieldName, Boolean.parseBoolean(expectedValue));
                checkPickedFilterValueForCheckbox(blockFieldName, expectedValue);
                break;
            case "radioButton":

                break;
            case "поля":
                changeFieldValue(Objects.requireNonNull(selectFilterBlock(filterBlockName)), blockFieldName, expectedValue);
                checkPickedFilterValueForField(blockFieldName, expectedValue);
                break;
        }
    }

    private WebElement selectFilterBlock(String filterBlockName) {
        if (filterBlockName.equals("*")) {
            return leftFilters;
        }
        for (WebElement filterBlock : filterBlocksList) {
            if (filterBlock.getAttribute("textContent").contains(filterBlockName)) {
                if (!filterBlock.getAttribute("class").equals("left-filters-toggle") &&
                        !filterBlock.findElement(By.xpath("./a")).getAttribute("class").contains("link_in")) {
                    scrollElementInCenter(filterBlock.findElement(By.xpath("./a")));
                    filterBlock.findElement(By.xpath("./a")).click();
                    wait.until(ExpectedConditions.attributeContains(filterBlock.findElement(By.xpath("./a")), "class", "link_in"));
                    Assert.assertTrue("Не удалось раскрыть блок фильтра под названием '" + filterBlockName + "'",
                            filterBlock.findElement(By.xpath("./a")).getAttribute("class").contains("link_in"));
                }
                return filterBlock;
            }
        }
        Assert.fail("Не найден блок фильтра под названием '" + filterBlockName + "'");
        return null;
    }


    private void changeCheckBoxValue(WebElement element, String checkBoxName, boolean expectedStatus) {
        List<WebElement> checkBoxList = element.findElements(By.xpath(".//label[not(@style='display: none;')]"));
        for (WebElement checkBox : checkBoxList) {
            if (checkBox.getAttribute("textContent").contains(checkBoxName)) {
                try {
                    checkBox.findElement(By.cssSelector(".ui-checkbox__input:checked"));
                    if (!expectedStatus) {
                        scrollAndClickCheckBox(checkBox);
                        return;
                    }
                } catch (NoSuchElementException ignore) {
                    if (expectedStatus) {
                        scrollAndClickCheckBox(checkBox);
                        return;
                    }
                }
                return;
            }
        }
    }

    private void checkPickedFilterValueForCheckbox(String checkBoxName, String expectedValue) {
        if (Boolean.parseBoolean(expectedValue)) {
            try {
                driverManager.getDriver().findElement(By.xpath(String.format("//div[@class='top-filters']//div[@class='picked-filter' and contains(., '%s')]", checkBoxName)));
            } catch (NoSuchElementException ignore) {
                Assert.fail("Результат поиска не отфильтрован по checkbox '" + checkBoxName + "'");
            }
        }

    }

    private void scrollAndClickCheckBox(WebElement element) {
        scrollElementInCenter(element);
        waitUtilElementToBeClickable(element).click();
        waitUtilElementToBeVisible(displayBtn);
        waitUtilElementToBeClickable(displayBtn).click();
    }

    private void changeFieldValue(WebElement element, String fieldName, String value) {
        List<WebElement> fieldsList = element.findElements(By.xpath(".//input[@placeholder]"));
        for (WebElement field : fieldsList) {
            if (field.getAttribute("placeholder").contains(fieldName)) {
                scrollElementInCenter(field);
                waitUtilElementToBeClickable(field).click();
                field.clear();
                field.sendKeys(value);
                waitUtilElementToBeVisible(displayBtn);
                waitUtilElementToBeClickable(displayBtn).click();
                return;
            }
        }
        Assert.fail("Не удалось найти поле '" + fieldName + "'");
    }

    private void checkPickedFilterValueForField(String fieldName, String expectedValue) {
        try {
            driverManager.getDriver().findElement(By.xpath(String.format("//div[@class='top-filters']//div[@class='picked-filter' and contains(., '%s') and contains(., '%s')]", fieldName, expectedValue)));
        } catch (NoSuchElementException ignore) {
            Assert.fail("Результат поиска не отфильтрован по полю '" + fieldName + "'");
        }
    }

    private void loadingFilterBlocks() {
        waitForJavascript();
        for (int i = 0; i < filterBlocksList.size() - 1; i+=2) {
            List<WebElement> filterBlocks = new ArrayList<>(filterBlocksList);
            scrollElementInCenter(filterBlocks.get(i));
        }
        scrollElementInCenter(leftFiltersButtonsBlock);
        scrollElementInCenter(topBlock);
    }
}
