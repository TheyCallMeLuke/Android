package cz.ackee.testtask.rm

import android.app.Application
import cz.ackee.testtask.rm.framework.api.apiModule
import cz.ackee.testtask.rm.framework.dataModule
import cz.ackee.testtask.rm.framework.db.AppDatabase
import cz.ackee.testtask.rm.framework.interactorsModule
import cz.ackee.testtask.rm.presentation.character_details.CharacterDetailsViewModel
import cz.ackee.testtask.rm.presentation.characters.CharactersViewModel
import cz.ackee.testtask.rm.presentation.favorites.FavoriteCharactersViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    /**
     * Koin modules that need a reference to the application are defined in this class.
     */
    private val viewModelModule = module {
        viewModel { CharactersViewModel(get()) }
        viewModel { CharacterDetailsViewModel(androidApplication(), get()) }
        viewModel { FavoriteCharactersViewModel(get()) }
    }

    private val databaseModule = module {
        single { AppDatabase.getInstance(applicationContext).characterDao() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(apiModule, databaseModule, dataModule, interactorsModule, viewModelModule)
        }
    }
}