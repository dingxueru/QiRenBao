package com.xiumi.qirenbao.officebuilding.bean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/1.
 */
public class BuildDetailBean {
    public String id;
    public String  city_id ;
    public String  area_id;
    public String  name;
    public String  pic;
    public String  square;
    public String  price;
    public String  address;
    public String gps_lat;
    public String  gps_long;
    public String content;
    public String  slider_pic;
    public String sort;
    public String  is_slider;
    public String status;
    public String  telephone;
    public String created_at;
    public String  updated_at;
    public  class BuildingImgs {
        /**
         "id": 1,
         "building_id": 1,
         "title": "全景图",
         "url": "image/峰创2.JPG",
         "created_at": "2017-02-24 17:30:46",
         "updated_at": "2017-02-24 17:30:46"
         */
        public String id;
        public String  building_id ;
        public String  title;
        public String  url;
        public String created_at;
        public String  updated_at;
    }
    public List<BuildingImgs> building_imgs;
    }
