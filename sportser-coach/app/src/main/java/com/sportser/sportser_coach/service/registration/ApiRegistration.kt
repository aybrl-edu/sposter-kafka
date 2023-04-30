package com.sportser.sportser_coach.service.registration

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

object ApiRegistration {

    private const val base_url = "http://172.20.10.12:8085/coach/"
//    private const val base_url ="http://172.31.240.15:9004/epi-sport/api/notification/"
    private var apiRegistrationInterface: ApiRegistrationInterface? = null

    fun build(): ApiRegistrationInterface {
        val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()
        apiRegistrationInterface = retrofit.create(
            ApiRegistrationInterface::class.java
        )

        return apiRegistrationInterface as ApiRegistrationInterface
    }
    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    interface ApiRegistrationInterface {
        @POST("present")
        fun getRegistration(@Body registrationRequest: RegistrationRequest): Call<String>

        @DELETE("registration/{email}")
        fun deleteRegistration(@Path(value = "email") email: String): Call<String>
    }

}

data class RegistrationRequest(
    val coachMail: String,
    val isPresent: Boolean
)
