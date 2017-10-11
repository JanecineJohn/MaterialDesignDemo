package com.example.xin.materialdesigndemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class picturesDetail extends AppCompatActivity {

    ImageView picture_big;
    public static final String PICTURES_TEXT = "pictures_text";
    public static final String PICTURES_ID = "pictures_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_detail);

        picture_big = (ImageView) findViewById(R.id.pictures_big);

        Intent intent = getIntent();
        int id = intent.getIntExtra(PICTURES_ID,0);
        picture_big.setImageResource(id);
    }
}
