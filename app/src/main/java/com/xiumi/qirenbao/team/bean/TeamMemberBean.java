package com.xiumi.qirenbao.team.bean;

/**
 * 作者 ：Created by DXR on 2017/3/7.
 * 团队成员列表返回信息
 */

public class TeamMemberBean {
    public int id;
    public int team_id;
    public int user_id;
    public String services;
    public int status;
    public int type;
    public String created_at;
    public String updated_at;
    public User user;
    public  boolean ischeck = false; // 判断是否点击

    public class User {
        public int id;
        public String name;
        public int login_type;
        public int reg_type;
        public String mobile;
        public String email;
        public String password;
        public String last_login_at;
        public String created_at;
        public String updated_at;
        public UserInfo user_info;

        public class UserInfo {
            public int id;
            public int user_id;
            public int role;
            public int level;
            public int growth_value;
            public int user_lv;
            public String sign_cities;
            public int belongs_city;
            public String avatar;
            public int sex;
            public String work_years;
            public String work_title;
            public String work_duty;
            public String company;
            public String description;
            public int class_id;
            public int child_class_id;
            public String created_at;
            public String updated_at;
            public int recommend;
        }
    }

}
