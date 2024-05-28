package com.exp.post.dbs

import android.content.Context
import com.exp.post.dbs.MyObjectBox.*
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        store = builder()
            .androidContext(context)
            .build()
//        val orderBox = store.boxFor(Order::class)
    }
}