package com.artproficiencyapp.restapi

import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel
import com.artproficiencyapp.test.model.Question

import com.artproficiencyapp.model.DirectorModel
import com.artproficiencyapp.model.LevelModel
import com.artproficiencyapp.model.ReportModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

//    @GET("user-login")
//    fun userLogin(@Query("login_type") login_type: String, @Query("login_key") login_key: String, @Query("password") password: String): Call<ResponseBody>

    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("user-login")
    fun userLogin(@Field("login_type") login_type: String, @Field("login_key") login_key: String, @Field("password") password: String): Call<ResponseBody>


    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("user-login")
    fun facebookuserLogin(@Field("login_type") login_type: String, @Field("login_key") login_key: String, @Field("password") password: String,@Field("email") email: String): Call<ResponseBody>


    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("forgot-password")
    fun forgotPassword(@Field("email") email: String): Call<ResponseBody>


    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("create-user")
    fun registerDirector(@Field("name") name: String, @Field("email") email: String, @Field("phone_number") phone_number: String,
                         @Field("user_type") user_type: String, @Field("designation") designation: String,
                         @Field("membership_society") membership_society: String, @Field("level") level: String, @Field("password") password: String,
                         @Field("device_type") device_type: String, @Field("device_token") device_token: String, @Field("address") address: String,
                         @Field("lattitude") lattitude: String, @Field("longitude") longitude: String, @Field("login_type") login_type: String,
                         @Field("login_key") login_key: String): Call<ResponseBody>

    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("create-user")
    fun registerStudent(@Field("name") name: String, @Field("email") email: String, @Field("phone_number") phone_number: String, @Field("user_type") user_type: String,
                        @Field("director") director: String, @Field("password") password: String, @Field("device_type") device_type: String,
                        @Field("device_token") device_token: String, @Field("address") address: String,
                        @Field("lattitude") lattitude: String, @Field("longitude") longitude: String, @Field("login_type") login_type: String,
                        @Field("login_key") login_key: String): Call<ResponseBody>



    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("get-directors")
    fun getDirectors(@Field("login") login: String): Call<DirectorModel>



    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("verify-otp")
    fun verifyOTP(@Header("authorization") inToken: String,@Field("student_id") student_id: String,@Field("otp_key") otp_key:String): Call<ResponseBody>

    //baki
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("resend-otp")
    fun resendOTP(@Header("authorization") inToken: String,@Field("student_id") student_id: String): Call<ResponseBody>



    @GET("get-report-agency-list")
    @Headers("accept:application/json")
    fun reportAgency(@Header("authorization") inToken: String): Call<ReportModel>

    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("get-level-and-membership")
    fun levelMember(@Field("login") login: String): Call<LevelModel>

    @GET("get-test-category")
    @Headers("accept:application/json")
    fun getExcepandableList(@Header("authorization") inToken: String): Call<CategoryModel>


    @GET("get-question-list/{id}")
    @Headers("accept:application/json")
    fun getQuestionList(@Header("authorization") inToken: String,@Path("id") inId: String): Call<Question>


}