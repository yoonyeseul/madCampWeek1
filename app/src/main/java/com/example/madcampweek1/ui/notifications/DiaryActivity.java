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

        diaryText = (EditText) findViewById(R.id.editText1);
        diaryDate = getIntent().getStringExtra("date");
        setDiary();
        setXButton();
        setSaveBtn();
        setDate();
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
        EditText editText = this.findViewById(R.id.editText1);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jArray = insertData(diaryDate, diaryText.getText().toString());
                JSONObject obj = new JSONObject();
                try {
                    obj.put("diaryList", jArray);
                    writeFile(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public JSONArray insertData(String date, String diary){
//        String fileTitle = "diary.json";
//        File file = new File(getFilesDir(), fileTitle);
        String json = readFile();
//        System.out.println("asdl;fkjasd;lfkj"+json);
        boolean exist = false;
        try {
            JSONArray oldJson = jsonParsing(json);
            for(int i=0; i < oldJson.length(); i++) {
                if(date == oldJson.getJSONObject(i).getString("date")) {
                    exist = true;
                    break;
                }
            }
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            JSONArray jArray = new JSONArray();
//            ContactItem newContact = new ContactItem(name, number);
            //COUNT변수 정수형에서 문자형으로 변환 후 JSONObject에 입력
            sObject.put("date", date);
            sObject.put("diary", diary);

            if(exist) {
                for(int i=0; i < oldJson.length(); i++) {
                    if(date == oldJson.getJSONObject(i).getString("date")) {
                        jArray.put(sObject);
                        continue;
                    }
                    jArray.put(oldJson.getJSONObject(i));
                }
                return jArray;
            }
            //JSONObject to JSONArray
            return oldJson.put(sObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(JSONObject object) {

        String fileTitle = "diary.json";
        File file = new File(getFilesDir(), fileTitle);

        try {
            //파일 생성

            BufferedWriter bw = new BufferedWriter(new FileWriter(file,false));
            bw.write(object.toString());
//
//            bw.newLine();
            bw.close();

        } catch (IOException e) {
            Log.i("저장오류",e.getMessage());
        }
    }

    private void setDiary() {
        String json = readFile();
        try {
            JSONArray oldJson = jsonParsing(json);
            for(int i = 0; i < oldJson.length(); i++) {
                if(oldJson.getJSONObject(i).getString("date").equals(diaryDate)) {
                    System.out.println("foundit!");
                    diaryText.setText(oldJson.getJSONObject(i).getString("diary"));
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray jsonParsing(String json) {
        JSONArray diaryList = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray diaryArray = jsonObject.getJSONArray("diaryList");

            for(int i=0; i<diaryArray.length(); i++)
            {
                JSONObject diaryObject = diaryArray.getJSONObject(i);


                diaryList.put(diaryObject);
            }
            return diaryList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return diaryList;
    }

    public String readFile() {

        String fileTitle = "diary.json";
        File file = new File(getFilesDir(), fileTitle);
        String result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();

            return result;

        } catch (FileNotFoundException e1) {
            Log.i("파일못찾음",e1.getMessage());
        } catch (IOException e2) {
            Log.i("읽기오류",e2.getMessage());
        }
        return result;
    }

}