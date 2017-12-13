package view;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDevice;
import controller.CamMoveListener;
import model.CamPosition;
import model.ZoomedArea;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_videoio.*;

public class DartCamPanel extends JPanel implements Runnable {
    public static List<String> cameraNames = DartCamPanel.getCameraNames();

    private VideoCapture camera;
    private BufferedImage bufferedImage;

    private VolatileImage volatileImage;
    private Graphics2D volatileContext;

    private GraphicsConfiguration gConfig;
    private Mat frame;
    private byte[] byteBuffer;

    private volatile boolean running = true;
    private Thread camThread;

    private CamPosition camPosition;
    private ZoomedArea[] zoomedAreas;

    private int frameCounter = 0;
    private int paintCounter = 0;

    public DartCamPanel(int deviceNumber) {
        this.setZoomAndMoveListener();
        this.init(deviceNumber);
    }

    public void init(int deviceNumber) {
        this.camera = new VideoCapture(deviceNumber);
        this.gConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        this.frame = new Mat();
        camera.read(frame);
        int frameWidth = frame.size().width();
        int frameHeight = frame.size().height();

        try {
            this.volatileImage = this.gConfig.createCompatibleVolatileImage(frameWidth, frameHeight, new ImageCapabilities(false), 3);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        this.volatileContext = this.volatileImage.createGraphics();

        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        this.bufferedImage = new BufferedImage(frameWidth, frameHeight, type);
        WritableRaster raster = this.bufferedImage.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        this.byteBuffer = dataBuffer.getData();
        this.camPosition.resetPosition();
        this.zoomedAreas = new ZoomedArea[]{new ZoomedArea()};
    }

    @Override
    public void paint(Graphics g) {
        this.paintCounter++;
//        g.drawImage(bufferedImage, 0, 0, this);
//        Dimension newDimension = this.getScaledDimension();
//        g.drawImage(volatileImage, 0, 0, newDimension.width, newDimension.height, this);
        g.drawImage(volatileImage, camPosition.refPoint.x, camPosition.refPoint.y, (int)(volatileImage.getWidth() * camPosition.zoomLevel), (int)(volatileImage.getHeight() * camPosition.zoomLevel), this);
//        this.drawZoomedAreas(g);
    }

    private void drawZoomedAreas(Graphics g) {
        if (zoomedAreas != null) {
            for (ZoomedArea za : zoomedAreas) {
                Rectangle r = za.getOriginRectToZoom();
                BufferedImage zoomedImage = bufferedImage.getSubimage(r.x, r.y, r.width, r.height);
                g.drawImage(zoomedImage, g.getClipBounds().width - zoomedImage.getWidth(), 0, null);
            }
        }
    }

//    public Dimension getScaledDimension() {
//        Dimension imgSize = new Dimension(1920, 1080);
//        Dimension boundary = this.getSize();
//
//        int original_width = imgSize.width;
//        int original_height = imgSize.height;
//        int bound_width = boundary.width;
//        int bound_height = boundary.height;
//        int new_width = original_width;
//        int new_height = original_height;
//
//        // first check if we need to scale width
//        if (original_width > bound_width) {
//            //scale width to fit
//            new_width = bound_width;
//            //scale height to maintain aspect ratio
//            new_height = (new_width * original_height) / original_width;
//        }
//
//        // then check if we need to scale even with the new height
//        if (new_height > bound_height) {
//            //scale height to fit instead
//            new_height = bound_height;
//            //scale width to maintain aspect ratio
//            new_width = (new_height * original_width) / original_height;
//        }
//
//        return new Dimension(new_width, new_height);
//    }

    @Override
    public void run() {
//        this.testFPS();
//        this.doRepaints();
        running = true;
        while(running){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
            if (camera.read(frame)){
                this.frameCounter++;
                ByteBuffer bb = frame.createBuffer();
                bb.get(this.byteBuffer, 0, bb.capacity());
                volatileContext.drawImage(this.bufferedImage, 0, 0, null);
            }
        }
    }

    private void doRepaints() {
        DartCamPanel panel = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    panel.repaint();
//                    panel.paintImmediately(panel.getBounds());
                }
            }
        });
        t.start();
    }

    private void testFPS() {
        DartCamPanel panel = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int frameCounterStart = panel.frameCounter;
                    int paintCounterStart = panel.paintCounter;
                    System.out.println(System.currentTimeMillis());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int frames = panel.frameCounter - frameCounterStart;
                    int paints = panel.paintCounter - paintCounterStart;
                    System.out.println(System.currentTimeMillis());
                    System.out.println("FPS: " + frames);
                    System.out.println("PPS: " + paints);
                }
            }
        });
        t.start();
    }

    public void start() {
        this.camThread = new Thread(this);
        this.camThread.start();
    }

    public void stop() {
        this.running = false;
        try {
            this.camThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setZoomAndMoveListener() {
        camPosition = new CamPosition();
        CamMoveListener camListener = new CamMoveListener(camPosition);
        this.addMouseListener(camListener);
        this.addMouseWheelListener(camListener);
        this.addMouseMotionListener(camListener);
    }


    private static List<String> getCameraNames() {
        List<String> names = new ArrayList<String>();
        List<Webcam> availableCams = Webcam.getWebcams();
        for (Webcam cam: availableCams) {
            names.add(((WebcamDefaultDevice)cam.getDevice()).getDeviceName());
        }

        return names;
    }
}
