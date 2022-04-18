package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WordpressBot extends BaseBot {

//    public static String usrName = "kimnguyenvn085";
//    public static String pwd = "qwer56783#2S";
    public static String usrName = "";
    public static String pwd = "";

    private static final Logger LOGGER = Logger.getLogger(WordpressBot.class);

    @Override
    public boolean login() {
        driver.get("https://wordpress.com/log-in");
        try {
            Thread.sleep(1000l);

            driver.findElement(By.id("usernameOrEmail")).sendKeys(usrName);
            driver.findElement(By.xpath("//button[@type = 'submit']")).click();
            Thread.sleep(1000l);
            driver.findElement(By.name("password")).sendKeys(pwd);
            driver.findElement(By.xpath("//button[@type = 'submit']")).click();
            Thread.sleep(3000l);
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
    public boolean postLink(String link) {
        try {
            driver.findElement(By.xpath("//a[@title = 'Create a New Post']")).click();
            Thread.sleep(8000l);

            WebDriver loginFrame = driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@src,'post-new.php')]")));

            // if there is tour guide, just skip
            String pageSource = loginFrame.getPageSource();
            if (!Jsoup.parse(pageSource).select("button:containsOwn(Skip)").isEmpty()) {
                driver.findElement(By.xpath("//button[text() = 'Skip']")).click();
                LOGGER.info("There is tour guide, just press skip");
            }

            Thread.sleep(3000l);
            driver.findElement(By.xpath("//button[@aria-label = 'Toggle block inserter']")).click();
            Thread.sleep(1500l);
            loginFrame.findElement(By.xpath("//button[contains(@class, 'editor-block-list-item-embed')]")).click();
            loginFrame.findElement(By.xpath("//input[@type='url']")).sendKeys(link);
            Thread.sleep(1000l);

            driver.findElement(By.xpath("//button[text() = 'Embed']")).click();
            Thread.sleep(600l);
            loginFrame.findElement(By.xpath("//button[text() = 'Publish']")).click();
            Thread.sleep(600l);
            loginFrame.findElement(By.xpath("//button[contains(@class, 'editor-post-publish-button ')]")).click();
            Thread.sleep(5000l);
            loginFrame.findElement(By.xpath("//a[text() = 'View Post']")).click();
            driver.switchTo().defaultContent();
            Thread.sleep(2000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Wordpress user " + usrName);
        takeScreenshot("Wordpress-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
