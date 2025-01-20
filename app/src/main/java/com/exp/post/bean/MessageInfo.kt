package com.exp.post.bean

class MessageInfo {
    var messageText:String?= null
    var messageId:Int= 0
    var messageUrl:String?= null
    override fun toString(): String {
        return "MessageInfo(messageText=$messageText, messageId=$messageId, messageUrl=$messageUrl)"
    }

}