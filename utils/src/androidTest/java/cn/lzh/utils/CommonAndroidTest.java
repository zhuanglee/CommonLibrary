package cn.lzh.utils;

import org.junit.Test;

import cn.lzh.utils.security.BinaryUtil;

/**
 * Created by lzh on 2017/9/18.<br/>
 */

public class CommonAndroidTest {

    @Test
    public void testBase64(){
        String text = "测试Base64加密";
        String str = BinaryUtil.toBase64String(text);
        System.out.println(str);
        System.out.println(new String(BinaryUtil.fromBase64String(str)));
    }

}
