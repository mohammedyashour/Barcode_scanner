package com.example.barcodescanner.Data;

public class User {

    String Email,Password, TopBadge,Uid, Username, dateofbirth,imageuri;
    public User() {

    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getTopBadge() {
        return TopBadge;
    }

    public void setTopBadge(String topBadge) {
        TopBadge = topBadge;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public User(String email, String password, String topBadge, String uid, String username, String dateofbirth, String imageuri) {
        Email = email;
        Password = password;
        TopBadge = topBadge;
        Uid = uid;
        Username = username;
        this.dateofbirth = dateofbirth;
        this.imageuri = imageuri;
    }
}
