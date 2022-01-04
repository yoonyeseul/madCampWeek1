package com.example.madcampweek1.ui.notifications;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madcampweek1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DiaryFragment extends Fragment {
    public String text;
    public View root;
    private TextView diaryTextView;

    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_diary, container, false);
        diaryTextView = root.findViewById(R.id.diaryTextViewInFragment);
        textViewWhenNoDiary = root.findViewById(R.id.textViewWhenNoDiary);
        if (text.length() == 0) {
            textViewWhenNoDiary.setVisibility(View.VISIBLE);
            diaryTextView.setVisibility(View.INVISIBLE);
        }
        else {
            textViewWhenNoDiary.setVisibility(View.INVISIBLE);
            diaryTextView.setText(text);
        }
        return root;
    }
    private TextView textViewWhenNoDiary;

    public void setDiaryText(Context context, String selectedDate) {
        String text = getDiaryText(context, selectedDate);
        if (root == null) {
            this.text = text;
            return;
        }
        diaryTextView = root.findViewById(R.id.diaryTextViewInFragment);
        textViewWhenNoDiary = root.findViewById(R.id.textViewWhenNoDiary);
        if (text.length() == 0) {
            textViewWhenNoDiary.setVisibility(View.VISIBLE);
            diaryTextView.setVisibility(View.INVISIBLE);
        }
        else {
            textViewWhenNoDiary.setVisibility(View.INVISIBLE);
            diaryTextView.setVisibility(View.VISIBLE);
            diaryTextView.setText(text);
        }
    }

    private String getDiaryText(Context context, String selectedDate) {
        String currentDiary = "";
        try {
            JSONObject obj = new JSONObject(readFile(context));
            currentDiary = obj.getString(selectedDate);
        } catch (JSONException e) {
        }
        return currentDiary;
    }

    private String readFile(Context context) {
        File file = new File(context.getFilesDir(), "diary.json");
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
            System.out.println("readFile() Exception 발생");
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        root.requestLayout(); // viewPager2 의 height 를 동적으로 바꿔주기 위한 코드
    }
}