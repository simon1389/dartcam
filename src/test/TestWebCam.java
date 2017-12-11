package test;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_videoio.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

//import org.opencv.core.Mat;
//import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TestWebCam extends JPanel{

    public BufferedImage image;
    public VolatileImage volatileImage;
    public Graphics2D g2dContext;
    public static TestWebCam t;
    public int frameWidth;
    public int frameHeight;
    GraphicsConfiguration gfx_config;

    public static void main (String args[]) throws InterruptedException, AWTException {
        System.setProperty("sun.java2d.opengl", "true");
        System.loadLibrary("opencv_java331");

        t = new TestWebCam();
        VideoCapture camera = new VideoCapture(0);
//        camera.set(opencv_videoio.CAP_PROP_FOURCC ,opencv_videoio.CV_FOURCC((byte)'M',(byte)'J',(byte)'P',(byte)'G'));
//        camera.set(opencv_videoio.CV_CAP_PROP_FPS, 1);
//        camera.set(opencv_videoio.CV_CAP_PROP_FRAME_WIDTH, 1024);
//        camera.set(opencv_videoio.CV_CAP_PROP_FRAME_HEIGHT, 768);


        t.gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();


        JFrame frame0 = new JFrame();
        frame0.getContentPane().add(t);
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setSize(1920, 1110);
        frame0.setLocation(0, 0);
        frame0.setVisible(true);


        Mat frame = new Mat();
        camera.read(frame);

        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {
            int type = 0;
            if (frame.channels() == 1) {
                type = BufferedImage.TYPE_BYTE_GRAY;
            } else if (frame.channels() == 3) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
//            t.image = new BufferedImage(frame.size().width(), frame.size().height(), type);
            t.frameWidth = frame.size().width();
            t.frameHeight = frame.size().height();
            t.volatileImage = t.gfx_config.createCompatibleVolatileImage(t.frameWidth, t.frameHeight, new ImageCapabilities(false), 3);
//            t.volatileImage = t.gfx_config.createCompatibleImage(t.frameWidth, t.frameHeight, 3);
            t.g2dContext = t.volatileImage.createGraphics();
//            t.g2dContext.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            t.g2dContext.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);


            t.tmp = new BufferedImage(t.frameWidth, t.frameHeight, type);
            WritableRaster raster = t.tmp.getRaster();
            DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
            t.tmpData = dataBuffer.getData();


            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Graphics2D g = (Graphics2D)t.getGraphics();
//            executorService.execute(new Runnable() {
//                public void run() {
//                    while(true) {
////                    t.repaint();
//                        t.paint(g);
//                        try {
//                            Thread.sleep(20);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });


            while(true){
                t.paint(g);
//                t.repaint();
//                Thread.sleep(33);
                if (camera.read(frame)){
//                    t.image = t.MatToBufferedImage(frame);
                    t.image = t.MatToBufferedImage(frame);

//                    t.image = img;
//                    t.repaint();
//                    t.window(image, "Original Image", 0, 0);

                    //t.window(t.grayscale(image), "Processed Image", 40, 60);

                    //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                    //break;
                }
            }
        }
        camera.release();



//        VideoInputFrameGrabber
//        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//        OpenCVFrameConverter.ToMat toMat = new OpenCVFrameConverter.ToMat();
//        Java2DFrameConverter conv = new Java2DFrameConverter();
//
//        try {
//            grabber.start();
//
//            Frame img;
//            while (true) {
//                t.repaint();
////                Thread.sleep(30);
//                img = grabber.grab();
//                if (img != null) {
////                    cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
////                    cvSaveImage((i++)+"-aa.jpg", img);
//                    // show image on window
////                    toMat.convert(img);
////                    canvas.showImage(img);
////                    t.image = t.MatToBufferedImage(toMat.convert(img));
////                    t.volatileImage = t.toCompatibleImage(conv.getBufferedImage(img));
//                    t.g2dContext.drawImage(conv.getBufferedImage(img), 0, 0, null);
////                    t.image = newImg;
//                }
//                //Thread.sleep(INTERVAL);
//            }
//        } catch (Exception e) {
//        }

    }

    @Override
    public void paint(Graphics g) {
//        g.drawImage(image, 0, 0, this);
//        Graphics2D g2d = (Graphics2D)g;
////        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.drawImage(volatileImage, 0, 0, t.getWidth(), t.getHeight(), this);
    }

    public TestWebCam() {
    }

    public BufferedImage tmp;
    public byte[] tmpData;

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
//        int type = 0;
//        if (frame.channels() == 1) {
//            type = BufferedImage.TYPE_BYTE_GRAY;
//        } else if (frame.channels() == 3) {
//            type = BufferedImage.TYPE_3BYTE_BGR;
//        }
////        VolatileImage volatileImage = createVolatileImage(frame.size().width(), frame.size().height());
//
//        BufferedImage image = new BufferedImage(frameWidth, frameHeight, type);
////        BufferedImage image = gfx_config.createCompatibleImage(
////                frameWidth, frameHeight, 3);
//        WritableRaster raster = image.getRaster();
//        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
//        byte[] data = dataBuffer.getData();
        ByteBuffer bb = frame.createBuffer();
        bb.get(tmpData, 0, bb.capacity());

        t.g2dContext.drawImage(tmp, 0, 0, null);

//        ByteBuffer bb = frame.createBuffer();
//        byte[] myData = new byte[bb.capacity()];
//        bb.get(myData, 0, bb.capacity());
//        t.image.getRaster().setDataElements(0,0, frameWidth, frameHeight, myData);
        //t.image.getRaster().setDataElements(0,0, 1920, 1080, myData);

//        InputStream in = new ByteArrayInputStream(myData);
//        BufferedImage bImageFromConvert = null;
//        try {
//            bImageFromConvert = ImageIO.read(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(b);
//        BufferedImage myimage;
//        try {
//            myimage = ImageIO.read(bais);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        raster.setDataElements();
//        frame.data().get(data,0,0);
//        data = frame.data().getStringBytes();
//        frame.get(0, 0, data);
//        frame.
//        data = frame.getByteBuffer().
//        return bImageFromConvert;
        return image;//this.toCompatibleImage(image);
    }

    private VolatileImage toCompatibleImage(BufferedImage image)
    {
        // obtain the current system graphical settings
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        /*
         * if image is already compatible and optimized for current system
         * settings, simply return it
         */
//        if (image.getColorModel().equals(gfx_config.getColorModel()))
//            return image;

        // image is not optimized, so create a new image that is
//        BufferedImage new_image = gfx_config.createCompatibleImage(
//                image.getWidth(), image.getHeight(), 3);

        VolatileImage new_image = null;
        try {
            new_image = gfx_config.createCompatibleVolatileImage(
                    image.getWidth(), image.getHeight(), new ImageCapabilities(false),3);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return new_image;
    }

}