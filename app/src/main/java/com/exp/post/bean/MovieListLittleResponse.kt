package com.exp.post.bean

import com.exp.post.dbs.PageBean

class MovieListLittleResponse {
    var code=0
    var res:List<PageBean>?=null
    override fun toString(): String {
        return "MovieListLittleResponse(code=$code, res=$res)"
    }

}