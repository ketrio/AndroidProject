package com.example.ketrio.androidapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [phoneInfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [phoneInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class phoneInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var IMEIShowButton: Button? = null
    private var IMEITextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_phone_info, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!isPhonePermissionGranted()) {
            view!!.findViewById<Button>(R.id.imei_show_button).apply {
                setOnClickListener { tryShowIMEI() }
            }
        } else {
            showIMEI()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment phoneInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            phoneInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val READ_PHONE_STATE_REQUEST_CODE: Int = 1


    private fun isPhonePermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun tryShowIMEI() {
        if (!isPhonePermissionGranted()) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                val message = "Grant permission to show IMEI"
                val builder = AlertDialog.Builder(activity!!)
                builder.setMessage(message)
                    .setPositiveButton("Grant") { dialog, id ->
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_PHONE_STATE),
                            READ_PHONE_STATE_REQUEST_CODE
                        )
                    }
                builder.create().show()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    READ_PHONE_STATE_REQUEST_CODE
                )
            }
        } else {
            showIMEI()
        }
    }

    private fun showIMEI() {
        view!!.findViewById<Button>(R.id.imei_show_button).apply {
            visibility = View.GONE
        }

        view!!.findViewById<TextView>(R.id.imei_text_view).apply {
            text = "IMEI: " + getIMEI()
        }
    }

    private fun getIMEI(): String {
        try {
            val tm = activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMEI = tm.getDeviceId()
            return IMEI
        } catch (e: SecurityException){
            throw e
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            READ_PHONE_STATE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showIMEI()
                } else {
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                        Toast.makeText(context, "You have to give permission to see the IMEI",
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "You have to give permission manually", Toast.LENGTH_LONG).show()
                    }
                }
                return
            }
            else -> {
                showIMEI()
            }
        }
    }
}
