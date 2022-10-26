package com.example.launderup.data.api

import com.example.launderup.data.models.ResendOTP
import com.example.launderup.data.models.SendOTP
import com.example.launderup.data.models.VerifyOTP
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface OtpApiInterface {
    @GET("api/v5/otp")
     fun getOTP(
        @Query("template_id")
        templateID: String = "62e7ec3e0f7fff3182774fe2",
        @Query("mobile")
        mobileNumber: Long,
        @Query("authkey")
        authKey:String="379241AFom8pFeIyEC62c66e46P1",
        @Query("otp_length")
        otpLength:Int=6,
        @Query("invisible")
        invisible:Int=1
    ):Call<SendOTP>

    @GET("api/v5/otp/verify")
    fun verifyOTP(
        @Query("otp")
        otp:Int,
        @Query("authkey")
        authKey:String="379241AFom8pFeIyEC62c66e46P1",
        @Query("mobile")
        mobileNumber: Long
    ):Call<VerifyOTP>

    @GET("api/v5/otp/retry")
    fun resendOTP(
        @Query("authkey")
        authKey:String="379241AFom8pFeIyEC62c66e46P1",
        @Query("retrytype")
        retryType: String = "text",
        @Query("mobile")
        mobileNumber: Long
    ):Call<ResendOTP>
}