package com.mriyang.model;

public class NomorBpjs {

    private String nomor;
    private String nama;
    private String poli;
    private String waktu;
    private String penjamin;
    private String nomorBpjs;
    private Boolean status=false;


    public NomorBpjs(){

    }
    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPoli() {
        return poli;
    }

    public void setPoli(String poli) {
        this.poli = poli;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getPenjamin() {
        return penjamin;
    }

    public void setPenjamin(String penjamin) {
        this.penjamin = penjamin;
    }

    public String getNomorBpjs() {
        return nomorBpjs;
    }

    public void setNomorBpjs(String nomorBpjs) {
        this.nomorBpjs = nomorBpjs;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public NomorBpjs(String nomor, String nama, String poli, String waktu, String penjamin, String nomorBpjs, Boolean status) {
        this.nomor = nomor;
        this.nama = nama;
        this.poli = poli;
        this.waktu = waktu;
        this.penjamin = penjamin;
        this.nomorBpjs = nomorBpjs;
        this.status = status;
    }


}
