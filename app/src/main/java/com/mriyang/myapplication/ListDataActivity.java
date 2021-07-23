package com.mriyang.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.adapter.RecyclerViewAdapter;
import com.mriyang.model.data_anggota;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity implements RecyclerViewAdapter.dataListener {

    // Deklarasi Variable Untuk Reclycle View

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Deklarasi variable database refence dan Arraylist dengan parameter model class kita

    private DatabaseReference reference;
    private ArrayList<data_anggota> dataAnggota;
    private FloatingActionButton fab,fab2;

    private EditText searchview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        recyclerView = findViewById(R.id.datalist);
        MyRecycleView();

        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.menutama);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActivity.this,MenuAnggotaActivity.class);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        // Ubah Getdata
        GetData("");

        // Inisialisasi Dengan fungsi SearchView
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
                    GetData(s.toString());
                } else {
                    GetData("");
                }
            }
        });

    }




    // Mengambil data dari database manampilkan data ke adapter
    private void GetData(String data) {
        Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar...",Toast.LENGTH_SHORT).show();

        // Mendapatkan Refensi database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Anggota").orderByChild("nama").startAt(data).endAt(data+"\uf8ff").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Inisialisasi arraylist

                        dataAnggota = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada data snapshot ke data KTP
                            data_anggota anggota = snapshot.getValue(data_anggota.class);

                            // Mengambil Primary key untuk proses update dan delete
                            anggota.setKey(snapshot.getKey());

                            dataAnggota.add(anggota);

                        }

                        // Inisiasi adapter dan data mahasiswa dalam bentuk array
                        adapter = new RecyclerViewAdapter(dataAnggota,ListDataActivity.this);


                        // Memasang adapter pada Recycle View

                        recyclerView.setAdapter(adapter);
                        ;
                        Toast.makeText(getApplicationContext(),"Data Berhasil Di Muat",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Data Gagal Di muat",Toast.LENGTH_SHORT).show();
                        Log.e("MyListActivity",databaseError.getDetails()+""+databaseError.getMessage());
                    }
                });
    }

    // Metode yang berisi mengatur Reclycle view
    private void MyRecycleView(){
        // Membuat layout mamager dan membuat list vertikal
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Membuat Underline Pada item di dalam List

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onDeleteData(data_anggota data, int position) {
        /* Kode ini di panggil ketika methot ondeletedata
        di panggil dari adapter reclycle melalui interface
        kemudian menghapus data berdasarkan primary key data tersebut
        jika berhasil maka akan muncul toast
         */
        if(reference !=null){
            reference.child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Anggota").child(data.getKey())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ListDataActivity.this,"Data Berhasil Di Hapus",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}