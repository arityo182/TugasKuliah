package com.mriyang.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.adapter.Riwayat_Adapter;
import com.mriyang.model.NomorAntrianModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RiwayatDaftarActivity extends AppCompatActivity {

    FirebaseDatabase db;
    RecyclerView mRecyclerView;
    String date = "";
    String date2 = "";
    private ArrayList<NomorAntrianModel> datainfo;
    ArrayList<String> dataAkun = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_riwayat_daftar);
        mRecyclerView = findViewById(R.id.datalist1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        db = FirebaseDatabase.getInstance();
        datainfo = new ArrayList<>();

        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        date = format2.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        Date date1 = calendar.getTime();
        date2 = format2.format(date1);

        // Ubah Getdata
        getData();


    }


    //membuat tulisan menjadi ganti secara otomatis
    private void getData() {
        FirebaseDatabase dbUser = FirebaseDatabase.getInstance();
        DatabaseReference Nama = dbUser.getReference().child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Anggota");
        Nama.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot document : task.getResult().getChildren()) {
                        dataAkun.add(document.child("nama").getValue().toString());
                        tools.setSharedPreference(RiwayatDaftarActivity.this,"namaRiwayat", document.child("nama").getValue().toString());
                    }
                }
            }
        });

        final DatabaseReference dbAkun = db.getReference().child("Pengguna").child("Daftar");
        dbAkun.child(date).orderByChild("nama").startAt(tools.getSharedPreferenceString(RiwayatDaftarActivity.this,"namaRiwayat","")).endAt(tools.getSharedPreferenceString(RiwayatDaftarActivity.this,"namaRiwayat","")+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()){
                    NomorAntrianModel data = new NomorAntrianModel(
                            document.child("nomor").getValue().toString(),
                            document.child("nama").getValue().toString(),
                            document.child("poli").getValue().toString(),
                            document.child("waktu").getValue().toString(),
                            document.child("penjamin").getValue().toString(),
                            document.child("jenisperiksa").getValue().toString(),
                            Boolean.getBoolean(document.child("status").getValue().toString())
                    );
                    datainfo.add(data);
                }
                RecyclerView.Adapter mAdapter = new Riwayat_Adapter(datainfo,RiwayatDaftarActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Data Gagal Di muat", Toast.LENGTH_SHORT).show();
                Log.e("Riwayat", databaseError.getDetails() + "" + databaseError.getMessage());
            }
        });
    }
}
