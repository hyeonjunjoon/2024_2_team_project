package com.example.dalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RoutineListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String param1;
    private String param2;

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private List<Routine> routineList;
    private Context context;

    public RoutineListFragment() {
        // Required empty public constructor
    }

    // 프래그먼트 생성 시 인자를 전달하는 메서드
    public static RoutineListFragment newInstance(String param1, String param2) {
        RoutineListFragment fragment = new RoutineListFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        routineList = new ArrayList<>();
        loadRoutinesFromPreferences();


        adapter = new RoutineAdapter(routineList, context);
        recyclerView.setAdapter(adapter);


        // RoutineMakerFragment에서 전달된 데이터 확인 및 추가
        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String memo = getArguments().getString("memo");
            String time = getArguments().getString("time");
            String days = getArguments().getString("days");
            String color = getArguments().getString("color");
            String imagePath = getArguments().getString("imagePath");

            if (name != null && memo != null) {
                // 중복 확인 후 추가
                if (!isRoutineAlreadyInList(name, memo, time, days, color)) {
                    routineList.add(new Routine(name, memo, time, true, days, color, imagePath));
                    saveRoutinesToPreferences();
                }
            }
        }

        enableSwipeToDelete();

        // 플러스 버튼으로 RoutineRecommandFragment로 이동
        ImageButton in_rtnList_plus_btn = view.findViewById(R.id.in_RtnList_new_Button);
        in_rtnList_plus_btn.setOnClickListener(v -> {
            Fragment fragment = new RoutineRecommandFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadRoutinesFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        String routinesData = sharedPreferences.getString("routines", "");

        routineList.clear(); // 기존 리스트 초기화

        if (!routinesData.isEmpty()) {
            String[] routines = routinesData.split("\\|");
            for (String routine : routines) {
                String[] parts = routine.split(";");
                if (parts.length == 6) {
                    routineList.add(new Routine(parts[0], parts[1], parts[2], true, parts[3], parts[4], parts[5]));
                }
            }
        }
    }





    private void saveRoutinesToPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
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
                routineList.remove(position); // 리스트에서 제거
                adapter.notifyItemRemoved(position); // RecyclerView 업데이트
                saveRoutinesToPreferences(); // SharedPreferences 업데이트
                Toast.makeText(context, "루틴이 삭제되었습니다!", Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }


    private boolean isRoutineAlreadyInList(String name, String memo, String time, String days, String color) {
        for (Routine routine : routineList) {
            if (routine.getName().equals(name) &&
                    routine.getMemo().equals(memo) &&
                    routine.getTime().equals(time) &&
                    routine.getDays().equals(days) &&
                    routine.getColor().equals(color)) {
                return true; // 중복된 루틴이 존재
            }
        }
        return false;
    }
}
