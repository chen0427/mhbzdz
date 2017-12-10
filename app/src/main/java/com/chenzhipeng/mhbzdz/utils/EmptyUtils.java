package com.chenzhipeng.mhbzdz.utils;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class EmptyUtils {
    /**
     * 判断String集合中是否有空
     *
     * @param str
     * @return
     */
    public static boolean isStringsEmpty(String... str) {
        for (String s : str) {
            if (TextUtils.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个list集合是否有空
     *
     * @param lists
     * @return
     */
    public static boolean isListsEmpty(List... lists) {
        for (List list : lists) {
            if (list == null || list.size() == 0) {
                return true;
            }
        }
        return false;
    }
}
