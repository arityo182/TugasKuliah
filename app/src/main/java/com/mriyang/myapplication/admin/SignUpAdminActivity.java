package com.mriyang.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mriyang.model.Pengguna;
import com.mriyang.myapplication.R;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;

public class SignUpAdminActivity extends AppCompatActivity {

    private EditText et_email, et_password,et_nama,et_password2;
    private Button  bt_signup;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String getEmail, getPassword,getNama,getPassword2,getStatus;
    private CheckBox showPassword, showPassword2;
    private TextView bt_login;
    AwesomeText ImgShowHidePassword,ImgShowHidePassword2;
    boolean pwd_status = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mriyang.myapplication.R.layout.activity_signup_admin);

        et_nama = findViewById(com.mriyang.myapplication.R.id.et_namaadmin);
        et_email = findViewById(com.mriyang.myapplication.R.id.et_usernameadmin);
        et_password = findViewById(com.mriyang.myapplication.R.id.et_Passwordadmin);
        et_password2 = findViewById(com.mriyang.myapplication.R.id.et_Password2admin);
        bt_login = findViewById(com.mriyang.myapplication.R.id.tv_masuk2admin);
        bt_signup = findViewById(com.mriyang.myapplication.R.id.bt_masuk1admin);
        progressBar = findViewById(com.mriyang.myapplication.R.id.progressBar);
        progressBar.setVisibility(View.GONE);



        // widget show hide password
        ImgShowHidePassword = (AwesomeText)findViewById(R.id.ImgShowPassword);

        // lalu kita beri action agar show hide password nya bisa berfungsi
        ImgShowHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    et_password.setSelection(et_password.length());
                } else {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    et_password.setSelection(et_password.length());
                }
            }
        });

        // widget show hide password
        ImgShowHidePassword2 = (AwesomeText)findViewById(R.id.ImgShowPassword2);

        // lalu kita beri action agar show hide password nya bisa berfungsi
        ImgShowHidePassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    et_password2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    et_password2.setSelection(et_password2.length());
                } else {
                    et_password2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    et_password2.setSelection(et_password2.length());
                }
            }
        });




        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpAdminActivity.this,LoginAdminActivity.class);
                startActivity(intent);
            }
        });

        //Instance / Membuat Objek Firebase Authentication

        auth = FirebaseAuth.getInstance();
        bt_signup.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNama= et_nama.getText().toString();
                        getEmail = et_email.getText().toString();
                        getPassword = et_password.getText().toString();
                        getPassword2 = et_password2.getText().toString();

                        if(getNama.isEmpty()){
                            et_nama.setError("Tolong Masukan Nama Anda");
                        }
                        else if (getEmail.isEmpty()) {
                            et_email.setError("Tolong Masukan Email");
                        }
                        else if (getPassword.isEmpty()) {
                            et_password.setError("Tolong Masukan Password");
                        }
                        else if (getPassword.length() < 8) {
                            et_password.setError("Tolong Masukan Password 8 digit");
                        }
                        else if (getPassword2.isEmpty()) {
                            et_password2.setError("Tolong Masukan Password");
                        }
                        else if (getPassword2.length() < 8) {
                            et_password2.setError("Tolong Masukan Password 8 digit");
                        }
                        else if (!(getPassword.equals(getPassword2))) {
                            Toast.makeText(SignUpAdminActivity.this, "Password Harus Sama",Toast.LENGTH_SHORT).show();
                        }
                        else if (getNama.isEmpty() && getEmail.isEmpty() && getPassword.isEmpty()&& getPassword2.isEmpty()){
                            Toast.makeText(SignUpAdminActivity.this, "Tidak Boleh Ada Yang Kosong",Toast.LENGTH_SHORT).show();
                        }
                        else if (!(getNama.isEmpty() && getEmail.isEmpty() && getPassword.isEmpty() && getPassword.isEmpty())){
                            progressBar.setVisibility(View.VISIBLE);
                            auth.createUserWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(SignUpAdminActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(SignUpAdminActivity.this, "Daftar Tidak Berhasil,Silahkan Ulangi Lagi", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Pengguna user = new Pengguna (getNama,getEmail,getPassword,getPassword2);

//                                        FirebaseDatabase baru1 = FirebaseDatabase.getInstance();
//                                        DatabaseReference getReference = baru1.getReference("user");

                                        FirebaseDatabase.getInstance().getReference().child("admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                if (user.isEmailVerified()){
                                                    startActivity(new Intent(SignUpAdminActivity.this, HomeAdminActivity.class));
                                                }
                                                else {
                                                    user.sendEmailVerification();
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(SignUpAdminActivity.this,"Tolong Cek Email Untuk Verifikasi Akun",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(SignUpAdminActivity.this, LoginAdminActivity.class));
                                                    finish();
                                                }

                                            }
                                        });

                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(SignUpAdminActivity.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }


                )
        );
//        bt_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cekDataUser();
//            }
//        });
    }

    //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
//    private void cekDataUser() {
//        getEmail = et_email.getText().toString();
//        getPassword = et_password.getText().toString();
//
//        //Mengecek apakah email dan sandi kosong atau tidak
//
//        if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
//            Toast.makeText(this, "Email Atau Kata Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
//        } else {
//            //Mengecek panjang karakter password baru yang akan didaftarkan
//            if (getPassword.length() < 6) {
//                Toast.makeText(this, "Sandi Terlalu Pendek, Minimal 6 Karalkter", Toast.LENGTH_SHORT).show();
//            } else {
//                progressBar.setVisibility(View.VISIBLE);
//                createUserAccount();
//            }
//        }
//    }
    //Method ini digunakan untuk membuat akun baru user
//    private void createUserAccount(){
//        auth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                Pengguna user = new Pengguna (getNama,getEmail,getPassword,getPassword2);
//
//                FirebaseDatabase.getInstance().getReference("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
//                        setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        //Mengecek status keberhasilan saat medaftarkan email dan sandi baru
//                        if (task.isSuccessful()) { Toast.makeText(SignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent (SignUpActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(SignUpActivity.this, "Terjadi Kesalahan, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });
//            }
//        });
//    }
}


