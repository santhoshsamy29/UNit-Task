package com.example.application.unitproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    Button loginScreen,scanScreen,navDrawScreen;
    private static final int PERM_ALL = 1;
    String[] permission = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginScreen = findViewById(R.id.loginScreen);
        scanScreen = findViewById(R.id.scanScreen);
        navDrawScreen = findViewById(R.id.navScreen);

        scanScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(request(permission,PERM_ALL)){
                    scanIntent();
                }
            }
        });

        loginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    loginIntent();
            }
        });

        navDrawScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(request(permission,PERM_ALL)){
                    navDrawIntent();
                }
            }
        });
    }

    private void scanIntent() {
        startActivity(new Intent(HomeActivity.this,ScanActivity.class));
    }

    private void loginIntent(){
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
    }

    private void navDrawIntent(){
        startActivity(new Intent(HomeActivity.this,NavDrawActivity.class));
    }

    public boolean request(String[] permission,int perm_id){
        ArrayList<String> temp_perm = new ArrayList<>();
        for(int i=0 ; i<permission.length ; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                temp_perm.add(permission[i]);
            }
        }
        String[] perm = new String[temp_perm.size()];
        perm = temp_perm.toArray(perm);
        if(perm.length > 0){
            ActivityCompat.requestPermissions(this,perm,perm_id);
            return false;
        }
        else {
            Log.d("SAN","Inside true");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "GRANT ALL PERMISSIONS", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
