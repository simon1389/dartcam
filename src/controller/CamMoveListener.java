package controller;

import model.CamPosition;
import model.Camera;
import model.MoveableElement;

import java.awt.*;
import java.awt.event.*;

public class CamMoveListener implements MouseListener, MouseWheelListener, MouseMotionListener{

    private Point lastPoint;
    private CamPosition camPosition;

    public CamMoveListener(CamPosition camPosition) {
        this.camPosition = camPosition;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastPoint == null) {
            lastPoint = e.getPoint();
        } else {
            int deltaX = e.getX() - lastPoint.x;
            int deltaY = e.getY() - lastPoint.y;
            lastPoint = e.getPoint();
            camPosition.move(deltaX, deltaY);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        camPosition.zoom(e.getWheelRotation() > 0);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        camPosition.showZoomLevel = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        camPosition.showZoomLevel = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        elementToMove = camera.camPosition;
//        for (ZoomedArea za : camera.zoomedAreas) {
//            if (za.getScaledOriginRectToZoom().contains(e.getPoint())) {
//                za.drawOrigin = true;
//                elementToMove = za;
//            } else {
//                za.drawOrigin = false;
//            }
//        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
}
