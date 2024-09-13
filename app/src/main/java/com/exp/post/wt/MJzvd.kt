package com.exp.post.wt

import android.content.Context
import android.util.AttributeSet
import cn.jzvd.JzvdStd

class MJzvd : JzvdStd {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onPrepared() {
        super.onPrepared()
        // 视频准备好后，执行 seekTo 跳转到指定时间
//        val seekTimeMillis = (60 * 1000).toLong() // 60秒
//        mediaInterface.seekTo(seekTimeMillis)
        mlistener?.onPrepared()
    }

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
        mlistener?.onFinish()
    }
    interface IPreparedListener{
        fun onPrepared()
        fun  onFinish()
    }
    private var mlistener:IPreparedListener?=null
    public fun setPreparedListener(listener: IPreparedListener){
        this.mlistener=listener;
    }
}
