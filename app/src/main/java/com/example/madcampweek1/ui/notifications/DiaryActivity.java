package com.example.madcampweek1.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.madcampweek1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {
    private String diaryDate;
    private EditText diaryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        getSupportActionBar().hide();

        diaryText = findViewById(R.id.editText1);
        diaryDate = getIntent().getStringExtra("date");
        setXButton();
        setDate();
        setSaveBtn();
        setDiary();
    }

    private void setXButton() {
        Button xBtn = this.findViewById(R.id.xButton);
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDate() {
        TextView textView = this.findViewById(R.id.diaryDateTextView);
        textView.setText(diaryDate);
    }

    private void setSaveBtn() {
        Button saveBtn = this.findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String tmp = readFile();
                    JSONObject obj = tmp.length() == 0 ? new JSONObject() : new JSONObject(tmp);
                    obj.put(diaryDate, diaryText.getText().toString());
                    writeFile(obj);
                } catch (Exception e) {
                }
                NotificationsFragment.textToChange = true;
                finish();
            }
        });
    }

    public String readFile() {
        File file = new File(getFilesDir(), "diary.json");
        String result = "";
        try {
            if (!file.exists())
                file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
            return result;
        } catch (Exception e) {
        }
        return result;
    }

    public void writeFile(JSONObject object) {
        File file = new File(getFilesDir(), "diary.json");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            bw.write(object.toString());
            bw.close();
        } catch (IOException e) {
            Log.i("저장오류", e.getMessage());
        }
    }

    private void setDiary() {
        try {
            JSONObject obj = new JSONObject(readFile());
            diaryText.setText(obj.getString(diaryDate));
        } catch (JSONException e) {
            diaryText.setText("");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}