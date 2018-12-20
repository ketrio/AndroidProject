package com.example.ketrio.androidapp

import android.R.id.*
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import androidx.navigation.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    @BindView(R.id.btn_login)
    lateinit var loginButton: Button

    @BindView(R.id.link_signup)
    lateinit var _signupLink: TextView

    @BindView(R.id.input_email)
    lateinit var emailInput: EditText

    @BindView(R.id.input_password)
    lateinit var passwordInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _signupLink.setOnClickListener {
            activity?.findViewById<View>(R.id.nav_host_fragment)?.findNavController()?.navigate(R.id.signupFragment)
        }

        loginButton.setOnClickListener {
            if (emailInput.text.isNotEmpty() && passwordInput.text.isNotEmpty()) {
                listener?.startProgress()
                login(view)
            }
        }
    }

    private fun login(view: View) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            emailInput.text.toString(),
            passwordInput.text.toString()
        ).addOnSuccessListener {
            (activity as AuthActivity).startMain()
        }.addOnFailureListener {
            Snackbar.make(view, it.localizedMessage, Snackbar.LENGTH_LONG).show()
        }.addOnCompleteListener {
            listener?.stopProgress()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun startProgress()
        fun stopProgress()
    }
}
