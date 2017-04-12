package com.xiumi.qirenbao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据格式验证工具类
 * Created by qianbailu on 2017/2/20.
 */
public class ValitateUtils {
    /**
     * 邮箱
     */
    public static final String EMAIL_FORMAT = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
    /**
     * 手机号
     */
    public static final String PHONE_FORMAT = "^[1][3578]\\d{9}";

    /**
     * 身份证
     */
    public static final String ID_FORMAT = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
    // public static final String USER_NAME="^([\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{0,9})$";//20160620 add by qianbailu
    public static final String USER_NAME="^([\\u4e00-\\u9fa5]+|[\\u4e00-\\u9fa5]+[a-zA-Z0-9]+|[a-zA-Z0-9]+|[\\x21-\\x7e]+)$";
    public static final String isHttp="^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";
    /**
     * 验证数据
     * @param data
     * @param rule
     * @return
     */
    public static boolean validate(String data, String rule){
        if(data == null || StringUtils.isEmpty(rule))
            return false;
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(data);
        return m.matches();
    }

    public static boolean isSame(String arg1, String arg2){
        if(StringUtils.isEmpty(arg1) || StringUtils.isEmpty(arg2))
            return false;
        return arg1.equals(arg2);
    }
}
