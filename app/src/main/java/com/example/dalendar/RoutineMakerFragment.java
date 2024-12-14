package com.example.dalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineMakerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineMakerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ToggleButton day1Toggle, day2Toggle, day3Toggle, day4Toggle, day5Toggle, day6Toggle, day7Toggle;
    private Spinner amPmSpinner, hourSpinner, minuteSpinner;

    private static final int PICK_IMAGE_REQUEST = 1;
    private String selectedImagePath = ""; // 선택된 이미지 경로 저장
    private String selectedColor = "#FFFFFF"; // 기본 색상: 흰색

    private Context context;

    public RoutineMakerFragment() {
        // Required empty public constructor
    }

    public static RoutineMakerFragment newInstance(String param1, String param2) {
        RoutineMakerFragment fragment = new RoutineMakerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_maker, container, false);

        // Photo button 클릭 이벤트 추가
        ImageView photoButton = view.findViewById(R.id.in_RtnMkr_photoBtn);
        photoButton.setOnClickListener(v -> openGallery());

        // 추천 액티비티에서 카테고리 가져오기
        Bundle args = getArguments();
        String selectedHobby = args != null ? args.getString("selectedHobby") : null;

        TextView categoryTextView = view.findViewById(R.id.inRtnMkrCategory);
        if (selectedHobby != null) {
            categoryTextView.setText(selectedHobby);
        }

        // 색상 버튼 초기화
        ImageButton redButton = view.findViewById(R.id.in_RtnMkr_colorBtn_r);
        ImageButton blueButton = view.findViewById(R.id.in_RtnMkr_colorBtn_b);
        ImageButton yellowButton = view.findViewById(R.id.in_RtnMkr_colorBtn_y);
        ImageButton greenButton = view.findViewById(R.id.in_RtnMkr_colorBtn_g);
        ImageButton magentaButton = view.findViewById(R.id.in_RtnMkr_colorBtn_m);

        redButton.setOnClickListener(v -> selectedColor = "#ECBFC7");
        blueButton.setOnClickListener(v -> selectedColor = "#CBDCEE");
        yellowButton.setOnClickListener(v -> selectedColor = "#FAF8CC");
        greenButton.setOnClickListener(v -> selectedColor = "#DEEBDF");
        magentaButton.setOnClickListener(v -> selectedColor = "#F0EBF0");

        // 토글 및 스피너 초기화
        day1Toggle = view.findViewById(R.id.inRtnMkr_Day1tg);
        day2Toggle = view.findViewById(R.id.inRtnMkr_Day2tg);
        day3Toggle = view.findViewById(R.id.inRtnMkr_Day3tg);
        day4Toggle = view.findViewById(R.id.inRtnMkr_Day4tg);
        day5Toggle = view.findViewById(R.id.inRtnMkr_Day5tg);
        day6Toggle = view.findViewById(R.id.inRtnMkr_Day6tg);
        day7Toggle = view.findViewById(R.id.inRtnMkr_Day7tg);

        amPmSpinner = view.findViewById(R.id.in_RtnMkr_spinner_am_pm);
        hourSpinner = view.findViewById(R.id.in_RtnMkr_spinner_hour);
        minuteSpinner = view.findViewById(R.id.in_RtnMkr_spinner_minute);

        // 완료 버튼
        Button finishButton = view.findViewById(R.id.in_RtnMkr_finish_btn);
        finishButton.setOnClickListener(v -> {
            String actName = ((EditText) view.findViewById(R.id.inRtnMkrActName)).getText().toString();
            String memo = ((EditText) view.findViewById(R.id.inRtnMkrMemo)).getText().toString();
            String amPm = amPmSpinner.getSelectedItem().toString();
            String hour = hourSpinner.getSelectedItem().toString();
            String minute = minuteSpinner.getSelectedItem().toString();
            String time = amPm + " " + hour + ":" + minute;
            String selectedDays = getSelectedDays();

            // 데이터 저장
            saveRoutineToPreferences(actName, memo, time, selectedDays, selectedColor, true);

            Toast.makeText(context, "알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private String getSelectedDays() {
        StringBuilder daysBuilder = new StringBuilder();

        if (day1Toggle.isChecked()) daysBuilder.append("월, ");
        if (day2Toggle.isChecked()) daysBuilder.append("화, ");
        if (day3Toggle.isChecked()) daysBuilder.append("수, ");
        if (day4Toggle.isChecked()) daysBuilder.append("목, ");
        if (day5Toggle.isChecked()) daysBuilder.append("금, ");
        if (day6Toggle.isChecked()) daysBuilder.append("토, ");
        if (day7Toggle.isChecked()) daysBuilder.append("일, ");

        if (daysBuilder.length() > 0) {
            daysBuilder.setLength(daysBuilder.length() - 2);
        }

        return daysBuilder.toString();
    }

    private void saveRoutineToPreferences(String name, String memo, String time, String days, String color, boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String existingData = sharedPreferences.getString("routines", "");
        String newRoutine = name + ";" + memo + ";" + time + ";" + days + ";" + color + ";" + isEnabled;

        if (!existingData.isEmpty()) {
            existingData += "|";
        }

        editor.putString("routines", existingData + newRoutine);
        editor.apply();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImagePath = data.getData().toString();
        }
    }
}
