package com.example.top_top

import SmsReceiver
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.top_top.MainActivity.Companion.SMS_PERMISSION_CODE

class ButtonsFunctions {
    var smsReceiver: SmsReceiver? = null
    var smsFromTracker: String = ""
    var smsReceiverForCharge: SmsReceiver? = null

    fun obtainSmsFromTracker(): String {
        return smsFromTracker
    }
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MainActivity.SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initSmsReceiver(activity)
                }
            }
            else -> {
                // Обработка других запросов разрешений, если необходимо
            }
        }
    }
    fun initSmsReceiver(context: Context) {
        smsReceiver = SmsReceiver()
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        context.registerReceiver(smsReceiver, filter)
    }
    fun getGeoDataFunction(context: Context, smsFromTracker: String) {
        // Проверяем, есть ли у приложения разрешение на отправку SMS
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            // Если разрешение не предоставлено, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.SEND_SMS),
                SMS_PERMISSION_CODE
            )
        } else {
            // Если разрешение уже предоставлено, можно выполнять операции отправки SMS
            val smsSender = SmsSender()
            smsSender.sendSms("999") // Пример отправки SMS
        }
    }



    //  fun initBatteryChargeReceiver(context: Context) {
  //     smsReceiverForCharge = SmsReceiver()
   //     smsReceiverForCharge?.setOnSmsReceivedListener(object : SmsReceiver.OnSmsReceivedListener {
    //        override fun onSmsReceived(message: String) {
   //             smsFromTracker = message
    //            BatteryCharge(context)
    //        }
    //    })
    //    context.registerReceiver(smsReceiverForCharge, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
   // }

    fun BatteryCharge(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.SEND_SMS),
                MainActivity.SMS_PERMISSION_CODE
            )
        } else {
            val smsSender = SmsSender()
            smsSender.sendSms("999")
        }
    }

    private fun showAlert(context: Context, batteryCharge: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Battery Charge")
        builder.setMessage("Battery charge: $batteryCharge")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()

        // Закрываем SmsReceiver после отображения AlertDialog
        context.unregisterReceiver(smsReceiverForCharge)
    }

}
