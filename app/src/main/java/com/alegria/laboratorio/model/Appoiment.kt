package com.alegria.laboratorio.model

import com.google.gson.annotations.SerializedName

/*
*
* "id": 1,
 "status": "Reservada",
 "nameExam": "orina",
 "scheduled_date": "2022-08-02",
 "scheduled_time": "12:00:00"
  * */
data class Appoiment(
    val id: Int,
    val status: String,
    @SerializedName("name")val type: String,
    @SerializedName("scheduled_date") val scheduledDate: String,
    @SerializedName("scheduled_time") val scheduledTime: String,

    )
