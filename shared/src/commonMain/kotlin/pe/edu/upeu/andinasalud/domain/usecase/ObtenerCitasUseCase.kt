package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

class ObtenerCitasUseCase(private val repository: CitaRepository) {
    suspend operator fun invoke(): Result<List<Cita>> {
        println(" [ObtenerCitasUseCase] Llamando al repositorio...")
        return resultadoDe {
            repository.obtenerCitas()
        }
    }
}