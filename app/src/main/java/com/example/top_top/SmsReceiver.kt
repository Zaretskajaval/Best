import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsMessage
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.top_top.ButtonsFunctions
import com.example.top_top.MainActivity
import com.example.top_top.MapActivity
import com.example.top_top.R


class SmsReceiver : BroadcastReceiver() {
    companion object {
        const val TRACKER_NUMBER = "+79213794299"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        if (bundle != null) {
            val pdus = bundle["pdus"] as Array<Any>?
            if (pdus != null) {
                for (pdu in pdus) {
                    val message = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = message.originatingAddress
                    val messageBody = message.messageBody

                    if (sender == TRACKER_NUMBER) {
                        // При получении SMS от указанного номера, открываем MapActivity
                        Toast.makeText(context, "Received SMS from tracker", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MapActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context?.startActivity(intent)
                    }
                }
            }
        }
    }


    fun unregister(context: Context?) {
        context?.unregisterReceiver(this)
    }
}
