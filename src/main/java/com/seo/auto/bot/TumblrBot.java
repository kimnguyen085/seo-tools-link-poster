package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TumblrBot extends BaseBot {

//    public static String usrName = "kimnguyen085@gmail.com";
//    public static String pwd = "AN6W4w^mXS5ZD(d";
    public static String usrName = "";
    public static String pwd = "";
    private static final Logger LOGGER = Logger.getLogger(TumblrBot.class);

    @Override
    public boolean login() {
        driver.get("https://www.tumblr.com/");
        try {
            Thread.sleep(5000l);
            driver.findElement(By.xpath("//a[contains(@href,'login')]")).click();

            driver.findElement(By.name("email")).sendKeys(usrName);
            driver.findElement(By.name("password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.xpath("//button[contains(@aria-label,'Log in')]")).click();
            Thread.sleep(2000l);

            // Check login success
            if (!driver.findElements(By.xpath("//div[contains(@data-has-error,'true')]")).isEmpty()) { // captcha
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
        return "TumblrBot";
    }

    @Override
    public boolean postLink(String link) {
        try {
            driver.findElement(By.xpath("//a[contains(@aria-label,'New post')]")).click();
            Thread.sleep(1500l);
            driver.findElement(By.xpath("//a[contains(@href,'new/link')]")).click();
            Thread.sleep(3000l);
            driver.switchTo().frame(0);  // freaking tumblr using iframe
            driver.findElement(By.xpath("//div[contains(@aria-label,'Type or paste a URL')]")).sendKeys(link);
            Thread.sleep(2000l);
            driver.findElement(By.className("create_post_button")).click();
            Thread.sleep(3000l);
            driver.switchTo().defaultContent();

            driver.findElement(By.xpath("//button[contains(@aria-label,'Account')]")).click();
            Thread.sleep(600l);
            driver.findElement(By.xpath("//span[text() = 'Posts']")).click();
            Thread.sleep(2000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Tumblr for user " + usrName);
        takeScreenshot("Tumblr-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
