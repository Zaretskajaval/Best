package com.example.top_top
import android.telephony.SmsManager

class SmsSender {
    fun sendSms(message: String) {
        val phoneNumber = "+79213794299" // Номер, на который отправляем SMS

        // Получаем экземпляр SmsManager
        val smsManager = SmsManager.getDefault()

        // Отправляем SMS
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}