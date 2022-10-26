package framework.utils;


import framework.managers.DriverManager;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class MyAllureListener extends AllureCucumber5Jvm {

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, testStepFinished -> {
            if (testStepFinished.getResult().getStatus().equals(Status.FAILED)) {
                Allure.attachment("Screenshot", new ByteArrayInputStream(getScreenshot()));
            }
        });
        super.setEventPublisher(publisher);
    }

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    public byte[] getScreenshot() {
        return ((TakesScreenshot) DriverManager.getINSTANCE().getDriver()).getScreenshotAs(OutputType.BYTES);
    }

}
