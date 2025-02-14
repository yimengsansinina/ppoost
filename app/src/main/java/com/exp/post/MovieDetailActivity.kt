package com.exp.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
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
import com.exp.post.wt.MJzvd
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class MovieDetailActivity : AppCompatActivity() {

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


    private val mVideoView by lazy { findViewById<MJzvd>(R.id.jz_video)!! }
    private fun initView() {
        if (mId == 0L) {
            return
        }
        mVideoView.setPreparedListener(object : MJzvd.IPreparedListener {
            override fun onPrepared() {
                Log.d(TAG, "onPrepared() called,mCurrentProgress=$mCurrentProgress")
                mVideoView.mediaInterface.seekTo(mCurrentProgress)
            }

            override fun onFinish() {
                //播放下一個
                playNext()
                Log.d(TAG, "onFinish: ")
            }
        })
        binding.backTiny.setOnClickListener {
            finish()
        }
        requestMovie()
//        val jzvdStd: JzvdStd = findViewById<View>(R.id.jz_video) as JzvdStd
//        jzvdStd.setUp(
//            "https://s5.bfengbf.com/video/tunxiayuzhoudenanhai/第05集/index.m3u8",
//            "饺子闭眼睛"
//        )
//        jzvdStd.posterImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"))

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
        Log.d(TAG, "initRecycler() playList=$playList")
        if (playList.isNullOrEmpty()) {
            return
        }
        playList.forEachIndexed { index, s ->
            if (!TextUtils.isEmpty(s)) {
                fetchBeanList(index, s)
                routeList.add(index)
            }
        }
        if (!routeList.contains(mCurrentRouteIndex)) {
            if (routeList.isNotEmpty()){
                mCurrentRouteIndex=routeList[0]
            }
        }

        Log.d(TAG, "initRecycler: routeList=$routeList")
        initRouteRv()
        val bean = initEpRv()
        bean?.run {
            val sec = mMovie?.historyProgress ?: 0L
            Log.d(TAG, "initRecycler: sec=$sec")
            clickEp(this, sec)
        }
    }

    private fun initEpRv(): EpBean? {
        if (mCurrentRouteIndex > routeMap.size - 1 || mCurrentRouteIndex < 0) {
            return null
        }
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

    private val routeList by lazy {
        arrayListOf<Int>()
    }
    private val routeAdapter by lazy {
        RouteAdapter {
            clickRoute(it)
        }
    }

    private fun clickRoute(pos: Int) {
        if (pos == mCurrentRouteIndex) {
            return
        }
//        if (pos > routeMap.size - 1 || pos < 0) {
//            return
//        }
        val epBeans = routeMap[pos]
        if (epBeans == null) {
            return
        }
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding.recyclerView.adapter = epAdapter
        }
        mCurrentRouteIndex = pos
        epAdapter.setList(epBeans)
        //
        if (mCurrentEpIndex >= 0 && mCurrentEpIndex < epBeans.size) {
            val epBean = epBeans[mCurrentEpIndex]
            clickEp(epBean, mVideoView.currentPositionWhenPlaying)
            epAdapter.checkPos = mCurrentEpIndex
            epAdapter.notifyDataSetChanged()
        } else {
            mCurrentEpIndex = 0
            val epBean = epBeans[0]
            clickEp(epBean, mVideoView.currentPositionWhenPlaying)
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
        mCurrentProgress = playTime
        mVideoView.setUp(
            epBean.epUrl,
            epBean.name + "," + epBean.epName
        )
        mVideoView.posterImageView.setImageURI(Uri.parse(epBean.cover))
        mVideoView.startVideo()
    }

    private var mCurrentRouteIndex = 0
    private var mCurrentProgress = 0L
    private var mCurrentEpIndex = 0
    private fun initRouteRv() {
        binding.routeRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.routeRecyclerView.adapter = routeAdapter
        routeAdapter.checkIndex = mCurrentRouteIndex
        routeAdapter.setList(routeList)
    }


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

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
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
            val progress = mVideoView.getCurrentPositionWhenPlaying()
            Log.d(TAG, "onPause: progress=$progress")
            mMovie?.let {
                val historyPageBean = convert(it)
                historyPageBean.playDesInfo=null
                historyPageBean.historySource = mCurrentRouteIndex
                historyPageBean.historyEpIndex = mCurrentEpIndex
                historyPageBean.historyProgress = progress
                historyPageBean.time = Calendar.getInstance().timeInMillis / 1000L
                historyPageBean.id = mId
                HistoryUtils.insert(historyPageBean)
            }
        }
        Jzvd.releaseAllVideos()
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

        val adapter1 = EpisodeAdapter(5) { position,item ->
            Log.d(TAG, "showDesContent() called with: position = $position,item=$item")

            if (position == mCurrentEpIndex) {
               return@EpisodeAdapter
            }
            mCurrentEpIndex = position
            clickEp(item, 0)
        }

        bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)?.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = adapter1
            addItemDecoration(GradleItemDecord())
        }

        // 生成集数列表
        if (mCurrentRouteIndex >= 0 && mCurrentRouteIndex < routeMap.size) {
            val epBean = routeMap[mCurrentRouteIndex]
            epBean?.let { adapter1.updateData(it) }
        }
        bottomSheetDialog.show()
    }

}