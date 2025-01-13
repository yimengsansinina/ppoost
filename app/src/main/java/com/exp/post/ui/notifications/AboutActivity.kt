package com.exp.post.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import com.exp.post.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setupViews()
    }

    private fun setupViews() {
        supportActionBar?.hide()
        // 设置返回按钮
        findViewById<ImageView>(R.id.ivBack)?.setOnClickListener {
            finish()
        }

        // 设置版本号
        findViewById<TextView>(R.id.tvVersion)?.text = "v${getVersionName()}"

        // 设置隐私政策点击事件
        findViewById<CardView>(R.id.cvPrivacy)?.setOnClickListener {
            openPrivacyPolicy()
        }
    }

    private fun getVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    private fun openPrivacyPolicy() {
        // TODO: 实现打开隐私政策页面的逻辑
        Toast.makeText(this, "打开隐私政策", Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val TAG = "AboutActivity"
        fun nav(activity: FragmentActivity) {
            val intent = Intent(activity, AboutActivity::class.java)
            activity.startActivity(intent)
        }
    }
} 