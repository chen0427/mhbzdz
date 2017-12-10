package com.chenzhipeng.mhbzdz.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

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

    public static <T extends ImageView> void setGIF(String url, final T imageView, @Nullable final ProgressBar progressBar, int placeholderId) {
        if (!TextUtils.isEmpty(url) && imageView != null) {
            glide().load(url).downloadOnly(new GifListener<>(imageView, progressBar, placeholderId));
        }
    }

    public static void setLargeImage(String url, SubsamplingScaleImageView largeImageView, @Nullable ProgressBar progressBar, int placeholderId) {
        if (!TextUtils.isEmpty(url) && largeImageView != null) {
            glide().load(url).downloadOnly(new LargeListener(largeImageView, progressBar, placeholderId));
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

    /**
     * 设置壁纸
     */
    public static void setWallpaper(String url, PhotoView photoView, final ProgressBar progressBar) {
        if (!TextUtils.isEmpty(url) && photoView != null && progressBar != null) {
            glide().load(url).placeholder(R.color.black).into(new GlideDrawableImageViewTarget(photoView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 设置壁纸
     */
    public static void setWallpaper(File file, PhotoView photoView, final ProgressBar progressBar) {
        if (file != null && file.exists() && photoView != null && progressBar != null) {
            glide().load(file).placeholder(R.color.black).into(new GlideDrawableImageViewTarget(photoView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    progressBar.setVisibility(View.GONE);
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

    public static void setComicPicture(String url, final ComicReadPictureView pictureView) {
        if (!TextUtils.isEmpty(url) && pictureView != null) {
            glide().load(url).placeholder(R.color.black).fitCenter().into(new GlideDrawableImageViewTarget(pictureView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    pictureView.setText(BaseApplication.getContext().getString(R.string.comic_picture_loading));
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    pictureView.setTextVisibility(false);
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    pictureView.setTextVisibility(false);
                }
            });
        }
    }

    public static void setComicPicture(File file, final ComicReadPictureView pictureView) {
        if (file != null && file.exists() && pictureView != null) {
            glide().load(file).placeholder(R.color.black).fitCenter().into(new GlideDrawableImageViewTarget(pictureView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    pictureView.setText(BaseApplication.getContext().getString(R.string.comic_picture_loading));
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    pictureView.setTextVisibility(false);
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


    /**
     * 加载长图
     */
    private static class LargeListener extends SimpleTarget<File> {
        private SubsamplingScaleImageView largeImageView;
        private ProgressBar progressBar;
        private int placeholderId;


        LargeListener(SubsamplingScaleImageView largeImageView, ProgressBar progressBar, int placeholderId) {
            this.largeImageView = largeImageView;
            this.progressBar = progressBar;
            this.placeholderId = placeholderId;
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            largeImageView.setImage(ImageSource.resource(placeholderId));
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
            largeImageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
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


    private static class GifListener<T extends ImageView> extends SimpleTarget<File> {
        private ProgressBar progressBar;
        private T imageView;
        private int placeholderId;

        GifListener(T imageView, ProgressBar progressBar, int placeholderId) {
            this.progressBar = progressBar;
            this.imageView = imageView;
            this.placeholderId = placeholderId;
        }


        @Override
        public void onLoadStarted(Drawable placeholder) {
            imageView.setImageResource(placeholderId);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

        }

        @Override
        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
            glide().load(resource).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


}
