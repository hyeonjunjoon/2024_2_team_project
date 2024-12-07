package com.example.dalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class WeekView extends LinearLayout {

    private List<String> weekDates; // 주 데이터 저장

    public WeekView(Context context) {
        super(context);
        init();
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL); // 날짜를 가로로 나열하도록 설정
        setWeightSum(7); // 7개의 항목이 가로로 균등하게 배치되도록 설정
    }

    public void setWeekData(int weekIndex, List<String> weekDates) {
        this.weekDates = weekDates;

        // 기존 뷰 제거
        removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 날짜 추가
        for (String date : weekDates) {
            // day_item.xml을 이용하여 날짜를 추가
            LinearLayout dayLayout = (LinearLayout) inflater.inflate(R.layout.calendar_cell, this, false);

            TextView dayText = dayLayout.findViewById(R.id.dayText);
            dayText.setText(date.isEmpty() ? " " : date); // 빈 날짜 처리

            // 각 날짜를 LinearLayout에 추가
            addView(dayLayout);
        }
    }

}