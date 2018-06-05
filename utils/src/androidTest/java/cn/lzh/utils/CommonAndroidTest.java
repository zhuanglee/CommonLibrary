package cn.lzh.utils;

import org.junit.Test;

import cn.lzh.utils.io.Base64Util;

/**
 * Created by lzh on 2017/9/18.<br/>
 */

public class CommonAndroidTest {

    @Test
    public void testBase64(){
        String text = "测试Base64加密";
        String str = Base64Util.encode(text);
        System.out.println(str);
        System.out.println(Base64Util.decode(str));
    }

}
