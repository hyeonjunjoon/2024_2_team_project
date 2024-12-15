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
    private int id; // 알람 ID를 위한 필드 추가


    // 기본 생성자
    public Routine() {
        this.actName = "";
        this.memo = "";
        this.time = "";
        this.isEnabled = false;
        this.days = "";
        this.color = "#FFFFFF";
        this.imagePath = "";
        this.id = (int) System.currentTimeMillis(); // 기본 ID 설정
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
        this.id = (int) System.currentTimeMillis(); // 생성 시 고유 ID 생성
    }

    // Getter와 Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return actName; }
    public void setName(String name) { this.actName = name; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { this.isEnabled = enabled; }


    public String getDays() { return days; }
    public void setDays(String days) { this.days = days; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
