package com.exp.post.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicProgressUtil {

    // 将时间戳转换为友好的时间进度显示
    public static String formatMusicProgress(long timestamp) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceMillis = currentTimeMillis - timestamp;

        // 刚刚 (1 分钟以内)
        if (timeDifferenceMillis < TimeUnit.MINUTES.toMillis(1)) {
            return "刚刚";
        }

        // 几分钟前 (1 分钟到1小时)
        if (timeDifferenceMillis < TimeUnit.HOURS.toMillis(1)) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);
            return minutes + "分钟前";
        }

        // 时:分:秒 (1小时以上)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return timeFormat.format(new Date(timestamp));
    }

    // 将播放进度（毫秒）转化为 时:分:秒 格式
    public static String formatPlayTime(long progressMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(progressMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(progressMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(progressMillis) % 60;

        // 格式化为 时:分:秒
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

}
