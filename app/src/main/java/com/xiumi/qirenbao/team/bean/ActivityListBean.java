package com.xiumi.qirenbao.team.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 ：Created by DXR on 2017/3/6.
 * 团队活动列表
 */

public class ActivityListBean {
    /**
     *  "id": 1,
     "team_id": 1,
     "title": "2222",
     "start_at": "2016-07-07 00:00:00",
     "address": "三生三世",
     "description": "吾问无为谓",
     "img_url": "",
     "status": 0,
     "created_at": "2017-03-04 16:22:36",
     "updated_at": "2017-03-04 16:22:36"
     */
    public String id;
    public String team_id;
    public String title;
    public String start_at;
    public String address;
    public String description;
    public String img_url;
    public String status;
    public String created_at;
    public String updated_at;
    public ArrayList<Team_Activity_Users> team_activity_users;

    public class Team_Activity_Users {
        public String id;
        public String team_activity_id;
        public String user_id;
        public String created_at;
        public String updated_at;
        public User user;

        public class User {
            public String id;
            public String name;
            public String login_type;
            public String reg_type;
            public String mobile;
            public String last_login_at;
            public String created_at;
            public String updated_at;
            public User_Info user_info;
            public class User_Info {
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
            }
        }
    }
}
