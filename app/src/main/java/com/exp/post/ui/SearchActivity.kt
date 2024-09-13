package com.exp.post.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exp.post.MovieDetailActivity
import com.exp.post.adapter.WatchHistoryAdapter
import com.exp.post.databinding.ActivityWatchHistoryBinding
import com.exp.post.dbs.HistoryPageBean
import com.exp.post.dbs.HistoryUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class SearchActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SearchActivity"
        fun nav(activity: FragmentActivity) {
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private var disposable: Disposable?=null
    private lateinit var binding: ActivityWatchHistoryBinding
    private val adapter by lazy {
        WatchHistoryAdapter {
            gotoDetail(it)
        }
    }

    private fun gotoDetail(pageBean: HistoryPageBean) {
        MovieDetailActivity.nav(this,pageBean.id)
        finish()
    }
    private fun fetchLocalData(ok: (List<HistoryPageBean>?) -> Unit) {
        // 创建一个 Observable，用于模拟耗时操作
        val observable: Observable<List<HistoryPageBean>> = Observable.create { emitter ->
            // 在这里进行一些耗时操作，比如网络请求
            try {
                val query = HistoryUtils.selectAll()
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWatchHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        HistoryUtils.selectAll()
        fetchLocalData {
            Log.d(TAG, "onCreate() called,it=${it?.size}")
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            it.let {list->
                adapter.setList(list)
            }
        }
        binding.backFl.setOnClickListener { finish() }
    }
}