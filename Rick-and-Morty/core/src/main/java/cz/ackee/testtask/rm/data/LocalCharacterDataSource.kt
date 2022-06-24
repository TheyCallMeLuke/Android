package cz.ackee.testtask.rm.data

import cz.ackee.testtask.rm.domain.Character

interface LocalCharacterDataSource {

    suspend fun saveCharacter(character: Character)

    suspend fun removeCharacter(character: Character)

    suspend fun getSavedCharacters(): List<Character>

    suspend fun isCharacterSaved(id: Long): Boolean
}