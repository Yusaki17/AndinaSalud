package pe.edu.upeu.andinasalud.presentation.citas

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita

/**
 * Representación UI de una cita, aplanando la sealed class del dominio.
 * Contiene solo los datos necesarios para dibujar la tarjeta en pantalla.
 */
data class CitaUi(
    val id: Long,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: String,
    val hora: String,
    val estadoTexto: String,
    val esProgramada: Boolean
)

/**
 * Extensión para mapear el modelo de dominio al modelo de presentación.
 */
fun Cita.aUi(): CitaUi {
    val (estadoTexto, esProgramada) = when (val est = this.estado) {
        is EstadoCita.Programada -> "Programada${if (est.recordatorioActivo) " (con recordatorio)" else ""}" to true
        is EstadoCita.Atendida -> "Atendida" to false
        is EstadoCita.Cancelada -> "Cancelada" to false
    }

    return CitaUi(
        id = this.id,
        especialidad = this.especialidad,
        medico = this.medico,
        sede = this.sede,
        fecha = this.fecha,
        hora = this.hora,
        estadoTexto = estadoTexto,
        esProgramada = esProgramada
    )
}

/**
 * Helper para búsqueda insensible a mayúsculas y tildes (RF-05)
 */
fun String.normalizarParaBusqueda(): String {
    return this.lowercase()
        .replace("á", "a").replace("é", "e").replace("í", "i")
        .replace("ó", "o").replace("ú", "u").replace("ñ", "n")
        .trim()
}