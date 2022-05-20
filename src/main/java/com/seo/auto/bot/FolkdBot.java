package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Magazines service ??? Make sure there is default magazines
public class FolkdBot extends BaseBot {

//    public static String usrName = "kimnguyenvn085@gmail.com";
//    public static String pwd = "1234qwer!@#e4df";
    public static String usrName = "";
    public static String pwd = "";
    private static final Logger LOGGER = Logger.getLogger(FolkdBot.class);

    @Override
    public boolean login() {
        driver.get("https://folkd.com/page/login.html");
        try {
            Thread.sleep(3000l);

            // accept cookie policy first if any
//            String pageSource = driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title = 'SP Consent Message']"))).getPageSource();
            String pageSource = driver.getPageSource();
            if (!Jsoup.parse(pageSource).select("div#CybotCookiebotDialogHeader").isEmpty()) {
                driver.findElement(By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")).click();
                LOGGER.info("There is cookie policy, just press skip");
                Thread.sleep(2000l);
            }

            driver.findElement(By.id("username")).sendKeys(usrName);
            driver.findElement(By.id("password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.id("submit_login")).click();
            Thread.sleep(4000l);

            // Check login success
            if (!driver.findElements(By.xpath("//img[contains(@src,'/images/warning.gif')]")).isEmpty()) {
                LOGGER.info("Login error");
                return false;
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "FolkdBot";
    }

    @Override
    public boolean postLink(String link) {
        try {
            driver.get("https://folkd.com/page/submit.html");
            Thread.sleep(1500l);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById('url_page').value = '';");

            driver.findElement(By.id("url_page")).sendKeys(link);
            driver.findElement(By.xpath("//button[text() = 'Submit']")).click();
            Thread.sleep(1500l);
            driver.findElement(By.id("add_title")).sendKeys(link);
            driver.findElement(By.xpath("//input[contains(@value,'Save Item')]")).click();
            Thread.sleep(2000l);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Folkd for user " + usrName);
        takeScreenshot("Folkd-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
