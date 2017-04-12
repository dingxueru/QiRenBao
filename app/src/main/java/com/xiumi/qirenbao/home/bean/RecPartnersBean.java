package com.xiumi.qirenbao.home.bean;

/**
 * Created by qianbailu on 2017/3/22.
 */

public class RecPartnersBean {
    /*
    "id": 1,
      "user_id": 1,
      "role": 2,
      "level": 1,
      "growth_value": 0,
      "user_lv": 2,
      "sign_cities": "1",
      "belongs_city": 1,
      "avatar": "avatar/11.jpg",
      "sex": 1,
      "work_years": "11",
      "work_title": "222",
      "work_duty": "asd",
      "company": "222",
      "description": "2222",
      "class_id": 1,
      "child_class_id": 22,
      "created_at": "2017-02-11 07:57:36",
      "updated_at": "2017-03-14 17:05:35",
      "recommend": 1,
      "recommend_pic": "slider/1461043293.jpg",
     */
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
    public User user;
    public class User{
        /*
          "id": 1,
        "name": "王峻",
        "login_type": 3,
        "reg_type": 1,
        "mobile": "18936485097",
        "last_login_at": null,
        "created_at": "2017-02-11 07:39:45",
        "updated_at": "2017-03-14 17:05:23",
         */
        public String id;
        public String name;
        public String login_type;
        public String reg_type;
        public String mobile;
        public String last_login_at;
        public String created_at;
        public String updated_at;
        public Team team;
        public class Team{
            /*
          "id": 5,
          "user_id": 1,
          "name": "王峻的团队",
          "description": "王峻的团队",
          "service_ids": "[1]",
          "vr_words": "[商标知识产权]",
          "qr_code": "1_6124",
          "created_at": "2017-03-14 17:03:05",
          "updated_at": "2017-03-14 17:03:05"
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
        }
    }
}

