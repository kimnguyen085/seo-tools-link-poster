package main.java.com.seo.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import main.java.com.seo.auto.bot.*;
import main.java.com.seo.auto.data.AppData;
import main.java.com.seo.auto.data.AppDataManipulator;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;

public class CredentialsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField bczUsr;
    private JPasswordField bczPwd;
    private JTextField elloCoUsr;
    private JPasswordField elloCoPwd;
    private JTextField flipboardUsr;
    private JPasswordField flipboardPwd;
    private JTextField getPocketUsr;
    private JPasswordField getPocketPwd;
    private JTextField instapaperUsr;
    private JPasswordField instapaperPwd;
    private JTextField scoopItUsr;
    private JPasswordField scoopItPwd;
    private JTextField tumblrUsr;
    private JPasswordField tumblrPwd;
    private JLabel vingleLbl;
    private JPasswordField vinglePwd;
    private JTextField wpUsr;
    private JPasswordField wpPwd;
    private JTextField vingleUsr;
    private JTextField folkdUsr;
    private JPasswordField folkdPwd;
    private JComboBox profileCbb;
    private JButton addNewBtn;
    private JButton deleteBtn;
    private JButton importBtn;
    private JButton editBtn;
    private JTextField contractIdField;
    private JTextArea noteField;

    private ProfilesDialog dialog;
    private static final Logger LOGGER = Logger.getLogger(CredentialsDialog.class);

    public CredentialsDialog(MainScreen mainScreen) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Profile Management");
        getRootPane().setDefaultButton(buttonOK);
        reloadProfileCbb();
        CredentialsDialog component = this;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                    mainScreen.fetchDataUI();
                } catch (JsonProcessingException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        addNewBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog = new ProfilesDialog(component, null);
                dialog.setSize(600, 480);
                dialog.setVisible(true);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // initialise username password
        fetchingDataToUI();
        profileCbb.addItemListener(getProfileCbbListener());
        deleteBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String item = (String) profileCbb.getSelectedItem();
                AppDataManipulator.deleteProfile(item);
                reloadProfileCbb();
            }
        });

        importBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                performFileChooser(component);
            }
        });
        editBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog = new ProfilesDialog(component, AppDataManipulator.activeProfile);
                dialog.setSize(600, 480);
                dialog.setVisible(true);
            }
        });
    }

    private void performFileChooser(CredentialsDialog component) {
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // invoke the showsSaveDialog function to show the save dialog
        int r = j.showSaveDialog(component);

        if (r == JFileChooser.APPROVE_OPTION) {
            // set the label to the path of the selected directory
            String filePath = j.getSelectedFile().getAbsolutePath();
            AppDataManipulator.importPwdFromCsv(filePath, (String) profileCbb.getSelectedItem());
            fetchingDataToUI();
        }
        // if the user cancelled the operation
//        else
//            l.setText("the user cancelled the operation");
    }

    private void fetchingDataToUI() {
        bczUsr.setText(BczBot.usrName);
        bczPwd.setText(BczBot.pwd);
        elloCoUsr.setText(ElloCoBot.usrName);
        elloCoPwd.setText(ElloCoBot.pwd);
        flipboardUsr.setText(FlipboardBot.usrName);
        flipboardPwd.setText(FlipboardBot.pwd);
        getPocketUsr.setText(GetPocketBot.usrName);
        getPocketPwd.setText(GetPocketBot.pwd);
        instapaperUsr.setText(InstapaperBot.usrName);
        instapaperPwd.setText(InstapaperBot.pwd);
        scoopItUsr.setText(ScoopItBot.usrName);
        scoopItPwd.setText(ScoopItBot.pwd);
        tumblrUsr.setText(TumblrBot.usrName);
        tumblrPwd.setText(TumblrBot.pwd);
        vingleUsr.setText(VingleBot.usrName);
        vinglePwd.setText(VingleBot.pwd);
        wpUsr.setText(WordpressBot.usrName);
        wpPwd.setText(WordpressBot.pwd);
        folkdUsr.setText(FolkdBot.usrName);
        folkdPwd.setText(FolkdBot.pwd);

        contractIdField.setText(AppDataManipulator.activeProfile == null ? "" : AppDataManipulator.activeProfile.getContractId());
        noteField.setText(AppDataManipulator.activeProfile == null ? "" : AppDataManipulator.activeProfile.getNote());
    }

    public void reloadProfileCbb() {
        profileCbb.removeAllItems();
        Arrays.stream(AppDataManipulator.getAllProfileNames()).sorted(Collections.reverseOrder()).forEach(name -> {
            profileCbb.addItem(name);
        });

        if (AppDataManipulator.activeProfile != null) {
            profileCbb.setSelectedItem(AppDataManipulator.activeProfile.getName());
            fetchingDataToUI();
        }
    }

    private ItemListener getProfileCbbListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("freaking thing trigger" + (String) e.getItem());
                    String item = (String) e.getItem();
                    AppDataManipulator.changeActiveProfile(item);
                    fetchingDataToUI();
                }
            }
        };
    }

    private void onOK() throws JsonProcessingException {
        String profileName = (String) profileCbb.getSelectedItem();

        if (profileName == null || profileName.equals("")) {
            JOptionPane.showMessageDialog(this, "Profile Name cannot be empty !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            BczBot.usrName = bczUsr.getText();
            BczBot.pwd = new String(bczPwd.getPassword());
            ElloCoBot.usrName = elloCoUsr.getText();
            ElloCoBot.pwd = new String(elloCoPwd.getPassword());
            FlipboardBot.usrName = flipboardUsr.getText();
            FlipboardBot.pwd = new String(flipboardPwd.getPassword());
            GetPocketBot.usrName = getPocketUsr.getText();
            GetPocketBot.pwd = new String(getPocketPwd.getPassword());
            InstapaperBot.usrName = instapaperUsr.getText();
            InstapaperBot.pwd = new String(instapaperPwd.getPassword());
            ScoopItBot.usrName = scoopItUsr.getText();
            ScoopItBot.pwd = new String(scoopItPwd.getPassword());
            TumblrBot.usrName = tumblrUsr.getText();
            TumblrBot.pwd = new String(tumblrPwd.getPassword());
            VingleBot.usrName = vingleUsr.getText();
            VingleBot.pwd = new String(vinglePwd.getPassword());
            WordpressBot.usrName = wpUsr.getText();
            WordpressBot.pwd = new String(wpPwd.getPassword());
            FolkdBot.usrName = folkdUsr.getText();
            FolkdBot.pwd = new String(folkdPwd.getPassword());

            AppDataManipulator.modifyProfile(profileName, contractIdField.getText(), noteField.getText().trim());
            AppDataManipulator.changeActiveProfile(profileName);
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        importBtn = new JButton();
        importBtn.setText("Import");
        panel1.add(importBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(10, 3, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Bcz");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bczUsr = new JTextField();
        panel3.add(bczUsr, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        bczPwd = new JPasswordField();
        panel3.add(bczPwd, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ElloCo");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Flipboard");
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("GetPocket");
        panel3.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Instapaper");
        panel3.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("ScoopIt");
        panel3.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Tumblr");
        panel3.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vingleLbl = new JLabel();
        vingleLbl.setText("Vingle");
        panel3.add(vingleLbl, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Wordpress");
        panel3.add(label8, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("FolkD");
        panel3.add(label9, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        elloCoUsr = new JTextField();
        panel3.add(elloCoUsr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flipboardUsr = new JTextField();
        panel3.add(flipboardUsr, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        getPocketUsr = new JTextField();
        panel3.add(getPocketUsr, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        instapaperUsr = new JTextField();
        panel3.add(instapaperUsr, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scoopItUsr = new JTextField();
        panel3.add(scoopItUsr, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tumblrUsr = new JTextField();
        panel3.add(tumblrUsr, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        vingleUsr = new JTextField();
        panel3.add(vingleUsr, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        wpUsr = new JTextField();
        panel3.add(wpUsr, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        folkdUsr = new JTextField();
        panel3.add(folkdUsr, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        elloCoPwd = new JPasswordField();
        panel3.add(elloCoPwd, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        flipboardPwd = new JPasswordField();
        panel3.add(flipboardPwd, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        getPocketPwd = new JPasswordField();
        panel3.add(getPocketPwd, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        instapaperPwd = new JPasswordField();
        panel3.add(instapaperPwd, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scoopItPwd = new JPasswordField();
        panel3.add(scoopItPwd, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tumblrPwd = new JPasswordField();
        panel3.add(tumblrPwd, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        vinglePwd = new JPasswordField();
        panel3.add(vinglePwd, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        wpPwd = new JPasswordField();
        panel3.add(wpPwd, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        folkdPwd = new JPasswordField();
        panel3.add(folkdPwd, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 3, new Insets(0, 10, 0, 10), 0, 0));
        contentPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 80), new Dimension(-1, 80), new Dimension(-1, 80), 0, false));
        profileCbb = new JComboBox();
        panel4.add(profileCbb, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 20), new Dimension(-1, 20), null, 0, false));
        addNewBtn = new JButton();
        addNewBtn.setText("Add New");
        panel4.add(addNewBtn, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        deleteBtn = new JButton();
        deleteBtn.setText("Delete");
        panel4.add(deleteBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editBtn = new JButton();
        editBtn.setText("Edit Profile");
        panel4.add(editBtn, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(0, 10, 0, 10), -1, -1));
        contentPane.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Contract ID");
        panel5.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Note");
        panel5.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contractIdField = new JTextField();
        contractIdField.setEditable(false);
        panel5.add(contractIdField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        noteField = new JTextArea();
        noteField.setEditable(false);
        panel5.add(noteField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
