package com.exp.post

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.exp.post.bean.CommitHistoryResponse
import com.exp.post.bean.HistoryCommitRequest
import com.exp.post.bean.HistoryResponse
import com.exp.post.databinding.ActivityMainBinding
import com.exp.post.dbs.HistoryPageBean
import com.exp.post.dbs.HistoryUtils
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.SPTools
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        supportActionBar?.hide()
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId==R.id.navigation_dashboard){
                    MovieListActivity.navMovieListActivity(this@MainActivity)
                    return false
                }else{
                    NavigationUI.onNavDestinationSelected(item, navController)
                 return true
                }
            }

        })
        historyGET()
    }
    companion object{
        const val TAG="MainActivity"
    }

    override fun onPause() {
        super.onPause()
        historyCommitPOST()
    }
    private fun historyCommitPOST(){
        if (SPTools.isLogin()){
            historyCommit(SPTools.getAccount(),{
                Log.d(TAG, "historyCommitPOST: success")
            },{
                Log.d(TAG, "historyCommitPOST: fail,it=$it")
            })
        }
    }
    private fun historyGET(){
        if (!SPTools.getLoginHistoryFlag()){
            if (SPTools.isLogin()){
                history(SPTools.getAccount(),{
                    SPTools.saveLoginHistoryFlag(true)
                },{
                    SPTools.saveLoginHistoryFlag(false)
                })
            }

        }
    }
    private val mHandler = Handler(Looper.getMainLooper())
    private fun historyCommit(
        account: String,
        success: () -> Unit,
        fail: (Int) -> Unit
    ) {
        val selectAll = HistoryUtils.selectAll()
        Log.d(TAG, "historyCommit: selectAll=$selectAll")
        if (selectAll.isNullOrEmpty()){
            return
        }
        val request = HistoryCommitRequest()
        request.account = account
        request.data =Gson().toJson(selectAll)
        HttpClient.instance.getServer(NetApi::class.java)
            .commitHistory(request)
            .enqueue(object : Callback<CommitHistoryResponse> {
                override fun onResponse(
                    call: Call<CommitHistoryResponse>,
                    response: Response<CommitHistoryResponse>
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
                            fail(100)
                        }
                        return
                    }
                    //
                    if (body.code!=0) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    mHandler.post {
                        success()
                    }
                }

                override fun onFailure(call: Call<CommitHistoryResponse>, t: Throwable) {
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }

    private fun history(
        account: String,
        success: (List<HistoryPageBean>) -> Unit,
        fail: (Int) -> Unit
    ) {
        HttpClient.instance.getServer(NetApi::class.java)
            .history(account)
            .enqueue(object : Callback<HistoryResponse> {
                override fun onResponse(
                    call: Call<HistoryResponse>,
                    response: Response<HistoryResponse>
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
                            fail(100)
                        }
                        return
                    }
                    //
                    val res = body.res!!
                    Log.d(TAG, "onResponse: res=$res")
                    if (res == null) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    res.forEach {
                        if (it!=null){
                            HistoryUtils.insert(it)
                        }
                    }
                    mHandler.post {
                        success(res)
                    }
                }

                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }
}