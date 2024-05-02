package com.example.top_top
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.ImageView
import com.bumptech.glide.Glide

class SmsSender {
    fun sendSms(message: String) {
        val phoneNumber = "+79213794299" // Номер, на который отправляем SMS

        // Получаем экземпляр SmsManager
        val smsManager = SmsManager.getDefault()

        // Отправляем SMS
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

    }
}