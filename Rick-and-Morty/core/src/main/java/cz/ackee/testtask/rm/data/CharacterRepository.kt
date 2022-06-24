package cz.ackee.testtask.rm.data

import androidx.paging.PagingData
import cz.ackee.testtask.rm.domain.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    suspend fun getAllCharacters(): Flow<PagingData<Character>>

    suspend fun getCharacter(id: Long): Character

    suspend fun saveCharacter(character: Character)

    suspend fun removeCharacter(character: Character)

    suspend fun filterCharacters(query: String): Flow<PagingData<Character>>

    suspend fun getSavedCharacters(): List<Character>

}
