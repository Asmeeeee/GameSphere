package com.example.soundwave.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favorite_albums")
data class Album(
    @PrimaryKey
    @SerialName(value = "id")
    var id: String,

    @SerialName(value = "album_type")
    var albumType: String,

    @SerialName(value = "name")
    var title: String,

    @SerialName(value = "release_date")
    var releaseDate: String,

//    @SerialName(value = "artists")
//    var artists: List<Artist>
)
