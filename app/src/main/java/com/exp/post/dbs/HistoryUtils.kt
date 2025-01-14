package com.exp.post.dbs

import android.util.Log
import io.objectbox.query.QueryBuilder.DESCENDING

object HistoryUtils {
    private const val TAG = "HistoryUtils"
    private const val MAX_COUNT = 50
    fun selectAll(): MutableList<HistoryPageBean>? {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
        val query = pageBeanBox
            .query()
            .order(HistoryPageBean_.time,DESCENDING)
            .build().find()

        // 判断记录数量是否超过 15
        if (query.size > MAX_COUNT) {
            // 获取超过 15 条的记录部分
            val messagesToDelete: List<HistoryPageBean> = query.subList(MAX_COUNT, query.size)
            // 删除多余的记录
            pageBeanBox.remove(messagesToDelete)
            return query.subList(0, MAX_COUNT)
        }
        return query
    }

    fun insert(pageBean: HistoryPageBean) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
        pageBeanBox.put(pageBean)
    }

        fun deleteAll() {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
            pageBeanBox.removeAll()
    }

    fun query(id: Long): HistoryPageBean? {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
        val query = pageBeanBox
            .query(HistoryPageBean_.id.equal(id))
//            .order(User_.name)
            .build()
        val results = query.find()
        Log.d(TAG, "query: results=$results")
        query.close()
        if (results.isNullOrEmpty()) {
            return null
        }
        return results[0]
    }


}