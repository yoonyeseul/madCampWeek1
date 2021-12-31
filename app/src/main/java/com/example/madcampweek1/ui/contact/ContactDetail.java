package com.example.madcampweek1.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek1.MainActivity;
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

public class ContactDetail extends AppCompatActivity {
    TextView nameView, numberView, numberHeader;
    int id;
    int COUNT;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.detail_contact));
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("연락처 세부정보");
        ab.setDisplayHomeAsUpEnabled(true);
////        TextView textView = findViewById(R.id.contact_name);
//        getSupportActionBar().setTitle("연락처 부정보");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String name = (String) intent.getSerializableExtra("contact_name");
        String number = (String) intent.getSerializableExtra("contact_number");

        id = (int) intent.getSerializableExtra("contact_id");

//        ContactItem contact = new ContactItem(name, number);
        nameView = (TextView) findViewById(R.id.contact_name);
        numberView = (TextView) findViewById(R.id.contact_number);
        numberHeader = (TextView) findViewById(R.id.number_header);
        nameView.setText(name);
        numberView.setText(number);
        numberHeader.setText("휴대전화");
        ContactItem contact = (ContactItem) intent.getSerializableExtra("contact");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_toolbar_action, menu) ;

        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_edit :
                Intent intent = new Intent(getApplicationContext(), ContactEdit.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent getintent = getIntent();
                String name = (String) getintent.getSerializableExtra("contact_name");
                String number = (String) getintent.getSerializableExtra("contact_number");
                intent.putExtra("contact_name", name);
                intent.putExtra("contact_number", number);
                intent.putExtra("contact_id", id);
                getApplicationContext().startActivity(intent);
                return true ;
            case R.id.contact_delete :

                ContactEdit c = ((ContactEdit) ContactEdit.mContext);
                System.out.println(id);

//                COUNT = ((ContactEdit) ContactEdit.mContext).getCOUNT();
                JSONArray jArray = deleteData(id);
                System.out.println("삭제 눌림");
                System.out.println(jArray);
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contact", jArray);
                    obj.put("count", COUNT);
                    writeFile(obj);
                    Intent delIntent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(delIntent);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            default :
                return super.onOptionsItemSelected(item);
        }
    }
    public void writeFile(JSONObject object) {

        String fileTitle = "contact.json";
        File file = new File(getFilesDir(), fileTitle);

        try {
            //파일 생성

            BufferedWriter bw = new BufferedWriter(new FileWriter(file,false));
            bw.write(object.toString());
            bw.close();

        } catch (IOException e) {
            Log.i("저장오류",e.getMessage());
        }
    }
    public JSONArray deleteData(int id) {
        String json = readFile();
        int i;
        try {
            JSONArray oldJson = jsonParsing(json);
            JSONArray newJson = new JSONArray();
            for(i = 0; i < oldJson.length(); i++) {
                if(oldJson.getJSONObject(i).getInt("id") == id) {
                    continue;
                }
                newJson.put(oldJson.getJSONObject(i));
            }
            return newJson;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_right2);
    }
}
