package cn.ian2018.facesignin.bean;

import java.util.List;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class Saying {

    /**
     * data : [{"id":1,"content":"此心不动，随机而行"},{"id":2,"content":"坏人拍马屁是为了做坏事，好人拍马屁是为了干实事"},{"id":3,"content":"懂得道理是重要的，但实际运用也是重要的\u2014\u2014知行合一"},{"id":4,"content":"心如止水者，虽繁华纷扰之世间红尘，已然空无一物"},{"id":5,"content":"天地虽大，但有一念向善，心存良知，虽凡夫俗子，皆可为圣贤"},{"id":6,"content":"别人笑我太疯癫，我笑他人看不穿。"},{"id":7,"content":"大学之道，在重通与专"},{"id":8,"content":"时常省问父母，朔望恭谒圣贤"},{"id":9,"content":"这个世界是如此喧哗，让沉默的人显得有点傻"},{"id":10,"content":"勿以小恶弃人大美，勿以小怨忘人大恩"},{"id":11,"content":"不是因为某件事很难，你才不想做。而是因为你不想做，才让这件事变的困难"},{"id":12,"content":"人要常怀一颗感恩的心"},{"id":13,"content":"成功是一种态度、一种习惯，主动一些，远方就离你近一些"},{"id":14,"content":"如果完美的爱情无法如愿来临，那么至少可以把它变成最美好的暗恋"},{"id":15,"content":"成熟是一种明亮而不刺眼的光辉"},{"id":16,"content":"人生，需避得开\u201c惰\u201d\u201c骄\u201d，守得住\u201c勤\u201d\u201c敬\u201d"},{"id":17,"content":"上善若水，水善利万物而不争"},{"id":18,"content":"时光如禅，你如茶"},{"id":19,"content":"任时光流转，我心如初"},{"id":20,"content":"无事则安，有事则磨"},{"id":21,"content":"有你，一切那么美"},{"id":22,"content":"你若盛开，蝴蝶自来，你若精彩，天自安排"},{"id":23,"content":"进德修业在有恒"},{"id":24,"content":"善良的人总是快乐，感恩的人总是知足"},{"id":25,"content":"目中有人才有路，心中有爱才有度"},{"id":26,"content":"人能相遇，已是不易；心灵若相知，更要珍惜"},{"id":27,"content":"心是一块田，快乐自己种"},{"id":28,"content":"喜欢是一种心情，而爱是一种深情"},{"id":29,"content":"树欲静而风不止，子欲养而亲不待"},{"id":30,"content":"水到绝境是飞瀑，人到绝境是转机"},{"id":31,"content":"惟天下之大诚者，能立天下之大本"},{"id":32,"content":"少一些浮躁，多一份情怀"}]
     * sucessed : true
     */

    private boolean sucessed;
    private List<DataBean> data;

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
         * id : 1
         * content : 此心不动，随机而行
         */

        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
