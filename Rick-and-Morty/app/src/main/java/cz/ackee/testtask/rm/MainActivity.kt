package cz.ackee.testtask.rm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import cz.ackee.testtask.rm.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavController()
        setupToolbar()
        setupBottomNav()
    }

    private fun setupNavController() {
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            // Enables inflation of custom  menu
            setSupportActionBar(this)

            /**
             * Enables automatic pop of a fragment from the backstack when navigating the bottom
             * navigation.
             */
            val appBarConfiguration =
                AppBarConfiguration.Builder(R.id.navigation_characters, R.id.navigation_favorites)
                    .build()
            this.setupWithNavController(navController, appBarConfiguration)
        }
    }

    /**
     * Delegates the management of the fragment backstack when navigating the bottom navigation
     * to the Navigation Component.
     */
    private fun setupBottomNav() {
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
    }
}
