package com.example.ketrio.androidapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private val READ_PHONE_STATE_REQUEST_CODE: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isPhonePermissionGranted()) {
            IMEIShowButton.setOnClickListener({ tryShowIMEI() })
        } else {
            showIMEI()
        }
    }

    private fun isPhonePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun tryShowIMEI() {
        if (!isPhonePermissionGranted()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                val message = "Grant permission to show IMEI"
                val builder = AlertDialog.Builder(this)
                builder.setMessage(message)
                    .setPositiveButton("Grant") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_PHONE_STATE),
                            READ_PHONE_STATE_REQUEST_CODE
                        )
                    }
                builder.create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    READ_PHONE_STATE_REQUEST_CODE
                )
            }
        } else {
            showIMEI()
        }
    }

    private fun showIMEI() {
        IMEITextView.text = "IMEI: " + getIMEI()
        IMEIShowButton.visibility = View.GONE
    }

    private fun getIMEI(): String {
        try {
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE)){
                        Toast.makeText(this, "You have to give permission to see the IMEI",
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "You have to give permission manually", Toast.LENGTH_LONG).show()
                    }
                }
                return
            }
            else -> {

            }
        }
    }
}
