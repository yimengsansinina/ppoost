package com.exp.post.dbs

import android.util.Log
import io.objectbox.query.QueryBuilder.DESCENDING

object HistoryUtils {
    private const val TAG = "HistoryUtils"
    fun selectAll(): MutableList<HistoryPageBean>? {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
        val query = pageBeanBox
            .query()
            .order(HistoryPageBean_.time,DESCENDING)
            .build()
        return query.find()
    }

    fun insert(pageBean: HistoryPageBean) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
        pageBeanBox.put(pageBean)
    }

    fun delete(pageBean: HistoryPageBean) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryPageBean::class.java)
        pageBeanBox.remove(pageBean)
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