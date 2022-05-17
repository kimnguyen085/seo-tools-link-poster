package main.java.com.seo.auto.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.seo.auto.bot.*;
import main.java.com.seo.auto.model.Credential;
import main.java.com.seo.auto.model.Profile;
import main.java.com.seo.auto.utils.Constants;
import main.java.com.seo.auto.utils.UtilsMeth;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppDataManipulator {
    private static final Logger LOGGER = Logger.getLogger(AppDataManipulator.class);
    private static final String CSV_SEPARATOR = ",";
    public static Profile activeProfile;

    public static AppData appData = new AppData();

    public static void applyActiveProfile() {
        BczBot.usrName = activeProfile.getBczCredentials().getUsrName();
        BczBot.pwd = activeProfile.getBczCredentials().getPwd();
        ElloCoBot.usrName = activeProfile.getEllocoCredentials().getUsrName();
        ElloCoBot.pwd = activeProfile.getEllocoCredentials().getPwd();
        FlipboardBot.usrName = activeProfile.getFlipboardCredentials().getUsrName();
        FlipboardBot.pwd = activeProfile.getFlipboardCredentials().getPwd();
        GetPocketBot.usrName = activeProfile.getGetPocketCredentials().getUsrName();
        GetPocketBot.pwd = activeProfile.getGetPocketCredentials().getPwd();
        InstapaperBot.usrName = activeProfile.getInstapaperCredentials().getUsrName();
        InstapaperBot.pwd = activeProfile.getInstapaperCredentials().getPwd();
        ScoopItBot.usrName = activeProfile.getScoopitCredentials().getUsrName();
        ScoopItBot.pwd = activeProfile.getScoopitCredentials().getPwd();
        TumblrBot.usrName = activeProfile.getTumblrCredentials().getUsrName();
        TumblrBot.pwd = activeProfile.getTumblrCredentials().getPwd();
        VingleBot.usrName = activeProfile.getVingleCredentials().getUsrName();
        VingleBot.pwd = activeProfile.getVingleCredentials().getPwd();
        WordpressBot.usrName = activeProfile.getWpCredentials().getUsrName();
        WordpressBot.pwd = activeProfile.getWpCredentials().getPwd();
        FolkdBot.usrName = activeProfile.getFolkdCredentials().getUsrName();
        FolkdBot.pwd = activeProfile.getFolkdCredentials().getPwd();
    }

    public static void loadData() {
        try {
            String txtCredentials = UtilsMeth.readFile(Constants.APP_DATA_DEFAULT_DIRECTORY_FILE);
            if (txtCredentials.isEmpty()) return;
            ObjectMapper mapper = new ObjectMapper();
            appData = mapper.readValue(txtCredentials, AppData.class);

            if (appData != null && !appData.getProfiles().isEmpty()) {
                activeProfile = appData.getProfiles().get(0);
            }
            applyActiveProfile();

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UtilsMeth.writeFile(Constants.APP_DATA_DEFAULT_DIRECTORY_FILE, mapper.writeValueAsString(appData));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeActiveProfile(String name) {
        List<Profile> profiles = appData.getProfiles();
        if (profiles == null) {
            return;
        }

        activeProfile = profiles.stream().filter(p -> p.getName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
        applyActiveProfile();
    }

    public static void createProfile(String name) {
        List<Profile> profiles = appData.getProfiles();
        if (profiles == null) {
            profiles = new ArrayList<>();
        }

        Profile toBeCreated = initialiseEmptyProfile(name);
        profiles.add(toBeCreated);
        appData.setProfiles(profiles);
    }

    private static Profile initialiseEmptyProfile(String name) {
        Profile profile = new Profile(name);

        profile.setBczCredentials(new Credential("bcz.com", "", ""));
        profile.setEllocoCredentials(new Credential("ello.co", "", ""));
        profile.setFlipboardCredentials(new Credential("flipboard.com", "", ""));
        profile.setFolkdCredentials(new Credential("folkd.com", "", ""));
        profile.setWpCredentials(new Credential("wordpress.com", "", ""));
        profile.setGetPocketCredentials(new Credential("getpocket.com", "", ""));
        profile.setInstapaperCredentials(new Credential("www.instapaper.com", "", ""));
        profile.setScoopitCredentials(new Credential("www.scoop.it", "", ""));
        profile.setVingleCredentials(new Credential("www.vingle.net", "", ""));
        profile.setTumblrCredentials(new Credential("www.tumblr.com", "", ""));

        return profile;
    }

    public static void modifyProfile(String profileName) {
        Profile profile = new Profile(profileName);

        profile.setBczCredentials(new Credential("bcz.com", BczBot.usrName, BczBot.pwd));
        profile.setEllocoCredentials(new Credential("ello.co", ElloCoBot.usrName, ElloCoBot.pwd));
        profile.setFlipboardCredentials(new Credential("flipboard.com", FlipboardBot.usrName, FlipboardBot.pwd));
        profile.setFolkdCredentials(new Credential("folkd.com", FolkdBot.usrName, FolkdBot.pwd));
        profile.setWpCredentials(new Credential("wordpress.com", WordpressBot.usrName, WordpressBot.pwd));
        profile.setGetPocketCredentials(new Credential("getpocket.com", GetPocketBot.usrName, GetPocketBot.pwd));
        profile.setInstapaperCredentials(new Credential("www.instapaper.com", InstapaperBot.usrName, InstapaperBot.pwd));
        profile.setScoopitCredentials(new Credential("www.scoop.it", ScoopItBot.usrName, ScoopItBot.pwd));
        profile.setVingleCredentials(new Credential("www.vingle.net", VingleBot.usrName, VingleBot.pwd));
        profile.setTumblrCredentials(new Credential("www.tumblr.com", TumblrBot.usrName, TumblrBot.pwd));

        List<Profile> profiles = appData.getProfiles();
        if (profiles == null) {
            profiles = new ArrayList<>();
            profiles.add(profile);
        } else {
            appData.setProfiles(profiles.stream().map(p -> {
                if (p.getName().equalsIgnoreCase(profile.getName()))
                    return profile;
                return p;
            }).collect(Collectors.toList()));
        }

        AppDataManipulator.writeData();
    }

    public static void deleteProfile(String name) {
        List<Profile> profiles = appData.getProfiles();
        if (profiles == null) {
            return;
        }

        appData.setProfiles(profiles.stream().filter(p -> !p.getName().equalsIgnoreCase(name)).collect(Collectors.toList()));
    }

    public static Object[] getAllProfileNames() {
        List<Profile> profiles = appData.getProfiles();
        if (profiles == null) {
            return new String[0];
        }
        Object[] allProfileName = (Object[]) profiles.stream().map(p -> p.getName()).toArray();
        return allProfileName;
    }

    /**
     * CSV columns order is: name,url, username, password
     *
     * @param csvPath
     * @param profileName
     */
    public static void importPwdFromCsv(String csvPath, String profileName) {
        try {
            ArrayList<List> arList;
            ArrayList al;
            String thisLine;
            arList = new ArrayList<List>();

            FileInputStream fis = new FileInputStream(csvPath);
            DataInputStream myInput = new DataInputStream(fis);
            while ((thisLine = myInput.readLine()) != null) {
                al = new ArrayList();
                String[] strar = thisLine.split(CSV_SEPARATOR);
                al.addAll(Arrays.asList(strar));
                arList.add(al);
            }

            Profile profile = new Profile(profileName);
            // process import
            arList.stream().forEach(line -> {
                String url = line.get(1).toString();
                if (url.contains("bcz.com")) {
                    profile.setBczCredentials(new Credential("bcz.com", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("ello.co")) {
                    profile.setEllocoCredentials(new Credential("ello.co", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("flipboard.com")) {
                    profile.setFlipboardCredentials(new Credential("flipboard.com", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("folkd.com")) {
                    profile.setFolkdCredentials(new Credential("folkd.com", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("wordpress.com")) {
                    profile.setWpCredentials(new Credential("wordpress.com", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("getpocket.com")) {
                    profile.setGetPocketCredentials(new Credential("getpocket.com", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("www.instapaper.com")) {
                    profile.setInstapaperCredentials(new Credential("www.instapaper.com", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("www.scoop.it")) {
                    profile.setScoopitCredentials(new Credential("www.scoop.it", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("www.vingle.net")) {
                    profile.setVingleCredentials(new Credential("www.vingle.net", line.get(2).toString(), line.get(3).toString()));
                }
                if (url.contains("www.tumblr.com")) {
                    profile.setTumblrCredentials(new Credential("www.tumblr.com", line.get(2).toString(), line.get(3).toString()));
                }
            });

            activeProfile = profile;
            applyActiveProfile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
