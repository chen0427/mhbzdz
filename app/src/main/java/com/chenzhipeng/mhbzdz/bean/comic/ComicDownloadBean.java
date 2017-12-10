package com.chenzhipeng.mhbzdz.bean.comic;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.download.ComicChapterDownloader;

import java.io.Serializable;
import java.util.List;

public class ComicDownloadBean {
    private String comicId;
    private String comicName;
    private String chapterName;
    private String state;
    private String page;
    private int progress;
    private int maxProgress;
    private boolean isChecked;
    private boolean isShowChecked;
    private boolean isRead = false;
    private List<String> urls;

    private ComicChapterDownloader downloader;
    private Listener listener;

    public ComicDownloadBean(String comicId, String comicName, String chapterName, String state, String page, int progress, int maxProgress, boolean isChecked, boolean isShowChecked, boolean isRead, List<String> urls) {
        this.comicId = comicId;
        this.comicName = comicName;
        this.chapterName = chapterName;
        this.state = state;
        this.page = page;
        this.progress = progress;
        this.maxProgress = maxProgress;
        this.isChecked = isChecked;
        this.isShowChecked = isShowChecked;
        this.isRead = isRead;
        this.urls = urls;
        this.downloader = new ComicChapterDownloader(comicId, comicName, chapterName);
        this.downloader.setListener(new ComicDownloaderListener());
    }

    private String getStringId(int id) {
        return BaseApplication.getContext().getResources().getString(id);
    }

    public ComicDownloadBean setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    private void setStateToPosition(String stateStr) {
        if (listener != null) {
            listener.onStateToPosition(stateStr);
        }
    }

    private void setProgressToPosition(int progress) {
        if (listener != null) {
            listener.onProgressToPosition(progress);
        }
    }

    private void setPageToPosition(String pageStr) {
        if (listener != null) {
            listener.onPageToPosition(pageStr);
        }
    }

    private void setPause() {
        if (listener != null) {
            listener.onPause();
        }
    }

    private void setCompleteToAll() {
        if (listener != null) {
            listener.onCompleteToAll();
        }
    }

    private void setError(Throwable e) {
        if (listener != null) {
            listener.onError(e);
        }
    }

    private void setDelete() {
        if (listener != null) {
            listener.onDelete();
        }
    }

    public void pause() {
        downloader.pause();
    }

    public void delete() {
        downloader.delete();
    }

    public void start() {
        downloader.setUrls(urls).start(progress);
    }


    private class ComicDownloaderListener implements ComicChapterDownloader.Listener, Serializable {
        @Override
        public void onStart() {
            state = getStringId(R.string.running_download);
            setStateToPosition(state);
        }

        @Override
        public void onCompleteToItem(int position) {
            progress = position + 1;
            page = progress + "/" + maxProgress;
            setProgressToPosition(progress);
            setPageToPosition(page);
        }

        @Override
        public void onPause() {
            state = getStringId(R.string.pause_download);
            setStateToPosition(state);
            setPause();
        }

        @Override
        public void onCompleteToAll() {
            state = getStringId(R.string.complete_download);
            setStateToPosition(state);
            setCompleteToAll();
        }

        @Override
        public void onError(Throwable e) {
            state = getStringId(R.string.fail_download);
            setStateToPosition(state);
            setError(e);
        }

        @Override
        public void onDelete() {
            setDelete();
        }
    }


    public interface Listener {
        void onStateToPosition(String state);

        void onPageToPosition(String page);

        void onProgressToPosition(int progress);

        void onPause();

        void onCompleteToAll();

        void onError(Throwable e);

        void onDelete();
    }


    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isShowChecked() {
        return isShowChecked;
    }

    public void setShowChecked(boolean showChecked) {
        isShowChecked = showChecked;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isRead() {
        return isRead;
    }
}
