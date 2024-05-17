package com.example.soundwave.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favorite_games")
data class Game(
    @PrimaryKey
    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "name")
    var name: String,

    @SerialName(value = "released")
    var released: String,

    @SerialName(value = "background_image")
    var background_image: String,

    @SerialName(value = "metacritic")
    var metacritic: Int,
)