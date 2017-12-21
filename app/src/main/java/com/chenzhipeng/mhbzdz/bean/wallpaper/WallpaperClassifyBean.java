package com.chenzhipeng.mhbzdz.bean.wallpaper;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class WallpaperClassifyBean implements Serializable {
    private String msg;
    @SerializedName("res")
    private Res res;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Res getRes() {
        return res;
    }

    public void setRes(Res res) {
        this.res = res;
    }

    public class Res implements Serializable {
        @SerializedName("category")
        private List<Classify> classifies;

        public List<Classify> getClassifies() {
            return classifies;
        }

        public void setClassifies(List<Classify> classifies) {
            this.classifies = classifies;
        }

        public class Classify implements Serializable {
            private String id;
            private String name;
            private String cover;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }
        }
    }
}
