package com.example.ketrio.androidapp

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.DialogInterface
import java.util.*
import android.content.Intent
import android.provider.MediaStore
import com.example.ketrio.androidapp.data.AppDatabase
import com.example.ketrio.androidapp.data.entity.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.image
import org.jetbrains.anko.uiThread
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ketrio.androidapp.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.imageBitmap
import java.io.File
import androidx.core.content.ContextCompat.getSystemService




class EditProfileFragment : androidx.fragment.app.Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private var user: User? = null

    var isDirty = false

    private val REQUEST_IMAGE_PHOTO = 1
    private val REQUEST_IMAGE_GALLERY = 2

    private val textWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            isDirty = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        view.findViewById<View>(R.id.imageview_profile_image).setOnClickListener {
            showPictureDialog()
        }

        doAsync {
            user = AppDatabase.getInstance(context!!).userDao().get()

            uiThread {
                text_input_full_name.setText(user?.fullName)
                text_input_email.setText(user?.email)
                text_input_phone.setText(user?.phoneNumber)
                imageview_profile_image.setImageBitmap(bytesToBitmap(user?.profileImage!!))

                text_input_full_name.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        isDirty = true
                        if (!isValidFullname(s.toString())) {
                            text_input_full_name_layout.setError("Fullname should consist at least of 2 words")
                        } else {
                            text_input_full_name_layout.setError(null)
                        }
                        checkErrors()
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
                })
                text_input_phone.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        isDirty = true
                        if (!isValidMobile(s.toString())) {
                            text_input_phone_layout.setError("Invalid phone number")
                        } else {
                            text_input_phone_layout.setError(null)
                        }
                        checkErrors()
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
                })
                text_input_email.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        isDirty = true
                        if (!isValidEmail(s.toString())) {
                            text_input_email_layout.setError("Invalid email")
                        } else {
                            text_input_email_layout.setError(null)
                        }
                        checkErrors()
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
                })
            }
        }

        view.findViewById<View>(R.id.floating_action_button).setOnClickListener {
            user?.run {
                this.fullName = text_input_full_name.text.toString()
                this.phoneNumber = text_input_phone.text.toString()
                this.email = text_input_email.text.toString()

                AppDatabase.getInstance(context!!).userDao().update(this)

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)

                activity?.findViewById<View>(R.id.nav_host_fragment)?.findNavController()?.navigate(R.id.profile_fragment)
            }
        }

        return view
    }

    private fun checkErrors() {
        val hasErrors = arrayOf(text_input_email_layout, text_input_full_name_layout, text_input_phone_layout).map {
                it -> it.error
        }.any {
                it -> it != null
        }


        val fab = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
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
        var compressedImage = bytesToBitmap(user?.profileImage!!)
        if (requestCode == REQUEST_IMAGE_PHOTO) {
            val extras = data!!.extras
            val image = extras?.get("data") as Bitmap
            compressedImage = resize(image, 500, 500)
            imageview_profile_image.setImageBitmap(compressedImage)
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            val imageUri = data!!.data
            val image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
            compressedImage = resize(image, 500, 500)
            imageview_profile_image.setImageBitmap(compressedImage)
        }
        user?.run {
            this.profileImage = bitmapToBytes(compressedImage)
            AppDatabase.getInstance(context!!).userDao().update(this)
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
