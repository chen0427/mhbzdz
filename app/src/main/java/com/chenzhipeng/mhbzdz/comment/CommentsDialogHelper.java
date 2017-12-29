package com.chenzhipeng.mhbzdz.comment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.joke.JokeCommentsAdapter;
import com.chenzhipeng.mhbzdz.bean.joke.NeiHanCommentsBean;
import com.chenzhipeng.mhbzdz.bean.joke.NeiHanCommentsItemBean;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.joke.JokeCommentsService;
import com.chenzhipeng.mhbzdz.utils.JokeApiUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 段子评论 dialog
 */
public class CommentsDialogHelper {
    private static volatile CommentsDialogHelper commentsDialogHelper;
    private int offset = 0;
    private JokeCommentsAdapter adapter;

    private CommentsDialogHelper() {

    }

    public static CommentsDialogHelper getCommentsDialogHelper() {
        if (commentsDialogHelper == null) {
            synchronized (CommentsDialogHelper.class) {
                if (commentsDialogHelper == null) {
                    commentsDialogHelper = new CommentsDialogHelper();
                }
            }
        }
        return commentsDialogHelper;
    }

    private List<Disposable> disposables = new ArrayList<>();

    private void initList(final RxAppCompatActivity context,
                          List<NeiHanCommentsItemBean> beanList,
                          final View view, final String groupId) {
        final RecyclerView recyclerView = view.findViewById(R.id.rv_wallpaperType);
        if (adapter == null) {
            adapter = new JokeCommentsAdapter(beanList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            offset = offset + 15;
                            getData(context, groupId, offset, view);
                        }
                    });
                }
            }, recyclerView);
        } else {
            adapter.addData(beanList);
        }

    }


    public void show(final RxAppCompatActivity activity, String groupId) {
        if (activity == null || TextUtils.isEmpty(groupId)) {
            return;
        }
        final View view = getView(activity);
        if (view != null) {
            showDialog(activity, view);
            getData(activity, groupId, offset, view);
        }
    }

    private void getData(final RxAppCompatActivity activity, final String groupId, int offset, final View view) {
        RetrofitHelper.getInstance()
                .create(JokeCommentsService.class)
                .get(JokeApiUtils.getComments(groupId, offset))
                .compose(activity.<NeiHanCommentsBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<NeiHanCommentsBean, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull NeiHanCommentsBean bean) throws Exception {
                        List<NeiHanCommentsItemBean> beanList = new ArrayList<>();
                        List<NeiHanCommentsBean.Data.TopComments> topCommences = bean.getData().getTopCommentses();
                        List<NeiHanCommentsBean.Data.RecentComments> recentCommences = bean.getData().getRecentCommentses();
                        if (topCommences != null && topCommences.size() > 0) {
                            if (adapter == null) {
                                //未下拉加载 所以标记是否热门 新鲜要显示出来
                                beanList.add(new NeiHanCommentsItemBean(null, null, null, NeiHanCommentsItemBean.TYPE_HOT));
                            }
                            for (NeiHanCommentsBean.Data.TopComments t : topCommences) {
                                NeiHanCommentsItemBean itemBean =
                                        new NeiHanCommentsItemBean(t.getText(),
                                                t.getAvatarUrl(),
                                                t.getUserName(),
                                                NeiHanCommentsItemBean.TYPE_NORMAL);
                                beanList.add(itemBean);
                            }
                        }
                        if (recentCommences != null && recentCommences.size() > 0) {
                            if (adapter == null) {
                                //未下拉加载 所以标记是否热门 新鲜要显示出来
                                beanList.add(new NeiHanCommentsItemBean(null, null, null, NeiHanCommentsItemBean.TYPE_NEW));
                            }
                            for (NeiHanCommentsBean.Data.RecentComments r : recentCommences) {
                                NeiHanCommentsItemBean itemBean =
                                        new NeiHanCommentsItemBean(r.getText(),
                                                r.getAvatarUrl(),
                                                r.getUserName(),
                                                NeiHanCommentsItemBean.TYPE_NORMAL);
                                beanList.add(itemBean);
                            }
                        }
                        return Observable.just(beanList);
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                        if (adapter == null) {
                            view.findViewById(R.id.pb).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        List<NeiHanCommentsItemBean> beanList = (List<NeiHanCommentsItemBean>) o;
                        if (beanList != null && beanList.size() > 0) {
                            if (adapter != null) {
                                adapter.loadMoreComplete();
                            }
                            initList(activity, beanList, view, groupId);
                        } else {
                            if (adapter != null) {
                                adapter.loadMoreEnd();
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (adapter != null) {
                            adapter.loadMoreEnd();
                        }
                        view.findViewById(R.id.pb).setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        view.findViewById(R.id.pb).setVisibility(View.GONE);
                    }
                });
    }


    private void showDialog(Context context, View view) {
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.AppThemeDialog).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            attributes.height = -1;
            window.setGravity(Gravity.BOTTOM);
            window.setAttributes(attributes);
            window.setContentView(view);
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (adapter != null) {
                    adapter = null;
                }
                offset = 0;
                if (disposables != null && disposables.size() > 0) {
                    for (Disposable d : disposables) {
                        if (!d.isDisposed()) {
                            d.dispose();
                        }
                    }
                }
            }
        });
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private View getView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_dialog_comments, new FrameLayout(context), false);
    }
}
