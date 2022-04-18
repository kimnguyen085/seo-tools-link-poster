package main.java.com.seo.auto.bot;

import main.java.com.seo.auto.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.network.Network;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseBot implements BotInterface {

    private static final Logger LOGGER = Logger.getLogger(BaseBot.class);
    protected WebDriver driver;

    public abstract boolean login();

    public abstract boolean postLink(String link);

    public void takeScreenshot(String screenshotName) {
        try {
            FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
                    new File(Constants.SCREENSHOTS_DEFAULT_DIRECTORY + "/" + screenshotName + ".jpg"), true);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("error ", e);
        }
    }

    public boolean scrollPage() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement endDivElement = driver.findElement(By.xpath("//div[last()]"));
            js.executeScript("arguments[0].scrollIntoView();", endDivElement);

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public boolean openPhantomJs() {
        try {
            if (driver != null) {
                driver = null;
            }

            if (Constants.PHANTOM_JS_PATH.contains("chrome")) {
                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--headless");
                System.setProperty("webdriver.chrome.driver",
                        System.getProperty("user.dir") + "/" + Constants.PHANTOM_JS_PATH);
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_settings.popups", 0);

                if (!Files.isDirectory(Paths.get(Constants.DOWNLOAD_DEFAULT_DIRECTORY))) {
                    Files.createDirectories(Paths.get(Constants.DOWNLOAD_DEFAULT_DIRECTORY));
                }

                prefs.put("download.default_directory", new File(Constants.DOWNLOAD_DEFAULT_DIRECTORY).getAbsolutePath());
                options.addArguments("--no-sandbox");
                options.setExperimentalOption("prefs", prefs);
                if (System.getenv("SMPL_IS_CONTAINER") != null) {
                    options.addArguments("--user-agent=" + System.getenv("SMPL_USER_AGENT"));
                }

                driver = new ChromeDriver(ChromeDriverService.createDefaultService(), options);
                driver.manage().window().setSize(new Dimension(1024, 1800));

                return true;
            } else {
                System.setProperty("phantomjs.binary.path", Constants.PHANTOM_JS_PATH);
                DesiredCapabilities dcap = new DesiredCapabilities();
                dcap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                        new String[]{"--webdriver-loglevel=NONE"});
                driver = new PhantomJSDriver(dcap);
                driver.manage().window().maximize();
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("error with opening chromedriver ", e);
        }

        return false;
    }

    public void closePhantomJsBr() {

        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    public void clearBrowsingData() {

        if (driver != null) {
            try {
                ((RemoteWebDriver)driver).manage().deleteAllCookies();
                ChromeDriver chromeDriver  = (ChromeDriver) driver;
                chromeDriver.getDevTools().createSessionIfThereIsNotOne();
                chromeDriver.getDevTools().send(Network.clearBrowserCookies());
                chromeDriver.getDevTools().send(Network.clearBrowserCache());
                Thread.sleep(500l);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
