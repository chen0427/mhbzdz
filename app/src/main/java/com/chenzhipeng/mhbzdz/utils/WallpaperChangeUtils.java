package com.chenzhipeng.mhbzdz.utils;

import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyItemBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;

import java.util.ArrayList;
import java.util.List;


public class WallpaperChangeUtils {
    /**
     * 将数据转换成item
     *
     * @param bean
     * @return
     */
    public static List<WallpaperItemBean> getWallpaperItemBeen(WallpaperBean bean) {
        List<WallpaperItemBean> beanList = new ArrayList<>();
        if (bean != null) {
            List<WallpaperBean.Res.Vertical> verticals = bean.getRes().getVerticals();
            if (verticals != null && verticals.size() > 0) {
                for (WallpaperBean.Res.Vertical v : verticals) {
                    WallpaperItemBean itemBean = new WallpaperItemBean(v.getThumb(), v.getImg());
                    beanList.add(itemBean);
                }
            }
        }
        return beanList;
    }

    public static List<WallpaperClassifyItemBean> getWallpaperClassifyItemBeen(WallpaperClassifyBean bean) {
        List<WallpaperClassifyItemBean> beanList = new ArrayList<>();
        if (bean != null) {
            List<WallpaperClassifyBean.Res.Classify> classifies = bean.getRes().getClassifies();
            if (classifies != null && classifies.size() > 0) {
                for (WallpaperClassifyBean.Res.Classify c : classifies) {
                    WallpaperClassifyItemBean itemBean =
                            new WallpaperClassifyItemBean(c.getName(), c.getId(), c.getCover());
                    beanList.add(itemBean);
                }
            }
        }
        return beanList;
    }
}
