package com.catasoft.autoclub.api

import com.google.common.io.Resources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesAPIService {
    @GET("photo?")
    fun getPhoto(
        @Query("maxwidth") maxWidth: Int = 400,
        @Query("photoreference") photoReference: String,
        @Query("key") apiKey: String
    ): Call<String?>?
}
