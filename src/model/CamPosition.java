package model;

import java.awt.*;

public class CamPosition {
    public Point refPoint = new Point(0,0);
    public double zoomLevel = 1;

    public void moveCam(int deltaX, int deltaY) {
        refPoint.translate(deltaX, deltaY);
    }

    public void zoom(boolean zoomOut) {
        if (zoomOut) {
            double zoomLevel = this.zoomLevel;
            zoomLevel -= 0.01;
            zoomLevel = Math.max(0.3, zoomLevel);
            this.zoomLevel = zoomLevel;
        } else {
            double zoomLevel = this.zoomLevel;
            zoomLevel += 0.01;
            zoomLevel = Math.min(3, zoomLevel);
            this.zoomLevel = zoomLevel;
        }
    }

    public void resetPosition() {
        zoomLevel = 1;
        refPoint = new Point(0,0);
    }
}
