package com.exp.post.billing

import com.android.billingclient.api.ProductDetails

interface IBillingListener {
    fun onConnected()
    fun onLaunchSuccess()
    fun onConsumeSuccess()
    fun onQuerySuccess(productDetail: ProductDetails)
    fun onError(code:Int,msg:String?,customCode:Int)
}