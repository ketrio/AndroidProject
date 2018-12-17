package com.example.ketrio.androidapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.example.ketrio.androidapp.R.id.*
import com.example.ketrio.androidapp.data.AppDatabase
import com.example.ketrio.androidapp.data.dao.UserDao
import com.example.ketrio.androidapp.data.entity.User
import com.example.ketrio.androidapp.utils.bitmapToBytes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity(),
    BlankFragment.OnFragmentInteractionListener, phoneInfoFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener, BlankFragment2.OnFragmentInteractionListener,
    BlankFragment3.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        doAsync {
            val db = AppDatabase.getInstance(this@MainActivity)

            if (db.userDao().get() == null) {
                val userImage: ByteArray = bitmapToBytes(BitmapFactory.decodeResource(this@MainActivity.resources, R.drawable.profile_image))
                val user = User(
                    0,
                    "Vlad Gutkovsky",
                    "911",
                    "email@mail.com",
                    userImage)
                db.userDao().insert(user)
            }
        }
    }

}
