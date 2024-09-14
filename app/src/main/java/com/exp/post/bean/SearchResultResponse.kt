package com.exp.post.bean

import com.exp.post.dbs.PageBean

class SearchResultResponse {
    var code = 0
    var res: List<PageBean>? = null
    override fun toString(): String {
        return "SearchResultResponse(code=$code, res=$res)"
    }


}