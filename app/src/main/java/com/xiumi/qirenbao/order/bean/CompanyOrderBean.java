package com.xiumi.qirenbao.order.bean;

/**
 * Created by qianbailu on 2017/3/15.
 */

public class CompanyOrderBean {
    /*
    "id": 6,
        "team_id": 1,
        "company_user_id": 3,
        "partner_user_id": 2,
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
        "created_at": "2017-03-11 10:34:17",
        "updated_at": "2017-03-11 10:34:17",
        "order_comment": null,
        "service_user": null,
        "tel_service_user": null,

     */
    public String id;
    public String name;
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
    public String off_desc;
    public String status;
    public String created_at;
    public String updated_at;
    public TelService tel_service_user;
    public CompanyUser company_user;
    public PartnerUser partner_user;
    public ServiceUser service_user;
    public OrderComment order_comment;
    public PayOrder pay_order;

    public class PartnerUser{
        /*
        "id": 2,
          "name": "test_user_001oo",
          "login_type": 1,
          "reg_type": 1,
          "mobile": "13276589912",
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
        public UserInfo user_info;
        public class UserInfo{
            /*
                "id": 3,
            "user_id": 2,
            "role": 2,
            "level": 1,
            "growth_value": 0,
            "user_lv": 1,
            "sign_cities": "1",
            "belongs_city": 1,
            "avatar": "avatar/44.jpg",
            "sex": 1,
            "work_years": "",
            "work_title": "",
            "work_duty": "",
            "company": "",
            "description": null,
            "class_id": 1,
            "child_class_id": null,
            "created_at": null,
            "updated_at": "2017-03-13 10:21:26",
            "recommend": 0,
            "recommend_pic": ""
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
        }
    }
    public class OrderComment {
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

    public class TelService{
        public String id;
        public String name;
        public String email;
        public String status;
        public String created_at;
        public String updated_at;
    }

    public class CompanyUser {
        /*
         "id": 3,
              "name": "钱白露",
              "login_type": 3,
              "reg_type": 2,
              "mobile": "13151068634",
              "last_login_at": "2017-03-01 09:53:47",
              "created_at": "2017-03-01 09:53:47",
              "updated_at": "2017-03-14 17:05:53"
         */
        public String id;
        public String name;
        public String login_type;
        public String reg_type;
        public String mobile;
        public String last_login_at;
        public String created_at;
        public String updated_at;
    }

    public class ServiceUser {
        /*
         "id": 3,
                    "name": "钱白露",
                    "login_type": 3,
                    "reg_type": 2,
                    "mobile": "13151068634",
                    "last_login_at": "2017-03-01 09:53:47",
                    "created_at": "2017-03-01 09:53:47",
                    "updated_at": "2017-03-14 17:05:53"
         */
        public String id;
        public String name;
        public String login_type;
        public String reg_type;
        public String mobile;
        public String last_login_at;
        public String created_at;
        public String updated_at;
        public UserInfo user_info;
        public class UserInfo{
            /*
               "user_info": {
                 "id": 2,
                 "user_id": 3,
                 "role": 3,
                 "level": 1,
                 "growth_value": 0,
                 "user_lv": 2,
                 "sign_cities": "1",
                 "belongs_city": 1,
                 "avatar": "avatar/fff.jpg",
                 "sex": 2,
                 "work_years": "11",
                 "work_title": "222",
                 "work_duty": "asd",
                 "company": "222",
                 "description": "2222",
                 "class_id": 1,
                 "child_class_id": 22,
                 "created_at": "2017-03-01 10:30:24",
                 "updated_at": "2017-03-14 17:05:53",
                 "recommend": 1,
                 "recommend_pic": "slider/77.jpg"
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
        }
    }

    public class PayOrder {
        /*
         "id": 1,
          "user_id": 3,
          "order_id": 9,
          "type": "commission",
          "pay_type": "alipay",
          "subject": "3123123",
          "body": "asdasd",
          "total_fee": "333.00",
          "out_trade_no": "946914895550493",
          "status": 0,
          "ali_trade_no": null,
          "ali_trade_status": null,
          "ali_notify_id": null,
          "ali_notify_time": null,
          "ali_payment_type": null,
          "ali_total_fee": null,
          "ali_buyer_id": null,
          "ali_buyer_email": null,
          "ali_seller_id": null,
          "ali_seller_email": null,
          "ali_gmt_payment": null,
          "wx_prepay_id": null,
          "wx_transaction_id": null,
          "wx_openid": null,
          "wx_time_end": null,
          "wx_result_code": null,
          "created_at": "2017-03-15 13:17:29",
          "updated_at": "2017-03-15 13:17:29"
         */
        public String id;
        public String user_id;
        public String order_id;
        public String type;
        public String pay_type;
        public String subject;
        public String body;
        public String total_fee;
        public String out_trade_no;
        public String status;
        public String ali_trade_no;
        public String ali_trade_status;
        public String ali_notify_id;
        public String ali_notify_time;
        public String ali_payment_type;
        public String ali_total_fee;
        public String ali_buyer_id;
        public String ali_buyer_email;
        public String ali_seller_id;
        public String ali_seller_email;
        public String ali_gmt_payment;
        public String wx_prepay_id;
        public String wx_transaction_id;
        public String wx_openid;
        public String wx_time_end;
        public String wx_result_code;
        public String created_at;
        public String updated_at;
    }
}
