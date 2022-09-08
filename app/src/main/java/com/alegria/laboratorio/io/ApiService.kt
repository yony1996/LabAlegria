package com.alegria.laboratorio.io




import com.alegria.laboratorio.BuildConfig
import com.alegria.laboratorio.io.response.InfoResponse
import com.alegria.laboratorio.io.response.LoginResponse
import com.alegria.laboratorio.io.response.SimpleResponse
import com.alegria.laboratorio.io.response.UrlResponse
import com.alegria.laboratorio.model.Appoiment
import com.alegria.laboratorio.model.Exam
import com.alegria.laboratorio.model.HourInterval
import com.alegria.laboratorio.model.Result
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun postLogin(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @POST("register")
    @Headers("Accept:application/json")
    fun postRegister(
        @Query("nui") nui: String,
        @Query("name") name: String,
        @Query("last_name") last_name: String,
        @Query("age") age: String,
        @Query("gender") gender: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("password") password: String,
    ): Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>

    @GET("appoiments")
    fun getAppointments(@Header("Authorization") authHeader: String): Call<ArrayList<Appoiment>>

    @GET("schedule/hours")
    fun getHours(
        @Query("date") date: String,
        @Query("user_id") userId: Int
    ): Call<ArrayList<HourInterval>>

    @GET("exams")
    fun getExams(@Header("Authorization") authHeader: String): Call<ArrayList<Exam>>

    @GET("results")
    fun getResult(@Header("Authorization") authHeader: String): Call<ArrayList<Result>>

    @POST("appoiments/create")
    @Headers("Accept:application/json")
    fun storeAppointment(
        @Header("Authorization") authHeader: String,
        @Query("user_id") userId: Int,
        @Query("exam_id") examId: Int,
        @Query("scheduled_date") scheduledDate: String,
        @Query("scheduled_time") scheduledTime: String,

        ): Call<SimpleResponse>

    @GET("information")
    fun getInformation(@Header("Authorization") authHeader: String): Call<InfoResponse>

    @GET("download/result/{result}")
    fun getUrlPdf(
        @Header("Authorization") authHeader: String,
        @Path("result") result: Int
    ): Call<UrlResponse>

    companion object Factory {
        private const val BASE_URL = BuildConfig.BASE_PATH

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}