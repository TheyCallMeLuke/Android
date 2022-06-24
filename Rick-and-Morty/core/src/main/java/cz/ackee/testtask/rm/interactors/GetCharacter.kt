package cz.ackee.testtask.rm.interactors

import cz.ackee.testtask.rm.data.CharacterRepository
import cz.ackee.testtask.rm.domain.Character

class GetCharacter(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke(id: Long): Character = characterRepository.getCharacter(id)
}