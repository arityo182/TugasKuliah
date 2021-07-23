package com.mriyang.model;

public class Informasi {
    private String judul;
    private String isiinfo;

    public Informasi() {

    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsiinfo() {
        return isiinfo;
    }

    public void setIsiinfo(String isiinfo) {
        this.isiinfo = isiinfo;
    }

    public Informasi(String judul, String isiinfo) {
        this.judul = judul;
        this.isiinfo = isiinfo;
    }
}

