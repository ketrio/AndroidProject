package com.example.ketrio.androidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import android.R.color.transparent
import android.app.Dialog
import android.view.Window.FEATURE_NO_TITLE
import android.view.LayoutInflater
import android.view.Window


class AuthActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        ButterKnife.bind(this)
    }

    fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
