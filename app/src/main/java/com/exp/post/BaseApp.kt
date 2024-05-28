package com.exp.post

import android.app.Application
import com.exp.post.dbs.ObjectBox
import com.tencent.mmkv.MMKV

class BaseApp : Application() {
    companion object {
        var mApp: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
        // 初始化 MMKV
        MMKV.initialize(this);
        ObjectBox.init(this)
    }
}