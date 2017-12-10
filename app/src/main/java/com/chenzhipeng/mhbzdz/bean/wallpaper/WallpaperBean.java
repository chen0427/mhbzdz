package com.chenzhipeng.mhbzdz.bean.wallpaper;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class WallpaperBean implements Serializable {
    private String msg;
    private Res res;

    public Res getRes() {
        return res;
    }

    public void setRes(Res res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Res implements Serializable {
        @SerializedName("vertical")
        private List<Vertical> verticals;


        public List<Vertical> getVerticals() {
            return verticals;
        }

        public void setVerticals(List<Vertical> verticals) {
            this.verticals = verticals;
        }

        public static class Vertical implements Serializable {
            private String thumb;
            private String img;

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }

}
