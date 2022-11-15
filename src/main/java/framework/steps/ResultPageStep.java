package framework.steps;

import framework.managers.PageManager;
import framework.pages.ResultPage;
import io.cucumber.java.ru.И;

public class ResultPageStep {

    PageManager pageManager = PageManager.getINSTANCE();

//    @И("^Изменить статус чекбокса (Рейтинг|В наличии) на (true|false)$")
//    public void selectCheckBox(String checkBoxName, boolean expectedStatus){
//        pageManager.getPage(ResultPage.class).selectCheckBox(checkBoxName);
//    }

    @И("^В блоке (.+) изменить значение (checkBox|radioButton|поля) '(.+)' на (.+)$")
    public void selectBlock(String filterBlockName, String fieldType, String blockFieldName, String expectedValue){
        pageManager.getPage(ResultPage.class).findBlockAndSetValue(filterBlockName, fieldType, blockFieldName, expectedValue);
    }
}
