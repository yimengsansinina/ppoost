package com.exp.post.bean

class UpgradeVersion {
    var upgradeVersion = 0
    var upgradePath: String? = null
    var upgradeInfo: String? = null
    var forceUpgradeVersion = 1
    override fun toString(): String {
        return "UpgradeVersion(upgradeVersion=$upgradeVersion, upgradePath=$upgradePath, upgradeInfo=$upgradeInfo, forceUpgradeVersion=$forceUpgradeVersion)"
    }

}