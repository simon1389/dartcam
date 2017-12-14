package view;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import controller.CamMoveListener;
import model.CamPosition;
import model.Camera;
import model.ZoomedArea;

public class DartCamPanel extends JPanel implements Observer {

    private Camera cameraModel;
    private CamPosition camPosition;

    private long m;

    public DartCamPanel(Camera camera) {
        this.setIgnoreRepaint(true);
        this.cameraModel = camera;
        this.camPosition = new CamPosition();
//        this.cameraModel.zoomedAreas = new ZoomedArea[]{new ZoomedArea(camera, camPosition)};
        setZoomAndMoveListener();
        camera.addObserver(this);
    }

    public Camera getCameraModel() {
        return cameraModel;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        cameraModel.paintCounter++;
        g.drawImage(cameraModel.volatileImage, camPosition.refPoint.x, camPosition.refPoint.y, (int)(cameraModel.volatileImage.getWidth() * camPosition.zoomLevel), (int)(cameraModel.volatileImage.getHeight() * camPosition.zoomLevel), this);
//      drawZoomedAreas(g);
    }

//    private void drawZoomedAreas(Graphics g) {
//        if (cameraModel.zoomedAreas != null) {
//            for (ZoomedArea za : cameraModel.zoomedAreas) {
//                za.draw(g);
//            }
//        }
//    }

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

//    private void doRepaints() {
//        DartCamPanel panel = this;
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    panel.repaint();
////                    panel.paintImmediately(panel.getBounds());
//                }
//            }
//        });
//        t.start();
//    }

    private void setZoomAndMoveListener() {
//        cameraModel = new Camera(this);
        CamMoveListener camListener = new CamMoveListener(camPosition);
        addMouseListener(camListener);
        addMouseWheelListener(camListener);
        addMouseMotionListener(camListener);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
