package main.java.com.seo.auto.bot;

import main.java.com.seo.auto.utils.ClipboardUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ElloCoBot extends BaseBot {

//    public static String usrName = "inoxtrihieu";
//    public static String pwd = "Phanhuuduc27031964";
    public static String usrName = "";
    public static String pwd = "";

    private static final Logger LOGGER = Logger.getLogger(ElloCoBot.class);

    @Override
    public boolean login() {
        driver.get("https://ello.co/enter");
        try {
            Thread.sleep(2000l);

            driver.findElement(By.id("usernameOrEmail")).sendKeys(usrName);
            driver.findElement(By.name("user[password]")).sendKeys(pwd);
            Thread.sleep(2000l);
            driver.findElement(By.xpath("//button[contains(@class,'FormButton')]")).click();
            Thread.sleep(2000l);
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
            driver.findElement(By.xpath("//button[contains(@class,'NavbarOmniButton')]")).click();
            Thread.sleep(2000l);

            // copy link to clipboard
            ClipboardUtils.copyStringToClipboard(link);
            WebElement inputElement  = driver.findElement(By.xpath("//div[contains(@class,'editable text')]"));
            Thread.sleep(500l);
            inputElement.click();
            Thread.sleep(1000l);
            Actions builder = new Actions(driver);

            // performing paste behavior
            if (SystemUtils.IS_OS_MAC_OSX) {
                builder.keyDown( Keys.META ).sendKeys( "v" ).keyUp( Keys.META ).build().perform();
            } else {
                builder.keyDown( Keys.CONTROL ).sendKeys( "v" ).keyUp( Keys.CONTROL ).build().perform();
            }

            Thread.sleep(2000l);
            JavascriptExecutor js = (JavascriptExecutor) driver;
//            driver.findElement(By.xpath("//button[contains(@class,'PostActionButton forSubmit forPost')]")).click();
            WebElement button = driver.findElement(By.xpath("//button[contains(@class,'PostActionButton forSubmit forPost')]"));
            js.executeScript("arguments[0].click();", button);
            Thread.sleep(1000l);

            driver.findElement(By.xpath("//button[contains(@class,'Avatar')]")).click();
            Thread.sleep(600l);
            driver.findElement(By.xpath("//nav[contains(@class,'css-19889w0 isActive')]")).findElement(By.tagName("a")).click(); // click on first link
            Thread.sleep(2000l);

            // scroll to new post
            WebElement toDivElement = driver.findElements(By.tagName("div")).get(12);
            js.executeScript("arguments[0].scrollIntoView();", toDivElement);
            Thread.sleep(600l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to ElloCo for user " + usrName);
        takeScreenshot("Elloco-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
