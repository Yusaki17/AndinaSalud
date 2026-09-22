package pe.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import pe.edu.upeu.andinasalud.domain.usecase.resultadoDe


class ObtenerCitasUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(): Result<List<Cita>> = resultadoDe {
        repository.obtenerCitas()
    }
}