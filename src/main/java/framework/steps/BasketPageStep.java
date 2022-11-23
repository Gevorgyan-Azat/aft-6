package framework.steps;

import framework.managers.PageManager;
import framework.pages.BasketPage;
import io.cucumber.java.ru.И;

public class BasketPageStep {

    PageManager pageManager = PageManager.getINSTANCE();

    @И("^Проверка корзины$")
    public void checkBasket() {
        pageManager.getPage(BasketPage.class).checkBasket();
    }

    @И("^Удалить товар '(.+)'$")
    public void deleteProduct(String productName) {
        pageManager.getPage(BasketPage.class).deleteProduct(productName);
    }
}
