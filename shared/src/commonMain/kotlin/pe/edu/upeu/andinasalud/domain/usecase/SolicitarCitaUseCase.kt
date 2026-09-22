package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import pe.upeu.andinasalud.domain.usecase.CitaInvalidaException
import pe.upeu.andinasalud.domain.usecase.ErroresDeCita

class SolicitarCitaUseCase(private val repository: CitaRepository) {

    suspend operator fun invoke(
        especialidad: String,
        sede: String,
        fecha: String,
        hora: String,
        motivo: String
    ): Result<Long> {
        val errores = ErroresDeCita(
            especialidad = if (especialidad.isBlank()) "La especialidad es obligatoria" else null,
            sede = if (sede.isBlank()) "La sede es obligatoria" else null,
            fecha = validarFecha(fecha),
            hora = validarHora(fecha, hora),
            motivo = validarMotivo(motivo)
        )

        if (errores.especialidad != null || errores.sede != null ||
            errores.fecha != null || errores.hora != null || errores.motivo != null) {
            return Result.failure(CitaInvalidaException(errores))
        }

        // RN-02: No más de 3 citas programadas
        val citasExistentes = repository.obtenerCitas()
        val citasProgramadas = citasExistentes.filter { it.estado is EstadoCita.Programada }

        if (citasProgramadas.size >= 3) {
            return Result.failure(
                CitaInvalidaException(errores.copy(global = "No puedes tener más de 3 citas programadas simultáneamente"))
            )
        }

        // RN-05: No dos citas en mismo día y hora
        val citaMismoDiaYHora = citasProgramadas.any { it.fecha == fecha && it.hora == hora }
        if (citaMismoDiaYHora) {
            return Result.failure(
                CitaInvalidaException(errores.copy(global = "Ya existe una cita programada en esa fecha y hora"))
            )
        }

        val nuevaCita = Cita(
            id = 0L,
            especialidad = especialidad.trim(),
            medico = "Por asignar",
            sede = sede.trim(),
            fecha = fecha.trim(),
            hora = hora.trim(),
            estado = EstadoCita.Programada(recordatorioActivo = true),
            motivo = motivo.trim()
        )

        return resultadoDe { repository.registrarCita(nuevaCita) }
    }

    // Validación manual de fecha "yyyy-MM-dd"
    private fun validarFecha(fecha: String): String? {
        if (fecha.isBlank()) return "La fecha es obligatoria"

        val partes = fecha.split("-")
        if (partes.size != 3) return "Formato de fecha inválido (use yyyy-MM-dd)"

        val anio = partes[0].toIntOrNull() ?: return "Formato de fecha inválido"
        val mes = partes[1].toIntOrNull() ?: return "Formato de fecha inválido"
        val dia = partes[2].toIntOrNull() ?: return "Formato de fecha inválido"

        if (anio !in 2000..2100) return "Año de fecha inválido"
        if (mes !in 1..12) return "Mes de fecha inválido"
        if (dia !in 1..31) return "Día de fecha inválido"

        // RN-01: No fechas anteriores a hoy
        val hoy = obtenerFechaHoy() // "2026-09-23"
        if (fecha < hoy) return "No se puede solicitar una cita en una fecha anterior a hoy"

        return null
    }

    // Validación manual de hora "HH:mm"
    private fun validarHora(fecha: String, hora: String): String? {
        if (hora.isBlank()) return "La hora es obligatoria"

        val partes = hora.split(":")
        if (partes.size != 2) return "Formato de hora inválido (use HH:mm)"

        val horas = partes[0].toIntOrNull() ?: return "Formato de hora inválido"
        val minutos = partes[1].toIntOrNull() ?: return "Formato de hora inválido"

        if (horas !in 0..23) return "Hora inválida"
        if (minutos !in 0..59) return "Minutos inválidos"

        // RN-01: Si es hoy, no puede ser hora pasada
        val hoy = obtenerFechaHoy()
        if (fecha == hoy && hora < obtenerHoraActual()) {
            return "No se puede solicitar una cita en una hora anterior a la actual"
        }

        return null
    }

    private fun validarMotivo(motivo: String): String? {
        return when {
            motivo.isBlank() -> "El motivo es obligatorio"
            motivo.length < 10 -> "El motivo debe tener al menos 10 caracteres"
            motivo.length > 200 -> "El motivo no puede exceder los 200 caracteres"
            else -> null
        }
    }

    // Helpers para obtener fecha y hora actual como strings
    private fun obtenerFechaHoy(): String {
        // Para el examen, usamos una fecha fija o la del sistema
        // En KMP real usarías kotlinx-datetime
        return "2026-09-23" // Reemplaza con la fecha actual real si tienes kotlinx-datetime
    }

    private fun obtenerHoraActual(): String {
        return "00:00" // Simplificado para el examen
    }
}