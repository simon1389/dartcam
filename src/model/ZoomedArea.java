package model;

import java.awt.*;

public class ZoomedArea {
    private Rectangle originRectToZoom;
    private Rectangle destinationRect;

    public ZoomedArea() {
        this.originRectToZoom = new Rectangle(0,0,100,100);
    }

    public void setOriginRectToZoom(Rectangle originRectToZoom) {
        this.originRectToZoom = originRectToZoom;
    }

    public void setDestinationRect(Rectangle destinationRect) {
        this.destinationRect = destinationRect;
    }

    public Rectangle getOriginRectToZoom() {
        return this.originRectToZoom;
    }

    public Rectangle getDestinationRect() {
        return this.destinationRect;
    }

}
