package main.java.com.seo.auto.bot;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VingleBot extends BaseBot {

    private static String usrName = "kimnguyenvn085";
    private static String pwd = "%_xV&B6NV6z-d3n";
    private static final Logger LOGGER = Logger.getLogger(VingleBot.class);

    @Override
    public boolean login() {
        driver.get("https://www.vingle.net/users/sign_in");
        try {
            Thread.sleep(2000l);

            driver.findElement(By.name("username")).sendKeys(usrName);
            driver.findElement(By.name("password")).sendKeys(pwd);
            Thread.sleep(3000l);
            driver.findElement(By.xpath("//button[contains(text(),'Log In')]")).click();
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
            // bloody vingle using reactjs
            driver.navigate().to("https://www.vingle.net/writing_form/new");
            Thread.sleep(4000l);
            driver.findElements(By.className("vingleToolTip__rootWrapper__2Qz")).get(1).findElement(By.className("leftToolBar__boxItemWrapper__2re")).click();
            Thread.sleep(500l);
            driver.findElements(By.xpath("//input[contains(@placeholder,'Insert a link')]")).get(1).sendKeys(link);
            Thread.sleep(500l);
            driver.findElements(By.xpath("//button[text() = 'Done']")).get(1).click();
            Thread.sleep(4000l);

            driver.findElement(By.xpath("//button[contains(@class,'editorRightMenu__save')]")).click();
            Thread.sleep(600l);
            // confirmation of interest
            driver.findElement(By.xpath("//button[text() = 'OK!']")).click();
            Thread.sleep(600l);
            driver.findElement(By.xpath("//button[contains(@class,'publishPopover__primaryButton')]")).click();
            driver.findElement(By.xpath("//button[contains(@class,'vingleDialog__submitButton')]")).click();

            Thread.sleep(4000l);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (NoClassDefFoundError err) {
            LOGGER.error(err.getMessage());
            return false;
        }
        LOGGER.info("Posted " + link + " to Vingle for user " + usrName);
        takeScreenshot("Vingle-post" + usrName + "-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-ss")));
        return true;
    }
}
