package com.example.soundwave.auth

interface LoginListener {
    suspend fun onSpotify(): Boolean
    fun onLogin(username: String)
}