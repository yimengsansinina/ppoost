package com.exp.post.tools

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

object MMKVManager {
    private const val KEY_MEMBER_PAY_LIST = "MemberPayList"
    private const val KEY_LIST_INFO = "ListInfoTextArr"
    private const val KEY_PAGE_LIST = "PageList"
    private const val KEY_LAST_MESSAGE_ID = "LastMessageId"
    
    private val mmkv by lazy { MMKV.defaultMMKV() }
    private val gson by lazy { Gson() }

    // 通用的保存方法
    private inline fun <reified T> saveData(key: String, data: T?) {
        if (data == null) return
        try {
            mmkv.encode(key, gson.toJson(data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 通用的获取方法
    private inline fun <reified T> getData(key: String, defaultValue: T): T {
        return try {
            val json = mmkv.decodeString(key, "")
            if (json.isNullOrEmpty()) {
                defaultValue
            } else {
                gson.fromJson(json, object : TypeToken<T>() {}.type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    // 保存最后一次显示的消息ID
    fun saveLastMessageId(messageId: Int) {
        mmkv.encode(KEY_LAST_MESSAGE_ID, messageId)
    }

    // 获取最后一次显示的消息ID
    fun getLastMessageId(): Int {
        return mmkv.decodeInt(KEY_LAST_MESSAGE_ID, 0)
    }
} 