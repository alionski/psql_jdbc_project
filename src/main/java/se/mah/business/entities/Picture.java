package se.mah.business.entities;

/**
 * Created by aliona on 2017-07-23.
 */
public class Picture {
    private int picture_id;
    private String altimage;
    private String url;

    public Picture() {  }

    public int getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(int picture_id) {
        this.picture_id = picture_id;
    }

    public String getAltimage() {
        return altimage;
    }

    public void setAltimage(String altimage) {
        this.altimage = altimage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
