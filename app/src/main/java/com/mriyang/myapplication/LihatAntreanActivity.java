package com.mriyang.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.model.NomorAntrianModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LihatAntreanActivity extends AppCompatActivity {
    private TextView rngPeriksa;
    private TextView nmrAntrian;
    private TextView noKtp;
    private TextView namaPasien;
    private TextView waktu;
    private TextView timer;
    private boolean run = true;

    String poli;
    String nomor;
    String nik;
    String nmPasien;
    String wkt;
    FirebaseDatabase database;
    String date="";
    NomorAntrianModel model ;
    NomorAntrianModel  modelNow ;
    static boolean active = false;

    TextView tv_nomor_anda,tv_nomor_now, tv_ruangperiksa_anda, tv_waktu_anda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomor_antrian2);
        poli = getIntent().getStringExtra("poli");
        wkt = getIntent().getStringExtra("waktu");


        tv_nomor_anda = findViewById(R.id.tv_nomor_anda);
        tv_nomor_now= findViewById(R.id.tv_nomor_now);
        tv_ruangperiksa_anda= findViewById(R.id.tv_ruangperiksa_anda);
        tv_waktu_anda= findViewById(R.id.tv_waktu_anda);



        database = FirebaseDatabase.getInstance();
        SimpleDateFormat format2= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 =new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        date= format2.format(new Date());

        getNomorAntrianAnda();
        getNomorAntrianLain();


    }

    private CountDownTimer countDownTimer = new CountDownTimer(12000, 1000) {
        //The method will be called back after 1 second, the parameter millisUntilFinished indicates the millisecond value of the remaining time
        public void onTick(long millisUntilFinished) {
            timer = findViewById(R.id.timer);
            timer.setText("AKAN REFRESH OTOMATIS DALAM : "+ millisUntilFinished / 1000);
        }

        //The method executed at the end of the timer
        public void onFinish() {
            timer.setText("Wait");
            Intent i = new Intent(LihatAntreanActivity.this, LihatAntreanActivity.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
        }

    };

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        countDownTimer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        countDownTimer.cancel();
    }

    boolean isfound=false;


    private void getNomorAntrianAnda(){
        isfound=false;

        FirebaseDatabase dbUser = FirebaseDatabase.getInstance();
        DatabaseReference Nama = dbUser.getReference().child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Anggota");
        Nama.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList <String> dataAkun = new ArrayList<>();
                    ArrayList <String>  dataNIK = new ArrayList<>();
                    for (DataSnapshot document : task.getResult().getChildren()) {
                        dataAkun.add(document.child("nama").getValue().toString());
                        tools.setSharedPreference(LihatAntreanActivity.this,"namadata", document.child("nama").getValue().toString());
                    }

                }
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myDef = database.getReference().child("Pengguna").child("Daftar").child(date);


        myDef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot document : dataSnapshot.getChildren()) {
                    Log.d("aaa", "onDataChange: ");
                    Log.d("aaa", "onDataChange: ");
                    if (document.getChildrenCount()>0){
                        if (document.child("status").getValue().toString().equals("false") &&
                                document.child("nama").getValue().toString()
                                        .equals(tools.getSharedPreferenceString(LihatAntreanActivity
                                                .this, "namadata", ""))){
                            isfound=true;
                            model = new NomorAntrianModel(
                                    document.child("nomor").getValue().toString(),
                                    document.child("nama").getValue().toString(),
                                    document.child("poli").getValue().toString(),
                                    document.child("waktu").getValue().toString(),
                                    document.child("penjamin").getValue().toString(),
                                    document.child("jenisperiksa").getValue().toString(),
                                    Boolean.getBoolean(document.child("status").getValue().toString())
                            );
                            tv_nomor_anda  .setText(model.getNomor());
                            tv_ruangperiksa_anda  .setText(model.getPoli());
                            tv_waktu_anda  .setText(model.getWaktu());

                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    private void getNomorAntrianLain(){
        isfound=false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myDef = database.getReference().child("Pengguna").child("Daftar").child(date);
        myDef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                isfound=false;

                int index=0;
                for (DataSnapshot document : dataSnapshot.getChildren()) {
                    index ++;
                    Log.d("aaa", "onDataChange: ");
                    Log.d("aaa", "onDataChange: ");
                    if (document.getChildrenCount()>0){
                        if (document.child("status").getValue().toString().equals("false")) {
                            isfound=true;

                        }else {
                            if (index ==dataSnapshot.getChildrenCount()){
                                isfound =true;
                            }
                        }


                        if (isfound){
                            modelNow = new NomorAntrianModel(
                                    document.child("nomor").getValue().toString(),
                                    document.child("nama").getValue().toString(),
                                    document.child("poli").getValue().toString(),
                                    document.child("waktu").getValue().toString(),
                                    document.child("penjamin").getValue().toString(),
                                    document.child("jenisperiksa").getValue().toString(),
                                    Boolean.getBoolean(document.child("status").getValue().toString())
                            );

                            break;
                        }
                    }
                }

                if (isfound){
                    tv_nomor_now.setText(modelNow.getNomor());
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

}
