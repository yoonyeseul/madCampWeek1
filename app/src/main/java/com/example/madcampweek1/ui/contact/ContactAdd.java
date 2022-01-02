package com.example.madcampweek1.ui.contact;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.madcampweek1.MainActivity;
import com.example.madcampweek1.R;

import org.json.*;

import java.io.*;
import java.util.ArrayList;

public class ContactAdd extends AppCompatActivity {
    private Button writeBtn;
    private EditText nameText, numberText;


    private int COUNT = 0;
    private JSONObject obj = null;
    private TextView tv; //저장한 json값 불러오기기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
        nameText = (EditText)findViewById(R.id.edit_name);
        numberText = (EditText)findViewById(R.id.edit_number);


        obj = new JSONObject();

//        /** 외부 저장소에에 저장하기 위 권한 설정 **/
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
//        checkExternalStorage();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_edit_toolbar, menu) ;

        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_complete:
                JSONArray jArray = insertData(nameText.getText().toString(), numberText.getText().toString());

                try {
                    obj.put("contact", jArray);//배열을 넣음
                    obj.put("count", COUNT);
                    writeFile(obj);
                    Toast.makeText(getApplicationContext(),"입력완료",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);


            default :
                return super.onOptionsItemSelected(item);
        }
    }
    public JSONArray insertData(String name, String number){
        COUNT++; //입력한 데이터가 몇개인지 카운트하는 변수, 없어도 무관
        String fileTitle = "contact.json";
        File file = new File(getFilesDir(), fileTitle);
        String json = readFile();
        try {
            JSONArray oldJson = jsonParsing(json);
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            JSONArray jArray = new JSONArray();
//            ContactItem newContact = new ContactItem(name, number);
            //COUNT변수 정수형에서 문자형으로 변환 후 JSONObject에 입력
            COUNT = COUNT+1;
            sObject.put("id", COUNT);
            sObject.put("name", name);
            sObject.put("number", number);


            //JSONObject to JSONArray
            return oldJson.put(sObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(JSONObject object) {

        String fileTitle = "contact.json";
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

    private JSONArray jsonParsing(String json) {
        JSONArray contactList = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray contactArray = jsonObject.getJSONArray("contact");
            COUNT = jsonObject.getInt("count");

            for(int i=0; i<contactArray.length(); i++)
            {
                JSONObject contactObject = contactArray.getJSONObject(i);


                contactList.put(contactObject);
            }
            return contactList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public String readFile() {

        String fileTitle = "contact.json";
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