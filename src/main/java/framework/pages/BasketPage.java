package framework.pages;

import framework.classes.Product;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class BasketPage extends BasePage {

    @FindBy(xpath = "//div[@class='cart-items__content-container']")
    private List<WebElement> productBlockList;

    @FindBy(xpath = "//div[@class='summary-header__total-items']")
    private WebElement totalProducts;

    @FindBy(xpath = "//div[@class='empty-message__title-empty-cart']")
    private WebElement basketEmpty;

    public void checkBasket() {
        waitForJavascript();
        if (basket.getProducts().size() == 0) {
            try {
                basketEmpty.isDisplayed();
                return;
            } catch (NoSuchElementException ignore) {
                Assert.fail("Корзина не пуста");
            }
        } else if (getTotalProducts() == basket.getProducts().size()) {
            int count = 0;
            for (WebElement pb : productBlockList) {
                Product product = new Product(getName(pb), getPrice(pb), getCount(pb));
                scrollElementInCenter(pb);
                for (int i = 0; i < basket.getProducts().size(); i++) {
                    if (basket.getProducts().get(i).equals(product)) {
                        count++;
                        break;
                    }
                }
            }
            if (productBlockList.size() == count) {
                return;
            }
        }
        Assert.fail("В корзине отсутствуют ранее добавленные товары");
    }

    public void deleteProduct(String productName) {
        if (productName.equals("Все")) {
            List<Product> products = new ArrayList<>(basket.getProducts());
            for (Product pr: products) {
                deleteProduct(pr.getName());
            }
            checkBasket();
        } else {
            for (WebElement product : productBlockList) {
                int prSize = productBlockList.size();
                String prName = getName(product);
                if (prName.equals(productName)) {
                    scrollAndClick(getDeleteBtn(product));
                    wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//div[@class='cart-items__content-container']"), prSize));
                    if (productBlockList.size() < prSize) {
                        for (Product pr: basket.getProducts()) {
                            if (pr.getName().equals(prName)) {
                                basket.getProducts().remove(pr);
                                return;
                            }
                        }
                    }
                    Assert.assertEquals("Продукт " + productName + " не удален из корзины",
                            productBlockList.size(), basket.getProducts().size());
                }
            }
        }
    }

    private String getName(WebElement element) {
        return element.findElement(By.xpath(".//div[@class='cart-items__product-name']/a")).getText();
    }

    private int getPrice(WebElement element) {
        return Integer.parseInt(element.findElement(By.xpath(".//span[@class='price__current']"))
                .getAttribute("textContent").split("₽")[0].replaceAll("\\D", ""));
    }

    private int getCount(WebElement element) {
        return Integer.parseInt(element.findElement(By.xpath(".//input[@class='count-buttons__input']"))
                .getAttribute("value"));
    }

    private int getTotalProducts() {
        return Integer.parseInt(totalProducts.getAttribute("textContent").replaceAll("\\D", ""));
    }

    private WebElement getDeleteBtn(WebElement element) {
        return element.findElement(By.xpath(".//button[@class='menu-control-button remove-button']"));
    }
}
