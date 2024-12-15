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

    public RoutineAdapter(List<Routine> routineList, Context context) {
        this.routineList = routineList;
        this.context = this.context; // context 초기화
    }


    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_item, parent, false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Routine routine = routineList.get(position);

        holder.nameTextView.setText(routine.getName());
        holder.memoTextView.setText(routine.getMemo());
        holder.timeTextView.setText(routine.getTime());
        holder.daysTextView.setText(routine.getDays());
        holder.itemView.setBackgroundColor(Color.parseColor(routine.getColor()));

        // 이미지 설정
        if (routine.getImagePath() != null && !routine.getImagePath().isEmpty()) {
            Uri imageUri = Uri.parse(routine.getImagePath());
            holder.imageView.setImageURI(imageUri);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_app_icons); // 기본 이미지
        }

        holder.alarmSwitch.setChecked(routine.isEnabled());
        holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            routine.setEnabled(isChecked);
            toggleAlarm(isChecked, routine);
        });
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
            daysTextView = itemView.findViewById(R.id.routine_days);
            imageView = itemView.findViewById(R.id.routine_image);
            alarmSwitch = itemView.findViewById(R.id.routine_switch);
        }
    }

    private void toggleAlarm(boolean isOn, Routine routine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, routine.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (isOn) {
            Calendar calendar = Calendar.getInstance();
            String[] timeParts = routine.getTime().split(" ");
            String amPm = timeParts[0];
            String[] hourMinute = timeParts[1].split(":");

            int hour = Integer.parseInt(hourMinute[0]);
            int minute = Integer.parseInt(hourMinute[1]);

            if (amPm.equals("오후") && hour != 12) hour += 12;
            if (amPm.equals("오전") && hour == 12) hour = 0;

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel(); // 확실히 PendingIntent 해제
        }
    }

}
