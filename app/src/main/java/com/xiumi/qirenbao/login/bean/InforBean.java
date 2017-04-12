package com.xiumi.qirenbao.login.bean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/7.
 */

public class InforBean {
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
    public List<TeamBean> teamBean;
    public List<UserBean> userBean;
}
