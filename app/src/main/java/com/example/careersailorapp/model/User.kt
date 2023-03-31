package com.example.careersailorapp.model

data class User (
    val email : String ?= null,
    val experience: Int ?= 0,
    val interest: String ?= null,
    val name: String ?= null,
    val profile_picture: String ?= null,
    val skill: String ?= null,
    val mentor: Int ?= 0
)