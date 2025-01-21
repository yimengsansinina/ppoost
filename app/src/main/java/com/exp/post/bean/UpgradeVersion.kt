package com.exp.post.bean

import android.os.Parcel
import android.os.Parcelable

class UpgradeVersion() :Parcelable{
    var upgradeVersion = 0
    var upgradePath: String? = null
    var upgradeInfo: String? = null
    var forceUpgradeVersion = 1

    constructor(parcel: Parcel) : this() {
        upgradeVersion = parcel.readInt()
        upgradePath = parcel.readString()
        upgradeInfo = parcel.readString()
        forceUpgradeVersion = parcel.readInt()
    }

    override fun toString(): String {
        return "UpgradeVersion(upgradeVersion=$upgradeVersion, upgradePath=$upgradePath, upgradeInfo=$upgradeInfo, forceUpgradeVersion=$forceUpgradeVersion)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(upgradeVersion)
        parcel.writeString(upgradePath)
        parcel.writeString(upgradeInfo)
        parcel.writeInt(forceUpgradeVersion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpgradeVersion> {
        override fun createFromParcel(parcel: Parcel): UpgradeVersion {
            return UpgradeVersion(parcel)
        }

        override fun newArray(size: Int): Array<UpgradeVersion?> {
            return arrayOfNulls(size)
        }
    }

}