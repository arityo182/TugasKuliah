package com.mriyang.model;

public class data_anggota {

    //  Deklarasi Variabel
    private String no_ktp;
    private String nama;
    private String hubungan;
    private String key;
    private String goldar;
    private String gender;
    private String date;
    private String nmrhp;
    private String brtbadan;
    private String alamat;
    private String imageName;
    private String imageURL;



    public data_anggota() {

    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan (String hubungan) {
        this.hubungan = hubungan;
    }


    public String getGoldar(){
        return goldar;
    }
    public void setGoldar( String goldar) {
        this.goldar = goldar;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getNmrhp(){
        return nmrhp;
    }
    public void setNmrhp(String nmrhp){
        this.nmrhp = nmrhp;
    }

    public String getBrtbadan(){
        return brtbadan;
    }
    public void setBrtbadan(String brtbadan){
        this.brtbadan = brtbadan;
    }

    public String getAlamat(){
        return alamat;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName( String imageName) {
        this.imageName = imageName;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL (String imageURL) {
        this.imageURL = imageURL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Konstruktor kosong untuk membaca data snapshot


    public data_anggota(String no_ktp, String nama, String hubungan, String goldar, String gender, String date, String nmrhp, String brtbadan, String alamat, String imageURL) {
        this.no_ktp = no_ktp;
        this.nama = nama;
        this.hubungan = hubungan;
        this.goldar = goldar;
        this.gender = gender;
        this.date = date;
        this.nmrhp = nmrhp;
        this.brtbadan = brtbadan;
        this.alamat = alamat;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.key = key;
    }
}
