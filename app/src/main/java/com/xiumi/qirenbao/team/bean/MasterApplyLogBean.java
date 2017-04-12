package com.xiumi.qirenbao.team.bean;

/**
 * 作者 ：Created by DXR on 2017/3/11.
 * 达人-申请列表返回值
 */

public class MasterApplyLogBean {
    public int id;
    public int team_id;
    public int user_id;
    public int apply_result;
    public String  created_at;
    public String  updated_at;
    public Team team;

    public class Team {
        public int id;
        public int user_id;
        public String name;
        public String description;
        public String service_ids;
        public String vr_words;
        public String qr_code;
        public String created_at;
        public String updated_at;
        public User user;
        public class User {
            /**
             *  "id": 3,
             "name": "钱白露",
             "login_type": 3,
             "reg_type": 2,
             "mobile": "13151068634",
             "last_login_at": "2017-03-01 09:53:47",
             "created_at": "2017-03-01 09:53:47",
             "updated_at": "2017-03-14 17:05:53"
             */
            public int id;
            public String name;
            public int login_type;
            public int reg_type;
            public String mobile;
            public String last_login_at;
            public String created_at;
            public String updated_at;
            public UserInfo user_info;
            public class UserInfo {
                public String id;
                public String user_id;
                public String role;
                public String level;
                public String growth_value;
                public String user_lv;
                public String sign_cities;
                public String belongs_city;
                public String avatar;
                public String sex;
                public String work_years;
                public String work_title;
                public String work_duty;
                public String company;
                public String description;
                public String class_id;
                public String child_class_id;
                public String created_at;
                public String updated_at;
                public String recommend;
                public String recommend_pic;
            }
        }
    }
}
