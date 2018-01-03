package com.chenzhipeng.mhbzdz.activity.joke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.Toast;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.presenter.joke.JokePicturePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.view.joke.IJokePictureView;
import com.chenzhipeng.mhbzdz.widget.JokeViewPaper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class JokePictureActivity extends BaseActivity implements IJokePictureView, JokeViewPaper.Listener {
    private JokePicturePresenter presenter;
    private AlertDialog alertDialog;
    public static int readPosition = 0;
    public static List<String> stringList;
    @BindView(R.id.JokeViewPaper)
    JokeViewPaper jokeViewPaper;


    public static <T> void startActivity(Activity activity, T data, int currentPosition) {
        if (activity != null && data != null) {
            Intent intent = new Intent(activity, JokePictureActivity.class);
            if (data instanceof ArrayList) {
                stringList = (List<String>) data;
            } else if (data instanceof String) {
                stringList = new ArrayList<>();
                stringList.add(String.valueOf(data));
            }
            readPosition = currentPosition;
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }

    @Override
    public void finish() {
        stringList = null;
        readPosition = 0;
        super.finish();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public JokePicturePresenter getPresenter() {
        if (presenter == null) {
            presenter = new JokePicturePresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_joke_picture);
        ButterKnife.bind(this);
        getPresenter().initData();
    }

    @Override
    public void start() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
        alertDialog.setMessage(getString(R.string.picture_downloading));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void complete() {
        Toast.makeText(BaseApplication.getContext(), R.string.album_download, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }

    @Override
    public void error() {
        Toast.makeText(BaseApplication.getContext(), R.string.picture_download_fail, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }


    @Override
    public <T> void onData(T data, int position) {
        jokeViewPaper.setData((List<String>) data, position);
        jokeViewPaper.setActivity(this);
        jokeViewPaper.setListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ConfigUtils.getVolumePage()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    jokeViewPaper.volumeDown();
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    jokeViewPaper.volumeUp();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
