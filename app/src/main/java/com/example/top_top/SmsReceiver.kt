package com.example.top_top

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


class SmsReceiver : BroadcastReceiver() {
    companion object {
        const val TRACKER_NUMBER = "+79213794299"
        const val TRACKER_NUMBER_MOPS = "+79231019957"

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("com.example.top_top.SmsReceiver", "SMS received")

        val bundle = intent?.extras
        if (bundle != null) {
            val pdus = bundle["pdus"] as Array<Any>?
            if (pdus != null) {
                for (pdu in pdus) {
                    val message = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = message.originatingAddress
                    val messageBody = message.messageBody


                    if (sender == TRACKER_NUMBER||sender==TRACKER_NUMBER_MOPS) {
                        (context as? Activity)?.finish()
                        val intent = Intent(context, MapActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context?.startActivity(intent)
                        val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val substring = messageBody.substring(4, 7)
                        sharedPreferences?.edit()?.putString("chargePercent", substring)?.apply()

                    }
                }
            }
        }
    }


    fun unregister(context: Context?) {
        context?.unregisterReceiver(this)
    }
}
