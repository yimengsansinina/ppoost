package com.exp.post.dbs

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient
import io.objectbox.annotation.Unique
import org.jetbrains.annotations.NotNull

@Entity
data class HistoryBean
    (
    @Id
    var id: Long = 0,
    @Unique
    var key: String?=null,
    var time: Long = 0L
) {

    override fun toString(): String {
        return "HistoryBean(key=$key, time=$time)"
    }
}

