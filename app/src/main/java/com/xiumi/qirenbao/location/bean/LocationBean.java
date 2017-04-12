package com.xiumi.qirenbao.location.bean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/2.
 */
public class LocationBean {
    /*
            "id": 1,
            "name": "扬州",
            "sort": 0,
            "status": 1,
            "created_at": "2017-02-22 03:19:54",
            "updated_at": "2017-02-22 03:19:54",
     */
    public String id;
    public String  name ;
    public String  sort;
    public String  status;
    public String  created_at;
    public String  updated_at;
    public List<AreasBean> areas;
}
