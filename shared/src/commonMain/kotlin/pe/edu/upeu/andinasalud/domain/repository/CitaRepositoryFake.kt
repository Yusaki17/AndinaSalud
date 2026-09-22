package pe.edu.upeu.andinasalud.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.andinasalud.data.local.CitasSimuladas
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

/**
 * Implementación fake del repositorio de citas.
 * Simula operaciones en memoria con latencia artificial de 800ms.
 */
class CitaRepositoryFake : CitaRepository {

    private val mutex = Mutex()
    private val citas = CitasSimuladas.citas.toMutableList()
    private var nextId = (citas.maxOfOrNull { it.id } ?: 0L) + 1

    override suspend fun obtenerCitas(): List<Cita> {
        delay(800) // Simula latencia de red (RF-08)
        return mutex.withLock {
            citas.sortedWith(
                compareBy<Cita> { it.fecha }.thenBy { it.hora }
            )
        }
    }

    override suspend fun registrarCita(cita: Cita): Long {
        delay(800) // Simula latencia de red
        return mutex.withLock {
            val nuevaCita = cita.copy(id = nextId++)
            citas.add(nuevaCita)
            nuevaCita.id
        }
    }

    override suspend fun cancelarCita(id: Long, motivo: String) {
        delay(800) // Simula latencia de red
        mutex.withLock {
            val index = citas.indexOfFirst { it.id == id }
            if (index != -1) {
                val citaActual = citas[index]
                citas[index] = citaActual.copy(
                    estado = EstadoCita.Cancelada(
                        motivo = motivo,
                        canceladaPorPaciente = true
                    )
                )
            }
        }
    }
}