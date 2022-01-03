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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private TodoRecyclerAdapter mRecyclerAdapter;
    private ArrayList<TodoItem> todoItems = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    public String text;
    public String date;
    public View root;
    private TextView contentText;

    public TodoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance(String param1, String param2) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_todo, container, false);
        String json = getJsonString();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.todoRecyclerView);

        todoItems = TodoItem.createTodoList(json, date);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new TodoRecyclerAdapter(getActivity(), this.todoItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);

//        System.out.println("Todo Fragment : onCreateView() 호출됨");
        return root;
    }
    public void setDate(String date) {
        this.date = date;
        System.out.println("current todo date: " + date);
        String json = getJsonString();
//
        todoItems = TodoItem.createTodoList(json, date);
        System.out.println(json);
    }
    public void setNewDate(String date) {
        this.date = date;
        String json = getJsonString();
//
        todoItems = TodoItem.createTodoList(json, date);
        System.out.println(json);
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
    public void onResume() {
        super.onResume();
        root.requestLayout(); // viewPager2 의 height 를 동적으로 바꿔주기 위한 코드
    }
}