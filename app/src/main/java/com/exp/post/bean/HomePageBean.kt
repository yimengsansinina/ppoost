package com.exp.post.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.exp.post.dbs.PageBean

class HomePageBean: MultiItemEntity {
    var type:Int=0
    var entity: PageBean?=null
    var desTitle:String?=null

    override val itemType: Int
        get() =type

    override fun toString(): String {
        return "HomePageBean(type=$type, entity=$entity, desTitle=$desTitle, itemType=$itemType)"
    }

}