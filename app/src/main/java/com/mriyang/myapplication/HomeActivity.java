package com.mriyang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mriyang.myapplication.login.LoginActivityBaru;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CardView bt_Logout , bt_Anggota, bt_Editpoli, bt_Informasi, bt_DaftarOnLine, bt_MenuAntre, bt_Riwayat;
    private ImageView mImgProfil;
    private TextView mNameProfil;

    //Deklarasi Variable
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private RewardedAd mRewardedAd;
    private final String TAG ="--->AdMob";

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__home);

        //Addmob
        textView=findViewById(R.id.id_textView);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadRewardedAd();
            }
        });

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        mImgProfil = headerview.findViewById(R.id.nav_header_imageView);
        mNameProfil = headerview.findViewById(R.id.nav_header_textView);
        mNameProfil.setText(tools.getSharedPreferenceString(this, "namauser", "Not Found"));

        //Instance Firebasee Auth
        auth = FirebaseAuth.getInstance();

        //Menambahkan Listener untuk mengecek apakah user telah logout / keluar
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Jika Iya atau Null, maka akan berpindah pada halaman Login
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    Toast.makeText(HomeActivity.this, "Logout Succes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, LoginActivityBaru.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        bt_Anggota = findViewById(R.id.menu_anggota);

        bt_Anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

        bt_Editpoli = findViewById(R.id.menu_dftpoli);

        bt_Editpoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditPoliActivity.class);
                startActivity(intent);
            }
        });

        bt_Informasi = findViewById(R.id.menu_informasi);

        bt_Informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, InformasiActivity.class);
                startActivity(intent);
            }
        });


//        bt_Logout = findViewById(R.id.bt_Logout);
//
//        bt_Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Fungsi untuk logout
//                auth.signOut();
//            }
//        });

        bt_DaftarOnLine = findViewById(R.id.menu_pendaftaran);

        bt_DaftarOnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PendaftaranOnlineActivity.class);
                startActivity(intent);
            }
        });

        bt_MenuAntre = findViewById(R.id.menu_antean);
        bt_MenuAntre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LihatAntreanActivity.class);
                startActivity(intent);
            }
        });

        bt_Riwayat = findViewById(R.id.menu_riwayat);
        bt_Riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,RiwayatDaftarActivity.class);
                startActivity(intent);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_daftar) {
            Intent intent = new Intent(HomeActivity.this, PendaftaranOnlineActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_antre) {
            Intent intent = new Intent(HomeActivity.this, LihatAntreanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_data_pasien) {
            Intent intent = new Intent(HomeActivity.this, MenuAnggotaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_riwayat) {
            Intent intent = new Intent(HomeActivity.this, RiwayatDaftarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            showRewardedAd();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        mNameProfil.setText(tools.getSharedPreferenceString(this, "namauser", "Not Found"));
        super.onResume();
    }
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                        Log.d(TAG, "onAdFailedToLoad");
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad is Loaded");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                loadRewardedAd();
                            }
                        });
                    }
                });
    }

    private void showRewardedAd(){
        if (mRewardedAd != null) {
            Activity activityContext = HomeActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                    int reward = Integer.parseInt(textView.getText().toString().trim());

                    textView.setText(String.valueOf(reward +rewardAmount));
                    auth.signOut();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

}
