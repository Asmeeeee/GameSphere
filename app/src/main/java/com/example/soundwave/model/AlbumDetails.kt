package com.example.soundwave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDetails(
    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "album_type")
    var albumType: String,

    @SerialName(value = "name")
    var albumName: String,

    @SerialName(value = "release_date")
    var releaseDate: String,

    @SerialName(value = "uri")
    var uri: String,

    @SerialName(value = "genres")
    var genres: List<String>,

    @SerialName(value = "total_tracks")
    var totalTracks: Int,

    @SerialName(value = "artists")
    var artists: List<Artist>
)
