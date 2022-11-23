package framework.steps;

import framework.managers.PageManager;
import framework.pages.blocks.HeaderBlock;
import io.cucumber.java.ru.И;

public class HeaderBlockStep {

    PageManager pageManager = PageManager.getINSTANCE();

    @И("^Поиск продукта по запросу '(.+)'")
    public void searching(String searchValue) {
        pageManager.getPage(HeaderBlock.class).search(searchValue);
    }

    @И("^Перейти в корзину")
    public void switchBasket() {
        pageManager.getPage(HeaderBlock.class).switchBasket();
    }
}
