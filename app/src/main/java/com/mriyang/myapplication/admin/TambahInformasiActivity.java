package com.mriyang.myapplication.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mriyang.model.Informasi;
import com.mriyang.myapplication.R;

public class TambahInformasiActivity extends AppCompatActivity {
    private EditText Informasi1, Judul;
    private Button SaveDataInformasi;
    private Button KembaliInformasi;
    private FirebaseDatabase db;
    boolean isUpdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_informasi);

        Judul = findViewById(R.id.edit_judul);
        Informasi1 = findViewById(R.id.edit_informasi);
        SaveDataInformasi = findViewById(R.id.btn_savedatainformasi);

        KembaliInformasi = findViewById(R.id.btn_kembaliinformasi);

        KembaliInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        db = FirebaseDatabase.getInstance();

        SaveDataInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Informasi1.getText().toString().isEmpty() && Judul.getText().toString().isEmpty() ) {
                    Toast.makeText(TambahInformasiActivity.this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                final DatabaseReference dbAkun = db.getReference().child("admin").child("informasi");

                final String judul = Judul.getText().toString();
                final String isikonten = Informasi1.getText().toString();

                com.mriyang.model.Informasi informasi = new Informasi(judul, isikonten);


                //update
                dbAkun.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot document : task.getResult().getChildren()) {
                                if (document.equals("informasi") && document.child("informasi").toString().equals(Judul.getText().toString())){
                                    isUpdate = true;
                                    Toast.makeText(TambahInformasiActivity.this, "Informasi telah di perbaharui",
                                            Toast.LENGTH_LONG).show();
                                    dbAkun.child(Judul.getText().toString()).setValue(informasi);
                                    finish();
                                }

                            }

                            if (!isUpdate) {//insertdata
                                //ngesafe ke firebase nya
                                dbAkun.get()
                                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot documentReference) {
                                                Toast.makeText(TambahInformasiActivity.this, "Informasi telah di tambahkan",
                                                        Toast.LENGTH_LONG).show();
                                                dbAkun.child(Judul.getText().toString()).setValue(informasi);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TambahInformasiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }

                        }
                    }
                });


            }
        });

    }
}
