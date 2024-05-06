package com.example.soundwave.model

import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @PrimaryKey
    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "name")
    var name: String,

    @SerialName(value = "type")
    var type: String,

    @SerialName(value = "uri")
    var uri: String
)
