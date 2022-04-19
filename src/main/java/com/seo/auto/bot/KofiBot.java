package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Under construction, just 5 posts accepted
public class KofiBot extends BaseBot {

//    public static String usrName = "kimnguyenvn085@gmail.com";
//    public static String pwd = "1234qwer!@#e4df";
    public static String usrName = "kimnguyenvn085@gmail.com";
    public static String pwd = "1234qwer!@#e4df";
    private static final Logger LOGGER = Logger.getLogger(KofiBot.class);

    @Override
    public boolean login() {
        driver.get("https://ko-fi.com/account/login");
        try {
            Thread.sleep(3000l);

            driver.findElement(By.id("UserName")).sendKeys(usrName);
            driver.findElement(By.id("Password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.id("formSubmitButton")).click();
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
//            driver.get("https://ko-fi.com/manage/mypage");
//
//            driver.findElement(By.id("btnAddSomethingFixed")).click();
//            Thread.sleep(2000l);

            driver.get("https://ko-fi.com/blog/editor");
            driver.findElement(By.id("blogPostTitle")).sendKeys(link);
            driver.findElement(By.id("embedUrl")).sendKeys(link);
            Thread.sleep(2000l);

            driver.findElement(By.xpath("//div[text() = 'Publish']")).click();
            driver.findElement(By.xpath("//button[@name = 'submit']")).click();
            Thread.sleep(1000l);
            driver.findElement(By.xpath("//button[text() = 'Publish it!']")).click();
            Thread.sleep(4000l);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Kofi for user " + usrName);
        takeScreenshot("Kofi-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
