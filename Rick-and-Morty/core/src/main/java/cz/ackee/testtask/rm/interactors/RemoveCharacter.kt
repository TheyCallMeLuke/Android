package cz.ackee.testtask.rm.interactors

import cz.ackee.testtask.rm.data.CharacterRepository
import cz.ackee.testtask.rm.domain.Character

class RemoveCharacter(private val characterRepository: CharacterRepository) {

    suspend operator fun invoke(character: Character) =
        characterRepository.removeCharacter(character)
}