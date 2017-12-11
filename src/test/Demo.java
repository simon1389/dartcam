package test;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

public class Demo implements Runnable {
    //final int INTERVAL=1000;///you may use interval
    IplImage image;
    CanvasFrame canvas = new CanvasFrame("Web Cam");
    public Demo() throws CanvasFrame.Exception {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void run() {
//        FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setFormat("MJPG");
//        grabber.setImageMode(FrameGrabber.ImageMode.COLOR);
        //Java2DFrameConverter.
//        grabber.setFormat();

        int i=0;
        try {
            grabber.start();
            Frame img;
            while (true) {
                img = grabber.grab();
                if (img != null) {
//                    cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
//                    cvSaveImage((i++)+"-aa.jpg", img);
                    // show image on window
                    canvas.showImage(img);
                }
                //Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
        }
    }



    public static void main(String[] args) throws CanvasFrame.Exception {
        System.loadLibrary("opencv_java331");
        Demo gs = new Demo();
        Thread th = new Thread(gs);
        th.start();
    }
}