package com.mriyang.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NomorAntrianActivity extends AppCompatActivity {
    private TextView nmrAntrian,namaPasien, namaPoli, tglPeriksa, penjamin, jenisPeriksa;

    String poli;
    String nomor;
    String id;
    String nmPasien;
    String wkt;
    String s_penjamin, s_jenisPeriksa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomor_antrian);
        poli = getIntent().getStringExtra("poli");
        wkt = getIntent().getStringExtra("waktu");
        nmPasien = getIntent().getStringExtra("nama");
        s_penjamin = getIntent().getStringExtra("penjamin");
        s_jenisPeriksa = getIntent().getStringExtra("jenisPeriksa");

        nmrAntrian = findViewById(R.id.tv_nomorAntrian);
        namaPasien = findViewById(R.id.tv_Nama);
        namaPoli = findViewById(R.id.tv_ruangperiksa);
        tglPeriksa = findViewById(R.id.tv_waktu);
        penjamin = findViewById(R.id.tv_Penjamin);
        jenisPeriksa = findViewById(R.id.tv_Jnispemeriksa);


        nmrAntrian.setText(getIntent().getStringExtra("nomor"));
        namaPasien.setText(nmPasien);
        namaPoli.setText(poli);
        tglPeriksa.setText(wkt);
        penjamin.setText(s_penjamin);
        jenisPeriksa.setText(s_jenisPeriksa);
    }
}
