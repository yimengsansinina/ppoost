package com.exp.post.net
import com.exp.post.bean.HomePageRequest
import com.exp.post.bean.HomePageResponse
import com.exp.post.bean.MovieListRequest
import com.exp.post.bean.MovieListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NetApi {
    //    val account = req.getParameter("account")
//    val password = req.getParameter("password")
    @POST("homePage")
    fun homePage(@Body p: HomePageRequest): Call<HomePageResponse>

    @POST("queryMovieList")
    fun queryMovieList(@Body p: MovieListRequest): Call<MovieListResponse>

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