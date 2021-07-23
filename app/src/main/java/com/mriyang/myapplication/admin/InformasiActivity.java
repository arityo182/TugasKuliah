package com.mriyang.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.adapter.informasi_adapter;
import com.mriyang.model.Informasi;
import com.mriyang.myapplication.R;

import java.util.ArrayList;

public class InformasiActivity extends AppCompatActivity {
    private ImageView tomboltambah;
    private EditText searchview;
    FirebaseDatabase db;
    RecyclerView mRecyclerView;

    private ArrayList <Informasi> datainfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_informasi_dokter);
        mRecyclerView = findViewById(R.id.datalist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        db = FirebaseDatabase.getInstance();
        datainfo = new ArrayList<>();


        tomboltambah = findViewById(R.id.prefences);
        tomboltambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformasiActivity.this, TambahInformasiActivity.class);
                startActivity(intent);
            }
        });

        // Ubah Getdata
        getData("");

        searchview = findViewById(R.id.etSearch);
        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null){
                    getData(s.toString());
                } else {
                    getData("");
                }
            }
        });

    }


    //membuat tulisan menjadi ganti secara otomatis
    private void getData(String data) {
        final DatabaseReference dbAkun = db.getReference().child("admin").child("informasi");
        dbAkun.orderByChild("judul").startAt(data).endAt(data+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Informasi data = ds.getValue(Informasi.class);
                    datainfo.add(data);
                }
                RecyclerView.Adapter mAdapter = new informasi_adapter(datainfo);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Data Gagal Di muat",Toast.LENGTH_SHORT).show();
                Log.e("MyListActivity",databaseError.getDetails()+""+databaseError.getMessage());
            }
        });
    }
}



