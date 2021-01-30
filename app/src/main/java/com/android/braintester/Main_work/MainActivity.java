package com.android.braintester.Main_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import com.android.braintester.bookmark_work.Bookmark_Activity;
import com.android.braintester.Category_work.Categories_Activity;
import com.android.braintester.R;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private Button startBtn,bookmark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.start);
        bookmark = findViewById(R.id.bookmark);

        MobileAds.initialize(this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(MainActivity.this, Categories_Activity.class);
                startActivity(categoryIntent);
            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(MainActivity.this, Bookmark_Activity.class);
                startActivity(bookIntent);
            }
        });
    }
}