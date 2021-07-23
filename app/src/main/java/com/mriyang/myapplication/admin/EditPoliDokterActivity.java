package com.mriyang.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mriyang.model.Pelayanan;
import com.mriyang.myapplication.R;

import java.util.ArrayList;

public class EditPoliDokterActivity extends AppCompatActivity {

    private CardView mUmum;
    private CardView mLaktasi;
    private CardView mGizi;
    private CardView mLansia;
    private CardView mKB;
    private CardView mGigi;

    private TextView editNamaDokter;
    private TextView editSenkam;
    private TextView editJumat;
    private TextView editSabtu;

    private ImageButton mTambahData;
    private ArrayList<Pelayanan> listAnggota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpoli_dokter);

        // Inisiasi Button
        mUmum = findViewById(R.id.periksa_umum);
        mLaktasi = findViewById(R.id.periksa_laktasi);
        mGizi = findViewById(R.id.periksa_gizi);
        mLansia = findViewById(R.id.perikas_lansia);
        mKB = findViewById(R.id.periksa_kb);
        mGigi = findViewById(R.id.periksa_gigi);
        mTambahData = findViewById(R.id.img_tambahData);

        mTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPoliDokterActivity.this,DetailPoliDokterActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference gotRefence = db.getReference().child("admin").child("poliklinik");



        mUmum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.view_design_dokter, null);

                editNamaDokter = popupView.findViewById(R.id.edit_namaDok);
                editSenkam = popupView.findViewById(R.id.edit_hariPel);
                editJumat = popupView.findViewById(R.id.edit_hariPeljumat);
                editSabtu = popupView.findViewById(R.id.edit_hariPelSabtu);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                gotRefence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DataSnapshot document : task.getResult().getChildren()) {
                                        String stgUserHomeName = document.getKey();
                                        if ("POLIKLINIK UMUM".equals(stgUserHomeName)) {
                                            editNamaDokter.setText(document.child("namadokter").getValue().toString());
                                            editSenkam.setText(document.child("senkam").getValue().toString());
                                            editJumat.setText(document.child("jumat").getValue().toString());
                                            editSabtu.setText(document.child("sabtu").getValue().toString());
                                        }
                                    }
                                }
                            }
                        });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        mLaktasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.view_design_dokter, null);

                editNamaDokter = popupView.findViewById(R.id.edit_namaDok);
                editSenkam = popupView.findViewById(R.id.edit_hariPel);
                editJumat = popupView.findViewById(R.id.edit_hariPeljumat);
                editSabtu = popupView.findViewById(R.id.edit_hariPelSabtu);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                gotRefence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot document : task.getResult().getChildren()) {
                                String stgUserHomeName = document.getKey();
                                if ("POLIKLINIK LAKTASI".equals(stgUserHomeName)) {
                                    editNamaDokter.setText(document.child("namadokter").getValue().toString());
                                    editSenkam.setText(document.child("senkam").getValue().toString());
                                    editJumat.setText(document.child("jumat").getValue().toString());
                                    editSabtu.setText(document.child("sabtu").getValue().toString());
                                }
                            }
                        }
                    }
                });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        mGizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.view_design_dokter, null);

                editNamaDokter = popupView.findViewById(R.id.edit_namaDok);
                editSenkam = popupView.findViewById(R.id.edit_hariPel);
                editJumat = popupView.findViewById(R.id.edit_hariPeljumat);
                editSabtu = popupView.findViewById(R.id.edit_hariPelSabtu);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                gotRefence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot document : task.getResult().getChildren()) {
                                String stgUserHomeName = document.getKey();
                                if ("POLIKLINIK GIZI".equals(stgUserHomeName)) {
                                    editNamaDokter.setText(document.child("namadokter").getValue().toString());
                                    editSenkam.setText(document.child("senkam").getValue().toString());
                                    editJumat.setText(document.child("jumat").getValue().toString());
                                    editSabtu.setText(document.child("sabtu").getValue().toString());
                                }
                            }
                        }
                    }
                });
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        mLansia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.view_design_dokter, null);

                editNamaDokter = popupView.findViewById(R.id.edit_namaDok);
                editSenkam = popupView.findViewById(R.id.edit_hariPel);
                editJumat = popupView.findViewById(R.id.edit_hariPeljumat);
                editSabtu = popupView.findViewById(R.id.edit_hariPelSabtu);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                gotRefence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot document : task.getResult().getChildren()) {
                                String stgUserHomeName = document.getKey();
                                if ("POLIKLINIK LANSIA".equals(stgUserHomeName)) {
                                    editNamaDokter.setText(document.child("namadokter").getValue().toString());
                                    editSenkam.setText(document.child("senkam").getValue().toString());
                                    editJumat.setText(document.child("jumat").getValue().toString());
                                    editSabtu.setText(document.child("sabtu").getValue().toString());
                                }
                            }
                        }
                    }
                });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });


        mKB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.view_design_dokter, null);

                editNamaDokter = popupView.findViewById(R.id.edit_namaDok);
                editSenkam = popupView.findViewById(R.id.edit_hariPel);
                editJumat = popupView.findViewById(R.id.edit_hariPeljumat);
                editSabtu = popupView.findViewById(R.id.edit_hariPelSabtu);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                gotRefence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot document : task.getResult().getChildren()) {
                                String stgUserHomeName = document.getKey();
                                if ("POLIKLINIK KIA DAN KB".equals(stgUserHomeName)) {
                                    editNamaDokter.setText(document.child("namadokter").getValue().toString());
                                    editSenkam.setText(document.child("senkam").getValue().toString());
                                    editJumat.setText(document.child("jumat").getValue().toString());
                                    editSabtu.setText(document.child("sabtu").getValue().toString());
                                }
                            }
                        }
                    }
                });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        mGigi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.view_design_dokter, null);

                editNamaDokter = popupView.findViewById(R.id.edit_namaDok);
                editSenkam = popupView.findViewById(R.id.edit_hariPel);
                editJumat = popupView.findViewById(R.id.edit_hariPeljumat);
                editSabtu = popupView.findViewById(R.id.edit_hariPelSabtu);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                gotRefence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot document : task.getResult().getChildren()) {
                                String stgUserHomeName = document.getKey();
                                if ("POLIKLINIK GIGI DAN MULUT".equals(stgUserHomeName)) {
                                    editNamaDokter.setText(document.child("namadokter").getValue().toString());
                                    editSenkam.setText(document.child("senkam").getValue().toString());
                                    editJumat.setText(document.child("jumat").getValue().toString());
                                    editSabtu.setText(document.child("sabtu").getValue().toString());
                                }
                            }
                        }
                    }
                });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });
    }

}
