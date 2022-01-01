package com.example.madcampweek1.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madcampweek1.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {
    int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        imageId = getIntent().getIntExtra("imageIdx", 0);

//        Toast.makeText(this, "인덱스: " + getIntent().getIntExtra("imageIdx", 0), Toast.LENGTH_LONG).show();

        String imgpath = getCacheDir() + "/" + "image" + imageId;
        Bitmap bm = BitmapFactory.decodeFile(imgpath);

//        ImageView imageView = new ImageView(this);
//        imageView.setImageBitmap(bm);

        PhotoView imageView = (PhotoView) findViewById(R.id.photo_view);
        imageView.setImageBitmap(bm);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        imageView.setLayoutParams(lp);
//        ((ConstraintLayout) findViewById(R.id.constraintLayout)).addView(imageView);


        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button trashButton = findViewById(R.id.trashButton);
        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteMessage();
            }
        });
    }

    private void showDeleteMessage() {
        new AlertDialog.Builder(this)
                .setTitle("삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DashboardFragment.viewToDelete = imageId;
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Toast.makeText(getActivity(), "삭제 취소", Toast.LENGTH_SHORT);
                    }
                })
                .show();
    }
}