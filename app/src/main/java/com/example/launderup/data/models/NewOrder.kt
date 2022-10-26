package com.example.launderup.data.models

data class NewOrder(
    val cloth_order_id: String,
    val payment_id: String,
    val payment_order_id: String,
    val status: String
)