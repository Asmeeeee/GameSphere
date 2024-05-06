package com.example.soundwave.model

import kotlinx.serialization.SerialName

data class Album(
    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "album_type")
    var albumType: String,

    @SerialName(value = "name")
    var albumName: String,

    @SerialName(value = "release_date")
    var releaseDate: String,

    @SerialName(value = "artists")
    var artists: List<Artist>
)
