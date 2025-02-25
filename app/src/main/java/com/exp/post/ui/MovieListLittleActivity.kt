package com.exp.post.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.exp.post.MovieDetailActivity
import com.exp.post.adapter.MovieListAdapter
import com.exp.post.adapter.MovieListContentAdapter
import com.exp.post.bean.MovieListBean
import com.exp.post.bean.MovieListLittleRequest
import com.exp.post.bean.MovieListLittleResponse
import com.exp.post.dbs.PageBean
import com.exp.post.databinding.ActivityMovieListLittleBinding
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.AndroidUtils
import com.exp.post.ui.home.ShowFragment
import com.jaeger.library.StatusBarUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class MovieListLittleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListLittleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListLittleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        StatusBarUtil.setLightMode(this)
        supportActionBar?.hide()
        initView()
    }

    companion object {
        fun navMovieListActivity(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, MovieListLittleActivity::class.java))
        }

        const val TAG = "MovieListActivity"
    }

    private var pos1 = 0

    //    private var pos2 = 0
//    private var pos3 = 0
    private var pos4 = 0
    private var littleType = 0

    //    private var movieModle: String? = null
//    private var areaModle: String? = null
    private var yearModle: String? = null
    private val adapter1 by lazy {
        MovieListAdapter {
            Log.d(TAG, "adapter1() called it.pos=${it.id},pre=$pos1")
            val id = it.id
            if (id == pos1) {
                return@MovieListAdapter
            }
            littleType = id
            pos1 = id
            //reequest
            postNet(false)
        }
    }

    private val adapter4 by lazy {
        MovieListAdapter {
            Log.d(TAG, "adapter4() called it.pos=${it.id}")
            val id = it.id
            if (id == pos4) {
                return@MovieListAdapter
            }
            yearModle = when (id) {
                0 -> {
                    null
                }

                else -> {
                    if (it.name == "更早") {
                        "2000"
                    } else {
                        it.name
                    }
                }
            }
            Log.d(
                TAG,
                "adapter4() called yearModle=${yearModle}"
            )
            pos4 = id
            postNet(false)
        }
    }
    private val adapter by lazy {
        MovieListContentAdapter {
            MovieDetailActivity.nav(this, it.id)
        }
    }

    private fun initView(loadMore: Boolean, list: List<PageBean>?, noMore: Boolean) {
        if (binding.recyclerView.adapter == null) {
            val layoutManager = GridLayoutManager(this, 3)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
            binding.recyclerView.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val gridLayoutManager = parent.layoutManager as GridLayoutManager
                    val span = gridLayoutManager.spanCount
                    val pos = parent.getChildLayoutPosition(view)
                    val col = pos % span
                    outRect.left = AndroidUtils.dp2px(15f) - col * AndroidUtils.dp2px(15f) / span
                    outRect.right = (col + 1) * AndroidUtils.dp2px(15f) / span
                    Log.d(
                        TAG,
                        "getItemOffsets: left=${outRect.left},right=${outRect.right},pos=$pos,col=$col"
                    )
                }
            })
        }
        if (loadMore) {
            adapter.addData(list!!)
        } else {
            adapter.setList(list)

        }
        if (loadMore) {
            if (noMore) {
                adapter.loadMoreModule.loadMoreEnd()

            } else {
                adapter.loadMoreModule.loadMoreComplete()

            }
        } else {

        }

    }

    private var page = 1
    private fun postNet(loadMore: Boolean) {
        if (loadMore) {
            page++
            hideLoading()

        } else {
            page = 1
            showLoading()
        }

        queryMovieList(page, { list, noMore ->
            hideLoading()
            initView(loadMore, list, noMore)
        }, {
            hideLoading()
            if (loadMore) {
                adapter.loadMoreModule.loadMoreFail()
            } else {
                //首页失败
            }
        })
    }

    private val listTitle1 by lazy {
        arrayListOf("全部", "都市", "爽文", "反转", "恋爱", "仙侠", "穿越", "悬疑")
    }
    private val listTitle4 by lazy {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val list = arrayListOf("年份")
        for (index in year downTo 2009) {
            list.add(index.toString())
        }
        list.add("之前")
        list
    }

    private fun initView() {
        binding.backFl.setOnClickListener {
            finish()

        }
        adapter.loadMoreModule.setOnLoadMoreListener {
            postNet(true)
        }
        initView1()
//        initView2()
//        initView3()
        initView4()
        postNet(false)
    }

    private fun initView1() {
        binding.recycler1.adapter = adapter1
        binding.recycler1.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val list1 = arrayListOf<MovieListBean>()
        listTitle1.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 1
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        adapter1.setList(list1)
    }

    private fun initView4() {
        binding.recycler4.adapter = adapter4
        binding.recycler4.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val list1 = arrayListOf<MovieListBean>()
        listTitle4.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 4
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        adapter4.setList(list1)
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun queryMovieList(
        pg: Int,
        success: (List<PageBean>?, noMore: Boolean) -> Unit,
        fail: (Int) -> Unit
    ) {
//        if (topC == "0") {
//            topC = "1"
//        }
        Log.d(TAG, "queryMovieList: yearModle=$yearModle,")
        val bean = MovieListLittleRequest()
        bean.pg = pg
        bean.littleType = littleType
        bean.yearModle = yearModle
        HttpClient.instance.getServer(NetApi::class.java)
            .queryMovieListLittle(bean)
            .enqueue(object : Callback<MovieListLittleResponse> {
                override fun onResponse(
                    call: Call<MovieListLittleResponse>,
                    response: Response<MovieListLittleResponse>
                ) {
                    Log.d(TAG, "onResponse() called with: call = $call, response = $response")
                    if (!response.isSuccessful) {
                        mHandler.post {
                            fail(100)
                        }
                        return
                    }
                    Log.d(TAG, "onResponse() called11 with: call = $call, response = $response")
                    val body = response.body()
                    if (body == null) {
                        mHandler.post {
                            fail(101)
                        }
                        return
                    }
                    Log.d(TAG, "onResponse() called with: call = $call, body = $body")
                    //
                    if (body.res == null) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    mHandler.post {
                        success(body.res!!, body.res!!.isEmpty())
                    }
                }

                override fun onFailure(call: Call<MovieListLittleResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: t=" + t.message)
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }

    private fun showLoading() {
        binding.loadingViewContainer.setVisibility(View.VISIBLE)
    }

    private fun hideLoading() {
        binding.loadingViewContainer.setVisibility(View.GONE)
    }
}
