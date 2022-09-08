package com.alegria.laboratorio.model

import com.google.gson.annotations.SerializedName

data class Result(
    val id: Int,
    val doctor: String,
    val orden: String,
    val type: String,
    @SerializedName("created_at") val createdAt: String,
)
