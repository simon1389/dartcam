package view;

import com.sun.jna.Platform;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import model.AppPreferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DartMainForm extends JFrame implements Runnable {

    private JPanel mainPanel = new JPanel();
    private JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    private JSplitPane splitPaneHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

    private Settings settingsDialog;
    private JDialog secondCamDialog = new JDialog();

    public DartMainForm() {
        this.setListeners();
    }

    private void setListeners() {
        splitPaneVertical.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        AppPreferences.getInstance().setDividerLocationVertical(splitPaneVertical.getDividerLocation());
                    }
                });

        splitPaneHorizontal.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        AppPreferences.getInstance().setDividerLocationHorizontal(splitPaneHorizontal.getDividerLocation());
                    }
                });


        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_E && e.getModifiers() == KeyEvent.CTRL_MASK) {
                        settingsDialog.setVisible(true);
                    }
                }
                return false;
            }
        });

        secondCamDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                AppPreferences.getInstance().setSecondCamBounds(e.getComponent().getBounds());
            }
            public void componentMoved(ComponentEvent e) {
                AppPreferences.getInstance().setSecondCamBounds(e.getComponent().getBounds());
            }
        });
    }

    @Override
    public void run() {
        this.setContentPane(mainPanel);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0,0, screenSize.width, screenSize.height);
        if (Platform.isMac()) {
            this.setUndecorated(true);
        }

        this.setMenuBar();

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(splitPaneVertical, BorderLayout.CENTER);

        this.setVisible(true);

        this.generateRightPanel();
        this.generateLeftPanel();

        splitPaneVertical.setDividerLocation(AppPreferences.getInstance().getDividerLocationVertical());
    }

    private void setMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Dart");

        JMenuItem settings = new JMenuItem("Einstellungen");
        settingsDialog = new Settings();
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.setVisible(true);
            }
        });
        menu.add(settings);

        JCheckBoxMenuItem b = new JCheckBoxMenuItem("SecondScreenDraggable", true);
        b.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                secondCamDialog.setVisible(false);
                secondCamDialog.dispose();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    secondCamDialog.setUndecorated(false);
                } else {
                    secondCamDialog.setUndecorated(true);
                }
                secondCamDialog.setVisible(true);
                secondCamDialog.setAlwaysOnTop(true);
            }
        });

        menu.add(b);
        bar.add(menu);
        this.setJMenuBar(bar);
    }

    private void generateRightPanel() {
        DartCamPanel pan = new DartCamPanel(AppPreferences.getInstance().getChoosenCamera());
        settingsDialog.camPanel = pan;
        splitPaneHorizontal.setBottomComponent(pan);
        splitPaneHorizontal.setTopComponent(new JPanel());
        splitPaneVertical.setRightComponent(splitPaneHorizontal);
        splitPaneVertical.setContinuousLayout(true);

        splitPaneHorizontal.setDividerLocation(AppPreferences.getInstance().getDividerLocationHorizontal());

        pan.start();
    }

    private void generateLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        splitPaneVertical.setLeftComponent(panel);

        Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
        BrowserView view = new BrowserView(browser);

        panel.add(view, BorderLayout.CENTER);
        browser.loadURL(AppPreferences.getInstance().getLeftPanelURL());


        Browser browser1 = new Browser(BrowserType.LIGHTWEIGHT);
        BrowserView view1 = new BrowserView(browser1);

        secondCamDialog.getContentPane().add(view1, BorderLayout.CENTER);
        browser1.loadURL(AppPreferences.getInstance().getSecondCamURL());
        secondCamDialog.setBounds(AppPreferences.getInstance().getSecondCamBounds());
        secondCamDialog.setResizable(true);
        secondCamDialog.setVisible(true);
        secondCamDialog.setAlwaysOnTop(true);
    }
}
