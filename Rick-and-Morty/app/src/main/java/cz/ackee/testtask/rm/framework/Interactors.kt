package cz.ackee.testtask.rm.framework

import cz.ackee.testtask.rm.interactors.*
import org.koin.dsl.module

val interactorsModule = module {
    single { SaveCharacter(get()) }
    single { GetAllCharacters(get()) }
    single { GetCharacter(get()) }
    single { RemoveCharacter(get()) }
    single { FilterCharacters(get()) }
    single { GetFavoriteCharacters(get()) }
    single { Interactors(get(), get(), get(), get(), get(), get()) }
}

data class Interactors(
    val saveCharacter: SaveCharacter,
    val getAllCharacters: GetAllCharacters,
    val getCharacter: GetCharacter,
    val removeCharacter: RemoveCharacter,
    val filterCharacters: FilterCharacters,
    val getFavoriteCharacters: GetFavoriteCharacters,
)