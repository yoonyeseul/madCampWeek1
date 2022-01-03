package com.example.madcampweek1.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madcampweek1.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    CalendarView calendarView;
    ViewPager2 viewPager;
    TextView dateTextView;
    String selectedDate;

    FloatingActionButton floatingActionButton;

    TodoFragment toDoFragment = new TodoFragment();
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
                toDoFragment.setNewDate(selectedDate);
                initTodoAndDiaryText();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (textToChange == true) {
            textToChange = false;
            diaryFragment.setDiaryText(getDiaryText());
            toDoFragment.setNewDate(selectedDate);
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
        diaryFragment.setDiaryText(getDiaryText());
        toDoFragment.setContext(getContext());
        toDoFragment.setDate(selectedDate);
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
        File file = new File(getContext().getFilesDir(), "diary.json");
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