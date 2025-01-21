package com.exp.post

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.exp.post.bean.InitAppBeanLittle
import com.exp.post.bean.LoginAppBean
import com.exp.post.bean.LoginAppResponse
import com.exp.post.bean.MessageInfo
import com.exp.post.bean.PayInfo
import com.exp.post.bean.UpgradeVersion
import com.exp.post.databinding.ActivitySplashBinding
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.SPTools
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 设置全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupStatusBar()
        supportActionBar?.hide()
        initData()
    }
    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun initData() {
        loginApp()
    }
companion object{
    private const val TAG ="SplashActivity"
}
    private fun loginApp() {
        loginAppNet({
            Log.d(TAG, "loginApp() called,success,it=$it")
//            initUpgradeVersion(it.upgradeVersion)
//            initMessage(it.messageInfo)
            initMemberPayList(it.memberPayList)
            initListInfoTextArr(it.listInfoTextArr)
            initPageList(it.pageList)

            startMainActivity(it.upgradeVersion,it.messageInfo)
        }, {
            Log.d(TAG, "loginApp() error=$it")
            showRetryDialog()
        })
    }

    private fun initPageList(pageList: List<LoginAppBean>?) {
        SPTools.initPageList(pageList)
    }

    private fun initListInfoTextArr(listInfoTextArr: List<List<String>>?) {
        SPTools.initListInfoTextArr(listInfoTextArr)
    }

    private fun initMemberPayList(memberPayList: List<PayInfo>?) {
        SPTools.saveMemberPayList(memberPayList)
    }

//    private fun initMessage(messageInfo: MessageInfo?) {
//        MessageManager.handleMessageInfo(this, messageInfo)
//    }
//
//    private fun initUpgradeVersion(upgradeVersion: UpgradeVersion?) {
//        UpdateManager.checkUpdate(this, upgradeVersion)
//    }
    private val mHandler = Handler(Looper.getMainLooper())
    private fun loginAppNet(
        success: (InitAppBeanLittle) -> Unit,
        fail: (Int) -> Unit
    ) {
        HttpClient.instance.getServer(NetApi::class.java)
            .loginApp()
            .enqueue(object : Callback<LoginAppResponse> {
                override fun onResponse(
                    call: Call<LoginAppResponse>,
                    response: Response<LoginAppResponse>
                ) {
                    if (!response.isSuccessful) {
                        mHandler.post {
                            fail(101)
                        }
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        mHandler.post {
                            fail(102)
                        }
                        return
                    }
                    //
                    if (body.code != 0) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    if (body.res == null) {
                        mHandler.post {
                            fail(103)
                        }
                        return
                    }
                    mHandler.post {
                        success(body.res!!)
                    }
                }

                override fun onFailure(call: Call<LoginAppResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure() called with: call = $call, t = ${t.message}")
                    mHandler.post {
                        fail(104)
                    }
                }

            })
    }


    private fun startMainActivity(upgradeVersion: UpgradeVersion?, messageInfo: MessageInfo?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("upgradeVersion",upgradeVersion)
        intent.putExtra("messageInfo",messageInfo)
        startActivity(intent)
        finish()
    }
    
    private fun showRetryDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("初始化失败")
            .setMessage("网络异常,请重试")
            .setCancelable(false)
            .setPositiveButton("重试") { _, _ ->
                initData()
            }
            .setNegativeButton("退出") { _, _ ->
                finish()
            }
            .show()
    }
} 