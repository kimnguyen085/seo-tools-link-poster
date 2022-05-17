package main.java.com.seo.auto.utils;

import main.java.com.seo.auto.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.ZoneId;
import java.util.*;

public class UtilsMeth {

    private static final Logger LOGGER = Logger.getLogger(UtilsMeth.class);
    private static SecretKeyFactory keyFactory = null; // Get the secret key factor for generating DESede keys;
    private static String cryptographyKey = "wYu@U%b5R*n$L&xth!S243`RN;HT;bu";
    private static final IvParameterSpec params = new IvParameterSpec(new byte[] {7, 14, 21, 28, 9, 18, 27, 36} ); // Create an initialization vector (necessary for CBC mode);

    static {
        try {
            keyFactory = SecretKeyFactory.getInstance("DESede");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static int getYear() {
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    }

    public static int getMonth() {
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
    }

    public static int getDay() {
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
    }

    public static int getHour() {
        return GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);   // gets hour in 12h format
    }

    public static int getMinute() {
        return GregorianCalendar.getInstance().get(Calendar.MINUTE);   // gets hour in 12h format
    }

    public static int getSecond() {
        return GregorianCalendar.getInstance().get(Calendar.SECOND);   // gets hour in 12h format
    }

    public static boolean downloadFile(String downloadUrl, String cookie, String path) {
        //trusting all certs for downloads
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            URLConnection conn = new URL(downloadUrl).openConnection();
            conn.setRequestProperty("Cookie", cookie);

            try (InputStream inputStream = conn.getInputStream()) {
                try (final FileOutputStream fos = new FileOutputStream(new File(path))) {
                    fos.getChannel().transferFrom(Channels.newChannel(inputStream), 0, Long.MAX_VALUE);
                    return true;
                }
            }
        } catch (Exception ex) {
            LOGGER.info("ERROR grave- downloadFile. downloadUrl: " + downloadUrl+" "+ex.getMessage());
            return false;
        }
    }
    public static boolean copy(File cfgFilePath, String strTarget ){
        Path from = cfgFilePath.toPath(); //convert from File to Path
        Path to = Paths.get(strTarget); //convert from String to Path
        try {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("COPY ERROR");
            return false;
        }
        return true;
    }
    public static String removeLastChar(String str, String character) {
        if (str.endsWith(character)) {
            str = StringUtils.substringBeforeLast(str, character);
        }

        return str;
    }
    public static String getDateFromMillis(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return mDay+"/"+mMonth+"/"+mYear;
    }
    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static byte[] convertData(Integer mode, final byte[] input) throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        final DESedeKeySpec spec = new DESedeKeySpec(cryptographyKey.getBytes()); // Create a DESede key spec from the key
        final SecretKey key = keyFactory.generateSecret(spec); // Generate a DESede SecretKey object
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding"); // Create a DESede Cipher
        cipher.init(mode, key, params);  //Initialize the cipher and put it in decrypt mode
        final byte[] convertedData = cipher.doFinal(input); // Decrypt the data
        return convertedData;
    }

    public static String encrypt(String txt) {
        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(com.seo.auto.utils.Constants.KEY.getBytes("UTF-8"), "AES"), new IvParameterSpec(atmp.main.java.utils.Constants.INIT_VECTOR.getBytes("UTF-8")));
//            return Base64.encodeBase64String(cipher.doFinal(txt.getBytes()));
            final byte[] input = txt.getBytes();

            try {
                final byte[] convertedData = convertData(Cipher.ENCRYPT_MODE, input);
                return Base64.getEncoder().encodeToString(convertedData);
            } catch (final Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new RuntimeException();
            }
        } catch (Exception e) {
            LOGGER.error("ERROR - encrypt. TXT: " + txt, e);
        }

        return "";
    }

    public static String decrypt(String encrypted) {
        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(atmp.main.java.utils.Constants.KEY.getBytes("UTF-8"), "AES"), new IvParameterSpec(atmp.main.java.utils.Constants.INIT_VECTOR.getBytes("UTF-8")));
//            return new String(cipher.doFinal(Base64.decodeBase64(encrypted)));
            final byte[] input = Base64.getDecoder().decode(encrypted);
            try {
                final byte[] convertedData = convertData(Cipher.DECRYPT_MODE, input);
                return new String(convertedData);
            } catch (final IllegalBlockSizeException ex) {
                LOGGER.error("Error while decrypting database value. It seems the data was not base64 encoded. You can ignore this exception");
                throw new RuntimeException();
            } catch (final Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new RuntimeException();
            }
        } catch (Exception e) {
            LOGGER.error("ERROR - decrypt. TXT: " + encrypted, e);
        }

        return "";
    }

    public static void writeFile(String path, String data) {
        try {
            data = encrypt(data);
            Files.write(Paths.get(path), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath) throws IOException {
        String data = FileUtils.readFileToString(new File(filePath), String.valueOf(StandardCharsets.UTF_8));
        return decrypt(data);
//        return data;
    }

    public static String getLicenseUrlFromFile(String filename) {
        String url = "";
        File file = new File(filename);

        try (FileReader reader = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            String licenseUrl = StringUtils.substringAfter(new String(chars), "http").trim();

            if (licenseUrl.contains(" ")) {
                licenseUrl = StringUtils.substringBefore(licenseUrl, " ").trim();
            }

            return "http" + licenseUrl;
        } catch (Exception e) {
            LOGGER.error("ERROR - getLicenseUrlFromFile. name: " + filename, e);
        }

        return url;
    }


    static String randInt(int min, int max) {
        return Integer.toString(new Random().nextInt((max - min) + 1) + min);
    }

    public static HashMap initEtatSiret(int num) {
        HashMap<Integer, List<String>> h = new HashMap<>();

        for (int i = 0; i < num; i++) {
            ArrayList<String> list = new ArrayList<>();
            h.put(i, list);
        }

        return h;
    }

    static String getDateAsMillis(String inputDate) {
        if (inputDate.length() == 0) {
            return null;
        } else {
            return inputDate.substring(6, 10) + "-" + inputDate.substring(3, 5) + "-" + inputDate.substring(0, 2);
        }
    }

    public static String getFileAsString(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();
        return fileAsString;
    }

    public static int getStatusCodeUrl(String urlParam) {
        URL url = null;
        try {
            url = new URL(urlParam);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int code = 0;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("code = " + code);
        return code;
    }

    public static boolean inspect(String csvPath) {
        String csvString = null;
        try {
            csvString = getFileAsString(new File(csvPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean resultat = !csvString.contains("Votre session a");//csvString.contains("Bureau;CTN;NNS;Nom;Prenom;Type");
        return resultat;
    }

    public static void consolidate(String outputFolderPath) {
        try {
            FileUtils.deleteDirectory(new File(outputFolderPath+"/consolidation"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse(outputFolderPath);
    }
    public static void main(String[] args){
        consolidate("C:\\Users\\pc\\IdeaProjects\\atmp-bpij\\sortie\\TauxAT-MP\\17-1-2020-22H-18M-33s");
    }
    public static void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    //System.out.println(file);
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
        }
    }

    public static ArrayList<File> parse(String dir_name) {
        File des_dir = new File(dir_name + "/consolidation");
        if (!des_dir.exists()) {
            des_dir.mkdir();
        }
        File directory = new File(dir_name);
        ArrayList<File> resultList = new ArrayList<File>();
        List<File> files=new ArrayList<>();
        listf(dir_name,files);
        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        String outputFile="SIRET;TYPE TAUX;VALEUR TAUX";
        try {
            for (File file : files) {
                //System.out.println(file.getName());
                if (file.isFile() && file.getName().contains("FeuilleDeCalcul") && !file.getName().contains(".pdf")) {
                    Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(des_dir.getAbsolutePath(), file.getName()));
                    String typeTaux = parsingExcel(file, 1);
                    String valeurTaux = parsingExcel(file, 2);
                    String effectif=parsingExcel(file,3);
                    outputFile=outputFile+"\n"+(file.getName().split("-")[1]+";"+typeTaux+";"+valeurTaux+";"+effectif);
                } else if (file.isDirectory()) {
                    resultList.addAll(parse(file.getAbsolutePath()));
                }
            }
            try (PrintWriter out = new PrintWriter(dir_name + "/consolidation/output.csv")) {
                out.println(outputFile);
            }
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
        } finally {
            return resultList;
        }
    }

    public static String parsingExcel(File excelFile, int type) {
        String typeTaux = "";
        String valeurTaux = "";
        String effectif="";
        try {
            BufferedReader br = new BufferedReader(new FileReader(excelFile));
            int cour = 0;
            while (br.ready()) {
                cour++;
                String check = br.readLine();
                if (cour == 5) {
                    if (check.contains("Mixte")) {
                        typeTaux = "Taux Mixte";
                    } else {
                        typeTaux = "Taux Individuel";
                    }
                    effectif=check.split(";")[7];
                }
                if(cour==37){
                    //effectif=check.split(";")[1];
                }
                if (cour == 42 && check.contains("Taux Individuel")) {
                    valeurTaux = check.split(";")[1];
                }
                if (cour == 46 && check.contains("Taux Mixte")) {
                    valeurTaux = check.split(";")[1];
                }


            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        switch(type) {
            case 1:
                return typeTaux;
            case 2:
                return valeurTaux;
            case 3:
                return effectif;
            default:
                return "";
                // code block
        }
    }

    public static Double parseDouble(String doubleValue) {
        try {
            return Double.parseDouble(doubleValue);
        } catch (Exception e) {
            return 0d;
        }
    }
    public static void getPeople(String html, String riskCode, String companyID) {
        if(html.contains("Aucune information disponible")){
            LOGGER.info("AUCUNE INFO DISPONIBLE");
            return;
        }
        String cssyearRow = "#contenuAttestation > div > div:nth-child(5)";
        String cssPeopleRow = "#contenuAttestation > div > div:nth-child(6)";
        Elements yearRows = Jsoup.parse(html).select(cssyearRow);
        Elements peopleRows = Jsoup.parse(html).select(cssPeopleRow);
        int nbAnnees = peopleRows.get(0).select(".anneeN1").size();
        int nbYearRows = yearRows.get(0).select(".titre2").size();
        int nb = 0;
        if (yearRows.get(0).select(".titre2").get(nbYearRows - 1).text().contains("20")) {
            nb = 2;
        }
        if (yearRows.get(0).select(".titre2").get(nbYearRows - 2).text().contains("20")) {
            nb = 1;
        }
        System.out.println(nbAnnees + "--" + nbYearRows + "--" + nb);
        String year=yearRows.get(0).select(".titre2").get(nb + 1).text();
        System.out.println(year);
        String nbPeople=peopleRows.get(0).select(".anneeN1").get(nb).text();
        System.out.println(nbPeople);
    }

    }
