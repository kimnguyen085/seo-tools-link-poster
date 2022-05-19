package main.java.com.seo.auto.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import main.java.com.seo.auto.model.GoogleCredentialsConfig;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Example from https://developers.google.com/sheets/api/quickstart/java
 */
public class PermissionUsageUtils {

    private static final String GOOGLE_BASE_PATH = System.getProperty("user.dir") + "/licenseServer";
    private static final String APPLICATION_NAME = "Seo Tools link poster";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = GOOGLE_BASE_PATH + "/tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = GOOGLE_BASE_PATH + "/credentials.json";
    private static final String CREDENTIALS_CONFIG_FILE_PATH = GOOGLE_BASE_PATH + "/config.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static List<List<Object>> retriveSpreadSheetFile() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        final String spreadsheetId = "1kCD7chffG-fKAbYoMdxUqTN1rX1yS9exaA2DI6ypj30";
//        final String spreadsheetId = "16JpT3Q2eaaagZ349RC98mCI47npiqvzPL_Xdw-AX3Lg";
//        final String range = "Sheet1!A:A"; // https://developers.google.com/sheets/api/guides/concepts#cell

        ObjectMapper mapper = new ObjectMapper();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        GoogleCredentialsConfig configs = mapper.readValue(UtilsMeth.readTxtFile(CREDENTIALS_CONFIG_FILE_PATH), GoogleCredentialsConfig.class);
        ValueRange response = service.spreadsheets().values()
                .get(configs.getSpreadsheetId(), configs.getRange())
                .execute();
        List<List<Object>> values = response.getValues();
        return values;
    }

    public static boolean checkPermission() throws IOException, GeneralSecurityException {
        List<List<Object>> values = retriveSpreadSheetFile();
        if (values == null || values.isEmpty()) {
            return false;
        } else {
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                if (row.get(0) != null && row.get(0).toString().getBytes().length > 2 &&
                        UtilsMeth.decrypt((String)row.get(0)).equalsIgnoreCase(UtilsMeth.getMacAddress())){
                    return true;
                }
            }
            return false;
        }
    }

//    public static void main(String[] args) throws Exception{
//        System.out.println(checkPermission()+"  permitted");
////        System.out.println(UtilsMeth.encrypt(UtilsMeth.getMacAddress()));
//    }
}
