package com.xbzn.Intercom.db.confi;

public class AccountSchema {
    public static final class AccountTable {
        public static final String NAME = "account";
        public static final class Cols {
            public static final String account_id = "account_id";//居民id
            public static final String account_name = "account_name";//居民姓名
            public static final String building_name = "building_name";//楼栋名称
            public static final String room_id = "room_id";//房屋id
            public static final String room_no = "room_no";//房屋名称
            public static final String face_img = "face_img";//人脸图片base64编码
            public static final String face_img_path = "face_img_path";//人脸图片地址
            public static final String cache_status="cache_status";//缓存状态
            public static final String created_time = "created_time";//创建时间
            public static final String changed_time = "changed_time";//修改时间
            public static final String remark = "remark";//修改时间
        }
    }


}
