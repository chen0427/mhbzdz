package com.chenzhipeng.mhbzdz.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.widget.ComicReadPictureView;
import com.chenzhipeng.mhbzdz.widget.JokePictureView;
import com.chenzhipeng.mhbzdz.widget.WallpaperPictureView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.blurry.Blurry;

public class ImageHelper {
    private static RequestManager glide() {
        return Glide.with(BaseApplication.getContext());
    }


    /**
     * @param placeholderId -1代表无placeholder
     */
    public static <T extends ImageView> void setImage(String url, T imageView, int placeholderId) {
        if (!TextUtils.isEmpty(url) && imageView != null) {
            if (placeholderId == -1) {
                glide().load(url).into(imageView);
            } else {
                glide().load(url).placeholder(placeholderId).into(imageView);
            }
        }
    }

    /**
     * @param placeholderId -1代表无placeholder
     */
    public static <T extends ImageView> void setImage(File file, T imageView, int placeholderId) {
        if (file != null && file.exists() && imageView != null) {
            if (placeholderId == -1) {
                glide().load(file).into(imageView);
            } else {
                glide().load(file).placeholder(placeholderId).into(imageView);
            }

        }
    }

    public static void setJokeToGIF(final String url, final JokePictureView jokePictureView) {
        if (!TextUtils.isEmpty(url) && jokePictureView != null) {
            glide().load(url).placeholder(R.color.black)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(jokePictureView.getImageView()) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            jokePictureView.setProgressBarVisibility(true);
                            jokePictureView.setTextVisibility(false);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            jokePictureView.setProgressBarVisibility(false);
                            jokePictureView.setText(BaseApplication.getContext().getString(R.string.load_failed));
                            jokePictureView.setTextVisibility(true);
                            jokePictureView.getImageView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setJokeToGIF(url, jokePictureView);
                                }
                            });
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            jokePictureView.setProgressBarVisibility(false);
                            jokePictureView.setTextVisibility(false);
                        }
                    });
        }
    }

    public static void setJokeToLargeImage(final String url, final JokePictureView jokePictureView) {
        if (!TextUtils.isEmpty(url) && jokePictureView != null) {
            glide().load(url).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    jokePictureView.getScaleImageView().setImage(ImageSource.resource(R.color.black));
                    jokePictureView.setProgressBarVisibility(true);
                    jokePictureView.setTextVisibility(false);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    jokePictureView.setProgressBarVisibility(false);
                    jokePictureView.setText(BaseApplication.getContext().getString(R.string.load_failed));
                    jokePictureView.setTextVisibility(true);
                    jokePictureView.getScaleImageView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setJokeToLargeImage(url, jokePictureView);
                        }
                    });
                }

                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    jokePictureView.getScaleImageView().setImage(ImageSource.uri(Uri.fromFile(resource)));
                    jokePictureView.setProgressBarVisibility(false);
                    jokePictureView.setTextVisibility(false);
                }
            });
        }
    }

    /**
     * 设置模糊效果
     */
    public static void setBlurry(String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url) && imageView != null) {
            glide().load(url).asBitmap().into(new BlurryListener(imageView));
        }
    }


    public static void setWallpaper(final String url, final WallpaperPictureView wallpaperPictureView) {
        if (!TextUtils.isEmpty(url) && wallpaperPictureView != null) {
            glide().load(url)
                    .placeholder(R.color.black)
                    .into(new GlideDrawableImageViewTarget(wallpaperPictureView.getPhotoView()) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            wallpaperPictureView.getProgressBar().setVisibility(View.VISIBLE);
                            wallpaperPictureView.getTextView().setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            wallpaperPictureView.getProgressBar().setVisibility(View.GONE);
                            wallpaperPictureView.getTextView().setText(BaseApplication.getContext().getString(R.string.load_failed));
                            wallpaperPictureView.getTextView().setVisibility(View.VISIBLE);
                            wallpaperPictureView.getPhotoView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setWallpaper(url, wallpaperPictureView);
                                }
                            });
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            wallpaperPictureView.getProgressBar().setVisibility(View.GONE);
                            wallpaperPictureView.getTextView().setVisibility(View.GONE);
                        }
                    });
        }
    }


    public static void setWallpaper(final File file, final WallpaperPictureView wallpaperPictureView) {
        if (file != null && file.exists() && wallpaperPictureView != null) {
            glide().load(file)
                    .placeholder(R.color.black)
                    .into(new GlideDrawableImageViewTarget(wallpaperPictureView.getPhotoView()) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            wallpaperPictureView.getProgressBar().setVisibility(View.VISIBLE);
                            wallpaperPictureView.getTextView().setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            wallpaperPictureView.getProgressBar().setVisibility(View.GONE);
                            wallpaperPictureView.getTextView().setText(BaseApplication.getContext().getString(R.string.load_failed));
                            wallpaperPictureView.getTextView().setVisibility(View.VISIBLE);
                            wallpaperPictureView.getPhotoView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setWallpaper(file, wallpaperPictureView);
                                }
                            });
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            wallpaperPictureView.getProgressBar().setVisibility(View.GONE);
                            wallpaperPictureView.getTextView().setVisibility(View.GONE);
                        }
                    });
        }
    }

    public static File getFile(String url) {
        File file = null;
        try {
            file = glide().load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void setComicPicture(final String url, final ComicReadPictureView pictureView) {
        if (!TextUtils.isEmpty(url) && pictureView != null) {
            glide().load(url).placeholder(R.color.black).fitCenter().into(new GlideDrawableImageViewTarget(pictureView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    pictureView.setText(BaseApplication.getContext().getString(R.string.comic_picture_loading));
                    pictureView.setTextVisibility(true);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    pictureView.setText(BaseApplication.getContext().getString(R.string.load_failed));
                    pictureView.setTextVisibility(true);
                    pictureView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setComicPicture(url, pictureView);
                        }
                    });
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    pictureView.setTextVisibility(false);
                }
            });
        }
    }

    public static void setComicPicture(final File file, final ComicReadPictureView pictureView) {
        if (file != null && file.exists() && pictureView != null) {
            glide().load(file).placeholder(R.color.black).fitCenter().into(new GlideDrawableImageViewTarget(pictureView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    pictureView.setText(BaseApplication.getContext().getString(R.string.comic_picture_loading));
                    pictureView.setTextVisibility(true);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    pictureView.setText(BaseApplication.getContext().getString(R.string.load_failed));
                    pictureView.setTextVisibility(true);
                    pictureView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setComicPicture(file, pictureView);
                        }
                    });
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    pictureView.setTextVisibility(false);
                }
            });
        }
    }

    public static void setLargeImageToList(String url, final SubsamplingScaleImageView scaleImageView, final AppCompatTextView seeTextView) {
        if (!TextUtils.isEmpty(url) && scaleImageView != null && seeTextView != null) {
            glide().load(url).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    scaleImageView.setImage(ImageSource.resource(R.color.placeholder));
                    seeTextView.setVisibility(View.GONE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    seeTextView.setVisibility(View.GONE);
                }

                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    scaleImageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                    seeTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public static void setGIFToList(String url, final AppCompatImageView imageView, final AppCompatTextView tagTextView, final boolean isSetSize, final Bitmap bitmap) {
        if (!TextUtils.isEmpty(url) && imageView != null && tagTextView != null) {
            glide().load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            if (isSetSize) {
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            } else {
                                imageView.setImageResource(R.color.placeholder);
                            }
                            tagTextView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            tagTextView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            tagTextView.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    public static void setImageToList(String url, final AppCompatImageView imageView, final boolean isSetSize, final Bitmap bitmap) {
        if (!TextUtils.isEmpty(url) && imageView != null) {
            glide().load(url).into(new GlideDrawableImageViewTarget(imageView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    if (isSetSize) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    } else {
                        imageView.setImageResource(R.color.placeholder);
                    }
                }
            });
        }
    }


    private static class BlurryListener extends SimpleTarget<Bitmap> {
        private ImageView imageView;

        BlurryListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            Blurry.with(BaseApplication.getContext()).from(resource).into(imageView);
        }
    }

}
