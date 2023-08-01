package com.android.validationprocess

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.app.OTPAutoRead.SMSAutoReadHelper
import com.android.app.material.view.validation.TYPE
import com.android.app.material.view.validation.Validation.onItemClickAutoCompleteTextView
import com.android.app.material.view.validation.Validation.validateAutoCompleteTextView
import com.android.app.material.view.validation.Validation.validateInputEditText
import com.anndroid.validationprocess.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            smsAutoReadHelper.onActivityResult(result)
        }
    private lateinit var smsAutoReadHelper: SMSAutoReadHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fillCountryList()

        binding.apply {

            val regex = "^[\\p{L} .'-]+$"
            tilFirstName.validateInputEditText("First name is required", TYPE.REGEX, regex = regex)
            tilLastName.validateInputEditText("Last name is required", TYPE.NON_EMPTY_STRING)
            tilEmail.validateInputEditText("Valid email is required", TYPE.EMAIL)
            tilMobileNumber.validateInputEditText("Valid mobile is required", TYPE.MOBILE_NUMBER)
            tilEmailOrMobileNumber.validateInputEditText(
                "Valid email or mobile is required",
                TYPE.EMAIL_OR_MOBILE
            )
            tilCountry.validateAutoCompleteTextView("Please select country", TYPE.NON_EMPTY_STRING)
            /** For Dropdown/Spinner add onItemClickAutoCompleteTextView in itemClickListener section
             * tilCountry.onItemClickAutoCompleteTextView("Please select country", TYPE.NON_EMPTY_STRING)
             */

            btnSave.setOnClickListener {
                /*if (validateRegisteredField(
                        tilEmail,
                        tilFirstName,
                        tilLastName,
                        tilMobileNumber,
                        tilEmailOrMobileNumber,
                        tilCountry
                    )
                ) {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                }*/
                startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            }
        }

        /*binding.mobilePin.onPinFilledListener(object : PinViewOnPinFilled.OnPinFill {
            override fun onPinFilled(text: String?) {
                //binding.btnNext.performClick()
            }
        })*/
    }

    override fun onStart() {
        super.onStart()
        smsAutoReadHelper = SMSAutoReadHelper(this@MainActivity)
        smsAutoReadHelper.registerBroadcastReceiver(launcher)
        smsAutoReadHelper.setAuthCodeListener(object : SMSAutoReadHelper.AuthCodeListener {
            override fun onRead(authCode: String) {
                //binding.mobilePin.setText(authCode)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        smsAutoReadHelper.unregisterReceiver()
    }


    private fun fillCountryList() {
        binding.country.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListOf("IN +91", "US +1")
            )
        )
        binding.country.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                binding.tilCountry.onItemClickAutoCompleteTextView()
                Toast.makeText(
                    this@MainActivity,
                    "${parent.getItemAtPosition(position)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}