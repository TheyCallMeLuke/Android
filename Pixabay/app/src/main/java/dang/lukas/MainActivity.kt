package dang.lukas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import dang.lukas.data.PhotoRepository
import dang.lukas.data.PhotosRemoteDataSource
import dang.lukas.framework.PhotoApi
import dang.lukas.ui.details.PhotoDetailsViewModel
import dang.lukas.ui.photos.PhotosViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * The main activity that hosts a fragment container
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val appModule = module {
        single { resources }
        single { PhotoApi.service }
        single { PhotosRemoteDataSource(get()) }
        single { PhotoRepository(get()) }
        viewModel { PhotosViewModel(get(), get()) }
        viewModel { PhotoDetailsViewModel(get()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(appModule)
            }
        }

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    /**
     * Called by fragments to create a different toolbar for each fragment
     */
    fun setupActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }

    /**
     * Support for navigation up (aka back button) in Navigation Component
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}