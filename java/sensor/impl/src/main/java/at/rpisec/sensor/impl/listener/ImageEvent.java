package at.rpisec.sensor.impl.listener;

import at.rpisec.sensor.api.listener.IncidentEvent;

import java.util.EventObject;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class ImageEvent extends EventObject implements IncidentEvent {

    private ImageData imageData;

    public ImageEvent(Object source, ImageData imageData) {
        super(source);
        this.imageData = imageData;
    }

    public ImageData getImageData() {
        return this.imageData;
    }
}
