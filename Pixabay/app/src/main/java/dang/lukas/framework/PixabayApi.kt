package dang.lukas.framework

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dang.lukas.domain.PhotosResponseEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_API_URL = "https://www.pixabay.com/"

interface PixabayApi {

    @GET("/api")
    suspend fun getRecent(
        @Query("key") apiKey: String,
        @Query("order") order: String = "latest",
        @Query("per_page") perPage: Int = 20,
        @Query("safesearch") safeSearch: Boolean = true
    ): PhotosResponseEntity

    @GET("/api")
    suspend fun searchByQuery(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 20,
        @Query("safesearch") safeSearch: Boolean = true
    ): PhotosResponseEntity

    @GET("/api")
    suspend fun searchById(
        @Query("key") apiKey: String,
        @Query("id") id: Long,
    ): PhotosResponseEntity
}

object PhotoApi {
    val service: PixabayApi by lazy {
        retrofit.create(PixabayApi::class.java)
    }
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
    return OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .build()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_API_URL)
    .client(provideOkHttpClient())
    .build()