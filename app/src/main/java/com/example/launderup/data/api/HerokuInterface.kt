package com.example.launderup.data.api

import com.example.launderup.data.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

public interface HerokuInterface {
    @POST("api/userlogin")
    fun userLogin(
        @Query("phone")
        mobileNumber:String,
    ):Call<UserLogin>

    @POST("api/userregister")
    fun userRegister(
        @Query("uid")
        uid:String,
        @Query("phone")
        phoneNumber:String,
        @Query("name")
        name:String,
        @Query("email")
        email:String,
        @Query("city")
        city:String="New Delhi",
        @Query("pin")
        pinCode:String
    ):Call<UserRegister>

    @POST("api/updateUserDetails")
    fun userDetailsUpdate(
        @Query("uid")
        uid:String,
        @Query("phone")
        phoneNumber:String,
        @Query("name")
        name:String,
        @Query("email")
        email:String,
        @Query("city")
        city:String="New Delhi",
        @Query("pin")
        pinCode:String
    ):Call<UserDetailsUpdate>

    @GET("api/userFetch/{uid}")
    fun getUserDetails(
        @Path("uid")
        userID:String
    ):Call<UserDetails>

    @GET("api/shopUserFetch/{express}/{services}/{search}")
    fun getShopsList(
        @Path("express")
        expressService:String,
        @Path("services")
        serviceAvailable:String,
        @Path("search")
        search:String?
    ):Call<ShopsList>


    @GET("api/shopFetch/{shid}")
    fun getShopDetails(
        @Path("shid")
        shopID:String
    ):Call<ShopDetails>

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json"
    )
    @POST("api/neworder")
    fun userNewOrder(
        @Query("uid")
        uid:String,
        @Query("shid")
        shopID: String,
        @Query("pickup_dt")
        pickUpDate:String,
        @Query("delivery_dt")
        deliveryDate:String,
        @Query("geolocation")
        geolocation:String,
        @Query("address")
        userAddress:String,
        @Query("status")
        orderStatus:String="placed",
        @Query("express")
        expressAvailable:String,
        @Query("service_type")
        serviceType:String,
        @Query("total_cost")
        totalCost:String,
        @Query("clothes_types")
        clothesTypes:JSONObject
    ):Call<NewOrder>

    @GET("api/userOrderFetch/{uid}/{page}")
    fun getUserOrders(
        @Path("uid")
        uid:String,
        @Path("page")
        page:Int=4
    ):Call<UserOrders>

    @GET("api/orderFetch/{order_id}")
    fun getOrderDetails(
        @Path("order_id")
        orderID:String
    ):Call<OrderData>

    @POST("/api/cancelOrder")
    fun cancelOrder(
        @Query("uid")
        uid: String,
        @Query("cloth_order_id")
        clothOrderID:String
    ):Call<CancelOrder>

    @POST("/api/placeOrder")
    fun verifyOrder(
        @Query("cloth_order_id")
        clothOrderID: String,
        @Query("payment_id")
        paymentID:String,
        @Query("razorpay_payment_id")
        razorpayPaymentID:String,
        @Query("razorpay_order_id")
        razorpayOrderID:String,
        @Query("razorpay_signature")
        razorpaySignature:String
    ):Call<VerifyOrder>


}