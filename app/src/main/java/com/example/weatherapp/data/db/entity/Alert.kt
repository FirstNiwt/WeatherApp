package com.example.weatherapp.data.db.entity

data class Alert(
    val event:String,
    val start: Int,
    val end: Int,
    val description:String,
    val expanded:Boolean = false

)
