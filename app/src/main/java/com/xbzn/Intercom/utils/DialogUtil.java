package com.xbzn.Intercom.utils;

import android.content.Context;
import android.content.DialogInterface;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

/**
 * Created by Enzo Cotter on 2020/6/5.
 */
public class DialogUtil {
    Context context;
//QMUI的帮助类 传入的时候切记 传activity 不要传入全局变量
    public DialogUtil(Context context) {
        this.context = context;
    }
    //基本的带有标题的提示框 点击按钮就退出 参数 三 是否能点击外面退出
    public void messageDialog(String title,String message,boolean isCanceled){
        new QMUIDialog.MessageDialogBuilder(context)
                     .setMessage(message)
                     .setCanceledOnTouchOutside(isCanceled)
                     .setTitle(title)
                     .addAction("确定", new QMUIDialogAction.ActionListener() {
                         @Override
                         public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                         }
                     }).show();
    }
    //带有列表的 和按钮的 dialog 也含有标题
    public void listDialog(String title, List<String> list, DialogInterface.OnClickListener onClickListener){
        final String[] items = list.toArray(new String[list.size()]);
        new QMUIDialog.MenuDialogBuilder(context)
                .setCanceledOnTouchOutside(false)
                .setTitle(title)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addItems(items,onClickListener)
                .show();
    }
    public LoadingDialog loadingDialog(String message){
        LoadingDialog loadingDialog;
        //加载弹窗
        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(context)
                .setMessage(message)
                .setCancelable(true)//返回键是否可点击
                .setCancelOutside(false);//窗体外是否可点击
        loadingDialog = loadBuilder.create();
        loadingDialog.show();//显示弹窗
        return loadingDialog;
    }
}
