// 알람 설정이나,, 루틴 설정부터 리스트 연계 중 필요한 데이터 모델 파일

package com.example.dalendar;

public class Routine {
    private String actName;
    private String memo;
    private String time;
    private boolean isEnabled;
    private String days;
    private String color;
    private String imagePath;

    // 기본 생성자
    public Routine() {
        this.actName = "";
        this.memo = "";
        this.time = "";
        this.isEnabled = false;
        this.days = "";
        this.color = "#FFFFFF"; // 기본 색상
        this.imagePath =imagePath ;
    }

    // 생성자 (이미지 경로 포함)
    public Routine(String name, String memo, String time, boolean isEnabled, String days, String color, String imagePath) {
        this.actName = name;
        this.memo = memo;
        this.time = time;
        this.isEnabled = isEnabled;
        this.days = days;
        this.color = color;
        this.imagePath = imagePath;
    }

    // 생성자 (이미지 경로 미포함, 기본값 "")
    public Routine(String name, String memo, String time, boolean isAlarmEnabled, String days, String color) {
        this(name, memo, time, isAlarmEnabled, days, color, "");
    }

    // Getter와 Setter
    public String getName() { return actName; }
    public void setName(String name) { this.actName = name; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean alarmEnabled) { isEnabled = isEnabled; }

    public String getDays() { return days; }
    public void setDays(String days) { this.days = days; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
