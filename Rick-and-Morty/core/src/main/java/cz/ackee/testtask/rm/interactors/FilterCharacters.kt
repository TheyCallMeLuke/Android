package cz.ackee.testtask.rm.interactors

import cz.ackee.testtask.rm.data.CharacterRepository

class FilterCharacters(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke(query: String) = characterRepository.filterCharacters(query)
}