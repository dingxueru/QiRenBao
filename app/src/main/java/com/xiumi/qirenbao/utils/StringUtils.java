package com.xiumi.qirenbao.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qianbailu on 2017/2/20.
 */
public class StringUtils {
    public static String EMPTY_STRING = "";

    public static boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }

    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 比较两个字符串的大小，规则：1、null等于null；
     * 2、null小于非null；3、长度为0的字符串相等；
     * 4、长度为0的字符串小于非0的字符串；
     * 5、长度不为0的两个字符串，从第一个字符开始比较大小
     *
     *
     * @param left
     * @param right
     * @return
     */
    public static int compare(String left, String right) {
        // 默认相等
        int result = 0;
        if (left == null || right == null) {
            result = (left == null ? 0 : 1) - (right == null ? 0 : 1);
        } else if (left.length() == 0 || right.length() == 0) {
            result = (left.length() == 0 ? 0 : 1)
                    - (right.length() == 0 ? 0 : 1);
        } else {
            int leftLength = left.length();
            int rightLength = right.length();

            // 默认长度长的大
            result = leftLength - rightLength;
            // 取较小的长度
            int lenght = leftLength > rightLength ? rightLength : leftLength;

            // 长度相同，比较每一位
            char[] leftChars = left.toCharArray();
            char[] rightChars = right.toCharArray();

            for (int i = 0; i < lenght; i++) {
                if (leftChars[i] - rightChars[i] == 0) {
                } else {
                    result = leftChars[i] - rightChars[i];
                    break;
                }
            }
        }

        return result;
    }

    public static boolean equals(String a, String b) {
        if (a == b)
            return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串MD5加密
     * @param string
     * @return
     */
    public static String md5(String string){
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}

