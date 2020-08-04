package com.example.projectadmin;

public class admin {
    public admin(String name, String nip, String email) {
        this.name = name;
        this.nip = nip;
        this.email = email;
    }
    public admin(){
    }

    private String name;
    private String nip;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
