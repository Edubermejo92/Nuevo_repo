package padelpulseapp2.netlify.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

/**
 * Piezas visuales compartidas por todas las pantallas del reloj.
 * Objetivo: una sola escala tipografica y un solo lenguaje de tarjetas, para que
 * la app no parezca seis pantallas distintas pegadas.
 */
object PP {

    // Superficies (negro puro de fondo: en pantallas OLED gasta menos bateria)
    val Bg = Color(0xFF000000)
    val Surface = Color(0xFF121212)
    val SurfaceHigh = Color(0xFF1C1C1C)
    val Line = Color(0xFF262626)
    val TextDim = Color(0xFF8A8A8A)
    val TextMuted = Color(0xFF5C5C5C)
    val Danger = Color(0xFFFF4E50)
    val Warn = Color(0xFFFFB020)

    // Escala tipografica: 5 tamaños, ni uno mas
    val Display = 34.sp
    val Title = 15.sp
    val Body = 12.sp
    val Label = 10.sp
    val Micro = 8.sp

    val CardShape = RoundedCornerShape(16.dp)
    val PillShape = RoundedCornerShape(50)
}

/** Etiqueta en mayusculas con tracking: el patron de titulillo de toda la app. */
@Composable
fun PPLabel(
    text: String,
    color: Color = PP.TextDim,
    size: androidx.compose.ui.unit.TextUnit = PP.Micro,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        color = color,
        fontSize = size,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

/** Tarjeta base. Todo lo que agrupa informacion usa esta, sin excepciones. */
@Composable
fun PPCard(
    modifier: Modifier = Modifier,
    accent: Color? = null,
    padding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(PP.CardShape)
            .background(PP.Surface)
            .then(
                if (accent != null) Modifier.border(1.dp, accent.copy(alpha = 0.35f), PP.CardShape)
                else Modifier
            )
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

/**
 * Pildora de estado del enlace con el movil. Se ve en todas las pantallas:
 * en pista hay que saber de un vistazo si el marcador esta viajando o no.
 */
@Composable
fun PPStatusPill(
    label: String,
    color: Color,
    pulsing: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(PP.PillShape)
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), PP.PillShape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(if (pulsing) color else color.copy(alpha = 0.6f))
        )
        PPLabel(label, color = color, size = PP.Micro)
    }
}

/** Degradado sutil detras del marcador, con el acento del tema activo. */
fun accentGlow(accent: Color): Brush = Brush.verticalGradient(
    listOf(accent.copy(alpha = 0.14f), Color.Transparent)
)
