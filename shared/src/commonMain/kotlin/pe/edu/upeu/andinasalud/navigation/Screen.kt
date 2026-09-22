package pe.edu.upeu.andinasalud.navigation

/**
 * Destinos de navegación de la aplicación AndinaSalud.
 * RF-07: Barra de navegación inferior con tres destinos (Inicio, Citas, Perfil)
 * más navegación interna a Detalle y Solicitud.
 */
sealed class Screen {

    // Destinos de la barra de navegación inferior
    data object Inicio : Screen()
    data object Citas : Screen()
    data object Perfil : Screen()

    // Destinos de navegación interna (no aparecen en la barra inferior)
    data class DetalleCita(val citaId: Long) : Screen()
    data object SolicitudCita : Screen()
}