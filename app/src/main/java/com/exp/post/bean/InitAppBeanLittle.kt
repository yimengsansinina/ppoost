package com.exp.post.bean

class InitAppBeanLittle {
    var memberPayList: List<PayInfo>? = null
    var pageList: List<LoginAppBean>? = null
    var listInfoTextArr: List<List<String>>? = null
    var messageInfo: MessageInfo? = null
    var upgradeVersion: UpgradeVersion? = null
    override fun toString(): String {
        return "InitAppBeanLittle(memberPayList=$memberPayList, pageList=$pageList, listInfoTextArr=$listInfoTextArr, messageInfo=$messageInfo, upgradeVersion=$upgradeVersion)"
    }

}