package com.xbzn.Intercom.serial.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author ZhaoFuXin
 * @Email：18276061387@163.com
 * @description:
 * @date :2020/3/21 18:13
 */
public class GPIOUtils {

    //将GPIO口设置为输出的时候,默认是输出，调用下面的方法即可变成默认输入
    public static boolean gpio_open0(String message) {
        return RootCommand(message);
    }

    //读GPIO
    public static String getGpioString(String path) {
        String defString = "0";// 默认值
        try {
            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(new FileReader(path));
            defString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defString;
    }

    //下面的是执行的方法
    private static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {

            process = Runtime.getRuntime().exec("/system/xbin/su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

}
