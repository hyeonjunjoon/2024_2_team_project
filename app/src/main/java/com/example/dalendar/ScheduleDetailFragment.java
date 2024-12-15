package com.example.dalendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ScheduleDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleAdapter taskAdapter;
    private ArrayList<Task> taskList = new ArrayList<>();

    private String selectedDate; // 선택된 날짜

    public static ScheduleDetailFragment newInstance(String date) {
        ScheduleDetailFragment fragment = new ScheduleDetailFragment();
        Bundle args = new Bundle();
        args.putString("selectedDate", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.category1RecyclerView);
        Button addButton = view.findViewById(R.id.category1Btn);

        // 전달받은 날짜 가져오기
        if (getArguments() != null) {
            selectedDate = getArguments().getString("selectedDate");
        }

        taskAdapter = new ScheduleAdapter(taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskAdapter);


        // 새 일정 추가 버튼 클릭 시 처리
        addButton.setOnClickListener(v -> addNewTask());
    }


    private void addNewTask() {
        // 새 일정을 추가하는 기능 구현 (예: 다이얼로그로 입력받고 Firebase에 저장)
    }
}