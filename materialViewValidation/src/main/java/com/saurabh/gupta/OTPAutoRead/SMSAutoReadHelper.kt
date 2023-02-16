package com.saurabh.gupta.OTPAutoRead

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern


class SMSAutoReadHelper(activity: Activity) {

    private var activity: Activity
    private var smsBroadcastReceiver: SMSBroadcastReceiver
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var listener: AuthCodeListener

    init {
        this.activity = activity
        this.smsBroadcastReceiver = SMSBroadcastReceiver()
        startUserConsent()
    }

    private fun startUserConsent() {
        val client = SmsRetriever.getClient(activity.applicationContext)
        client.startSmsUserConsent(null)
    }

    fun registerBroadcastReceiver(launcher: ActivityResultLauncher<Intent>) {
        this.launcher = launcher
        smsBroadcastReceiver.smsReceiverListener =
            object : SMSBroadcastReceiver.SMSBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    launcher.launch(intent)
                }

                override fun onFailure() {

                }
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity.registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    fun unregisterReceiver() {
        activity.unregisterReceiver(smsBroadcastReceiver)
    }

    fun onActivityResult(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
            val data: Intent? = result.data
            val stringExtra = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            getMessage(stringExtra)
        }
    }

    private fun getMessage(message: String?) {
        val compile = Pattern.compile("(|^)\\d{6}")
        val matcher = compile.matcher(message)
        if (matcher.find()) {
            val authCode = matcher.group(0)
            listener.onRead(authCode)
        }
    }

    fun setAuthCodeListener(listener: AuthCodeListener){
        this.listener = listener
    }

    interface AuthCodeListener {
        fun onRead(authCode: String)
    }
}