package com.exp.post.dbs
import io.objectbox.query.QueryBuilder.DESCENDING

object HistoryWordUtils {
    private const val TAG = "HistoryWordUtils"
    fun selectAll(): MutableList<HistoryBean>? {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        val query = pageBeanBox
            .query()
            .order(HistoryBean_.time,DESCENDING)
            .build()
        return query.find()
    }

    fun insert(pageBean: HistoryBean) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        pageBeanBox.put(pageBean)
    }

    fun delete(pageBean: HistoryBean) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        pageBeanBox.remove(pageBean)
    }


}