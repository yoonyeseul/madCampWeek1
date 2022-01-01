package com.example.madcampweek1.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class ContactEdit extends AppCompatActivity {
    private Button writeBtn;
    private EditText nameText, numberText, emailText, webText, jobText, snsText, addressText;
    private TextView title;
    private int id;
    private String name, number, email, web, job, sns, address;
    public static Context mContext;
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
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("편집");
        Intent getintent = getIntent();
        name = (String) getintent.getSerializableExtra("contact_name");
        number = (String) getintent.getSerializableExtra("contact_number");
        email = (String) getintent.getSerializableExtra("contact_email");
        web = (String) getintent.getSerializableExtra("contact_web");
        job = (String) getintent.getSerializableExtra("contact_job");
        sns = (String) getintent.getSerializableExtra("contact_sns");
        address = (String) getintent.getSerializableExtra("contact_address");
        id = (int) getintent.getSerializableExtra("contact_id");
        nameText = (EditText)findViewById(R.id.edit_name);
        numberText = (EditText)findViewById(R.id.edit_number);
        emailText = (EditText)findViewById(R.id.edit_email);
        webText = (EditText)findViewById(R.id.edit_web);
        jobText = (EditText)findViewById(R.id.edit_job);
        snsText = (EditText)findViewById(R.id.edit_sns);
        addressText = (EditText)findViewById(R.id.edit_address);

        nameText.setText(name);
        numberText.setText(number);
        emailText.setText(email);
        webText.setText(web);
        jobText.setText(job);
        snsText.setText(sns);
        addressText.setText(address);

        obj = new JSONObject();
        mContext = this;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_edit_toolbar, menu) ;

        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent1 = new Intent(getApplicationContext(), ContactDetail.class);
                intent1.putExtra("contact_name", name);
                intent1.putExtra("contact_number", number);
                intent1.putExtra("contact_email", email);
                intent1.putExtra("contact_web", web);
                intent1.putExtra("contact_job", job);
                intent1.putExtra("contact_sns", sns);
                intent1.putExtra("contact_address", address);
                intent1.putExtra("contact_id", id);
                startActivity(intent1);
                ((Activity) this).overridePendingTransition(R.anim.slide_right, R.anim.slide_right2);
                finish();
                return true;
            case R.id.edit_complete:
                JSONArray jArray = editData(nameText.getText().toString(), numberText.getText().toString(), emailText.getText().toString(),
                        webText.getText().toString(), jobText.getText().toString(), snsText.getText().toString(), addressText.getText().toString(), id);

                try {
                    obj.put("contact", jArray);//배열을 넣음
                    obj.put("count", COUNT);
                    writeFile(obj);
                    Toast.makeText(getApplicationContext(),"수정완료",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), ContactDetail.class);
                intent.putExtra("contact_name", nameText.getText().toString());
                intent.putExtra("contact_number", numberText.getText().toString());
                intent.putExtra("contact_email", emailText.getText().toString());
                intent.putExtra("contact_web", webText.getText().toString());
                intent.putExtra("contact_job", jobText.getText().toString());
                intent.putExtra("contact_sns", snsText.getText().toString());
                intent.putExtra("contact_address", addressText.getText().toString());
                intent.putExtra("contact_id", id);
                startActivity(intent);
                ((Activity) this).overridePendingTransition(R.anim.slide_right, R.anim.slide_right2);
                finish();

            default :
                return super.onOptionsItemSelected(item);
        }
    }
    public JSONArray editData(String name, String number, String email, String web, String job, String sns, String address, int id) {
        String fileTitle = "contact.json";
        File file = new File(getFilesDir(), fileTitle);
        String json = readFile();
        try {
            int pos = 0;
            int newpos = -1;
            int cnt = 0;
            JSONArray oldJson = jsonParsing(json);
            JSONArray t = new JSONArray();
            JSONArray newJson = new JSONArray();
            JSONObject sObject = new JSONObject();
            for(int i = 0; i < oldJson.length(); i++) {
                if(oldJson.getJSONObject(i).getInt("id") == id) {
                    pos = i;
                    sObject.put("name", name);
                    sObject.put("number", number);
                    sObject.put("email", email);
                    sObject.put("web", web);
                    sObject.put("job", job);
                    sObject.put("sns", sns);
                    sObject.put("address", address);
                    sObject.put("id", id);
                    continue;
                }
                t.put(oldJson.getJSONObject(i));
            }

            for(int i = 0; i < t.length(); i++) {
                if(t.getJSONObject(i).getString("name").compareTo(sObject.getString("name")) >= 0) {
                    newpos = i;
                    newJson.put(i, sObject);
                    break;
                }
                newJson.put(i, t.getJSONObject(i));
            }

            if (newpos == -1) {
                newpos = t.length() - 1;
                newJson.put(newpos + 1, sObject);
            }
            else {
                for (int j = newpos + 1; j <= t.length(); j++) {
                    newJson.put(j, t.getJSONObject(j - 1));
                }
            }
//            ContactItem newContact = new ContactItem(name, number);
            //COUNT변수 정수형에서 문자형으로 변환 후 JSONObject에 입력

            //JSONObject to JSONArray
            return newJson;

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
}
