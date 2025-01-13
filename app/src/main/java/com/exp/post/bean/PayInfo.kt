package com.exp.post.bean

import android.os.Parcel
import android.os.Parcelable

class PayInfo() :Parcelable {
    var type: Int = 0
    var num: String?=null

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
        num = parcel.readString()
    }

//    constructor(type: Int, num: String) {
//        this.type = type
//        this.num = num
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(num)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PayInfo> {
        override fun createFromParcel(parcel: Parcel): PayInfo {
            return PayInfo(parcel)
        }

        override fun newArray(size: Int): Array<PayInfo?> {
            return arrayOfNulls(size)
        }
    }
}