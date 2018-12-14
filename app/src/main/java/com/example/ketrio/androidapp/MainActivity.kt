package com.example.ketrio.androidapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ketrio.androidapp.R.id.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(),
    BlankFragment.OnFragmentInteractionListener,
    phoneInfoFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener,
    BlankFragment2.OnFragmentInteractionListener,
    BlankFragment3.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.let { bottomNavigationView ->
            NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
        }

        intent?.data?.let {
            val pageNum = it.lastPathSegment?.toString()?.toIntOrNull()
            when(pageNum) {
                1 -> navHostFragment.navController.navigate(blankFragment)
                2 -> navHostFragment.navController.navigate(blankFragment2)
                3 -> navHostFragment.navController.navigate(blankFragment3)
            }
        }
    }
}
