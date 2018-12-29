package cn.lzh.utils;

import org.junit.Test;

import cn.lzh.utils.security.ByteUtil;
import cn.lzh.utils.security.DES;
import cn.lzh.utils.security.MD5Util;

/**
 * Created by lzh on 2017/9/18.<br/>
 */

public class CommonTest {

    @Test
    public void testDES() {
        try {
            String text = "测试DES加密,哈哈哈哈哈哈";//30dd5dca323591339ec193ee5b0ab91d
            DES des = new DES();
            System.out.println("加密前的字符：" + text);
            System.out.println("加密后的字符：" + des.encrypt(text));
            String decrypt = des.decrypt(des.encrypt(text));
            System.out.println("解密后的字符：" + decrypt);
            assert text.equals(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMD5() {
        String text = "测试MD5加密";
        String md5 = MD5Util.getMd5(text);
        System.out.println("md5=" + md5);
        assert "7b78acb12c955bfe233469aa4bfa7fe0".equals(md5);
    }

    @Test
    public void testByteUtil() {
        String text = "1234567890ABCDEF";
        byte[] bytes = ByteUtil.hex2bytes(text);
        assert text.equals(ByteUtil.bytes2hex(bytes));
    }

}
