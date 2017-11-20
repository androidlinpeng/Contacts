package com.contacts;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */

public class UserInfo implements Serializable {

    public String username;
    public String phone;
    public String icon="";
    public String pinyin="";

    public String initialLetter = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getInitialLetter() {
        if(initialLetter == null){
            CommonUtil.setUserInitialLetter(this);
        }
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }
}
