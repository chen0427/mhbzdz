package com.chenzhipeng.mhbzdz.bean.joke;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2017/8/12.
 */


public class NeiHanCommentsItemBean implements MultiItemEntity {
    private String text;
    private String avatarUrl;
    private String userName;
    public static final int TYPE_HOT = 1;
    public static final int TYPE_NEW = 2;
    public static final int TYPE_NORMAL = 3;
    private int type;

    public NeiHanCommentsItemBean(String text, String avatarUrl, String userName, int type) {
        this.text = text;
        this.avatarUrl = avatarUrl;
        this.userName = userName;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
