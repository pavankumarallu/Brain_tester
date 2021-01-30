package com.android.braintester.Category_work;

public class CategoryModel {

    private String name;
    private Integer sets;
    private String url;
    public CategoryModel(){

    }

    public CategoryModel(String name, Integer sets, String url) {
        this.name = name;
        this.sets = sets;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
