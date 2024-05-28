package com.exp.post.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class HomePageBean: MultiItemEntity {
    var type:Int=0
    var entity:PageBean?=null
    var desTitle:String?=null
    override val itemType: Int
        get() =type
}