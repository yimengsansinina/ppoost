package com.exp.post.bean

class PageBean {
    var id:Int=0
    var playName:String?=null
    var cover:String?=null
    var playScore:String?=null
    var playMark:String?=null
    
    //

//    var id: Int
    var playActor: String? = null
    var clsId: Int=0
//    var playName: String? = null
//    var cover: String? = null
//    var playMark: String? = null
    var playYear: String? = null
    var playArea:String? = null
    var topClass: Int=0
    var playLanguage: String? = null
    var playDirector: String? = null
//    var playScore: String? = null
    var playDesInfo: String? = null
    var playList: List<String>? = null
    override fun toString(): String {
        return "PageBean(id=$id, playName=$playName, cover=$cover, playScore=$playScore, playMark=$playMark, playActor=$playActor, clsId=$clsId, playYear=$playYear, playArea=$playArea, topClass=$topClass, playLanguage=$playLanguage, playDirector=$playDirector, playDesInfo=$playDesInfo, playList=$playList)"
    }

}