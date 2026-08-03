package padelpulseapp2.netlify.app.sync

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import padelpulseapp2.netlify.app.MainActivity

/**
 * Recibe los mensajes del reloj tambien con la app en segundo plano o cerrada.
 * No interpreta el marcador: el estado vive en la capa JS, asi que este servicio
 * solo entrega el mensaje intacto a PPSync (o lo guarda si el WebView no esta listo).
 */
class PhoneWearableListenerService : WearableListenerService() {

    override fun onMessageReceived(event: MessageEvent) {
        val path = event.path
        val payload = String(event.data, Charsets.UTF_8)
        Log.d(TAG, "Del reloj: $path")

        WearLink.setConnected(true)

        val delivered = MainActivity.instance?.deliverToWeb(path, payload) ?: false
        if (!delivered) {
            // App cerrada o WebView aun sin cargar: se guarda y se entrega al abrir
            PendingInbox.put(applicationContext, path, payload)
        }
    }

    companion object {
        private const val TAG = "PadelPulseSvc"
    }
}
