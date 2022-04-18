package main.java.com.seo.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.seo.auto.bot.*;
import main.java.com.seo.auto.utils.Constants;
import main.java.com.seo.auto.utils.UtilsMeth;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

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
    private CredentialsDialog dialog;
    private static final Logger LOGGER = Logger.getLogger(MainScreen.class);

    private static List<BaseBot> botList = Arrays.asList(
            new ElloCoBot(),
            new WordpressBot(),
            new VingleBot(),
            new TumblrBot(),
            new BczBot(),
            new FlipboardBot(),
            new GetPocketBot(),
            new InstapaperBot(),
            new ScoopItBot()
    );

    private void showPostLinkFailedMessage(BaseBot targetBot) {
        JLabel field = findCorrelatedFieldForBot(targetBot);
        if (field == null) txtLogArea.append("/nCannot find correlated field message");

        field.setText("Post link failed");
        field.setForeground(Color.RED);
    }

    private void showLoginFailedMessage(BaseBot targetBot) {
        JLabel field = findCorrelatedFieldForBot(targetBot);
        if (field == null) txtLogArea.append("/nCannot find correlated field message");

        field.setText("Login failed");
        field.setForeground(Color.RED);
    }

    private void showSuccessMessage(BaseBot targetBot) {
        JLabel field = findCorrelatedFieldForBot(targetBot);
        if (field == null) txtLogArea.append("/nCannot find correlated field message");

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
        return null;
    }

    public MainScreen() {

        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                try {
                submitBtn.setEnabled(false);
                ExecutorService service = Executors.newFixedThreadPool(Constants.NUMBER_OF_CONCURRENT_TASKS);
                botList.stream().forEach(bot -> {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("bot number " + bot.toString());
                            bot.openPhantomJs();
                            if (!bot.login()) {
                                System.out.println("cant login ");
                                showLoginFailedMessage(bot);
                                bot.closePhantomJsBr();
                                return;
                            }

                            if (!bot.postLink(urlTxt.getText())) {
                                System.out.println("cant post link");
                                showPostLinkFailedMessage(bot);
                                bot.closePhantomJsBr();
                                return;
                            }

                            showSuccessMessage(bot);
                            bot.closePhantomJsBr();
                        }
                    });
                });
                service.shutdown();

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


//                } catch (Exception ex) {
//                    LOGGER.error(ex.getMessage());
//                    ex.printStackTrace();
//                } finally {
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
//
//                    txtLogArea.append(fileContent);
//                }
            }
        });

        credentialsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog = new CredentialsDialog();
                dialog.setSize(600, 600);
                dialog.setVisible(true);
            }
        });

        loadCredentialsFile();
    }

    private void loadCredentialsFile() {
        try {
            String txtCredentials = UtilsMeth.readFile(Constants.CREDENTIALS_DEFAULT_DIRECTORY_FILE);
            if (txtCredentials.isEmpty()) return;
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> root = mapper.readValue(txtCredentials, Map.class);

            Map<String, String> bczCredentials = (Map<String, String>) root.get("Bcz");
            Map<String, String> ellocoCredentials = (Map<String, String>) root.get("ElloCo");
            Map<String, String> flipboardCredentials = (Map<String, String>) root.get("Flipboard");
            Map<String, String> getPocketCredentials = (Map<String, String>) root.get("GetPocket");
            Map<String, String> instapaperCredentials = (Map<String, String>) root.get("Instapaper");
            Map<String, String> scoopitCredentials = (Map<String, String>) root.get("ScoopIt");
            Map<String, String> tumblrCredentials = (Map<String, String>) root.get("Tumblr");
            Map<String, String> vingleCredentials = (Map<String, String>) root.get("Vingle");
            Map<String, String> wpCredentials = (Map<String, String>) root.get("Wordpress");

            BczBot.usrName = bczCredentials.get("usr");
            BczBot.pwd = bczCredentials.get("pwd");
            ElloCoBot.usrName = ellocoCredentials.get("usr");
            ElloCoBot.pwd = ellocoCredentials.get("pwd");
            FlipboardBot.usrName = flipboardCredentials.get("usr");
            FlipboardBot.pwd = flipboardCredentials.get("pwd");
            GetPocketBot.usrName = getPocketCredentials.get("usr");
            GetPocketBot.pwd = getPocketCredentials.get("pwd");
            InstapaperBot.usrName = instapaperCredentials.get("usr");
            InstapaperBot.pwd = instapaperCredentials.get("pwd");
            ScoopItBot.usrName = scoopitCredentials.get("usr");
            ScoopItBot.pwd = scoopitCredentials.get("pwd");
            TumblrBot.usrName = tumblrCredentials.get("usr");
            TumblrBot.pwd = tumblrCredentials.get("pwd");
            VingleBot.usrName = vingleCredentials.get("usr");
            VingleBot.pwd = vingleCredentials.get("pwd");
            WordpressBot.usrName = wpCredentials.get("usr");
            WordpressBot.pwd = wpCredentials.get("pwd");

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("MainScreen");
        frame.setContentPane(new MainScreen().MainScreen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        if (!new File("./LOG/log_seo-tools.log").isFile()) {
            new File("./LOG/log_seo-tools.log").createNewFile();
            LOGGER.info("Create log_seo-tools.log successfully");
        }

        if (!new File(Constants.SCREENSHOTS_DEFAULT_DIRECTORY).isDirectory()) {
            new File(Constants.SCREENSHOTS_DEFAULT_DIRECTORY).mkdirs();
            LOGGER.info("Create screenshots folder successfully");
        }

        if (!new File(Constants.CREDENTIALS_DEFAULT_DIRECTORY_FILE).isFile()) {
            new File(Constants.CREDENTIALS_DEFAULT_DIRECTORY_FILE).createNewFile();
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
        MainScreen.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), -1, -1));
        MainScreen.setMaximumSize(new Dimension(658, 500));
        MainScreen.setMinimumSize(new Dimension(658, 500));
        MainScreen.setPreferredSize(new Dimension(658, 450));
        final JLabel label1 = new JLabel();
        label1.setText("URL to post");
        MainScreen.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlTxt = new JTextField();
        MainScreen.add(urlTxt, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        submitBtn = new JButton();
        submitBtn.setText("Start");
        MainScreen.add(submitBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHEAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        MainScreen.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(600, 250), new Dimension(600, 250), new Dimension(600, 250), 0, false));
        txtLogArea = new JTextArea();
        txtLogArea.setEditable(false);
        txtLogArea.setMaximumSize(new Dimension(650, 300));
        txtLogArea.setWrapStyleWord(true);
        scrollPane1.setViewportView(txtLogArea);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        MainScreen.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loadingTumblrLbl = new JLabel();
        loadingTumblrLbl.setText("");
        panel1.add(loadingTumblrLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tumblrLbl = new JLabel();
        tumblrLbl.setText("Tumblr");
        panel1.add(tumblrLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingWPLbl = new JLabel();
        loadingWPLbl.setText("");
        panel1.add(loadingWPLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        wplbl = new JLabel();
        wplbl.setText("Wordpress");
        panel1.add(wplbl, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ElloCo");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Bcz");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingBczLbl = new JLabel();
        loadingBczLbl.setText("");
        panel1.add(loadingBczLbl, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingFlipboardLbl = new JLabel();
        loadingFlipboardLbl.setText("");
        panel1.add(loadingFlipboardLbl, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Flipboard");
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Instapaper");
        panel1.add(label5, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Vingle");
        panel1.add(label6, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("GetPocket");
        panel1.add(label7, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("ScoopIt");
        panel1.add(label8, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("INCOMING");
        panel1.add(label9, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingElloCoLbl = new JLabel();
        loadingElloCoLbl.setText("");
        panel1.add(loadingElloCoLbl, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingInstaLbl = new JLabel();
        loadingInstaLbl.setText("");
        panel1.add(loadingInstaLbl, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingVingleLbl = new JLabel();
        loadingVingleLbl.setText("");
        panel1.add(loadingVingleLbl, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingGetPocketLbl = new JLabel();
        loadingGetPocketLbl.setText("");
        panel1.add(loadingGetPocketLbl, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingScoopItLbl = new JLabel();
        loadingScoopItLbl.setText("");
        panel1.add(loadingScoopItLbl, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("");
        panel1.add(label10, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        credentialsBtn = new JButton();
        credentialsBtn.setText("Credentials");
        MainScreen.add(credentialsBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainScreen;
    }

}
