package com.exp.post.net

import com.exp.post.BaseApp
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
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
        val cacheSize = 300 * 1024 * 1024L // 10 MB
        val cache = Cache(File(BaseApp.mApp!!.getCacheDir(), "http_cache"), cacheSize)
        val builder = OkHttpClient.Builder()
        allowAllSSL(builder)
        builder.connectTimeout(connectTime, TimeUnit.SECONDS)
        builder.readTimeout(readTime, TimeUnit.SECONDS)
        builder.writeTimeout(writeTime, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)
            .cache(cache)
            .addInterceptor(object :Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()

                    // 默认缓存时间
                    val cacheControl: CacheControl = CacheControl.Builder()
                        .maxAge(5, TimeUnit.MINUTES) // 默认在线时缓存 5 分钟
                        .build()

                    // 将默认缓存策略应用到请求
                    request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()

                    val response = chain.proceed(request)
                    return response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build()
                }

            })
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