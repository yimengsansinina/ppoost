package com.exp.post.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.exp.post.R;

public class GlideUtils {

    // 加载图片到 ImageView
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // 缓存策略
                        .placeholder(R.drawable.video_default) // 占位符
                        .error(R.drawable.video_default)) // 错误图像
                .into(imageView);
    }
    public static void loadImageCenterCrop(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // 缓存策略
                        .placeholder(R.drawable.video_default) // 占位符
                        .error(R.drawable.video_default)) // 错误图像
                .into(imageView);
    }

    // 加载圆形图片
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .circleCrop() // 圆形裁剪
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.video_default)
                        .error(R.drawable.video_default))
                .into(imageView);
    }

    // 加载图片并禁用缓存
    public static void loadImageWithoutCache(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .skipMemoryCache(true) // 禁用内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // 禁用磁盘缓存
                        .placeholder(R.drawable.video_default)
                        .error(R.drawable.video_default))
                .into(imageView);
    }

    // 加载带有尺寸限制的图片
    public static void loadImageWithSize(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .override(width, height) // 指定尺寸
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.video_default)
                        .error(R.drawable.video_default))
                .into(imageView);
    }
}
