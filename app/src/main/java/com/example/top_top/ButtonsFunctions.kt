package com.example.top_top

import SmsReceiver
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.top_top.MainActivity.Companion.SMS_PERMISSION_CODE

class ButtonsFunctions {
    private var smsReceiver: SmsReceiver? = null
    private var smsFromTracker: String = ""
    private var smsReceiverForCharge: SmsReceiver? = null


    fun initSmsReceiver(context: Context) {
        smsReceiver = SmsReceiver()
        smsReceiver?.setOnSmsReceivedListener(object : SmsReceiver.OnSmsReceivedListener {
            override fun onSmsReceived(message: String) {
                smsFromTracker = message
                openMapActivity(context)
            }
        })
        context.registerReceiver(smsReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    fun onDestroy(context: Context) {
        context.unregisterReceiver(smsReceiver)
    }

    fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MainActivity.SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено, инициализируем BroadcastReceiver
                initSmsReceiver(context)
                initBatteryChargeReceiver(context)
            } else {
                // Разрешение не предоставлено, сообщаем об этом пользователю
                // textView.text = "Без разрешения на получение SMS невозможно прочитать сообщения"
            }
        }
    }

    fun getGeoDataFunction(context: Context) {
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

    fun openMapActivity(context: Context) {
        // Создаем Intent для открытия MapActivity
        val intent = Intent(context, MapActivity::class.java)
        val patOnMap = smsFromTracker.substring(8)
        intent.putExtra("PAT_ON_MAP", patOnMap)
        context.startActivity(intent)
    }

    fun initBatteryChargeReceiver(context: Context) {
        smsReceiverForCharge = SmsReceiver()
        smsReceiverForCharge?.setOnSmsReceivedListener(object : SmsReceiver.OnSmsReceivedListener {
            override fun onSmsReceived(message: String) {
                smsFromTracker = message
                BatteryCharge(context)
            }
        })
        context.registerReceiver(smsReceiverForCharge, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

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
