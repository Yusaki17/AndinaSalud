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
    data class EstadoInfo(
        val texto: String,
        val esProgramada: Boolean,
        val indicaciones: String?,
        val motivo: String?
    )

    val info = when (val est = this.estado) {
        is EstadoCita.Programada -> EstadoInfo(
            texto = "Programada${if (est.recordatorioActivo) " (con recordatorio)" else ""}",
            esProgramada = true,
            indicaciones = null,
            motivo = null
        )
        is EstadoCita.Atendida -> EstadoInfo(
            texto = "Atendida",
            esProgramada = false,
            indicaciones = est.indicaciones,
            motivo = null
        )
        is EstadoCita.Cancelada -> EstadoInfo(
            texto = "Cancelada",
            esProgramada = false,
            indicaciones = null,
            motivo = est.motivo
        )
    }

    return DetalleCitaUi(
        id = this.id,
        especialidad = this.especialidad,
        medico = this.medico,
        sede = this.sede,
        fecha = this.fecha,
        hora = this.hora,
        estadoTexto = info.texto,
        esProgramada = info.esProgramada,
        indicaciones = info.indicaciones,
        motivoCancelacion = info.motivo,
        puedeCancelarse = info.esProgramada
    )
}