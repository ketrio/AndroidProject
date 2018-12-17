package com.example.ketrio.androidapp

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ketrio.androidapp.data.AppDatabase
import com.example.ketrio.androidapp.data.entity.User
import com.example.ketrio.androidapp.utils.bytesToBitmap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.image
import org.jetbrains.anko.uiThread


class ProfileFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val button = view.findViewById<View>(R.id.floating_action_button)
        button.setOnClickListener {
            activity?.findViewById<View>(R.id.nav_host_fragment)?.findNavController()?.navigate(R.id.edit_profile_fragment)
        }

        doAsync {
            val user: User? = AppDatabase.getInstance(context!!).userDao().get()

            uiThread {
                textview_full_name.text = user?.fullName
                textview_phone_number.text = user?.phoneNumber
                textview_email.text = user?.email
                imageview_profile_image.setImageBitmap(bytesToBitmap(user?.profileImage!!))
            }
        }

        return view
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
