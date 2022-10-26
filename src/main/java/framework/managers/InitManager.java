package framework.managers;

import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static framework.utils.PropConst.IMPLICITLY_WAIT;
import static framework.utils.PropConst.PAGE_LOAD_TIMEOUT;


public class InitManager {

    private static final TestPropManager props = TestPropManager.getINSTANCE();

    private static final DriverManager driverManager = DriverManager.getINSTANCE();

    public static void initFramework() {
        driverManager.getDriver().manage().window().maximize();
        driverManager.getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(props.getProperty(IMPLICITLY_WAIT)), TimeUnit.SECONDS);
        driverManager.getDriver().manage().timeouts().pageLoadTimeout(Integer.parseInt(props.getProperty(PAGE_LOAD_TIMEOUT)), TimeUnit.SECONDS);
    }

    public static void quitFramework() {
        driverManager.quitDriver();
    }
}
