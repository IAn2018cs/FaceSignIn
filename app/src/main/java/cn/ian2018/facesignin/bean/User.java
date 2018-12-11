package cn.ian2018.facesignin.bean;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/11
 */
public class User {

    /**
     * data : {"InterestGroupCode":1,"Group":1,"Type":0,"NewImage":"201542330054.jpg","GradeCode":15,"Phone":"15511297542","ClassDescription":"软件","OldImage":"15130632150308.JPG","Name":"陈帅"}
     * sucessed : true
     */

    private DataBean data;
    private boolean sucessed;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSucessed() {
        return sucessed;
    }

    public void setSucessed(boolean sucessed) {
        this.sucessed = sucessed;
    }

    public static class DataBean {
        /**
         * InterestGroupCode : 1
         * Group : 1
         * Type : 0
         * NewImage : 201542330054.jpg
         * GradeCode : 15
         * Phone : 15511297542
         * ClassDescription : 软件
         * OldImage : 15130632150308.JPG
         * Name : 陈帅
         */

        private int InterestGroupCode;
        private int Group;
        private int Type;
        private String NewImage;
        private int GradeCode;
        private String Phone;
        private String ClassDescription;
        private String OldImage;
        private String Name;

        public int getInterestGroupCode() {
            return InterestGroupCode;
        }

        public void setInterestGroupCode(int InterestGroupCode) {
            this.InterestGroupCode = InterestGroupCode;
        }

        public int getGroup() {
            return Group;
        }

        public void setGroup(int Group) {
            this.Group = Group;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getNewImage() {
            return NewImage;
        }

        public void setNewImage(String NewImage) {
            this.NewImage = NewImage;
        }

        public int getGradeCode() {
            return GradeCode;
        }

        public void setGradeCode(int GradeCode) {
            this.GradeCode = GradeCode;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public String getClassDescription() {
            return ClassDescription;
        }

        public void setClassDescription(String ClassDescription) {
            this.ClassDescription = ClassDescription;
        }

        public String getOldImage() {
            return OldImage;
        }

        public void setOldImage(String OldImage) {
            this.OldImage = OldImage;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
