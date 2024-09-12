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


class WatchHistoryActivity : AppCompatActivity() {
    companion object {
        const val TAG = "WatchHistoryActivity"
        fun nav(activity: FragmentActivity) {
            val intent = Intent(activity, WatchHistoryActivity::class.java)
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

//        val users: List<User> = getNewUsers()
//        userBox.put(users)
//        val user = userBox[userId]
//
//        val users = userBox.all
//        val query = userBox
//            .query(User_.name.equal("fffff"))
//            .order(User_.name)
//            .build()
//        val results = query.find()
//        Log.d(TAG, "onCreate: results=$results")
//        query.close()

//        val isRemoved = userBox.remove(userId)
//
//        userBox.remove(users)
//// alternatively:
//        userBox.removeByIds(userIds)
//
//        userBox.removeAll()
        //val userCount = userBox.count()
//        store.callInTxAsync(object :Callable<String>{
//            override fun call(): String {
//               val box = ObjectBox.store.boxFor(User::class.java)
//                val name = box.get(2).name;
//                box.remove(1);
//                return "text";
//            }
//
//        }) { result, error ->
//            if (error != null) {
//                System.out.println("Failed to remove user with id " + "userId");
//            } else {
//                System.out.println("Removed user with name: " + result);
//            }
//        }

    }


}