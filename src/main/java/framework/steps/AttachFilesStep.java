package framework.steps;

import framework.utils.GsonSerial;
import io.cucumber.java.ru.И;
import io.qameta.allure.Allure;

import java.io.IOException;

public class AttachFilesStep {

    @И("^Прикрепить Json файл$")
    public void attachJsonFile() throws IOException {
        Allure.attachment("Json-report", new GsonSerial().parse());
    }
}
