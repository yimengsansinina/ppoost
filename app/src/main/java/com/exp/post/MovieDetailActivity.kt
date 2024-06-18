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
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.exp.post.adapter.EPAdapter
import com.exp.post.adapter.RouteAdapter
import com.exp.post.bean.EpBean
import com.exp.post.bean.MovieInfoRequest
import com.exp.post.bean.MovieInfoResponse
import com.exp.post.bean.MovieListRequest
import com.exp.post.bean.MovieListResponse
import com.exp.post.bean.PageBean
import com.exp.post.databinding.ActivityMovieDetailBinding
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.ui.home.ShowFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initView()
    }

    private val mId by lazy {
        intent?.getIntExtra("id", 0) ?: 0
    }

    companion object {
        const val TAG = "MovieDetailActivity"
        fun nav(activity: AppCompatActivity, id: Int) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
        }
    }

    private fun initView() {
        if (mId == 0) {
            return
        }
        requestMovie()
//        val jzvdStd: JzvdStd = findViewById<View>(R.id.jz_video) as JzvdStd
//        jzvdStd.setUp(
//            "https://s5.bfengbf.com/video/tunxiayuzhoudenanhai/第05集/index.m3u8",
//            "饺子闭眼睛"
//        )
//        jzvdStd.posterImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"))

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
        }
        binding.desc.setOnClickListener {
            showDesContent()
        }
    }

    private fun requestMovie() {
        queryMovieList(mId, {
            mMovie = it
            Log.d(TAG, "requestMovie: mMovie=$mMovie")
            initMovieUI()
            initRecycler()
        }, {
            //todo
        })
    }

    //private val map =ArrayMap<Int>
    private fun initRecycler() {
        if (mMovie == null) {
            return
        }
        val playList = mMovie!!.playList
        if (playList.isNullOrEmpty()) {
            return
        }
        playList.forEachIndexed { index, s ->
            if (!TextUtils.isEmpty(s)) {
                fetchBeanList(index, s)
                routeList.add(index)
            }
        }
        initRouteRv()
        initEpRv()
    }
    private fun initEpRv() {
        val epBean= routeMap[mCurrentRouteIndex]
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = epAdapter
        epAdapter.setList(epBean)

    }

    private val routeList by lazy {
        arrayListOf<Int>()
    }
    private val routeAdapter by lazy {
        RouteAdapter {

        }
    }
    private val epAdapter by lazy {
        EPAdapter {

        }
    }
private var mCurrentRouteIndex =0
    private fun initRouteRv() {
        binding.routeRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.routeRecyclerView.adapter = routeAdapter
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
            val arr = urlF.split("$")
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

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


    private val mHandler = Handler(Looper.getMainLooper())
    private fun queryMovieList(
        id: Int,
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
                    Log.d(ShowFragment.TAG, "onFailure: t=" + t.message)
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

}