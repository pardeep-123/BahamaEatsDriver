package com.bahamaeatsdriver.model_class;

/**
 * Created by root on 18/11/19.
 */

public class ImageVideoModel {
    String id="";
    String imageVideoPath="";
    String thumbnailPath="";
    String extension="";
    String type="";

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }

    String isAdded="";

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    String isUpload="";

    public String getDurationVideo() {
        return durationVideo;
    }

    public void setDurationVideo(String durationVideo) {
        this.durationVideo = durationVideo;
    }

    String durationVideo="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageVideoPath() {
        return imageVideoPath;
    }

    public void setImageVideoPath(String imageVideoPath) {
        this.imageVideoPath = imageVideoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
