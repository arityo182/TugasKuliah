package com.mriyang.myapplication.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.mriyang.myapplication.HomeActivity;
import com.mriyang.myapplication.R;
import com.mriyang.myapplication.admin.HomeAdminActivity;
import com.mriyang.myapplication.tools;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;

public class LoginTabFragment extends Fragment {

    private Context context;
    private EditText email, pass;
    private Button login;
    private TextView lupa;
    boolean pwd_status = true;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private String getEmail, getPassword;

    float v = 0;
    AwesomeText ImgShowHidePassword;



    @Override
    public void onAttach(@NonNull  Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        lupa = root.findViewById(R.id.lupa);
        login = root.findViewById(R.id.CobaLogin);
        // widget show hide password
        ImgShowHidePassword = (AwesomeText)root.findViewById(R.id.ImgShowPassword);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        email.setTranslationX(800);
        pass.setTranslationX(800);
        ImgShowHidePassword.setTranslationX(800);
        lupa.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        ImgShowHidePassword.setAlpha(v);
        lupa.setAlpha(v);
        lupa.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ImgShowHidePassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        lupa.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();



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

        lupa.setOnClickListener(new View.OnClickListener() {
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
                                    Toast.makeText(getActivity(), "Link Reset Email Telah Di Kirim",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Toast.makeText(getActivity(),"Error !, Tidak Berhasil Mengirim Link" + e.getMessage(),Toast.LENGTH_LONG).show();
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmail = email.getText().toString();
                getPassword = String.valueOf(pass.getText());


                if(getEmail.isEmpty()) {
                    email.setError("Tuliskan Emailmu!");
                    email.requestFocus();
                } else if (getPassword.isEmpty()) {
                    pass.setError("Tuliskan Passwordmu!");
                    pass.requestFocus();
                } else if (getEmail.isEmpty() && getPassword.isEmpty()) {
                    email.requestFocus();
                    email.setError("Harus diisi!");
                    pass.setError("Harus diisi!");
                    Toast.makeText(getActivity(), "Fields are correct!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(v,getEmail,getPassword);
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
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
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
        return root;
    }
    //Menerapkan Listener
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }
    //Melepaskan Litener
    @Override
    public void onStop(){
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
                    Toast.makeText(getActivity(),"Login Succes",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Tidak Dapat Masuk, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void loginUser(View v, final String userLoginEmail, final String userLoginPassword) {
        auth.signInWithEmailAndPassword(userLoginEmail, userLoginPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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
                                        tools.setSharedPreference(v.getContext(),"namauser", snapshot.child("Pengguna").child(RegisteredUserID).child("nama").getValue().toString());
//                                        Toast.makeText(getActivity(), "Success User", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(v.getContext(), HomeActivity.class));
//                                        getActivity().finish();
                                    }
                                    else {
                                        if (snapshot.child("admin").hasChild(RegisteredUserID)) {
//                                            Toast.makeText(getActivity(), "Success Admin", Toast.LENGTH_SHORT).show();
                                            context.startActivity(new Intent(v.getContext(), HomeAdminActivity.class));
//                                            getActivity().finish();
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

    private void SavePreferences(String key, String value) {
        String MY_PREFS_NAME = "TestName";
        SharedPreferences.Editor pref = getContext().getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();
//        SharedPreferences.Editor edt = pref.edit();
        pref.putString(key, value);
        pref.apply();
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
