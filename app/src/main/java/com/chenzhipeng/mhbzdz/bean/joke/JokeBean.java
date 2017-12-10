package com.chenzhipeng.mhbzdz.bean.joke;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chenzhipeng.mhbzdz.utils.DisplayUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class JokeBean implements Serializable {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_LARGE_PICTURE = 3;
    public static final int TYPE_GIF_PICTURE = 4;
    public static final int TYPE_LIST_PICTURE = 5;
    public static final int TYPE_NORMAL_PICTURE = 6;
    public static final int TYPE_REFRESH = 7;
    private String message;

    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable {
        private String tip;

        @SerializedName("data")
        private List<Data.Dates> dates;

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public List<Data.Dates> getDates() {
            return dates;
        }

        public void setDates(List<Data.Dates> dates) {
            this.dates = dates;
        }

        public static class Dates implements MultiItemEntity, Serializable {
            private boolean isShowRefresh;

            public boolean isShowRefresh() {
                return isShowRefresh;
            }

            public void setShowRefresh(boolean showRefresh) {
                isShowRefresh = showRefresh;
            }

            private String type;

            private Data.Dates.Group group;

            @SerializedName("comments")
            private List<Comment> comments;

            public List<Comment> getComments() {
                return comments;
            }

            public void setComments(List<Comment> comments) {
                this.comments = comments;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Data.Dates.Group getGroup() {
                return group;
            }

            public void setGroup(Data.Dates.Group group) {
                this.group = group;
            }

            @Override
            public int getItemType() {
                if (isShowRefresh) {
                    return TYPE_REFRESH;
                }

                if (getGroup().getVideo() != null) {
                    return TYPE_VIDEO;
                }
                if (getGroup().getLargeImageLists() != null) {
                    return TYPE_LIST_PICTURE;
                }
                Group.LargeImage largeImage = getGroup().getLargeImage();
                if (largeImage != null) {
                    //判断是否gif图
                    if (!largeImage.getUrlLists().get(0).getUrl().contains("webp")) {
                        return TYPE_GIF_PICTURE;
                    } else {
                        //判断是否长图
                        String height = largeImage.getHeight();
                        if (DisplayUtils.getScreenHeight() <= Integer.parseInt(height)) {
                            return TYPE_LARGE_PICTURE;
                        }
                        return TYPE_NORMAL_PICTURE;
                    }
                }
                return TYPE_TEXT;
            }

            public class Comment implements Serializable {
                private String text;
                @SerializedName("user_name")
                private String userName;
                @SerializedName("avatar_url")
                private String avatarUrl;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
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
            }

            public class Group implements Serializable {
                private String text;
                @SerializedName("comment_count")
                private String commentCount;

                @SerializedName("bury_count")
                private String buryCount;

                @SerializedName("group_id")
                private String groupId;

                @SerializedName("digg_count")
                private String diggCount;

                private Data.Dates.Group.User user;
                @SerializedName("large_cover")
                private Data.Dates.Group.LargeCover largeCover;

                @SerializedName("thumb_image_list")
                private List<Data.Dates.Group.ThumbImageList> thumbImageLists;

                @SerializedName("large_image_list")
                private List<Data.Dates.Group.LargeImageList> largeImageLists;
                @SerializedName("large_image")
                private Data.Dates.Group.LargeImage largeImage;
                @SerializedName("middle_image")
                private Data.Dates.Group.MiddleImage middleImage;
                @SerializedName("360p_video")
                private Video video;

                public Video getVideo() {
                    return video;
                }

                public void setVideo(Video video) {
                    this.video = video;
                }

                public Data.Dates.Group.LargeImage getLargeImage() {
                    return largeImage;
                }

                public void setLargeImage(Data.Dates.Group.LargeImage largeImage) {
                    this.largeImage = largeImage;
                }

                public Data.Dates.Group.MiddleImage getMiddleImage() {
                    return middleImage;
                }

                public void setMiddleImage(Data.Dates.Group.MiddleImage middleImage) {
                    this.middleImage = middleImage;
                }

                public class LargeImage implements Serializable {
                    @SerializedName("r_height")
                    private String height;
                    @SerializedName("r_width")
                    private String width;
                    @SerializedName("url_list")
                    private List<Data.Dates.Group.LargeImage.UrlList> urlLists;

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public List<Data.Dates.Group.LargeImage.UrlList> getUrlLists() {
                        return urlLists;
                    }

                    public void setUrlLists(List<Data.Dates.Group.LargeImage.UrlList> urlLists) {
                        this.urlLists = urlLists;
                    }

                    public class UrlList implements Serializable {
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }

                public class MiddleImage implements Serializable {
                    @SerializedName("r_height")
                    private String height;
                    @SerializedName("r_width")
                    private String width;
                    @SerializedName("url_list")
                    private List<Data.Dates.Group.MiddleImage.UrlList> urlLists;

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public List<Data.Dates.Group.MiddleImage.UrlList> getUrlLists() {
                        return urlLists;
                    }

                    public void setUrlLists(List<Data.Dates.Group.MiddleImage.UrlList> urlLists) {
                        this.urlLists = urlLists;
                    }

                    public class UrlList implements Serializable {
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }


                public List<Data.Dates.Group.ThumbImageList> getThumbImageLists() {
                    return thumbImageLists;
                }

                public void setThumbImageLists(List<Data.Dates.Group.ThumbImageList> thumbImageLists) {
                    this.thumbImageLists = thumbImageLists;
                }

                public List<Data.Dates.Group.LargeImageList> getLargeImageLists() {
                    return largeImageLists;
                }

                public void setLargeImageLists(List<Data.Dates.Group.LargeImageList> largeImageLists) {
                    this.largeImageLists = largeImageLists;
                }

                public class ThumbImageList implements Serializable {
                    @SerializedName("url_list")
                    private List<Data.Dates.Group.ThumbImageList.UrlList> urlLists;

                    public List<Data.Dates.Group.ThumbImageList.UrlList> getUrlLists() {
                        return urlLists;
                    }

                    public void setUrlLists(List<Data.Dates.Group.ThumbImageList.UrlList> urlLists) {
                        this.urlLists = urlLists;
                    }

                    public class UrlList implements Serializable {
                        //多图图片预览图
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }

                public class LargeImageList implements Serializable {
                    @SerializedName("url_list")
                    private List<Data.Dates.Group.LargeImageList.UrlList> urlLists;

                    public List<Data.Dates.Group.LargeImageList.UrlList> getUrlLists() {
                        return urlLists;
                    }

                    public void setUrlLists(List<Data.Dates.Group.LargeImageList.UrlList> urlLists) {
                        this.urlLists = urlLists;
                    }

                    public class UrlList implements Serializable {
                        //多图高清图片
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }


                public Data.Dates.Group.LargeCover getLargeCover() {
                    return largeCover;
                }

                public void setLargeCover(Data.Dates.Group.LargeCover largeCover) {
                    this.largeCover = largeCover;
                }

                public class LargeCover implements Serializable {
                    @SerializedName("url_list")
                    private List<Data.Dates.Group.LargeCover.UrlList> urlLists;

                    public List<Data.Dates.Group.LargeCover.UrlList> getUrlLists() {
                        return urlLists;
                    }

                    public void setUrlLists(List<Data.Dates.Group.LargeCover.UrlList> urlLists) {
                        this.urlLists = urlLists;
                    }

                    public class UrlList implements Serializable {
                        //视频预览图
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }


                public class Video implements Serializable {
                    @SerializedName("url_list")
                    private List<Data.Dates.Group.Video.UrlList> urlLists;
                    private String height;
                    private String width;

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public List<Data.Dates.Group.Video.UrlList> getUrlLists() {
                        return urlLists;
                    }

                    public void setUrlLists(List<Data.Dates.Group.Video.UrlList> urlLists) {
                        this.urlLists = urlLists;
                    }

                    public class UrlList implements Serializable {
                        //视播放地址
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }


                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getCommentCount() {
                    return commentCount;
                }

                public void setCommentCount(String commentCount) {
                    this.commentCount = commentCount;
                }

                public String getBuryCount() {
                    return buryCount;
                }

                public void setBuryCount(String buryCount) {
                    this.buryCount = buryCount;
                }

                public String getGroupId() {
                    return groupId;
                }

                public void setGroupId(String groupId) {
                    this.groupId = groupId;
                }

                public String getDiggCount() {
                    return diggCount;
                }

                public void setDiggCount(String diggCount) {
                    this.diggCount = diggCount;
                }

                public Data.Dates.Group.User getUser() {
                    return user;
                }

                public void setUser(Data.Dates.Group.User user) {
                    this.user = user;
                }

                public class User implements Serializable {
                    private String name;
                    @SerializedName("avatar_url")
                    private String avatarUrl;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getAvatarUrl() {
                        return avatarUrl;
                    }

                    public void setAvatarUrl(String avatarUrl) {
                        this.avatarUrl = avatarUrl;
                    }
                }
            }
        }
    }
}
