package main.java.com.seo.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import main.java.com.seo.auto.bot.*;
import main.java.com.seo.auto.data.AppDataManipulator;
import main.java.com.seo.auto.service.MixpanelService;
import main.java.com.seo.auto.utils.Constants;
import main.java.com.seo.auto.utils.PermissionUsageUtils;
import main.java.com.seo.auto.utils.UtilsMeth;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainScreen {
    private JTextField urlTxt;
    private JButton submitBtn;
    private JTextArea txtLogArea;
    private JPanel MainScreen;
    private JLabel loadingTumblrLbl;
    private JLabel tumblrLbl;
    private JLabel loadingWPLbl;
    private JLabel wplbl;
    private JLabel loadingBczLbl;
    private JLabel loadingElloCoLbl;
    private JLabel loadingFlipboardLbl;
    private JLabel loadingInstaLbl;
    private JLabel loadingVingleLbl;
    private JLabel loadingGetPocketLbl;
    private JLabel loadingScoopItLbl;
    private JButton credentialsBtn;
    private JCheckBox openBrowser;
    private JLabel loadingForlkdLbl;
    private JCheckBox wpChk;
    private JCheckBox instapaperChk;
    private JCheckBox bczChk;
    private JCheckBox getpocketChk;
    private JCheckBox scoopitChk;
    private JCheckBox folkDChk;
    private JCheckBox flipboardChk;
    private JCheckBox ellocoChk;
    private JCheckBox tumblrChk;
    private JCheckBox vingleChk;
    private JButton stopBtn;
    private JPanel actionPanel;
    private CredentialsDialog dialog;

    private ExecutorService service;
    private static final Logger LOGGER = Logger.getLogger(MainScreen.class);

    private static List<BaseBot> botList = new ArrayList<>();

    private void showPostLinkFailedMessage(BaseBot targetBot) {
        JLabel field = findCorrelatedFieldForBot(targetBot);
        if (field == null) txtLogArea.append("\n Cannot find correlated field message");

        field.setText("Post link failed");
        field.setForeground(Color.RED);
    }

    private void showLoginFailedMessage(BaseBot targetBot) {
        JLabel field = findCorrelatedFieldForBot(targetBot);
        if (field == null) txtLogArea.append("\n Cannot find correlated field message");

        field.setText("Login failed");
        field.setForeground(Color.RED);
    }

    private void showSuccessMessage(BaseBot targetBot) {
        JLabel field = findCorrelatedFieldForBot(targetBot);
        if (field == null) txtLogArea.append("\n Cannot find correlated field message");

        field.setText("Success");
        field.setForeground(Color.GREEN);
    }

    private JLabel findCorrelatedFieldForBot(BaseBot targetBot) {
        if (targetBot instanceof ElloCoBot) {
            return loadingElloCoLbl;
        }
        if (targetBot instanceof BczBot) {
            return loadingBczLbl;
        }
        if (targetBot instanceof VingleBot) {
            return loadingVingleLbl;
        }
        if (targetBot instanceof InstapaperBot) {
            return loadingInstaLbl;
        }
        if (targetBot instanceof TumblrBot) {
            return loadingTumblrLbl;
        }
        if (targetBot instanceof WordpressBot) {
            return loadingWPLbl;
        }
        if (targetBot instanceof FlipboardBot) {
            return loadingFlipboardLbl;
        }
        if (targetBot instanceof GetPocketBot) {
            return loadingGetPocketLbl;
        }
        if (targetBot instanceof ScoopItBot) {
            return loadingScoopItLbl;
        }
        if (targetBot instanceof FolkdBot) {
            return loadingForlkdLbl;
        }
        return null;
    }

    public MainScreen() {

        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addInfoLogMessage(":::......::::::::: Bot is started :::......:::::::::");
                initializeBot();
                submitBtn.setEnabled(false);
                service = Executors.newFixedThreadPool(Constants.NUMBER_OF_CONCURRENT_TASKS);
                MixpanelService.getInstance().userPostLinkEvent(botList.stream().map(b -> b.getName()).collect(Collectors.toList()));
                botList.stream().forEach(bot -> {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("bot number " + bot.toString());
                            addInfoLogMessage(bot.toString() + " has started");
                            bot.openPhantomJs(openBrowser.isSelected());
                            if (!bot.login()) {
                                System.out.println("cant login ");
                                addErrorLogMessage(bot.toString() + " has failed to log in");
                                showLoginFailedMessage(bot);
                                bot.closePhantomJsBr();
                                return;
                            }
                            addInfoLogMessage(bot.toString() + " has logged in");

                            if (!bot.postLink(urlTxt.getText())) {
                                System.out.println("cant post link");
                                addErrorLogMessage(bot.toString() + " has failed to post link");
                                showPostLinkFailedMessage(bot);
                                bot.closePhantomJsBr();
                                MixpanelService.getInstance().userFailedPostedLinkEvent(bot.getName());
                                return;
                            }
                            addInfoLogMessage(bot.toString() + " has posted link. Please check screenshots folder for preview");

                            showSuccessMessage(bot);
                            bot.closePhantomJsBr();
                            MixpanelService.getInstance().userSuccessPostedLinkEvent(bot.getName());
                        }
                    });
                });
                service.shutdown();

                submitBtn.setEnabled(true);
//                    // log to area
//                    Path filePath = Path.of("./LOG/log_seo-tools.log");
//                    String fileContent = "";
//                    byte[] bytes = new byte[0];
//                    try {
//                        bytes = Files.readAllBytes(filePath);
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                    fileContent = new String(bytes);
//                    txtLogArea.append(fileContent);

//                    try {
//                        service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
//                    } catch (InterruptedException exx) {
//                        exx.printStackTrace();
//                    }
            }
        });
        stopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                service.shutdownNow();
                botList.stream().forEach(bot -> {
                    bot.closePhantomJsBr();
                });
                addInfoLogMessage(":::......::::::::: Bot is stopped :::......:::::::::");
            }
        });

        credentialsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog = new CredentialsDialog();
                dialog.setSize(600, 550);
                dialog.setVisible(true);
            }
        });

        loadAppData();
    }

    private void loadAppData() {
        AppDataManipulator.loadData();
    }

    private void initializeBot() {
        botList = new ArrayList<>();

        if (bczChk.isSelected()) botList.add(new BczBot());
        if (getpocketChk.isSelected()) botList.add(new GetPocketBot());
        if (folkDChk.isSelected()) botList.add(new FolkdBot());
        if (flipboardChk.isSelected()) botList.add(new FlipboardBot());
        if (ellocoChk.isSelected()) botList.add(new ElloCoBot());
        if (tumblrChk.isSelected()) botList.add(new TumblrBot());
        if (vingleChk.isSelected()) botList.add(new VingleBot());
        if (scoopitChk.isSelected()) botList.add(new ScoopItBot());
        if (wpChk.isSelected()) botList.add(new WordpressBot());
        if (instapaperChk.isSelected()) botList.add(new InstapaperBot());

        if (botList.isEmpty()) {
            botList = Arrays.asList(
                    new ElloCoBot(),
                    new WordpressBot(),
                    new VingleBot(),
                    new TumblrBot(),
                    new BczBot(),
                    new FlipboardBot(),
                    new GetPocketBot(),
                    new InstapaperBot(),
                    new ScoopItBot(),
                    new FolkdBot()
            );
        }
    }

    private void addInfoLogMessage(String msg) {
        txtLogArea.append(" \n [Info] " + msg);
    }

    private void addErrorLogMessage(String msg) {
        txtLogArea.append(" \n [Error] " + msg);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("MainScreen");
        // checking permission to use the app
        try {
            if (PermissionUsageUtils.checkPermission()) {
                frame.setContentPane(new MainScreen().MainScreen);
            } else {
                frame.setContentPane(new ErrorScreen().getErrorScreen());
                frame.setMinimumSize(new Dimension(658, 550));
            }
        } catch (GeneralSecurityException e) {
            frame.setContentPane(new ErrorScreen().getErrorScreen());
            frame.setMinimumSize(new Dimension(658, 550));
            throw new RuntimeException(e);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        if (!new File("./LOG/log_seo-tools.log").isFile()) {
            new File("./LOG").mkdirs();
            new File("./LOG/log_seo-tools.log").createNewFile();
            LOGGER.info("Create log_seo-tools.log successfully");
        }

        if (!new File(Constants.SCREENSHOTS_DEFAULT_DIRECTORY).isDirectory()) {
            new File(Constants.SCREENSHOTS_DEFAULT_DIRECTORY).mkdirs();
            LOGGER.info("Create screenshots folder successfully");
        }

        if (!new File(Constants.APP_DATA_DEFAULT_DIRECTORY_FILE).isFile()) {
            new File(Constants.APP_DATA_DEFAULT_DIRECTORY_FILE).createNewFile();
            LOGGER.info("Create credentials file successfully");
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        MainScreen = new JPanel();
        MainScreen.setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, -1));
        MainScreen.setMaximumSize(new Dimension(658, 550));
        MainScreen.setMinimumSize(new Dimension(658, 550));
        MainScreen.setPreferredSize(new Dimension(658, 500));
        final JLabel label1 = new JLabel();
        label1.setText("URL to post");
        MainScreen.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlTxt = new JTextField();
        MainScreen.add(urlTxt, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        MainScreen.add(scrollPane1, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(600, 250), new Dimension(600, 250), new Dimension(600, 250), 0, false));
        txtLogArea = new JTextArea();
        txtLogArea.setEditable(false);
        txtLogArea.setMaximumSize(new Dimension(650, 300));
        txtLogArea.setWrapStyleWord(true);
        scrollPane1.setViewportView(txtLogArea);
        actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayoutManager(5, 7, new Insets(0, 0, 0, 0), -1, -1));
        MainScreen.add(actionPanel, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loadingTumblrLbl = new JLabel();
        loadingTumblrLbl.setText("");
        actionPanel.add(loadingTumblrLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        tumblrLbl = new JLabel();
        tumblrLbl.setText("Tumblr");
        actionPanel.add(tumblrLbl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingWPLbl = new JLabel();
        loadingWPLbl.setText("");
        actionPanel.add(loadingWPLbl, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        wplbl = new JLabel();
        wplbl.setText("Wordpress");
        actionPanel.add(wplbl, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ElloCo");
        actionPanel.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Bcz");
        actionPanel.add(label3, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingBczLbl = new JLabel();
        loadingBczLbl.setText("");
        actionPanel.add(loadingBczLbl, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        loadingFlipboardLbl = new JLabel();
        loadingFlipboardLbl.setText("");
        actionPanel.add(loadingFlipboardLbl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Flipboard");
        actionPanel.add(label4, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Instapaper");
        actionPanel.add(label5, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Vingle");
        actionPanel.add(label6, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("GetPocket");
        actionPanel.add(label7, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("ScoopIt");
        actionPanel.add(label8, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("FolkD");
        actionPanel.add(label9, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingElloCoLbl = new JLabel();
        loadingElloCoLbl.setText("");
        actionPanel.add(loadingElloCoLbl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        loadingInstaLbl = new JLabel();
        loadingInstaLbl.setText("");
        actionPanel.add(loadingInstaLbl, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        loadingVingleLbl = new JLabel();
        loadingVingleLbl.setText("");
        actionPanel.add(loadingVingleLbl, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        loadingGetPocketLbl = new JLabel();
        loadingGetPocketLbl.setText("");
        actionPanel.add(loadingGetPocketLbl, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        loadingScoopItLbl = new JLabel();
        loadingScoopItLbl.setText("");
        actionPanel.add(loadingScoopItLbl, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        loadingForlkdLbl = new JLabel();
        loadingForlkdLbl.setText("");
        actionPanel.add(loadingForlkdLbl, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        wpChk = new JCheckBox();
        wpChk.setText("");
        actionPanel.add(wpChk, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bczChk = new JCheckBox();
        bczChk.setText("");
        actionPanel.add(bczChk, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        getpocketChk = new JCheckBox();
        getpocketChk.setText("");
        actionPanel.add(getpocketChk, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scoopitChk = new JCheckBox();
        scoopitChk.setText("");
        actionPanel.add(scoopitChk, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        folkDChk = new JCheckBox();
        folkDChk.setText("");
        actionPanel.add(folkDChk, new GridConstraints(4, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        instapaperChk = new JCheckBox();
        instapaperChk.setText("");
        actionPanel.add(instapaperChk, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        flipboardChk = new JCheckBox();
        flipboardChk.setText("");
        actionPanel.add(flipboardChk, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ellocoChk = new JCheckBox();
        ellocoChk.setText("");
        actionPanel.add(ellocoChk, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tumblrChk = new JCheckBox();
        tumblrChk.setText("");
        actionPanel.add(tumblrChk, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vingleChk = new JCheckBox();
        vingleChk.setText("");
        actionPanel.add(vingleChk, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        credentialsBtn = new JButton();
        credentialsBtn.setText("Profiles");
        MainScreen.add(credentialsBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openBrowser = new JCheckBox();
        openBrowser.setText("Open Browser?");
        MainScreen.add(openBrowser, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        MainScreen.add(panel1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        submitBtn = new JButton();
        submitBtn.setText("Start");
        panel1.add(submitBtn);
        stopBtn = new JButton();
        stopBtn.setText("Stop");
        panel1.add(stopBtn);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainScreen;
    }

}
