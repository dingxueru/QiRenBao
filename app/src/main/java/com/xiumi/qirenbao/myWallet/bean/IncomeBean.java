package com.xiumi.qirenbao.myWallet.bean;

/**
 * Created by qianbailu on 2017/3/18.
 * 钱包收支记录
 */

public class IncomeBean {
    /*
        "id": 2,
        "user_id": 3,
        "change_type": "REDUCE",
        "change_num": "1.00",
        "last_num": "9.00",
        "log_title": "钱包余额提现",
        "created_at": "2017-03-18 10:48:04",
        "updated_at": "2017-03-18 10:48:04",
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
