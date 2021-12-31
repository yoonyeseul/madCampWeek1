package com.example.madcampweek1.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madcampweek1.R;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

//        Toast.makeText(this, "인덱스: " + getIntent().getIntExtra("imageIdx", 0), Toast.LENGTH_LONG).show();

        String imgpath = getCacheDir() + "/" + "image" + getIntent().getIntExtra("imageIdx", 0);
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bm);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        ((ConstraintLayout) findViewById(R.id.constraintLayout)).addView(imageView);
    }
}