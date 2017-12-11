package view;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_videoio.*;

public class DartCamPanel extends JPanel implements Runnable {


    private int deviceNumber;
    private VideoCapture camera;
    private BufferedImage bufferedImage;

    private VolatileImage volatileImage;
    private Graphics2D volatileContext;

    private GraphicsConfiguration gConfig;
    private Mat frame;
    private byte[] byteBuffer;

    public DartCamPanel(int deviceNumber) {
        this.deviceNumber = deviceNumber;
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
    }

    @Override
    public void paint(Graphics g) {
//        g.drawImage(bufferedImage, 0, 0, this);
        Dimension newDimension = this.getScaledDimension();
        g.drawImage(volatileImage, 0, 0, newDimension.width, newDimension.height, this);
    }

    public Dimension getScaledDimension() {
        Dimension imgSize = new Dimension(1920, 1080);
        Dimension boundary = this.getSize();

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    @Override
    public void run() {
        while(true){
            repaint();
            if (camera.read(frame)){
                ByteBuffer bb = frame.createBuffer();
                bb.get(this.byteBuffer, 0, bb.capacity());
                volatileContext.drawImage(this.bufferedImage, 0, 0, null);
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
