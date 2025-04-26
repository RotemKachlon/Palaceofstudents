package com.example.anew.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;

//קוד זה הוא הקובץ ג'בה המתקשר לדף האודות של האפליקציה, אין בו פעולות מיוחדות או לחיצות כפתור
public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        getSupportActionBar().hide();
    }
}