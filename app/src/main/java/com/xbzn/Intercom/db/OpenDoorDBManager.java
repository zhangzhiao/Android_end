package com.xbzn.Intercom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.xbzn.Intercom.mvp.model.entity.DaoMaster;
import com.xbzn.Intercom.mvp.model.entity.DaoSession;
import com.xbzn.Intercom.mvp.model.entity.UserData;
import com.xbzn.Intercom.mvp.model.entity.UserDataDao;
import com.xbzn.Intercom.network.requst.OpenData;
import com.xbzn.Intercom.network.requst.OpenDataDao;

/**
 * Created by Enzo Cotter on 2020/6/5.
 */
public class OpenDoorDBManager {
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private OpenDataDao openDataDao;

    private static OpenDoorDBManager mDbController;

    /**
     * 获取单例
     */
    public static OpenDoorDBManager getInstance(Context context){
        if(mDbController == null){
            synchronized (OpenDoorDBManager.class){
                if(mDbController == null){
                    mDbController = new OpenDoorDBManager(context);
                }
            }
        }
        return mDbController;
    }
    /**
     * 初始化
     * @param context
     */
    public OpenDoorDBManager(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context,"open.db", null);
        mDaoMaster =new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        openDataDao = mDaoSession.getOpenDataDao();
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context,"open.db",null);
        }
        SQLiteDatabase db =mHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper =new DaoMaster.DevOpenHelper(context,"user.db",null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    /**
     * 会自动判定是插入还是替换
     * @param openData
     */
    public void insertOrReplace(OpenData openData){

        if(openDataDao.insertOrReplace(openData)!=0){
            Log.i("OpenDataData", "insertOrReplace: 开门记录存入到本地 信息"+openData.getVisit_time());
        }else {
            Log.e("OpenDataData", "insertOrReplace: 存入失败");
        }
    }

    /**
     * 根据开门时间来进行查询
     * @param time 开门时间
     * @return 实体类
     */
    public OpenData queryByTime(String time){
        return  openDataDao.queryBuilder().where(OpenDataDao.Properties.Visit_time.eq(time)).build().unique();
    }
    /**
     * 根据ID来进行查询
     * @param id
     * @return
     */
    public OpenData queryByID(String id){
        return  openDataDao.queryBuilder().where(OpenDataDao.Properties.Visit_time.eq(id)).build().unique();
    }
}
