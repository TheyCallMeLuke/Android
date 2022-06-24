package cz.ackee.testtask.rm.framework.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val apiModule = module { single { RickAndMortyApi.service } }

private const val BASE_URL = "https://rickandmortyapi.com/api/"

interface RickAndMortyService {

    @GET("character")
    suspend fun getAll(@Query("page") page: Int): CharactersResponse


    @GET("character/")
    suspend fun getFiltered(
        @Query("page") page: Int,
        @Query("name") name: String,
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacterEntity(
        @Path("id") name: Long,
    ): CharacterResponseEntity
}

object RickAndMortyApi {
    val service: RickAndMortyService by lazy {
        retrofit.create(RickAndMortyService::class.java)
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
    .baseUrl(BASE_URL)
    .client(provideOkHttpClient())
    .build()