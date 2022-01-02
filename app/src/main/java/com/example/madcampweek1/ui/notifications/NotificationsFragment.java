package com.example.madcampweek1.ui.notifications;

import android.app.FragmentManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madcampweek1.R;
import com.example.madcampweek1.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

        calendarView = binding.calendarView;
        someFunctionOfCalender();

        floatingActionButton = binding.floatingActionButton;
        someFuncOfFloatingBtn();

        setDateTextView();

        return root;
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

    private void someFunctionOfCalender() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String text = year + "." + (month / 10 == 0 ? "0" : "") + (month + 1) + "."
                        + (dayOfMonth / 10 == 0 ? "0" : "") + dayOfMonth;
                selectedDate = text;
                dateTextView.setText(selectedDate);
            }
        });
    }

    private void someFuncOfFloatingBtn() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class PagerAdapter extends FragmentStateAdapter {
        public PagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            int idx = position % 2;
            if (idx == 0)
                return new ToDoFragment();
            return new DiaryFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}