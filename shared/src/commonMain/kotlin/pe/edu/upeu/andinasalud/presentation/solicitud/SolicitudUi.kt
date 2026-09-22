package pe.edu.upeu.andinasalud.presentation.solicitud

/**
 * Constantes de negocio usadas en la UI de solicitud de cita.
 * Centralizadas aquí para evitar hardcodeo en ViewModel y Screen (RF-04).
 */
object SolicitudConstantes {

    /**
     * Especialidades disponibles en la red AndinaSalud.
     * Si mañana se agregan más, solo se modifica esta lista.
     */
    val ESPECIALIDADES = listOf(
        "Medicina General",
        "Odontología",
        "Pediatría",
        "Nutrición",
        "Psicología"
    )

    /**
     * Sedes disponibles en la red AndinaSalud.
     */
    val SEDES = listOf(
        "Ñaña",
        "Chosica",
        "Chaclacayo",
        "Santa Anita"
    )

    /**
     * Longitud mínima y máxima del motivo (RN-04).
     */
    const val MOTIVO_MIN_CARACTERES = 10
    const val MOTIVO_MAX_CARACTERES = 200

    /**
     * Formatos esperados para fecha y hora.
     */
    const val FORMATO_FECHA = "YYYY-MM-DD"
    const val FORMATO_HORA = "HH:mm"
}

/**
 * Representación UI de una opción de especialidad para el selector.
 * Útil si en el futuro se quiere mostrar iconos o descripciones por especialidad.
 */
data class EspecialidadUi(
    val nombre: String,
    val iconoResId: String? = null // Placeholder para futuro icono
)

/**
 * Representación UI de una opción de sede para el selector.
 */
data class SedeUi(
    val nombre: String,
    val direccion: String? = null // Placeholder para futuro detalle
)

/**
 * Extensiones para mapear las listas de constantes a sus representaciones UI.
 */
fun List<String>.aEspecialidadesUi(): List<EspecialidadUi> =
    map { EspecialidadUi(nombre = it) }

fun List<String>.aSedesUi(): List<SedeUi> =
    map { SedeUi(nombre = it) }

/**
 * Helper para validar visualmente el formato de fecha (YYYY-MM-DD).
 * No valida lógica de negocio (eso lo hace el UseCase), solo el patrón.
 */
fun String.esFormatoFechaValido(): Boolean {
    val regex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
    return regex.matches(this)
}

/**
 * Helper para validar visualmente el formato de hora (HH:mm).
 */
fun String.esFormatoHoraValido(): Boolean {
    val regex = Regex("^\\d{2}:\\d{2}$")
    return regex.matches(this)
}