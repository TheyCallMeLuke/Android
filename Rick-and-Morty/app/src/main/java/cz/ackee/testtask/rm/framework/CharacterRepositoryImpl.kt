package cz.ackee.testtask.rm.framework

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import cz.ackee.testtask.rm.data.CharacterRepository
import cz.ackee.testtask.rm.data.LocalCharacterDataSource
import cz.ackee.testtask.rm.data.RemoteCharacterDataSource
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.paging.GetAllCharactersPagingSource
import cz.ackee.testtask.rm.framework.paging.GetFilteredCharactersPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.dsl.module

val dataModule = module {
    single<RemoteCharacterDataSource> { RetrofitCharacterDataSource(get()) }
    single<LocalCharacterDataSource> { RoomCharacterDataSource(get()) }
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
}

class CharacterRepositoryImpl(
    private val remoteDataSource: RemoteCharacterDataSource,
    private val localDataSource: LocalCharacterDataSource,
) : CharacterRepository {

    override suspend fun getAllCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetAllCharactersPagingSource(remoteDataSource) }
        ).flow.map { pagingData -> pagingData.map { character -> character.withSavedFlag() } }
    }

    override suspend fun filterCharacters(query: String): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetFilteredCharactersPagingSource(query, remoteDataSource) }
        ).flow.map { pagingData -> pagingData.map { character -> character.withSavedFlag() } }
    }

    override suspend fun getSavedCharacters() =
        localDataSource.getSavedCharacters().withSavedFlags()

    override suspend fun getCharacter(id: Long) = remoteDataSource.getCharacter(id).withSavedFlag()

    override suspend fun saveCharacter(character: Character) =
        localDataSource.saveCharacter(character)

    override suspend fun removeCharacter(character: Character) =
        localDataSource.removeCharacter(character)

    private suspend fun List<Character>.withSavedFlags(): List<Character> =
        this.map { it.withSavedFlag() }

    private suspend fun Character.withSavedFlag(): Character {
        this.isSaved = localDataSource.isCharacterSaved(this.id)
        return this
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}