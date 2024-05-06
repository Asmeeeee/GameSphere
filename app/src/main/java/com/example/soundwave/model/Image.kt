package com.example.soundwave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image (

    @SerialName(value = "url")
    var url: String,

    @SerialName(value = "height")
    var height: Int,

    @SerialName(value = "width")
    var width: Int,
)