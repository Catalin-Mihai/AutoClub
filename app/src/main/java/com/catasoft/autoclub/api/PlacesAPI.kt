package com.catasoft.autoclub.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class PlacesAPI {
    private var apiBuilder: PlacesAPIService? = null
    val instance: PlacesAPIService?
        get() {
            if (apiBuilder == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
//                    .addConverterFactory(MoshiConverterFactory.create().asLenient())
                    .build()
                apiBuilder = retrofit.create(PlacesAPIService::class.java)
            }
            return apiBuilder
        }

    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"
    }
}