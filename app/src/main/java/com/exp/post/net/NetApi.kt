package com.exp.post.net

import com.exp.post.bean.CommitHistoryResponse
import com.exp.post.bean.HistoryCommitRequest
import com.exp.post.bean.HistoryResponse
import com.exp.post.bean.HomePageRequest
import com.exp.post.bean.HomePageResponse
import com.exp.post.bean.LoginAppResponse
import com.exp.post.bean.LoginRequest
import com.exp.post.bean.LoginResponse
import com.exp.post.bean.MaxSearchResponse
import com.exp.post.bean.MovieInfoRequest
import com.exp.post.bean.MovieInfoResponse
import com.exp.post.bean.MovieListLittleRequest
import com.exp.post.bean.MovieListLittleResponse
import com.exp.post.bean.MovieListRequest
import com.exp.post.bean.MovieListResponse
import com.exp.post.bean.SearchResultResponse
import com.exp.post.bean.UpdateMemberRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface NetApi {
    @POST("login")
    fun login(@Body p: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body p: LoginRequest): Call<LoginResponse>

    //    val uuid = req.getParameter("uuid")
//    val memberTime = req.getParameter("memberTime")
    @POST("updateMember")
    fun updateMember(@Body p: UpdateMemberRequest): Call<LoginResponse>

    //    val account = req.getParameter("account")
//    val password = req.getParameter("password")
    @POST("homePage")
    fun homePage(@Body p: HomePageRequest): Call<HomePageResponse>
//    @Headers("Cache-Control: no-cache")
    @POST("queryMovieList")
    fun queryMovieList(@Body p: MovieListRequest): Call<MovieListResponse>
    @POST("queryMovieLittleList")
    fun queryMovieListLittle(@Body p: MovieListLittleRequest): Call<MovieListLittleResponse>

    @POST("movieInfo")
    fun movieInfo(@Body p: MovieInfoRequest): Call<MovieInfoResponse>

    @GET("maxSearchWords")
    fun maxSearchWords(): Call<MaxSearchResponse>
    @GET("loginApp")
    fun loginApp(): Call<LoginAppResponse>

    @GET("history")
    fun history(@Query("account") account: String): Call<HistoryResponse>
    @POST("commitHistory")
    fun commitHistory(@Body p:HistoryCommitRequest): Call<CommitHistoryResponse>
    @GET("searchByKeyWords")
    fun searchByKeyWords(
        @Query("pg") pg: Int,
        @Query("key") key: String
    ): Call<SearchResultResponse>
    //@Headers("Cache-Control: no-cache") // 禁用缓存
    //@Headers("Cache-Control: max-age=3600") // 设置这个接口的缓存为 1 小时
//    @FormUrlEncoded
//    @POST("register")
//    fun register(@FieldMap p: Map<String, String>): Call<LoginResponse>
//
//    //    val uuid = req.getParameter("uuid")
////    val memberTime = req.getParameter("memberTime")
//    @FormUrlEncoded
//    @POST("updateMember")
//    fun updateMember(@FieldMap p: Map<String, String>): Call<LoginResponse>
//
//    //   val account = req.getParameter("account")
//    @FormUrlEncoded
//    @POST("sendEmailCode")
//    fun sendEmailCode(@FieldMap p: Map<String, String>): Call<LoginResponse>
//
//    //    val account = req.getParameter("account")
////    val password = req.getParameter("password")
////    val emailCode = req.getParameter("emailCode")
//    @FormUrlEncoded
//    @POST("updatePassword")
//    fun updatePassword(@FieldMap p: Map<String, String>): Call<LoginResponse>
//
//    @FormUrlEncoded
//    @POST("initApp")
//    fun initApp(@FieldMap p: Map<String, String>): Call<InitAppResponse>
}