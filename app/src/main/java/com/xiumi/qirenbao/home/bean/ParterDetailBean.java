package com.xiumi.qirenbao.home.bean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/8.
 */

public class ParterDetailBean {
    /**
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
    public  TeamBean  team;
    public  class UserInfoBean {
            /**
             "id": 1,
             "user_id": 1,
             "role": 2,
             "level": 1,
             "growth_value": 0,
             "user_lv": 1,
             "sign_cities": null,
             "belongs_city": 1,
             "avatar": "avatar/11.jpg",
             "sex": 2,
             "work_years": "11",
             "work_title": "222",
             "work_duty": "asd",
             "company": "222",
             "description": "2222",
             "class_id": 1,
             "child_class_id": 22,
             "created_at": "2017-02-11 07:57:36",
             "updated_at": "2017-03-06 13:51:18",
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
    public class TeamBean{
        /*
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
        public List<OrderBean> order;
        public class OrderBean{
            /*
            "id": 1,
            "team_id": 1,
            "company_user_id": 2,
            "partner_user_id": 3,
            "service_user_id": 0,
            "tel_service_id": null,
            "total_fee": null,
            "commission_fee": null,
            "gift_for_executor_id": null,
            "gift_for_executor_total": null,
            "gift_for_tel_id": null,
            "gift_for_tel_total": null,
            "underway_desc": null,
            "off_desc": null,
            "status": 0,
            "created_at": "2017-03-08 16:15:05",
            "updated_at": "2017-03-08 16:15:05",
            "order_comment": null,
             */
            public String id;
            public String team_id;
            public String company_user_id;
            public String partner_user_id;
            public String service_user_id;
            public String tel_service_id;
            public String total_fee;
            public String commission_fee;
            public String gift_for_executor_id;
            public String gift_for_executor_total;
            public String gift_for_tel_id;
            public String gift_for_tel_total;
            public String underway_desc;
            public String status;
            public String created_at;
            public String off_desc;
            public String updated_at;
            public OrderComment order_comment;
            public companyUser company_user;
            public class companyUser{
                /*
                "id": 2,
              "name": "test_user_001oo",
              "login_type": 1,
              "reg_type": 1,
              "mobile": "13276589911",
              "email": "3953ww364@qq.com",
              "password": "$2y$10$ar.sYLwW0kHUoz9JA5/ZwebJuNg7jDz8H6d8DuNv4xMuJqNdhyhvu",
              "last_login_at": "2017-03-01 09:46:17",
              "created_at": "2017-03-01 09:46:17",
              "updated_at": "2017-03-07 09:32:46",
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
                public userInfoBean user_info;

                public class userInfoBean{
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
            public class OrderComment{
                /*
                 "id": 1,
                        "order_id": 1,
                        "company_user_id": 2,
                        "service_user_id": 3,
                        "tel_service_id": 4,
                        "c2t_labels": "",
                        "c2t_at": "0000-00-00 00:00:00",
                        "c2p_labels": "好好好",
                        "c2p_at": "0000-00-00 00:00:00",
                        "p2t_labels": "擦查查",
                        "p2t_at": "0000-00-00 00:00:00",
                        "p2c_labels": "",
                        "p2c_at": "0000-00-00 00:00:00",
                        "created_at": null,
                        "updated_at": null
                 */
                public String id;
                public String order_id;
                public String company_user_id;
                public String service_user_id;
                public String tel_service_id;
                public String c2t_labels;
                public String c2t_at;
                public String c2p_labels;
                public String c2p_at;
                public String p2t_labels;
                public String p2t_at;
                public String p2c_labels;
                public String p2c_at;
                public String created_at;
                public String updated_at;
            }
        }
    }

}
