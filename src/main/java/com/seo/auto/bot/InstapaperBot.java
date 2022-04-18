package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Magazines service ??? Make sure there is default magazines
public class InstapaperBot extends BaseBot {

    private static String usrName = "kimnguyenvn085@gmail.com";
    private static String pwd = "1234qwer!@#e4df";
    private static final Logger LOGGER = Logger.getLogger(InstapaperBot.class);

    @Override
    public boolean login() {
        driver.get("https://www.instapaper.com/user/login");
        try {
            Thread.sleep(3000l);

            driver.findElement(By.name("username")).sendKeys(usrName);
            driver.findElement(By.name("password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.id("log_in")).click();
            Thread.sleep(4000l);
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
            driver.findElement(By.xpath("//a[contains(@data-modal,'add_link_modal')]")).click();
            driver.findElement(By.xpath("//input[contains(@name,'bookmark[url]')]")).sendKeys(link);
            Thread.sleep(600l);
            driver.findElement(By.xpath("//input[contains(@name,'bookmark[url]')]/following-sibling::input")).click();

            Thread.sleep(3000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Instapaper for user " + usrName);
        takeScreenshot("Instapaper-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
