package main.java.com.seo.auto.utils;

public class Constants {

    public static final int NUMBER_OF_CONCURRENT_TASKS = 3;
    /*ENV VAR*/
    public static final String API_ADMIN_SUPER_KEY = System.getenv("SMPL_ADMIN_SUPER_APIKEY");
    public static final String API_ADMIN_SUPER_SECRET = System.getenv("SMPL_ADMIN_SUPER_APISECRET");
    public static final String API_HOST = System.getenv("SMPL_HOST");
    public static final String API_PORT = System.getenv("SMPL_PORT");
    public static final String API_PREFIX = System.getenv("SMPL_PREFIX");
    public static final String PHANTOM_JS_PATH = System.getenv("SMPL_PHANTOM_JS_PATH");
    public static final String ATMP_SIRET_FILTER=System.getenv("SMPL_ATMP_SIRET_FILTER");

    public static final String DOWNLOAD_DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/downloads";
    public static final String SCREENSHOTS_DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/screenshots";
    public static final boolean isSc2Normal=false;

}
