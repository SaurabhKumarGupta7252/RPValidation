package com.saurabh.gupta.material.view.validation

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.saurabh.gupta.material.view.validation.ValidateInput.checkDataWithValidType
import com.saurabh.gupta.material.view.validation.ValidateInput.checkEmail
import com.saurabh.gupta.material.view.validation.ValidateInput.checkMobileNumber
import com.saurabh.gupta.material.view.validation.ValidateInput.checkOnlyNumber
import com.saurabh.gupta.material.view.validation.ValidateInput.checkString

object Validation {

    fun TextInputLayout.validateInputEditText(
        errorMessage: String,
        type: TYPE? = null,
        isValidOnFocusChange: Boolean? = true
    ) {
        initForFocusChange(
            textInputLayout = this,
            errorMessage_ = errorMessage, type_ = type,
            isValidOnFocusChange = isValidOnFocusChange
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
        isValidOnFocusChange: Boolean? = true
    ) {
        initForFocusChange(
            textInputLayout = this,
            errorMessage_ = errorMessage, type_ = type,
            isValidOnFocusChange = isValidOnFocusChange,
            isValidOnKeyChange = false
        )
    }

    fun TextInputLayout.onItemClickAutoCompleteTextView(
        errorMessage: String = "",
        type: TYPE? = null,
        isValidOnFocusChange: Boolean? = true,
        isValidOnKeyChange: Boolean? = true
    ) {
        initForFocusChange(
            textInputLayout = this,
            errorMessage_ = errorMessage, type_ = type,
            isValidOnFocusChange = isValidOnFocusChange,
            isValidOnKeyChange = isValidOnKeyChange
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
        isValidOnFocusChange: Boolean? = true,
        isValidOnKeyChange: Boolean? = true
    ) {
        val type = type_ ?: TYPE.NON_EMPTY_STRING
        val errorMessage =
            if (errorMessage_.isNullOrEmpty()) textInputLayout.errorMessage() else errorMessage_
        textInputLayout.errorMessage(errorMessage)
        textInputLayout.inputDataType(type)
        if (textInputLayout.editText is AppCompatAutoCompleteTextView) {
            if (isValidOnKeyChange == true) setErrorAndRemove(type, textInputLayout, errorMessage)
        } else {
            if (isValidOnFocusChange == true) {
                textInputLayout.editText?.onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            setErrorAndRemove(type, textInputLayout, errorMessage)
                        }
                    }
            }
            if (isValidOnKeyChange == true) {
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
                        setErrorAndRemove(type, textInputLayout, errorMessage)
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })
            }
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
        }
    }

    fun checkErrorNull(vararg textInputLayout: TextInputLayout): Boolean {
        var errorCount = 0
        for (textInputLayout in textInputLayout) {
            textInputLayout.error =
                if (!checkDataWithValidType(textInputLayout)) textInputLayout.errorMessage()
                else null
            if (textInputLayout.error != null) errorCount++
        }
        return errorCount == 0
    }
}