package pe.edu.upeu.andinasalud.domain.usecase

import kotlinx.coroutines.CancellationException

/**
 * Helper que envuelve operaciones suspend en un Result.
 * Relanza CancellationException para no romper corrutinas de Compose.
 */
suspend fun <T> resultadoDe(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e // Relanza la excepción de cancelación
    } catch (e: Exception) {
        Result.failure(e)
    }
}