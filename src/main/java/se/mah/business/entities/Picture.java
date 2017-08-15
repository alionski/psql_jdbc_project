package se.mah.business.entities;

/**
 * Created by aliona on 2017-07-23.
 */
public class Picture {
    private int pictureId;
    private String alttext;
    private String url;
    private String descriptionForArticle;

    public Picture() {  }

    public int getPictureId() {return pictureId; }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getAlttext() {
        return alttext;
    }

    public void setAlttext(String alttext) {
        this.alttext = alttext;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescriptionForArticle() {
        return descriptionForArticle;
    }

    public void setDescriptionForArticle(String descriptionForArticle) {
        this.descriptionForArticle = descriptionForArticle;
    }
}
