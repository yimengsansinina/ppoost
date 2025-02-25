package com.exp.post

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
import com.exp.post.adapter.MovieListAdapter
import com.exp.post.adapter.MovieListContentAdapter
import com.exp.post.bean.MovieListBean
import com.exp.post.bean.MovieListRequest
import com.exp.post.bean.MovieListResponse
import com.exp.post.dbs.PageBean
import com.exp.post.databinding.ActivityMovieListBinding
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.AndroidUtils
import com.exp.post.tools.SPTools
import com.exp.post.ui.home.ShowFragment
import com.jaeger.library.StatusBarUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        StatusBarUtil.setLightMode(this)
        supportActionBar?.hide()
        initView()
    }

    companion object {
        fun navMovieListActivity(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, MovieListActivity::class.java))
        }

        const val TAG = "MovieListActivity"
    }

    private var pos1 = 0
    private var pos2 = 0
    private var pos3 = 0
    private var pos4 = 0
    private var topC = "1"
    private var movieModle: String? = null
    private var areaModle: String? = null
    private var yearModle: String? = null
    private val adapter1 by lazy {
        MovieListAdapter {
            Log.d(TAG, "adapter1() called it.pos=${it.id},pre=$pos1")
            val id = it.id
            if (id == pos1) {
                return@MovieListAdapter
            }
            topC = id.toString()
            adapter2.checkPos = 0
            movieModle = null
            when (id) {
                0 -> {
                    adapter2.setList(list2_1)
                }

                1 -> {
                    adapter2.setList(list2_1)
                }

                2 -> {
                    adapter2.setList(list2_2)
                }

                3 -> {
                    adapter2.setList(list2_3)
                }

                4 -> {
                    adapter2.setList(list2_4)
                }

                else -> {
                    adapter2.setList(list2_5)
                }
            }
            pos1 = id
            //
            //reequest
            postNet(false)
        }
    }

    private val adapter2 by lazy {
        MovieListAdapter {
            Log.d(TAG, "adapter2() called it.pos=${it.id}")
            val id = it.id
            if (id == pos2) {
                return@MovieListAdapter
            }
            pos2 = id
            movieModle = when (pos1) {
                0, 1 -> {
                    when (id) {
                        0 -> {
                            null
                        }

                        1 -> {
                            "121"
                        }

                        2 -> {
                            "122"
                        }

                        3 -> {
                            "123"
                        }

                        4 -> {
                            "124"
                        }

                        5 -> {
                            "125"
                        }

                        6 -> {
                            "126"
                        }

                        7 -> {
                            "127"
                        }

                        8 -> {
                            "157"
                        }

                        else -> {
                            "157"
                        }
                    }
                }

                2 -> {
                    when (id) {
                        0 -> {
                            null
                        }

                        1 -> {
                            "128"
                        }

                        2 -> {
                            "129"
                        }

                        3 -> {
                            "130"
                        }

                        4 -> {
                            "131"
                        }

                        5 -> {
                            "132"
                        }

                        6 -> {
                            "133"
                        }

                        7 -> {
                            "134"
                        }

                        8 -> {
                            "170"
                        }

                        else -> {
                            "170"
                        }
                    }
                }

                3 -> {
                    when (id) {
                        0 -> {
                            null
                        }

                        1 -> {
                            "182"
                        }

                        2 -> {
                            "183"
                        }

                        3 -> {
                            "184"
                        }

                        4 -> {
                            "185"
                        }

                        5 -> {
                            "186"
                        }

                        else -> {
                            "186"
                        }
                    }
                }

                4 -> {
                    when (id) {
                        0 -> {
                            null
                        }

                        1 -> {
                            "178"
                        }

                        2 -> {
                            "179"
                        }

                        3 -> {
                            "180"
                        }

                        4 -> {
                            "181"
                        }

                        else -> {
                            "181"
                        }
                    }
                }

                5 -> {
                    "136"
                }

                else -> {
                    "121"
                }
            }
            Log.d(
                TAG,
                "adapter2() called movieModle=$movieModle,areaModle=${areaModle},yearModle=${yearModle}"
            )
            postNet(false)
        }
    }
    private val adapter3 by lazy {
        MovieListAdapter {
            Log.d(TAG, "adapter3() called it.pos=${it.id}")
            val id = it.id
            if (id == pos3) {
                return@MovieListAdapter
            }
            areaModle = when (id) {
                0 -> {
                    null
                }

                else -> {
                    id.toString()
                }
            }
            Log.d(
                TAG,
                "adapter3() called movieModle=$movieModle,areaModle=${areaModle},yearModle=${yearModle}"
            )
            pos3 = id
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
                "adapter4() called movieModle=$movieModle,areaModle=${areaModle},yearModle=${yearModle}"
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
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("all", "mo", "tv", "dm", "zy", "record")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[0]
            arrayListOf(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4], arr1[5])
        }
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
    private val listTitle2_1 by lazy {
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("all", "dz", "xj", "love", "kh", "kb","jq","zz","dh")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[1]
            arrayListOf(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4], arr1[5], arr1[6], arr1[7], arr1[8])
        }
    }
    private val listTitle2_2 by lazy {
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("all", "gc", "xg", "tw", "rb", "hj","om","tg","hw")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[2]
            arrayListOf(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4], arr1[5], arr1[6], arr1[7], arr1[8])
        }
    }

    private val listTitle2_3 by lazy {
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("all", "gcdm", "rhdm", "omdm", "gtdm","hwdm")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[3]
            arrayListOf(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4], arr1[5])
        }
    }
    private val listTitle2_4 by lazy {
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("all", "dlzy", "gtzy", "rhzy", "omzy")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[4]
            arrayListOf(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4])
        }
    }
    private val listTitle2_5 by lazy {
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("lx", "jlp")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[5]
            arrayListOf(arr1[0], arr1[1])
        }
    }
    private val listTitle3 by lazy {
        if (SPTools.getListInfoTextArr().isEmpty()) {
            arrayListOf("dq", "dl", "xg", "tw", "rb","hg","mg","yg","fg","tg","yd")
        } else {
            val arr1 = SPTools.getListInfoTextArr()[6]
            arrayListOf(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4], arr1[5], arr1[6], arr1[7], arr1[8], arr1[9], arr1[10])
        }
    }

    private fun initView() {
        binding.backFl.setOnClickListener {
            finish()

        }
        adapter.loadMoreModule.setOnLoadMoreListener {
            postNet(true)
        }
        initView1()
        initView2()
        initView3()
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

    private val list2_1 by lazy {
        val list1 = arrayListOf<MovieListBean>()
        listTitle2_1.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 2
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        list1
    }
    private val list2_2 by lazy {
        val list1 = arrayListOf<MovieListBean>()
        listTitle2_2.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 2
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        list1
    }
    private val list2_3 by lazy {
        val list1 = arrayListOf<MovieListBean>()
        listTitle2_3.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 2
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        list1
    }
    private val list2_4 by lazy {
        val list1 = arrayListOf<MovieListBean>()
        listTitle2_4.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 2
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        list1
    }
    private val list2_5 by lazy {
        val list1 = arrayListOf<MovieListBean>()
        listTitle2_5.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 2
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        list1
    }

    private fun initView2() {
        binding.recycler2.adapter = adapter2
        binding.recycler2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter2.setList(list2_1)
    }


    private fun initView3() {
        binding.recycler3.adapter = adapter3
        binding.recycler3.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val list1 = arrayListOf<MovieListBean>()
        listTitle3.forEachIndexed { index, s ->
            val listBean = MovieListBean()
            listBean.type = 3
            listBean.name = s
            listBean.id = index
            list1.add(listBean)
        }
        adapter3.setList(list1)
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
        if (topC == "0") {
            topC = "1"
        }
        Log.d(TAG, "queryMovieList: movieModle=$movieModle")
        val bean = MovieListRequest()
        bean.pg = pg

        bean.topC = topC
        bean.movieModle = movieModle
        bean.areaModle = areaModle
        bean.yearModle = yearModle
        HttpClient.instance.getServer(NetApi::class.java)
            .queryMovieList(bean)
            .enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
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
                        success(body.res!!, body.res!!.isEmpty())
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    Log.d(ShowFragment.TAG, "onFailure: t=" + t.message)
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
