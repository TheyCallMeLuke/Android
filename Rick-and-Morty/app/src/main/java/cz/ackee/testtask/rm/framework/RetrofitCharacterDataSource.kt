package cz.ackee.testtask.rm.framework

import cz.ackee.testtask.rm.data.RemoteCharacterDataSource
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.api.CharacterResponseEntity
import cz.ackee.testtask.rm.framework.api.RickAndMortyService

class RetrofitCharacterDataSource(private val service: RickAndMortyService) :
    RemoteCharacterDataSource {

    override suspend fun getAllCharacters(position: Int) = service.getAll(position)
        .characterResponseEntities
        .toCharacters()

    override suspend fun getCharacter(id: Long): Character =
        service.getCharacterEntity(id).toCharacter()

    override suspend fun filterCharacters(position: Int, query: String) =
        service.getFiltered(position, query).characterResponseEntities.toCharacters()

}

private fun List<CharacterResponseEntity>.toCharacters(): List<Character> =
    this.map { it.toCharacter() }

/**
 * An empty type should be transformed to a '-' character according to the task.
 */
private fun CharacterResponseEntity.toCharacter(): Character =
    Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = if (this.type.isEmpty()) "-" else this.type,
        gender = this.gender,
        origin = Character.Origin(this.origin.name),
        location = Character.Location(this.location.name),
        image = this.image
    )