package com.exp.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.exp.post.bean.MovieInfoRequest
import com.exp.post.bean.MovieInfoResponse
import com.exp.post.bean.MovieListRequest
import com.exp.post.bean.MovieListResponse
import com.exp.post.bean.PageBean
import com.exp.post.databinding.ActivityMovieDetailBinding
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.ui.home.ShowFragment
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
            val info = when(topClass){
                1->"电影"
                2->"电视剧"
                3->"动漫"
                4->"综艺"
                else->"纪录片"
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
    }
    private fun requestMovie() {
        queryMovieList(mId, {
            mMovie = it
            Log.d(TAG, "requestMovie: mMovie=$mMovie")
            initMovieUI()
        }, {
            //todo
        })
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

}