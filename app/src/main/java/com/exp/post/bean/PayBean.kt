package com.exp.post.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class PayBean(override val itemType: Int) :MultiItemEntity {
     var title:String?=null
     var num:String?=null
     var type:Int=1
}