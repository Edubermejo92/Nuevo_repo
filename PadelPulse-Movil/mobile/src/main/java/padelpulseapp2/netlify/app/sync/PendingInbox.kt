package padelpulseapp2.netlify.app.sync

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Buzon para los mensajes que llegan del reloj con la app cerrada o con el WebView
 * todavia sin cargar. Se guarda el ultimo mensaje de cada ruta (no hace falta el
 * historial: el estado es completo) y la capa JS lo vacia al arrancar.
 */
object PendingInbox {

    private const val PREFS = "padel"
    private const val KEY = "pending_inbox"

    @Synchronized
    fun put(context: Context, path: String, payload: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val map = read(prefs.getString(KEY, null))
        map[path] = payload
        val out = JSONObject()
        map.forEach { (k, v) -> out.put(k, v) }
        prefs.edit().putString(KEY, out.toString()).apply()
    }

    /** Devuelve los mensajes pendientes como array JSON y vacia el buzon. */
    @Synchronized
    fun drain(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val map = read(prefs.getString(KEY, null))
        prefs.edit().remove(KEY).apply()
        val arr = JSONArray()
        // Orden estable: ajustes y emparejado antes que el estado del partido
        val order = listOf(
            SyncProtocol.PATH_HELLO, SyncProtocol.PATH_PAIR, SyncProtocol.PATH_LEGACY_BT,
            SyncProtocol.PATH_SETTINGS, SyncProtocol.PATH_HEALTH,
            SyncProtocol.PATH_CMD, SyncProtocol.PATH_LEGACY_POINT,
            SyncProtocol.PATH_STATE, SyncProtocol.PATH_LEGACY_SYNC
        )
        (order + map.keys.filterNot { it in order }).distinct().forEach { path ->
            map[path]?.let { arr.put(JSONObject().put("path", path).put("payload", it)) }
        }
        return arr.toString()
    }

    private fun read(raw: String?): LinkedHashMap<String, String> {
        val map = LinkedHashMap<String, String>()
        if (raw.isNullOrEmpty()) return map
        runCatching {
            val obj = JSONObject(raw)
            obj.keys().forEach { k -> map[k] = obj.optString(k, "") }
        }
        return map
    }
}
