package com.xiumi.qirenbao.message.bean;

/**
 * 作者 ：Created by DXR on 2017/3/13.
 * 消息实体类
 */

public class MessageBean {
    /**
     * {
     "id": 4,
     "user_id": 4,
     "title": "测试4",
     "content": "测试内容",
     "is_read": 0,
     "created_at": "2017-03-13 09:37:10",
     "updated_at": "2017-03-13 09:37:14"
     },
     */
    public int id;
    public int user_id;
    public String title;
    public String content;
    public int is_read;
    public String created_at;
    public String updated_at;
}
