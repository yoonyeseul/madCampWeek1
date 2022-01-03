package com.example.madcampweek1.ui.notifications;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class TodoEditRecyclerAdapter extends RecyclerView.Adapter<TodoEditRecyclerAdapter.Holder>{
    private Context context;
    private JSONArray list;
    private String date;
    public JSONArray getEditingList() {
        return list;
    }
    public TodoEditRecyclerAdapter(Context context, JSONArray list) {
        super();
        this.context = context;
        this.list = list;
    }
    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public TodoEditRecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_edit_item, parent, false);
        TodoEditRecyclerAdapter.Holder holder = new TodoEditRecyclerAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TodoEditRecyclerAdapter.Holder holder, int position) {
        try {
            holder.editText.setText(list.getJSONObject(position).getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        holder.todoCheck.setText(list.get(position).content);
//        holder.todoCheck.setText(list.get(position).content);
//        holder.todoCheck.setChecked(list.get(position).check);
//        System.out.println("holder content: " + list.get(position).content);
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
        return list.length(); // RecyclerView의 size return
    }

    // ViewHolder는 하나의 View를 보존하는 역할을 한다
    public class Holder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView number;
        public CheckBox todoCheck;
        public EditText editText;
        private Button deleteButton;

        public Holder(View view){
            super(view);
            deleteButton = (Button) view.findViewById(R.id.todo_delete);

//            view.setClickable(true);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    System.out.println("delete clicked!");
                    if(pos != RecyclerView.NO_POSITION) {
                        String json = readFile();
                        list.remove(pos);
                        notifyDataSetChanged();

                    }
                }
            });

            editText = (EditText) view.findViewById(R.id.edit_todo);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int pos = getAdapterPosition();
                    try {
                        list.getJSONObject(pos).put("content", editText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
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
