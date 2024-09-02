package com.exp.post.dbs

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient
import io.objectbox.annotation.Unique

@Entity
data class User(
    @Id
    var id: Long = 0,
    @Unique
    var name: String, // 字符串字段，带有唯一索引
    var name2: String? = null // 字符串字段，带有唯一索引

){
    @Transient
    var name1: String? = null
}
