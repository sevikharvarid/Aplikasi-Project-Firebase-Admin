package com.example.projectadmin;

public class user {

    public String Name,Email,Phone;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public user()
    {

    }

    public user(String Name,String Email,String Phone )
    {
        this.Name=Name;
        this.Email=Email;
        this.Phone=Phone;

    }
}
