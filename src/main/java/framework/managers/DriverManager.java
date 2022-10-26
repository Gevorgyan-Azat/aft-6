package framework.managers;

import org.apache.commons.exec.OS;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;

import static framework.utils.PropConst.*;

public class DriverManager {

    private WebDriver driver;
    private final TestPropManager propManager = TestPropManager.getINSTANCE();

    private static DriverManager INSTANCE = null;

    private String propBrowserName = System.getProperty("browser", propManager.getProperty(TYPE_BROWSER));


    public static DriverManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DriverManager();
        }
        return INSTANCE;
    }

    public WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }

    private void initDriver() {
        if (OS.isFamilyWindows()) {
            initDriverWindowsOsFamily();
        } else if (OS.isFamilyMac()) {
            initDriverMacOsFamily();
        } else if (OS.isFamilyUnix()) {
            initDriverUnixOsFamily();
        }
    }

    private void initDriverWindowsOsFamily() {
        initDriverAnyOsFamily(PATH_GECKO_DRIVER_WINDOWS, PATH_CHROME_DRIVER_WINDOWS);
    }

    private void initDriverMacOsFamily() {
        initDriverAnyOsFamily(PATH_GECKO_DRIVER_MAC, PATH_CHROME_DRIVER_MAC);
    }

    private void initDriverUnixOsFamily() {
        initDriverAnyOsFamily(PATH_GECKO_DRIVER_UNIX, PATH_CHROME_DRIVER_UNIX);
    }

    private void initDriverAnyOsFamily(String gecko, String chrome) {
        switch (propBrowserName) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", propManager.getProperty(chrome));
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-gpu-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-blink-features");
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("useAutomationExtension", false);
                options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-popup-blocking");
                options.addArguments("--profile-directory=Default");
                options.addArguments("--ignore-certificate-errors");
                options.addArguments("--disable-plugins-discovery");
                options.addArguments("--incognito");
                options.addArguments("user_agent=DN");
                options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
                options.addArguments("user-data-dir=C:\\Users\\PC\\AppData\\Local\\Google\\Chrome\\User Data");
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability(ChromeOptions.CAPABILITY, options);
                driver = new ChromeDriver(cap);
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", propManager.getProperty(gecko));
                driver = new FirefoxDriver();
                break;
            case "remote":
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setBrowserName("chrome");
                capabilities.setVersion("84.0");
                capabilities.setCapability("enableVNC", true);
                capabilities.setCapability("enableVideo", false);
                try {
                    driver = new RemoteWebDriver(
                            URI.create("http://130.193.49.85:4444/wd/hub").toURL(),
                            capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Assert.fail("Типа браузера '" + propBrowserName + "' не существует во фреймворке");
        }
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
