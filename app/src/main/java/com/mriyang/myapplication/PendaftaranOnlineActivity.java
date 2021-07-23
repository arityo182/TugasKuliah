package com.mriyang.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.model.NomorAntrianModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PendaftaranOnlineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner sp_Nama, sp_Penjamin, sp_Poliklinik;
    private DatePickerDialog datePickerDialog;
    private String getNama, getTanggal, getPenjamin, getPeriksa, getBpjs, getPoli;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private Button btDatePicker, bt_TambahPasien, bt_DaftarOnline;
    private RelativeLayout rg_RL,bpjs_RL;
    private RadioGroup rg_Periksa;
    ValueEventListener listener;
    private RadioButton rb_pilih;
    private EditText et_NmrBpjs;
    RadioButton rb_labo,rb_rawat;

    NomorAntrianModel model;

    String nik = "";
    String nama = "";

    boolean cekSekali = false;
    boolean isExecute = true;
    private InterstitialAd mInterstitialAd;

    String id;
    private int year, month, day;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftaronline);

        //admob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createInterstitialAdd();
            }
        });

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        nama = tools.getSharedPreferenceString(PendaftaranOnlineActivity.this, "namaPasien", "");

        // Data Pasien yang di ambil
        sp_Nama = findViewById(R.id.spin_nama);

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
                        dataNIK.add(document.child("no_ktp").getValue().toString());
                    }

                    if (dataAkun.size() > 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PendaftaranOnlineActivity.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, dataAkun);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_Nama.setAdapter(adapter);
                        sp_Nama.setOnItemSelectedListener(PendaftaranOnlineActivity.this);

                        sp_Nama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                nik = dataNIK.get(position);
                                nama = dataAkun.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
        });


        // Tambahkan Nama


//        dbUser = FirebaseDatabase.getInstance();
//        Nama = dbUser.getReference().child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Anggota");
//        Nama.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull  Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    ArrayList<String> data = new ArrayList<>();
//                    for (DataSnapshot document : task.getResult().getChildren()) {
//                        data.add(document.child("nama").getValue().toString());
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PendaftaranOnlineActivity.this,
//                                android.R.layout.simple_list_item_1, android.R.id.text1, data);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp_Nama.setAdapter(adapter);
//                        sp_Nama.setOnItemSelectedListener(PendaftaranOnlineActivity.this);
//                    }
//                } else {
//                    Log.w(String.valueOf("aaa"), "Error getting documents.", task.getException());
//                }
//            }
//        });

        // Tanggal Kontrol
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tvDateResult = (TextView) findViewById(R.id.tv_tanggal);
        btDatePicker = (Button) findViewById(R.id.bt_tanggal);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });

        // Spinner untuk Penjamin
        sp_Penjamin = (Spinner) findViewById(R.id.spin_penjamin);

        // Array Penjamin
        ArrayList arrayList_parent = new ArrayList<>();
        arrayList_parent.add("UMUM");
        arrayList_parent.add("BPJS PBI");
        arrayList_parent.add("BPJS NON PBI");

        // Adapter Penjamin
        ArrayAdapter arrayAdapter_parent = new ArrayAdapter<>(PendaftaranOnlineActivity.this, android.R.layout.simple_spinner_item,arrayList_parent);
        arrayAdapter_parent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Penjamin.setAdapter(arrayAdapter_parent);

        // Relative layout penjamin
        rg_RL = findViewById(R.id.rgRL);
        bpjs_RL = findViewById(R.id.bpjsRL);
        rg_RL.setVisibility(View.GONE);
        bpjs_RL.setVisibility(View.GONE);

        sp_Penjamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("UMUM")){
                    rg_RL.setVisibility(View.VISIBLE);
                    bpjs_RL.setVisibility(View.GONE);
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("BPJS PBI")){
                    bpjs_RL.setVisibility(View.VISIBLE);
                    rg_RL.setVisibility(View.GONE);
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("BPJS NON PBI")){
                    bpjs_RL.setVisibility(View.VISIBLE);
                    rg_RL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Tambah data pasien
        bt_TambahPasien = findViewById(R.id.bt_tambahdata);
        bt_TambahPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PendaftaranOnlineActivity.this, MenuAnggotaActivity.class);
                startActivity(intent);
            }
        });

        // Jenis Perika
        rb_rawat = findViewById(R.id.rb_rawat);
        rb_labo = findViewById(R.id.rb_laborat);
        rg_Periksa = findViewById(R.id.rg_Periksa);
        rg_Periksa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb_pilih = rg_Periksa.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_rawat:
                        getPeriksa = rb_pilih.getText().toString();
                        break;
                    case R.id.rb_laborat:
                        getPeriksa = rb_pilih.getText().toString();
                        break;

                    default:
                }
            }
        });

        // Nomor Bpjs
        et_NmrBpjs = findViewById(R.id.et_bpjs);

        // Daftar Online
        bt_DaftarOnline = findViewById(R.id.bt_daftarpoli);
        bt_DaftarOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkData();
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(PendaftaranOnlineActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });

        // Spinner Poliklinik
        sp_Poliklinik = findViewById(R.id.spin_poli);
        FirebaseDatabase dbAdmin = FirebaseDatabase.getInstance();
        DatabaseReference dtPoli = dbAdmin.getReference().child("admin").child("poliklinik");
        dtPoli.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> data = new ArrayList<>();
                    for (DataSnapshot document : task.getResult().getChildren()) {
                        data.add(document.child("poli").getValue().toString());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PendaftaranOnlineActivity.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, data);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_Poliklinik.setAdapter(adapter);
                        sp_Poliklinik.setOnItemSelectedListener(PendaftaranOnlineActivity.this);
                    }
                } else {
                    Log.w(String.valueOf("aaa"), "Error getting documents.", task.getException());
                }
            }
        });
        SimpleDateFormat format2= null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 =new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        String date= format2.format(new Date());



        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRefdbRealtime = database1.getReference().child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date);

        myRefdbRealtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isExecute = true;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getChildrenCount() > 0) {
                        Log.d("aaa", "onDataChange: ");

                        if (snapshot.child("Anggota").child("nama").getValue().toString().equals(nama) &&
                                snapshot.child("status").getValue().toString().equals("false")) {
                            isExecute = false;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void checkData() {
        getNama = sp_Nama.getSelectedItem().toString();
        getTanggal = tvDateResult.getText().toString();
        getPenjamin = sp_Penjamin.getSelectedItem().toString();
        getBpjs = et_NmrBpjs.getText().toString();
        getPoli = sp_Poliklinik.getSelectedItem().toString();

        if (getNama.isEmpty()) {
            Toast.makeText(PendaftaranOnlineActivity.this, "Tolong Tambahkan Nama Pasien", Toast.LENGTH_SHORT).show();
        } else if (getTanggal.isEmpty()) {
            Toast.makeText(PendaftaranOnlineActivity.this, "Tolong Masukan Tanggal Control", Toast.LENGTH_SHORT).show();
        } else {
            regisData();
        }
    }

    private void regisData() {

        if (getPenjamin.equals("UMUM")){
            if (getPeriksa.isEmpty()){
                Toast.makeText(PendaftaranOnlineActivity.this,"Isi Jenis Periksa",Toast.LENGTH_SHORT).show();
            } else {
                database = FirebaseDatabase.getInstance();
                DatabaseReference myDef = database.getReference().child("Pengguna").child("Daftar").child(getTanggal);
                myDef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            int nomor = (int) (task.getResult().getChildrenCount());
                            cekSekali = true;
                            if (nomor == 0) {
                                nomor = 1;
                            } else {
                                nomor++;
                            }
                            final int finalNomor = nomor;
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(getTanggal).push();
                            NomorAntrianModel nomorUmum = new NomorAntrianModel(
                                    String.valueOf(finalNomor),
                                    getNama,
                                    getPoli,
                                    getTanggal,
                                    getPenjamin,
                                    getPeriksa,false
                            );
                            final Intent lanjut = new Intent(PendaftaranOnlineActivity.this, NomorAntrianActivity.class);

                            lanjut.putExtra("poli", getPoli);
                            lanjut.putExtra("waktu", getTanggal);
                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                            lanjut.putExtra("nama", getNama);
                            lanjut.putExtra("penjamin", getPenjamin);
                            lanjut.putExtra("jenisPeriksa", getPeriksa);

                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            myref.setValue(nomorUmum);
                            startActivity(lanjut);
                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } if (!cekSekali){
                            int nomor = 0;
                            if (nomor == 0) {
                                nomor = 1;
                            } else {
                                nomor++;
                            }
                            final int finalNomor = nomor;
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(getTanggal).push();
                            NomorAntrianModel nomorUmum = new NomorAntrianModel(
                                    String.valueOf(finalNomor),
                                    getNama,
                                    getPoli,
                                    getTanggal,
                                    getPenjamin,
                                    getPeriksa, false
                            );
                            final Intent lanjut = new Intent(PendaftaranOnlineActivity.this, NomorAntrianActivity.class);

                            lanjut.putExtra("poli", getPoli);
                            lanjut.putExtra("waktu", getTanggal);
                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                            lanjut.putExtra("nama", getNama);
                            lanjut.putExtra("penjamin", getPenjamin);
                            lanjut.putExtra("jenisPeriksa", getPeriksa);

                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            myref.setValue(nomorUmum);
                            startActivity(lanjut);
                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }

        } else if (getPenjamin.equals("BPJS PBI")){
            if (getBpjs.isEmpty()){
                et_NmrBpjs.setError("Masukan Nomor BPJS");
            } else {
                database = FirebaseDatabase.getInstance();
                DatabaseReference myDef = database.getReference().child("Pengguna").child("Daftar").child(getTanggal);
                myDef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            int nomor = (int) (task.getResult().getChildrenCount());
                            cekSekali = true;
                            if (nomor == 0) {
                                nomor = 1;
                            } else {
                                nomor++;
                            }
                            final int finalNomor = nomor;
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(getTanggal).push();
                            NomorAntrianModel nomorUmum = new NomorAntrianModel(
                                    String.valueOf(finalNomor),
                                    getNama,
                                    getPoli,
                                    getTanggal,
                                    getPenjamin,
                                    getBpjs,false
                            );
                            final Intent lanjut = new Intent(PendaftaranOnlineActivity.this, NomorAntrianActivity.class);

                            lanjut.putExtra("poli", getPoli);
                            lanjut.putExtra("waktu", getTanggal);
                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                            lanjut.putExtra("nama", getNama);
                            lanjut.putExtra("penjamin", getPenjamin);
                            lanjut.putExtra("jenisPeriksa", getBpjs);

                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            myref.setValue(nomorUmum);
                            startActivity(lanjut);
                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } if (!cekSekali){
                            int nomor = 0;
                            if (nomor == 0) {
                                nomor = 1;
                            } else {
                                nomor++;
                            }
                            final int finalNomor = nomor;
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(getTanggal).push();
                            NomorAntrianModel nomorUmum = new NomorAntrianModel(
                                    String.valueOf(finalNomor),
                                    getNama,
                                    getPoli,
                                    getTanggal,
                                    getPenjamin,
                                    getBpjs, false
                            );
                            final Intent lanjut = new Intent(PendaftaranOnlineActivity.this, NomorAntrianActivity.class);

                            lanjut.putExtra("poli", getPoli);
                            lanjut.putExtra("waktu", getTanggal);
                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                            lanjut.putExtra("nama", getNama);
                            lanjut.putExtra("penjamin", getPenjamin);
                            lanjut.putExtra("jenisPeriksa", getBpjs);

                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            myref.setValue(nomorUmum);
                            startActivity(lanjut);
                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        } else {
            if (getBpjs.isEmpty()){
                et_NmrBpjs.setError("Masukan Nomor BPJS");
            } else {
                database = FirebaseDatabase.getInstance();
                DatabaseReference myDef = database.getReference().child("Pengguna").child("Daftar").child(getTanggal);
                myDef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            int nomor = (int) (task.getResult().getChildrenCount());
                            cekSekali = true;
                            if (nomor == 0) {
                                nomor = 1;
                            } else {
                                nomor++;
                            }
                            final int finalNomor = nomor;
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(getTanggal).push();
                            NomorAntrianModel nomorUmum = new NomorAntrianModel(
                                    String.valueOf(finalNomor),
                                    getNama,
                                    getPoli,
                                    getTanggal,
                                    getPenjamin,
                                    getBpjs,false
                            );
                            final Intent lanjut = new Intent(PendaftaranOnlineActivity.this, NomorAntrianActivity.class);

                            lanjut.putExtra("poli", getPoli);
                            lanjut.putExtra("waktu", getTanggal);
                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                            lanjut.putExtra("nama", getNama);
                            lanjut.putExtra("penjamin", getPenjamin);
                            lanjut.putExtra("jenisPeriksa", getBpjs);

                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            myref.setValue(nomorUmum);
                            startActivity(lanjut);
                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } if (!cekSekali){
                            int nomor = 0;
                            if (nomor == 0) {
                                nomor = 1;
                            } else {
                                nomor++;
                            }
                            final int finalNomor = nomor;
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference myref = database.getReference().child("Pengguna").child("Daftar").child(getTanggal).push();
                            NomorAntrianModel nomorUmum = new NomorAntrianModel(
                                    String.valueOf(finalNomor),
                                    getNama,
                                    getPoli,
                                    getTanggal,
                                    getPenjamin,
                                    getBpjs, false
                            );
                            final Intent lanjut = new Intent(PendaftaranOnlineActivity.this, NomorAntrianActivity.class);

                            lanjut.putExtra("poli", getPoli);
                            lanjut.putExtra("waktu", getTanggal);
                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                            lanjut.putExtra("nama", getNama);
                            lanjut.putExtra("penjamin", getPenjamin);
                            lanjut.putExtra("jenisPeriksa", getBpjs);

                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            myref.setValue(nomorUmum);
                            startActivity(lanjut);
                            Toast.makeText(PendaftaranOnlineActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }
    }


    boolean hariiniusai = false;
    boolean isjumat = false;
    boolean isminggu = false;
    boolean issabtu = false;


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        hariiniusai = false;
        isjumat = false;
        issabtu = false;
        isminggu = false;

        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 = new SimpleDateFormat("EEEE", Locale.forLanguageTag("in"));
        }
        if (format2.format(new Date()).toLowerCase().equals("jumat")) {
            isjumat = true;
        } else if (format2.format(new Date()).toLowerCase().equals("sabtu")) {
            issabtu = true;
        } else if (format2.format(new Date()).toLowerCase().equals("minggu")) {
            isminggu = true;
        }

        FirebaseDatabase dbAdmin = FirebaseDatabase.getInstance();
        DatabaseReference dtPoli = dbAdmin.getReference().child("admin").child("poliklinik");
        dtPoli.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {
                    for (DataSnapshot document : task.getResult().getChildren()) {
                        String weekday = "";

                        if (isminggu) {
                            hariiniusai = true;
                            Toast.makeText(PendaftaranOnlineActivity.this, "tidak ada pendaftaran dihari minggu", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if (!(document.child("senkam").getValue().toString().isEmpty())
                                || !(document.child("sabtu").getValue().toString().isEmpty())
                                || !(document.child("jumat").getValue().toString().isEmpty())
                        ) {

                            if (isjumat) {
                                weekday = document.child("jumat").getValue().toString();
                            } else if (issabtu) {
                                weekday = document.child("sabtu").getValue().toString();
                            } else {
                                weekday = document.child("senkam").getValue().toString();
                            }

                            if (weekday.contains("-")) {

                                String[] split = weekday.split("-");

                                if (split.length > 1 && !split[0].isEmpty() && !split[1].isEmpty()) {
                                    String strTimeStart = split[0].trim();
                                    String strTimeEnd = split[1].trim();
                                    DateFormat dateFormat = new SimpleDateFormat("HH.mm");
                                    Date dEnd = null;
                                    Date dStart = null;
                                    try {
                                        dStart = dateFormat.parse(strTimeStart);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        dEnd = dateFormat.parse(strTimeEnd);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (dStart == null) {
                                        Toast.makeText(PendaftaranOnlineActivity.this, "Format weekday mulai kurang sesuai", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else if (dEnd == null) {
                                        Toast.makeText(PendaftaranOnlineActivity.this, "Format weekday selesai kurang sesuai", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {

                                        DateFormat format = new SimpleDateFormat("HH.mm");
                                        Date date = new Date();

                                        Date dateCompare = null;
                                        try {
                                            dateCompare = dateFormat.parse(format.format(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (!dateCompare.before(dEnd)) {
                                            hariiniusai = true;
                                            Toast.makeText(PendaftaranOnlineActivity.this, "Pendaftaran hari ini usai", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                } else {
                                    Toast.makeText(PendaftaranOnlineActivity.this, "Format weekday kurang sesuai", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            } else {

                                Toast.makeText(PendaftaranOnlineActivity.this, "Tidak ada weekday pelayanan pada poli ini", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        } else {
                            Toast.makeText(PendaftaranOnlineActivity.this, "Jam Kosong", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                if (!hariiniusai) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DATE, 1);
                    datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DATE, 1);
                    datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }

                datePickerDialog.show();
            }
        });
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            String date = (selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);

            SimpleDateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);
            Date newDate = null;
            try {
                newDate = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //pengaturan output tanggal di layout jadwal
            SimpleDateFormat format2 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                format2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
            }
            String s = format2.format(newDate);
           tvDateResult.setText(s);

        }
    };



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }
    private void createInterstitialAdd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712",adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("--AddMob", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("AddMob", "The ad was dismissed.");
                                checkData();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("AddMob", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("AddMob", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("--AddMob", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }


}
