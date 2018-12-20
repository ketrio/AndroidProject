package com.example.ketrio.androidapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import android.widget.EditText
import android.widget.ProgressBar
import com.example.ketrio.androidapp.utils.isValidEmail
import com.example.ketrio.androidapp.utils.isValidFullname
import com.example.ketrio.androidapp.utils.isValidPassword
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class SignupFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    @BindView(R.id.indeterminateBar)
    lateinit var _progressBar: ProgressBar

    @BindView(R.id.input_name_layout)
    lateinit var _nameLayout: TextInputLayout

    @BindView(R.id.input_name)
    lateinit var _nameText: EditText

    @BindView(R.id.input_email_layout)
    lateinit var _emailLayout: TextInputLayout

    @BindView(R.id.input_email)
    lateinit var _emailText: EditText

    @BindView(R.id.input_password_layout)
    lateinit var _passwordLayout: TextInputLayout

    @BindView(R.id.input_password)
    lateinit var _passwordText: EditText
    
    @BindView(R.id.link_login)
    lateinit var _loginLink: TextView

    @BindView(R.id.btn_signup)
    lateinit var _signupButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _loginLink.setOnClickListener {
            activity?.findViewById<View>(R.id.nav_host_fragment)?.findNavController()?.navigate(R.id.loginFragment)
        }

        setupWatchValidation()

        _signupButton.setOnClickListener {
            if (validateForm()) {
                signup(view)
            }
        }
    }

    private fun signup(view: View) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            _emailText.text.toString(),
            _passwordText.text.toString()
        ).addOnSuccessListener {
            (activity as AuthActivity).startMain()
        }.addOnFailureListener {
            Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_LONG).show()
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
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (!isValidFullname(_nameText.text.toString())) {
            _nameLayout.error = "Name should consist of at least 2 words"
            isValid = false
        } else {
            _nameLayout.error = null
        }

        if (!isValidEmail(_emailText.text.toString())) {
            _emailLayout.error = "Incorrect email"
            isValid = false
        } else {
            _nameLayout.error = null
        }

        if (_passwordLayout.error !== null) {
            isValid = false
        }

        return isValid
    }

    private fun setupWatchValidation() {
        _passwordText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isValidPassword(s.toString())) {
                    _passwordLayout.error = "Minimum eight characters, at least one letter and one number"
                } else {
                    _passwordLayout.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        _nameText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                _nameLayout.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        _emailText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                _emailLayout.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }
}
