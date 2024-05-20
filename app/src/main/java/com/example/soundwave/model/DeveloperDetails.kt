package com.example.soundwave.model

import kotlinx.serialization.SerialName

data class DeveloperDetails(

    @SerialName(value = "id")
    val id : Int,

    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "age")
    val age: Int,

    @SerialName(value = "image_background")
    val image_background:String,

    @SerialName(value = "gender")
    val gender: String,

    @SerialName(value = "games_count")
    val games_count: Int,

    @SerialName(value = "description")
    val description: String,
){
    fun toDeveloper(): Developer{
        return Developer(
            id = id,
            name = name,
            age = age,
            image_background = image_background
        )
    }
}
