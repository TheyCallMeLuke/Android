package dang.lukas.data

import dang.lukas.domain.PhotosResponseEntity
import dang.lukas.framework.PixabayApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotosRemoteDataSource(private val api: PixabayApi) {

    fun getRecentPhotos(): Flow<PhotosResponseEntity> = flow {
        val photosResponseEntity = api.getRecent(API_KEY)
        emit(photosResponseEntity)
    }

    fun searchPhotos(query: String): Flow<PhotosResponseEntity> = flow {
        val photosResponseEntity = api.searchByQuery(API_KEY, query)
        emit(photosResponseEntity)
    }

    fun getPhoto(id: Long): Flow<PhotosResponseEntity> = flow {
        val photosResponseEntity = api.searchById(API_KEY, id)
        emit(photosResponseEntity)
    }
}
