package com.mingle.myapplication.research.model;

public class SampleData {
    private String title;
    private String link;
    private String image;
    private String lprice;
    private String mallName;

    public SampleData(String title, String link, String image, String lprice, String mallName){
        this.title = title;
        this.link = link;
        this.image = image;
        this.lprice = lprice;
        this.mallName = mallName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLprice() {
        return lprice;
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }
}
