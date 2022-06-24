package com.example.flickrreranker.data

import com.example.flickrreranker.app.framework.network.API_KEY
import com.example.flickrreranker.app.framework.network.PhotoApiService

/**
 * The repository used to fetch data.
 */
class PhotoRepository(private val service: PhotoApiService) {
    suspend fun searchPhotos(keywords: String, photoCount: Int) =
        service.searchPhotos(
            apiKey = API_KEY,
            maxPhotosPerPage = photoCount,
            text = keywords
        ).photosWrapper.photoData.toMutableList()
}