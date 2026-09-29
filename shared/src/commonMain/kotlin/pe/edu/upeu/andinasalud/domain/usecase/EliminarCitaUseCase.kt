package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

class EliminarCitaUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return runCatching {
            repository.eliminarCita(id)
        }
    }
}