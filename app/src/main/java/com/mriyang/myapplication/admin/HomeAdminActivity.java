package com.mriyang.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mriyang.myapplication.HomeActivity;
import com.mriyang.myapplication.LoginActivity;
import com.mriyang.myapplication.R;


public class HomeAdminActivity extends AppCompatActivity {

    private Button bt_DfKlinik, bt_Informasi, bt_Logout, bt_Panggil, bt_DaftarOnLine;
    //Deklarasi Variable
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        // Inisiasi Button

        bt_DfKlinik = findViewById(R.id.menu_daftarklinik);
        bt_Panggil = findViewById(R.id.menu_panggilantri);
        bt_Informasi = findViewById(R.id.menu_informasi);
        bt_Logout = findViewById(R.id.bt_Logoutadmin);


        //Instance Firebasee Auth
        auth = FirebaseAuth.getInstance();

        //Menambahkan Listener untuk mengecek apakah user telah logout / keluar
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Jika Iya atau Null, maka akan berpindah pada halaman Login
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    Toast.makeText(HomeAdminActivity.this, "Logout Succes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeAdminActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        bt_DfKlinik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this,EditPoliDokterActivity.class);
                startActivity(intent);
            }
        });

        bt_Informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this,InformasiActivity.class);
                startActivity(intent);
            }
        });

        bt_Panggil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this,PanggilNomorActivity.class);
                startActivity(intent);
            }
        });
        bt_Logout = findViewById(R.id.bt_Logoutadmin);

        bt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fungsi untuk logout
                auth.signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }

    }
}
