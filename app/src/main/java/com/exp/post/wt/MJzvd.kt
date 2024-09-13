package com.exp.post.wt

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import cn.jzvd.JzvdStd
class MJzvd : JzvdStd {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onPrepared() {
        super.onPrepared()
        mlistener?.onPrepared()
    }
    override fun gotoFullscreen()
       {
           super.gotoFullscreen()
           btnSpeed?.visibility = INVISIBLE
    }
    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
        mlistener?.onFinish()
    }
    interface IPreparedListener{
        fun onPrepared()
        fun  onFinish()
    }

    private var currentSpeed = 1.0f // 当前倍速

    private var btnSpeed: Button?=null
    private var mlistener:IPreparedListener?=null
    public fun setPreparedListener(listener: IPreparedListener){
        this.mlistener=listener;
    }
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (btnSpeed == null) {
            btnSpeed = findViewById<Button>(com.exp.post.R.id.btn_speed)
            // 设置倍速按钮的点击事件
            btnSpeed?.setOnClickListener { // 每次点击改变倍速
                changeSpeed()
            }
        }
    }
    // 切换倍速
    private fun changeSpeed() {
        if (currentSpeed === 1.0f) {
            currentSpeed = 1.5f
        } else if (currentSpeed === 1.5f) {
            currentSpeed = 2.0f
        } else {
            currentSpeed = 1.0f
        }

        // 修改按钮文本
        btnSpeed?.setText(""+currentSpeed + "x")

        // 设置播放速度
        setPlaybackSpeed(currentSpeed)
    }

    // 设置播放速度的方法
    private fun setPlaybackSpeed(speed: Float) {
        if (mediaInterface != null) {
            try {
                mediaInterface.setSpeed(speed)
                Toast.makeText(context, "播放速度设置为 " + speed + "x", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun setAllControlsVisiblity(
        topCon: Int,
        bottomCon: Int,
        startBtn: Int,
        loadingPro: Int,
        posterImg: Int,
        bottomPro: Int,
        retryLayout: Int
    ) {
        super.setAllControlsVisiblity(
            topCon,
            bottomCon,
            startBtn,
            loadingPro,
            posterImg,
            bottomPro,
            retryLayout
        )
        Log.d(TAG, "setAllControlsVisiblity: topCon=${topCon}")
        if (topCon==View.VISIBLE){
            btnSpeed?.visibility=View.VISIBLE
        }else{
            btnSpeed?.visibility=View.INVISIBLE
        }

    }

    override fun dissmissControlView() {
        super.dissmissControlView()
        Log.d(TAG, "dissmissControlView: ")
        btnSpeed?.visibility=View.INVISIBLE
    }
}
