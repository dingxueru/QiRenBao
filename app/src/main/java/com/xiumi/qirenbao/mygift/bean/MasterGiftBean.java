package com.xiumi.qirenbao.mygift.bean;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-送出的礼物
 */

public class MasterGiftBean {
    /**
     *  "id": 24,
     "user_id": 57,
     "to_user_id": 5,
     "gift_id": 1,
     "total": 1,
     "type": "SEND",
     "status": 1,
     "created_at": "2017-03-20 15:19:12",
     "updated_at": "2017-03-20 15:19:12",
     "role_type": "NOTTEL",
     "gift": {
     "id": 1,
     "name": "玫瑰花",
     "icon": null,
     "price": 1,
     "status": 1,
     "created_at": "2017-03-06 11:50:32",
     "updated_at": "2017-03-06 11:50:32"
     */
    public String id;
    public String user_id;
    public String to_user_id;
    public String gift_id;
    public String total;
    public String type;
    public String status;
    public String created_at;
    public String updated_at;
    public String role_type;
    public Gift gift;

    public class Gift {
        public String id;
        public String name;
        public String icon;
        public String price;
        public String status;
        public String created_at;
        public String updated_at;
    }
}
