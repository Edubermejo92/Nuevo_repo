package padelpulseapp2.netlify.app

import androidx.compose.ui.graphics.Color

object ThemeUtils {
    fun getColor(themeName: String): Color {
        return when (themeName.lowercase()) {
            "neon" -> Color(0xFF00FD87)
            "fuego" -> Color(0xFFFF6B1A)
            "hielo" -> Color(0xFF38D4FF)
            "clasico" -> Color(0xFFC8E840)
            "noche" -> Color(0xFFA070FF)
            "oro" -> Color(0xFFFFD700)
            else -> Color(0xFF00FD87)
        }
    }

    fun getBgColor(themeName: String): Color {
        return getColor(themeName).copy(alpha = 0.2f)
    }

    fun getHexColor(theme: String): String = when (theme.lowercase()) {
        "neon" -> "#00FD87"
        "fuego" -> "#FF6B1A"
        "hielo" -> "#38D4FF"
        "clasico" -> "#C8E840"
        "noche" -> "#A070FF"
        "oro" -> "#FFD700"
        else -> "#00FD87"
    }

    fun getDotColor(theme: String): Color = getColor(theme)

    /** Inverso de getHexColor: el movil v2 manda el tema como color hex. */
    fun themeFromHex(hex: String): String = when (hex.uppercase().trim()) {
        "#00FD87" -> "neon"
        "#FF6B1A" -> "fuego"
        "#38D4FF" -> "hielo"
        "#C8E840" -> "clasico"
        "#A070FF" -> "noche"
        "#FFD700" -> "oro"
        else -> "neon"
    }

    /** Fondo tenue con el acento del tema, para tarjetas y estados activos. */
    fun tint(theme: String, alpha: Float): Color = getColor(theme).copy(alpha = alpha)

    val themesList = listOf("neon", "fuego", "hielo", "clasico", "noche", "oro")
}
