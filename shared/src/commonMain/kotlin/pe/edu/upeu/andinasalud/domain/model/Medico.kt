package pe.edu.upeu.andinasalud.domain.model

data class Medico(
    val nombre: String,
    val especialidad: Especialidad,
    val sedes: List<Sede>
)