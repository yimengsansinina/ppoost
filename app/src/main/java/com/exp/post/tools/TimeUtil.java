package com.exp.post.tools;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.exp.post.BaseApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {


    // 将时间戳转换为更友好的格式
    public static void closeInput(EditText editText) {
        // 获取输入法管理器
        InputMethodManager imm = (InputMethodManager) BaseApp.Companion.getMApp().getSystemService(Context.INPUT_METHOD_SERVICE);
// 关闭键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

// 移除焦点，隐藏光标
        editText.clearFocus();
    }

    public static String formatFriendlyTimestamp(long timestamp) {
        // 当前时间
        Calendar now = Calendar.getInstance();

        // 时间戳对应的日期
        Calendar targetDate = Calendar.getInstance();
        targetDate.setTimeInMillis(timestamp);

        // 判断是否是今天
        if (isSameDay(now, targetDate)) {
            return "今天 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(timestamp));
        }

        // 判断是否是昨天
        now.add(Calendar.DAY_OF_YEAR, -1); // 设置为前一天
        if (isSameDay(now, targetDate)) {
            return "昨天 " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(timestamp));
        }

        // 否则，返回 "MM月dd日 时分秒"
        return new SimpleDateFormat("MM月dd日 HH:mm:ss", Locale.getDefault()).format(new Date(timestamp));
    }

    // 判断两个 Calendar 是否是同一天
    private static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

}

