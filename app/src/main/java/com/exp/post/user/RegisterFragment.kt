package com.exp.calculator.user

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.exp.post.bean.LoginRequest
import com.exp.post.R
import com.exp.post.bean.LoginEvent
import com.exp.post.bean.LoginResponse
import com.exp.post.bean.User
import com.exp.post.net.HttpClient
import com.exp.post.net.NetApi
import com.exp.post.tools.SPTools
import com.exp.post.user.UserRememberActivity
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterFragment : DialogFragment() {
    private lateinit var mRoot: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mRoot = LayoutInflater.from(activity).inflate(R.layout.fragment_register, container, false)
        return mRoot
    }

    private abstract class NoUnderlineClickableSpan : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
//            ds.setColor(ds.linkColor)
            ds.setColor(Color.parseColor("#1296db"))
            ds.bgColor = Color.TRANSPARENT
            ds.isUnderlineText = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val agree = view.findViewById<TextView>(R.id.agree)
        val builder = SpannableStringBuilder()
        builder.append("已阅读并同意服务条款和隐私政策")

        // 将"世界"设置为蓝色，并添加点击事件
        builder.setSpan(ForegroundColorSpan(Color.parseColor("#1296db")), 6, 10, 0)
        builder.setSpan(object : NoUnderlineClickableSpan() {
            override fun onClick(view: View) {
                // 点击"世界"时的操作
                val intent = Intent()
                intent.putExtra("url","https://studiotu.uk:8443/user_agreement.html")
                intent.putExtra("title","用戶協議")
                intent.setClass(requireActivity(), UserRememberActivity::class.java)
                startActivity(intent)
            }
        }, 6, 10, 0)

        builder.setSpan(ForegroundColorSpan(Color.parseColor("#1296db")), 11, 15, 0)
        builder.setSpan(object : NoUnderlineClickableSpan() {
            override fun onClick(view: View) {
                // 点击"世界"时的操作
                val intent = Intent()
                intent.putExtra("url", "https://docs.google.com/document/d/1g2kmRRk-hw3JQxAj5RJU6bR-GT1sDXrN-2wj5LS6bXY/edit?hl=zh-cn&pli=1#heading=h.bmfkudbe7avy")
                intent.putExtra("title","隱私政策")
                intent.setClass(requireActivity(),UserRememberActivity::class.java)
                startActivity(intent)
            }
        }, 11, 15, 0)
        agree.setMovementMethod(LinkMovementMethod.getInstance())
        agree.setHighlightColor(Color.TRANSPARENT);
        agree.setText(builder)




        val loadingView = view.findViewById<View>(R.id.loadingView)
        view.findViewById<View>(R.id.login).setOnClickListener {
            val account_et = view.findViewById<EditText>(R.id.account_et)
            val password_et = view.findViewById<EditText>(R.id.password_et)
            val account = account_et.text.trim().toString()
            val password = password_et.text.trim().toString()
            val cb = view.findViewById<CheckBox>(R.id.cb)
            if (!cb.isChecked) {
                Toast.makeText(activity, "请先勾选同意协议", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                Toast.makeText(activity, "邮箱或者密码为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loadingView.visibility = View.VISIBLE
            login(account, password, {
                Log.d(TAG, "onViewCreated:register success,uuid=${it.uuid}")
                loadingView.visibility = View.GONE
                SPTools.saveAccount(account)
                SPTools.savePassword(password)
                SPTools.saveUUID(it.uuid)
                Toast.makeText(activity, "注册成功", Toast.LENGTH_SHORT).show()
                EventBus.getDefault().post(LoginEvent())
                dismiss()
            }, {
                Log.d(TAG, "onViewCreated: code=$it")
                loadingView.visibility = View.GONE
                if (it == 0) {
                    //
                } else if (it == 1) {
//                    Toast.makeText(activity, "当前邮箱还没有注册", Toast.LENGTH_SHORT).show()
                    showAlert("当前邮箱已经注册")

                } else {
//                    Toast.makeText(activity, "网络异常,请重试", Toast.LENGTH_SHORT).show()
                    showAlert("网络异常,请重试")
                }
            })
        }
    }

    private var mDialog: AlertDialog? = null
    private fun showAlert(msg: String) {
        if (activity == null || requireActivity().isDestroyed || requireActivity().isFinishing) {
            return
        }
        mDialog?.dismiss()
        mDialog = AlertDialog.Builder(requireActivity()).setTitle("提示")
            .setMessage(msg)
            .setPositiveButton(
                "确认"
            ) { dialog, which ->
                dialog.dismiss()
            }
//            .setNegativeButton(
//                "取消"
//            ) { dialog, which ->
//
//            }
            .create()
        mDialog?.show()
    }

    override fun onStart() {
        super.onStart()
        dialog?.run {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            val d = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(d)
            val win = window
            win?.let {
                it.setDimAmount(0.5F)
                it.setGravity(Gravity.BOTTOM)
                it.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

        }
    }

    companion object {
        private const val TAG = "LoginFragment"
        fun instance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun login(
        account: String,
        password: String,
        success: (User) -> Unit,
        fail: (Int) -> Unit
    ) {
        val bean = LoginRequest()
        bean.account=account
        bean.password=password
        HttpClient.instance.getServer(NetApi::class.java)
            .register(bean)
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
                    val code = body.code
                    if (code != 0) {
                        mHandler.post {
                            fail(code)
                        }
                        return
                    }
                    val res = body.res
                    if (res == null) {
                        mHandler.post {
                            fail(code)
                        }
                        return
                    }
                    mHandler.post {
                        success(res)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    mHandler.post {
                        fail(100)
                    }
                }

            })
    }
}