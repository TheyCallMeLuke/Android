package cz.ackee.testtask.rm.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.Interactors
import kotlinx.coroutines.flow.Flow

class CharactersViewModel(private val interactors: Interactors) : ViewModel() {

    /**
     * Used to restore the search view after a configuration change.
     */
    var lastQuery = ""

    /**
     * Used to render a different UI when filtering characters.
     */
    var filteringOn: Boolean = false

    suspend fun filterCharacters(query: String): Flow<PagingData<Character>> {
        lastQuery = query
        return interactors.filterCharacters(query)
    }

    suspend fun loadCharacters(): Flow<PagingData<Character>> = interactors.getAllCharacters()
}