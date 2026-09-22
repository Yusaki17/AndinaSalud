package pe.edu.upeu.andinasalud.domain.repository

import pe.edu.upeu.andinasalud.domain.model.Cita

/**
 * Repositorio de citas médicas de AndinaSalud.
 * Define las operaciones disponibles para gestionar citas del paciente.
 */
interface CitaRepository {

    /**
     * Obtiene todas las citas del paciente ordenadas por fecha y hora.
     */
    suspend fun obtenerCitas(): List<Cita>

    /**
     * Registra una nueva cita médica.
     * @param cita la cita a registrar (sin id asignado)
     * @return el id de la cita creada
     */
    suspend fun registrarCita(cita: Cita): Long

    /**
     * Cancela una cita existente.
     * @param id el id de la cita a cancelar
     * @param motivo el motivo de la cancelación
     */
    suspend fun cancelarCita(id: Long, motivo: String)
}