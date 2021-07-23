package com.mriyang.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mriyang.myapplication.admin.HomeAdminActivity;
import com.mriyang.myapplication.admin.SignUpAdminActivity;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;


public class LoginActivity extends AppCompatActivity {

    //Deklarasi Variable
    private EditText et_email, et_password;
    private Button bt_login;
    private ProgressBar progressBar;
    private TextView tv_lupa, tv_daftarAdmin;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private String getEmail, getPassword;
    private CheckBox showPassword;
    private TextView bt_signup;
    AwesomeText ImgShowHidePassword;
    boolean pwd_status = true;
    private final static String TAG = "Banner";
    private FrameLayout adContainerView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inisialisasi Widget
        et_email = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_Password);
        bt_login = findViewById(R.id.bt_masuk1);
        bt_signup = findViewById(R.id.tv_daftar);
        tv_lupa = findViewById(R.id.lupa);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });



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

        //Admin
        tv_daftarAdmin = findViewById(R.id.tv_daftaradmin);
        tv_daftarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpAdminActivity.class);
                startActivity(intent);
            }
        });


        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        tv_lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Ganti Password");
                passwordResetDialog.setMessage("Tuliskan Email Untuk Menerima Link Ganti Password");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        if (mail.isEmpty()) {
                            resetMail.setError("Masukan Email Akun Kamu");
                            resetMail.requestFocus();
                        } else if (!(mail.isEmpty())){
                            auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LoginActivity.this, "Link Reset Email Telah Di Kirim",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Toast.makeText(LoginActivity.this,"Error !, Tidak Berhasil Mengirim Link" + e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

        //Instance / Membuat Objek Firebase Authentication
        auth = FirebaseAuth.getInstance();
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmail = et_email.getText().toString();
                getPassword = String.valueOf(et_password.getText());


                if(getEmail.isEmpty()) {
                    et_email.setError("Tuliskan Emailmu!");
                    et_email.requestFocus();
                } else if (getPassword.isEmpty()) {
                    et_password.setError("Tuliskan Passwordmu!");
                    et_password.requestFocus();
                } else if (getEmail.isEmpty() && getPassword.isEmpty()) {
                    et_email.requestFocus();
                    et_email.setError("Harus diisi!");
                    et_password.setError("Harus diisi!");
                    Toast.makeText(LoginActivity.this, "Fields are correct!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(getEmail,getPassword);
//                    auth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(!task.isSuccessful()) {
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(LoginActivity.this, "Akun Salah!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                progressBar.setVisibility(View.GONE);
//                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });



        //Mengecek Keberadaan User

        listener = new  FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Mengecek apakah ada user yang sudah login / belum logout

                FirebaseUser user = firebaseAuth.getCurrentUser(); if(user != null){
                    //Jika ada, maka halaman akan langsung berpidah pada MainActivity
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };

//        bt_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Mendapatkan dat yang diinputkan User
//                getEmail = et_email.getText().toString();
//                getPassword = et_password.getText().toString();
//                //Mengecek apakah email dan sandi kosong atau tidak
//
//                if(TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)){
//                    Toast.makeText(LoginActivity.this,"Email atau Sandi Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
//                } else {
//                    loginUserAccount();
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }

    //Menerapkan Listener
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }
    //Melepaskan Litener
    @Override
    protected void onStop(){
        super.onStop();
        if(listener !=null){
            auth.removeAuthStateListener(listener);
        }
    }

    //Method ini digunakan untuk proses autentikasi user menggunakan email dan kata sandi
    private void loginUserAccount(){
        auth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Mengecek status keberhasilan saat login
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Succes",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Tidak Dapat Masuk, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void loginUser(final String userLoginEmail, final String userLoginPassword) {
        auth.signInWithEmailAndPassword(userLoginEmail, userLoginPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String RegisteredUserID = currentUser.getUid();

                            DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference();
                            jLoginDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child("Pengguna").hasChild(RegisteredUserID)) {
                                        tools.setSharedPreference(LoginActivity.this,"namauser",snapshot.child("Pengguna").child(RegisteredUserID).child("nama").getValue().toString());
                                        Toast.makeText(LoginActivity.this, "Success User", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                    else {
                                        if (snapshot.child("admin").hasChild(RegisteredUserID)) {
                                            Toast.makeText(LoginActivity.this, "Success Admin", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, HomeAdminActivity.class));
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }

//    private void loadBanner() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//    }
//
//    private AdSize adSize() {
//
//        float widthPixels = getResources().getDisplayMetrics().widthPixels;
//        float density = getResources().getDisplayMetrics().density;
//
//        int adWidth = (int) (widthPixels / density);
//
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
//    }

}