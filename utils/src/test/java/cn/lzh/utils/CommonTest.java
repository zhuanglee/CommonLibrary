package cn.lzh.utils;

import org.junit.Test;

import cn.lzh.utils.io.ByteUtil;
import cn.lzh.utils.io.BytesTransUtil;
import cn.lzh.utils.io.DES;
import cn.lzh.utils.io.MD5Util;

/**
 * Created by lzh on 2017/9/18.<br/>
 */

public class CommonTest {
    @Test
    public void testDES(){
        try {
//            String test = "测试DES加密";//30dd5dca323591339ec193ee5b0ab91d
            String test = "测试DES加密,哈哈哈哈哈哈";//30dd5dca323591339ec193ee5b0ab91d
            // 注意这里，自定义的加密的KEY要和解密的KEY一致，这就是钥匙，如果你上锁了，却忘了钥匙，那么是解密不了的
            DES des = DES.getInstance();
            System.out.println("加密前的字符：" + test);
            System.out.println("加密后的字符：" + des.encrypt(test));
            System.out.println("解密后的字符：" + des.decrypt(des.encrypt(test)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMD5(){
        String text = "测试MD5加密"; // 7b78acb12c955bfe233469aa4bfa7fe0
        System.out.println(MD5Util.digest(text));
    }

    @Test
    public void testByteTransUtil(){
        String test = "Test";
        byte[] bytes = test.getBytes();
        assert test.equals(new String(BytesTransUtil.Shorts2Bytes(BytesTransUtil.Bytes2Shorts(bytes))));
    }

    @Test
    public void testByteUtil(){
        String test = "Test";
        assert test.equals(ByteUtil.toHexString(ByteUtil.hex2byte(test)));
    }

    @Test
    public void testSerializeUtil(){
        String test = "Test";
        assert test.equals(ByteUtil.toHexString(ByteUtil.hex2byte(test)));
    }



}
