package com.alegria.laboratorio.io.response

import com.alegria.laboratorio.model.User

data class LoginResponse(
    val success:Boolean,
    val user: User,
    val passport:String
)
