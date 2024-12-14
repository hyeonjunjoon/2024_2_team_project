package com.example.dalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private List<Routine> routineList;
    private Context context;

    public RoutineAdapter(List<Routine> routineList) {
        this.routineList = routineList;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.routine_item, parent, false);
        return new RoutineViewHolder(view);
    }

    private SaveRoutineCallback callback;

    public RoutineAdapter(List<Routine> routineList, SaveRoutineCallback callback) {
        this.routineList = routineList;
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Routine routine = routineList.get(position);

        holder.nameTextView.setText(routine.getName());
        holder.memoTextView.setText(routine.getMemo());
        holder.daysTextView.setText(routine.getDays()); // 요일 데이터 설정
        String displayTime = routine.getTime();
        holder.timeTextView.setText(displayTime);

        // 이미지 경로 처리
        if (routine.getImagePath() != null && !routine.getImagePath().isEmpty()) {
            Uri imageUri = Uri.parse(routine.getImagePath());
            holder.imageView.setImageURI(imageUri);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_app_icons); // 기본 이미지 설정
        }

        holder.itemView.setBackgroundColor(Color.parseColor(routine.getColor()));

        holder.alarmSwitch.setChecked(routine.isEnabled());
        holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 스위치 상태 변경에 따라 알람 활성화/비활성화
            routine.setEnabled(isChecked);
            toggleAlarm(position, isChecked, routine);
            if (callback != null) {
                callback.saveRoutinesToPreferences(); // 저장 콜백 호출
            }
        });
    }

    public interface SaveRoutineCallback {
        void saveRoutinesToPreferences();
    }


    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public static class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, memoTextView, timeTextView, daysTextView;
        ImageView imageView;
        Switch alarmSwitch;

        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.routine_name);
            memoTextView = itemView.findViewById(R.id.routine_memo);
            timeTextView = itemView.findViewById(R.id.routine_time);
            daysTextView=itemView.findViewById(R.id.routine_days);
            imageView = itemView.findViewById(R.id.routine_image); // ImageView 연결
            alarmSwitch = itemView.findViewById(R.id.routine_switch);
        }
    }

    private void toggleAlarm(int requestCode, boolean isOn, Routine routine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            if (isOn) {
                try {
                    // 알람 설정
                    Calendar calendar = Calendar.getInstance();

                    // 시간과 요일 정보 파싱
                    String[] timeParts = routine.getTime().split(" ");
                    String amPm = timeParts[0]; // "오전"/"오후"
                    String[] hourMinute = timeParts[1].split(":");
                    int hour = Integer.parseInt(hourMinute[0]);
                    int minute = Integer.parseInt(hourMinute[1]);

                    // AM/PM에 따라 시간 변환
                    if (amPm.equalsIgnoreCase("PM") && hour != 12) hour += 12;
                    if (amPm.equalsIgnoreCase("AM") && hour == 12) hour = 0;

                    // 알람 설정 시간 지정
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    // 알람이 과거면 다음날로 설정
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                    }

                    // 알람 등록
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                } catch (Exception e) {
                    e.printStackTrace(); // 오류 로그 출력
                }
            } else {
                // 알람 해제
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }


    // 요일 문자열을 Calendar 상수로 변환
    private int getDayOfWeek(String day) {
        switch (day) {
            case "월": return Calendar.MONDAY;
            case "화": return Calendar.TUESDAY;
            case "수": return Calendar.WEDNESDAY;
            case "목": return Calendar.THURSDAY;
            case "금": return Calendar.FRIDAY;
            case "토": return Calendar.SATURDAY;
            case "일": return Calendar.SUNDAY;
            default: return -1;
        }
    }


}
