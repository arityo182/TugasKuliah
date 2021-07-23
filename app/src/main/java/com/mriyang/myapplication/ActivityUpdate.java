package com.mriyang.myapplication;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mriyang.model.data_anggota;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class ActivityUpdate extends AppCompatActivity {

    // Deklarasi Variabel

    private EditText ktpBaru, namaBaru,nmrhpBaru,beratbBaru,alamatBaru;
    private Spinner spinHubunganBaru;
    private Button update;
    private DatabaseReference database;
    private String cekKtp, cekNama, cekHubungan,cekGoldar,cekGender,cekDate,ceknmrHp,cekBerat,cekAlamat,cekFoto;
    ArrayAdapter<String> arrayAdapter_parent;
    ArrayList<String> arrayList_parent;
    private CheckBox c1,c2,c3,c4;
    private RadioGroup maFe;
    private RadioButton maLe,male,female;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private Button btDatePicker;
    private Button btnDaftar;
    private String userID;
    private String tanggal;
    private String dokter;
    FirebaseAuth mAuth;
    private Button profilpic;
    private EditText tvfoto;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUrl;
    private ImageView tvImage;
    private RadioButton rb_cewe,rb_cowo;
    DatabaseReference getReferences,getReferences1;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);



        ktpBaru = findViewById(R.id.ktp);
        namaBaru = findViewById(R.id.nama);
        spinHubunganBaru = (Spinner) findViewById(R.id.spnr_hubungan);
        nmrhpBaru = findViewById(R.id.nmrhp);
        beratbBaru = findViewById(R.id.beratbadan);
        alamatBaru = findViewById(R.id.alamat);
        update = findViewById(R.id.update);
        rb_cewe = findViewById(R.id.rb_cewe);
        rb_cowo = findViewById(R.id.rb_cowo);

        database = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        getReferences1 = FirebaseDatabase.getInstance().getReference().child("Admin").child("Anggota");

        getReferences = database.getReference(); //Mendapatkan Refensi Dari Database
        storageReference = storage.getReference();

        arrayList_parent = new ArrayList<>();
        arrayList_parent.add("Ayah");
        arrayList_parent.add("Ibu");
        arrayList_parent.add("Sudara");
        arrayList_parent.add("Kakek");
        arrayList_parent.add("Nenek");

        // Adapter Hubungan
        arrayAdapter_parent = new ArrayAdapter<>(ActivityUpdate.this, android.R.layout.simple_spinner_item,arrayList_parent);
        arrayAdapter_parent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHubunganBaru.setAdapter(arrayAdapter_parent);


        // Checklist
        c1 = (CheckBox) findViewById(R.id.a);
        c2 = (CheckBox) findViewById(R.id.b);
        c3 = (CheckBox) findViewById(R.id.ab);
        c4 = (CheckBox) findViewById(R.id.o);

        data_anggota anggota1 = new data_anggota();

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    c2.setChecked(false);
                    c3.setChecked(false);
                    c4.setChecked(false);
//                    cekGoldar = c1.getText().toString();
                    anggota1.setGoldar(c1.getText().toString());
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
//                    cekGoldar = c2.getText().toString();
                    anggota1.setGoldar(c2.getText().toString());
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
//                    cekGoldar = c3.getText().toString();
                    anggota1.setGoldar(c3.getText().toString());
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
//                    cekGoldar = c4.getText().toString();
                    anggota1.setGoldar(c4.getText().toString());

                }
            }
        });

        //Inisialisasi Radio Group dan Button
        maFe = (RadioGroup) findViewById(R.id.rg_gender);
        maFe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                maLe = maFe.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_cowo:
                        cekGender = maLe.getText().toString();
                        break;
                    case R.id.rb_cewe:
                        cekGender = maLe.getText().toString();
                        break;

                    default:
                }
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

        // foto


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
//
//            }
//        });

        getData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mendapatkan Data Mahasiswa yang akan di cek
                cekKtp = ktpBaru.getText().toString();
                cekNama = namaBaru.getText().toString();
                cekHubungan = spinHubunganBaru.getSelectedItem().toString();
                cekGoldar = c1.getText().toString();
                cekGoldar = c2.getText().toString();
                cekGoldar = c3.getText().toString();
                cekGoldar = c4.getText().toString();
                cekDate = tvDateResult.getText().toString();
                ceknmrHp = nmrhpBaru.getText().toString();
                cekBerat = beratbBaru.getText().toString();
                cekAlamat = alamatBaru.getText().toString();


                // Mengecek agar Tidak ada data yang Kosong saat proses Update

                if(TextUtils.isEmpty(cekKtp) || TextUtils.isEmpty(cekNama) || TextUtils.isEmpty(cekHubungan) || TextUtils.isEmpty(cekGoldar)
                        || TextUtils.isEmpty(cekGoldar) || TextUtils.isEmpty(cekGender) || TextUtils.isEmpty(cekDate)
                        || TextUtils.isEmpty(ceknmrHp) || TextUtils.isEmpty(cekBerat) || TextUtils.isEmpty(cekAlamat)){
                    Toast.makeText(ActivityUpdate.this,"Data Tidak Boleh Ada Yang Kosong",Toast.LENGTH_SHORT).show();
                } else {
                    /* Menjalankan Proses Update data
                    method setter digunakan untuk mendapatkan data baru yang baru du input user
                     */
                    if (imageUrl !=null){
                        final ProgressDialog pd = new ProgressDialog(ActivityUpdate.this);
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
//                                    String TempImageName = tvfoto.getText().toString().trim();
                                                Snackbar.make(findViewById(android.R.id.content),"Mengunggah Gambar",Snackbar.LENGTH_LONG).show();
                                                String baru1 = uri.toString();
//                                    String imageid = getReferences1.push().getKey();

                                                data_anggota  setAnggota = new data_anggota();
                                                setAnggota.setNo_ktp(ktpBaru.getText().toString());
                                                setAnggota.setNama(namaBaru.getText().toString());
                                                setAnggota.setHubungan(spinHubunganBaru.getSelectedItem().toString());
                                                if (c1.isChecked()){
                                                    setAnggota.setGoldar(c1.getText().toString());

                                                } else if (c2.isChecked()){
                                                    setAnggota.setGoldar(c2.getText().toString());

                                                } else if (c3.isChecked()){
                                                    setAnggota.setGoldar(c3.getText().toString());

                                                } else if (c4.isChecked()){
                                                    setAnggota.setGoldar(c4.getText().toString());
                                                }
                                                setAnggota.setGender(maLe.getText().toString());
                                                setAnggota.setDate(tvDateResult.getText().toString());
                                                setAnggota.setNmrhp(nmrhpBaru.getText().toString());
                                                setAnggota.setBrtbadan(beratbBaru.getText().toString());
                                                setAnggota.setAlamat(alamatBaru.getText().toString());
                                                setAnggota.setImageURL(baru1);
                                                updateAnggota(setAnggota);
//                                    getReferences1.child(imageid).setValue(setAnggota);
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
                        Toast.makeText(ActivityUpdate.this,"Tidak Ada File Foto Yang di Pilih",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void getData(){
        //Menampilkan data dari item yang di pilih sebelumnya

        final String getKtp = getIntent().getExtras().getString("dataKtp");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getHubungan = getIntent().getExtras().getString("dataHubungan");
        final String getGoldar = getIntent().getExtras().getString("dataGoldar");
        final String getGender = getIntent().getExtras().getString("dataGender");
        final String getDate = getIntent().getExtras().getString("dataDate");
        final String getNmrHp = getIntent().getExtras().getString("dataNmrhp");
        final String getBeratb = getIntent().getExtras().getString("dataBeratb");
        final String getAlamat = getIntent().getExtras().getString("dataAlamat");
        final String getFoto = getIntent().getExtras().getString("dataFoto");
//
//        getReferences1 = FirebaseDatabase.getInstance().getReference("imageURL").child("Admin").child("Anggota");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference("images/");
//
        //FOto
        Glide.with(this).load(getFoto).into(tvImage);

        ktpBaru.setText(getKtp);
        namaBaru.setText(getNama);
        //Hubungan
        spinHubunganBaru.setPrompt(getHubungan);
        //Jenis Kelamin
        if(getGender.equals("Perempuan")) {
            rb_cewe.setChecked(true);
        } else {
            rb_cowo.setChecked(true);
        }
        // Goldar
        if(getGoldar.equals("A")) {
            c1.setChecked(true);
        } else if (getGoldar.equals("B")) {
            c2.setChecked(true);
        } else if (getGoldar.equals("AB")) {
            c3.setChecked(true);
        } else if (getGoldar.equals("O")) {
            c4.setChecked(true);
        }

        tvDateResult.setText(getDate);
        nmrhpBaru.setText(getNmrHp);
        beratbBaru.setText(getBeratb);
        alamatBaru.setText(getAlamat);

    }

    private void updateAnggota(data_anggota anggota){
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Anggota").child(getKey)
                .setValue(anggota).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ktpBaru.setText("");
                namaBaru.setText("");
                spinHubunganBaru.setPrompt("");
                c1.setText("");
                c2.setText("");
                c3.setText("");
                c4.setText("");
                tvDateResult.setText("");
                nmrhpBaru.setText("");
                beratbBaru.setText("");
                alamatBaru.setText("");
                Toast.makeText(ActivityUpdate.this,"Data Berhasil Di Ubah",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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

//    private void uploadPicture() {
//        if (imageUrl !=null){
//            final ProgressDialog pd = new ProgressDialog(this);
//            pd.setTitle("Mengungah Gambar....");
//            pd.show();
//
//            final String randomkey = UUID.randomUUID().toString();
//            StorageReference riversRef = storageReference.child("images/" +randomkey);
//            riversRef.putFile(imageUrl)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    pd.dismiss();
////                                    String TempImageName = tvfoto.getText().toString().trim();
//                                    Snackbar.make(findViewById(android.R.id.content),"Mengunggah Gambar",Snackbar.LENGTH_LONG).show();
//                                    String baru1 = uri.toString();
////                                    String imageid = getReferences1.push().getKey();
//
//                                    data_anggota  setAnggota = new data_anggota();
//                                    setAnggota.setImageURL(baru1);
//                                    updateAnggota(setAnggota);
////                                    getReferences1.child(imageid).setValue(setAnggota);
//                                }
//                            });
//
//
////                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
////                            String ImageUploadId = getReferences1.push().getKey();
////                            getReferences1.child(ImageUploadId).setValue(imageUploadInfo);
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            pd.dismiss();
//                            Toast.makeText(getApplicationContext(),"Tidak Bisa Mengunggah",Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                            pd.setMessage("Percent :" + (int) progressPercent + "%");
//                        }
//                    });
//
//        } else {
//            Toast.makeText(this,"Tidak Ada File Yang di Pilih",Toast.LENGTH_SHORT).show();
//        }
//    }


}
