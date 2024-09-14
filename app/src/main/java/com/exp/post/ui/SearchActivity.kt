package com.exp.post.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.post.MovieDetailActivity
import com.exp.post.MovieListActivity
import com.exp.post.adapter.SearchHotAdapter
import com.exp.post.adapter.SearchResultAdapter
import com.exp.post.bean.MaxSearchResponse
import com.exp.post.bean.MovieListRequest
import com.exp.post.bean.MovieListResponse
import com.exp.post.bean.SearchResultResponse
import com.exp.post.databinding.ActivitySearchBinding
import com.exp.post.dbs.HistoryBean
import com.exp.post.dbs.HistoryPageBean
import com.exp.post.dbs.HistoryWordUtils
import com.exp.post.dbs.PageBean
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.AndroidUtils
import com.exp.post.ui.home.ShowFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SearchActivity"
        fun nav(activity: FragmentActivity) {
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private var disposable: Disposable? = null
    private lateinit var binding: ActivitySearchBinding
    private val hotAdapter by lazy {
        SearchHotAdapter {
            binding.recyclerView.visibility = View.VISIBLE
            binding.searchEt.setText(it)
            postNet(it, false)
        }
    }
    private val resultAdapter by lazy {
        SearchResultAdapter {
            MovieDetailActivity.nav(this, it.id)
        }
    }


    private fun gotoDetail(pageBean: HistoryPageBean) {
        MovieDetailActivity.nav(this, pageBean.id)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        fetchLocalData {
            Log.d(TAG, "onCreate() called,it=${it?.size}")
            it?.let { list ->
                val listT = list.map { bean -> bean.key }
                binding.tagGroup.setTags(listT)
            }
//            binding.recyclerView.adapter = adapter
//            binding.recyclerView.layoutManager = LinearLayoutManager(this)
//            it.let {list->
//                adapter.setList(list)
//            }
        }
        binding.backFl.setOnClickListener { finish() }
        initView()
        queryMovieHot({
            Log.d(TAG, "onCreate() called,t=$it")
            if (binding.hotRv.adapter == null) {
                binding.hotRv.adapter = hotAdapter
                binding.hotRv.layoutManager = LinearLayoutManager(this)
            }
            hotAdapter.setList(it)
        }, {
            Log.d(TAG, "onCreate: it=$it")
        })
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun queryMovieHot(
        success: (List<String>?) -> Unit,
        fail: (Int) -> Unit
    ) {

        Log.d(TAG, "queryMovieList")
//        val bean = MovieInfoRequest()
//        bean.mID = id
        HttpClient.instance.getServer(NetApi::class.java)
            .maxSearchWords()
            .enqueue(object : Callback<MaxSearchResponse> {
                override fun onResponse(
                    call: Call<MaxSearchResponse>,
                    response: Response<MaxSearchResponse>
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

                override fun onFailure(call: Call<MaxSearchResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: t=" + t.message)
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }

    private fun initView() {
        binding.clearView.setOnClickListener {
            binding.searchEt.setText("")
            binding.recyclerView.visibility = View.INVISIBLE

        }
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "afterTextChanged: ")
                val trim = binding.searchEt.text.trim()
                if (TextUtils.isEmpty(trim)) {
                    binding.clearView.visibility = View.INVISIBLE
                } else {
                    binding.clearView.visibility = View.VISIBLE
                }

            }
        })
        binding.searchBt.setOnClickListener {
            Log.d(TAG, "initView: searchBt")
            val trim = binding.searchEt.text.trim()
            if (TextUtils.isEmpty(trim)) {
                Toast.makeText(this, "搜索內容為空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            postNet(trim.toString(), false)
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.loadingViewContainer.setVisibility(View.VISIBLE)
    }

    private fun hideLoading() {
        binding.loadingViewContainer.setVisibility(View.GONE)
    }

    private var page = 1
    private fun postNet(key: String, loadMore: Boolean) {
        if (loadMore) {
            page++
            hideLoading()
        } else {
            page = 1
            showLoading()
        }
        queryMovieList(key, page, { list, noMore ->
            hideLoading()
            initView(loadMore, list, noMore)
        }, {
            hideLoading()
            if (loadMore) {
                resultAdapter.loadMoreModule.loadMoreFail()
            } else {
                //首页失败
            }
        })
    }

    private fun initView(loadMore: Boolean, list: List<PageBean>?, noMore: Boolean) {
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = resultAdapter
        }
        if (loadMore) {
            resultAdapter.addData(list!!)
        } else {
            resultAdapter.setList(list)

        }
        if (loadMore) {
            if (noMore) {
                resultAdapter.loadMoreModule.loadMoreEnd()

            } else {
                resultAdapter.loadMoreModule.loadMoreComplete()

            }
        } else {

        }

    }

    private fun queryMovieList(
        key: String,
        pg: Int,
        success: (List<PageBean>?, noMore: Boolean) -> Unit,
        fail: (Int) -> Unit
    ) {
        Log.d(TAG, "queryMovieList: key=$key,pg=$pg")
        HttpClient.instance.getServer(NetApi::class.java)
            .searchByKeyWords(pg, key)
            .enqueue(object : Callback<SearchResultResponse> {
                override fun onResponse(
                    call: Call<SearchResultResponse>,
                    response: Response<SearchResultResponse>
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
                    Log.d(TAG, "onResponse: res=${body.res}")
                    mHandler.post {
                        success(body.res!!, body.res!!.isEmpty())
                    }
                }

                override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                    Log.d(ShowFragment.TAG, "onFailure: t=" + t.message)
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }


    private fun fetchLocalData(ok: (List<HistoryBean>?) -> Unit) {
        // 创建一个 Observable，用于模拟耗时操作
        val observable: Observable<List<HistoryBean>> = Observable.create { emitter ->
            // 在这里进行一些耗时操作，比如网络请求
            try {
                val query = HistoryWordUtils.selectAll()
                if (query == null) {
                    emitter.onError(Exception("null"))
                    return@create
                }
                emitter.onNext(query)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
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
}