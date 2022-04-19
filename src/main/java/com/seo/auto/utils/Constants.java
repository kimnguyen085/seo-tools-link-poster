package main.java.com.seo.auto.utils;

import org.apache.commons.lang3.SystemUtils;

public class Constants {

    public static final int NUMBER_OF_CONCURRENT_TASKS = 3;
    /*ENV VAR*/
    public static final boolean IS_MAC = SystemUtils.IS_OS_MAC_OSX;
    public static final String PHANTOM_JS_PATH = "chromedrivers/macos/chromedriver"; // chromedrivers/macos/chromedriver
    public static final String PHANTOM_JS_WINDOW_PATH = "chromedrivers/window/chromedriver.exe";
    public static final String ATMP_SIRET_FILTER=System.getenv("SMPL_ATMP_SIRET_FILTER");

    public static final String DOWNLOAD_DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/downloads";
    public static final String SCREENSHOTS_DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/screenshots";
    public static final String CREDENTIALS_DEFAULT_DIRECTORY_FILE = System.getProperty("user.dir") + "/credentials";

}
