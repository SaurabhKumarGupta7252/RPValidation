package com.android.app.material.view.validation

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.google.android.material.textfield.TextInputLayout
import com.android.app.material.view.validation.Validation.inputDataType
import com.android.app.material.view.validation.Validation.regex

object ValidateInput {

    fun checkString(textInputLayout: TextInputLayout): Boolean {
        return !(textInputLayout.editText?.text?.trim().isNullOrEmpty() ||
                textInputLayout.editText?.text?.trim().isNullOrBlank())
    }

    fun checkEmail(textInputLayout: TextInputLayout): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(textInputLayout.editText?.text!!).matches()
    }

    fun checkOnlyNumber(textInputLayout: TextInputLayout): Boolean {
        return textInputLayout.editText?.text?.trim().toString().isDigitsOnly()
    }

    fun checkMobileNumber(textInputLayout: TextInputLayout): Boolean {
        return checkString(textInputLayout) &&
                textInputLayout.editText?.text?.trim().toString().length == 10 &&
                textInputLayout.editText?.text?.trim().toString().isDigitsOnly()
    }

    fun checkEmailOrMobileNumber(textInputLayout: TextInputLayout): Boolean {
        return checkEmail(textInputLayout) || checkMobileNumber(textInputLayout)
    }

    fun checkRegex(textInputLayout: TextInputLayout): Boolean {
        return Regex(textInputLayout.regex()).matches(textInputLayout.editText?.text!!.toString())
    }

    fun checkDataWithValidType(textInputLayout: TextInputLayout): Boolean {
        return when (textInputLayout.inputDataType()) {
            TYPE.NON_EMPTY_STRING -> checkString(textInputLayout)
            TYPE.EMAIL -> checkEmail(textInputLayout)
            TYPE.NUMBER -> checkOnlyNumber(textInputLayout)
            TYPE.MOBILE_NUMBER -> checkMobileNumber(textInputLayout)
            TYPE.EMAIL_OR_MOBILE -> checkEmailOrMobileNumber(textInputLayout)
            TYPE.REGEX -> checkRegex(textInputLayout)
        }
    }
}