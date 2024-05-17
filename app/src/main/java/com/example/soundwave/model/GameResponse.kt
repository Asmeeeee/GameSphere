package com.example.soundwave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameResponse<T> (
    @SerialName(value = "count")
    var count: Int = 0,

    @SerialName(value = "items")
    var results: List<T> = listOf(),

)