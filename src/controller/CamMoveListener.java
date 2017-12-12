package controller;

import model.CamPosition;
import view.DartCamPanel;

import java.awt.*;
import java.awt.event.*;

public class CamMoveListener implements MouseListener, MouseWheelListener, MouseMotionListener{

    private CamPosition camPosition;
    private Point lastPoint;

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
            this.camPosition.moveCam(deltaX, deltaY);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.camPosition.zoom(e.getWheelRotation() > 0);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
}
