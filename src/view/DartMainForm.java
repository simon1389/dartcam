package view;

import com.sun.jna.Platform;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import model.AppPreferences;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DartMainForm extends JFrame implements Runnable {

    private JPanel mainPanel = new JPanel();
    private JSplitPane splitPane = new JSplitPane();
    private JScrollPane camPanel;

    private JDialog settingsDialog;
    private JDialog secondCamDialog;


    public DartMainForm() {
        this.setListeners();
    }

    @Override
    public void run() {
        this.setMenuBar();

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
//        camPanel.setLayout(new BorderLayout());

        this.setContentPane(mainPanel);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0,0, screenSize.width, screenSize.height);

        if (Platform.isMac()) {
            this.setUndecorated(true);
        }

        this.setVisible(true);

        this.generateRightPanel();
        this.generateLeftPanel();

        splitPane.setDividerLocation(AppPreferences.getInstance().getDividerLocation());
//        Webcam w = Webcam.getDefault();
//        Dimension d = new Dimension(640, 480);
//        w.setCustomViewSizes(new Dimension[] { d });
//        w.setViewSize(d);
//        new WebcamStreamer(8080, w, 30, true);
//        while (true) {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void setMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Dart");

        JMenuItem settings = new JMenuItem("Einstellungen");
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (settingsDialog == null) {
                    settingsDialog = new Settings();
                    settingsDialog.pack();
                }
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

    private void generateLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        splitPane.setLeftComponent(panel);

        Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
        BrowserView view = new BrowserView(browser);

        panel.add(view, BorderLayout.CENTER);
        browser.loadURL(AppPreferences.getInstance().getLeftPanelURL());


        secondCamDialog = new JDialog();
        Browser browser1 = new Browser(BrowserType.LIGHTWEIGHT);
        BrowserView view1 = new BrowserView(browser1);

        secondCamDialog.getContentPane().add(view1, BorderLayout.CENTER);
        browser1.loadURL(AppPreferences.getInstance().getSecondCamURL());
        secondCamDialog.setSize(200,200);
//        secondCamDialog.setUndecorated(false);

//        ComponentResizer cr = new ComponentResizer();
//        cr.registerComponent(secondCamDialog);
//        DragListener listener = new DragListener();
//        secondCamDialog.addMouseListener(listener);
//        secondCamDialog.addMouseMotionListener(listener);
        secondCamDialog.setResizable(true);
        secondCamDialog.setVisible(true);
        secondCamDialog.setAlwaysOnTop(true);

    }

    private void generateRightPanel() {
        this.createWebcamPanels();

//        Browser browser1 = new Browser();
//        BrowserView view1 = new BrowserView(browser1);
//
//        camPanel.add(view1, BorderLayout.CENTER);


//        Webcam cam = Webcam.getDefault(); // take default webcam
//        Dimension d = new Dimension(1920, 1080);
//        cam.setCustomViewSizes(new Dimension[] { d });
//        cam.setViewSize(d);
//        WebcamStreamer stream = new WebcamStreamer(8080, cam, 30, true);
//        browser1.loadURL("http://127.0.0.1:8080");
    }

    private void createWebcamPanels() {
        DartCamPanel pan = new DartCamPanel(0);
//        camPanel = new JScrollPane(pan);
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        sp.setBottomComponent(pan);
        sp.setTopComponent(new JPanel());
        splitPane.setRightComponent(sp);
        splitPane.setContinuousLayout(true);

        pan.start();

//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OpenCVFrameConverter toMat = new OpenCVFrameConverter.ToMat();
//                Java2DFrameConverter conv = new Java2DFrameConverter();
//                OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(pan.deviceNumber);
//                grabber.setFormat("MJPG");
//
//                int i=0;
//                try {
//                    grabber.start();
//                    Frame img;
//                    while (true) {
//                        pan.repaint();
//                        img = grabber.grab();
//                        if (img != null) {
//                            pan.bufferedImage = conv.convert(img);
////                            BufferedImage copy = new BufferedImage(pan.bufferedImage.getWidth(), pan.bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
////
//////                            createVolatileImage()
////
////                            Graphics2D g2d = copy.createGraphics();
////                            g2d.setColor(Color.WHITE); // Or what ever fill color you want...
////                            g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
////                            g2d.drawImage(pan.bufferedImage, 0, 0, null);
////                            pan.bufferedImage = copy;
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//        });
//        t.start();

//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        Thread.sleep(33);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    pan.repaint();
//                }
//            }
//        });
//        t2.start();

//        List<Webcam> webcams = Webcam.getWebcams();
//        for (Webcam cam: webcams) {
//            if (AppPreferences.getInstance().isWebcamActivated(cam.getName())) {
//                Dimension[] sizes = cam.getViewSizes();
//                //cam.setViewSize(WebcamResolution);
//                //cam.setViewSize(AppPreferences.getInstance().getWebcamDimension(cam.getName()));
//                Dimension d = new Dimension(1920, 1080);
//                cam.setCustomViewSizes(new Dimension[] { d });
//                cam.setViewSize(d);
//                //cam.setViewSize(new Dimension(1920,1080));
//                WebcamPanel panel = new WebcamPanel(cam);
//                panel.setDisplayDebugInfo(true);
//                panel.setFPSDisplayed(true);
//                panel.setImageSizeDisplayed(true);
//                //panel.setPainter();
//                panel.setMirrored(false);
//                camPanel.add(panel, BorderLayout.CENTER);
//            }
//        }
//        webcam = Webcam.getDefault();
//        webcam.setViewSize(WebcamResolution.VGA.getSize());

//        WebcamPanel panel = new WebcamPanel(webcam);
//        panel.setMirrored(false);

//        camPanel.add(panel, BorderLayout.CENTER);

    }

    private void setListeners() {
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        AppPreferences.getInstance().setDividerLocation(splitPane.getDividerLocation());
                    }
                });


        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_E && e.getModifiers() == KeyEvent.CTRL_MASK) {
                        if (settingsDialog == null) {
                            settingsDialog = new Settings();
                            settingsDialog.pack();
                        }
                        settingsDialog.setVisible(true);
                    }
                }
                return false;
            }
        });
    }
}
