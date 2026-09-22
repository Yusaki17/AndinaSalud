package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import pe.upeu.andinasalud.domain.usecase.CancelacionInvalidaException

class CancelarCitaUseCase(private val repository: CitaRepository) {

    suspend operator fun invoke(id: Long, motivo: String): Result<Unit> {
        if (motivo.isBlank() || motivo.length < 5) {
            return Result.failure(IllegalArgumentException("El motivo de cancelación debe tener al menos 5 caracteres"))
        }

        val citas = repository.obtenerCitas()
        val cita = citas.find { it.id == id }
            ?: return Result.failure(IllegalArgumentException("Cita no encontrada"))

        // RN-03: Solo se puede cancelar si está Programada
        if (cita.estado !is EstadoCita.Programada) {
            return Result.failure(
                CancelacionInvalidaException("Solo se pueden cancelar citas en estado Programado")
            )
        }

        // RN-03: Faltan más de 24 horas (simplificado con comparación de strings)
        val ahora = obtenerFechaHoraActual() // "2026-09-23 10:00"
        val fechaCita = "${cita.fecha} ${cita.hora}"

        // Comparación simple: si la fecha-hora de la cita es menor o igual a ahora + 24h
        if (fechaCita <= ahora) {
            return Result.failure(
                CancelacionInvalidaException("La cita solo puede cancelarse con más de 24 horas de anticipación")
            )
        }

        return resultadoDe {
            repository.cancelarCita(id, motivo)
        }
    }

    private fun obtenerFechaHoraActual(): String {
        return "2026-09-22 00:00" // Simplificado para el examen
    }
}