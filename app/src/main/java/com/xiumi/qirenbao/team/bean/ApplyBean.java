package com.xiumi.qirenbao.team.bean;

/**
 * Created by qianbailu on 2017/3/10.
 */

public class ApplyBean {
    /**
     "id": 2,
     "team_id": 1,
     "user_id": 2,
     "apply_result": 1,
     "created_at": "2017-03-04 14:17:32",
     "updated_at": "2017-03-04 16:21:09",
     "comment": "丁雪茹，申请加入",
     */
    public String id;
    public String team_id;
    public String user_id;
    public String apply_result;
    public String created_at;
    public String updated_at;
    public String comment;
    public TeamBean team;
    public UserBean user;
    public class TeamBean{
        /*
         "id": 1,
          "user_id": 3,
          "name": "多大的的团队",
          "description": "多大的的团队",
          "service_ids": "[15][1][11]",
          "vr_words": "[创客空间][商标知识产权][税务咨询]",
          "qr_code": "",
          "created_at": "2017-03-01 09:53:47",
          "updated_at": "2017-03-10 08:52:49"
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
    public class UserBean{
        /*
         "id": 2,
          "name": "test_user_001oo",
          "login_type": 1,
          "reg_type": 1,
          "mobile": "13276589911",
          "last_login_at": "2017-03-01 09:46:17",
          "created_at": "2017-03-01 09:46:17",
          "updated_at": "2017-03-07 09:32:46",
         */
        public String id;
        public String name;
        public String login_type;
        public String reg_type;
        public String mobile;
        public String last_login_at;
        public String created_at;
        public String updated_at;
        public UserInfoBean user_info;
        public class UserInfoBean{
          /*
          "id": 3,
            "user_id": 2,
            "role": 2,
            "level": 1,
            "growth_value": 0,
            "user_lv": 1,
            "sign_cities": null,
            "belongs_city": 1,
            "avatar": "avatar/afd.jpg",
            "sex": 1,
            "work_years": "",
            "work_title": "",
            "work_duty": "",
            "company": "",
            "description": null,
            "class_id": 1,
            "child_class_id": null,
            "created_at": null,
            "updated_at": "2017-03-06 13:52:11",
            "recommend": 0
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
        }
    }
}
