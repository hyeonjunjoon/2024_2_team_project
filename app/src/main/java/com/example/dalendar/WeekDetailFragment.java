package com.example.dalendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dalendar.databinding.FragmentWeekdetailBinding;

import java.util.ArrayList;

public class WeekDetailFragment extends Fragment {

    private int weekIndex = -1; // 기본값 -1로 초기화
    private ArrayList<String> weekDates = new ArrayList<>(); // 빈 ArrayList로 초기화
    private TextView yearmonth ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 레이아웃 파일을 inflate
        return inflater.inflate(R.layout.fragment_weekdetail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView detail_1dateTv = view.findViewById(R.id.detail_1dateTv);
        TextView detail_2dateTv = view.findViewById(R.id.detail_2dateTv);
        TextView detail_3dateTv = view.findViewById(R.id.detail_3dateTv);
        TextView detail_4dateTv = view.findViewById(R.id.detail_4dateTv);
        TextView detail_5dateTv = view.findViewById(R.id.detail_5dateTv);
        TextView detail_6dateTv = view.findViewById(R.id.detail_6dateTv);
        TextView detail_7dateTv = view.findViewById(R.id.detail_7dateTv);
        yearmonth = view.findViewById(R.id.monthyearInDetail);

        // 전달받은 데이터 가져오기
        if (getArguments() != null) {
            weekIndex = getArguments().getInt("weekIndex", -1); // 기본값 -1
            weekDates = getArguments().getStringArrayList("weekDates");
            yearmonth.setText(getArguments().getString("year"));
        }


        detail_1dateTv.setText(weekDates.get(0));
        detail_2dateTv.setText(weekDates.get(1));
        detail_3dateTv.setText(weekDates.get(2));
        detail_4dateTv.setText(weekDates.get(3));
        detail_5dateTv.setText(weekDates.get(4));
        detail_6dateTv.setText(weekDates.get(5));
        detail_7dateTv.setText(weekDates.get(6));







    } //onViewCreated

}