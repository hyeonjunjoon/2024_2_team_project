package com.example.dalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class RoutineListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private List<Routine> routineList;

    public RoutineListFragment() {
    }

    public static RoutineListFragment newInstance(String param1, String param2) {
        RoutineListFragment fragment = new RoutineListFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 루틴 리스트 초기화
        routineList = new ArrayList<>();
        loadRoutinesFromPreferences(); // 데이터 로드

        // Routine_Maker로부터 데이터 받기
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("actName")) {
            String actName = intent.getStringExtra("actName");
            String memo = intent.getStringExtra("memo");
            String time = intent.getStringExtra("time");
            String days = intent.getStringExtra("days");
            String color = intent.getStringExtra("color");
            boolean isEnabled = intent.getBooleanExtra("isEnabled", true);
            String imagePath = intent.getStringExtra("imagePath");

            if (!isRoutineAlreadyInList(actName, memo, time, days, color)) {
                routineList.add(new Routine(actName, memo, time, isEnabled, days, color, imagePath));
                saveRoutinesToPreferences();
            }
        }

        // RecyclerView 어댑터 설정
        adapter = new RoutineAdapter(routineList);
        recyclerView.setAdapter(adapter);

        // 스와이프 삭제 활성화
        enableSwipeToDelete();

        // 플러스 버튼 클릭 시 동작 설정
        ImageButton inRtnListPlusBtn = view.findViewById(R.id.in_RtnList_new_Button);
        inRtnListPlusBtn.setOnClickListener(v -> {
            Intent intent2 = new Intent(getActivity(), RoutineRecommandFragment.class);
            startActivity(intent2);
        });

        return view;
    }

    private void loadRoutinesFromPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RoutinePrefs", getActivity().MODE_PRIVATE);
        String routinesData = sharedPreferences.getString("routines", "");

        if (!routinesData.isEmpty()) {
            String[] routines = routinesData.split("\\|");
            for (String routine : routines) {
                String[] parts = routine.split(";");
                if (parts.length == 7) { // 데이터 개수 확인
                    routineList.add(new Routine(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[5]), parts[3], parts[4], parts[6]));
                }
            }
        }
    }

    private boolean isRoutineAlreadyInList(String name, String memo, String time, String days, String color) {
        for (Routine routine : routineList) {
            if (routine.getName().equals(name)
                    && routine.getMemo().equals(memo)
                    && routine.getTime().equals(time)
                    && routine.getDays().equals(days)
                    && routine.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    private void saveRoutinesToPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RoutinePrefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder routinesData = new StringBuilder();
        for (Routine routine : routineList) {
            routinesData.append(routine.getName()).append(";")
                    .append(routine.getMemo()).append(";")
                    .append(routine.getTime()).append(";")
                    .append(routine.getDays()).append(";")
                    .append(routine.getColor()).append(";")
                    .append(routine.isEnabled()).append(";")
                    .append(routine.getImagePath()).append("|");
        }

        if (routinesData.length() > 0) {
            routinesData.setLength(routinesData.length() - 1);
        }

        editor.putString("routines", routinesData.toString());
        editor.apply();
    }

    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                routineList.remove(position);
                adapter.notifyItemRemoved(position);
                saveRoutinesToPreferences();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(dX > 0 ? Color.GREEN : Color.RED);
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
