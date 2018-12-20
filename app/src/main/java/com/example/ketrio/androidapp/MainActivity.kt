package com.example.ketrio.androidapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ketrio.androidapp.R.id.*
import com.example.ketrio.androidapp.data.entity.User
import com.example.ketrio.androidapp.utils.bitmapToBytes
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity(),
    BlankFragment.OnFragmentInteractionListener, phoneInfoFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener, BlankFragment2.OnFragmentInteractionListener,
    BlankFragment3.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(nav_host_fragment) as NavHostFragment
        val currentDestination = navHostFragment.navController.currentDestination
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        if (currentDestination?.id == R.id.edit_profile_fragment) {
            if ((currentFragment as EditProfileFragment).isDirty) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("You have unsaved changes")
                    .setPositiveButton("Keep") { _, _ -> }
                    .setNegativeButton("Discard") { _, _ -> super.onBackPressed()}
                builder.create().show()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(nav_host_fragment) as NavHostFragment
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.let { bottomNavigationView ->
            NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
        }

        intent?.data?.let {
            val pageNum = it.lastPathSegment?.toString()?.toIntOrNull()
            when(pageNum) {
                1 -> navHostFragment.navController.navigate(blank_fragment)
                2 -> navHostFragment.navController.navigate(blank_fragment2)
                3 -> navHostFragment.navController.navigate(blank_fragment3)
                else -> {

                }
            }
        }
    }
}
