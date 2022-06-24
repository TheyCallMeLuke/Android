package cz.ackee.testtask.rm.interactors

import cz.ackee.testtask.rm.data.CharacterRepository

class GetFavoriteCharacters(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke() = characterRepository.getSavedCharacters()
}