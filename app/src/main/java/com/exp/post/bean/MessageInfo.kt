package com.exp.post.bean

import android.os.Parcel
import android.os.Parcelable

class MessageInfo() :Parcelable {
    var messageText:String?= null
    var messageId:Int= 0
    var messageUrl:String?= null

    constructor(parcel: Parcel) : this() {
        messageText = parcel.readString()
        messageId = parcel.readInt()
        messageUrl = parcel.readString()
    }

    override fun toString(): String {
        return "MessageInfo(messageText=$messageText, messageId=$messageId, messageUrl=$messageUrl)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(messageText)
        parcel.writeInt(messageId)
        parcel.writeString(messageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageInfo> {
        override fun createFromParcel(parcel: Parcel): MessageInfo {
            return MessageInfo(parcel)
        }

        override fun newArray(size: Int): Array<MessageInfo?> {
            return arrayOfNulls(size)
        }
    }

}