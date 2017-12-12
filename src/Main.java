import view.DartMainForm;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String args[])  {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.accthreshold","0");
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        DartMainForm form = new DartMainForm();
        SwingUtilities.invokeLater(form);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SplashScreen splash = SplashScreen.getSplashScreen();
//                if (splash == null) {
//                    System.out
//                            .println("SplashScreen kann nicht erzeugt werden.");
//                    return;
//                }
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    System.err.println("Thread unterbrochen");
//                }
//                splash.close();
//                form.setVisible(true);
//            }
//        }).start();

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
