package padelpulseapp2.netlify.app.sync

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import java.util.concurrent.atomic.AtomicLong

/**
 * Enlace del reloj con el movil: descubrimiento de nodo, estado de conexion,
 * numeracion anti-eco y envio con reintento. Un solo sitio por el que sale todo.
 */
object PhoneLink {

    private const val TAG = "PadelPulseLink"
    const val CAPABILITY_PHONE = "padelpulse_phone"
    const val CAPABILITY_WATCH = "padelpulse_watch"

    /** Hay un movil alcanzable ahora mismo. */
    var connected by mutableStateOf(false)
        private set

    /** El movil ha aceptado el codigo: la sincronizacion esta activa. */
    var paired by mutableStateOf(false)

    var pairedCode by mutableStateOf("")
    var phoneName by mutableStateOf("")

    /** Ultimo error legible, para poder enseñarlo en pantalla en vez de fallar en silencio. */
    var lastError by mutableStateOf<String?>(null)

    private val seq = AtomicLong(System.currentTimeMillis() / 1000)
    private var lastSeenPhoneSeq = -1L

    /** True mientras aplicamos un estado remoto: impide reemitirlo y crear un bucle. */
    @Volatile
    var applyingRemote = false

    fun nextSeq(): Long = seq.incrementAndGet()

    /**
     * Descarta mensajes repetidos o desordenados del movil.
     * Devuelve true si hay que procesarlo.
     */
    fun acceptSeq(s: Long): Boolean {
        if (s <= 0) return true // emisor antiguo sin seq
        if (s <= lastSeenPhoneSeq) return false
        lastSeenPhoneSeq = s
        return true
    }

    fun resetSeqWindow() {
        lastSeenPhoneSeq = -1L
    }

    fun setConnected(value: Boolean, name: String = "") {
        connected = value
        if (name.isNotEmpty()) phoneName = name
        if (!value) resetSeqWindow()
    }

    /** Publica la capacidad del reloj para que el movil pueda descubrirlo. */
    fun announce(context: Context) {
        Thread {
            runCatching {
                Tasks.await(Wearable.getCapabilityClient(context).addLocalCapability(CAPABILITY_WATCH))
            }.onFailure { Log.w(TAG, "No se pudo publicar la capacidad", it) }
            refreshConnection(context)
        }.start()
    }

    fun refreshConnection(context: Context) {
        Thread {
            runCatching {
                val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes)
                val near = nodes.firstOrNull { it.isNearby } ?: nodes.firstOrNull()
                setConnected(near != null, near?.displayName ?: "")
            }.onFailure {
                setConnected(false)
                Log.w(TAG, "No hay nodos", it)
            }
        }.start()
    }

    /**
     * Envia a todos los nodos conectados. Un solo reintento: si el movil esta
     * dormido el primer envio falla y el segundo, 400 ms despues, suele entrar.
     */
    fun send(context: Context, path: String, payload: String) {
        Thread {
            var ok = false
            repeat(2) { attempt ->
                if (ok) return@repeat
                runCatching {
                    val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes)
                    if (nodes.isEmpty()) {
                        setConnected(false)
                        return@runCatching
                    }
                    val bytes = payload.toByteArray(Charsets.UTF_8)
                    nodes.forEach { node ->
                        Tasks.await(Wearable.getMessageClient(context).sendMessage(node.id, path, bytes))
                    }
                    setConnected(true, nodes.first().displayName)
                    lastError = null
                    ok = true
                }.onFailure { e ->
                    if (attempt == 1) {
                        lastError = e.message
                        setConnected(false)
                        Log.w(TAG, "Fallo enviando $path", e)
                    } else {
                        Thread.sleep(400)
                    }
                }
            }
        }.start()
    }

    fun addListeners(
        context: Context,
        messageListener: MessageClient.OnMessageReceivedListener,
        capabilityListener: CapabilityClient.OnCapabilityChangedListener
    ) {
        runCatching {
            Wearable.getMessageClient(context).addListener(messageListener)
            Wearable.getCapabilityClient(context)
                .addListener(capabilityListener, CAPABILITY_PHONE)
        }.onFailure { Log.w(TAG, "No se pudieron registrar listeners", it) }
    }

    fun removeListeners(
        context: Context,
        messageListener: MessageClient.OnMessageReceivedListener,
        capabilityListener: CapabilityClient.OnCapabilityChangedListener
    ) {
        runCatching {
            Wearable.getMessageClient(context).removeListener(messageListener)
            Wearable.getCapabilityClient(context).removeListener(capabilityListener)
        }
    }
}
