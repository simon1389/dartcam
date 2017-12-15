package model;

import view.DartCamPanel;

import java.awt.*;

public class CamPosition implements MoveableElement {
    private static final double MAX_ZOOM = 3.0;
    private static final double MIN_ZOOM = 0.3;
    private static final double ZOOM_STEP = 0.01;

    public Point refPoint = new Point(0,0);
    public double zoomLevel = 1;
    public boolean showZoomLevel = false;
    public DartCamPanel panel;

    public void move(int deltaX, int deltaY) {
        refPoint.translate(deltaX, deltaY);
    }

    public void zoom(boolean zoomOut) {
        double delta;
        if (zoomOut) {
            delta = -ZOOM_STEP;
            if (zoomLevel == MIN_ZOOM) {
                return;
            }
        } else {
            delta = ZOOM_STEP;
            if (zoomLevel == MAX_ZOOM) {
                return;
            }
        }

        double zoomLevel = this.zoomLevel;
        zoomLevel += delta;
        zoomLevel = Math.max(Math.min(MAX_ZOOM, zoomLevel),MIN_ZOOM);
        int deltaX = (int)(-delta*(1/zoomLevel*(panel.getWidth()/2-refPoint.x)));
        int deltaY = (int)(-delta*(1/zoomLevel*(panel.getHeight()/2-refPoint.y)));
        move(deltaX,deltaY);
        this.zoomLevel = Math.round(zoomLevel * 100.0) / 100.0;
    }

    public void reset() {
        zoomLevel = 1;
        refPoint = new Point(0,0);
    }
}
