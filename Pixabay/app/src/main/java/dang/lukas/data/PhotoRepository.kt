package dang.lukas.data

import dang.lukas.domain.Photo
import dang.lukas.domain.PhotosResponseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepository(private val photosRemoteDataSource: PhotosRemoteDataSource) {

    fun getRecentPhotos(): Flow<MutableList<Photo>> =
        mapEntityToPhoto(photosRemoteDataSource.getRecentPhotos())

    fun searchPhotos(query: String): Flow<MutableList<Photo>> =
        mapEntityToPhoto(photosRemoteDataSource.searchPhotos(query))

    fun getPhotoById(id: Long): Flow<Photo> =
        mapEntityToPhoto(photosRemoteDataSource.getPhoto(id)).map { it[0] }

    private fun mapEntityToPhoto(result: Flow<PhotosResponseEntity>) =
        result.map { photosResponseEntity ->
            photosResponseEntity.photoEntities.map {
                Photo(
                    it.id,
                    it.tags,
                    it.url,
                    it.user,
                    "${it.views.toString()} views",
                    "${it.downloads.toString()} downloads",
                    "${it.likes.toString()} likes",
                    "${it.comments.toString()} comments",
                    it.profilePictureUrl,
                    it.pageUrl,
                    it.userId
                )
            }.toMutableList()
        }

}