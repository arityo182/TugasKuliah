package com.mriyang.myapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mriyang.myapplication.R;
import com.mriyang.myapplication.admin.SignUpAdminActivity;

public class LoginActivityBaru extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    float v = 0;
    private AdView mAdView;
    FloatingActionButton fabAdmin;

    private LoginTabFragment loginTabFragment;
    private SignUpTabFragment signUpTabFragment;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_baru);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        mAdView = findViewById(R.id.adView);
        fabAdmin = findViewById(R.id.fab_admin);

        tabLayout.addTab(tabLayout.newTab().setText("Masuk"));
        tabLayout.addTab(tabLayout.newTab().setText("Daftar"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);
        tabLayout.setAlpha(v);
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        mAdView.setTranslationY(300);
        mAdView.setAlpha(v);
        mAdView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        fabAdmin.setTranslationY(300);
        fabAdmin.setAlpha(v);
        fabAdmin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();



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



        fabAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivityBaru.this, SignUpAdminActivity.class);
                startActivity(intent);
            }
        });
        loginTabFragment = new LoginTabFragment();
        signUpTabFragment = new SignUpTabFragment();


    }


}
