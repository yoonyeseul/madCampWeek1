package com.example.madcampweek1.ui.notifications;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madcampweek1.R;
import com.example.madcampweek1.ui.contact.MyRecyclerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TodoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TodoRecyclerAdapter mRecyclerAdapter;
    private ArrayList<TodoItem> todoItems = new ArrayList<>();
    private Context context;
    public String text;
    public String date;
    public View root;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_todo, container, false);
        String json = getJsonString();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.todoRecyclerView);

        todoItems = TodoItem.createTodoList(json, date);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new TodoRecyclerAdapter(getActivity(), this.todoItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        return root;
    }

    public void setDate(String date) {
        this.date = date;
        String json = getJsonString();
//
        todoItems = TodoItem.createTodoList(json, date);
    }

    public void setNewDate(String date) {
        this.date = date;
        String json = getJsonString();
//
        todoItems = TodoItem.createTodoList(json, date);
        mRecyclerAdapter = new TodoRecyclerAdapter(getActivity(), this.todoItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter.setDate(date);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private String getJsonString() {

        String fileTitle = "todo.json";
        File file = new File(context.getFilesDir(), fileTitle);

        String result = "";
        try {
            if (!file.exists()) {
                file.createNewFile();
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
            System.out.println("file not found");
        } catch (IOException e2) {
            Log.i("읽기오류",e2.getMessage());
            System.out.println("file not read");
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        root.requestLayout(); // viewPager2 의 height 를 동적으로 바꿔주기 위한 코드
    }
}