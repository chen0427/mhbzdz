package com.chenzhipeng.mhbzdz.presenter.joke;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.joke.JokePictureActivity;
import com.chenzhipeng.mhbzdz.adapter.joke.JokePictureListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.download.PictureDownloader;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.joke.IJokePictureView;

import java.io.File;
import java.util.ArrayList;


public class JokePicturePresenter {
    private IJokePictureView iJokePictureView;
    private JokePictureActivity activity;
    private ArrayList<String> list;
    private int position;
    private PictureDownloader downloader;

    public JokePicturePresenter(JokePictureActivity activity) {
        this.iJokePictureView = activity;
        this.activity = activity;
        downloader = new PictureDownloader(activity);
    }

    public void initData() {
        list = (ArrayList<String>) SuperIntent.getInstance().get(SuperIntent.S16);
        position = (int) SuperIntent.getInstance().get(SuperIntent.S15);
        if (!EmptyUtils.isListsEmpty(list)) {
            JokePictureListAdapter adapter = new JokePictureListAdapter(R.layout.itemview_joke_picture, list);
            iJokePictureView.onAdapter(adapter, position);
            updateBottomBar(position);
        }
    }

    public void updateBottomBar(int position) {
        if (!EmptyUtils.isListsEmpty(list)) {
            this.position = position;
            String s = position + 1 + "/" + list.size();
            iJokePictureView.onUpdateBottomBar(s);
        }
    }

    public void download() {
        if (!EmptyUtils.isListsEmpty(list)) {
            String url = list.get(position);
            final File file = getFileName(url);
            if (file != null) {
                if (!file.exists()) {
                    downloader.setUrl(url)
                            .setFile(file)
                            .setListener(new PictureDownloader.Listener() {
                                @Override
                                public void onStart() {
                                    iJokePictureView.start();
                                }

                                @Override
                                public void onComplete() {
                                    iJokePictureView.complete();
                                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    iJokePictureView.error(e);
                                }
                            }).start();
                } else {
                    iJokePictureView.start();
                    iJokePictureView.complete();
                }
            }
        }
    }


    private File getFileName(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("webp")) {
                String name = url.replace("http://", "").replace(File.separator, "") + ".jpg";
                return new File(BaseApplication.JOKE_PATH, name);
            } else {
                String name = url.replace("http://", "").replace(File.separator, "") + ".gif";
                return new File(BaseApplication.JOKE_PATH, name);
            }
        }
        return null;
    }
}
