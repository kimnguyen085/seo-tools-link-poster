package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Magazines service ??? Make sure there is default magazines
public class FlipboardBot extends BaseBot {

//    public static String usrName = "kimnguyenvn085@gmail.com";
//    public static String pwd = "1234qwer!@#e4df";
    public static String usrName = "";
    public static String pwd = "";
    private static final Logger LOGGER = Logger.getLogger(FlipboardBot.class);

    @Override
    public boolean login() {
        driver.get("https://flipboard.com/login");
        try {
            Thread.sleep(3000l);

            // accept cookie policy first if any
//            String pageSource = driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title = 'SP Consent Message']"))).getPageSource();
            String pageSource = driver.getPageSource();
            if (!Jsoup.parse(pageSource).select("button:containsOwn(Accept)").isEmpty()) {
                String fpageSource = driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title = 'SP Consent Message']"))).getPageSource();
                driver.findElement(By.xpath("//button[text() = 'Accept']")).click();
                LOGGER.info("There is cookie policy, just press skip");
                Thread.sleep(2000l);
            }

            driver.findElement(By.name("username")).sendKeys(usrName);
            driver.findElement(By.name("password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.xpath("//button[contains(@data-vars-button-name,'login__form-submit')]")).click();
            Thread.sleep(7000l);
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
        return "FlipboardBot";
    }

    @Override
    public boolean postLink(String link) {
        try {
            driver.findElement(By.xpath("//button[contains(@data-vars-button-name,'navbar-flip-compose')]")).click();
            driver.findElement(By.xpath("//input[contains(@name,'flip-compose-flip-url')]")).sendKeys(link);
            Thread.sleep(600l);

            driver.findElement(By.xpath("//button[contains(@data-vars-button-name,'flip-compose-submit')]")).click();
            Thread.sleep(4000l);
            driver.findElement(By.xpath("//button[contains(@data-vars-button-name,'navbar-profile')]")).click();
            driver.findElement(By.xpath("//a[contains(@name,'main-nav-profile')]")).click();
            Thread.sleep(2000l);

            driver.findElement(By.xpath("//a[contains(@class,'section-tiles__tile')]")).click();
            Thread.sleep(3000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Flipboard for user " + usrName);
        takeScreenshot("Flipboard-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
