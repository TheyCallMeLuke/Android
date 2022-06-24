package com.example.flickrreranker.model

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrreranker.data.PhotoRepository
import com.example.flickrreranker.domain.Photo
import com.example.flickrreranker.model.algorithms.DistanceCalculator
import kotlinx.coroutines.launch

enum class PhotoApiStatus { LOADING, ERROR, DONE }

/**
 * The view model for the photos.
 */
class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {

    /**
     * This list contains enums indicating input errors which are observed by the edit texts.
     * It is used for rendering an error message on the UI for the user.
     */
    val formErrors = ObservableArrayList<FormErrors>()

    /**
     * The list of photos which is bind to the recycler view.
     */
    private val _photos = MutableLiveData<MutableList<Photo>>(mutableListOf())
    val photos: LiveData<MutableList<Photo>> = _photos

    /**
     * The API status indicating the status of the API call to fetch the photos. It is used to
     * render the status (loading, error, done) to the user.
     */
    private val _status = MutableLiveData<PhotoApiStatus>()
    val status: LiveData<PhotoApiStatus> = _status

    /**
     * The following are variables bind to the user inputs.
     */
    var keywords: String?
    var photoCount: Int?
    var author: String?
    var date: Long? // The target upload date of a photo in unix time.
    var latitude: Double? = null
    var longitude: Double? = null
    var viewCount: Int?

    var authorEnabled = true
    var dateEnabled = true
    var geoEnabled = true
    var viewCountEnabled = true

    /**
     * When opening the app, we use some custom data to show some photos to the user even before
     * the user searches anything.
     */
    init {
        keywords = "egg"
        photoCount = 5
        author = "luke"
        date = 1636280147
        latitude = 12.0
        longitude = 24.0
        viewCount = 100
        loadPhotos()
    }

    /**
     * Resets the data representing the user inputs. This is used right after the user clicks the
     * search options button. It makes sense, because when the user is done with choosing the
     * search options and submits the request, the app needs to verify that the data is valid. For
     * that the data must not contain data from any of the previous requests.
     */
    fun reset() {
        keywords = null
        photoCount = null
        author = null
        date = null
        latitude = null
        longitude = null
        viewCount = null
    }

    /**
     * Loads photos in a coroutine and sorts them according to a score which is defined in the
     * [DistanceCalculator] class.
     */
    fun loadPhotos() {
        viewModelScope.launch {
            try {
                _status.value = PhotoApiStatus.LOADING
                val photos = repository.searchPhotos(keywords!!, photoCount!!)
                sortPhotos(photos)
                _photos.value = photos
                _status.value = PhotoApiStatus.DONE
            } catch (e: Exception) {
                _status.value = PhotoApiStatus.ERROR
                _photos.value = mutableListOf()
                e.printStackTrace()
            }
        }
    }

    /**
     * Sort photos by their score.
     */
    private fun sortPhotos(photos: MutableList<Photo>) {
        val calc = DistanceCalculator(this@PhotoViewModel)
        calc.calculateScoreFromAuthor(photos)
        calc.calculateScoreFromDate(photos)
        calc.calculateScoreFromLocation(photos)
        calc.recalculateScoresWithViewCountDistances(photos)
        photos.sortByDescending { it.score }
    }

    /**
     * Checks if the data is valid. We mostly check for null and blank values. Optional inputs
     * can be invalid only when they are enabled. If they are disabled, it doesn't matter that the
     * input is.
     */
    fun isDataValid(): Boolean {
        formErrors.clear()
        if (keywords.isNullOrBlank()) formErrors.add(FormErrors.MISSING_KEYWORDS)
        if (photoCount == null) formErrors.add(FormErrors.MISSING_PHOTO_COUNT)
        if (photoCount == 0) formErrors.add(FormErrors.PHOTO_COUNT_IS_ZERO) // TODO errorText in layout file
        if (author.isNullOrBlank() && authorEnabled) formErrors.add(FormErrors.MISSING_USERNAME)
        if (date == null && dateEnabled) formErrors.add(FormErrors.MISSING_DATE)
        if (latitude == null && geoEnabled) formErrors.add(FormErrors.MISSING_GEO)
        if (longitude == null && geoEnabled) formErrors.add(FormErrors.MISSING_GEO)
        if (viewCount == null && viewCountEnabled) formErrors.add(FormErrors.MISSING_VIEW_COUNT)
        return formErrors.isEmpty()
    }

    /**
     * The form errors used by the input edit texts to render an error.
     */
    enum class FormErrors {
        MISSING_KEYWORDS,
        MISSING_PHOTO_COUNT,
        MISSING_USERNAME,
        MISSING_DATE,
        MISSING_GEO,
        MISSING_VIEW_COUNT,
        PHOTO_COUNT_IS_ZERO,
    }
}