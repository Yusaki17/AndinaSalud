package pe.edu.upeu.andinasalud.navigation

sealed class Screen {
    data object Inicio : Screen()
    data object Citas : Screen()
    data object Perfil : Screen()
    data object SolicitudCita : Screen()
    data object DetalleCita : Screen()  // ← data object, NO data class
}