package pe.edu.upeu.andinasalud.presentation.detalle

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita

/**
 * Representación UI específica para la pantalla de Detalle de Cita.
 * Contiene toda la información necesaria para dibujar la tarjeta de detalle,
 * incluyendo datos que solo se muestran aquí (indicaciones, motivo de cancelación).
 */
data class DetalleCitaUi(
    val id: Long,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: String,
    val hora: String,
    val estadoTexto: String,
    val esProgramada: Boolean,
    val indicaciones: String?,       // Solo visible si fue Atendida
    val motivoCancelacion: String?,  // Solo visible si fue Cancelada
    val puedeCancelarse: Boolean     // Control visual del botón (RN-03)
)

/**
 * Extensión para mapear el modelo de dominio directamente al modelo de detalle.
 */
fun Cita.aDetalleUi(): DetalleCitaUi {
    val (estadoTexto, esProgramada, indicaciones, motivo) = when (val est = this.estado) {
        is EstadoCita.Programada ->
            "Programada${if (est.recordatorioActivo) " (con recordatorio)" else ""}" to true to null to null
        is EstadoCita.Atendida ->
            "Atendida" to false to est.indicaciones to null
        is EstadoCita.Cancelada ->
            "Cancelada" to false to null to est.motivo
    }

    // RN-03: Visualmente solo habilitamos el botón si está Programada.
    // La validación de las 24 horas se hace en el UseCase al hacer clic.
    val puedeCancelarse = esProgramada

    return DetalleCitaUi(
        id = this.id,
        especialidad = this.especialidad,
        medico = this.medico,
        sede = this.sede,
        fecha = this.fecha,
        hora = this.hora,
        estadoTexto = estadoTexto,
        esProgramada = esProgramada,
        indicaciones = indicaciones,
        motivoCancelacion = motivo,
        puedeCancelarse = puedeCancelarse
    )
}