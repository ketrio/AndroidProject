package com.example.ketrio.androidapp

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import android.content.Intent
import android.provider.MediaStore
import com.example.ketrio.androidapp.data.entity.User
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.ketrio.androidapp.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.doAsync


class EditProfileFragment : androidx.fragment.app.Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    var isDirty = false

    private val REQUEST_IMAGE_PHOTO = 1
    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doAsync {
            loadData(view)

        }

        setupCircleImage(view)
        setupInputValidation(view)
        setupSaveButton(view)
    }

    private fun setupSaveButton(view: View) {
        val saveButton = view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        val fullnameInput = view.findViewById<TextInputEditText>(R.id.text_input_full_name)
        val phoneInput = view.findViewById<TextInputEditText>(R.id.text_input_phone)
        val emailInput = view.findViewById<TextInputEditText>(R.id.text_input_email)

        saveButton.setOnClickListener {
            val db = FirebaseDatabase.getInstance()
            val userRef = db.reference.child("users").child("0")

            userRef.updateChildren(mapOf(
                "fullName" to fullnameInput.text.toString(),
                "phoneNumber" to phoneInput.text.toString(),
                "email" to emailInput.text.toString()
            ))

            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            activity?.findViewById<View>(R.id.nav_host_fragment)?.findNavController()?.navigate(R.id.profile_fragment)
        }
    }

    private fun setupCircleImage(view: View) {
        view.findViewById<View>(R.id.imageview_profile_image).setOnClickListener {
            showPictureDialog()
        }
    }

    private fun loadData(view: View) {
        val db = FirebaseDatabase.getInstance()
        val userRef = db.reference.child("users").child("0")

        val fullnameInput = view.findViewById<TextInputEditText>(R.id.text_input_full_name)
        val phoneInput = view.findViewById<TextInputEditText>(R.id.text_input_phone)
        val emailInput = view.findViewById<TextInputEditText>(R.id.text_input_email)

        userRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)

                fullnameInput.setText(user?.fullName)
                emailInput.setText(user?.email)
                phoneInput.setText(user?.phoneNumber)

                if (user?.customPhoto == false) {
                    view.findViewById<ImageView>(R.id.imageview_profile_image).setImageBitmap(
                        BitmapFactory.decodeResource(activity?.resources, R.drawable.profile_image)
                    )
                } else {
                    loadPhoto()
                }
            }
        })
    }

    private fun setupInputValidation(view: View) {
        val fullnameInput = view.findViewById<TextInputEditText>(R.id.text_input_full_name)
        val phoneInput = view.findViewById<TextInputEditText>(R.id.text_input_phone)
        val emailInput = view.findViewById<TextInputEditText>(R.id.text_input_email)

        val fullnameLayout = fullnameInput.parent.parent as TextInputLayout
        val phoneLayout = phoneInput.parent.parent as TextInputLayout
        val emailLayout = emailInput.parent.parent as TextInputLayout

        fullnameInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isDirty = true
                if (!isValidFullname(s.toString())) {
                    fullnameLayout.error = "Fullname should consist at least of 2 words"
                } else {
                    fullnameLayout.error = null
                }
                checkErrors(view)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        phoneInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isDirty = true
                if (!isValidMobile(s.toString())) {
                    phoneLayout.error = "Invalid phone number"
                } else {
                    phoneLayout.error = null
                }
                checkErrors(view)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        emailInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isDirty = true
                if (!isValidEmail(s.toString())) {
                    emailLayout.error = "Invalid email"
                } else {
                    emailLayout.error = null
                }
                checkErrors(view)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun checkErrors(view: View) {
        val fullnameLayout = view.findViewById<TextInputLayout>(R.id.text_input_full_name_layout)
        val phoneLayout = view.findViewById<TextInputLayout>(R.id.text_input_phone_layout)
        val emailLayout = view.findViewById<TextInputLayout>(R.id.text_input_email_layout)

        val hasErrors = arrayOf(fullnameLayout, phoneLayout, emailLayout).map {
                it -> it.error
        }.any {
                it -> it != null
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        if (hasErrors) {
            fab?.hide()
        } else {
            fab?.show()
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

    private fun loadPhoto() {
        val storageRef = FirebaseStorage.getInstance().reference
        val photoRef = storageRef.child("photos").child("0.webp")

        photoRef.getBytes(3000000).addOnSuccessListener {
            activity?.findViewById<ImageView>(R.id.imageview_profile_image)?.setImageBitmap(
                bytesToBitmap(it)
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
    }

    private fun dispatchTakePhotoFromCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_PHOTO)
    }

    private fun dispatchTakePhotoFromGalleryIntent() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        this.startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        var scaledImage: Bitmap? = null
        if (requestCode == REQUEST_IMAGE_PHOTO) {
            val extras = data!!.extras
            val image = extras?.get("data") as Bitmap
            scaledImage = resize(image, 500, 500)
            imageview_profile_image.setImageBitmap(scaledImage)
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            val imageUri = data!!.data
            val image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
            scaledImage = resize(image, 500, 500)
            imageview_profile_image.setImageBitmap(scaledImage)
        }

        val dbRef = FirebaseDatabase.getInstance().reference
        val userRef = dbRef.child("users").child("0")

        val storageRef = FirebaseStorage.getInstance().reference
        val photoRef = storageRef.child("photos").child("0.webp")

        photoRef.putBytes(bitmapToBytes(scaledImage!!)).addOnSuccessListener {
            userRef.child("customPhoto").setValue(true)
        }.addOnFailureListener {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(it.message).create().show()
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(Objects.requireNonNull(activity))
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems)
        { _, which ->
            when (which) {
                0 -> dispatchTakePhotoFromGalleryIntent()
                1 -> dispatchTakePhotoFromCameraIntent()
            }
        }
        pictureDialog.show()
    }
}
