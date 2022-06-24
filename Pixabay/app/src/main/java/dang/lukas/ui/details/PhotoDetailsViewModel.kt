package dang.lukas.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dang.lukas.data.PhotoRepository
import dang.lukas.domain.Photo
import dang.lukas.ui.photos.PhotoApiStatus
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PhotoDetailsViewModel(private val photoRepository: PhotoRepository) : ViewModel() {

    private val _status = MutableLiveData<PhotoApiStatus>()
    val status: LiveData<PhotoApiStatus> = _status

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo> = _photo

    fun loadPhoto(id: Long) {
        viewModelScope.launch {
            photoRepository.getPhotoById(id)
                .onStart {
                    _status.value = PhotoApiStatus.LOADING
                }
                .catch {
                    _status.value = PhotoApiStatus.ERROR
                    it.printStackTrace()
                }
                .collect {
                    _photo.value = it
                    _status.value = PhotoApiStatus.DONE
                }
        }
    }
}