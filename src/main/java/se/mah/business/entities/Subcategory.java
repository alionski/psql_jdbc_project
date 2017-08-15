package se.mah.business.entities;

/**
 * Created by aliona on 2017-07-22.
 */
public class Subcategory {
    private String subCat;
    private String mainCat;
    private int subCatId;

    public Subcategory() { }

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

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }
}
