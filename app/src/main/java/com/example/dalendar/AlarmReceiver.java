package com.example.dalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람 소리 가져오기
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            // 알람 소리가 설정되지 않은 경우 기본 알림 소리로 대체
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // MediaPlayer로 알람 소리 재생
        mediaPlayer = MediaPlayer.create(context, alarmUri);
        mediaPlayer.start();

        // 알람 소리 재생 중인 상태를 유지
        mediaPlayer.setOnCompletionListener(mp -> {
            // 소리가 끝나면 MediaPlayer 해제
            mediaPlayer.release();
        });
    }
}

