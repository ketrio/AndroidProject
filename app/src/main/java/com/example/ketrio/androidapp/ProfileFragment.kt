package com.example.ketrio.androidapp

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.ketrio.androidapp.data.entity.User
import com.example.ketrio.androidapp.utils.bytesToBitmap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.doAsync
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
            val db = FirebaseDatabase.getInstance()
            val userRef = db.getReference("users").child("0")

            userRef.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    activity?.findViewById<TextView>(R.id.textview_full_name)?.setText(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)

                    activity?.findViewById<TextView>(R.id.textview_full_name)?.setText(user?.fullName)
                    activity?.findViewById<TextView>(R.id.textview_email)?.setText(user?.email)
                    activity?.findViewById<TextView>(R.id.textview_phone_number)?.setText(user?.phoneNumber)

                    user?.customPhoto?.run {
                        if (this) {
                            loadPhoto()
                        } else {
                            activity?.findViewById<ImageView>(R.id.imageview_profile_image)?.setImageBitmap(
                                BitmapFactory.decodeResource(activity?.resources, R.drawable.profile_image)
                            )
                        }
                    }
                }
            })
        }

        return view
    }

    private fun loadPhoto() {
        val storageRef = FirebaseStorage.getInstance().reference
        val photoRef = storageRef.child("photos").child("0.webp")

        photoRef.getBytes(3000000).addOnSuccessListener {
            activity?.findViewById<ImageView>(R.id.imageview_profile_image)?.setImageBitmap(
                bytesToBitmap(it)
            )
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
