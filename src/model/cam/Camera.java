package model.cam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDevice;
import model.ocr.DartMachineAnalyzer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacpp.opencv_videoio.VideoCapture;

import java.awt.*;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Camera extends Observable implements Runnable {
    private static final int DEFAULT_CAMERA_WIDTH = 1280;
    private static final int DEFAULT_CAMERA_HEIGHT = 720;

    private static List<String> cameraNames;

    private volatile boolean running = true;
    private Thread camThread;
    private int frameCounter = 0;
    public int paintCounter = 0;

    private VideoCapture camera;
    public BufferedImage bufferedImage;

    public VolatileImage volatileImage;
    private Graphics2D volatileContext;

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

    public void init(int width, int height) {
        init(AppPreferences.getInstance().getChoosenCamera(), width, height);
    }

    public void init(int deviceNumber) {
        init(deviceNumber, -1, -1);
    }

    public void init(int deviceNumber, int width, int height) {
        this.camera = new VideoCapture(deviceNumber);
        if (width != -1 && height != -1) {
            camera.set(opencv_videoio.CV_CAP_PROP_FRAME_WIDTH, width);
            camera.set(opencv_videoio.CV_CAP_PROP_FRAME_HEIGHT, height);
        } else {
            camera.set(opencv_videoio.CV_CAP_PROP_FRAME_WIDTH, DEFAULT_CAMERA_WIDTH);
            camera.set(opencv_videoio.CV_CAP_PROP_FRAME_HEIGHT, DEFAULT_CAMERA_HEIGHT);
        }

        GraphicsConfiguration gConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        this.frame = new Mat();
        camera.read(frame);
        int frameWidth = frame.size().width();
        int frameHeight = frame.size().height();

        try {
            this.volatileImage = gConfig.createCompatibleVolatileImage(frameWidth, frameHeight, new ImageCapabilities(false), 3);
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

//        DartMachineAnalyzer dma = new DartMachineAnalyzer(volatileImage);
//        dma.addOCRArea("p1", new Rectangle(0,0,200,100));
//        dma.start();
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
    //testFPS();
//        this.doRepaints();
        running = true;

//        g.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
//                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
//                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        g.setRenderingHint(RenderingHints.KEY_DITHERING,
//                RenderingHints.VALUE_DITHER_ENABLE);
//
//        volatileContext.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//        volatileContext.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        volatileContext.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        volatileContext.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
//                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        volatileContext.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
//                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        volatileContext.setRenderingHint(RenderingHints.KEY_DITHERING,
//                RenderingHints.VALUE_DITHER_ENABLE);
        while(running){
            if (camera.read(frame)){
                frameCounter++;

                ByteBuffer bb = frame.createBuffer();
                bb.get(byteBuffer, 0, bb.capacity());

                volatileContext.drawImage(bufferedImage, 0, 0, null);
                setChanged();
                notifyObservers();
            }
        }
        camera.release();
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
