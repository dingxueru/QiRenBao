package com.xiumi.qirenbao.order.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者 ：Created by DXR on 2017/3/14.
 * 合伙人订单列表（包含评价）返回参数
 */

public class PartnerOrderListBean{
    /**
     * "id": 1,
     * "name": null,
     * "team_id": 1,
     * "company_user_id": 2,
     * "partner_user_id": 3,
     * "service_user_id": 0,
     * "tel_service_id": null,
     * "total_fee": null,
     * "commission_fee": null,
     * "gift_for_executor_id": null,
     * "gift_for_executor_total": null,
     * "gift_for_tel_id": null,
     * "gift_for_tel_total": null,
     * "underway_desc": null,
     * "off_desc": null,
     * "status": 0,
     * "created_at": "2017-03-08 16:15:05",
     * "updated_at": "2017-03-08 16:15:05",
     * "order_comment": null
     */
    public int id;
    public String name;
    public int team_id;
    public int company_user_id;
    public int partner_user_id;
    public int service_user_id;
    public String tel_service_id;
    public String total_fee;
    public String commission_fee;
    public String gift_for_executor_id;
    public String gift_for_executor_total;
    public String gift_for_tel_id;
    public String gift_for_tel_total;
    public String underway_desc;
    public String off_desc;
    public int status;
    public String created_at;
    public String updated_at;
    public ArrayList<OrderLog> order_log;

    public class OrderLog{
        /**
         * "id": 110,
         * "role_type": 3,
         * "user_id": 3,
         * "to_user_id": 58,
         * "order_id": 160,
         * "type": "\u5408\u4f19\u4eba\u5206\u914d\u8ba2\u5355\u7ed9\u56e2\u5458",
         * "ip": "117.91.13.203",
         * "mac": "",
         * "created_at": "2017-03-30 09:37:56",
         * "updated_at": "2017-03-30 09:37:56",
         * "to_user": {
         * "id": 58,
         * "name": "\u5434",
         * "login_type": 3,
         * "reg_type": 1,
         * "mobile": "13912134052",
         * "last_login_at": "2017-03-22 16:11:14",
         * "created_at": "2017-03-22 16:11:14",
         * "updated_at": "2017-03-27 17:19:35"
         */
        public String id;
        public String role_type;
        public String user_id;
        public String to_user_id;
        public String order_id;
        public String type;
        public String ip;
        public String mac;
        public String created_at;
        public String updated_at;
        public ToUser to_user;
        public class ToUser {
            public String id;
            public String name;
            public String login_type;
            public String reg_type;
            public String mobile;
            public String last_login_at;
            public String created_at;
            public String updated_at;
        }
    }

    public OrderComment order_comment;
    public class OrderComment {

        public int id;
        public int order_id;
        public int company_user_id;
        public int service_user_id;
        public int tel_service_id;
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

    public ServiceUser service_user;

    public class ServiceUser {
        /**
         * "service_user": {
         * "id": 3,
         * "name": "钱白露",
         * "login_type": 3,
         * "reg_type": 2,
         * "mobile": "13151068634",
         * "last_login_at": "2017-03-01 09:53:47",
         * "created_at": "2017-03-01 09:53:47",
         * "updated_at": "2017-03-14 17:05:53"
         * },
         */
        public int id;
        public String name;
        public int login_type;
        public int reg_type;
        public String mobile;
        public String last_login_at;
        public String created_at;
        public String updated_at;
    }

    public TelServiceUser tel_service_user;

    /**
     * "tel_service_user": {
     * "id": 1,
     * "name": "test1_server",
     * "email": "395355364@qq.com",
     * "status": 1,
     * "created_at": "2017-03-06 11:40:38",
     * "updated_at": "2017-03-18 09:36:03"
     * },
     */
    public class TelServiceUser {
        public int id;
        public String name;
        public String email;
        public String status;
        public String created_at;
        public String updated_at;
    }

    public CompanyUser company_user;

    public class CompanyUser {

        /**
         * "id": 11,
         * "name": "\u6211\u770b\u770b",
         * "login_type": 1,
         * "reg_type": 1,
         * "mobile": "17306270554",
         * "last_login_at": "2017-03-10 10:27:56",
         * "created_at": "2017-03-10 10:27:56",
         * "updated_at": "2017-03-10 10:27:56",
         * "user_info": {
         * "id": 9,
         * "user_id": 11,
         * "role": 2,
         * "level": 1,
         * "growth_value": 0,
         * "user_lv": 1,
         * "sign_cities": null,
         * "belongs_city": null,
         * "avatar": "avatar\/279d06b6e002dcbc8d90d9718928e3e2.jpeg",
         * "sex": 2,
         * "work_years": "10",
         * "work_title": "\u5f8b\u5e08",
         * "work_duty": null,
         * "company": "\u53cb\u76df",
         * "description": "\u6263",
         * "class_id": 12,
         * "child_class_id": null,
         * "created_at": "2017-03-10 10:27:56",
         * "updated_at": "2017-03-14 17:35:33",
         * "recommend": 0,
         * "recommend_pic": null
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

    public PayOrder pay_order;

    // 待完善支付实体类
    public class PayOrder {
        public String id;
        public String user_id;
        public String order_id;
        public String type;
        public String pay_type;
        public String subject;
        public String body;
        public String total_fee;
        public String out_trade_no;
        public String c;
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

        /**
         * "id": 1,
         * "user_id": 3,
         * "order_id": 9,
         * "type": "commission",
         * "pay_type": "alipay",
         * "subject": "3123123",
         * "body": "asdasd",
         * "total_fee": "333.00",
         * "out_trade_no": "946914895550493",
         * "c": 0,
         * "ali_trade_no": null,
         * "ali_trade_status": null,
         * "ali_notify_id": null,
         * "ali_notify_time": null,
         * "ali_payment_type": null,
         * "ali_total_fee": null,
         * "ali_buyer_id": null,
         * "ali_buyer_email": null,
         * "ali_seller_id": null,
         * "ali_seller_email": null,
         * "ali_gmt_payment": null,
         * "wx_prepay_id": null,
         * "wx_transaction_id": null,
         * "wx_openid": null,
         * "wx_time_end": null,
         * "wx_result_code": null,
         * "created_at": "2017-03-15 13:17:29",
         * "updated_at": "2017-03-15 13:17:29"
         */
        public String wx_transaction_id;
        public String wx_openid;
        public String wx_time_end;
        public String wx_result_code;
        public String created_at;
        public String updated_at;
    }
}
