package pe.edu.upeu.andinasalud.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// === PALETA ÍNDIGO + ÁMBAR ===

private val IndigoPrimario = Color(0xFF3F51B5)
private val IndigoOnPrimario = Color(0xFFFFFFFF)
private val IndigoContainer = Color(0xFFC5CAE9)
private val IndigoOnContainer = Color(0xFF1A237E)

private val AmbarSecundario = Color(0xFFFFC107)
private val AmbarOnSecundario = Color(0xFF000000)
private val AmbarContainer = Color(0xFFFFECB3)
private val AmbarOnContainer = Color(0xFF5D4037)

private val RojoError = Color(0xFFD32F2F)
private val RojoOnError = Color(0xFFFFFFFF)
private val RojoErrorContainer = Color(0xFFFFCDD2)
private val RojoOnErrorContainer = Color(0xFFB71C1C)

private val BlancoSurface = Color(0xFFFAFAFA)
private val NegroSurface = Color(0xFF121212)

// Esquema claro
private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimario,
    onPrimary = IndigoOnPrimario,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = IndigoOnContainer,
    secondary = AmbarSecundario,
    onSecondary = AmbarOnSecundario,
    secondaryContainer = AmbarContainer,
    onSecondaryContainer = AmbarOnContainer,
    error = RojoError,
    onError = RojoOnError,
    errorContainer = RojoErrorContainer,
    onErrorContainer = RojoOnErrorContainer,
    surface = BlancoSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF9F9F9),
    surfaceContainer = Color(0xFFF3F3F3),
    surfaceContainerHigh = Color(0xFFEDEDED),
    surfaceContainerHighest = Color(0xFFE7E7E7),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E)
)

// Esquema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9FA8DA),
    onPrimary = Color(0xFF1A237E),
    primaryContainer = Color(0xFF283593),
    onPrimaryContainer = Color(0xFFC5CAE9),
    secondary = Color(0xFFFFD54F),
    onSecondary = Color(0xFF5D4037),
    secondaryContainer = Color(0xFF8D6E00),
    onSecondaryContainer = Color(0xFFFFECB3),
    error = Color(0xFFEF9A9A),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFC62828),
    onErrorContainer = Color(0xFFFFCDD2),
    surface = NegroSurface,
    onSurface = Color(0xFFE6E1E5),
    surfaceContainerLowest = Color(0xFF0A0A0A),
    surfaceContainerLow = Color(0xFF171717),
    surfaceContainer = Color(0xFF1F1F1F),
    surfaceContainerHigh = Color(0xFF292929),
    surfaceContainerHighest = Color(0xFF343434),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99)
)

@Composable
fun AndinaSaludTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}