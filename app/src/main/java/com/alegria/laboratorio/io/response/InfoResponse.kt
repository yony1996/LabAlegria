package com.alegria.laboratorio.io.response



data class InfoResponse(
    val id: Int,
    val avatar: String,
    val nui: String,
    val age: Int,
    val name: String,
    val last_name: String,
    val phone: String,
    val gender: String,
    val email: String,
    val status: Int
)
