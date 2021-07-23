package com.mriyang.myapplication.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mriyang.model.Pelayanan;
import com.mriyang.myapplication.R;

import java.util.ArrayList;

public class DetailPoliDokterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button mSave;
    private Button mKembali;
    private EditText editIdDokter;
    private EditText editNamaDokter;
    private EditText editSenkam;
    private EditText editJumat;
    private EditText editSabtu;
    boolean isUpdate = false;

    Spinner spinner;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poli);

        // Inisiasi Button
        mSave = findViewById(R.id.btn_savedata);
        mKembali = findViewById(R.id.btnkembali);
        editIdDokter = findViewById(R.id.edt_idDok);
        editNamaDokter = findViewById(R.id.edt_namaDok);
        editSenkam = findViewById(R.id.edt_senkam);
        editJumat = findViewById(R.id.edt_jumat);
        editSabtu = findViewById(R.id.edt_sabtu);
        spinner = findViewById(R.id.spinner1);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        mKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDokter();
            }
        });

        ArrayList<String> data = new ArrayList<>();
        data.add("POLIKLINIK UMUM");
        data.add("POLIKLINIK LAKTASI");
        data.add("POLIKLINIK GIZI");
        data.add("POLIKLINIK LANSIA");
        data.add("POLIKLINIK KIA DAN KB");
        data.add("POLIKLINIK GIGI DAN MULUT");
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(DetailPoliDokterActivity.this, android.R.layout.simple_list_item_1, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        gotReference.child("poliklinik").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull  Task <DataSnapshot> task) {
//                for (DataSnapshot document : task.getResult().getChildren()){
//                    data.add(document.getValue().toString());
//                }
//            }
//        });
    }
    private void registerDokter() {
        final String iddokter = editIdDokter.getText().toString().trim();
        final String namadokter = editNamaDokter.getText().toString().trim();
        final String senkam = editSenkam.getText().toString().trim();
        final String jumat = editJumat.getText().toString().trim();
        final String sabtu = editSabtu.getText().toString().trim();


        if (iddokter.isEmpty()) {
            editIdDokter.setError("Wajib diisi!");
            editIdDokter.requestFocus();
            return;
        }

        if (namadokter.isEmpty()) {
            editNamaDokter.setError("Wajib diisi!");
            editNamaDokter.requestFocus();
            return;
        }

        if (senkam.isEmpty()) {
            editSenkam.setError("Wajib diisi!");
            editSenkam.requestFocus();
            return;
        }

        if (jumat.isEmpty()) {
            editJumat.setError("Wajib diisi!");
            editJumat.requestFocus();
            return;
        }

        if (sabtu.isEmpty()) {
            editSabtu.setError("Wajib diisi");
            editSabtu.requestFocus();
            return;
        }

        final DatabaseReference dbPelayanan = db.getReference().child("admin").child("poliklinik");

        final Pelayanan pelayanan = new Pelayanan(
                iddokter,
                namadokter,
                senkam,
                jumat,
                sabtu,
                spinner.getSelectedItem().toString()
        );

        Log.d("", pelayanan.toString());


        dbPelayanan.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot document : task.getResult().getChildren()) {
                        if (document.equals("poliklinik") && document.child("poliklinik").toString().equals(spinner.getSelectedItem().toString())){
                            isUpdate = true;
                            Toast.makeText(DetailPoliDokterActivity.this, "Informasi telah di perbaharui",
                                    Toast.LENGTH_LONG).show();
                            dbPelayanan.child(spinner.getSelectedItem().toString()).setValue(pelayanan);
                            finish();
                        }
                    }

                    if (!isUpdate) {//databaru ngesafe ke firebase nya
                        dbPelayanan.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot snapshot) {
                                Toast.makeText(DetailPoliDokterActivity.this, "Informasi telah di tambahkan",
                                        Toast.LENGTH_LONG).show();
                                dbPelayanan.child(spinner.getSelectedItem().toString()).setValue(pelayanan);
                                finish();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DetailPoliDokterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
