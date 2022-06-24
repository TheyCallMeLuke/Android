package com.example.flickrreranker.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.flickrreranker.R
import com.example.flickrreranker.app.framework.network.PhotoApi
import com.example.flickrreranker.data.PhotoRepository
import com.example.flickrreranker.model.PhotoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * The main activity that hosts individual fragments.
 */
class MainActivity : AppCompatActivity() {

    /**
     * The navigation controller for setting up navigation between fragments
     * with the navigation component.
     */
    private lateinit var navController: NavController

    /**
     * Defines the app module to be used by the Koin DI framework for defining dependencies.
     */
    private val appModule = module {
        single { PhotoApi.service }
        single { PhotoRepository(get()) }
        viewModel { PhotoViewModel(get()) }
    }

    /**
     * Starts the Koin framework and delegates the setup for the action bar
     * to the navigation component.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(appModule)
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

    /**
     * Enables to navigate up with the navigation component, if possible.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}