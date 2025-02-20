package com.exp.post

import android.app.Application
import com.exp.post.dbs.ObjectBox
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.tencent.mmkv.MMKV
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

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
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) //EXO模式
//        PlayerFactory.setPlayManager(SystemPlayerManager::class.java) //系统模式
//        PlayerFactory.setPlayManager(IjkPlayerManager::class.java) //ijk模式
//        PlayerFactory.setPlayManager(AliPlayerManager::class.java) //aliplay 内核模式
//        CacheFactory.setCacheManager(ExoPlayerCacheManager());//exo缓存模式，支持m3u8，只支持exo
//CacheFactory.setCacheManager(new ProxyCacheManager());//代理缓存模式，支持所有模式，不支持m3u8等
    }
}