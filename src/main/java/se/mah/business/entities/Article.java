package se.mah.business.entities;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliona on 2017-05-12.
 */
public class Article {
    private int id;
    private String lead;
    private String headline;
    private String body;
    private String date;
    private String subCat;
    private String mainCat;
    private int subcatId;
    private List<Picture> pictures;
    private List<Person> authors;

    public Article() { }

    public Article(int numAuthors, int numPics) {
        authors = new ArrayList<>();
        for (int i = 0; i < numAuthors; i++) {
            authors.add(new Person());
        }

        pictures = new ArrayList<>();
        for (int i = 0; i < numPics; i++) {
            pictures.add(new Picture());
        }
    }

    public int getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(int subcatId) {
        this.subcatId = subcatId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public String getMainCat() {
        return mainCat;
    }

    public void setMainCat(String mainCat) {
        this.mainCat = mainCat;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
