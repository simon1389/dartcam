package model;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDevice;
import com.jogamp.common.nio.ByteBufferInputStream;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_videoio.VideoCapture;
import view.DartCamPanel;
import view.FramelessDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Camera extends Observable implements Runnable {
    private static List<String> cameraNames;

    private volatile boolean running = true;
    private Thread camThread;
    private int frameCounter = 0;
    public int paintCounter = 0;

    private VideoCapture camera;
    public BufferedImage bufferedImage;

    public VolatileImage volatileImage;
    private Graphics2D volatileContext;

    private GraphicsConfiguration gConfig;
    private Mat frame;
    private byte[] byteBuffer;

//    public ZoomedArea[] zoomedAreas;

//    private DartCamPanel dartCamPanel;

//    public Camera(DartCamPanel dartCamPanel) {
////        this.dartCamPanel = dartCamPanel;
//    }

    public Camera() {
//        this.dartCamPanel = dartCamPanel;
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
//        this.reset();
//        this.zoomedAreas = new ZoomedArea[]{new ZoomedArea(this)};
        start();
    }

//    public CamPosition getCamPosition() {
//        return this.camPosition;
//    }

    public VideoCapture getInternalCamera() {
        return camera;
    }

    public Mat getFrame() {
        return frame;
    }

    public byte[] getByteBuffer() {
        return this.byteBuffer;
    }

//    public void reset() {
//        this.camPosition.reset();
//    }

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

    @Override
    public void run() {
//    testFPS();
//        this.doRepaints();
        running = true;
        while(running){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            dartCamPanel.repaint();
            if (camera.read(frame)){
                frameCounter++;
                ByteBuffer bb = frame.createBuffer();
//                frame.create(new opencv_core.Size(100,100), 1);
//                BufferedImage img = gConfig.createCompatibleImage(frame.size().width(), frame.size().height());
//                img.
                bb.get(byteBuffer, 0, bb.capacity());
                volatileContext.drawImage(bufferedImage, 0, 0, null);
//                System.out.println(images.size());
                setChanged();
                notifyObservers();
            }
        }
    }

    private void testFPS() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int frameCounterStart = frameCounter;
                    int paintCounterStart = paintCounter;
                    System.out.println(System.currentTimeMillis());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int frames = frameCounter - frameCounterStart;
                    int paints = paintCounter - paintCounterStart;
                    System.out.println(System.currentTimeMillis());
                    System.out.println("FPS: " + frames);
                    System.out.println("PPS: " + paints);
                }
            }
        });
        t.start();
    }

    public static java.util.List<String> getCameraNames() {
        if (cameraNames == null) {
            cameraNames = new ArrayList<String>();
            List<Webcam> availableCams = Webcam.getWebcams();
            for (Webcam cam: availableCams) {
                cameraNames.add(((WebcamDefaultDevice)cam.getDevice()).getDeviceName());
            }
        }

        return cameraNames;
    }
}
