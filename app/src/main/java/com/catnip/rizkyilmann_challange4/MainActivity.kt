package com.catnip.rizkyilmann_challange4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.catnip.rizkyilmann_challange4.databinding.ActivityMainBinding
import com.catnip.rizkyilmann_challange4.ui.loginactivity.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == R.id.navigation_profile && FirebaseAuth.getInstance().currentUser == null) {
                controller.popBackStack()
                navigateToLogin()
            }
        }
        supportActionBar?.hide()
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
