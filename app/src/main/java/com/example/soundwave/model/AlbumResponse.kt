package com.example.soundwave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse<T> (
    @SerialName(value = "page")
    var page: Int = 0,

    @SerialName(value = "items")
    var results: List<T> = listOf(),

    @SerialName(value = "total")
    var total_results: Int = 0,
)