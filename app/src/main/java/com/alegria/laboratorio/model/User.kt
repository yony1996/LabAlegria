package com.alegria.laboratorio.model

data class User(
    val id: Int,
    val avatar: String,
    val name: String,
    val email: String,
    val status: Int,
    val rol: String
)
