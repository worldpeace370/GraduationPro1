package com.lebron.graduationpro1.minepage.model;

/**
 * Created by wuxiangkun on 2017/2/15.
 * Contact way wuxiangkun2015@163.com
 */

public class UserInfo {
    private String avatarUrl;
    private String nickName;
    private String mobile;

    public UserInfo() {
    }

    public UserInfo(String avatarUrl, String nickName, String mobile) {
        this.avatarUrl = avatarUrl;
        this.nickName = nickName;
        this.mobile = mobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
