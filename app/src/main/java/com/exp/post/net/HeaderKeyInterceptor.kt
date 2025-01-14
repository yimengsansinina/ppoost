package com.exp.post.net

import okhttp3.Interceptor
import okhttp3.Response

class HeaderKeyInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val header = request.newBuilder()
            .header("appVersion", "1")
       return chain.proceed(header.build())
    }
}