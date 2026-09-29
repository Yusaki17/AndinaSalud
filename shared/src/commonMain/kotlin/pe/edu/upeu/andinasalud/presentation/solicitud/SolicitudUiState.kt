package pe.edu.upeu.andinasalud.presentation.solicitud

/**
 * Estado completo del formulario de solicitud de cita (RF-04).
 * Mantiene los valores ingresados y los errores de validación por campo.
 */
data class SolicitudUiState(
    val especialidad: String = "",
    val sede: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",

    // Errores de validación (se muestran debajo del campo)
    val errorEspecialidad: String? = null,
    val errorSede: String? = null,
    val errorFecha: String? = null,
    val errorHora: String? = null,
    val errorMotivo: String? = null,
    val errorGlobal: String? = null, // Para RN-02 y RN-05

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)