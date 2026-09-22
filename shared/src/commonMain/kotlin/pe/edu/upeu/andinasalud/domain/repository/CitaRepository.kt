package pe.edu.upeu.andinasalud.domain.repository

import pe.edu.upeu.andinasalud.domain.model.Cita

interface CitaRepository {
    /**
     * Obtiene todas las citas del paciente
     */
    suspend fun obtenerCitas(): List<Cita>

    /**
     * Registra una nueva cita
     * @return el id de la cita creada
     */
    suspend fun registrarCita(cita: Cita): Long

    /**
     * Cancela una cita existente
     */
    suspend fun cancelarCita(id: Long, motivo: String)
}