package net.snails.common.algorithm.consistenthash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author krisjin
 * @date 2015年1月21日
 */
public class MD5Hash {

    public String hash(String key) {
        String ret = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            ret = toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private String toHex(byte[] bytes) {

        // 将任意字节转换为十六进制的数字
        char[] hexDigit = "0123456789abcdef".toCharArray();
        // MD5的结果：128位的长整数，用字节表示就是16个字节，用十六进制表示的话，使用两个字符，所以表示成十六进制需要32个字符
        char[] str = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte b = bytes[i];
            // 逻辑右移4位，与0xf（00001111）相与，为高四位的值，然后再hexDigits数组中找到对应的16进制值
            str[k++] = hexDigit[b >>> 4 & 0xf];
            // 与0xf（00001111）相与，为低四位的值，然后再hexDigits数组中找到对应的16进制值
            str[k++] = hexDigit[b & 0xf];

        }
        return new String(str);
    }
}
