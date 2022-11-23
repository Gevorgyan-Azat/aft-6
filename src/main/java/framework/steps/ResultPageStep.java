package framework.steps;

import framework.managers.PageManager;
import framework.pages.ResultPage;
import io.cucumber.java.ru.И;

public class ResultPageStep {

    PageManager pageManager = PageManager.getINSTANCE();

    @И("^В блоке (.+) изменить значение (checkBox|radioButton|поля) '(.+)' на (.+)$")
    public void selectBlock(String filterBlockName, String fieldType, String blockFieldName, String expectedValue) {
        pageManager.getPage(ResultPage.class).findBlockAndSetValue(filterBlockName, fieldType, blockFieldName, expectedValue);
    }

    @И("^Добавить '(\\d+)' четных товаров в корзину$")
    public void addProductInBasket(int amountProducts) {
        pageManager.getPage(ResultPage.class).addProductsInBasket(amountProducts);
    }
}
