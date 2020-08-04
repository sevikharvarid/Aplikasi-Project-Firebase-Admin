package com.example.projectadmin;

public class Question {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private int views;
    private String title;
    private String author;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    private String description;

    //    private Answer[] answers;
    public Question() {
    }

    public Question(String category, String description,String id,String author) {

        this.id = id;
        this.category = category;
        this.description = description;
        this.author=author;
    }

//    getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
//
//    public Answer[] getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(Answer[] answers) {
//        this.answers = answers;
//    }
}
