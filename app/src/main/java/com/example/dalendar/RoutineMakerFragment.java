package com.example.dalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class RoutineMakerFragment extends Fragment {

    private static final String ARG_PARAM1 = "selectedHobby";
    private static final String ARG_PARAM2 = "defaultMemo";

    private String selectedHobby;
    private String defaultMemo;

    private ToggleButton day1Toggle, day2Toggle, day3Toggle, day4Toggle, day5Toggle, day6Toggle, day7Toggle;
    private Spinner amPmSpinner, hourSpinner, minuteSpinner;
    private String selectedImagePath = "";
    private String selectedColor = "#FFFFFF";
    private Context context;

    // 이미지 선택을 위한 ActivityResultLauncher
    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    selectedImagePath = selectedImageUri.toString();
                    ImageView photoButton = getView().findViewById(R.id.in_RtnMkr_photoBtn);
                    photoButton.setImageURI(selectedImageUri);
                }
            }
    );

    public RoutineMakerFragment() {
        // Required empty public constructor
    }

    // 프래그먼트 생성 시 인자 전달을 위한 newInstance 메서드
    public static RoutineMakerFragment newInstance(String selectedHobby, String defaultMemo) {
        RoutineMakerFragment fragment = new RoutineMakerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, selectedHobby);
        args.putString(ARG_PARAM2, defaultMemo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_maker, container, false);

        // 인자에서 데이터 가져오기
        if (getArguments() != null) {
            selectedHobby = getArguments().getString(ARG_PARAM1);
            defaultMemo = getArguments().getString(ARG_PARAM2);
        }

        // UI 요소 초기화
        initUI(view);
        setupColorButtons(view);
        setupTogglesAndSpinners(view);
        setupFinishButton(view);

        return view;
    }

    private void initUI(View view) {
        TextView categoryTextView = view.findViewById(R.id.inRtnMkrCategory);
        categoryTextView.setText(selectedHobby);

        EditText memoEditText = view.findViewById(R.id.inRtnMkrMemo);
        memoEditText.setText(defaultMemo);

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

        ImageView photoButton = view.findViewById(R.id.in_RtnMkr_photoBtn);
        photoButton.setOnClickListener(v -> openGallery());
    }

    private void setupColorButtons(View view) {
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
    }

    private void setupTogglesAndSpinners(View view) {
        ArrayAdapter<String> amPmAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"오전", "오후"});
        amPmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amPmSpinner.setAdapter(amPmAdapter);

        String[] hourOptions = new String[12];
        for (int i = 0; i < 12; i++) hourOptions[i] = String.valueOf(i + 1);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, hourOptions);
        hourSpinner.setAdapter(hourAdapter);

        String[] minuteOptions = new String[60];
        for (int i = 0; i < 60; i++) minuteOptions[i] = String.valueOf(i);
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, minuteOptions);
        minuteSpinner.setAdapter(minuteAdapter);

        ToggleButton[] toggleButtons = {
                day1Toggle, day2Toggle, day3Toggle, day4Toggle, day5Toggle, day6Toggle, day7Toggle
        };

        for (ToggleButton toggleButton : toggleButtons) {
            toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    toggleButton.setBackgroundResource(R.drawable.day_picker_checked);
                } else {
                    toggleButton.setBackgroundResource(R.drawable.day_picker_unchecked);
                }
            });
        }
    }

    private void setupFinishButton(View view) {
        Button finishButton = view.findViewById(R.id.in_RtnMkr_finish_btn);
        finishButton.setOnClickListener(v -> {
            String name = ((EditText) view.findViewById(R.id.inRtnMkrActName)).getText().toString();
            String memo = ((EditText) view.findViewById(R.id.inRtnMkrMemo)).getText().toString();
            String time = amPmSpinner.getSelectedItem().toString() + " " +
                    hourSpinner.getSelectedItem().toString() + ":" +
                    minuteSpinner.getSelectedItem().toString();
            String days = getSelectedDays();

            // 입력된 정보를 문자열로 저장
            String routineString = name + ";" + memo + ";" + time + ";" + days + ";" + selectedColor + ";" + selectedImagePath;

            // SharedPreferences에 저장
            SharedPreferences sharedPreferences = context.getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String existingData = sharedPreferences.getString("routines", "");

            if (!existingData.isEmpty()) {
                existingData += "|"; // 기존 데이터가 있다면 구분자 추가
            }
            editor.putString("routines", existingData + routineString);
            editor.apply();

            Toast.makeText(context, "루틴이 저장되었습니다!", Toast.LENGTH_SHORT).show();

            // RoutineListFragment로 이동
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new RoutineListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
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
            daysBuilder.setLength(daysBuilder.length() - 2); // 마지막 쉼표 제거
        }

        return daysBuilder.toString();
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
