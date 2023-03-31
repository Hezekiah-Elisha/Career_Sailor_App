package com.example.careersailorapp.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.careersailorapp.R
import com.example.careersailorapp.databinding.ActivityMainBinding
import com.example.careersailorapp.model.Article
import com.example.careersailorapp.model.ArticleRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val toolbar = binding.materialToolbar
//        setSupportActionBar(toolbar)

//        toolbar.title = "Career Sailor hahaha"


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.articlesFragment,
                R.id.mentorFragment,
                R.id.jobsFragment
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        val toolbar = binding.toolbar
//        toolbar.setupWithNavController(navController, appBarConfiguration)



        bottomNavigationView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.articlesFragment, R.id.mentorFragment, R.id.jobsFragment))
        return findNavController(R.id.myNavHostFragment)
            .navigateUp(appBarConfiguration = appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.it_profile -> {
//                Toast.makeText(this, "the Profile thing", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.it_logout -> {
//                Toast.makeText(this, "The logout thing", Toast.LENGTH_SHORT).show()
                auth = FirebaseAuth.getInstance()
                auth.signOut()

                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
//        return
    }
}