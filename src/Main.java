import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.ds.javacv.JavaCvDriver;
import com.github.sarxos.webcam.ds.nativeapi.NativeWebcamDriver;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import view.DartMainForm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.util.NixVideoDevUtils;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Main {

//    private static List<MediaListItem> EMPTY = new ArrayList<MediaListItem>();
//    private static final MediaListItem dev0 = new MediaListItem("BliBlaBlub", "qtcapture://0xfa20000005ac8509", EMPTY);
//    private static final MediaListItem dev1 = new MediaListItem("Blubbi", "qtcapture://0xfd12000005a39230", EMPTY);


//    static {
//        Webcam.setDriver(new VlcjDriver(Arrays.asList(dev1)));
//    }

//    static {
//        Webcam.setDriver(new GStreamerDriver(new ArrayList(Arrays.asList("image/jpeg", "video/x-raw-rgb", "video/x-raw-yuv"))));
//    }

//    static {
//        Webcam.setDriver(new JavaCvDriver());
//    }

//    static {
//        Webcam.setDriver(new NativeWebcamDriver());
//    }


    public static void main(String args[])  {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.accthreshold","0");
        System.setProperty("apple.laf.useScreenMenuBar", "true");


//        System.loadLibrary("opencv_java331");
//        opencv_videoio.VideoCapture cam = new opencv_videoio.VideoCapture(0);
//        cam.release();
//        opencv_videoio.VideoCapture camera = new VideoCapture(0);
//        opencv_videoio.VideoCapture cam = new opencv_videoio.VideoCapture(0);
//        cam.release();
//        cam.close();
//        String[] args2 = new String[0];
//        String name = GStreamerDriver.class.getSimpleName();
//        Gst.init(GStreamerDriver.class.getSimpleName(), args2);
        //Runtime.getRuntime().addShutdownHook(new GStreamerDriver.GStreamerShutdownHook());
//        List<Webcam> webcams = Webcam.getWebcams();
//        System.out.println(webcams.get(0).getName());
//        System.out.println(webcams.get(1).getName());

//        JFrame window = new JFrame("Webcam Panel");
//        Dimension[] sizes = new Dimension[] {new Dimension(1920,1080)};
//        webcams.get(1).setViewSize(new Dimension(1280,720));
//        window.add(new WebcamPanel(webcams.get(1)));
//        window.setResizable(false);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.pack();
//        window.setVisible(true);
//        List<Webcam> webcams = Webcam.getWebcams();

//
//        System.out.format("Webcams detected: %d \n", webcams.size());
//
//        for (int i = 0; i < webcams.size(); i++) {
//            System.out.println(webcams.get(i).getName());
////            System.out.format("%d: %s \n", i + 1, webcams.get(i));
//        }

//        JFrame frame = new JFrame("vlcj Tutorial");
//
//        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
//
//        frame.setContentPane(mediaPlayerComponent);
//
//        frame.setLocation(100, 100);
//        frame.setSize(500, 500);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//
//        mediaPlayerComponent.getMediaPlayer().playMedia("/test.m4v");



        DartMainForm form = new DartMainForm();
        SwingUtilities.invokeLater(form);

//        boolean found = new NativeDiscovery().discover();
//        System.out.println(found);
//        System.out.println(LibVlc.INSTANCE.libvlc_get_version());


//        Tesseract instance = Tesseract.getInstance();
//        instance.setLanguage("automat-2");
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        int i = 0;
//        while(true) {
//            try {
//                System.out.println("Get new image...");
//                BufferedImage img = form.webcam.getImage();
//                ImageIO.write(img, "png", new File("test.png"));
//
//                System.out.println("Doing ocr...");
//                String result = instance.doOCR(new File("test.png"));
//                System.out.println(result);
//
//            } catch (TesseractException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            form.pointsLabel.setText("Zähler: " + i);
//            i++;
//        }
    }
}
