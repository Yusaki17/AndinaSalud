package pe.upeu.andinasalud.domain.usecase

data class ErroresDeCita(
    val especialidad: String? = null,
    val sede: String? = null,
    val fecha: String? = null,
    val hora: String? = null,
    val motivo: String? = null,
    val global: String? = null // Para RN-02 y RN-05
)

class CitaInvalidaException(val errores: ErroresDeCita) : Exception("Cita inválida")
class CancelacionInvalidaException(message: String) : Exception(message)