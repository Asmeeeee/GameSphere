package com.example.soundwave.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class RegistrationViewModel : ViewModel() {




    fun saveSpotifyToken(token: String?) {
        _spotifyToken.value = token
    }

    private val tokenData = MutableLiveData<JSONObject?>()
    val tokenCode = MutableLiveData<String>()
    private val addTokenResponse = MutableLiveData<AddMobileTokenResponse>()
    private val _response = MutableLiveData<String?>()
    private val _spotiResponse = MutableLiveData<String?>()
    val response: MutableLiveData<String?> = _response
    private val _spotifyToken = MutableLiveData<String?>()
    val spotifyToken: LiveData<String?> = _spotifyToken
    private val _combinedData = MediatorLiveData<Pair<String?, String?>>().apply {
        addSource(response) { response ->
            value = response to _spotifyToken.value
        }
        addSource(_spotifyToken) { token ->
            value = response.value to token
        }
    }
    val combinedData: LiveData<Pair<String?, String?>> = _combinedData




    fun sendSpotifyIntent() {
        val apiUrl = "http://51.20.128.164/spoti_login"

        val jsonObject = JSONObject().apply {
            put("username", "berkantumut@hotmail.com")
            put("password", "Be24522452")
            //put("public_id",publicId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder().url(apiUrl).post(requestBody).build()
                val response = client.newCall(request).execute()
                if(response.isSuccessful) {
                    val responseBody = response.body?.string()
                    _spotiResponse.postValue(responseBody)
                }
                else{
                    _spotiResponse.postValue("Request was not successful. HTTP code: ${response.code}")
                }


            }
            catch (e: IOException) {
                _spotiResponse.postValue("Request failed: ${e.message}")
            }
        }
    }

    fun saveTokenData(jsonResponse: JSONObject) {
        val result = jsonResponse
        result?.let{
            tokenData.postValue(jsonResponse)
        }
    }






}
