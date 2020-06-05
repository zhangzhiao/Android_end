package com.xbzn.Intercom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xbzn.Intercom.mvp.model.entity.BaseConfig;
import com.xbzn.Intercom.mvp.model.entity.DaoMaster;
import com.xbzn.Intercom.mvp.model.entity.DaoSession;
import com.xbzn.Intercom.mvp.model.entity.User;
import com.xbzn.Intercom.mvp.model.entity.UserData;
import com.xbzn.Intercom.mvp.model.entity.UserDataDao;

import java.util.List;

public class UserDBManager {
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
    private UserDataDao userDataDao;

    private static UserDBManager mDbController;

    /**
     * 获取单例
     */
    public static UserDBManager getInstance(Context context){
        if(mDbController == null){
            synchronized (UserDBManager.class){
                if(mDbController == null){
                    mDbController = new UserDBManager(context);
                }
            }
        }
        return mDbController;
    }
    /**
     * 初始化
     * @param context
     */
    public UserDBManager(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context,"user.db", null);
        mDaoMaster =new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        userDataDao = mDaoSession.getUserDataDao();
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context,"user.db",null);
        }
        SQLiteDatabase db =mHelper.getReadableDatabase();
        return db;
    }
    //通过住户id来进行查询

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
     * @param userData
     */
    public void insertOrReplace(UserData userData){

        if(userDataDao.insertOrReplace(userData)!=0){
            Log.i("zza", "insertOrReplace: "+userData.getUser_name()+"card_id"+userData.getIc_card());
            userDataDao.insertOrReplace(userData);

        }
    }
    /**插入一条记录，表里面要没有与之相同的记录
     *
     * @param userData
     */
    public long insert(UserData userData){
        return  userDataDao.insert(userData);
    }

    /**
     * 更新数据
     * @param userData
     */
    public void update(UserData userData){
        UserData mOldPersonInfor = userDataDao.queryBuilder().where(UserDataDao.Properties.User_id.eq(userData.getUser_id())).build().unique();//拿到之前的记录
        if(mOldPersonInfor !=null){
            userDataDao.update(mOldPersonInfor);
        }
    }
    /**
     * 按条件查询数据
     */
    public UserData searchByWhere(String id){
        UserData userData  = userDataDao.queryBuilder().where(UserDataDao.Properties.User_id.eq(id)).build().unique();
        return userData;
    }

    /**
     * 查询所有数据
     */
    public List<UserData> searchAll(){
        List<UserData>personInfors=userDataDao.queryBuilder().list();

        return personInfors;
    }
    /**
     * 删除数据
     */
    public void delete(String wherecluse){
        userDataDao.queryBuilder().where(UserDataDao.Properties.User_id.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}