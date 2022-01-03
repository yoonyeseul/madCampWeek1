package com.example.madcampweek1.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madcampweek1.R;
import com.example.madcampweek1.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    CalendarView calendarView;
    ViewPager2 viewPager;
    TextView dateTextView;
    String selectedDate;

    FloatingActionButton floatingActionButton;

    ToDoFragment toDoFragment = new ToDoFragment();
    DiaryFragment diaryFragment = new DiaryFragment();

    public static boolean textToChange = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager = binding.viewPager;
        viewPager.setAdapter(new PagerAdapter(getActivity()));

        TabLayout tabLayout = binding.tabLayout;

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "To-do" : "diary")
        ).attach();

        setCalendarListener();
        setFloatingBtnListener();
        setDateTextView();
        initTodoAndDiaryText();

        return root;
    }

    private void setCalendarListener() {
        calendarView = binding.calendarView;
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String text = year + "." + (month / 10 == 0 ? "0" : "") + (month + 1) + "."
                        + (dayOfMonth / 10 == 0 ? "0" : "") + dayOfMonth;
                selectedDate = text;
                dateTextView.setText(selectedDate);

                initTodoAndDiaryText();
//                diaryFragment.setDiaryText(getDiaryText()); // (view 만들어지기 전 날짜 선택하는 경우 text 안 바뀜)
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (textToChange == true) {
            textToChange = false;
            diaryFragment.setDiaryText(getDiaryText());
        }
    }

    private void setFloatingBtnListener() {
        floatingActionButton = binding.floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (viewPager.getCurrentItem() == 0)
                    intent = new Intent(getActivity(), TodoActivity.class);
                else
                    intent = new Intent(getActivity(), DiaryActivity.class);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            }
        });
    }

    private void setDateTextView() {
        dateTextView = binding.dateTextView;
        String currentDate = convertDateToString(calendarView.getDate());
        dateTextView.setText(currentDate);
        selectedDate = currentDate;
    }

    public static String convertDateToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.DD");
        return sdf.format(date);
    }

    public void initTodoAndDiaryText() {
        if (diaryFragment.root == null) {
            diaryFragment.text = getDiaryText();
            System.out.println("루트가 null. . .. ... . .... .. .. . .. ...... .. .");
        }
        else
            diaryFragment.setDiaryText(getDiaryText());
    }

    private String getDiaryText() {
        String currentDiary = "일기를 작성해주세요";
        try {
            JSONObject obj = new JSONObject(readFile());
            currentDiary = obj.getString(selectedDate);
        } catch (JSONException e) {
        }
        System.out.println("currentDiary : " + currentDiary);
        return currentDiary;
    }

    public String readFile() {
        File file = new File(getActivity().getFilesDir(), "diary.json");
        String result = "";
        try {
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
        System.out.println("readFile result: " + result); // 내 에뮬레이터에서 이 코드가 호출되는 이유 ..?
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class PagerAdapter extends FragmentStateAdapter {
        final private int itemCount = 2;

        public PagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            int idx = position % itemCount;
            if (idx == 0)
                return toDoFragment;
            return diaryFragment;
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }
}