package view;

import model.cam.AppPreferences;
import model.cam.Camera;

import javax.swing.*;
import java.awt.event.*;

public class Settings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JTextField leftPanelURLField;
    private JTextField secondCamURLField;

    public Camera camera;
    private JPanel settingsPanel;
    private JComboBox cameraComboBox;

    public Settings() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.setValues();

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

        cameraComboBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                int deviceNumber = cameraComboBox.getSelectedIndex();
                if (deviceNumber != AppPreferences.getInstance().getChoosenCamera()) {
                    AppPreferences.getInstance().setChoosenCamera(deviceNumber);
                    camera.stop();
                    camera.init(deviceNumber);
                    camera.start();
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void setValues() {
        leftPanelURLField.setText(AppPreferences.getInstance().getLeftPanelURL());
        secondCamURLField.setText(AppPreferences.getInstance().getSecondCamURL());

        for (String camName : Camera.getCameraNames()) {
            cameraComboBox.addItem(camName);
        }
        cameraComboBox.setSelectedIndex(AppPreferences.getInstance().getChoosenCamera());
    }

    private void onOK() {
        AppPreferences.getInstance().setLeftPanelURL(leftPanelURLField.getText());
        AppPreferences.getInstance().setSecondCamURL(secondCamURLField.getText());
        AppPreferences.getInstance().setChoosenCamera(cameraComboBox.getSelectedIndex());

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
