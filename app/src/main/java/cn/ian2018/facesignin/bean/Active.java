package cn.ian2018.facesignin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/9
 */
public class Active implements Serializable {

    /**
     * msg : 查询成功
     * data : [{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","backTo":0,"week":0,"display":1,"activityName":"晨读测试2","rule":5,"location":"B3-123","endTime":"2018-03-09 08:00:00.0","id":27,"time":"2018-03-09 08:00:00.0","studentNum":"","account":"-1"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","backTo":0,"week":0,"display":1,"activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"","account":"-1"}]
     * sucessed : true
     */

    private String msg;
    private boolean sucessed;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSucessed() {
        return sucessed;
    }

    public void setSucessed(boolean sucessed) {
        this.sucessed = sucessed;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * activityDes : 请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐
         * backTo : 0
         * week : 0
         * display : 1
         * activityName : 晨读测试2
         * rule : 5
         * location : B3-123
         * endTime : 2018-03-09 08:00:00.0
         * id : 27
         * time : 2018-03-09 08:00:00.0
         * studentNum :
         * account : -1
         */

        private String activityDes;
        private int backTo;
        private int week;
        private int display;
        private String activityName;
        private int rule;
        private String location;
        private String endTime;
        private int id;
        private String time;
        private String studentNum;
        private String account;

        private boolean isScan = false;

        public String getActivityDes() {
            return activityDes;
        }

        public void setActivityDes(String activityDes) {
            this.activityDes = activityDes;
        }

        public int getBackTo() {
            return backTo;
        }

        public void setBackTo(int backTo) {
            this.backTo = backTo;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public int getDisplay() {
            return display;
        }

        public void setDisplay(int display) {
            this.display = display;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public int getRule() {
            return rule;
        }

        public void setRule(int rule) {
            this.rule = rule;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public boolean isScan() {
            return isScan;
        }

        public void setScan(boolean scan) {
            isScan = scan;
        }
    }
}
