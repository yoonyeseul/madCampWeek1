package com.example.madcampweek1.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {
    private Button xButton;
    private Button addButton;
    private TextView dateTextView;
    private String date;
    private EditText contentText;
    private JSONObject obj = null;
    private View root = null;
    private JSONArray editingTodoList = new JSONArray();
    private RecyclerView mRecyclerView;
    private TodoEditRecyclerAdapter mRecyclerAdapter;
    private ArrayList<TodoItem> todoItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        date = getIntent().getStringExtra("date");
        String json = readFile();
        editingTodoList = jsonParsing(json, date);
        System.out.println("editing List: "+editingTodoList);
//        editText.addTextChangedListener(this);
//        String json = getJsonString(getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.todoEditRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new TodoEditRecyclerAdapter(getApplicationContext(), editingTodoList);
        System.out.println("onCreateView editing List: "+this.editingTodoList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setDate(date);
        getSupportActionBar().hide();



        setXButton();
        setAddButton();
        setDate();
        setSaveBtn();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        System.out.println("Todo Fragment : onCreateView() 호출됨");
        return root;
    }
    private void setAddButton() {
        xButton = this.findViewById(R.id.addButtonInTodoActivity);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject newObject = new JSONObject();
                try {
                    newObject.put("check", false);
                    newObject.put("content", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editingTodoList = mRecyclerAdapter.getEditingList();
                editingTodoList.put(newObject);
                mRecyclerAdapter = new TodoEditRecyclerAdapter(getApplicationContext(), editingTodoList);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(mRecyclerAdapter);
            }
        });
    }
    private void setXButton() {
        xButton = this.findViewById(R.id.xButtonInTodoActivity);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDate() {
        dateTextView = this.findViewById(R.id.todoDateTextView);
        dateTextView.setText(date);
    }


    private void setSaveBtn() {
        Button saveBtn = this.findViewById(R.id.saveButtonInTodoActivity);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String json = readFile();
                    editingTodoList = mRecyclerAdapter.getEditingList();
                    obj = json.length() == 0 ? new JSONObject() : new JSONObject(json);
                    obj.put(date, editingTodoList);
                    writeFile(obj);
                } catch (Exception e) {
                }
                NotificationsFragment.textToChange = true;
                finish();
            }
        });
    }
    public JSONArray insertData(Boolean check, String content){
        String json = readFile();
        try {
            JSONArray oldJson = jsonParsing(json, date);

            JSONArray newJson = new JSONArray();
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
//           ContactItem newContact = new ContactItem(name, number);
            //COUNT변수 정수형에서 문자형으로 변환 후 JSONObject에 입력
            sObject.put("check", check);
            sObject.put("content", content);

            oldJson.put(sObject);

            //JSONObject to JSONArray
            return oldJson;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void writeFile(JSONObject object) {

        String fileTitle = "todo.json";
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
    public JSONArray deleteData(int id, String date) {
        String json = readFile();
        int i;
        try {
            JSONArray oldJson = jsonParsing(json, date);
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
    private JSONArray jsonParsing(String json, String date) {
        JSONArray todoList = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray todoArray = jsonObject.getJSONArray(date);
            for(int i=0; i<todoArray.length(); i++)
            {
                JSONObject todoObject = todoArray.getJSONObject(i);


                todoList.put(todoObject);
            }
            return todoList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return todoList;
    }

    public String readFile() {

        String fileTitle = "todo.json";
        File file = new File(getFilesDir(), fileTitle);
        String result = "";
        try {
            if(!file.exists()) {
                file.createNewFile();
                System.out.println("file created in recyclerAdapter");
            }
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
    private String getJsonString() {

        String fileTitle = "todo.json";
        File file = new File(getFilesDir(), fileTitle);

        String result = "";
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("file created");
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
            System.out.println("file read complete");
            return result;

        } catch (FileNotFoundException e1) {
            Log.i("파일못찾음",e1.getMessage());
            System.out.println("file not found");
        } catch (IOException e2) {
            Log.i("읽기오류",e2.getMessage());
            System.out.println("file not read");
        }
        return result;
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