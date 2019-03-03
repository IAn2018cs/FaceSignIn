package cn.ian2018.facesignin.bean;

import java.io.Serializable;

public class HistoryActive implements Serializable {
    private String hInTime;
    private String hOutTime;
    private String hTime;
    private String hLocation;
    private String hActivityName;
    private String ActivityDescription;
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String gethInTime() {
        return hInTime;
    }

    public void sethInTime(String hInTime) {
        this.hInTime = hInTime;
    }

    public String gethOutTime() {
        return hOutTime;
    }

    public void sethOutTime(String hOutTime) {
        this.hOutTime = hOutTime;
    }

    public String gethTime() {
        return hTime;
    }

    public void sethTime(String hTime) {
        this.hTime = hTime;
    }

    public String getLocation() {
        return hLocation;
    }

    public void sethLocation(String hLocationt) {
        this.hLocation = hLocationt;
    }

    public String gethActivityName() {
        return hActivityName;
    }

    public void sethActivityName(String hActivityName) {
        this.hActivityName= hActivityName;
    }

    public String getActivityDescription() {
        return ActivityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        ActivityDescription = activityDescription;
    }
}
