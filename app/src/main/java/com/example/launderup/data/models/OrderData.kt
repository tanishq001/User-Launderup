package com.example.launderup.data.models

data class OrderData(
    val address: String,
    val clothes_types: String,
    val created_at: String,
    val delivery_dt: String,
    val express: Boolean,
    val geolocation: String,
    val id: Int,
    val order_id: String,
    val payment_id: String,
    val pickup_dt: String,
    val service_type: String,
    val shid: String,
    val status: String,
    val total_cost: String,
    val uid: String,
    val updated_at: String
)