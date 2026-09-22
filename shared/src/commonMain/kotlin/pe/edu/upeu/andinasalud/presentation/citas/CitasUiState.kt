package pe.edu.upeu.andinasalud.presentation.citas

/**
 * Fases excluyentes de la pantalla de Citas (RF-08)
 */
sealed interface CitasUiState {
    data object Cargando : CitasUiState
    data object SinCitas : CitasUiState
    data class ConCitas(
        val citas: List<CitaUi>,
        val filtro: FiltroCita = FiltroCita.TODAS,
        val busqueda: String = ""
    ) : CitasUiState
    data class Error(val mensaje: String) : CitasUiState
}

enum class FiltroCita {
    TODAS, PROGRAMADA, ATENDIDA, CANCELADA
}