package com.example.soundwave.spotify

import com.example.soundwave.spotify.Album

data class SpotifyArtistAlbumsResponse (
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Album>
)

