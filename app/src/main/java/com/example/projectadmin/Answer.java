package com.example.projectadmin;

public class Answer {
    private String description;
    private String author;

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String nip;
    private String username;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public Answer() {

    }

    public Answer(String username, String nip, String key, String description, String author) {
        this.username = username;
        this.nip = nip;
        this.key = key;
        this.description = description;
        this.author = author;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
