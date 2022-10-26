package framework.steps;

import framework.managers.PageManager;
import framework.pages.StartPage;
import framework.pages.blocks.SearchBlock;
import io.cucumber.java.ru.И;

public class StartPageStep {

    PageManager pageManager = PageManager.getINSTANCE();

    @И("^Поиск продукта по запросу '(.+)'")
    public void searching(String searchValue){
        pageManager.getPage(SearchBlock.class).search(searchValue);
    }
}
