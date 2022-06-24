package dang.lukas.ui.photos

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dang.lukas.R
import dang.lukas.data.PhotoRepository
import dang.lukas.domain.Photo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

enum class PhotoApiStatus { LOADING, ERROR, DONE }

internal const val KEY_QUERY = "query"

/**
 * The view model for loading photos from the Pixabay website
 */
class PhotosViewModel(
    private val photoRepository: PhotoRepository,
    private val resources: Resources
) : ViewModel() {

    private val _photos = MutableLiveData<MutableList<Photo>>(mutableListOf())
    val photos: LiveData<MutableList<Photo>> = _photos

    private val _status = MutableLiveData<PhotoApiStatus>()
    val status: LiveData<PhotoApiStatus> = _status

    private val _title = MutableLiveData("Recent")
    val title: LiveData<String> = _title

    private val _photoCountText = MutableLiveData<String>()
    val photoCountText: LiveData<String> = _photoCountText

    /**
     * Used for Swipe-to-Refresh
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Memory cache for the query text
     */
    private var latestQuery = ""

    /**
     * Load both recent photos and photos searched by a keywords
     */
    fun loadPhotos(query: String) {
        viewModelScope.launch {
            val result =
                if (query.isBlank()) photoRepository.getRecentPhotos()
                else photoRepository.searchPhotos(query)

            result.onStart {
                updateLatestQuery(query)
                updateTitle(query)
                _status.value = PhotoApiStatus.LOADING
                _isLoading.value = true
            }.catch {
                _status.value = PhotoApiStatus.ERROR
                _photos.value = mutableListOf()
                it.printStackTrace()
                _isLoading.value = false
            }.collect {
                _photos.value = it
                _status.value = PhotoApiStatus.DONE
                updatePhotoCount()
                _isLoading.value = false
            }
        }
    }

    /**
     * Load photos saved after configuration change
     */
    fun loadRecentPhotos() {
        loadPhotos("")
    }

    fun reloadPhotos() {
        loadPhotos(latestQuery)
    }

    private fun updateLatestQuery(query: String) {
        latestQuery = query
    }

    private fun updatePhotoCount() {
        _photoCountText.value = _photos.value?.let {
            if (it.size == 0) {
                resources.getString(R.string.no_results)
            } else {
                resources.getQuantityString(
                    R.plurals.photo_count,
                    it.size,
                    it.size
                )
            }
        }
    }

    private fun updateTitle(query: String) {
        if (query.isBlank()) {
            _title.value = "Recent"
        } else {
            _title.value = "\"$query\""
        }
    }
}