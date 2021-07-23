package com.mriyang.model;

public class Pengguna {
    public String nama, email, password, password2;
    private String key;
    public Pengguna() {

    }
    public Pengguna(String nama, String email, String password, String password2) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }


    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword2() {
        return password2;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}

