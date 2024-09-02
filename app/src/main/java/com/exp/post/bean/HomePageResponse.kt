package com.exp.post.bean

class HomePageResponse {
    var code=0
    var res:List<HomePageBean>?=null
    override fun toString(): String {
        return "HomePageResponse(code=$code, res=$res)"
    }


}