package pe.edu.upeu.andinasalud.data.local

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.model.Paciente

object CitasSimuladas {

    val paciente = Paciente(
        id = "P-0417",
        nombre = "Julio Ernsto Pantoja",
        documento = "70154823",
        correo = "juliopantoja@gmail.pe"
    )

    val sedes = listOf("Ñaña", "Chosica", "Chaclacayo", "Santa Anita")

    val especialidades = listOf(
        "Medicina General",
        "Odontología",
        "Pediatría",
        "Nutrición",
        "Psicología"
    )

    val citas = listOf(
        Cita(
            id = 1,
            especialidad = "Medicina General",
            medico = "Dr. Iván Rojas",
            sede = "Ñaña",
            fecha = "2026-09-18",
            hora = "09:00",
            estado = EstadoCita.Programada(recordatorioActivo = true)
        ),
        Cita(
            id = 2,
            especialidad = "Odontología",
            medico = "Dra. Rosa Flores",
            sede = "Chosica",
            fecha = "2026-09-21",
            hora = "16:30",
            estado = EstadoCita.Programada(recordatorioActivo = false)
        ),
        Cita(
            id = 3,
            especialidad = "Nutrición",
            medico = "Eliana Cabrera",
            sede = "Santa Anita",
            fecha = "2026-09-25",
            hora = "11:15",
            estado = EstadoCita.Programada(recordatorioActivo = true)
        ),
        Cita(
            id = 4,
            especialidad = "Pediatría",
            medico = "Dra. Carla Núñez",
            sede = "Chaclacayo",
            fecha = "2026-08-30",
            hora = "08:45",
            estado = EstadoCita.Atendida("Control en tres meses")
        ),
        Cita(
            id = 5,
            especialidad = "Psicología",
            medico = "Ps. Luis Tapia",
            sede = "Ñaña",
            fecha = "2026-09-02",
            hora = "15:00",
            estado = EstadoCita.Atendida("Continuar sesiones quincenales")
        ),
        Cita(
            id = 6,
            especialidad = "Medicina General",
            medico = "Dr. Iván Rojas",
            sede = "Chosica",
            fecha = "2026-09-05",
            hora = "10:30",
            estado = EstadoCita.Cancelada("Viaje del paciente", true)
        )
    )
}