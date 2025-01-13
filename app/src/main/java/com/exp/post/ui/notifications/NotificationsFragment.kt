package com.exp.post.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.exp.calculator.user.RegisterFragment
import com.exp.post.bean.LoginEvent
import com.exp.post.user.LoginFragment
import com.exp.post.databinding.FragmentNotificationsBinding
import com.exp.post.tools.SPTools
import com.exp.post.ui.WatchHistoryActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Calendar

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.historyTv.setOnClickListener {
            WatchHistoryActivity.nav(requireActivity())
        }
        binding.setting.setOnClickListener {
            SettingsActivity.nav(requireActivity())
        }
        binding.aboutApp.setOnClickListener {
            AboutActivity.nav(requireActivity())
        }

        binding.loginNewTv.setOnClickListener {
            if (!isLogin()) {
                showLogin()
            }

        }
        binding.coverIv.setOnClickListener {
            if (!isLogin()) {
                showLogin()
            }
        }
        binding.vipLl.setOnClickListener {
            if (!isLogin()) {
                showLogin()
            } else {
                //
                val intent = Intent(requireActivity(), MemberPayActivity::class.java)
                startActivity(intent)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        initLoginUI()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        initLoginUI()
    }
    private fun initLoginUI() {

        if (isLogin()) {
            val memberTime = SPTools.getMemberTime()
            val memberStr = if (memberTime == 0L) {
                "---"
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                sdf.format(memberTime * 1000L)
            }

            binding.loginNewTv.text = SPTools.getAccount()
            binding.nameTv.text = "会员权益至:${memberStr}"

            if (memberTime > Calendar.getInstance().timeInMillis / 1000L) {
                binding.openTv.text = "续费"
            } else {
                binding.openTv.text = "开通会员"
            }
        } else {
            binding.loginNewTv.text = "登录/注册"
            binding.nameTv.text = "会员享受诸多权益!"
            binding.openTv.text = "开通会员"
        }
    }

    private fun isLogin(): Boolean {
        val account = SPTools.getAccount()
        val password = SPTools.getPassword()
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)
    }

    //
    private fun showLogin() {
        val beginTransaction = childFragmentManager.beginTransaction()
        val fragment = childFragmentManager.findFragmentByTag("login")
        if (fragment != null) {
            beginTransaction.remove(fragment)
        }
        beginTransaction.addToBackStack(null)
        val instance = LoginFragment.instance()
        instance.show(childFragmentManager, "login")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    public fun showRegister() {
        val beginTransaction = childFragmentManager.beginTransaction()
        val fragment = childFragmentManager.findFragmentByTag("register")
        if (fragment != null) {
            beginTransaction.remove(fragment)
        }
        beginTransaction.addToBackStack(null)
        val instance = RegisterFragment.instance()
        instance.show(childFragmentManager, "register")
    }
}