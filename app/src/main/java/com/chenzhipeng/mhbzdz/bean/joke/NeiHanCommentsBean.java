package com.chenzhipeng.mhbzdz.bean.joke;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NeiHanCommentsBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("top_comments")
        private List<TopComments> topCommentses;
        @SerializedName("recent_comments")
        private List<RecentComments> recentCommentses;

        public List<TopComments> getTopCommentses() {
            return topCommentses;
        }

        public void setTopCommentses(List<TopComments> topCommentses) {
            this.topCommentses = topCommentses;
        }

        public List<RecentComments> getRecentCommentses() {
            return recentCommentses;
        }

        public void setRecentCommentses(List<RecentComments> recentCommentses) {
            this.recentCommentses = recentCommentses;
        }

        public class TopComments {
            private String text;
            @SerializedName("avatar_url")
            private String avatarUrl;
            @SerializedName("user_name")
            private String userName;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

        public class RecentComments {
            private String text;
            @SerializedName("avatar_url")
            private String avatarUrl;
            @SerializedName("user_name")
            private String userName;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
