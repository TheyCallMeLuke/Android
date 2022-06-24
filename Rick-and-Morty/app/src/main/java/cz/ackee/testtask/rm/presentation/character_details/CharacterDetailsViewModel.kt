package cz.ackee.testtask.rm.presentation.character_details

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.ackee.testtask.rm.R
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.Interactors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class CharacterDetailsViewModel(
    private val application: Application, private val interactors: Interactors,
) : ViewModel() {

    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> = _character

    private var isSaved by Delegates.notNull<Boolean>()

    /**
     * The isSaved variable is stored locally for simplicity.
     */
    fun loadCharacter(id: Long) =
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedCharacter = interactors.getCharacter(id)
            isSaved = fetchedCharacter.isSaved
            _character.postValue(fetchedCharacter)
        }

    private fun saveCharacter() {
        isSaved = true
        viewModelScope.launch(Dispatchers.IO) {
            character.value?.let { interactors.saveCharacter(it) }
        }
    }

    private fun removeCharacter() {
        isSaved = false
        viewModelScope.launch(Dispatchers.IO) {
            character.value?.let { interactors.removeCharacter(it) }
        }
    }

    /**
     * Save/Remove character from the DB and change the favorites icon appropriately.
     */
    fun handleSaveAndReturnToggledSaveIcon() =
        if (isSaved) {
            removeCharacter()
            getDrawable(R.drawable.ic_24_favorites_inactive)
        } else {
            saveCharacter()
            getDrawable(R.drawable.ic__24_favorites_active)
        }

    fun getFavoriteIcon() =
        if (isSaved) {
            getDrawable(R.drawable.ic__24_favorites_active)
        } else {
            getDrawable(R.drawable.ic_24_favorites_inactive)
        }

    private fun getDrawable(drawableId: Int) =
        ResourcesCompat.getDrawable(application.applicationContext.resources, drawableId, null)

}
