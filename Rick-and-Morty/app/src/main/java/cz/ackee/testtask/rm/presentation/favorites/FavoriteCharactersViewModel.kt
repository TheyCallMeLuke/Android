package cz.ackee.testtask.rm.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteCharactersViewModel(private val interactors: Interactors) : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    fun loadFavoriteCharacters() = viewModelScope.launch(Dispatchers.IO) {
        _characters.postValue(interactors.getFavoriteCharacters())
    }

}
