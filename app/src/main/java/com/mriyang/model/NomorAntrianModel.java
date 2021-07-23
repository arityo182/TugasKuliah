package com.mriyang.model;

public class NomorAntrianModel  {

    private String nomor;
    private String nama;
    private String poli;
    private String waktu;
    private String penjamin;
    private String jenisperiksa;
    private Boolean status = false;



    public NomorAntrianModel(){

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

    public String getJenisperiksa() {
        return jenisperiksa;
    }

    public void setJenisperiksa(String jenisperiksa) {
        this.jenisperiksa = jenisperiksa;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public NomorAntrianModel(String nomor, String nama, String poli, String waktu, String penjamin, String jenisperiksa, Boolean status) {
        this.nomor = nomor;
        this.nama = nama;
        this.poli = poli;
        this.waktu = waktu;
        this.penjamin = penjamin;
        this.jenisperiksa = jenisperiksa;
        this.status = status;
    }

}
