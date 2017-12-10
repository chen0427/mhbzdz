package com.chenzhipeng.mhbzdz.download;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import java.util.List;


/**
 * 漫画任务下载 整个book
 */
public class ComicBookDownloader {
    private List<ComicDownloadBean> comicDownloadBeanList;
    private Listener listener;


    public ComicBookDownloader setData(List<ComicDownloadBean> comicDownloadBeanList) {
        this.comicDownloadBeanList = comicDownloadBeanList;
        refreshListener();
        return this;
    }

    public ComicBookDownloader setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 刷新监听器 用于删除任务的时候刷新 不然不会进度不能更新
     */
    private void refreshListener() {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (int i = 0; i < comicDownloadBeanList.size(); i++) {
                ComicDownloadBean comicDownloadBean = comicDownloadBeanList.get(i);
                comicDownloadBean.setListener(new ComicBookDownloadListener(i));
            }
        }
    }

    public boolean isExistDownload() {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (ComicDownloadBean c : comicDownloadBeanList) {
                if (c.getState().equals(getStringId(R.string.running_download))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExistWaitDownload() {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (ComicDownloadBean c : comicDownloadBeanList) {
                if (c.getState().equals(getStringId(R.string.wait_download))) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isPause() {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (ComicDownloadBean c : comicDownloadBeanList) {
                if (c.getState().equals(getStringId(R.string.running_download))
                        || c.getState().equals(getStringId(R.string.wait_download))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setAllPause() {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (ComicDownloadBean c : comicDownloadBeanList) {
                if (c.getState().equals(getStringId(R.string.running_download))) {
                    c.pause();
                }
            }
        }
    }

    public void setAllStart() {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (ComicDownloadBean c : comicDownloadBeanList) {
                if (c.getState().equals(getStringId(R.string.pause_download))) {
                    c.setState(getStringId(R.string.wait_download));
                }
            }
            start();
        }
    }

    public void start() {
        download(0);
    }

    private void download(int group) {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            if (group < comicDownloadBeanList.size()) {
                ComicDownloadBean comicDownloadBean = comicDownloadBeanList.get(group);
                String state = comicDownloadBean.getState();
                if (getStringId(R.string.wait_download).equals(state)) {
                    //等待下载
                    if (!isExistDownload()) {
                        comicDownloadBean.start();
                    }
                } else if (getStringId(R.string.pause_download).equals(state)) {
                    //暂停下载
                    download(group + 1);
                } else if (getStringId(R.string.complete_download).equals(state)) {
                    //下载完成
                    download(group + 1);
                } else if (getStringId(R.string.running_download).equals(state)) {
                    //正在下载
                } else if (getStringId(R.string.fail_download).equals(state)) {
                    //下载错误
                    download(group + 1);
                }
            } else if (group == comicDownloadBeanList.size()) {
                if (isExistWaitDownload()) {
                    download(0);
                } else {
                    completeAllGroup();
                }
            }
        }
    }


    private class ComicBookDownloadListener implements ComicDownloadBean.Listener {
        private int group;

        ComicBookDownloadListener(int group) {
            this.group = group;
        }

        @Override
        public void onStateToPosition(String stateStr) {
            updateState(stateStr, group);
        }

        @Override
        public void onPageToPosition(String pageStr) {
            updatePage(pageStr, group);
        }

        @Override
        public void onProgressToPosition(int progress) {
            updateProgress(progress, group);
        }

        @Override
        public void onPause() {
            download(group + 1);
        }

        @Override
        public void onCompleteToAll() {
            download(group + 1);
            completeItem();
        }

        @Override
        public void onError(Throwable e) {
            download(group + 1);
        }

        @Override
        public void onDelete() {
            refreshListener();
            //因为删除了 后面的任务位置代替了删除的位置 所以不用+1
            download(group);
        }
    }

    private void updateState(String stateStr, int group) {
        if (listener != null) {
            listener.updateState(stateStr, group);
        }
    }

    private void updatePage(String pageStr, int group) {
        if (listener != null) {
            listener.updatePage(pageStr, group);
        }
    }

    private void updateProgress(int progress, int group) {
        if (listener != null) {
            listener.updateProgress(progress, group);
        }
    }

    private void completeAllGroup() {
        if (listener != null) {
            listener.onCompleteAllGroup();
        }
    }

    private void completeItem() {
        if (listener != null) {
            listener.onCompleteItem();
        }
    }


    private String getStringId(int id) {
        return BaseApplication.getContext().getResources().getString(id);
    }


    public interface Listener {
        void updateState(String stateStr, int group);

        void updateProgress(int progress, int group);

        void updatePage(String pageStr, int group);

        void onCompleteItem();

        void onCompleteAllGroup();
    }

}


