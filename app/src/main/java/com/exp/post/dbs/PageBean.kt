package com.exp.post.dbs

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient

@Entity
 data class PageBean
    (
    @Id
    var id:Long=0,
    var playName:String?=null,
    var cover:String?=null,
    var playScore:String?=null,
    var playMark:String?=null,
    
    var playActor: String? = null,
    var clsId: Int=0,
    var playYear: String? = null,
    var playArea:String? = null,
    var topClass: Int=0,
    var playLanguage: String? = null,
    var playDirector: String? = null,
    var playDesInfo: String? = null,

    //
    var historyProgress:Long=0L,
    var historySource:Int=0,
    var historyEpIndex:Int=0
)
{
    @Transient
    var playList: List<String>? = null
    override fun toString(): String {
        return "PageBean(id=$id, playName=$playName, cover=$cover, playScore=$playScore, playMark=$playMark, playActor=$playActor, clsId=$clsId, playYear=$playYear, playArea=$playArea, topClass=$topClass, playLanguage=$playLanguage, playDirector=$playDirector, playDesInfo=$playDesInfo, historyProgress=$historyProgress, historySource=$historySource, historyEpIndex=$historyEpIndex, playList=$playList)"
    }


}

