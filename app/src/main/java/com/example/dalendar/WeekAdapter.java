package com.example.dalendar;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    private List<List<String>> weeks; // 7일씩 나눈 날짜 리스트



    public interface OnWeekClickListener {
        void onWeekClick(int weekIndex, List<String> weekDates); // 주 데이터를 전달
    }

    private OnWeekClickListener weekClickListener;

    public void setOnWeekClickListener(OnWeekClickListener listener) {
        this.weekClickListener = listener;
    }

    public WeekAdapter(List<List<String>> weeks, OnWeekClickListener weekClickListener) {
        this.weeks = weeks;
        this.weekClickListener = weekClickListener;
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeekView weekView = new WeekView(parent.getContext());
        return new WeekViewHolder(weekView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        List<String> weekDates = weeks.get(position);
        holder.bind(position, weekDates, weekClickListener);
    }

    @Override
    public int getItemCount() {
        return weeks.size();
    }

    static class WeekViewHolder extends RecyclerView.ViewHolder {
        private WeekView weekView;

        public WeekViewHolder(@NonNull WeekView itemView) {
            super(itemView);
            this.weekView = itemView;
        }

        public void bind(int weekIndex, List<String> weekDates, OnWeekClickListener listener) {
            weekView.setWeekData(weekIndex, weekDates);

            // 클릭 이벤트를 직접 설정
            weekView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onWeekClick(weekIndex, weekDates);
                }
            });
        }
    }

    // 날짜를 7일씩 그룹화하는 메서드
    public static List<List<String>> getWeeksFromDates(List<String> dates) {
        List<List<String>> weeks = new ArrayList<>();
        List<String> currentWeek = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            currentWeek.add(dates.get(i));

            // 7일이 되면 새로운 주를 추가
            if (currentWeek.size() == 7 || i == dates.size() - 1) {

                weeks.add(currentWeek);
                currentWeek = new ArrayList<>(); // 새로운 주 시작
            }
        }

        return weeks;
    }


}