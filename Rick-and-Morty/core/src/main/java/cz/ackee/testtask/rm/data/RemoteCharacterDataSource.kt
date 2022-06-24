package cz.ackee.testtask.rm.data

import cz.ackee.testtask.rm.domain.Character

interface RemoteCharacterDataSource {

    suspend fun getAllCharacters(position: Int): List<Character>

    suspend fun getCharacter(id: Long): Character

    suspend fun filterCharacters(position: Int, query: String): List<Character>

}