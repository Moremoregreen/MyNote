package com.moremoregreen.mynote;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.add);
        //(view -> {}) add lamba support
        fab.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,EditorActivity.class));
        });
    }
}
