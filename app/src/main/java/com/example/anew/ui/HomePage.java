package com.example.anew.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;

//קוד זה מגדיר את דף הבית הראשי של האפליקציה, מטפל בלחיצות כפתורים ובנוסף גם מציג הודעת טוסט כאשר רמת הסוללה נמוכה מ15%
public class HomePage extends AppCompatActivity {

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if (level < 15) {
                Toast.makeText(context, "Battery is low!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();

        Button BTNhomelogin = findViewById(R.id.BTNhomelogin);
        Button BTNhomesignup = findViewById(R.id.BTNhomesignup);
        Button BTNhomeabout = findViewById(R.id.BTNhomeabout);

        BTNhomelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, LoginPage.class));
            }
        });

        BTNhomesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, SignupPage.class));
            }
        });

        BTNhomeabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, AboutPage.class));
            }
        });
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(batteryReceiver);
    }
}