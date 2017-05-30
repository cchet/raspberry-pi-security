package at.rpisec.sensor.impl.listener;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public abstract class DeviceData {
    private String fileName;
    private String fileTimeStamp;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTimeStamp() {
        return fileTimeStamp;
    }

    public void setFileTimeStamp(String fileTimeStamp) {
        this.fileTimeStamp = fileTimeStamp;
    }
}
