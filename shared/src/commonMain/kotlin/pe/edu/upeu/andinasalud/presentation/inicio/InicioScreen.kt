package pe.edu.upeu.andinasalud.presentation.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.edu.upeu.andinasalud.data.local.CitasSimuladas
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.navigation.Screen

data class OpcionInicio(
    val screen: Screen,
    val icono: androidx.compose.ui.graphics.vector.ImageVector,
    val titulo: String,
    val descripcion: String
)

val OPCIONES_INICIO = listOf(
    OpcionInicio(
        screen = Screen.Citas,
        icono = Icons.Default.ListAlt,
        titulo = "Mis citas",
        descripcion = "Revisa y gestiona tus citas"
    ),
    OpcionInicio(
        screen = Screen.SolicitudCita,
        icono = Icons.Default.AddCircle,
        titulo = "Solicitar cita",
        descripcion = "Agenda una nueva consulta"
    )
)

private fun obtenerProximaCitaProgramada() = CitasSimuladas.citas
    .filter { it.estado is EstadoCita.Programada }
    .sortedWith(compareBy({ it.fecha }, { it.hora }))
    .firstOrNull()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onNavegar: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val paciente = CitasSimuladas.paciente
    val proximaCita = obtenerProximaCitaProgramada()

    // ✅ CORRECCIÓN: verticalScroll está bien, pero eliminamos el LazyVerticalGrid de adentro
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Saludo
        Column {
            Text(
                text = "Hola, ${paciente.nombre.split(" ").first()}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Bienvenido a AndinaSalud",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 2. Tarjeta destacada con la próxima cita
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tu próxima cita",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (proximaCita != null) {
                    Text(
                        text = proximaCita.especialidad,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${proximaCita.medico} · ${proximaCita.sede}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${proximaCita.fecha} a las ${proximaCita.hora}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Text(
                        text = "No tienes citas programadas en este momento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // 3. Accesos rápidos (✅ CORREGIDO: Usamos Row simple en vez de LazyVerticalGrid)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Accesos rápidos",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OPCIONES_INICIO.forEach { opcion ->
                    Card(
                        onClick = { onNavegar(opcion.screen) },
                        modifier = Modifier.weight(1f) // Divide el espacio equitativamente
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = opcion.icono,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = opcion.titulo,
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = opcion.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}