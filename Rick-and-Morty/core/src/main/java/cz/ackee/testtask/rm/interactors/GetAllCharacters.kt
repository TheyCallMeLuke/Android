package cz.ackee.testtask.rm.interactors

import cz.ackee.testtask.rm.data.CharacterRepository

class GetAllCharacters(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke() = characterRepository.getAllCharacters()
}