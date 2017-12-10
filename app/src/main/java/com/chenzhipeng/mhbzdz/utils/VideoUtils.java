package com.chenzhipeng.mhbzdz.utils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * Created by Administrator on 2017/8/8.
 */

public class VideoUtils {
    /**
     * 视频暂停
     */
    public static void pause() {
        JCVideoPlayer jcVideoPlayer = JCVideoPlayerManager.getCurrentJcvd();
        if (jcVideoPlayer != null && jcVideoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
            jcVideoPlayer.startButton.performClick();
        }
    }

    /**
     * 视频销毁
     */
    public static void destroy() {
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 全屏后退回来
     *
     * @return
     */
    public static boolean backPress() {
        return JCVideoPlayer.backPress();
    }
}
