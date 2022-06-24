package cz.ackee.testtask.rm.framework

import cz.ackee.testtask.rm.data.LocalCharacterDataSource
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.db.character.CharacterDao
import cz.ackee.testtask.rm.framework.db.character.CharacterDbEntity

class RoomCharacterDataSource(private val characterDao: CharacterDao) : LocalCharacterDataSource {

    override suspend fun saveCharacter(character: Character) =
        characterDao.insert(character.toCharacterDbEntity())

    override suspend fun removeCharacter(character: Character) =
        characterDao.delete(character.toCharacterDbEntity())

    override suspend fun getSavedCharacters() =
        characterDao.getAll().toCharacters()

    override suspend fun isCharacterSaved(id: Long): Boolean =
        characterDao.exists(id)
}

/**
 * An empty type should be transformed to a '-' character according to the task.
 */
private fun Character.toCharacterDbEntity() = CharacterDbEntity(
    id = this.id,
    name = this.name,
    status = this.status,
    species = this.species,
    type = if (this.type.isEmpty()) "-" else this.type,
    gender = this.gender,
    origin = this.origin.name,
    location = this.location.name,
    image = this.image
)

private fun List<CharacterDbEntity>.toCharacters(): List<Character> =
    this.map { it.toCharacter() }


private fun CharacterDbEntity.toCharacter(): Character =
    Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = if (this.type.isEmpty()) "-" else this.type,
        gender = this.gender,
        origin = Character.Origin(this.origin),
        location = Character.Location(this.location),
        image = this.image
    )