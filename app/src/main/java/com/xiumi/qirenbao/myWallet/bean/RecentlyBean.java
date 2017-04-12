package com.xiumi.qirenbao.myWallet.bean;

/**
 * Created by qianbailu on 2017/3/18.
 * 最近一次收入
 */

public class RecentlyBean {
    /*
    "id": 6,
      "user_id": 3,
      "change_type": "REDUCE",
      "change_num": "1.00",
      "last_num": "1005.00",
      "log_title": "钱包余额提现",
      "created_at": "2017-03-18 14:01:05",
      "updated_at": "2017-03-18 14:01:05",
      "type": "AtmWallet"
     */
    public String id;
    public String user_id;
    public String change_type;
    public String change_num;
    public String last_num;
    public String log_title;
    public String created_at;
    public String updated_at;
    public String type;
}
