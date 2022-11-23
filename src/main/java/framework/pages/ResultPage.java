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

    @FindBy(xpath = "//button[@class='pagination-widget__show-more-btn']")
    private WebElement showMoreButton;

    @FindBy(xpath = "//div[@data-id='product']")
    private List<WebElement> productBlockList;

    @FindBy(xpath = "//li[contains(@class, 'pagination-widget__page_active')]")
    private WebElement activePage;

    @FindBy(xpath = "//div[@class='cart-modal']")
    private WebElement basketBlock;

    @FindBy(xpath = "//nav[@id='header-search']//span[@class='cart-link__badge']")
    private WebElement amountProductsInBasket;


    private static int counter = 1;

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

    public void addProductsInBasket(int expectedAmountProducts) {
        waitForJavascript();
        for (int i = counter; i < productBlockList.size(); i += 2) {
            if (basket.getProducts().size() < expectedAmountProducts) {
                try {
                    clickBuyBtn(productBlockList.get(i));
                    basket.setProducts(getProductName(productBlockList.get(i)), getProductPrice(productBlockList.get(i)), 1);
                    gson.writeJson(basket.getProducts().get(basket.getProducts().size()-1));
                } catch (NoSuchElementException ignore) {
                }
                counter += 2;
                Assert.assertEquals("Неверное количество товара в корзине",
                        basket.getProducts().size(), Integer.parseInt(amountProductsInBasket.getText()));
            } else if (basket.getProducts().size() == expectedAmountProducts) {
                counter = 1;
                return;
            }
        }
        if (basket.getProducts().size() < expectedAmountProducts) {
            int page = Integer.parseInt(activePage.getAttribute("textContent"));
            try {
                scrollAndClick(showMoreButton);
                wait.until(ExpectedConditions.attributeToBe(activePage, "textContent", String.valueOf(++page)));
                addProductsInBasket(expectedAmountProducts);
            } catch (NoSuchElementException ignore) {
                Assert.fail("Не удалось добавить указанное количество товаров в корзину.\n"
                        + "Страниц пройдено: " + --page + "\n"
                        + "Товаров добавлено: " + basket.getProducts().size());
            }
        }
    }

    private String getProductName(WebElement element) {
        return element.findElement(By.xpath(".//a[contains(@class, 'catalog-product__name')]/span")).getText().split(" \\[")[0];
    }

    private int getProductPrice(WebElement element) {
        return Integer.parseInt(element.findElement(By.xpath(".//div[@class='product-buy__price product-buy__price_active']"))
                .getAttribute("textContent").split("₽")[0].replaceAll("\\D", ""));
    }

    private void clickBuyBtn(WebElement element) {
        WebElement buyBtn = element.findElement(By.xpath(".//button[contains(@class, 'buy-btn')]"));
        scrollAndClick(buyBtn);
        wait.until(ExpectedConditions.textToBePresentInElement(buyBtn, "В корзине"));
        wait.until(ExpectedConditions.attributeToBe(basketBlock, "display", "none"));
    }

    private WebElement selectFilterBlock(String filterBlockName) {
        if (filterBlockName.equals("*")) {
            return leftFilters;
        }
        for (WebElement filterBlock : filterBlocksList) {
            if (filterBlock.getAttribute("textContent").contains(filterBlockName)) {
                if (!filterBlock.getAttribute("class").equals("left-filters-toggle") &&
                        !filterBlock.findElement(By.xpath("./a")).getAttribute("class").contains("link_in")) {
                    scrollAndClick(filterBlock.findElement(By.xpath("./a")));
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
                        scrollAndClick(checkBox);
                        applyFilterParam();
                        return;
                    }
                } catch (NoSuchElementException ignore) {
                    if (expectedStatus) {
                        scrollAndClick(checkBox);
                        applyFilterParam();
                        return;
                    }
                }
            }
        }
        Assert.fail("Не удалось найти поле '" + checkBoxName + "'");
    }

    private void changeFieldValue(WebElement element, String fieldName, String value) {
        List<WebElement> fieldsList = element.findElements(By.xpath(".//input[@placeholder]"));
        for (WebElement field : fieldsList) {
            if (field.getAttribute("placeholder").contains(fieldName)) {
                scrollAndClick(field);
                field.clear();
                field.sendKeys(value);
                applyFilterParam();
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

    private void checkPickedFilterValueForCheckbox(String checkBoxName, String expectedValue) {
        if (Boolean.parseBoolean(expectedValue)) {
            try {
                driverManager.getDriver().findElement(By.xpath(String.format("//div[@class='top-filters']//div[@class='picked-filter' and contains(., '%s')]", checkBoxName)));
            } catch (NoSuchElementException ignore) {
                Assert.fail("Результат поиска не отфильтрован по checkbox '" + checkBoxName + "'");
            }
        }

    }

    private void applyFilterParam() {
        waitUtilElementToBeVisible(displayBtn);
        waitUtilElementToBeClickable(displayBtn).click();
    }

    private void loadingFilterBlocks() {
        waitForJavascript();
        for (int i = 0; i < filterBlocksList.size() - 1; i++) {
            List<WebElement> filterBlocks = new ArrayList<>(filterBlocksList);
            scrollElementInCenter(filterBlocks.get(i));
        }
        scrollElementInCenter(leftFiltersButtonsBlock);
        scrollElementInCenter(topBlock);
    }
}
