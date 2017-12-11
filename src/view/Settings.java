package view;

import com.intellij.uiDesigner.core.GridConstraints;
import model.AppPreferences;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Settings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JTextField leftPanelURLField;
    private JTextField secondCamURLField;
    private HashMap<String, JCheckBox> checkBoxes;

    private JPanel settingsPanel;

    public Settings() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        this.setValues();
    }

    private void setValues() {
        leftPanelURLField.setText(AppPreferences.getInstance().getLeftPanelURL());
        secondCamURLField.setText(AppPreferences.getInstance().getSecondCamURL());


        List<Webcam> webcams = Webcam.getWebcams();
        this.checkBoxes = new HashMap<String, JCheckBox>();
        for(int i = 0; i< webcams.size(); i++) {
            Webcam cam = webcams.get(i);
            GridConstraints c = new GridConstraints();
            c.setRow(i + 2);
            this.settingsPanel.add(new JLabel(cam.getName()), c);
            c.setColumn(1);
            JCheckBox box = new JCheckBox("aktiviert", AppPreferences.getInstance().isWebcamActivated(cam.getName()));
            checkBoxes.put(cam.getName(), box);
            this.settingsPanel.add(box, c);
//            cam.setViewSize(AppPreferences.getInstance().getWebcamDimension(cam.getName()));
//            WebcamPanel panel = new WebcamPanel(cam);
//            panel.setMirrored(false);
        }

    }

    private void onOK() {
        AppPreferences.getInstance().setLeftPanelURL(leftPanelURLField.getText());
        AppPreferences.getInstance().setSecondCamURL(secondCamURLField.getText());

        for (Map.Entry<String,JCheckBox> pair : checkBoxes.entrySet()) {
            AppPreferences.getInstance().setWebcamActivated(pair.getKey(), pair.getValue().isSelected());
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

//    public static void main(String[] args) {
//        Settings dialog = new Settings();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
