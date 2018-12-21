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
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth


class AuthActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener {

    @BindView(R.id.progress_circular)
    lateinit var progressBar: ProgressBar
    @BindView(R.id.progress_layout)
    lateinit var progressLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuth.getInstance().currentUser?.let {
            startMain()
            return
        }

        setContentView(R.layout.activity_auth)
        ButterKnife.bind(this)
    }

    fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun startProgress() {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = View.VISIBLE
        progressLayout.visibility = View.VISIBLE
    }

    override fun stopProgress() {
        progressBar.visibility = View.INVISIBLE
        progressLayout.visibility = View.INVISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
