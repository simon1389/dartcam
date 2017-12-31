package model.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Map;

public class DartMachineAnalyzer implements Runnable{

    private VolatileImage imgRef;

    private BufferedImage snapShot;

    private Map<String, Rectangle> ocrAreas;

    public DartMachineAnalyzer(VolatileImage imgRef) {
        this.imgRef = imgRef;
        ocrAreas = new HashMap<>();
    }

    public void addOCRArea(String key, Rectangle area) {
        ocrAreas.put(key, area);
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String version = System.getProperty("java.specification.version");
        if (!version.startsWith("1.")) {
            System.setProperty("java.specification.version", "1." + version);
        }
        Tesseract instance = Tesseract.getInstance();
        instance.setLanguage("automat-2");

        while(true) {
            System.out.println("Get new image...");
            long m = System.currentTimeMillis();
            snapShot = imgRef.getSnapshot();
            System.out.println((System.currentTimeMillis() - m) + " ms for snapshot");
            System.out.println("Doing ocr...");
            for (Map.Entry<String, Rectangle> entry : ocrAreas.entrySet()) {
                String key = entry.getKey();
                Rectangle rec = entry.getValue();
                try {
                    String result = instance.doOCR(snapShot, rec);
                    System.out.println(key + ": " + result);
                } catch (TesseractException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
