package com.example.soundwave.model

import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDetails(
    @PrimaryKey
    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "name")
    var name: String,

    @SerialName(value = "released")
    var released: String,

    @SerialName(value = "background_image")
    var background_image: String,

    @SerialName(value = "description")
    var description: String,

    @SerialName(value = "metacritic")
    var metacritic: Int,

    @SerialName(value = "website")
    var website: String,
){
    fun toGame(): Game {
        return Game(
            id = id,
            name = name,
            released = released,
            background_image = background_image,
            metacritic = metacritic,
        )
    }
}
