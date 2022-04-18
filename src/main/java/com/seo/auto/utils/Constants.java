package main.java.com.seo.auto.utils;

public class Constants {

    /*ENV VAR*/
    public static String API_ADMIN_SUPER_KEY = System.getenv("SMPL_ADMIN_SUPER_APIKEY");
    public static String API_ADMIN_SUPER_SECRET = System.getenv("SMPL_ADMIN_SUPER_APISECRET");
    public static String API_HOST = System.getenv("SMPL_HOST");
    public static String API_PORT = System.getenv("SMPL_PORT");
    public static String API_PREFIX = System.getenv("SMPL_PREFIX");
    public static String PHANTOM_JS_PATH = System.getenv("SMPL_PHANTOM_JS_PATH");
    public static String ATMP_SIRET_FILTER=System.getenv("SMPL_ATMP_SIRET_FILTER");

    public static final String DOWNLOAD_DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/downloads";
    public static final String SCREENSHOTS_DEFAULT_DIRECTORY = System.getProperty("user.dir") + "/screenshots";
    public static boolean isSc2Normal=false;

}
