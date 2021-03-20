package tech.danielwaiguru.dscmuranga.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.viewmodel.ext.android.viewModel
import tech.danielwaiguru.dscmuranga.R
import tech.danielwaiguru.dscmuranga.ui.auth.login.SignInViewModel

class MainActivity : AppCompatActivity() {
    private val signInViewModel: SignInViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DscMuranga_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSession()
    }
    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    private fun getSession() {
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        if (signInViewModel.getCurrentUser() != null) {
            graph.startDestination = R.id.homeFragment
        } else {
            graph.startDestination = R.id.signInFragment
        }
        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)

    }
}