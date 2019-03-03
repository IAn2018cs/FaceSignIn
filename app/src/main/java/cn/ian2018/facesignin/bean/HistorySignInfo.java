package cn.ian2018.facesignin.bean;

import java.util.List;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class HistorySignInfo {

    /**
     * msg : 查询成功
     * data : [{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-03-10 14:13:49.0","activityName":"晨读测试2","rule":5,"location":"B3-123","endTime":"2018-03-09 08:00:00.0","id":27,"time":"2018-03-09 08:00:00.0","studentNum":"20154233005","outTime":"2018-03-10 14:15:51.0"},{"activityDes":"请准时到达指定位置，站好队形，等待跑步。","inTime":"2018-03-10 14:14:25.0","activityName":"跑步","rule":4,"location":"生活区移动营业厅门口","endTime":"2018-08-20 07:30:00.0","id":24,"time":"2018-05-11 07:00:00.0","studentNum":"20154233005","outTime":"2018-03-10 14:18:27.0"},{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-03-12 18:19:07.0","activityName":"第一大节值班","rule":2,"location":"B3-123","endTime":"2018-07-20 09:40:00.0","id":1,"time":"2017-10-22 08:00:00.0","studentNum":"20154233005","outTime":"2018-03-12 18:19:10.0"},{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-03-12 18:19:21.0","activityName":"第二大节值班","rule":2,"location":"B3-123","endTime":"2018-07-20 11:50:00.0","id":2,"time":"2017-10-22 10:10:00.0","studentNum":"20154233005","outTime":"2018-03-12 18:19:22.0"},{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-03-12 18:19:44.0","activityName":"第五大节值班","rule":2,"location":"B3-123","endTime":"2018-07-20 21:35:00.0","id":5,"time":"2017-10-22 19:00:00.0","studentNum":"20154233005","outTime":"2018-03-12 18:19:53.0"},{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-03-12 18:20:39.0","activityName":"第四大节值班","rule":2,"location":"B3-123","endTime":"2018-07-20 18:00:00.0","id":4,"time":"2017-10-22 16:20:00.0","studentNum":"20154233005","outTime":"2018-03-12 18:20:40.0"},{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-04-02 08:00:00.0","activityName":"第一大节值班","rule":2,"location":"B3-123","endTime":"2018-07-20 09:40:00.0","id":1,"time":"2017-10-22 08:00:00.0","studentNum":"20154233005","outTime":"2018-04-02 09:40:00.0"},{"activityDes":"请准时进行签到，值班结束务必签离，离开时将桌椅摆放整齐","inTime":"2018-04-02 14:30:00.0","activityName":"第三大节值班","rule":2,"location":"B3-123","endTime":"2018-07-20 16:10:00.0","id":3,"time":"2017-10-22 14:30:00.0","studentNum":"20154233005","outTime":"2018-04-02 16:10:00.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2018-12-09 17:08:10.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2018-12-09 17:08:12.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2018-12-11 09:10:49.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2018-12-11 09:17:23.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2018-12-13 19:56:40.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2018-12-13 19:56:41.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2019-02-24 17:02:03.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2019-01-05 00:00:00.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2019-02-24 18:01:35.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2019-02-24 18:01:35.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2019-02-24 20:39:06.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2019-02-24 20:39:06.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2019-02-24 20:41:05.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2019-02-24 20:41:05.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2019-02-24 20:44:12.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2019-02-24 20:44:12.0"},{"activityDes":"这是一个活动测试，请注意，这是一个活动测试","inTime":"2019-02-24 21:23:40.0","activityName":"活动测试","rule":1,"location":"北京 天通苑北","endTime":"2019-12-09 17:01:31.0","id":35,"time":"2018-12-09 06:00:00.0","studentNum":"20154233005","outTime":"2019-02-24 22:25:01.0"}]
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
         * inTime : 2018-03-10 14:13:49.0
         * activityName : 晨读测试2
         * rule : 5
         * location : B3-123
         * endTime : 2018-03-09 08:00:00.0
         * id : 27
         * time : 2018-03-09 08:00:00.0
         * studentNum : 20154233005
         * outTime : 2018-03-10 14:15:51.0
         */

        private String activityDes;
        private String inTime;
        private String activityName;
        private int rule;
        private String location;
        private String endTime;
        private int id;
        private String time;
        private String studentNum;
        private String outTime;

        public String getActivityDes() {
            return activityDes;
        }

        public void setActivityDes(String activityDes) {
            this.activityDes = activityDes;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
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

        public String getOutTime() {
            return outTime;
        }

        public void setOutTime(String outTime) {
            this.outTime = outTime;
        }
    }
}
