package com.exp.post.ui.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.ProductDetails
import com.exp.post.billing.BillingClientTool
import com.exp.post.billing.IBillingListener
import com.exp.post.BaseActivity
import com.exp.post.bean.LoginEvent
import com.exp.post.bean.LoginResponse
import com.exp.post.bean.PayBean
import com.exp.post.bean.UpdateMemberRequest
import com.exp.post.databinding.ActivityMemberPayBinding
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.SPTools
import com.jaeger.library.StatusBarUtil
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MemberPayActivity : BaseActivity() {
    private var _binding: ActivityMemberPayBinding? = null
    private val binding: ActivityMemberPayBinding
        get() = _binding!!
    private var mType = 1
    private val adapter by lazy {
        MemberPayAdapter {
            mType = it.type
            val su = getSuByType(it.type)
            Log.d(TAG, "MemberPayAdapter called,type=$it,su=$su")
            binding.payNum.text = "$${it.num}"
        }
    }

    private fun getSuByType(type: Int): String {
        return when (type) {
            1 -> {
                "cal_hui1"
            }

            3 -> {
                "cal_hui3"
            }

            6 -> {
                "cal_hui6"
            }

            12 -> {
                "cal_hui12"
            }

            100 -> {
                "cal_hui100"
            }

            else -> {
                "cal_hui1"
            }
        }
    }

    companion object {
        const val TAG = "MemberPayActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMemberPayBinding.inflate(LayoutInflater.from(this))
        setContentView(_binding?.root)
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        StatusBarUtil.setLightMode(this)
        supportActionBar?.hide()
        initUI()
    }

    private fun initUI() {
        binding.backFl.setOnClickListener { finish() }
        binding.payRecycler.adapter = adapter
        binding.payRecycler.layoutManager = LinearLayoutManager(this)
        val listT = SPTools.getMemberPayDetail()
        if (listT.isNullOrEmpty()) {
            return
        }
        mType = listT[0].type
        val firstNUM = listT[0].num
        binding.payNum.text = "$${firstNUM}"
        val list = arrayListOf<PayBean>()
        list.add(PayBean(0))
        listT.forEach {
            val bean = PayBean(0);
            bean.title = fetchTitle(it.type)
            bean.num = it.num
            bean.type = it.type
            list.add(bean)
        }
        adapter.setList(list)
//        binding.payNum.setOnClickListener {
//            buySever(3)
//        }
        binding.payNow.setOnClickListener {
            binding.loadingView.visibility=View.VISIBLE
            val su = getSuByType(mType)
            Log.d(TAG, "initUI() payNow,type=$mType,su=$su")
            BillingClientTool.instance.setListener(object : IBillingListener {
                override fun onConnected() {
                    Log.d(TAG, "onConnected() called")
                    runOnUiThread {
                        BillingClientTool.instance.queryProductDetails(su)
                    }
                }

                override fun onLaunchSuccess() {
                    Log.d(TAG, "onLaunchSuccess() called")
                }

                override fun onConsumeSuccess() {
                    Log.d(TAG, "onConsumeSuccess() called")
                    runOnUiThread {
                        buySever(mType)
                    }
                }

                override fun onQuerySuccess(productDetail: ProductDetails) {
                    Log.d(TAG, "onQuerySuccess() called with: productDetail = $productDetail")
                    runOnUiThread {
                        BillingClientTool.instance.buy(this@MemberPayActivity, productDetail)
                    }
                }

                override fun onError(code: Int, msg: String?,customCode:Int) {
                    Log.d(TAG, "onError() called with: code = $code, msg = $msg,customCode=$customCode")
                    runOnUiThread {
                        binding.loadingView.visibility=View.GONE
                        showAlert1("支付失败，是否重试？")
                    }
                }
            })
            BillingClientTool.instance.startConnect()
        }
    }

    private fun buySever(type: Int) {
        val time = SPTools.getMemberTime()
        val preTime = if (time > Calendar.getInstance().timeInMillis / 1000L) {
            time
        } else {
            Calendar.getInstance().timeInMillis / 1000L
        }
        val valueTime = when (type) {
            1 -> 2678500
            3 -> 8035400
            6 -> 16070600
            12 -> 32140900
            100 -> 630726000
            else -> 2678500
        }
        val nowTime = preTime + valueTime
        SPTools.saveMemberTime(nowTime)
        //
        realUpdateUser(nowTime)
    }

    private fun realUpdateUser(nowTime: Long) {
        updateUserInfo(nowTime, {
            //
            binding.loadingView.visibility=View.GONE
            showAlert2("购买成功!")
            EventBus.getDefault().post(LoginEvent())
        }, {
            binding.loadingView.visibility=View.GONE
            showAlert(nowTime)
        })
    }


    private var mDialog2: AlertDialog? = null
    private fun showAlert2(msg: String) {
        mDialog2?.dismiss()
        mDialog2 = AlertDialog.Builder(this).setTitle("提示")
            .setMessage(msg)
            .setPositiveButton(
                "确认"
            ) { dialog, which ->
                dialog.dismiss()
            }
            .create()
        mDialog2?.show()
    }


    private var mDialog1: AlertDialog? = null
    private fun showAlert1(msg: String) {
        mDialog1?.dismiss()
        mDialog1 = AlertDialog.Builder(this).setTitle("提示")
            .setMessage(msg)
            .setPositiveButton(
                "确认"
            ) { dialog, which ->
                binding.payNow.performClick()
                dialog.dismiss()
            }
            .setNegativeButton(
                "取消"
            ) { dialog, which ->
                dialog.dismiss()
            }
            .create()
        mDialog1?.show()
    }

    private var mDialog: AlertDialog? = null
    private fun showAlert(nowTime: Long) {
        mDialog?.dismiss()
        mDialog = AlertDialog.Builder(this).setTitle("提示")
            .setMessage("网络连接异常,请重试")
            .setPositiveButton(
                "确认"
            ) { dialog, which ->
                binding.loadingView.visibility=View.VISIBLE
                realUpdateUser(nowTime)
                dialog.dismiss()
            }
            .setNegativeButton(
                "取消"
            ) { dialog, which ->
                dialog.dismiss()
            }
            .create()
        mDialog?.show()
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun updateUserInfo(
        time: Long,
        success: () -> Unit,
        fail: (Int) -> Unit
    ) {
        val uuid = SPTools.getUUID()
        val bean = UpdateMemberRequest()
        bean.uuid = uuid
        bean.memberTime = time
        HttpClient.instance.getServer(NetApi::class.java)
            .updateMember(bean)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
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
                    if (body.code != 0) {
                        mHandler.post {
                            fail(body.code)
                        }
                        return
                    }
                    mHandler.post {
                        success()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }

    private fun fetchTitle(type: Int): String {
        return when (type) {
            1 -> "1个月会员"
            3 -> "3个月会员"
            6 -> "6个月会员"
            12 -> "12个月会员"
            else -> "永久会员"

        }
    }

//    override fun onResume() {
//        super.onResume()
//        buySever(1)
//
//    }
}