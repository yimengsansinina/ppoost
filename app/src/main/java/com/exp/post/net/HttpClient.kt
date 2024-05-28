package com.exp.post.net

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


class HttpClient {
    companion object {
        val instance: HttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpClient()
        }
    }

    private val okhttp: OkHttpClient
    private var mRetrofit: Retrofit? = null

    init {
        okhttp = initOK()
    }

    fun <T> getServer(server: Class<T>): T {
        if (mRetrofit == null) {
            mRetrofit = Retrofit.Builder()
                .baseUrl("https://studiotu.uk:8443/mt/")
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return mRetrofit!!.create(server)
    }

    private val connectTime = 15L
    private val readTime = 15L
    private val writeTime = 15L

    private fun initOK(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        allowAllSSL(builder)
        builder.connectTimeout(connectTime, TimeUnit.SECONDS)
        builder.readTimeout(readTime, TimeUnit.SECONDS)
        builder.writeTimeout(writeTime, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)
        return builder.build()
    }

    private fun allowAllSSL(builder: OkHttpClient.Builder) {
        try {
            val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
            trustStore.load(null, null)
            val ssl = SSLSocketFactoryLL(KeyStore.getInstance(KeyStore.getDefaultType()))
            val hostnameVerifier =
                HostnameVerifier { hostname, session -> true }
            builder.sslSocketFactory(ssl.getSSLContext().getSocketFactory(), ssl.getTrustManager())
                .hostnameVerifier(hostnameVerifier)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}