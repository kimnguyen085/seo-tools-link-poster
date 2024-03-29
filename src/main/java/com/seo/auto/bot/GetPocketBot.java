package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetPocketBot extends BaseBot {

//    public static String usrName = "kimnguyenvn085@gmail.com";
//    public static String pwd = "%_xV&B6NV6z-d3n";
    public static String usrName = "";
    public static String pwd = "";
    private static final Logger LOGGER = Logger.getLogger(GetPocketBot.class);

    @Override
    public boolean login() {
        driver.get("https://getpocket.com/login");
        try {
            Thread.sleep(2000l);

            driver.findElement(By.name("username")).sendKeys(usrName);
            driver.findElement(By.name("password")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.xpath("//input[contains(@class,'login-btn-email')]")).click();
            Thread.sleep(7000l);

            // Check login success
            if (!driver.findElements(By.xpath("//p[contains(@class,'login-error')]")).isEmpty() ||
                    !driver.findElements(By.xpath("//iframe[contains(@title,'recaptcha')]")).isEmpty()) { // captcha
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
        return "GetPocketBot";
    }

    @Override
    public boolean postLink(String link) {
        try {
            driver.navigate().to("https://getpocket.com/my-list");
            Thread.sleep(4000l);
            driver.findElement(By.xpath("//button[@aria-label = 'Save a URL']")).click();
            Thread.sleep(1000l);

            driver.findElement(By.xpath("//input[@aria-label = 'Add Item to Pocket']")).sendKeys(link);
            Thread.sleep(600l);

            driver.findElement(By.xpath("//button[@data-cy = 'add-submit']")).click();
            Thread.sleep(4000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to GetPocket for user " + usrName);
        takeScreenshot("GetPocket-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
