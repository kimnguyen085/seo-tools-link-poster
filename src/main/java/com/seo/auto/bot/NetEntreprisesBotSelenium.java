package main.java.com.seo.auto.bot;

import main.java.com.seo.auto.utils.Constants;
import main.java.com.seo.auto.utils.CustomFileUtils;
import main.java.com.seo.auto.utils.UtilsMeth;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.network.Network;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class NetEntreprisesBotSelenium {

    private static final Logger LOGGER = Logger.getLogger(NetEntreprisesBotSelenium.class);
    private WebDriver driver;
    private String status = "";
    private HttpClient httpClient;
    private CookieStore httpCookieStore;

    private static final String STR_ELEMENT_SELECTOR_CODE_RISQUE = "codeRisque";

    private static final String STR_ELEMENT_SELECTOR_VALID_BUTTON = "validButton";

    private static final String STR_ELEMENT_SELECTOR_IMG_ALT_PDF = "img[alt='PDF']";

    private static final String STR_ELEMENT_SELECTOR_IMG_ALT_CSV = "img[alt='CSV']";

    private static final String STR_WRONG_URL = "WRONG URL -- ";

    private static final String STR_TITLE = "title";

    private static final String STR_FEUILLE_DE_CALCUL = "feuille de calcul";

    private Map<String, Object[]> mapFileName = new LinkedHashMap<>();

    public WebDriver getDriver() {
        return driver;
    }

//    public boolean openPhantomJs() {
//
//        try {
//			if (driver != null) {
//				clearBrowsingData();
//				return true;
//			}
////            driver.findElement().
//            if (Constants.PHANTOM_JS_PATH.contains("chrome")) {
//                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--headless");
//                System.setProperty("webdriver.chrome.driver",
//                        System.getProperty("user.dir") + "/" + Constants.PHANTOM_JS_PATH);
//                Map<String, Object> prefs = new HashMap<>();
//                prefs.put("profile.default_content_settings.popups", 0);
//
//                if (!Files.isDirectory(Paths.get(Constants.DOWNLOAD_DEFAULT_DIRECTORY))) {
//                    Files.createDirectories(Paths.get(Constants.DOWNLOAD_DEFAULT_DIRECTORY));
//                }
//
//                prefs.put("download.default_directory", new File(Constants.DOWNLOAD_DEFAULT_DIRECTORY).getAbsolutePath());
//                options.addArguments("--no-sandbox");
//                options.setExperimentalOption("prefs", prefs);
//                if (System.getenv("SMPL_IS_CONTAINER") != null) {
//                    options.addArguments("--user-agent=" + System.getenv("SMPL_USER_AGENT"));
//                }
//
//                driver = new ChromeDriver(ChromeDriverService.createDefaultService(), options);
//                driver.manage().window().setSize(new Dimension(1024, 900));
//                if (System.getenv("SMPL_IS_CONTAINER") != null) {
//                    driver.get("https://www.whatismybrowser.com/detect/what-is-my-user-agent");
//                    LOGGER.info("USER AGENT FOUND = "
//                            + Jsoup.parse(driver.getPageSource()).select("#detected_value").text());
//                }
//                return true;
//            } else {
//                System.setProperty("phantomjs.binary.path", Constants.PHANTOM_JS_PATH);
//                DesiredCapabilities dcap = new DesiredCapabilities();
//                dcap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
//                        new String[]{"--webdriver-loglevel=NONE"});
//                driver = new PhantomJSDriver(dcap);
//                driver.manage().window().maximize();
//                return true;
//            }
//        } catch (Exception e) {
//            LOGGER.error("error with opening chromedriver ", e);
//        }
//
//        return false;
//    }
//    public static void main(String[] args){
//        /*InputData input=new InputData("84250305400012", "BEDOUCHA","CHARLES","24be09RZ&vivelevelo","","","");
//        NetEntreprisesBotSelenium bot = new NetEntreprisesBotSelenium();
//        int login=bot.loginNew(input);
//        LOGGER.info("login val="+login);*/
//        NetEntreprisesBotSelenium bot=new NetEntreprisesBotSelenium();
//        bot.openPhantomJs();
//        bot.getDriver().get("https://files.simplicia.co/elem_calcul.html");
//        bot.selectYear2020ForElemCalcul();
//        bot.getDriver().get("https://files.simplicia.co/historique_taux_2_pages.html");
//
//        bot.getLastPageNum();
//
//    }
//    public int loginNew(InputData inputData) {
//        try {
//            httpCookieStore = new BasicCookieStore();
//            httpClient = HttpClientBuilder.create()
//                    .setDefaultCookieStore(httpCookieStore)
//                    .setRedirectStrategy(new LaxRedirectStrategy())
//                    .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
//                            .loadTrustMaterial(null, (chain, authType) -> true)
//                            .useTLS()
//                            .build()))
//                    .build();
//
//            List<NameValuePair> params = new ArrayList<>(2);
//            params.add(new BasicNameValuePair("j_siret", inputData.getCompanyID()));
//            params.add(new BasicNameValuePair("j_nom", inputData.getLastname()));
//            params.add(new BasicNameValuePair("j_prenom", inputData.getFirstname()));
//            params.add(new BasicNameValuePair("j_password", inputData.getPassword()));
//
//            HttpPost httppost = new HttpPost("https://portail.net-entreprises.fr/auth/pass");
//            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//            httpClient.execute(httppost);
//            HttpResponse response = httpClient.execute(new HttpGet("https://portail.net-entreprises.fr/srv"));
//
//            String pageSource = EntityUtils.toString(response.getEntity());
//            if (pageSource.contains("Se déconnecter")) {
//                LOGGER.info("Logged in successfully - " + inputData.getCompanyID()
//                        +"-- ATMP="+pageSource.contains("ATMP")
//                        +"-- BPIJ="+ pageSource.contains("Attestation de salaire")
//                        +"-- DAT="+pageSource.contains("DAT"));
//                return 1;
//            }
//
//            if (pageSource.contains("Nous ne sommes pas en mesure de répondre à votre demande.")) {
//                return 0;
//            }
//
//            if (pageSource.contains("Vous avez échoué 3 fois consécutivement")) {
//                return -2;
//            }
//        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | IOException e) {
//            LOGGER.error("ERROR - login. companyID: " + inputData.getCompanyID(), e);
//        }
//
//        LOGGER.info("Failed to login - " + inputData.getCompanyID());
//        return -1;
//    }
//    public void quit() {
//
//        try {
//            driver.get("https://www.net-entreprises.fr");
//            TimeUnit.SECONDS.sleep(Constants.DELAY + 1l);
//        } catch (Exception e) {
//            LOGGER.error("ERROR - leaving net-entreprises", e);
//        }
//    }
//
//    public void clearCache() {
//        clearBrowsingData();
//        if (driver != null) {
//            driver.manage().deleteAllCookies();
//        }
//    }
//
//    public void closePhantomJsBr() {
//        if (driver != null) {
//            driver.close();
//            driver.quit();
//        }
//    }
//
//    public int getLastPageNum() {
//
//        return getLastPageNumNew(driver.getPageSource());
//    }
//
//    public static int getLastPageNumNew(String page) {
//
//        try {
//            Elements l = Jsoup.parse(page).getElementsContainingOwnText("Nombre total de lignes :");
//            Integer paginationTxt;
//            System.out.println(l.get(0).text());
//            System.out.println(l.get(0).text().split(":")[3].trim());
//            try {
//                paginationTxt = new Integer(l.get(0).text().split(":")[3].trim());
//            } catch (Exception e) {
//                paginationTxt = new Integer(l.get(0).text().replaceAll(".*Nombre total de lignes *\\: *([0-9]+) *[^0-9].*", "$1"));
//            }
//            int lastPageNumber = (paginationTxt + 99) / 100;
//            LOGGER.info("FOUND=" + lastPageNumber);
//            return lastPageNumber;
//        } catch (Exception ex) {
//            return 1;
//        }
//    }
//
//    public int goToDownloadPage(InputData inputData) {
//
//        String companyID = inputData.getCompanyID();
//        if (clickOnCompte(inputData)) {
//            return 1;
//        }
//
//        if (driver.getPageSource().contains("aucun AT/MP pour la")) {
//            return 3;
//        }
//
//        if (driver.getPageSource().contains("pour mon prochain taux")) {
//
//            Integer checked = checkDownloadPageSoureContainsSome(companyID);
//            if (checked != null) {
//                return checked;
//            }
//
//        } else {
//            LOGGER.info("OLD VERSION");
//            if (clickOnPassedAnchor("Compte Employeur Courant")) {
//                return 2;
//            }
//            LOGGER.info("URL="+driver.getCurrentUrl());
//        }
//
//        return 0;
//    }
//
//    private Integer checkDownloadPageSoureContainsSome(String companyID) {
//
//        driver.navigate().refresh();
//        LOGGER.info("NEW VERSION");
//
//        if (/* clickOnPassedAnchor("Compte Employeur Courant") */clickOnCEC()) {
//            return 2;
//        }
//
//        LOGGER.info("URL="+driver.getCurrentUrl());
//
//        if (Jsoup.parse(driver.getPageSource()).text().contains("non prise en compte")) {
//            LOGGER.info("[" + companyID + "] " + "INSCRIPTION NON PRISE EN COMPTE");
//            return 7;
//        }
//
//        if (Jsoup.parse(driver.getPageSource()).text().contains("aucun AT/MP pour la")) {
//            LOGGER.info("[" + companyID + "] " + "Il n’y a aucun AT/MP pour la période N et N-1");
//            return 3;
//        }
//
//        if (Jsoup.parse(driver.getPageSource()).text().contains("service indisponible")) {
//            LOGGER.info("[" + companyID + "] " + "SERVICE INDISPONIBLE");
//            return 8;
//        }
//
//        if (!Jsoup.parse(driver.getPageSource()).text().contains("SIRET " + companyID)) {
//            return 9;
//        }
//
//        if (driver.findElements(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_PDF)).isEmpty()
//                || driver.findElements(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_CSV)).isEmpty()) {
//            return 10;
//        }
//
//        return null;
//    }
//
//    private boolean clickOnAccepter() {
//
//        WebElement acceptButton = null;
//
//        try {
//            acceptButton = driver
//                    .findElement(By.cssSelector("#mat-dialog-0 > app-cgu > div > div > button:nth-child(2)"));
//        } catch (Exception e) {
//            LOGGER.info("ACCEPTATION MSG NOT FOUND !");
//            return false;
//        }
//
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", acceptButton);
//        takeScreenshot("LOG/clickAccepter");
//        if (acceptButton != null) {
//            LOGGER.info("ACCEPTATION MSG FOUND ! NOW CLICKING"+acceptButton);
//            acceptButton.click();
//            try {
//                TimeUnit.SECONDS.sleep(2*Constants.DELAY);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return true;
//        } else {
//            LOGGER.info("ACCEPTATION MSG FOUND but button is NULL ! acceptButton="+acceptButton);
//            return false;
//        }
//    }
//
//    private boolean clickOnCEC() {
//        String targetUrl = "https://www.teleservices.cnav.fr/ceweb/#/cptempl";
//        clickOnAccepter();
//        boolean error = false;
//        int cour = 0;
//
//        while (!driver.getCurrentUrl().contains(targetUrl) && cour++ <= 4) {
//
//            try {
//                LOGGER.info(STR_WRONG_URL + driver.getCurrentUrl() + " | expected = " + targetUrl);
//                driver.get("https://www.teleservices.cnav.fr/ceweb/#/cptempl");
//                TimeUnit.SECONDS.sleep(Constants.DELAY * 4l);
//            } catch (Exception e) {
//                LOGGER.error("ERROR - clickOnCEC - Compte Employeur Courant");
//                error = true;
//            }
//        }
//        return error;
//    }
//
//    private boolean clickOnCompte(InputData inputData) {
//        String companyID = inputData.getCompanyID();
//        boolean error;
//        String targetUrl = "https://www.teleservices.cnav.fr/ceweb/#/";
//        int maxAttempts = 0;
//        int cour = 0;
//
//        while (!driver.getCurrentUrl().contains("https://portail.net-entreprises.fr/priv/declarations?rs=1")
//                && cour++ <= 4) {
//            driver.get("https://portail.net-entreprises.fr/priv/declarations?rs=1");
//            //LOGGER.info(STR_WRONG_URL + driver.getCurrentUrl() + "| expected="+targetUrl);
//        }
//
//        try {
//
//            WebElement moduleATMPBox = driver.findElement(By.id("service-11"));
//            String prefixUrl = "https://portail.net-entreprises.fr/priv/";
//
//            if (moduleATMPBox.getAttribute("href").startsWith("https")) {
//                prefixUrl = "";
//            }
//
//            driver.get(prefixUrl + moduleATMPBox.getAttribute("href"));
//            TimeUnit.SECONDS.sleep(Constants.DELAY + 1l);
//
//            while (!driver.getCurrentUrl().startsWith(targetUrl) && maxAttempts < 5) {
//                moduleATMPBox = driver.findElement(By.id("service-11"));
//                prefixUrl = "https://portail.net-entreprises.fr/priv/";
//
//                if (moduleATMPBox.getAttribute("href").startsWith("https")) {
//                    prefixUrl = "";
//                }
//
//                driver.get(prefixUrl + moduleATMPBox.getAttribute("href"));
//                TimeUnit.SECONDS.sleep(Constants.DELAY + 1l);
//                LOGGER.info("goToAT/MP -- Attempt " + maxAttempts++);
//            }
//
//            if (driver.getCurrentUrl().contains(targetUrl)) {
//                error = false;
//                TimeUnit.SECONDS.sleep(2);
//            } else {
//                if (driver.getPageSource()
//                        .contains("aucun SIRET connu dans la base. Veuillez contacter votre CARSAT ou votre CGSS")) {
//                    LOGGER.error("ERROR - clickOnCompte. ** AUCUN SIRET CONNU DANS LA BASE **companyID: " + companyID);
//                }
//                error = true;
//            }
//        } catch (Exception e) {
//            LOGGER.error("ERROR - clickOnCompte. companyID: " + companyID, e);
//            LOGGER.info("Current url compte introuvable : " + driver.getCurrentUrl());
//            takeScreenshot("LOG/" + "compte-AT-MP-introuvable_" + companyID);
//            error = true;
//        }
//
//        return error;
//    }
//
//    public void leaveNetEntreprises() {
//
//        LOGGER.info("----> GOODBYE NET E");
//
//        try {
//
//            driver.get("https://portail.net-entreprises.fr/");
//            driver.manage().deleteAllCookies();
//        } catch (Exception e) {
//            LOGGER.error("ERROR - leaveNetEntreprises");
//            e.printStackTrace();
//        }
//    }
//
//    private boolean clickOnPassedAnchor(String menuItemName) {
//
//        boolean error;
//
//        try {
//
//            List<WebElement> menuEls = driver.findElement(By.id("menu")).findElements(By.tagName("a"));
//            for (WebElement menuEl : menuEls) {
//
//                if (menuEl.getText().contains(menuItemName)) {
//                    menuEl.click();
//                    TimeUnit.SECONDS.sleep(Constants.DELAY + 2l);
//                    break;
//                }
//            }
//
//            error = false;
//        } catch (Exception e) {
//            LOGGER.error("ERROR - clickOnPassedAnchor. menuItemName: " + menuItemName, e);
//            error = true;
//        }
//
//        return error;
//    }
//
//    public int login(InputData inputData) {
//
//    	clearBrowsingData();
//        String companyID = inputData.getCompanyID();
//        this.status = "";
//        int maxTries = 4;
//        int count = 0;
//        boolean elementNotFound = true;
//        try {
//            while (elementNotFound && count <= maxTries) {
//                driver.get(Constants.LOGIN_URL);
//                TimeUnit.SECONDS.sleep(Constants.DELAY);
//                try {
//                    driver.findElement(By.id("j_siret"));
//                    elementNotFound = false;
//                } catch (Exception e) {
//                    count++;
//                }
//            }
//            driver.findElement(By.id("j_siret")).sendKeys(inputData.getCompanyID());
//            driver.findElement(By.id("j_nom")).sendKeys(inputData.getLastname());
//            driver.findElement(By.id("j_prenom")).sendKeys(inputData.getFirstname());
//            driver.findElement(By.id("j_password")).clear();
//            driver.findElement(By.id("j_password")).sendKeys(inputData.getPassword());
//            if (driver.findElement(By.id(STR_ELEMENT_SELECTOR_VALID_BUTTON)) != null) {
//                LOGGER.info("clicking 'login' " + companyID);
//                driver.findElement(By.id(STR_ELEMENT_SELECTOR_VALID_BUTTON)).click();
//            } else {
//                LOGGER.info("Failed to login - no login button found " + companyID);
//                return -1;
//            }
//
//            TimeUnit.SECONDS.sleep(2 * Constants.DELAY);
//            String pageSource = driver.getPageSource();
//
//            if (pageSource.contains("/priv/mdpexpire")) {
//                this.status = "mot de passe expiré. À changer.";
//                LOGGER.info("LE MOT DE PASSE EXPIRE BIENTÔT");
//            }
//
//            if (Jsoup.parse(pageSource).text().contains("Vos coordonnées doivent être confirmées")) {
//                LOGGER.info("CONFIRMATION AUTO INFOS POST LOGIN-");
//                driver.findElement(By.id("inputConfirmationConditions")).click();
//                driver.findElement(By.id(STR_ELEMENT_SELECTOR_VALID_BUTTON)).click();
//                LOGGER.info("Logged in successfully with auto confirm INFOs- " + companyID);
//                return 2;
//            }
//
//            if (Jsoup.parse(pageSource).text().contains("Afin de garantir les échanges par courriel")) {
//                LOGGER.info("CONFIRMATION INFOS POST LOGIN");
//                return -3;
//            }
//
//            if (!Jsoup.parse(pageSource).select(".slick-track").isEmpty() && pageSource.contains("Se déconnecter")) {
//                LOGGER.info("Logged in successfully - " + companyID);
//                return 1;
//            }
//
//            if (pageSource.contains("Nous ne sommes pas en mesure de répondre à votre demande.")) {
//                return 0;
//            }
//
//            if (pageSource.contains("Vous avez échoué 3 fois consécutivement")) {
//                return -2;
//            }
//        } catch (Exception e) {
//            LOGGER.error("ERROR - login. companyID: " + companyID, e);
//        }
//
//        LOGGER.info("Failed to login - " + companyID);
//
//        return -1;
//    }
//
//    public boolean goToFeuilleDeCalculPage(InputData inputData) {
//        /*starting sequence*/
//        String companyID = inputData.getCompanyID();
//        int count = 0;
//        boolean success = false;
//        while (!success) {
//            LOGGER.info("loop to reach correct situation in goToFeuilleDeCalculPage" + count++);
//            if (count > 1) {
//                LOGGER.info("RETRYING AS PREVIOUS ACCOUNT IS STILL ACTIVE");
//            }
//            if (driver.getCurrentUrl().contains("erreurJetonInvalide.html")) {
//                LOGGER.info("TOKEN ISSUE SPOTTED!");
//                reLoginInNewTab(inputData);
//                clickOnAccepter();
//            } else {
//                boolean errorClickOnCompte=clickOnCompte(inputData);
//                clickOnAccepter();
//                if(errorClickOnCompte)
//                    return false;
//            }
//
//            success = clickOnNewSC2Link(inputData, true);
//            try {
//                TimeUnit.SECONDS.sleep(Constants.DELAY);
//            } catch (Exception e) {
//                LOGGER.error(e.getMessage(), e);
//            }
//        }
//        if (Constants.isSc2Simple) {
//            return true;
//        } else {
//
//            //driver.get("https://demo.simplicia.co/listing-taux.html");
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (Exception e) {
//                LOGGER.error(e.getMessage(), e);
//            }
//
//            select100RecordPerPage();
//            return selectYearAndHitOnRechercher(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), true, inputData);
//        }
//    }
//
//    public boolean reLoginInNewTab(InputData inputData) {
//        driver.switchTo().defaultContent();
//        String originalWindowId = driver.getWindowHandle();
//        ((JavascriptExecutor) driver).executeScript("window.open()");
//
//        String newTabId = driver.getWindowHandles().stream().filter(window -> !originalWindowId.equals(window))
//                .findAny().orElse("");
//
//        driver.switchTo().window(newTabId);
//        int attempts = 0;
//        while (login(inputData) != 1 && attempts < 4) {
//            LOGGER.info("reloging in...");
//            attempts++;
//        }
//        clickOnCompte(inputData);
//        driver.close();
//        driver.switchTo().window(originalWindowId);
//        //driver.navigate().refresh();
//        return attempts <= 3;
//    }
//
//    private boolean clickOnNewSC2Link(InputData inputData, boolean isSimple) {
//        String targetUrl = "https://www.teleservices.cnav.fr/ceweb/#/historiqueTaux";
//        boolean success = false;
//
//        boolean hasSelectYear = false;
//        while (!hasSelectYear || !driver.getCurrentUrl().contains(targetUrl)) {
//            try {
//                driver.get(targetUrl);
//                TimeUnit.SECONDS.sleep(Constants.DELAY);
//            } catch (Exception e) {
//                LOGGER.error("ERROR - clickOnNewSC2Link");
//            }
//
//            try {
//                /*we need year checkbox only in case of SC2 NORMAL*/
//                if(!isSimple){
//                    driver.findElement(
//                            By.cssSelector("#contenu > table > tbody > tr:nth-child(2) > td:nth-child(1) > select"));
//                }
//                hasSelectYear = true;
//            } catch (Exception e) {
//                hasSelectYear = false;
//                LOGGER.info("year checkbox not found - URL=" + driver.getCurrentUrl());
//                reLoginInNewTab(inputData);
//                LOGGER.error("Select box element not found", e);
//				/*if(!isSimple) {
//					if (driver.getPageSource().contains("Erreur de lecture du cookie, veuillez vous reconnecter sur Net-Entreprises")) {
//						//call Login
//						//call clickOnCompte()
//						login(inputData);
//						goToFeuilleDeCalculPage(inputData);
//					}
//				}*/
//            }
//        }
//        success = driver.getCurrentUrl().contains(targetUrl);
//        LOGGER.info("correct URL ? " + success + " -- hasSelectYear ? " + hasSelectYear);
//        clickOnAccepter();
//        takeScreenshot("LOG/validation-" + UtilsMeth.getRandomHexString(8));
//        return success && hasSelectYear;
//
//    }
//
//    public void refreshPage() {
//        try {
//            driver.navigate().refresh();
//            TimeUnit.SECONDS.sleep(2);
//        } catch (Exception e) {
//            LOGGER.error("ERROR - refreshPage", e);
//        }
//    }
//
//    public void clickOnElementsDeCalcul() {
//        try {
//            List<WebElement> anchors = driver.findElements(By.tagName("a"));
//
//            for (WebElement anchor : anchors) {
//                if (anchor.getText().contains("Eléments de calcul")) {
//                    //driver.get("http://demo.simplicia.co/rates/Compte%20AT%20_%20MP.html");
//                    anchor.click();
//
//                    TimeUnit.SECONDS.sleep(Constants.DELAY);
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.error("ERROR - clickOnElementsDeCalcul", e);
//        }
//    }
//
//    public boolean downloadFeuilleFiles(InputData inputData, String companyID, String outputFolderPath, atmp.main.java.bot.SC2Row sc2Row, String ratesSummariesCSVFilePath, String clientId, boolean isSc2Normal) {
//        int tryCount=0;
//        int maxTries=5;
//        try {
//            int currYear = Calendar.getInstance().get(Calendar.YEAR);
//            WebElement[] elements = getCurrentRow(sc2Row);
//            WebElement currentRow = elements[1];
//            if (currentRow != null) {
//                String siretYY = sc2Row.getSiret();
//                String pdfName = buildFileNameSc2Normal("FDC", "pdf", elements[0], currYear + "");
//                String csvName = buildFileNameSc2Normal("FDC", "csv", elements[0], currYear + "");
//
//                mapFileName.put(siretYY, new Object[]{
//                        buildFileNameSc2Normal("EDC", "pdf", elements[0], currYear + ""),
//                        buildFileNameSc2Normal("EDC", "csv", elements[0], currYear + "")
//                });
//                LOGGER.info("OPENING ROW");
//                while(!driver.getCurrentUrl().contains("https://www.teleservices.cnav.fr/ceweb/#/feuilleCalcul") & tryCount<maxTries) {
//                    currentRow.click();
//                    LOGGER.info("TRYING OPENING ROW "+tryCount++);
//                    TimeUnit.SECONDS.sleep(2);
//                }
//                LOGGER.info("[" + companyID + "] " + "Page(Scenario 2) - Siret: " + sc2Row.getSiret() + " - Risque: " + sc2Row.getRisque());
//
//                TimeUnit.SECONDS.sleep(Constants.DELAY);
//                String folder = outputFolderPath + "/" + sc2Row.getRisque()
//                        + "/" + siretYY.split(" +")[1] + "/";
//                new File(folder).mkdirs();
//
//                if (Files.exists(Paths.get(folder + pdfName))
//                        && Files.exists(Paths.get(folder + csvName))
//                ) {
//                    return true;
//                }
//                LOGGER.info("rates.csv folder : "+ratesSummariesCSVFilePath);
//                LOGGER.info("FDC.csv folder : "+folder+csvName);
//
//                boolean ret=download(folder + pdfName, folder + csvName);
//                /*sendRates SC2 Normal*/
//                if(ret) {
//                    boolean ratesSentOk=ControllerCLI.sendRates("", companyID, clientId, ratesSummariesCSVFilePath, folder + csvName,isSc2Normal);
//                    if(ratesSentOk)
//                        LOGGER.info("RATES ROWS SENT OK");
//                }
//                return ret;
//            }
//        } catch (Exception e) {
//            LOGGER.error("ERROR - downloadFirst2Files. companyID: " + companyID, e);
//        }
//
//        return false;
//    }
//
//    public String buildFileNameSc2Normal(String prefix, String extensions, WebElement trlEle, String yyyy) {
//
//        String temp = "--";
//        StringBuilder str = new StringBuilder(prefix);
//        List<WebElement> tds;
//        if (trlEle != null) {
//            tds = trlEle.findElements(By.tagName("td"));
//        } else {
//            return prefix + "--erreur-nom-de-fichier--" + yyyy + "." + extensions;
//        }
//        String id = tds.get(1).getText();
//        str.append(temp).append(id);
//
//        String rishCode = tds.get(3).getText();
//        str.append(temp).append(rishCode);
//
//        String effectDate = tds.get(5).getText();
//        str.append(temp).append(effectDate);
//
//        String notifDate = tds.get(6).getText();
//        str.append(temp).append(notifDate);
//
//        str.append(temp).append(extensions.toUpperCase());
//
//        str.append(temp).append(yyyy);
//
//        str.append('.').append(extensions);
//
//        return str.toString().replace("/", "-");
//    }
//
//    public void selectYear(String outputFolderPath) {
//        /*disabling parse all rows*/
//        /*try {
//            WebElement select = driver.findElements(By.tagName("select"))
//                    .stream()
//                    .filter(w -> StringUtils.isNotBlank(w.getText().replaceAll("[\r\n]", "").trim()))
//                    .findFirst()
//                    .orElse(null);
//            if (select != null) {
//                List<WebElement> options = select.findElements(By.tagName("option"));
//
//                for (WebElement option : options) {
//                    String optTxt = option.getText();
//                    if (StringUtils.isNotEmpty(optTxt)) {
//                        option.click();
//                        TimeUnit.SECONDS.sleep(Constants.DELAY);
//                        WebElement export = driver.findElements(By.className("exports"))
//                                .stream().findFirst().orElse(null);
//                        if (export != null) {
//                            List<WebElement> elements = export.findElements(By.tagName("img"));
//
//                            for (WebElement element : elements) {
//                                try {
//                                    TimeUnit.SECONDS.sleep(3);
//                                    element.click();
//                                    TimeUnit.SECONDS.sleep(3);
//                                    File savedFile = CustomFileUtils.getLatestDownloadedFileNew(Constants.ATMP_DOWNLOAD_DEFAULT_DIRECTORY);
//                                    LOGGER.info("last downloaded file=" + savedFile.getAbsolutePath());
//                                    String downloadFileName = outputFolderPath + "/Element-calculs-taux-nr-" + optTxt + "." + FilenameUtils.getExtension(savedFile.getName());
//                                    LOGGER.info("move file "+savedFile + " from "+ downloadFileName + " to " + outputFolderPath);
//                                    boolean moved=CustomFileUtils.moveFileToDirNew(savedFile, downloadFileName, outputFolderPath);
//                                    LOGGER.info("move file success ?"+moved);
//                                    TimeUnit.SECONDS.sleep(3);
//                                } catch (Exception e) {
//                                    LOGGER.error("ERROR - clickPrepareDownload. companyID: " + "companyID", e);
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.error(ex.getMessage(), ex);
//        }*/
//    }
//
//    public boolean downloadElemCalculFiles(InputData inputData, String companyID, String outputFolderPath,
//                                           SC2Row sc2Row, String year, String clientId) {
//        LOGGER.info("selecting year " + driver.getCurrentUrl());
//        try {
//            TimeUnit.SECONDS.sleep(2 * Constants.DELAY);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        selectYear2020ForElemCalcul();
//        selectYear(outputFolderPath);
//        try {
//            String folder = outputFolderPath + "/" + sc2Row.getRisque() + "/" + sc2Row.getSiret().split(" +")[1] + "/";
//            new File(folder).mkdirs();
//
//            Object[] fileNames = mapFileName.get(sc2Row.getSiret());
//            mapFileName.remove(sc2Row.getSiret());
//
//            if (Files.exists(Paths.get(folder + fileNames[0]))
//                    && Files.exists(Paths.get(folder + fileNames[1]))
//            ) {
//                return true;
//            }
//            return download(folder + fileNames[0], folder + fileNames[1]);
//        } catch (Exception e) {
//            LOGGER.error("ERROR - downloadFirst2Files. companyID: " + companyID + " year: " + year, e);
//        }
//
//        return false;
//    }
//
//    public void select100RecordPerPage() {
//
//        try {
//            WebElement select = driver.findElements(By.tagName("select"))
//                    .stream()
//                    .filter(w -> "2050100".equalsIgnoreCase(w.getText().replaceAll("[\r\n]", "").trim()))
//                    .findFirst()
//                    .orElse(null);
//            if (select != null) {
//                List<WebElement> options = select.findElements(By.tagName("option"));
//
//                for (WebElement option : options) {
//                    String optTxt = option.getText();
//                    if (StringUtils.isNotEmpty(optTxt) && optTxt.contains("100")) {
//                        option.click();
//                        TimeUnit.SECONDS.sleep(Constants.DELAY);
//                        break;
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.error(ex.getMessage(), ex);
//        }
//    }
//    public void selectYear2020ForElemCalcul() {
//        //System.out.println("OPTIONS FOUND!");
//        String cssSelector="#contenu > app-element-calcul-nt > table:nth-child(8) > tbody > tr.centre > td:nth-child(1) > select";
//        //System.out.println(driver.findElement(By.cssSelector(cssSelector)).getText());
//        try {
//            WebElement select = driver.findElements(By.cssSelector(cssSelector))
//                    .stream()
//                    .filter(w -> (w.getText().replaceAll("[\r\n]", "").trim()).contains("2020"))
//                    .findFirst()
//                    .orElse(null);
//           //System.out.println("OPTIONS FOUND!");
//
//            if (select != null) {
//                //System.out.println("OPTIONS FOUND!");
//                List<WebElement> options = select.findElements(By.tagName("option"));
//
//                for (WebElement option : options) {
//                    String optTxt = option.getText();
//                    if (StringUtils.isNotEmpty(optTxt) && optTxt.contains("2020")) {
//                        option.click();
//                        TimeUnit.SECONDS.sleep(Constants.DELAY);
//                        break;
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.error(ex.getMessage(), ex);
//        }
//    }
//    public boolean selectYearAndHitOnRechercher(String currYear, boolean isGoToFeuille, InputData inputData) {
//        return true;
//        /********************************************/
//        /*                                          */
//        /*                                          */
//        /*                                          */
//        /*CRAWLING ALL YEARS TO FILL RATES DASHBOARD*/
//        /*                                          */
//        /*                                          */
//        /*                                          */
//        /********************************************/
////        boolean success = false;
////        int maxTries=5;
////        int tryCount=0;
////        LOGGER.info("current URL selectYearAndHitOnRechercher :"+driver.getCurrentUrl());
////        while(tryCount++<maxTries & !success) {
////            try {
////                TimeUnit.SECONDS.sleep(3);
////                WebElement exerciceEl = driver.findElement(
////                        By.cssSelector("#contenu > table > tbody > tr:nth-child(2) > td:nth-child(1) > select"));
////                List<WebElement> options = exerciceEl.findElements(By.tagName("option"));
////
////                for (WebElement option : options) {
////                    String optTxt = option.getText();
////                    if (StringUtils.isNotEmpty(optTxt) && optTxt.contains(String.valueOf(currYear))) {
////                        option.click();
////                        //TimeUnit.SECONDS.sleep(Constants.DELAY);
////                        success = true;
////                        break;
////                    }
////                }
////                this.takeScreenshot("LOG/codeRisqueSetup-" + UtilsMeth.getRandomHexString(5));
////                TimeUnit.SECONDS.sleep(Constants.DELAY);
////            } catch (Exception e) {
////                LOGGER.info("page url =" + driver.getCurrentUrl());
////                LOGGER.info("page content=" + Jsoup.parse(driver.getPageSource()).text());
////                LOGGER.info("POTENTIAL TOKEN ISSUE SPOTTED!");
////                //reLoginInNewTab(inputData);
////                //driver.navigate().refresh();
////                try {
////                    TimeUnit.SECONDS.sleep(2);
////                } catch (InterruptedException interruptedException) {
////                    interruptedException.printStackTrace();
////                }
////                takeScreenshot("LOG/post-screenshot-sc2-2022-" + UtilsMeth.getRandomHexString(10));
////                LOGGER.error("ERROR - selectYearAndHitOnSearch. currYear: " + currYear, e);
////            }
////        }
////        return success;
//    }
//
//    public boolean downloadFiles(InputData inputData,
//                                 String companyID, String outputFolderPath,
//                                 boolean isAdminSuper,
//                                 String clientID) {
//
//        LOGGER.info("downloadFiles IS_ADMIN_SUPER: " + isAdminSuper + " clientID: " + clientID);
//        String correctUrl = "https://www.teleservices.cnav.fr/ceweb/";
//
//        if (!driver.getCurrentUrl().startsWith(correctUrl)
//                && !driver.getCurrentUrl().startsWith("https://www.teleservices.cnav.fr/CeWeb/rechercheCompte.htm")) {
//            LOGGER.info("erreur mauvaise URL pour DL PDF/CSV");
//            LOGGER.info(driver.getCurrentUrl());
//            return false;
//        }
//
//        String companyCustomName = inputData.getCompanyCustomName();
//        if (companyCustomName.length() == 0) {
//            companyCustomName = companyID;
//        }
//
//        try {
//
//            String date = UtilsMeth.getDay() + "-" + UtilsMeth.getMonth() + "-" + UtilsMeth.getYear();
//            String folder = outputFolderPath + "" + companyCustomName + "/";
//            folder = folder.replace("{DATE}", date);
//            new File(folder).mkdirs();
//
//            String pdfPath = folder + "CEC-" + companyID + "-" + date + ".pdf";
//            String csvPath = folder + "CEC-" + companyID + "-" + date + ".csv";
//
//            LOGGER.info("URL*******************" + driver.getCurrentUrl());
//            // below call creates pdf+CSV (csv from API call )
//            boolean download = downloadSC1NewUI(pdfPath, csvPath, companyID);
//
//            if (download && Constants.IS_COMMAND_LINE_VERSION && Constants.IS_API_VERSION) {
//                boolean resultPushCSVDB = false;
//                if (StringUtils.isNotBlank(System.getenv("API_VERSION")) && System.getenv("API_VERSION").equals("OLD")) {
//                    resultPushCSVDB = UtilsAPI.submitCSVtoDBOld(csvPath, companyID, LOGGER, isAdminSuper, clientID);
//                } else {
//                    resultPushCSVDB = UtilsAPI.submitCSVtoDB(csvPath, companyID, LOGGER, isAdminSuper, clientID);
//                }
//                if (System.getenv("SMPL_PDF_OFF") == null) {
//                    boolean resultPushPDFDB = false;
//                    if (StringUtils.isNotBlank(System.getenv("API_VERSION")) && System.getenv("API_VERSION").equals("OLD")) {
//                        resultPushPDFDB = UtilsAPI.submitPDFtoDBOld(pdfPath, companyID, LOGGER, isAdminSuper, clientID);
//                    } else {
//                        resultPushPDFDB = UtilsAPI.submitPDFtoDB(pdfPath, companyID, LOGGER, isAdminSuper, clientID);
//                    }
//
//                }
//                boolean resultPushCSVFileDB = false;
//                if (StringUtils.isNotBlank(System.getenv("API_VERSION")) && System.getenv("API_VERSION").equals("OLD")) {
//                    resultPushCSVFileDB = UtilsAPI.submitATMPCSVtoDBOld(csvPath, companyID, LOGGER, isAdminSuper, clientID);
//                } else {
//                    resultPushCSVFileDB = UtilsAPI.submitATMPCSVtoDB(csvPath, companyID, LOGGER, isAdminSuper, clientID);
//                }
//                Files.deleteIfExists(Paths.get(csvPath));
//                Files.deleteIfExists(Paths.get(pdfPath));
//            }
//
//            /* send Status */
//            if (!download) {
//                FileUtils.deleteDirectory(new File(folder));
//                TimeUnit.SECONDS.sleep(1);
//            }
//
//            return download;
//        } catch (Exception e) {
//            LOGGER.error("ERROR - downloadFiles. companyID: " + companyID, e);
//        }
//
//        return false;
//    }
//
//    private boolean download(String pdfPath, String csvPath) throws IOException {
//
//        /* PREPARING THE FILES */
//        LOGGER.info("*** trying download "+driver.getCurrentUrl());
//
//        Files.deleteIfExists(Paths.get(Constants.DOWNLOAD_DEFAULT_DIRECTORY + "/feuille_calcul.csv"));
//        Files.deleteIfExists(Paths.get(Constants.DOWNLOAD_DEFAULT_DIRECTORY + "/Element-calculs-taux.pdf"));
//
//        /* DOWNLOADING PDF FROM UI BY CLICKING ON BUTTON + COPY TO PATH */
//        if (driver.findElements(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_PDF)).isEmpty()) {
//            LOGGER.info("EXITING DOWNLOAD "+driver.getCurrentUrl()+"--"+Jsoup.parse(driver.getPageSource()).text());
//            return false;
//        }
//
//        driver.findElement(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_PDF)).click();
//        try {
//            Thread.sleep(5000);
//            Optional<File> pdf = CustomFileUtils.getLatestDownloadedFile();
//
//            if (!pdf.isPresent() || !pdf.get().getName().toLowerCase().contains(".pdf")) {
//                LOGGER.info("PDF NOT FOUND AFTER DOWNLOAD "+driver.getCurrentUrl()+"--"+Jsoup.parse(driver.getPageSource()).text());
//                return false;
//            }
//
//            CustomFileUtils.moveFileToDir(pdf.get(), pdfPath);
//
//            /* DOWNLOADING CSV FROM UI BY CLICKING ON BUTTON + COPY TO PATH */
//            if (driver.findElements(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_CSV)).isEmpty()) {
//                LOGGER.info("CSV BUTTON NOT FOUND FOR DOWNLOAD");
//                return false;
//            }
//            driver.findElement(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_CSV)).click();
//            Thread.sleep(5000);
//            Optional<File> csv = CustomFileUtils.getLatestDownloadedFile();
//            if (csv.isPresent())
//                CustomFileUtils.moveFileToDir(csv.get(), csvPath);
//
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//        return true;
//    }
//
//    private boolean downloadSC1NewUI(String pdfPath, String csvPath, String companyId) throws IOException, InterruptedException {
//
//        /* PREPARING THE FILES */
//        LOGGER.info("*** trying download");
//        Files.deleteIfExists(Paths.get("compte_courant.csv"));
//        Files.deleteIfExists(Paths.get("compte_employeur_courant.pdf"));
//
//        boolean copyCSV = false;
//        /* DOWNLOADING CSV FROM UI BY CLICKING ON BUTTON */
//        if (driver.findElements(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_PDF)).isEmpty())
//            return false;
//        driver.findElement(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_PDF)).click();
//        Thread.sleep(5000);
//
//        Optional<File> pdf = CustomFileUtils.getLatestDownloadedFile();
//        if (!pdf.isPresent() || !pdf.get().getName().toLowerCase().contains(".pdf")) {
//            return false;
//        }
//
//        CustomFileUtils.moveFileToDir(pdf.get(), pdfPath);
//
//        /* DOWNLOADING CSV FROM UI BY CLICKING ON BUTTON */
//        if (driver.findElements(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_CSV)).isEmpty())
//            return false;
//        driver.findElement(By.cssSelector(STR_ELEMENT_SELECTOR_IMG_ALT_CSV)).click();
//        Thread.sleep(5000);
//
//        Optional<File> csv = CustomFileUtils.getLatestDownloadedFile();
//        if (!csv.isPresent() || !csv.get().getName().toLowerCase().contains(".csv")) {
//            return false;
//        }
//
//        CustomFileUtils.moveFileToDir(csv.get(), csvPath);
//        TimeUnit.SECONDS.sleep(Constants.DELAY + 1l);
//
//        /* DOWNLOADING CSV FROM GOVT API */
//        driver.switchTo().defaultContent();
//        String originalWindowId = driver.getWindowHandle();
//        ((JavascriptExecutor) driver).executeScript("window.open()");
//
//        String newTabId = driver.getWindowHandles().stream().filter(window -> !originalWindowId.equals(window))
//                .findAny().orElse("");
//
//        driver.switchTo().window(newTabId);
//
//        String siren = companyId.substring(0, 9);
//        driver.get("https://www.teleservices.cnav.fr/api/v1/query/cptEmplDyn?siren=" + siren);
//        Thread.sleep(5000);
//
//        String atmpJson = driver.findElement(By.cssSelector("pre")).getText();
//
//        /*LOGGER.info(atmpJson);*/
//        List<AtmpApiResponse> atmpsList = new ArrayList<>(0);
//
//        if (StringUtils.isNotBlank(atmpJson)) {
//            atmpsList = new ObjectMapper().readValue(atmpJson, new TypeReference<List<AtmpApiResponse>>() {
//            });
//        }
//
//        driver.close();
//        driver.switchTo().window(originalWindowId);
//
//        if (Objects.isNull(atmpsList)) {
//            LOGGER.error("Error while fetching atmps from endpoint");
//            return false;
//        } else {
//            AtmpApiResponseToCSVMapper atmpApiResponseToCSVMapper = new AtmpApiResponseToCSVMapper(atmpsList);
//            copyCSV = atmpApiResponseToCSVMapper.storeAsCsv(csvPath);
//            if (!copyCSV) {
//                LOGGER.error("Error while transforming Or Storing ATMP CSV");
//                return false;
//            }
//        }
//
//        return true;
//        /**
//         * copy the PDF & CSV file from user.dir to pdfpath & csvpath dir, then all
//         * should be alright the app should keep running
//         **/
//    }
//
//    public void navigateToGivenPage(int currPage) {
//
//        try {
//
//            List<WebElement> aEls = driver.findElements(By.tagName("a"));
//            for (WebElement aEl : aEls) {
//                if ("Taux AT/MP".equals(aEl.getText())) {
//                    aEl.click();
//                    TimeUnit.SECONDS.sleep(Constants.DELAY + 1l);
//                    break;
//                }
//            }
//
//            driver.get("https://www.teleservices.cnav.fr/CeWeb/paginerTaux.htm?pager.offset=" + currPage * 10);
//            TimeUnit.SECONDS.sleep(Constants.DELAY);
//        } catch (Exception e) {
//            LOGGER.error("error - navigateBack", e);
//        }
//    }
//
//    private void takeScreenshot(String screenshotName) {
//        try {
//            FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), new File(screenshotName + ".jpg"), true);
//            LOGGER.info(driver.getCurrentUrl());
//        } catch (IOException e) {
//            LOGGER.error("error ", e);
//        }
//    }
//
//    public boolean hasDownloadZip(String companyID) {
//        boolean exists = false;
//        int tryCount = 0;
//        driver.navigate().refresh();
//        String targetUrl = "https://www.teleservices.cnav.fr/ceweb/#/historiqueTaux";
//        do {
//            if (!driver.getCurrentUrl().contains(targetUrl)) {
//                LOGGER.info("WRONG URL HASDOWNLOADZIP " + driver.getCurrentUrl());
//                driver.get(targetUrl);
//            }
//            //getJSONRates(companyID);
//            //exists=true;
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                if (!driver.getPageSource().contains("Préparer le téléchargement de toutes les données")) {
//                    LOGGER.info("TEXT ABSENT PREPARE ZIP FILE " + driver.getCurrentUrl());
//                    exists = false;
//                    break;
//                }
//                exists = driver.findElement(By.cssSelector("a.csv")) != null;
//                LOGGER.info("Preparer ZIP file download -- identified");
//            } catch (Exception e) {
//                LOGGER.error("ERROR - hasDownloadZip. companyID: " + companyID, e);
//                exists = false;
//            }
//        } while (tryCount++ <= 2 & !exists);
//        return exists;
//    }
//
//    public void clickPrepareDownloadSimple(String companyID) {
//        boolean exists = false;
//        int count = 0;
//        int tryCount = 0;
//        clickOnAccepter();
//        do {
//            try {
//                LOGGER.info("CLICKING PREPARE DOWNLOAD");
//                takeScreenshot("LOG/prepare.jpg");
//                TimeUnit.SECONDS.sleep(3);
//                driver.findElement(By.cssSelector("a.csv")).click();
//                LOGGER.info("WAITING POST CLICK");
//                exists = driver.getPageSource().contains("Télécharger le zip de toutes les données");
//                int newCount = 0;
//                while (!exists) {
//                    LOGGER.info("waiting download button");
//                    TimeUnit.SECONDS.sleep(7);
//                    newCount++;
//                    if (newCount >= 5 & !exists) {
//                        driver.navigate().refresh();
//                        TimeUnit.SECONDS.sleep(5);
//                        driver.findElement(By.cssSelector("a.csv")).click();
//                        TimeUnit.SECONDS.sleep(5);
//                        exists = driver.getPageSource().contains("Télécharger le zip de toutes les données");
//                    }
//                    if (driver.getPageSource().contains("En cours de")) {
//                        count++;
//                        if (count >= 5) {
//                            //remettre en statut "prepare"
//                            driver.navigate().refresh();
//                            TimeUnit.SECONDS.sleep(5);
//                            driver.findElement(By.cssSelector("a.csv")).click();
//                        }
//                    } else {
//                        driver.findElement(By.cssSelector("a.csv")).click();
//                    }
//                    exists = driver.getPageSource().contains("Télécharger le zip de toutes les données");
//                }
//            } catch (Exception e) {
//                LOGGER.error("ERROR - clickPrepareDownload. companyID: " + companyID, e);
//                takeScreenshot("error-click-hasprepare");
//            }
//        } while (tryCount++ <= 15 & !exists);
//    }
//
//
//    public boolean downloadPreparedFiles(String downloadFileName,String downloadFilePath,boolean isSimple) {
//        /* à changer */
//        boolean success = false;
//        LOGGER.info("DOWNLOADING PREPARED ZIP FILES");
//        String cssSelector = "img[title='Format CSV']";
//
//        if (downloadFileName.contains("zip")) {
//            cssSelector = "a.csv";
//        }
//        LOGGER.info("cssSelector=" + cssSelector);
//        int tryCount = 0;
//        File savedZip = CustomFileUtils.getLatestDownloadedFileNew(Constants.DOWNLOAD_DEFAULT_DIRECTORY);
//        do {
//            tryCount++;
//            try {
//                TimeUnit.SECONDS.sleep(3);
//                boolean exists = driver.findElement(By.cssSelector(cssSelector)) != null;
//                if (exists) {
//                    LOGGER.info("Download button read to be clicked -- FILE FOUND = " + (cssSelector.contains("a.csv") ? "ZIP" : "CSV"));
//                }
//                new File("taux.csv").delete();
//                driver.findElement(By.cssSelector(cssSelector)).click();
//                TimeUnit.SECONDS.sleep(3);
//                savedZip = CustomFileUtils.getLatestDownloadedFileNew(Constants.DOWNLOAD_DEFAULT_DIRECTORY);
//                LOGGER.info(savedZip.getAbsolutePath());
//                if (savedZip.getName().contains("csv") || savedZip.getName().contains("zip")) {
//                    LOGGER.info("last downloaded file=" + savedZip.getAbsolutePath());
//                    if(true) {
//                        TimeUnit.SECONDS.sleep(10);
//                        LOGGER.info("move file "+savedZip +" from "+downloadFileName +" "+savedZip.exists());
//                        boolean moved=CustomFileUtils.moveFileToDirNew(savedZip, downloadFileName,downloadFilePath);
//                        //new File("taux.csv").delete();
//                        LOGGER.info("move file success ?"+moved);
//                    }
//                    success = true;
//                }
//                LOGGER.info("WAITING POST CLICK " + downloadFileName +" "+ tryCount);
//                TimeUnit.SECONDS.sleep(3);
//            } catch (Exception e) {
//                LOGGER.error("ERROR - clickPrepareDownload. companyID: " + "companyID", e);
//            }
//        } while (!success & tryCount <= 10);
//        return success;
//    }
//
//
//    public boolean passwordWillExpire() {
//        return this.status.contains("expir");
//    }
//
//    public String getStatus() {
//        return this.status;
//    }
//
//    public void goback() {
//        ((JavascriptExecutor) driver).executeScript("history.back()");
//    }
//
//    public void goback(String url) {
//
//        int count = 0;
//        while (true) {
//            count++;
//            if (driver.getCurrentUrl().equalsIgnoreCase(url) || count > 10) {
//                break;
//            }
//
//            ((JavascriptExecutor) driver).executeScript("history.back()");
//            try {
//                Thread.sleep(5000l);
//            } catch (Exception e) {
//                LOGGER.error(e.getMessage(), e);
//            }
//        }
//    }
//
//	public void clearBrowsingData() {
//
//        if (driver != null) {
//            try {
//                ((RemoteWebDriver)driver).manage().deleteAllCookies();
//                ChromeDriver chromeDriver  = (ChromeDriver) driver;
//                chromeDriver.getDevTools().createSessionIfThereIsNotOne();
//                chromeDriver.getDevTools().send(Network.clearBrowserCookies());
//                chromeDriver.getDevTools().send(Network.clearBrowserCache());
//                Thread.sleep(500l);
//            } catch (Exception e) {
//                LOGGER.error(e.getMessage(), e);
//            }
//        }
//    }
}
