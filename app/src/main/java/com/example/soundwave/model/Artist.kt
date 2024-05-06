package com.example.soundwave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(

    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "name")
    var name: String,

    @SerialName(value = "type")
    var type: String,

    @SerialName(value = "uri")
    var uri: String
)
