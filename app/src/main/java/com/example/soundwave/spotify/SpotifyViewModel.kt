package com.example.soundwave.spotify

import SpotifyArtistInfoService
import SpotifyArtistInfoServiceProvider
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import com.google.gson.Gson



open class SpotifyViewModel(protected val token: String) : ViewModel() {

    private val repository = SpotifyRepository(SpotifyServiceProvider.instance,SpotifyTopArtistsServiceProvider.instance, SpotifySearchServiceProvider.instance,
        SpotifyRecommendationsServiceProvider.instance, SpotifyArtistInfoServiceProvider.instance, SpotifyTrackInfoServiceProvider.instance,
        SpotifyAlbumInfoServiceProvider.instance, SpotifyArtistTopTrackServiceProvider.instance, SpotifyArtistAlbumsServiceProvider.instance,
        SpotifyAlbumTracksServiceProvider.instance, SpotifyTokenDataServiceProvider.instance)



    val tokenCode = MutableLiveData<String>()
    val tokenData = MutableLiveData<JsonObject>()
    val selectedTerm = MutableLiveData<String>("short_term")
    val topTracks = MutableLiveData<TopTracksResponse>()

    val topArtists = MutableLiveData<TopArtistsResponse>()

    val searchResults = MutableLiveData<SpotifySearchResponse>()

    val recommendationResults = MutableLiveData<SpotifyRecommendationsResponse>()


    val selectedArtistInfo = MutableLiveData<SpotifyArtistInfoResponse>()
    val selectedTrackInfo = MutableLiveData<Track>()
    val selectedAlbumInfo = MutableLiveData<Album>()
    val selectedArtistTopTracks = MutableLiveData<SpotifyArtistTopTrackResponse>()
    val selectedArtistAlbums = MutableLiveData<SpotifyArtistAlbumsResponse>()

    val albumTracks = MutableLiveData<SpotifyAlbumTracksResponse>()

    val _selectedArtistID = MutableLiveData<String>()
    val _selectedTrackID = MutableLiveData<String>()
    val _selectedAlbumID = MutableLiveData<String>()









    // Public immutable data which the UI can observe


    fun exportTopTracksToJson(): String {
        val topTracksData = topTracks.value
        return Gson().toJson(topTracksData)
    }

    fun exportTopArtistsToJson(): String {
        val topArtistsData = topArtists.value
        return Gson().toJson(topArtistsData)
    }

    fun saveSelectedTerm(term: String) {
        val result = "short term"
        result?.let {
            selectedTerm.postValue(it)
        }
    }



    fun saveSelectedArtist(artistID: String) {
        val result = "0TnOYISbd1XYRBk9myaseg"
        result?.let {
            _selectedArtistID.postValue(it)
        }
    }
    fun saveSelectedTrack(trackID: String) {
        val result = "11dFghVXANMlKmJXsNCbNl"
        result?.let {
            _selectedTrackID.postValue(it)
        }
    }

    fun saveSelectedAlbum(albumID: String) {
        val result = "4aawyAB9vmqN3uQ7FjRGTy"
        result?.let {
            _selectedAlbumID.postValue(it)
        }
    }
    open fun getUserTopTracksBatch() {
        viewModelScope.launch {
            var offset = 0
            val accumulatedTracks = mutableListOf<Track>()
            var lastResponse: TopTracksResponse? = null

            repeat(6) {
                val result = repository.getUserTopTracks(token, selectedTerm.value.toString(), offset)
                lastResponse = result
                result?.items?.let {
                    accumulatedTracks.addAll(it)
                } // Handle null results appropriately
                offset += 50
            }

            lastResponse?.let {
                topTracks.postValue(TopTracksResponse(
                    href = it.href,
                    limit = it.limit,
                    next = it.next,
                    offset = offset,
                    previous = it.previous,
                    total = offset,
                    items = accumulatedTracks
                ))
            }
        }
    }
    open fun getUserTopArtistsBatch() {
        viewModelScope.launch {
            var offset = 0
            val accumulatedArtists = mutableListOf<Artist>()
            var lastResponse: TopArtistsResponse? = null

            repeat(6) {
                val result = repository.getUserTopArtist(token, selectedTerm.value.toString(), offset)
                lastResponse = result
                result?.items?.let {
                    Log.d("Mainhost", "${it.joinToString(separator = ",") }")
                    accumulatedArtists.addAll(it)
                } // Handle null results appropriately
                offset += 50
            }

            lastResponse?.let {
                topArtists.postValue(TopArtistsResponse(
                    href = it.href,
                    limit = it.limit,
                    next = it.next,
                    offset = offset,
                    previous = it.previous,
                    total = offset,
                    items = accumulatedArtists
                ))
            }
        }
    }



    open fun getUserTopTracks() {
        viewModelScope.launch {
            Log.d("getUserTopTracks", "Fetching top tracks...")
            Log.d("getUserTopTracks", token)
            val result = repository.getUserTopTracks(token, selectedTerm.value.toString(), 0)
            result?.let {
                topTracks.postValue(it)
                Log.d("getUserTopTracks", "Top tracks fetched successfully")
                Log.d("getUserTopTracks", "$it")

            } ?: run {
                Log.d("getUserTopTracks", "Failed to fetch top tracks")
            }
        }
    }

    open fun getUserTopArtists() {
        viewModelScope.launch {
            val result = repository.getUserTopArtist(token, selectedTerm.value.toString(), 0)
            result?.let {
                topArtists.postValue(it)
            } // Add error handling if result is null
        }
    }

    open fun getSelectedArtist() {
        viewModelScope.launch {
            val result = repository.getSelectedArtistInfo(token, _selectedArtistID.value.toString())
            result?.let{
                selectedArtistInfo.postValue(it)
            }
        }
    }
    open fun getSelectedArtistTopTracks() {
        viewModelScope.launch {
            val result = repository.getSelectedArtistTopTracks(token, _selectedArtistID.value.toString())
            result?.let {
                selectedArtistTopTracks.postValue(it)
            }
        }
    }

    open fun getSelectedArtistAlbums() {
        viewModelScope.launch {
            try {
                val result = repository.getSelectedArtistAlbums(token, _selectedArtistID.value.toString())
                result?.let {
                    selectedArtistAlbums.postValue(it)
                    Log.d(TAG, "Selected artist albums: $it")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
            }
        }
    }

    open fun getSelectedTrack() {
        viewModelScope.launch {
            val result = repository.getSelectedTrackInfo(token, _selectedTrackID.value.toString())
            result?.let {
                selectedTrackInfo.postValue(it)
            }
        }
    }

    open fun getSelectedAlbum() {
        viewModelScope.launch {
            try {
                val result = repository.getSelectedAlbumInfo(token, "0TnOYISbd1XYRBk9myaseg")
                result?.let {
                    selectedAlbumInfo.postValue(it)
                    Log.d("getSelectedAlbum", "Selected album info: $it")
                }
            } catch (e: Exception) {
                Log.e("getSelectedAlbum", "Exception: ${e.message}")
            }
        }
    }

    open fun getAlbumTracks() {
        viewModelScope.launch {
            try {
                val result = repository.getAlbumTracks(token, _selectedAlbumID.value.toString())
                result?.let {
                    albumTracks.postValue(it)
                    Log.d("getAlbumTracks", "Album tracks: $it")
                }
            } catch (e: Exception) {
                Log.e("getAlbumTracks", "Exception: ${e.message}")
            }
        }
    }


    open fun removeRatedTrack(trackId: String) {
        val currentTracks = recommendationResults.value?.tracks?.toMutableList()
        currentTracks?.let {
            it.removeAll { track -> track.id == trackId }
            recommendationResults.postValue(recommendationResults.value?.copy(tracks = it))
        }
    }
    open fun search(searchQuery: String) {
        viewModelScope.launch {
            val result = repository.search(token, searchQuery)
            result?.let {
                searchResults.postValue(it)
            }
        }
    }

    open fun getRecommendation(recommendationQuery: String, trackQuery: String, artistsQuery: String) {
        viewModelScope.launch {
            val result = repository.getRecommendation(token,recommendationQuery,artistsQuery,trackQuery)
            result?.let {
                recommendationResults.postValue(it)
            }
        }
    }

    fun saveTokenCode(code: String?) {
        val result = code
        result?.let{
            tokenCode.postValue(it)
        }

    }



    fun exchangeCodeForToken() {
        viewModelScope.launch{
            val result = repository.exchangeCodeForToken(tokenCode.value.toString())
            Log.d("MainHost",  "here112121")
            Log.d("MainHost",  " aaaa ${result.toString()}")

            result?.let{
                tokenData.postValue(it)
            }
        }
    }


}

open class SpotifyRepository(private val spotifyTopTracksService: SpotifyTopTracksService, private val spotifyTopArtistsService: SpotifyTopArtistsService, private val spotifySearchService: SpotifySearchService,
                             private val spotifyRecommendationsService: SpotifyRecommendationsService, private val spotifyArtistInfoService: SpotifyArtistInfoService,
                             private val spotifyTrackInfoService: SpotifyTrackInfoService, private val spotifyAlbumInfoService : SpotifyAlbumInfoService,
                             private val spotifyArtistTopTrackService: SpotifyArtistTopTrackService, private val spotifyArtistAlbumsService: SpotifyArtistAlbumsService,
                             private val spotifyAlbumTracksService: SpotifyAlbumTracksService, private val spotifyTokenDataService: SpotifyTokenDataService,
                             ) {
    open suspend fun getUserTopTracks(token: String?, term: String, offset: Int): TopTracksResponse? {
        return try {
            val response = spotifyTopTracksService.getUserTopTracks("Bearer $token", range = term, limit = 50, offset = offset)
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("getUserTopTracks", "Response: $responseBody")
                responseBody
            } else {
                Log.d("getUserTopTracks", "Unsuccessful response: ${response.code()}")
                null // or handle error response
            }
        } catch (e: Exception) {
            Log.e("getUserTopTracks", "Exception: ${e.message}")
            null // or handle exception
        }
    }

    open suspend fun getUserTopArtist(token: String?, term: String, offset: Int) : TopArtistsResponse? {
        return try {
            val response = spotifyTopArtistsService.getUserTopArtists(authHeader = "Bearer $token", range = term, limit = 50, offset = offset)
            if(response.isSuccessful) {
                response.body()
            } else{
                null
            }

        } catch (e: Exception) {
            null
        }
    }
    open suspend fun getSelectedArtistInfo(token: String?, selectedArtistId : String) : SpotifyArtistInfoResponse? {
        return try {
            Log.d("RegistrationActivity", "Pickle Riiiiick")
            val response = spotifyArtistInfoService.getArtistInfo("Bearer $token", selectedArtistId)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                Log.d("RegistrationActivity", "And every time I look at you")
                null
            }
        } catch(e: Exception) {
            null
        }
    }
    open suspend fun getSelectedArtistTopTracks(token: String?, selectedArtistId: String) : SpotifyArtistTopTrackResponse? {
        return try {
            Log.d("RegistrationActivity", "Hello There buradayım")
            val response = spotifyArtistTopTrackService.getArtistTopTracks("Bearer $token","0TnOYISbd1XYRBk9myaseg")
            Log.d("RegistrationActivity", response.code().toString())
            if(response.isSuccessful) {
                Log.d("RegistrationActivity", "Hello There satoptracks")
                Log.d("RegistrationActivity", response.body().toString())
                Log.d("RegistrationActivity", "Hello There satoptracks")
                response.body()

            }
            else {
                Log.d("Registration Activity", "Hello There")
                null
            }

        }
        catch (e: Exception) {

            Log.d("RegistrationActivity", "ao")
            null
        }
    }
    open suspend fun getSelectedArtistAlbums(token: String?,selectedArtistId: String): SpotifyArtistAlbumsResponse? {
        return try {
            val response = spotifyArtistAlbumsService.getArtistAlbums("Bearer $token","0TnOYISbd1XYRBk9myaseg" )
            if(response.isSuccessful){
                val responseBody = response.body()
                Log.d(TAG, "Album tracks response: ${responseBody?.toString() ?: "Empty response"}")
                responseBody
            }
            else {
                Log.d(TAG, "Unsuccessful response: ${response.code()}")
                null
            }
        }
        catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            null

        }
    }
    open suspend fun getAlbumTracks(token: String?, selectedAlbumId: String): SpotifyAlbumTracksResponse? {
        return try {
            val response = spotifyAlbumTracksService.getAlbumTracks("Bearer $token","4aawyAB9vmqN3uQ7FjRGTy")
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(" getAlbumTracks", "Album tracks response: $responseBody")
                responseBody
            } else {
                Log.d(" getAlbumTracks", "Unsuccessful response: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e(" getAlbumTracks", "Exception: ${e.message}")
            null
        }
    }

    open suspend fun getSelectedTrackInfo(token: String?, selectedTrackId: String) : Track?{
        return try {
            //Log.d()
            val response = spotifyTrackInfoService.getTrackInfo("Bearer $token", selectedTrackId)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }
        }
        catch(e: Exception) {
            null
        }
    }


    open suspend fun getSelectedAlbumInfo(token: String?, selectedAlbumId: String) : Album? {
        return try {
            val response = spotifyAlbumInfoService.getAlbumInfo("Bearer $token", "4aawyAB9vmqN3uQ7FjRGTy")
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }
        }
        catch(e: Exception) {
            null
        }
    }
    open suspend fun search(token: String?, searchQuery : String) : SpotifySearchResponse? {
        return try {
            Log.d("RegistrationActivity", "Every time I see your face")
            val response = spotifySearchService.searchSpotify("Bearer $token", searchQuery, "track,album,artist")
            if (response.isSuccessful) {
                Log.d("RegistrationActivity", "I know the love that lies and waits for me")
                parseSpotifySearchResponse(response.body())
                //response.body()
            } else {
                Log.d("RegistrationActivity", "And every time I look at you")
                null
            }
        } catch(e: Exception) {
            null
        }

    }
    //TODO:: Implement user entry recommendation
    open suspend fun getRecommendation(token: String?, recommendationQuery: String , artistsQuery: String, trackQuery: String) : SpotifyRecommendationsResponse? {
        return try {
            Log.d("RegistrationActivity", "big in japan")
            val response = spotifyRecommendationsService.getRecommendations("Bearer $token", 10, "TR", seedGenres = recommendationQuery, seedArtists = artistsQuery, seedTracks = trackQuery)
            if(response.isSuccessful) {
                Log.d("RegistrationActivity", response.body().toString())
                response.body()
            } else {
                Log.d("RegistrationActivity", "And every time I look at you")
                null
            }
        } catch(e: Exception) {
            null
        }
    }

    private fun parseSpotifySearchResponse(rawResponse: RawSpotifySearchResponse?): SpotifySearchResponse {
        val items = mutableListOf<SpotifySearchItem>()

        rawResponse?.tracks?.items?.forEach { track ->
            items.add(SpotifySearchItem.TrackItem(track))
        }

        rawResponse?.albums?.items?.forEach { album ->
            items.add(SpotifySearchItem.AlbumItem(album))
        }

        rawResponse?.artists?.items?.forEach { artist ->
            items.add(SpotifySearchItem.ArtistItem(artist))
        }
        //TODO:: assign proper href values
        return SpotifySearchResponse(

            href = "", // Assuming these fields are at the top level of the response
            limit = 0,
            next = "",
            offset = 0,
            previous = "",
            total = 0,
            items = items,
        )
    }


    open suspend fun exchangeCodeForToken(tokenCode: String): JsonObject? {
        val url = "https://accounts.spotify.com/api/token"
        val client = OkHttpClient()
        val REQUEST_CODE = 1337
        val REDIRECT_URI = "com.example.start2://callback"
        val CLIENT_ID = "67cce911671f41adaa404b600f14aa90"
        val CLIENT_SECRET = "1cbc10e1bed94c1192d7b9c94fb376a5"


// Construct the JsonObject using Gson
        val jsonBody = JsonObject().apply {
            addProperty("grant_type", "authorization_code")
            addProperty("code", tokenCode)
            addProperty("redirect_uri", REDIRECT_URI)
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET) // Warning: Including the client secret in your app is insecure
        }
        Log.d("MainHost",  "here211")

        return try {
            Log.d("MainHost",  "here211")
            val response = spotifyTokenDataService.getTokenData(jsonBody)
            Log.d("MainHost",  "here211")
            if(response.isSuccessful) {
                Log.d("MainHost",  "${response.body()}")
                response.body()
            }
            else{
                Log.d("MainHost",  "here2")
                null
            }

        } catch (e: Exception) {
            Log.d("MainHost",  "${e.message.toString()}")
            null
        }

    }


}





