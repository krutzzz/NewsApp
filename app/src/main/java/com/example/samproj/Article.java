package com.example.samproj;

import java.util.List;

public class Article {

    private String Caption;
    private String Description;
    private int Image_of_an_article;

    public Article(String Caption, String description, int pic) {

    this.Caption=Caption;
    this.Description=description;
    this.Image_of_an_article=pic;

    }


    public String getCaption(){
        return Caption;
    }
    public void setCaption(String description){
        Caption=description;
    }

    public int getImage_of_an_article() {
        return Image_of_an_article;
    }
    public void setImage_of_an_article(int image_of_an_article){
        Image_of_an_article=image_of_an_article;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


     private List<Article> articles;

    public void initializeData(){


    }

}
