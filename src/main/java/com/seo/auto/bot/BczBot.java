package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// basically wp bot
public class BczBot extends BaseBot {

    private static String usrName = "kimnguyenvn085@gmail.com";
    private static String pwd = "%_xV&B6NV6z-d3n";
    private static final Logger LOGGER = Logger.getLogger(BczBot.class);

    @Override
    public boolean login() {
        driver.get("https://bcz.com/sign");
        try {
            Thread.sleep(1000l);

            driver.findElement(By.id("user_login")).sendKeys(usrName);
            driver.findElement(By.id("user_pass")).sendKeys(pwd);
            driver.findElement(By.id("wp-submit")).click();
            Thread.sleep(4000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean postLink(String link) {
        try {
            driver.findElement(By.xpath("//a[contains(@href, 'edit.php')]")).click();
            driver.findElement(By.xpath("//a[contains(@href, 'post-new.php')]")).click();
            Thread.sleep(4000l);

            // if there is tour guide, just skip
            String pageSource = driver.getPageSource();
            if (!Jsoup.parse(pageSource).getElementsByAttributeValue("aria-label", "Close dialog").isEmpty()) {
                driver.findElement(By.xpath("//button[@aria-label = 'Close dialog']")).click();
                LOGGER.info("There is tour guide, just press skip");
            }

            Thread.sleep(1500l);
            driver.findElement(By.xpath("//button[@aria-label = 'Toggle block inserter']")).click();
            Thread.sleep(1500l);
            driver.findElement(By.xpath("//button[contains(@class, 'editor-block-list-item-embed')]")).click();
            driver.findElement(By.xpath("//input[@type='url']")).sendKeys(link);
            Thread.sleep(1000l);

            driver.findElement(By.xpath("//button[text() = 'Embed']")).click();
            Thread.sleep(600l);
            driver.findElement(By.xpath("//button[text() = 'Publish']")).click();
            Thread.sleep(600l);
            driver.findElement(By.xpath("//button[contains(@class, 'editor-post-publish-button ')]")).click();
            Thread.sleep(5000l);
            driver.findElement(By.xpath("//a[text() = 'View Post']")).click();
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("Posted " + link + " to Bcz user " + usrName);
        takeScreenshot("Bcz-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
