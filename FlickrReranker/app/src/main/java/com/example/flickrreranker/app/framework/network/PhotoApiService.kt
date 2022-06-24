package com.example.flickrreranker.app.framework.network

import com.example.flickrreranker.domain.SearchPhotoResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_API_URL = "https://www.flickr.com/services/"

private val moshi = Moshi.Builder() // check
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
    return OkHttpClient().newBuilder()
//        .connectTimeout(10000, TimeUnit.MILLISECONDS)
//        .readTimeout(10000, TimeUnit.MILLISECONDS)
        .addInterceptor(logging)
        .build()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_API_URL)
    .client(provideOkHttpClient())
    .build()

interface PhotoApiService {

    @GET("rest/?method=flickr.photos.search&format=json&nojsoncallback=1&per_page=5&extras=date_upload,%20owner_name,%20geo,%20views,%20url_c,%20license&page=1&has_geo=1&safe_search=1&content_type=1&privacy_filter=1&license=10,9,4,6,3,2,1,5,7")
    suspend fun searchPhotos(
        @Query("api_key") apiKey: String,
        @Query("per_page") maxPhotosPerPage: Int = 1,
        @Query("page") page: Int = 1,
        @Query("text") text: String
    ): SearchPhotoResponse
}

object PhotoApi {
    val service: PhotoApiService by lazy {
        retrofit.create(PhotoApiService::class.java)
    }
}