package com.xiumi.qirenbao.officebuilding.bean;

/**
 * Created by qianbailu on 2017/2/28.
 */
public class BuildBean {
    /*
     "total": 0,
      "per_page": 10,
      "current_page": 1,
      "last_page": 0,
      "next_page_url": null,
      "prev_page_url": null,
      "from": null,
      "to": null,
      "data": []
     */
    public String total;
    public String per_page;
    public String current_page;
    public String last_page;
    public String next_page_url;
    public String prev_page_url;
    public String from;
    public String to;
    public DataBean data;
    public static class DataBean{
        /*
         "id": 1,
          "city_id": 1,
          "area_id": 2,
          "name": "绿地峰创国际大厦",
          "pic": "image/7dec8673aa955a0c627972acda99a1f6.jpeg",
          "square": 120,
          "price": 4,
          "address": " 邗江区 江阳西路与润扬南路交叉口",
          "gps_lat": "119",
          "gps_long": "23",
          "content": "5.4米极致挑高LOFT，一层可做两层,毛坯。\r\n​‌‌绿地峰创国际，项目紧邻横向主干道江阳路以及纵向干道润阳路，此外，宁通、沪宁、扬溧高速也是必经之地。作为西南商圈绿地运河纪城市综合体的一部分，尽享综合体所带来的丰盛配套资源。记者在峰创国际整层写字楼办公样板示范区看到，以不同形式的办公环境真实还原了企业总部、金融类、IT类、广告类企业空间。参观其中犹如走进了不同企业公司中，感受到了国际办公理念所给你带来的视觉震撼和国际OFFICE文化。整个办公示范区空间明亮开阔，视野充足，布局合理。集成吊顶处理、网格地板铺设成为未来交付客户的基本配置。这在扬州众多写字楼中还是第一次。",
          "slider_pic": "image/f680355dd03a6938a7d7d830744616a9.jpeg",
          "sort": 0,
          "is_slider": 1,
          "status": 1,
          "telephone": "159 9511 3751",
          "created_at": "2017-02-24 10:24:13",
          "updated_at": "2017-02-24 11:46:22"
         */
        public String id;
        public String city_id;
        public String area_id;
        public String name;
        public String pic;
        public String square;
        public String price;
        public String address;
        public String gps_lat;
        public String gps_long;
        public String content;
        public String slider_pic;
        public String sort;
        public String is_slider;
        public String status;
        public String telephone;
        public String created_at;
        public String updated_at;
    }

}
