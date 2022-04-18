package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoopItBot extends BaseBot {

    private static String usrName = "kimnguyenvn085@gmail.com";
    private static String pwd = "%_xV&B6NV6z-d3n";
    private static final Logger LOGGER = Logger.getLogger(ScoopItBot.class);

    @Override
    public boolean login() {
        driver.get("https://www.scoop.it/login");
        try {
            Thread.sleep(2000l);

            driver.findElement(By.name("email")).sendKeys(usrName);
            driver.findElement(By.name("password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.xpath("//button[contains(@class,'button-blue')]")).click();
            Thread.sleep(2000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean postLink(String link) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // close suggestions box
            js.executeScript("document.getElementsByClassName(\"block-close\")[0].click();");

            driver.findElement(By.id("urlChooserField")).sendKeys(link);
            Thread.sleep(600l);

            // some freaking problem with that clickable div
            WebElement clickDivElement = driver.findElement(By.id("urlChooserButton"));
            js.executeScript("arguments[0].click();", clickDivElement);
            Thread.sleep(4000l);

            clickDivElement = driver.findElement(By.xpath("//div[text() = 'Publish']"));
            js.executeScript("arguments[0].click();", clickDivElement);
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to ScoopIt for user " + usrName);
        takeScreenshot("ScoopIt-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
