package com.saurabh.validationprocess

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saurabh.gupta.material.view.validation.TYPE
import com.saurabh.gupta.material.view.validation.Validation.checkErrorNull
import com.saurabh.gupta.material.view.validation.Validation.onItemClickAutoCompleteTextView
import com.saurabh.gupta.material.view.validation.Validation.validateAutoCompleteTextView
import com.saurabh.gupta.material.view.validation.Validation.validateInputEditText
import com.saurabh.validationprocess.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fillCountryList()

        binding.apply {

            tilFirstName.validateInputEditText("First name is required", TYPE.NON_EMPTY_STRING)
            tilLastName.validateInputEditText("Last name is required", TYPE.NON_EMPTY_STRING)
            tilEmail.validateInputEditText("Valid email is required", TYPE.EMAIL)
            tilMobileNumber.validateInputEditText("Valid mobile is required", TYPE.MOBILE_NUMBER)
            tilCountry.validateAutoCompleteTextView("Please select country", TYPE.NON_EMPTY_STRING)
            /** For Dropdown/Spinner add onItemClickAutoCompleteTextView in itemClickListener section
             * tilCountry.onItemClickAutoCompleteTextView("Please select country", TYPE.NON_EMPTY_STRING)
             */

            btnSave.setOnClickListener {
                if (checkErrorNull(
                        tilEmail,
                        tilFirstName,
                        tilLastName,
                        tilMobileNumber,
                        tilCountry
                    )
                ) {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
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