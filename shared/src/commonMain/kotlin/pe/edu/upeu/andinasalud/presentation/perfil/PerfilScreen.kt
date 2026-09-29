package pe.edu.upeu.andinasalud.presentation.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.andinasalud.data.local.CitasSimuladas

@Composable
fun PerfilScreen(
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val paciente = CitasSimuladas.paciente

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)

        // Tarjeta de datos del paciente
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(paciente.nombre, style = MaterialTheme.typography.titleLarge)
                        Text("Documento: ${paciente.documento}", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                HorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(paciente.correo)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(paciente.telefono ?: "No registrado")
                }
            }
        }

        // Tarjeta de modo oscuro (RF-06)
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Modo Oscuro", style = MaterialTheme.typography.titleMedium)
                }
                Switch(checked = darkTheme, onCheckedChange = onThemeChange)
            }
        }
    }
}