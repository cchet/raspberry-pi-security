package at.rpisec.sensor.impl.listener;

import java.io.File;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class ImageData extends DeviceData {
    private String imageFilePath;
    private int imageWidth;
    private int imageHeight;
    private int imageQuality;
    private boolean imageFlipped;

    public ImageData() {
    }

    public ImageData(String imageFilePath, int imageWidth, int imageHeight, int imageQuality, boolean imageFlipped) {
        this.imageFilePath = imageFilePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageQuality = imageQuality;
        this.imageFlipped = imageFlipped;
    }

    /**
     *
     * @return Filepath with trailing path separator
     */
    public String getImageFilePath() {

        if(!imageFilePath.endsWith(File.separator))
            imageFilePath += File.separator;

        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(int imageQuality) {
        this.imageQuality = imageQuality;
    }

    public boolean isImageFlipped() {
        return imageFlipped;
    }

    public void setImageFlipped(boolean imageFlipped) {
        this.imageFlipped = imageFlipped;
    }
}
