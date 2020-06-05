package com.xbzn.Intercom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.xbzn.Intercom.db.confi.AccountSchema;


public class ImOpenHelper extends SQLiteOpenHelper {



	/**用户数据表
	 * account_id       居民id
	 * account_name    居民姓名
	 * building_name   楼栋名称
	 * room_id         房屋id
	 * room_no  	   房屋名称
	 * face_img       人脸图片base64编码
	 * face_img_path  人脸网络地址
	 * cache_status   缓存状态
	 * created_time   创建时间
	 * changed_time   修改时间
	 * remark         备注
	 * */

    public static final String ACCOUNT = "create table if not exists " + AccountSchema.AccountTable.NAME + "("

            + "_id integer primary key autoincrement, " +
            AccountSchema.AccountTable.Cols.account_id + " text UNIQUE ," +
            AccountSchema.AccountTable.Cols.account_name + " default ''," +
            AccountSchema.AccountTable.Cols.building_name + " text default ''," +
            AccountSchema.AccountTable.Cols.room_no + " text default ''," +
			AccountSchema.AccountTable.Cols.room_id + " text default ''," +
            AccountSchema.AccountTable.Cols.face_img + " text default ''," +
            AccountSchema.AccountTable.Cols.face_img_path + " text default ''," +
            AccountSchema.AccountTable.Cols.cache_status + " text default ''," +
            AccountSchema.AccountTable.Cols.created_time + " timestamp default CURRENT_TIMESTAMP," +
            AccountSchema.AccountTable.Cols.changed_time + " timestamp default CURRENT_TIMESTAMP," +
            AccountSchema.AccountTable.Cols.remark + " text default ''" +
            ")";

	/**设备负责人员信息表
     * employee_id 管理人员id
	 * employee_name 管理人员name
	 * employee_phone  管理人员电话
	 * employee_type   管理人员类型
	 * face_img        人脸土片base64
	 * face_img_path   人脸图片网络地址
	 * cache_status   缓存状态
     * created_time  创建时间
	 * changed_time  修改时间
	 * remark        备注
	* */
//    public static final String EMPLOYEE = "create table if not exists "+ EmployeeSchema.EmployeeTable.NAME+"("
//			+ "_id integer primary key autoincrement, " +
//			EmployeeSchema.EmployeeTable.Cols.employee_id  + " text ," +
//			EmployeeSchema.EmployeeTable.Cols.employee_name + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.employee_phone + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.employee_type + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.face_img + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.account_id + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.face_img_path + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.cache_status + " text default ''," +
//			EmployeeSchema.EmployeeTable.Cols.created_time + " timestamp default CURRENT_TIMESTAMP," +
//			EmployeeSchema.EmployeeTable.Cols.changed_time + " timestamp default CURRENT_TIMESTAMP," +
//			EmployeeSchema.EmployeeTable.Cols.remark + " text default ''" +
//			")";


	/**垃圾桶数据表
	 * dustbin_id     垃圾桶id
	 * dustbin_name   垃圾桶名称
	 * dustbin_type   垃圾桶类型
	 * dustbin_on     垃圾桶开关状态
	 * created_time  创建时间
	 * changed_time  修改时间
	 * remark        备注
	 * */
//    public static final String DUSTBIN = "create table if not exists "+ DustbinSchema.DustbinTable.NAME +"("
//			+ "_id integer primary key autoincrement, " +
//			DustbinSchema.DustbinTable.Cols.dustbin_id + " text UNIQUE," +
//			DustbinSchema.DustbinTable.Cols.dustbin_name + " text," +
//			DustbinSchema.DustbinTable.Cols.dustbin_type + " text," +
//			DustbinSchema.DustbinTable.Cols.dustbin_on + " text," +
//			DustbinSchema.DustbinTable.Cols.app_id + " text default ''," +
//			DustbinSchema.DustbinTable.Cols.created_time + " timestamp default CURRENT_TIMESTAMP," +
//			DustbinSchema.DustbinTable.Cols.changed_time + " timestamp default CURRENT_TIMESTAMP," +
//			DustbinSchema.DustbinTable.Cols.remark + " text default ''" +
//			")";

	/**投递记录表
	 * account_id    用户id
	 * room_id       房间id
	 * uptown_id     小区id
	 * device_id     设备id
	 * weight        垃圾重量 g
	 * is_youke      是否是游客投递
	 * delivery_type 开门类型 0刷脸开门 1蓝牙卡开门 2二维码开门
	 * created_time  创建时间
	 * changed_time  改变时间
	 * remark        备注
	 * */
//    public static final String DELIVERY_RECORD = "create table if not exists "+ DeliveryRecordSchema.DeliveryRecordTable.NAME +"("
//			+ "_id integer primary key autoincrement, " +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.account_id + " text UNIQUE," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.room_id + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.uptown_id + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.is_youke + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.device_id + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.dustbin_id + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.weight + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.delivery_type + " text default ''," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.created_time + " timestamp default CURRENT_TIMESTAMP," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.changed_time + " timestamp default CURRENT_TIMESTAMP," +
//			DeliveryRecordSchema.DeliveryRecordTable.Cols.remark + " text default ''" +
//			")";


	/**设备预警类型表
	 * dustbin_warn_id     预警id
	 * dustbin_warn_name   预警名称
	 * dustbin_warn_type   预警类型
	 * dustbin_warn_value   预警阀值
	 * created_time  创建时间
	 * changed_time  修改时间
	 * remark        备注
	 * */
//    public static final String DUSTBIN_WARN= "create table if not exists "+DustbinSchema.DustbinWarnTable.NAME+"("
//			+ "_id integer primary key autoincrement, " +
//			DustbinSchema.DustbinWarnTable.Cols.dustbin_warn_id + " text UNIQUE," +
//			DustbinSchema.DustbinWarnTable.Cols.dustbin_warn_name + " text default ''," +
//			DustbinSchema.DustbinWarnTable.Cols.dustbin_warn_type + " text default ''," +
//			DustbinSchema.DustbinWarnTable.Cols.dustbin_warn_value + " text default ''," +
//			DustbinSchema.DustbinWarnTable.Cols.dustbin_warn_value_2 + " text default ''," +
//			DustbinSchema.DustbinWarnTable.Cols.created_time + " timestamp default CURRENT_TIMESTAMP," +
//			DustbinSchema.DustbinWarnTable.Cols.changed_time + " timestamp default CURRENT_TIMESTAMP," +
//			DustbinSchema.DustbinWarnTable.Cols.remark + " text default ''" +
//			")";


    public ImOpenHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //正事数据表
        db.execSQL(ACCOUNT);
//        db.execSQL(EMPLOYEE);
//        db.execSQL(DUSTBIN);
//        db.execSQL(DELIVERY_RECORD);
//        db.execSQL(DUSTBIN_WARN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}
