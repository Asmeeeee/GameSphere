package com.example.soundwave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Developer(

    @SerialName(value = "id")
    val id : Int,

    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "age")
    val age: Int,

    @SerialName(value = "languages")
    val languages: List<String>,

    @SerialName(value = "image_background")
    val image_background:String
)
