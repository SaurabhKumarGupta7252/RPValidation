package com.android.app.PinViewFilledListener

import android.text.Editable
import android.text.TextWatcher
import com.chaos.view.PinView

object PinViewOnPinFilled {

    private lateinit var onPin: OnPinFill

    fun PinView.onPinFilledListener(onPinFill: OnPinFill){
        onPin = onPinFill
        val pinView = this
        pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pinView.text.toString().length == pinView.itemCount) {
                    onPinFill.onPinFilled(pinView.text.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    interface OnPinFill {
        fun onPinFilled(text: String?)
    }
}