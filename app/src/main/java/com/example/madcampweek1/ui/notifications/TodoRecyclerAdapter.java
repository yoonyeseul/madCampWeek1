package com.example.madcampweek1.ui.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


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


public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.Holder>{

    private Context context;
    private ArrayList<TodoItem> list;
    private String date;
    public TodoRecyclerAdapter(Context context, ArrayList<TodoItem> list) {
        super();
        this.context = context;
        this.list = list;
    }
    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public TodoRecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        TodoRecyclerAdapter.Holder holder = new TodoRecyclerAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
//        holder.todoCheck.setText(list.get(position).content);
        holder.todoCheck.setText(list.get(position).content);
        holder.todoCheck.setChecked(list.get(position).check);
        System.out.println("holder content: " + list.get(position).content);
    }

    /*
     * Todo 만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일
     *
     * */
    public void setDate(String date) {
        this.date = date;
    }
    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size(); // RecyclerView의 size return
    }

    // ViewHolder는 하나의 View를 보존하는 역할을 한다
    public class Holder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView number;
        public CheckBox todoCheck;


        public Holder(View view){
            super(view);
            todoCheck = (CheckBox) view.findViewById(R.id.todo_check);

//            view.setClickable(true);

            todoCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    System.out.println("checkbox clicked!: "+ date);
                    if(pos != RecyclerView.NO_POSITION) {
                        String json = readFile();
                        JSONArray jArray = jsonParsing(json, date);
                        JSONArray newArray = new JSONArray();
                        JSONObject sObject = new JSONObject();
                        for(int i = 0; i < jArray.length(); i++) {
                            try {
                                if(i == pos) {
                                    sObject.put("check", todoCheck.isChecked());
                                    sObject.put("content", list.get(pos).content);
                                    newArray.put(sObject);
                                    continue;
                                }
                                newArray.put(jArray.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            JSONObject obj = (json.length() == 0) ? new JSONObject() : new JSONObject(json);
                            obj.put(date, newArray);
                            writeFile(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

        }
    }
    public void writeFile(JSONObject object) {

        String fileTitle = "todo.json";
        File file = new File(context.getFilesDir(), fileTitle);

        try {
            //파일 생성

            BufferedWriter bw = new BufferedWriter(new FileWriter(file,false));
            bw.write(object.toString());
            bw.close();

        } catch (IOException e) {
            Log.i("저장오류",e.getMessage());
        }
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
        File file = new File(context.getFilesDir(), fileTitle);
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

}
