package model.cam;

import view.FramelessDialog;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ZoomedArea implements MoveableElement {
    private Camera camera;
    private CamPosition camPosition;
    private Rectangle originRectToZoom;
    private Rectangle destinationRect;
    public boolean drawOrigin = false;

    private FramelessDialog dialog;

    public ZoomedArea(Camera camera, CamPosition camPosition) {
        this.camera = camera;
        this.camPosition = camPosition;
        originRectToZoom = new Rectangle(0,0,100,100);
//        dialog = new FramelessDialog();
//        dialog.setSize(100,100);
//        dialog.setVisible(true);
    }

    public void setOriginRectToZoom(Rectangle originRectToZoom) {
        this.originRectToZoom = originRectToZoom;
    }

    public Rectangle getOriginRectToZoom() {
        return this.originRectToZoom;
    }

    public Rectangle getScaledOriginRectToZoom() {
        Rectangle rect = new Rectangle(
                (int)(originRectToZoom.x  * camPosition.zoomLevel) + camPosition.refPoint.x,
                (int)(originRectToZoom.y * camPosition.zoomLevel) + camPosition.refPoint.y,
                    (int)(originRectToZoom.width * camPosition.zoomLevel),
                    (int)(originRectToZoom.height * camPosition.zoomLevel));


        return rect;
    }

    public void draw(Graphics g) {
        if (drawOrigin) {
            g.setColor(Color.RED);
            Rectangle r = getScaledOriginRectToZoom();
            g.drawRect(r.x, r.y, r.width, r.height);
        }
        BufferedImage zoomedImage = camera.bufferedImage.getSubimage(originRectToZoom.x, originRectToZoom.y, originRectToZoom.width, originRectToZoom.height);
//        dialog.getGraphics().drawImage(zoomedImage, 0, 0, dialog.getWidth(), dialog.getHeight(), dialog);
                g.drawImage(zoomedImage, 500,500,100,100,null);// destinationRect.width, destinationRect.height, null);
    }

    @Override
    public void move(int deltaX, int deltaY) {
        originRectToZoom.translate((int)(deltaX * (1/camPosition.zoomLevel)), (int)(deltaY * (1/camPosition.zoomLevel)));
    }
}
