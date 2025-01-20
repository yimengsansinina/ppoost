package com.exp.post.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.KeyEvent
import android.view.WindowManager
import com.exp.post.R
import com.exp.post.bean.UpgradeVersion
import com.google.android.material.dialog.MaterialAlertDialogBuilder
object UpdateManager {

    fun checkUpdate(context: Context, upgradeInfo: UpgradeVersion?) {
        if (upgradeInfo == null || upgradeInfo.upgradeVersion <= getAppVersionCode(context)) {
            return
        }

        showUpdateDialog(context, upgradeInfo)
    }

    private fun showUpdateDialog(context: Context, upgradeInfo: UpgradeVersion) {
        val builder = MaterialAlertDialogBuilder(context)
            .setTitle("发现新版本")
            .setMessage(upgradeInfo.upgradeInfo)
            .setPositiveButton("立即更新") { dialog, _ ->
                dialog.dismiss()
                upgradeInfo.upgradePath?.let { url ->
                    openBrowser(context, url)
                }
            }
            .setCancelable(!(upgradeInfo.forceUpgradeVersion==2))

        // 非强制更新才显示取消按钮
        if (!(upgradeInfo.forceUpgradeVersion==2)) {
            builder.setNegativeButton("暂不更新") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val dialog = builder.create()

        // 强制更新时禁用返回键
        dialog.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK && upgradeInfo.forceUpgradeVersion==2
        }

        dialog.show()
    }

    private fun openBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(context, "无法打开下载链接", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAppVersionCode(context: Context): Int {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
//private fun showUpdateDialog(context: Context, upgradeInfo: UpgradeVersion) {
//    val builder = MaterialAlertDialogBuilder(context, R.style.UpdateDialog)
//        .setTitle("发现新版本")
//        .setMessage(upgradeInfo.upgradeInfo)
//        .setPositiveButton("立即更新") { dialog, _ ->
//            dialog.dismiss()
//            upgradeInfo.upgradePath?.let { url ->
//                openBrowser(context, url)
//            }
//        }
//        .setCancelable(!upgradeInfo.forceUpgradeVersion)
//
//    // 非强制更新才显示取消按钮
//    if (!upgradeInfo.forceUpgradeVersion) {
//        builder.setNegativeButton("暂不更新") { dialog, _ ->
//            dialog.dismiss()
//        }
//    }
//
//    val dialog = builder.create()
//
//    // 强制更新时禁用返回键
//    dialog.setOnKeyListener { _, keyCode, event ->
//        keyCode == KeyEvent.KEYCODE_BACK && upgradeInfo.forceUpgradeVersion
//    }
//
//    // 设置对话框宽度
//    dialog.show()
//    dialog.window?.apply {
//        setLayout(
//            (context.resources.displayMetrics.widthPixels * 0.85).toInt(),
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//    }
//}
//private fun openBrowser(context: Context, url: String) {
//    try {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intent)
//    } catch (e: Exception) {
//        e.printStackTrace()
////            Toast.makeText(context, "无法打开链接", Toast.LENGTH_SHORT).show()
//    }
//}