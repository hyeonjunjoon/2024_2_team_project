package com.example.dalendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private TextView monthYearText;
    private LocalDate selectedDate;

    public CalendarFragment() {
    }
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monthYearText = view.findViewById(R.id.monthYearText);
        recyclerView = view.findViewById(R.id.recyclerView);
        ImageButton preBtn = view.findViewById(R.id.pre_btn);
        ImageButton nextBtn = view.findViewById(R.id.next_btn);

        selectedDate = LocalDate.now();

        setMonthView();

        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 월+1 변수에 담기
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });

    } //onViewCreated

    private String monthyearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM");
        return date.format(formatter);
    }

    private void setMonthView() {
        monthYearText.setText(monthyearFromDate(selectedDate));

        ArrayList<String> dayList = daysInMonthArray(selectedDate);

        // 주 단위로 나누기
        List<List<String>> weeks = new ArrayList<>();
        for (int i = 0; i < dayList.size(); i += 7) {
            weeks.add(dayList.subList(i, Math.min(i + 7, dayList.size())));
        }

        // 어댑터에 주 클릭 이벤트 전달
        WeekAdapter adapter = new WeekAdapter(weeks, new WeekAdapter.OnWeekClickListener() {
            @Override
            public void onWeekClick(int weekIndex, List<String> weekDates) {
                // 클릭 이벤트 처리
                WeekDetailFragment weekDetailFragment = new WeekDetailFragment();

                // 데이터를 전달할 Bundle 생성
                Bundle bundle = new Bundle();
                bundle.putInt("weekIndex", weekIndex + 1);
                bundle.putStringArrayList("weekDates", new ArrayList<>(weekDates)); // List를 ArrayList로 변환
                bundle.putString("year", monthYearText.getText().toString());

                // Bundle을 Fragment에 설정
                weekDetailFragment.setArguments(bundle);

                // Fragment 교체
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main, weekDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // RecyclerView 설정
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1); // 한 행씩 표시
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> dayList = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);

        // 해당 월의 마지막 날짜 가져오기 (예: 28, 30, 31)
        int lastDay = yearMonth.lengthOfMonth();

        // 해당 월의 첫 번째 날 가져오기
        LocalDate firstDay = selectedDate.withDayOfMonth(1);

        // 첫 번째 날의 요일 가져오기 (일요일: 0, 월요일: 1, ..., 토요일: 6)
        // 기존 달력과 달리 일요일이 시작이므로 일요일을 0으로 설정!
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7;

        // 공백 채우기 (첫 번째 날 이전의 빈 칸)
        for (int i = 0; i < dayOfWeek; i++) {
            dayList.add("");


        }

        // 실제 날짜 채우기
        for (int day = 1; day <= lastDay; day++) {
            dayList.add(String.valueOf(day));
        }

        // 나머지 빈 칸 채우기 (42칸 고정)
        while (dayList.size() < 42) {
            dayList.add("");
        }

        return dayList;
    }

}
