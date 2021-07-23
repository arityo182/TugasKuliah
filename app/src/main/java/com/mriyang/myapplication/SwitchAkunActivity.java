package com.mriyang.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SwitchAkunActivity extends AppCompatActivity {

    private Button bt_User,bt_Admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awal_admin_user);

        // Inisiasi button

        bt_Admin = (Button) findViewById(R.id.bt_admin);
        bt_User = findViewById(R.id.bt_user);


        bt_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwitchAkunActivity.this,com.mriyang.myapplication.admin.LoginAdminActivity.class);
                startActivity(intent);
            }
        });

        bt_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwitchAkunActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
