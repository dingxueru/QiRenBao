package com.xiumi.qirenbao.home.bean;



/**
 * Created by qianbailu on 2017/3/7.
 */

public class ChoseTeamBean {
    /**
     "id": 1,
     "user_id": 3,
     "name": "多大的的团队",
     "description": "多大的的团队",
     "service_ids": "[15]",
     "vr_words": "[创客空间]",
     "qr_code": "",
     "created_at": "2017-03-01 09:53:47",
     "updated_at": "2017-03-01 09:53:47",

     */
    public String id;
    public String user_id;
    public String name;
    public String description;
    public String service_ids;
    public String vr_words;
    public String qr_code;
    public String created_at;
    public String updated_at;

    public   class UserBean {
        /*
                   "id": 3,
                    "name": "多大的",
                    "login_type": 3,
                    "reg_type": 2,
                    "mobile": "13151068634",
                    "email": "13151068634@163.com",
                    "password": "$2y$10$YKK/3xCtYNwfLi0pDx4Z0.PCML.kKBvaVBWLL87lv48czwAlfXdXm",
                    "last_login_at": "2017-03-01 09:53:47",
                    "created_at": "2017-03-01 09:53:47",
                    "updated_at": "2017-03-07 09:40:53",
         */
        public String id;
        public String name;
        public String login_type;
        public String reg_type;
        public String mobile;
        public String email;
        public String password;
        public String last_login_at;
        public String created_at;
        public String updated_at;
        public UserInfoBean user_info;
        public  class UserInfoBean {
            /**
             "id": 2,
             "user_id": 3,
             "role": 3,
             "level": 1,
             "growth_value": 0,
             "user_lv": 1,
             "sign_cities": null,
             "belongs_city": 1,
             "avatar": "avatar/fsaa.jpg",
             "sex": 1,
             "work_years": "12",
             "work_title": "多大的",
             "work_duty": "等等",
             "company": "想的",
             "description": "小学生",
             "class_id": 1,
             "child_class_id": 22,
             "created_at": "2017-03-01 10:30:24",
             "updated_at": "2017-03-06 13:51:36",
             "recommend": 0
             */
            public String id;
            public String user_id;
            public String role;
            public String level;
            public String growth_value;
            public int user_lv;
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
    public UserBean user;
}
