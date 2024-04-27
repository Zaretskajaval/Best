import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.top_top.R


class SmsReceiver : BroadcastReceiver() {
    private var onSmsReceivedListener: OnSmsReceivedListener? = null
    private var gifImageView: ImageView? = null

    fun setOnSmsReceivedListener(listener: OnSmsReceivedListener) {
        onSmsReceivedListener = listener
    }


    fun setGifImageView(imageView: ImageView) {
        gifImageView = imageView
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.extras?.let {
            val pdus = it.get("pdus") as Array<Any>?
            pdus?.let { pdus ->
                for (pdu in pdus) {
                    val smsMessage = android.telephony.SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = smsMessage.originatingAddress
                    val message = smsMessage.messageBody

                    // Здесь указываем номер отправителя, от которого мы хотим обрабатывать SMS
                    val desiredSender = "+79213794299"

                    if (sender == desiredSender) {
                        onSmsReceivedListener?.onSmsReceived(message)
                        gifImageView?.let { context?.let { ctx -> showGifAnimation(ctx, it) } }

                    }
                }
            }
        }
    }
    private fun showGifAnimation(context: Context, imageView: ImageView) {
        // Ваш код для загрузки гиф-анимации в ImageView
        // Например, используя Glide:
        Glide.with(context)
            .asGif()
            .load(R.drawable.loadgif)
            .into(imageView)
    }

    private fun closeGifAnimation() {
        gifImageView?.setImageDrawable(null)
    }
    fun register(context: Context) {
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        context.registerReceiver(this, filter)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }

    interface OnSmsReceivedListener {
        fun onSmsReceived(message: String)
    }
}
