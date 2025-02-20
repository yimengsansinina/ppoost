package com.exp.post

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exp.post.adapter.EPAdapter
import com.exp.post.adapter.RouteAdapter
import com.exp.post.bean.EpBean
import com.exp.post.bean.MovieInfoRequest
import com.exp.post.bean.MovieInfoResponse
import com.exp.post.databinding.ActivityMovieDetailBinding
import com.exp.post.dbs.HistoryPageBean
import com.exp.post.dbs.HistoryUtils
import com.exp.post.dbs.PageBean
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.GradleItemDecord
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MovieDetailActivity : GSYBaseActivityDetail<StandardGSYVideoPlayer>() {

    private var disposable: Disposable? = null
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initView()
    }

    private val mId by lazy {
        intent?.getLongExtra("id", 0) ?: 0
    }

    companion object {
        const val TAG = "MovieDetailActivity"
        fun nav(activity: AppCompatActivity, id: Long) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
        }
    }

    private lateinit var detailPlayer: StandardGSYVideoPlayer
    private lateinit var speedControlLayout: Button
    interface OnVisibilityChangeListener {
        fun onVisibilityChanged(visible: Boolean)
    }
    private fun setVisibilityListener(view: View, listener: OnVisibilityChangeListener) {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            private var lastVisibility = view.visibility
            override fun onViewAttachedToWindow(v: View) {
                v.viewTreeObserver.addOnGlobalLayoutListener {
                    val newVisibility = v.visibility
                    if (newVisibility != lastVisibility) {
                        lastVisibility = newVisibility
                        listener.onVisibilityChanged(newVisibility == View.VISIBLE)
                    }
                }
            }
            override fun onViewDetachedFromWindow(v: View) {}
        })
    }

    private var currentSpeed = 1.0f

    // 切换倍速
    private fun changeSpeed() {
        detailPlayer.restartTimerTask()
        if (currentSpeed === 1.0f) {
            currentSpeed = 1.5f
        } else if (currentSpeed === 1.5f) {
            currentSpeed = 2.0f
        } else if (currentSpeed === 2.0f) {
            currentSpeed = 3.0f
        } else {
            currentSpeed = 1.0f
        }
        // 修改按钮文本
        speedControlLayout?.setText("" + currentSpeed + "x")
        // 设置播放速度
        detailPlayer.setSpeed(currentSpeed, false)
    }

    private fun initView() {
        if (mId == 0L) {
            return
        }
//        CacheFactory.setCacheManager(ExoPlayerCacheManager());//exo缓存模式，支持m3u8，只支持exo
        //11
        detailPlayer = findViewById(R.id.detail_player)
        speedControlLayout = detailPlayer.findViewById<Button>(R.id.btn_speed)
        speedControlLayout.setOnClickListener {
            Log.d(TAG, "initView() speedControlLayout called")
            changeSpeed()
        }
        //增加title
//        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);

        // 将按钮添加到播放器控制层
        // 获取底部控制栏
        val bottomContainer =
            detailPlayer.findViewById<ViewGroup>(com.shuyu.gsyvideoplayer.R.id.layout_bottom)
//        bottomContainer.addView(speedButton)
        setVisibilityListener(bottomContainer, object : OnVisibilityChangeListener {
            override fun onVisibilityChanged(visible: Boolean) {
                if (visible) {
                    speedControlLayout.visibility = View.VISIBLE
                } else {
                    speedControlLayout.visibility = View.GONE
                }
                Log.d(TAG, "onVisibilityChanged() called with: visible = $visible")
            }

        })

        // 方式1：使用 GSYSampleCallBack
        detailPlayer.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPlayError(url: String?, vararg objects: Any?) {
                // 播放错误回调
                Toast.makeText(this@MovieDetailActivity, "播放错误", Toast.LENGTH_SHORT).show()
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                // 播放完成回调
                Toast.makeText(this@MovieDetailActivity, "播放完成", Toast.LENGTH_SHORT).show()
                //播放下一個
                playNext()
                Log.d(TAG, "onFinish: ")
            }

        })
        binding.backTiny.setOnClickListener {
            finish()
        }
        requestMovie()
    }


    private fun playNext() {
        val currentPos = epAdapter.checkPos + 1
        if (currentPos < 0) {
            return
        }
        if (currentPos > epAdapter.data.size - 1) {
            Toast.makeText(this, "播放結束", Toast.LENGTH_SHORT).show()
            return
        }

        epAdapter.checkPos = currentPos
        mCurrentEpIndex = currentPos
        val epBean = epAdapter.data[currentPos]
        epAdapter.notifyDataSetChanged()
        clickEp(epBean, 0)
    }

    private var mMovie: PageBean? = null
    private fun initMovieUI() {

        mMovie?.run {
            binding.name.text = playName
            val info = when (topClass) {
                1 -> "电影"
                2 -> "电视剧"
                3 -> "动漫"
                4 -> "综艺"
                else -> "纪录片"
            }
            val sb = StringBuffer(playArea)
            sb
                .append("/")
                .append(info)
                .append("/")
                .append(playYear)
                .append("/")
                .append(playMark)
            binding.information.text = sb
            binding.actor.text = playActor
            binding.mark.text = playMark
            binding.nameTitle.text = playName
        }
        binding.desc.setOnClickListener {
            showDesContent()
        }
        binding.mark.setOnClickListener {
            xuanJi()
        }
        binding.more1.setOnClickListener {
            xuanJi()
        }
    }

    private fun requestMovie() {
        getFromHistory { history ->
            Log.d(TAG, "requestMovie: getFromHistory=${history?.historyProgress}")
            mCurrentRouteIndex = history?.historySource ?: 0
            mCurrentEpIndex = history?.historyEpIndex ?: 0
            queryMovieList(mId, {
                it.historyProgress = history?.historyProgress ?: 0L
                it.historySource = history?.historySource ?: 0
                it.historyEpIndex = history?.historyEpIndex ?: 0
                mMovie = it
                Log.d(TAG, "requestMovie: mMovie=$mMovie")
                initMovieUI()
                initRecycler()
//                startPlay()
            }, {
                //todo
            })
        }
    }

    private fun initRecycler() {
        if (mMovie == null) {
            return
        }
        val playList = mMovie!!.playList
        // playList=[, 全集@@@https://v5.daayee.com/yyv5/202501/20/6EFHAzhYEr19/video/index.m3u8, , , , , , ]
        Log.d(TAG, "initRecycler() playList=$playList")
        if (playList.isNullOrEmpty()) {
            return
        }
        playList.forEachIndexed { index, s ->
            if (!TextUtils.isEmpty(s)) {
                fetchBeanList(index, s)
                routeIndexList.add(index)
            }
        }
        if (!routeIndexList.contains(mCurrentRouteIndex)) {
            if (routeIndexList.isNotEmpty()) {
                mCurrentRouteIndex = routeIndexList[0]
            }
        }
        Log.d(TAG, "initRecycler: routeList=$routeIndexList")
        initRouteRv()
        val bean = initEpRv()
        bean?.run {
            val sec = mMovie?.historyProgress ?: 0L
            Log.d(TAG, "initRecycler: sec=$sec")
            mFirstEpBean = this
            initVideoBuilderMode()
            clickEp(this, sec)
        }
    }

    private var mFirstEpBean: EpBean? = null
    private fun initEpRv(): EpBean? {
        val epBean = routeMap[mCurrentRouteIndex]
        if (epBean == null) {
            return null
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = epAdapter
        if (mCurrentEpIndex <= epBean.size - 1 && mCurrentEpIndex >= 0) {
            epAdapter.checkPos = mCurrentEpIndex
        } else {
            mCurrentEpIndex = 0
        }
        epAdapter.setList(epBean)
        return epBean[mCurrentEpIndex]
    }

    // routeList=[1]
    private val routeIndexList by lazy {
        arrayListOf<Int>()
    }
    private val routeAdapter by lazy {
        RouteAdapter {
            clickRoute(it)
        }
    }

    private fun clickRoute(routeIndex: Int) {
        if (routeIndex == mCurrentRouteIndex) {
            return
        }
        val epBeans = routeMap[routeIndex]
        if (epBeans == null) {
            return
        }
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding.recyclerView.adapter = epAdapter
        }
        mCurrentRouteIndex = routeIndex
        epAdapter.setList(epBeans)
        //
        if (mCurrentEpIndex >= 0 && mCurrentEpIndex < epBeans.size) {
            val epBean = epBeans[mCurrentEpIndex]
            clickEp(epBean, detailPlayer.currentPositionWhenPlaying)
            epAdapter.checkPos = mCurrentEpIndex
            epAdapter.notifyDataSetChanged()
        } else {
            mCurrentEpIndex = 0
            val epBean = epBeans[0]
            clickEp(epBean, detailPlayer.currentPositionWhenPlaying)
            epAdapter.checkPos = 0
            epAdapter.notifyDataSetChanged()
        }
    }

    private val epAdapter by lazy {
        EPAdapter() { item, pos ->
            if (pos == mCurrentEpIndex) {
                return@EPAdapter
            }
            mCurrentEpIndex = pos
            clickEp(item, 0)
        }
    }

    private fun clickEp(epBean: EpBean, playTime: Long) {
        Log.d(TAG, "clickEp() called with: epBean = $epBean, playTime = $playTime")
        mCurrentProgress = playTime
        Log.d(TAG, "clickEp: setUp=${epBean.epUrl}")
        detailPlayer.setUp(
            epBean.epUrl, true,
            epBean.name + "," + epBean.epName
        )
        // 添加准备完成的监听
        detailPlayer.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                // 在准备完成后跳转
                detailPlayer.seekTo(playTime)
                detailPlayer.startAfterPrepared() // 准备完成后自动开始播放
            }
        })
//        detailPlayer.seekTo(playTime)
//        detailPlayer.seekOnStart = playTime // 设置开始位置
        detailPlayer.startPlayLogic()



        // 方式2：使用Glide加载网络图片
        val coverImageView = ImageView(this)
        Glide.with(this)
            .load(epBean.cover)
            .into(coverImageView)
        detailPlayer.thumbImageView = coverImageView
    }

    //route索引
    private var mCurrentRouteIndex = 0
    private var mCurrentProgress = 0L
    private var mCurrentEpIndex = 0
    private fun initRouteRv() {
        binding.routeRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.routeRecyclerView.adapter = routeAdapter
        routeAdapter.checkIndex = mCurrentRouteIndex
        routeAdapter.setList(routeIndexList)
    }

    //key为1,value剧集
    private val routeMap = HashMap<Int, List<EpBean>>()
    private fun fetchBeanList(index: Int, playUrl: String) {
        val arr = playUrl.split("\r\n")
        if (arr.isNullOrEmpty()) {
            return
        }
        val list = arrayListOf<EpBean>()
        for (urlF in arr) {
            val arr = urlF.split("@@@")
            if (arr.isNullOrEmpty() || arr.size != 2) {
                continue
            }
            val bean = EpBean()
            bean.epName = arr[0]
            bean.epUrl = arr[1]
            bean.name = mMovie?.playName
            bean.routeIndex = index
            bean.cover = mMovie?.cover
            list.add(bean)
        }
        routeMap[index] = list
    }


    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer {
        return detailPlayer
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        return GSYVideoOptionBuilder()
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
//            .setSeekRatio(1f)
            // .setIsTouchWiget(true)                    // 是否支持手势操作
            //        .setRotateViewAuto(false)                 // 是否开启自动旋转
            //        .setLockLand(false)                       // 是否一直横屏
//                .setShowFullAnimation(false)              // 是否显示全屏动画
//                .setNeedLockFull(true)                    // 是否需要全屏锁定
            //        .setSeekRatio(1f)                         // 设置滑动快进的比例
            //        .setVideoAllCallBack(videoCallBack)       // 设置播放器回调
//                .setLooping(false)                        // 是否循环播放
            //        .setAutoFullWithSize(true)                // 是否根据视频尺寸自动确定全屏
            .setDismissControlTime(5000)              // 控制器隐藏时间
//                .setShowPauseCover(true)                  // 是否显示暂停时的封面
        //        .setRotateWithSystem(false)
    }

    override fun clickForFullScreen() {
    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return false
    }

    private fun convert(pageBean: PageBean): HistoryPageBean {
        val historyPageBean = HistoryPageBean()
        historyPageBean.id = pageBean.id
        historyPageBean.playName = pageBean.playName
        historyPageBean.cover = pageBean.cover
        historyPageBean.playDesInfo = pageBean.playDesInfo
        return historyPageBean
    }

    override fun onPause() {
        super.onPause()
        //
        if (mMovie != null) {
            val progress = detailPlayer.getCurrentPositionWhenPlaying()
            Log.d(TAG, "onPause: progress=$progress")
            mMovie?.let {
                val historyPageBean = convert(it)
                historyPageBean.playDesInfo = null
                historyPageBean.historySource = mCurrentRouteIndex
                historyPageBean.historyEpIndex = mCurrentEpIndex
                historyPageBean.historyProgress = progress
                historyPageBean.time = Calendar.getInstance().timeInMillis / 1000L
                historyPageBean.id = mId
                HistoryUtils.insert(historyPageBean)
            }
        }
    }


    private val mHandler = Handler(Looper.getMainLooper())
    private fun getFromHistory(ok: (HistoryPageBean?) -> Unit) {
        // 创建一个 Observable，用于模拟耗时操作
        val observable: Observable<HistoryPageBean> = Observable.create { emitter ->
            // 在这里进行一些耗时操作，比如网络请求
            try {
                val query = HistoryUtils.query(mId)
                if (query == null) {

                    emitter.onError(Exception("null"))
                    return@create
                }
                emitter.onNext(query)
//                Thread.sleep(2000) // 模拟耗时操作
//                emitter.onNext("获取的数据")
//                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

        // 订阅 Observable

        // 订阅 Observable
        disposable = observable
            .subscribeOn(Schedulers.io()) // 在 IO 线程执行任务
            .observeOn(AndroidSchedulers.mainThread()) // 在主线程更新 UI
            .subscribe(
                { data ->
                    ok(data)
                }
            ) { throwable ->
                // 处理错误
                ok(null)
            }
    }

    private fun queryMovieList(
        id: Long,
        success: (PageBean) -> Unit,
        fail: (Int) -> Unit
    ) {

        Log.d(TAG, "queryMovieList:id=$id")
        val bean = MovieInfoRequest()
        bean.mID = id
        HttpClient.instance.getServer(NetApi::class.java)
            .movieInfo(bean)
            .enqueue(object : Callback<MovieInfoResponse> {
                override fun onResponse(
                    call: Call<MovieInfoResponse>,
                    response: Response<MovieInfoResponse>
                ) {
                    Log.d(TAG, "onResponse: ")
                    if (!response.isSuccessful) {
                        mHandler.post {
                            fail(100)
                        }
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        mHandler.post {
                            fail(101)
                        }
                        return
                    }
                    //
                    if (body.res == null) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    mHandler.post {
                        success(body.res!!)
                    }
                }

                override fun onFailure(call: Call<MovieInfoResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: t=" + t.message)
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }

    private fun showDesContent() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        mMovie?.let {
            bottomSheetView.findViewById<TextView>(R.id.name).text = it.playName
            bottomSheetView.findViewById<TextView>(R.id.actor).text = it.playActor
            bottomSheetView.findViewById<TextView>(R.id.director).text = it.playDirector
            bottomSheetView.findViewById<TextView>(R.id.year).text = it.playYear
            bottomSheetView.findViewById<TextView>(R.id.area).text = it.playArea
            bottomSheetView.findViewById<TextView>(R.id.des).text = it.playDesInfo
        }
        // 设置按钮点击事件
        bottomSheetView.findViewById<View>(R.id.close).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        // 显示BottomSheetDialog
        bottomSheetDialog.show()
    }

    private fun xuanJi() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_episode_list, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetView.findViewById<View>(R.id.ivClose)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        val adapter1 = EpisodeAdapter(5) { position, item ->
            Log.d(TAG, "showDesContent() called with: position = $position,item=$item")

            if (position == mCurrentEpIndex) {
                return@EpisodeAdapter
            }
            mCurrentEpIndex = position
            clickEp(item, 0)
            epAdapter.checkPos = mCurrentEpIndex
            epAdapter.notifyDataSetChanged()
        }

        bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)?.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = adapter1
            addItemDecoration(GradleItemDecord())
        }
        adapter1.selectedPosition = mCurrentEpIndex
        // 生成集数列表
        val epBean = routeMap[mCurrentRouteIndex]
        epBean?.let { adapter1.updateData(it) }
        bottomSheetDialog.show()
    }

}