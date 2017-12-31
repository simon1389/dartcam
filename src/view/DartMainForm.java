package view;

import com.sun.jna.Platform;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import model.cam.AppPreferences;
import model.cam.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class DartMainForm extends JFrame implements Runnable {

    private JPanel mainPanel = new JPanel();
    private JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    private JSplitPane splitPaneHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

    private Settings settingsDialog;
    private JDialog secondCamDialog = new FramelessDialog();

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

        Camera cam = new Camera();
        cam.init(AppPreferences.getInstance().getChoosenCamera());

        this.setMenuBar(cam);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(splitPaneVertical, BorderLayout.CENTER);

        this.setVisible(true);

        this.generateRightPanel(cam);
        this.generateLeftPanel();

        ZoomedAreaDialog.restoreAllFromSettings(cam);

        splitPaneVertical.setDividerLocation(AppPreferences.getInstance().getDividerLocationVertical());
    }

    private void setMenuBar(Camera cam) {
        JMenuBar bar = new JMenuBar();

        //## Einstellungen

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

        JMenuItem newZoomArea = new JMenuItem("Zoombereich erstellen");
        newZoomArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ZoomedAreaDialog(new DartCamPanel(cam));
            }
        });
        menu.add(newZoomArea);

        //## Auflösungen

        JMenu menuRes = new JMenu("Auflösung");
        Map<Integer,JRadioButtonMenuItem> items = new HashMap<>();
        ButtonGroup bg = new ButtonGroup();
        int currentHeight = cam.getFrame().size().height();
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] arr = ((JRadioButtonMenuItem)e.getSource()).getText().split("x");
                cam.stop();
                cam.init(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                cam.start();
            }
        };

        JRadioButtonMenuItem r1080 = new JRadioButtonMenuItem("1920x1080");
        JRadioButtonMenuItem r1024 = new JRadioButtonMenuItem("1280x1024");
        JRadioButtonMenuItem r720 = new JRadioButtonMenuItem("1280x720");
        JRadioButtonMenuItem r768 = new JRadioButtonMenuItem("1024x768");
        JRadioButtonMenuItem r600 = new JRadioButtonMenuItem("800x600");
        JRadioButtonMenuItem r480 = new JRadioButtonMenuItem("640x480");
        menuRes.add(r1080);
        menuRes.add(r1024);
        menuRes.add(r720);
        menuRes.add(r768);
        menuRes.add(r600);
        menuRes.add(r480);
        items.put(1080, r1080);
        items.put(1024, r1024);
        items.put(720, r720);
        items.put(768, r768);
        items.put(600, r600);
        items.put(480, r480);
        bg.add(r1080);
        bg.add(r1024);
        bg.add(r720);
        bg.add(r768);
        bg.add(r600);
        bg.add(r480);
        r1080.addActionListener(listener);
        r1024.addActionListener(listener);
        r720.addActionListener(listener);
        r768.addActionListener(listener);
        r600.addActionListener(listener);
        r480.addActionListener(listener);
        items.get(currentHeight).setSelected(true);

        bar.add(menu);
        bar.add(menuRes);
        this.setJMenuBar(bar);
    }

    private void generateRightPanel(Camera cam) {
//        cam.init(AppPreferences.getInstance().getChoosenCamera());
        DartCamPanel pan = new DartCamPanel(cam);
        settingsDialog.camera = cam;
        splitPaneHorizontal.setBottomComponent(pan);
        splitPaneHorizontal.setTopComponent(new JPanel());
        splitPaneVertical.setRightComponent(splitPaneHorizontal);
        splitPaneVertical.setContinuousLayout(true);

        splitPaneHorizontal.setDividerLocation(AppPreferences.getInstance().getDividerLocationHorizontal());

        Graphics2D g = (Graphics2D)pan.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

//        cam.start();
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

        secondCamDialog.add(view1);
        JTextField textField = new JTextField("FaceCam");
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Arial", Font.ITALIC, 20));
        textField.setForeground(Color.RED);
        textField.setEditable(false);
        textField.setBackground(Color.decode("#EEEEEE"));
        secondCamDialog.add(textField,BorderLayout.NORTH);
        browser1.loadURL(AppPreferences.getInstance().getSecondCamURL());
        secondCamDialog.setBounds(AppPreferences.getInstance().getSecondCamBounds());
        secondCamDialog.setResizable(true);
        secondCamDialog.setVisible(true);
        secondCamDialog.setAlwaysOnTop(true);
    }
}
