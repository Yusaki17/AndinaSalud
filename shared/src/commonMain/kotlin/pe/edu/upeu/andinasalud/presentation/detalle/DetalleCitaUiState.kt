package pe.edu.upeu.andinasalud.presentation.detalle

/**
 * Fases de la pantalla de Detalle de Cita (RF-08)
 */
sealed interface DetalleCitaUiState {
    data object Cargando : DetalleCitaUiState
    data class Detalle(val cita: DetalleCitaUi) : DetalleCitaUiState // <-- Cambio aquí
    data object CanceladoConExito : DetalleCitaUiState
    data class Error(val mensaje: String) : DetalleCitaUiState
}