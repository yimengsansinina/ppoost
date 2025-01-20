package com.exp.post.bean

class LoginAppBean {
    var title: String? = null
    var topC: Int = 0

    constructor(title: String?, topC: Int) {
        this.title = title
        this.topC = topC
    }

    override fun toString(): String {
        return "LoginAppBean(title=$title, topC=$topC)"
    }

}