package com.android.app.material.view.validation

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.android.app.material.view.validation.ValidateInput.checkDataWithValidType
import com.android.app.material.view.validation.ValidateInput.checkEmail
import com.android.app.material.view.validation.ValidateInput.checkEmailOrMobileNumber
import com.android.app.material.view.validation.ValidateInput.checkMobileNumber
import com.android.app.material.view.validation.ValidateInput.checkOnlyNumber
import com.android.app.material.view.validation.ValidateInput.checkRegex
import com.android.app.material.view.validation.ValidateInput.checkString

object Validation {

    private var state: Boolean = false

    fun initiateValidation() {
        state = false
    }

    fun resetValidation() {
        state = false
    }

    fun TextInputLayout.validateInputEditText(
        errorMessage: String,
        type: TYPE? = null,
        isValidOnFocusChange: Boolean? = false,
        regex: String? = ""
    ) {
        initForFocusChange(
            textInputLayout = this,
            errorMessage_ = errorMessage, type_ = type,
            isValidOnFocusChange = isValidOnFocusChange,
            regex = regex
        )
    }

    /* * fun validateInputEditText(
         textInputLayout: TextInputLayout,
         errorMessage: String,
         type: TYPE,
         isValidOnFocusChange: Boolean? = true
     ) {
         initForFocusChange(
             textInputLayout = textInputLayout,
             errorMessage = errorMessage, type = type,
             isValidOnFocusChange = isValidOnFocusChange
         )
     }*/

    fun TextInputLayout.validateAutoCompleteTextView(
        errorMessage: String,
        type: TYPE? = null,
        isValidOnFocusChange: Boolean? = false,
        regex: String? = ""
    ) {
        initForFocusChange(
            textInputLayout = this,
            errorMessage_ = errorMessage, type_ = type,
            isValidOnFocusChange = isValidOnFocusChange,
            isValidOnKeyChange = false, regex = regex
        )
    }

    fun TextInputLayout.onItemClickAutoCompleteTextView(
        errorMessage: String = "",
        type: TYPE? = null,
        isValidOnFocusChange: Boolean? = false,
        isValidOnKeyChange: Boolean? = false,
        regex: String? = ""
    ) {
        initForFocusChange(
            textInputLayout = this,
            errorMessage_ = errorMessage, type_ = type,
            isValidOnFocusChange = isValidOnFocusChange,
            isValidOnKeyChange = isValidOnKeyChange,
            regex = regex
        )
    }

    /* * fun validateAutoCompleteTextView(
         textInputLayout: TextInputLayout,
         errorMessage: String,
         type: TYPE,
         isValidOnFocusChange: Boolean? = true,
         isValidOnKeyChange: Boolean? = true
     ) {
         initForFocusChange(
             textInputLayout = textInputLayout,
             errorMessage = errorMessage, type = type,
             isValidOnFocusChange = isValidOnFocusChange,
             isValidOnKeyChange = isValidOnKeyChange
         )
     }*/

    private fun initForFocusChange(
        textInputLayout: TextInputLayout,
        errorMessage_: String,
        type_: TYPE?,
        isValidOnFocusChange: Boolean? = false,
        isValidOnKeyChange: Boolean? = false,
        regex: String?
    ) {
        val type = type_ ?: TYPE.NON_EMPTY_STRING
        val errorMessage =
            if (errorMessage_.isNullOrEmpty()) textInputLayout.errorMessage() else errorMessage_
        textInputLayout.errorMessage(errorMessage)
        textInputLayout.inputDataType(type)
        textInputLayout.regex(regex)
        if (textInputLayout.editText is AppCompatAutoCompleteTextView) {
            if (state || isValidOnKeyChange == true) setErrorAndRemove(type, textInputLayout, errorMessage)
        } else {
                textInputLayout.editText?.onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            if (state || isValidOnFocusChange == true) {
                                setErrorAndRemove(type, textInputLayout, errorMessage)
                            }
                        }
            }
                textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (state || isValidOnKeyChange == true) {
                            setErrorAndRemove(type, textInputLayout, errorMessage)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })
        }
    }

    private fun TextInputLayout.errorMessage(): String {
        return this.getTag(R.id.errorMessage) as String
    }

    private fun TextInputLayout.errorMessage(errorMessage: String) {
        this.setTag(R.id.errorMessage, errorMessage)
    }

    fun TextInputLayout.inputDataType(): TYPE {
        return TYPE.valueOf(this.getTag(R.id.inputDataType).toString())
    }

    fun TextInputLayout.inputDataType(type: TYPE) {
        this.setTag(R.id.inputDataType, type.name)
    }

    fun TextInputLayout.regex(): String {
        return this.getTag(R.id.regex) as String
    }

    fun TextInputLayout.regex(regex: String?) {
        this.setTag(R.id.regex, regex)
    }

    fun TextInputLayout.errorEnabled(): Boolean {
        return this.getTag(R.id.errorEnabled) == true
    }

    fun TextInputLayout.errorEnabled(errorEnabled: Boolean) {
        this.setTag(R.id.errorEnabled, errorEnabled)
    }

    internal fun setErrorAndRemove(
        type: TYPE,
        textInputLayout: TextInputLayout,
        errorMessage: String
    ) {
        when (type) {
            TYPE.NON_EMPTY_STRING -> {
                textInputLayout.error =
                    if (!checkString(textInputLayout)) errorMessage else null
                textInputLayout.isErrorEnabled = !checkString(textInputLayout)
            }
            TYPE.EMAIL -> {
                textInputLayout.error =
                    if (!checkEmail(textInputLayout)) errorMessage else null
                textInputLayout.isErrorEnabled = !checkEmail(textInputLayout)
            }
            TYPE.NUMBER -> {
                textInputLayout.error =
                    if (!checkOnlyNumber(textInputLayout)) errorMessage else null
                textInputLayout.isErrorEnabled = !checkOnlyNumber(textInputLayout)
            }
            TYPE.MOBILE_NUMBER -> {
                textInputLayout.error =
                    if (!checkMobileNumber(textInputLayout)) errorMessage else null
                textInputLayout.isErrorEnabled = !checkMobileNumber(textInputLayout)
            }
            TYPE.EMAIL_OR_MOBILE -> {
                textInputLayout.error =
                    if (!checkEmailOrMobileNumber(textInputLayout)) errorMessage else null
                textInputLayout.isErrorEnabled = !checkEmailOrMobileNumber(textInputLayout)
            }
            TYPE.REGEX -> {
                textInputLayout.error =
                    if (!checkRegex(textInputLayout)) errorMessage else null
                textInputLayout.isErrorEnabled = !checkRegex(textInputLayout)
            }
        }
    }

    fun validateRegisteredField(vararg textInputLayout: TextInputLayout): Boolean {
        var errorCount = 0
        state = true
        for (textInputLayout in textInputLayout) {
            textInputLayout.error =
                if (!checkDataWithValidType(textInputLayout)) textInputLayout.errorMessage()
                else null
            textInputLayout.isErrorEnabled = textInputLayout.error != null
            if (textInputLayout.error != null) errorCount++
        }
        return errorCount == 0
    }
}