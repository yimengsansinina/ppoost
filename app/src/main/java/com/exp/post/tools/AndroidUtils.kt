package com.exp.post.tools

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.text.TextUtils
import android.util.TypedValue
import com.exp.post.BaseApp
import java.util.Calendar
import java.util.Stack


object AndroidUtils {

    var soundPool: SoundPool? = null
    fun releasePlay() {
        if (soundPool != null) {
            soundPool?.release();
            soundPool = null;
        }
    }
    fun phoneW(): Int {
        return BaseApp.mApp!!.resources.displayMetrics.widthPixels
    }
    fun phoneH(): Int {
        return BaseApp.mApp!!.resources.displayMetrics.heightPixels
    }
    fun Context.dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    fun Context.dp2px(dp: Int): Int {
        return dp2px(dp.toFloat()).toInt()
    }

//    fun navDetail(activity: BaseActivity) {
//        val appPackageName = BaseApp.mApp?.packageName
//        // 构建跳转到 Play 商店的 URI
//        val uri = Uri.parse("market://details?id=" + appPackageName);
//        // 创建 Intent
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (intent.resolveActivity(BaseApp.mApp!!.getPackageManager()) != null) {
//            activity.startActivity(intent);
//        } else {
//            // 若没有应用能够处理该 Intent，则在浏览器中打开 Play 商店页面
//            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
//            activity.startActivity(intent);
//        }
//    }

    fun determineNumberLevel(number: String): String {
        if (TextUtils.isEmpty(number)) {
            return ""
        }
        var numberT = number
        if (number.contains(".")) {
            val index = number.lastIndexOf(".")
            numberT = number.substring(0, index)
        }
        val res = numberT.replace("-", "")
        val digitCount = res.length
        val levels =
            arrayOf("百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿")
        return when (digitCount) {
            0, 1, 2 -> ""
            in 3..13 -> levels[digitCount - 3]
            else -> {
                "亿亿"
            }
        }
    }

    public fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo: PackageInfo =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "1.0.0.0"
        }
    }
    public fun getAppVersionCode(context: Context):Int {
        return try {
            val packageInfo: PackageInfo =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
           0
        }
    }
}