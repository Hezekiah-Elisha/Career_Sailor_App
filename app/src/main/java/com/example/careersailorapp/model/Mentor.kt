package com.example.careersailorapp.model

data class Mentor(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val location: String,
    val bio: String,
    val field: String,
    val image_url: String ?= null
)

//https://www.youtube.com/watch?v=bqeXwwOyAVM&list=PLOwSvmGdGxXTKzd9kDS8lzee1Pb4DUGcG&index=4
//https://www.youtube.com/watch?v=Bb8SgfI4Cm4