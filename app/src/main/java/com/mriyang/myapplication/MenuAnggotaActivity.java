package com.mriyang.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mriyang.model.data_anggota;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class MenuAnggotaActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText Ktp, Nama,Nmrhp,Beratb,Alamat;
    private Button Simpan, ShowData;
    private String getKtp, getNama, getHubungan,getGoldar,getGender,getDate,getNmrHp,getBeratb,getAlamat,getFoto;
    private RadioButton male;
    private RadioGroup maFe;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter_parent;
    ArrayList<String> arrayList_parent;
    ArrayList<String>arrayList_ayah,arrayList_ibu,arrayList_saudara;
    ArrayAdapter<String>arrayAdapter_child;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private Button btDatePicker;
    private Button btnDaftar;
    private String userID;
    private String tanggal;
    private String dokter;
    data_anggota datas;
    private ImageView tvImage;
    private StorageTask uploadTask;

    String item;
    private Uri imageUrl;
    DatabaseReference getReferences,getReferences1;
    data_anggota anggota;
    FirebaseDatabase database;
    CheckBox c1,c2,c3,c4;
    int i = 0;
    FirebaseAuth mAuth;
    private Button profilpic ;
    private EditText tvfoto;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    private InterstitialAd mInterstitialAd;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuanggota);

        // Inisialisasi id (progresbar)

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        // Inisialisasi id Buttom

        Simpan = findViewById(R.id.save);
        ShowData = findViewById(R.id.showdata);

        // Inisialisasi id edittext

        Ktp = findViewById(R.id.ktp);
        Nama = findViewById(R.id.nama);
        Nmrhp = findViewById(R.id.nmrhp);
        Beratb = findViewById(R.id.beratbadan);
        Alamat = findViewById(R.id.alamat);

        // Mendapatkan Instance dari Database
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        getReferences = database.getReference("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()); //Mendapatkan Refensi Dari Database
        storageReference = storage.getReference();


        // Membuat Fungsi Tombol SImpan

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKtp = Ktp.getText().toString();
                getNama = Nama.getText().toString();
                getHubungan = spinner.getSelectedItem().toString();
                getDate = tvDateResult.getText().toString();
                getNmrHp = Nmrhp.getText().toString();
                getBeratb = Beratb.getText().toString();
                getAlamat = Alamat.getText().toString();


                checkUser();


                progressBar.setVisibility(View.VISIBLE);
            }
        });

        // Isisialisasi ImageView


        tvImage = findViewById(R.id.imageview);
//        profilpic = findViewById(R.id.bt_foto);

//        tvfoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choosePicture();
//            }
//        });
        tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

//        profilpic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadPicture();
//                getFoto = profilpic.getText().toString();
//
//            }
//        });

        // Spinner untuk Hubungan dan Status
        spinner = (Spinner) findViewById(R.id.spnr_hubungan);
        // Array Hubungan
        arrayList_parent = new ArrayList<>();
        arrayList_parent.add("Ayah");
        arrayList_parent.add("Ibu");
        arrayList_parent.add("Sudara");
        arrayList_parent.add("Kakek");
        arrayList_parent.add("Nenek");

        // Adapter Hubungan
        arrayAdapter_parent = new ArrayAdapter<>(MenuAnggotaActivity.this, android.R.layout.simple_spinner_item,arrayList_parent);
        arrayAdapter_parent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter_parent);


        // Checklist
        c1 = findViewById(R.id.a);
        c2 = findViewById(R.id.b);
        c3 = findViewById(R.id.ab);
        c4 = findViewById(R.id.o);


        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    c2.setChecked(false);
                    c3.setChecked(false);
                    c4.setChecked(false);
                    getGoldar = c1.getText().toString();
                }

            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    c1.setChecked(false);
                    c3.setChecked(false);
                    c4.setChecked(false);
                    getGoldar = c2.getText().toString();
                }

            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    c2.setChecked(false);
                    c1.setChecked(false);
                    c4.setChecked(false);
                    getGoldar = c3.getText().toString();
                }

            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    c2.setChecked(false);
                    c3.setChecked(false);
                    c1.setChecked(false);
                    getGoldar = c4.getText().toString();

                }
            }
        });

        // Inisialisasi RadioButton
        maFe = (RadioGroup) findViewById(R.id.rg_gender);
        maFe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                male = maFe.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_cowo:
                        getGender = male.getText().toString();
                        break;
                    case R.id.rb_cewe:
                        getGender = male.getText().toString();
                        break;

                    default:
                }
            }
        });



        ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAnggotaActivity.this, ListDataActivity.class);
                startActivity(intent);

            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        btDatePicker = (Button) findViewById(R.id.bt_datepicker);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

    }

    private void checkUser(){

        // Mengecek data Apakah ada yang kosong

        if (TextUtils.isEmpty(getKtp) && TextUtils.isEmpty(getNama) && TextUtils.isEmpty(getHubungan)&& TextUtils.isEmpty(getGoldar)&& TextUtils.isEmpty(getGender)
                && TextUtils.isEmpty(getDate)&& TextUtils.isEmpty(getNmrHp)&& TextUtils.isEmpty(getBeratb)&& TextUtils.isEmpty(getAlamat) && TextUtils.isEmpty(getFoto)){
            // Jika Ada maka akan menampilkan pesan
            Toast.makeText(MenuAnggotaActivity.this,"Data Tidak Boleh Ada Yang Kosong",Toast.LENGTH_SHORT).show();
        } else if (getNmrHp.length() < 10){
            Toast.makeText(MenuAnggotaActivity.this,"Masukan Nomor Hp yang Benar",Toast.LENGTH_SHORT).show();
        }else {
            /* Jika Tidak Maka data dapat di proses dan menyimpanya pada database menyimpan data
            referensi pada database bedasarkan user id dari masing masing akun
             */
            if (imageUrl !=null){
                final ProgressDialog pd = new ProgressDialog(MenuAnggotaActivity.this);
                pd.setTitle("Mengungah Gambar....");
                pd.show();

                final String randomkey = UUID.randomUUID().toString();
                StorageReference riversRef = storageReference.child("images/" +randomkey);
                riversRef.putFile(imageUrl)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        pd.dismiss();

//                            String TempImageName = tvfoto.getText().toString().trim();
                                        Snackbar.make(findViewById(android.R.id.content),"Mengunggah Gambar",Snackbar.LENGTH_LONG).show();
                                        String baru1 = uri.toString();
                                        getFoto = baru1;
                                        getReferences.child("Anggota").push()
                                                .setValue(new data_anggota (getKtp,getNama,getHubungan,getGoldar,getGender,getDate,getNmrHp,getBeratb,getAlamat,getFoto));
                                        Toast.makeText(MenuAnggotaActivity.this,"Data Tersimpan",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                });


//                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
//                            String ImageUploadId = getReferences1.push().getKey();
//                            getReferences1.child(ImageUploadId).setValue(imageUploadInfo);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"Tidak Bisa Mengunggah",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                pd.setMessage("Percent :" + (int) progressPercent + "%");
                            }
                        });

            } else {
                Toast.makeText(MenuAnggotaActivity.this,"Tidak Ada File Yang di Pilih",Toast.LENGTH_SHORT).show();
            }
//            getReferences.child("Admin").child("Anggota").push()
//                    .setValue(new data_anggota (getKtp,getNama,getHubungan,getGoldar,getGender,getDate,getNmrHp,getBeratb,getAlamat,getFoto))
//                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // Peristiwa ini terjadi saat user berhasil menyimpan data ke database
//
//                            Ktp.setText("");
//                            Nama.setText("");
//                            Beratb.setText("");
//                            Nmrhp.setText("");
//                            Alamat.setText("");
//
//                            Toast.makeText(MenuAnggotaActivity.this,"Data Tersimpan",Toast.LENGTH_LONG).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    });
        }

    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tvDateResult.setText(" "+dateFormatter.format(newDate.getTime()));
                tanggal = dateFormatter.format(newDate.getTime());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUrl = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUrl);
                tvImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadPicture() {
        if (imageUrl !=null){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Mengungah Gambar....");
            pd.show();

            final String randomkey = UUID.randomUUID().toString();
            StorageReference riversRef = storageReference.child("images/" +randomkey);
            riversRef.putFile(imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
//                            String TempImageName = tvfoto.getText().toString().trim();
                                    Snackbar.make(findViewById(android.R.id.content),"Mengunggah Gambar",Snackbar.LENGTH_LONG).show();
                                    String baru1 = uri.toString();
                                    getFoto = baru1;
                                }
                            });


//                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
//                            String ImageUploadId = getReferences1.push().getKey();
//                            getReferences1.child(ImageUploadId).setValue(imageUploadInfo);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Tidak Bisa Mengunggah",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Percent :" + (int) progressPercent + "%");
                        }
                    });

        } else {
            Toast.makeText(this,"Tidak Ada File Yang di Pilih",Toast.LENGTH_SHORT).show();
        }
    }

}