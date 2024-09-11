package com.exp.post.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exp.post.MovieDetailActivity
import com.exp.post.adapter.WatchHistoryAdapter
import com.exp.post.dbs.PageBean
import com.exp.post.databinding.ActivityWatchHistoryBinding
import com.exp.post.dbs.ObjectBox
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class WatchHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchHistoryBinding
    private val adapter by lazy {
        WatchHistoryAdapter {
            gotoDetail(it)
        }
    }

    private fun gotoDetail(pageBean: PageBean) {
        MovieDetailActivity.nav(this,pageBean.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWatchHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val pageBean = ObjectBox.store.boxFor(PageBean::class.java)
        pageBean.removeAll()

        val pageBean1 = PageBean(playName = "陈情令", playDesInfo = "好看的剧集1", progress = 1000L, playMark = "更新到第6集", id = 0, cover = "https://ww4.sinaimg.cn/mw690/008u6GgOgy1hr3z9wgshxj30u016sgui.jpg")
        val pageBean2 = PageBean(playName = "插翅难逃", playDesInfo = "好看的剧集2", progress = 1000L, playMark = "更新到第10集", id =0 , cover = "https://ww4.sinaimg.cn/mw690/008u6GgOgy1hr3z9wgshxj30u016sgui.jpg")
        pageBean.put(pageBean1)
        pageBean.put(pageBean2)

        fetchLocalData {
            Log.d(TAG, "onCreate() called,it=${it.size}")
            //
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            adapter.setList(it)
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

    private var mDisposable: Disposable? = null
    private fun fetchLocalData(success: (List<PageBean>) -> Unit) {
        mDisposable = Observable.create {
            val pageBean = ObjectBox.store.boxFor(PageBean::class.java)
//        val user = User(name = "fffff")
//        userBox.put(user)
            val all = pageBean.all
            Log.d(TAG, "onCreate: fetchLocalData=$all")
            it.onNext(all)

        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {

            })

    }

    companion object {
        const val TAG = "MainActivity"
    }
}