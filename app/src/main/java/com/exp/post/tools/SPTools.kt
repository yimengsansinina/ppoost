package com.exp.post.tools

import android.text.TextUtils
import com.exp.post.bean.LoginAppBean
import com.exp.post.bean.PayInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


object SPTools {
    fun saveMemberPayList(payInfoList: List<PayInfo>?) {
        if (payInfoList.isNullOrEmpty()) {
            return
        }
        MMKV.defaultMMKV().encode("pay_info", Gson().toJson(payInfoList))
    }

    fun getMemberPayDetail(): List<PayInfo> {
        //1,3,6,12,100 forever;
        val decodeString = MMKV.defaultMMKV().decodeString("pay_info")
        val list = jsonToList(decodeString)
        if (list.isNullOrEmpty()) {
            val listT = arrayListOf<PayInfo>()
            val info1 = PayInfo()
            info1.num = "3"
            info1.type = 1
            listT.add(info1)
            val info2 = PayInfo()
            info2.num = "9"
            info2.type = 3
            listT.add(info2)
            val info3 = PayInfo()
            info3.num = "18"
            info3.type = 6
            listT.add(info3)
            val info4 = PayInfo()
            info4.num = "36"
            info4.type = 12
            listT.add(info4)
            val info5 = PayInfo()
            info5.num = "72"
            info5.type = 100
            listT.add(info5)
            return listT
        }
        return list
    }

    private fun jsonToList(json: String?): List<PayInfo>? {
        if (TextUtils.isEmpty(json)) {
            return null
        }
        val gson = Gson()
        val userListType: Type = object : TypeToken<List<PayInfo>>() {}.type
        return gson.fromJson(json, userListType)
    }


    //    calcul01
//    calcul
    fun saveMemberTime(time: Long) {
        MMKV.defaultMMKV().encode("member_time", time)
    }

    fun getMemberTime(): Long {
        return MMKV.defaultMMKV().decodeLong("member_time", 0)
    }

    fun saveAccount(account: String?) {
        MMKV.defaultMMKV().encode("account", account)
    }

    fun getAccount(): String {
        return MMKV.defaultMMKV().decodeString("account", null) ?: ""
    }

    fun clear() {
        MMKV.defaultMMKV().encode("password", "")
//        MMKV.defaultMMKV().encode("account", "")
        MMKV.defaultMMKV().encode("member_time", "")
    }

    fun savePassword(password: String?) {
        MMKV.defaultMMKV().encode("password", password)
    }

    fun getPassword(): String {
        return MMKV.defaultMMKV().decodeString("password", null) ?: ""
    }

    fun saveUUID(uuid: String?) {
        MMKV.defaultMMKV().encode("uuid", uuid)
    }

    fun getUUID(): String {
        return MMKV.defaultMMKV().decodeString("uuid", null) ?: ""
    }

    fun saveLoginHistoryFlag(success: Boolean) {
        MMKV.defaultMMKV().encode("LoginHistoryFlag", success)
    }

    fun getLoginHistoryFlag(): Boolean {
        return MMKV.defaultMMKV().decodeBool("LoginHistoryFlag", true)
    }

    fun isLogin(): Boolean {
        if (!TextUtils.isEmpty(getAccount()) && !TextUtils.isEmpty(getPassword())) {
            return true
        }
        return false
    }


    fun saveLittleNum(littleNum: Int) {
        MMKV.defaultMMKV().encode("littleNum", littleNum)
    }

    fun getLittleNum(): Int {
        return MMKV.defaultMMKV().decodeInt("littleNum", 1)
    }

    fun saveSound(enable: Boolean) {
        MMKV.defaultMMKV().encode("sound", enable)
    }

    fun getSound(): Boolean {
        return MMKV.defaultMMKV().decodeBool("sound", false)
    }

    fun saveZhen(enable: Boolean) {
        MMKV.defaultMMKV().encode("zhendong", enable)
    }

    fun getZhen(): Boolean {
        return MMKV.defaultMMKV().decodeBool("zhendong", true)
    }

    fun initPageList(pageList: List<LoginAppBean>?) {
        if (pageList.isNullOrEmpty()){
            return
        }
        MMKV.defaultMMKV().encode("PageList",  Gson().toJson(pageList))
    }
    fun getPageList(): List<LoginAppBean> {
        return try {
            val jsonString = MMKV.defaultMMKV().decodeString("PageList", "")
            if (jsonString.isNullOrEmpty()) {
                emptyList()
            } else {
                Gson().fromJson<List<LoginAppBean>>(
                    jsonString,
                    object : TypeToken<List<LoginAppBean>>() {}.type
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    // 保存方法
    fun initListInfoTextArr(listInfoTextArr: List<List<String>>?) {
        if (listInfoTextArr.isNullOrEmpty()) {
            MMKV.defaultMMKV().encode("ListInfoTextArr", "")
            return
        }
        try {
            MMKV.defaultMMKV().encode("ListInfoTextArr", Gson().toJson(listInfoTextArr))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 获取方法
    fun getListInfoTextArr(): List<List<String>> {
        return try {
            val jsonString = MMKV.defaultMMKV().decodeString("ListInfoTextArr", "")
            if (jsonString.isNullOrEmpty()) {
                emptyList()
            } else {
                Gson().fromJson<List<List<String>>>(
                    jsonString,
                    object : TypeToken<List<List<String>>>() {}.type
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}