package com.xbzn.Intercom.mvp.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AutoInstallModel  {
    /**
     * 静默安装
     * @param packageName　 　调用installSilent函数的应用包名
     * @param filePath 　　　　静默安装应用的apk路径
     * @return 0 安装成功
     * 　　　　　　　 1 文件不存在
     * 　　　　　　　 2 安装失败
     */
    public static int installSilent(String packageName,String filePath) {
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0 || file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return 1;
        }
        //pm install -i 包名 --user 0 apkpath
        String[] args = { "pm", "install","-i",packageName,"--user","0", "-r", filePath };
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
                Log.d("installSilent", "while successMsg s:" + s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
                Log.d("installSilent", "while errorMsg s:" + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = 2;
        }
        Log.d("installSilent", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }




}
