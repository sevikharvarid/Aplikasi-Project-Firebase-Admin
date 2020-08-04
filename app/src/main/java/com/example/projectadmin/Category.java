package com.example.projectadmin;

public class Category {
    public Category(String title) {
        this.title = title;
    }

    public Category() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
