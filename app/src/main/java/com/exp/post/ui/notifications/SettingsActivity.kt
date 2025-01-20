
package com.exp.post.ui.notifications
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import com.exp.post.R
import com.exp.post.tools.SPTools
import com.exp.post.ui.WatchHistoryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupViews()
    }

    private fun setupViews() {
        supportActionBar?.hide()
        // 设置版本号
        findViewById<TextView>(R.id.tvVersion)?.text = getVersionName()

        // 空间清理点击事件
        findViewById<CardView>(R.id.cvClearCache)?.setOnClickListener {
            clearCache()
        }

        // 版本信息点击事件
        findViewById<CardView>(R.id.cvVersion)?.setOnClickListener {
            checkUpdate()
        }

        // 退出登录点击事件
        findViewById<AppCompatImageView>(R.id.ivBack)?.setOnClickListener {
            finish()
        }
        findViewById<AppCompatButton>(R.id.btnLogout)?.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun getVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            "v${packageInfo.versionName}"
        } catch (e: Exception) {
            "v1.0.0"
        }
    }

    private fun clearCache() {
        findViewById<CardView>(R.id.cvVersion)?.postDelayed({
            Toast.makeText(this, "清理完成", Toast.LENGTH_SHORT).show()
        },500)
        Toast.makeText(this, "正在清理缓存...", Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val TAG = "SettingsActivity"
        fun nav(activity: FragmentActivity) {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private fun openPlayStore(context: Context) {
        try {
            val packageName = getPackageName()
            // 尝试打开 Play 商店应用
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$packageName")
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // 如果没有安装 Play 商店应用，则打开网页版
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            }
            context.startActivity(webIntent)
        }
    }
    private fun checkUpdate() {
        openPlayStore(this)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("确定要退出登录吗？")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定") { _, _ ->
                logout()
            }
            .show()
    }

    private fun logout() {
        SPTools.clear()
        Toast.makeText(this, "退出登录成功", Toast.LENGTH_SHORT).show()
        finish()
    }
} 