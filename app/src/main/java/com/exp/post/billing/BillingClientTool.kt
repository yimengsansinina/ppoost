package com.exp.post.billing

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.exp.post.BaseApp
import com.google.common.collect.ImmutableList
class BillingClientTool : PurchasesUpdatedListener {
    private val billingClient: BillingClient

    init {
        val  pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
        billingClient = BillingClient.newBuilder(BaseApp.mApp!!)
            .setListener(this).enablePendingPurchases(pendingPurchasesParams).build()
    }

    private var mListener: IBillingListener? = null
    fun setListener(listener: IBillingListener) {
        mListener = listener
    }

    fun startConnect() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d(
                        TAG,
                        "onBillingSetupFinished() called with: billingResult = $billingResult"
                    )
                    mListener?.onConnected()
                } else {
                    Log.d(
                        TAG,
                        "onBillingSetupFinished() called with: billingResult = $billingResult"
                    )
                    mListener?.onError(billingResult.responseCode, billingResult.debugMessage,1)
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected() called")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                mListener?.onError(1, "onBillingServiceDisconnected",2)
            }
        })
    }

    fun queryProductDetails(productID: String) {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productID)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult,
                                                                            productDetailsList ->
            Log.d(
                TAG,
                "queryProductDetails() called with: billingResult = $billingResult, productDetailsList = $productDetailsList"
            )
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                Log.d(
                    TAG,
                    "queryProductDetails: billingResult.responseCode=${billingResult.responseCode}"
                )
                mListener?.onError(billingResult.responseCode, billingResult.debugMessage,3)
                return@queryProductDetailsAsync
            }
            if (productDetailsList.isEmpty()) {
                Log.d(
                    TAG,
                    "queryProductDetails: productDetailsList empty"
                )
                mListener?.onError(2001, "productDetailsList empty",4)
                return@queryProductDetailsAsync
            }
            val productDetail = productDetailsList[0]
            mListener?.onQuerySuccess(productDetail)
            Log.d(TAG, "queryProductDetails: productDetail=$productDetail")
        }

    }

    fun buy(activity: Activity, productDetails: ProductDetails) {
        // An activity reference from which the billing flow will be launched.
//        val activity : Activity = ...;

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(productDetails)
                // For One-time product, "setOfferToken" method shouldn't be called.
                // For subscriptions, to get an offer token, call ProductDetails.subscriptionOfferDetails()
                // for a list of offers that are available to the user
//                .setOfferToken(selectedOfferToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

// Launch the billing flow
        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.d(TAG, "buy: billingResult.responseCode=${billingResult.responseCode}")
            mListener?.onError(billingResult.responseCode, billingResult.debugMessage,5)
            return
        }
        Log.d(TAG, "buy: billingResult.responseCode=${billingResult.responseCode} ")
        mListener?.onLaunchSuccess()
    }

    companion object {
        val instance: BillingClientTool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BillingClientTool()
        }
        const val TAG = "BillingClientTool"
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        Log.d(TAG, "onPurchasesUpdated: billingResult.responseCode=${billingResult.responseCode}")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            mListener?.onError(billingResult.responseCode, billingResult.debugMessage,6)
        } else {
            // Handle any other error codes.
            mListener?.onError(billingResult.responseCode, billingResult.debugMessage,7)
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build();

        val listener = ConsumeResponseListener { billingResult, p1 ->
            Log.d(
                TAG,
                "onConsumeResponse: billingResult.responseCode=${billingResult.responseCode}"
            )
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
                mListener?.onConsumeSuccess()
            }else{
                mListener?.onError(billingResult.responseCode, billingResult.debugMessage,8)
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }
}