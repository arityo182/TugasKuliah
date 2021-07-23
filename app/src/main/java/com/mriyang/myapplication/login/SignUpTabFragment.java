package com.mriyang.myapplication.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mriyang.model.Pengguna;
import com.mriyang.myapplication.HomeActivity;
import com.mriyang.myapplication.LoginActivity;
import com.mriyang.myapplication.R;
import com.mriyang.myapplication.tools;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;

public class SignUpTabFragment extends Fragment {

    private EditText email,nama,pass,pass1;
    private Button daftar;
    float v = 0;
    AwesomeText ImgShowHidePassword,ImgShowHidePassword2;
    private String getEmail, getPassword,getNama,getPassword2;
    boolean pwd_status = true;
    private ProgressBar progressBar;
    private Context context;

    @Override
    public void onAttach(@NonNull  Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        nama = root.findViewById(R.id.nama);
        pass1 = root.findViewById(R.id.pass1);
        daftar = root.findViewById(R.id.CobaSignup);

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // widget show hide password
        ImgShowHidePassword = (AwesomeText)root.findViewById(R.id.ImgShowPassword);
        ImgShowHidePassword2 = (AwesomeText)root.findViewById(R.id.ImgShowPassword2);

        email.setTranslationX(800);
        pass.setTranslationX(800);
        pass1.setTranslationX(800);
        ImgShowHidePassword.setTranslationX(800);
        ImgShowHidePassword2.setTranslationX(800);
        nama.setTranslationX(800);
        daftar.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        nama.setAlpha(v);
        pass1.setAlpha(v);
        ImgShowHidePassword.setAlpha(v);
        ImgShowHidePassword2.setAlpha(v);
        daftar.setAlpha(v);

        nama.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        pass1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ImgShowHidePassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ImgShowHidePassword2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        daftar.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        // lalu kita beri action agar show hide password nya bisa berfungsi
        ImgShowHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    pass.setSelection(pass.length());
                } else {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    pass.setSelection(pass.length());
                }
            }
        });


        // lalu kita beri action agar show hide password nya bisa berfungsi
        ImgShowHidePassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    pass1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    pass1.setSelection(pass1.length());
                } else {
                    pass1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    ImgShowHidePassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    pass1.setSelection(pass1.length());
                }
            }
        });

        //Instance / Membuat Objek Firebase Authentication

        FirebaseAuth auth = FirebaseAuth.getInstance();
        daftar.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNama= nama.getText().toString();
                        getEmail = email.getText().toString();
                        getPassword = pass.getText().toString();
                        getPassword2 = pass1.getText().toString();

                        if(getNama.isEmpty()){
                            nama.setError("Tolong Masukan Nama Anda");
                        }
                        else if (getEmail.isEmpty()) {
                            email.setError("Tolong Masukan Email");
                        }
                        else if (getPassword.isEmpty()) {
                            pass.setError("Tolong Masukan Password");
                        }
                        else if (getPassword.length() < 8) {
                            pass.setError("Tolong Masukan Password 8 digit");
                        }
                        else if (getPassword2.isEmpty()) {
                            pass1.setError("Tolong Masukan Password");
                        }
                        else if (getPassword2.length() < 8) {
                            pass1.setError("Tolong Masukan Password 8 digit");
                        }
                        else if (!(getPassword.equals(getPassword2))) {
                            Toast.makeText(getActivity(), "Password Harus Sama",Toast.LENGTH_SHORT).show();
                        }
                        else if (getNama.isEmpty() && getEmail.isEmpty() && getPassword.isEmpty()&& getPassword2.isEmpty()){
                            Toast.makeText(getActivity(), "Tidak Boleh Ada Yang Kosong",Toast.LENGTH_SHORT).show();
                        }
                        else if (!(getNama.isEmpty() && getEmail.isEmpty() && getPassword.isEmpty() && getPassword.isEmpty())){
                            progressBar.setVisibility(View.VISIBLE);
                            auth.createUserWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Email Telah di Gunakan Silakan Pakai Email Lain", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Pengguna user = new Pengguna (getNama,getEmail,getPassword,getPassword2);
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference dbAkun = db.getReference().child("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        dbAkun.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                if (user.isEmailVerified()){
                                                    context.startActivity(new Intent(v.getContext(), HomeActivity.class));
                                                }
                                                else {
                                                    user.sendEmailVerification();
                                                    progressBar.setVisibility(View.GONE);
                                                    tools.setSharedPreference(v.getContext(),"namauser",getNama);
//                                                    Toast.makeText(getActivity().getApplicationContext(),"Tolong Cek Email Untuk Verifikasi Akun",Toast.LENGTH_LONG).show();
                                                    context.startActivity(new Intent(v.getContext(), LoginActivity.class));
//                                                    finish();
                                                }

                                            }
                                        });

                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getActivity(), "Error Occured!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }


                )
        );

        return root;
    }
    //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
    private void cekDataUser() {
        getEmail = email.getText().toString();
        getPassword = pass.getText().toString();

        //Mengecek apakah email dan sandi kosong atau tidak

        if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
            Toast.makeText(getActivity(), "Email Atau Kata Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            //Mengecek panjang karakter password baru yang akan didaftarkan
            if (getPassword.length() < 6) {
                Toast.makeText(getActivity(), "Sandi Terlalu Pendek, Minimal 6 Karalkter", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                createUserAccount();
            }
        }
    }
    //Method ini digunakan untuk membuat akun baru user
    private void createUserAccount(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Pengguna user = new Pengguna (getNama,getEmail,getPassword,getPassword2);

                FirebaseDatabase.getInstance().getReference("Pengguna").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Mengecek status keberhasilan saat medaftarkan email dan sandi baru
                        if (task.isSuccessful()) { Toast.makeText(getActivity(), "Sign Up Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent (getActivity(), HomeActivity.class);
                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(getActivity(), "Terjadi Kesalahan, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}
