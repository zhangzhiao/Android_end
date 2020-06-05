package com.xbzn.Intercom.service_android;

import com.xbzn.Intercom.mvp.model.entity.UserData;

import java.util.List;

public class AllDataInfoBean {
    List<UserData> userDataList;
    List<WarnInfoBean> warnInfoBeanList;

    public List<WarnInfoBean> getWarnInfoBeanList() {
        return warnInfoBeanList;
    }

    public void setWarnInfoBeanList(List<WarnInfoBean> warnInfoBeanList) {
        this.warnInfoBeanList = warnInfoBeanList;
    }

    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

}
