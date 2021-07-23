package com.mriyang.myapplication.admin;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.model.NomorAntrianModel;
import com.mriyang.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PanggilNomorActivity extends AppCompatActivity {
    private TextView mNomorAntrian;
    private LinearLayout mplayBtn;
    private LinearLayout mnextBtn;
    private TextToSpeech textToSpeech;
    RecyclerView mRecyclerView;

    String date = "";
    NomorAntrianModel model;

    @Override
    public void onBackPressed() {
        textToSpeech.stop();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panggil_nomor);

        mNomorAntrian = findViewById(R.id.nomor_antrian);
        mplayBtn = findViewById(R.id.btn_play);
        mnextBtn = findViewById(R.id.btn_next);
        model = new NomorAntrianModel();



        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        date = format2.format(new Date());


        getNomorAntrian(false);


        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int ttsLang = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ttsLang = textToSpeech.setLanguage(Locale.forLanguageTag("in-ID"));
                        }
                        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "The Language is not supported!");
                        } else {
                            Log.i("TTS", "Language Supported.");
                        }
                        Log.i("TTS", "Initialization success.");
                    } else {
                        Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        mplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getNama().isEmpty()) {
                    String data = "panggilan nomor antrian " + mNomorAntrian.getText().toString();
                    Log.i("TTS", "button clicked: " + data);
                    int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_ADD, null);

                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("TTS", "Error in converting Text to Speech!");
                    }
                } else {
                    Toast.makeText(PanggilNomorActivity.this, "Tidak ada antrian hari ini", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!model.getJenisperiksa().isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(date+ "/" +model.getNomor()+ "-" + model.getNama());

                    NomorAntrianModel nomorAntrianModel = new NomorAntrianModel(
                            String.valueOf(model.getNomor()),
                            model.getNama(),
                            model.getPoli(),
                            model.getWaktu(),
                            model.getPenjamin(),
                            model.getJenisperiksa(),true
                    );
                    myref.setValue(nomorAntrianModel, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@NonNull DatabaseError databaseError,
                                                       @NonNull DatabaseReference databaseReference) {
                                    getNomorAntrian(true);
                                }
                            }
                    );
                } else {
                    Toast.makeText(PanggilNomorActivity.this, "Tidak ada antrian hari ini", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    boolean isfound = false;

    private void getNomorAntrian(final boolean useSound) {
        isfound = false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myDef = database.getReference().child("Pengguna").child("Daftar").child(date);

        myDef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()){
                    if (document.hasChild("status")){
                        if (document.child("status").getValue().toString().equals("false")){
                            isfound = true;
                            model = new NomorAntrianModel(
                                    document.child("nomor").getValue().toString(),
                                    document.child("nama").getValue().toString(),
                                    document.child("poli").getValue().toString(),
                                    document.child("waktu").getValue().toString(),
                                    document.child("penjamin").getValue().toString(),
                                    document.child("jenisperiksa").getValue().toString(),
                                    Boolean.getBoolean(document.child("status").getValue().toString())
                            );
                            mNomorAntrian.setText(model.getNomor());
                            if (useSound){
                                mplayBtn.performClick();
                            }
                            break;
                        }
                    }
                }
                if (!isfound) {
                    Toast.makeText(PanggilNomorActivity.this, "Tidak ada antrian lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }
}
