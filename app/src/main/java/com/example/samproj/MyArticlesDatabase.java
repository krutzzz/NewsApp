package com.example.samproj;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class MyArticlesDatabase extends RealmObject {

    @Required
    private String caption;

    private String content;


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }







}
