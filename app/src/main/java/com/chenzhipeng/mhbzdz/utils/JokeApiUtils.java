package com.chenzhipeng.mhbzdz.utils;

/**
 * Created by Administrator on 2017/7/16.
 */

public class JokeApiUtils {
    public static final String BASE_URL = "http://lf.snssdk.com/neihan/stream/mix/v1/";
    /**
     * 精华
     */
    private static final String ESSENCE = "http://ic.snssdk.com/neihan/in_app/essence_list/";
    /**
     * 推荐
     */
    public static final int RECOMMEND = -101;
    /**
     * 视频
     */
    public static final int VIDEO = -104;
    /**
     * 图片
     */
    public static final int PICTURE = -103;
    /**
     * 段子
     */
    public static final int TEXT = -102;

    private static final String HOST = "http://lf.snssdk.com";


    public static String getComments(String groupId, int offset) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HOST);
        stringBuilder.append("/neihan/comments/?group_id=");
        stringBuilder.append(groupId);
        stringBuilder.append("&item_id=");
        stringBuilder.append(groupId);
        stringBuilder.append("&count=20&offset=");
        stringBuilder.append(offset);
        stringBuilder.append("&iid=12318438630&device_id=13366604115&ac=wifi&channel" +
                "=download&aid=7&app_name=joke_essay&version_code=");
        //version_code
        stringBuilder.append("647");
        stringBuilder.append("&version_name=");
        //version_name
        stringBuilder.append("6.4.7");
        stringBuilder.append("&device_platform=android&ssmix=a&device_type=");
        //手机型号
        stringBuilder.append("D6503");
        stringBuilder.append("&device_brand=");
        //手机牌子
        stringBuilder.append("Sony");
        stringBuilder.append("&os_api=");
        //手机系统版本api
        stringBuilder.append("22");
        stringBuilder.append("&os_version=");
        //手机系统版本
        stringBuilder.append("5.1.1");
        stringBuilder.append("&uuid=352787060712458&openudid=3a13ac7bbe83d307&manifest_version_code=");
        //manifest_version_code
        stringBuilder.append("647");
        stringBuilder.append("&resolution=");
        //分辨率
        stringBuilder.append("1080*1800");
        stringBuilder.append("&dpi=");
        //dpi
        stringBuilder.append("480");
        stringBuilder.append("&update_version_code=");
        //update_version_code
        stringBuilder.append("6471");
        return stringBuilder.toString();
    }


    public static String getApi(int type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?mpic=1&webp=1&essence=1&video_cdn_first=1&fetch_activity=1&");
        stringBuilder.append("content_type=");
        //获取什么类型的内容
        stringBuilder.append(type);
        stringBuilder.append("&message_cursor=-1&am_longitude=");
        //经度
        stringBuilder.append("113.298123");
        stringBuilder.append("&am_latitude=");
        //纬度
        stringBuilder.append("23.285515");
        stringBuilder.append("&am_city=%E5%B9%BF%E5%B7%9E%E5%B8%82&am_loc_time=");
        //loc_time
        stringBuilder.append("1500217594474");
        stringBuilder.append("&count=");
        //count
        stringBuilder.append("30");
        stringBuilder.append("&min_time=");
        //min_time
        long min_time = System.currentTimeMillis();
        //stringBuilder.append("1500217704");
        stringBuilder.append(min_time);
        stringBuilder.append("&screen_width=");
        //屏幕宽
        stringBuilder.append("1080");
        stringBuilder.append("&double_col_mode=0&local_request_tag=");
        //local_request_tag
        stringBuilder.append("1500271541385");
        stringBuilder.append("&iid=12318438630&device_id=13366604115&ac=wifi&channel" +
                "=download&aid=7&app_name=joke_essay&version_code=");
        //version_code
        stringBuilder.append("647");
        stringBuilder.append("&version_name=");
        //version_name
        stringBuilder.append("6.4.7");
        stringBuilder.append("&device_platform=android&ssmix=a&device_type=");
        //手机型号
        stringBuilder.append("D6503");
        stringBuilder.append("&device_brand=");
        //手机牌子
        stringBuilder.append("Sony");
        stringBuilder.append("&os_api=");
        //手机系统版本api
        stringBuilder.append("22");
        stringBuilder.append("&os_version=");
        //手机系统版本
        stringBuilder.append("5.1.1");
        stringBuilder.append("&uuid=352787060712458&openudid=3a13ac7bbe83d307&manifest_version_code=");
        //manifest_version_code
        stringBuilder.append("647");
        stringBuilder.append("&resolution=");
        //分辨率
        stringBuilder.append("1080*1800");
        stringBuilder.append("&dpi=");
        //dpi
        stringBuilder.append("480");
        stringBuilder.append("&update_version_code=");
        //update_version_code
        stringBuilder.append("6471");
        return BASE_URL + stringBuilder.toString();
    }
}
