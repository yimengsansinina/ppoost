package com.exp.post.bean

class PayEntity {
   var type:Int=1
   var num:String?=null

   constructor(type: Int, num: String?) {
      this.type = type
      this.num = num
   }

   override fun toString(): String {
      return "PayEntity(type=$type, num=$num)"
   }

}