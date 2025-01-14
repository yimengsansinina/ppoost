
package com.exp.post.ui.notifications
import android.content.Intent
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
        Toast.makeText(this, "正在清理缓存...", Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val TAG = "SettingsActivity"
        fun nav(activity: FragmentActivity) {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private fun checkUpdate() {
        Toast.makeText(this, "检查更新中...", Toast.LENGTH_SHORT).show()
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