package com.example.ketrio.androidapp

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
import android.R.id.button2
import android.R.id.button1


class LoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    @BindView(R.id.link_signup)
    lateinit var _signupLink: TextView

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
}
