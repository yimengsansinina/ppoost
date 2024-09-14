package com.exp.post.dbs

import io.objectbox.query.QueryBuilder
import io.objectbox.query.QueryBuilder.DESCENDING
import java.util.Calendar

object HistoryWordUtils {
    private const val TAG = "HistoryWordUtils"
    fun selectAll(): MutableList<HistoryBean>? {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        val query = pageBeanBox
            .query()
            .order(HistoryBean_.time, DESCENDING)
            .build().find()
        // 判断记录数量是否超过 15
        if (query.size > 15) {
            // 获取超过 15 条的记录部分
            val messagesToDelete: List<HistoryBean> = query.subList(15, query.size)
            // 删除多余的记录
            pageBeanBox.remove(messagesToDelete)
            return query.subList(0, 15)
        }
        return query
    }

    fun insert(key:String) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        val existingUser = pageBeanBox.query().
        equal(HistoryBean_.key, key, QueryBuilder.StringOrder.CASE_SENSITIVE)
            .build()
            .findFirst()

        if (existingUser != null) {
            // 如果已有记录，更新该记录的其他字段
            existingUser.time = Calendar.getInstance().timeInMillis/1000L
            existingUser.key=key
            pageBeanBox.put(existingUser) // 更新记录
        } else {
            // 如果不存在记录，创建新记录并插入
            val newUser = HistoryBean()
            newUser.time = Calendar.getInstance().timeInMillis/1000L
            newUser.key=key
            pageBeanBox.put(newUser) // 插入新记录
        }
    }

    fun delete(pageBean: HistoryBean) {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        pageBeanBox.remove(pageBean)
    }

    fun deleteAll() {
        val pageBeanBox = ObjectBox.store.boxFor(HistoryBean::class.java)
        pageBeanBox.removeAll()
    }


}