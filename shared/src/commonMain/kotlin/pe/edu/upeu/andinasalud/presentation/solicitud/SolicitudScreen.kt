package pe.edu.upeu.andinasalud.presentation.solicitud

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estados para los dropdowns
    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedSede by remember { mutableStateOf(false) }

    if (uiState.isSuccess) {
        Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cita solicitada con éxito", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onVolver) { Text("Ver mis citas") } // ← Cambia el texto
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Solicitar nueva cita", style = MaterialTheme.typography.headlineMedium)

            // ✅ Selector de Especialidad con dropdown
            ExposedDropdownMenuBox(
                expanded = expandedEspecialidad,
                onExpandedChange = { expandedEspecialidad = it }
            ) {
                OutlinedTextField(
                    value = uiState.especialidad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Especialidad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecialidad) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    isError = uiState.errorEspecialidad != null,
                    supportingText = uiState.errorEspecialidad?.let { { Text(it) } }
                )

                ExposedDropdownMenu(
                    expanded = expandedEspecialidad,
                    onDismissRequest = { expandedEspecialidad = false }
                ) {
                    viewModel.especialidades.forEach { especialidad ->
                        DropdownMenuItem(
                            text = { Text(especialidad) },
                            onClick = {
                                viewModel.onEspecialidadChange(especialidad)
                                expandedEspecialidad = false
                            }
                        )
                    }
                }
            }

            // ✅ Selector de Sede con dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSede,
                onExpandedChange = { expandedSede = it }
            ) {
                OutlinedTextField(
                    value = uiState.sede,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sede") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSede) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    isError = uiState.errorSede != null,
                    supportingText = uiState.errorSede?.let { { Text(it) } }
                )

                ExposedDropdownMenu(
                    expanded = expandedSede,
                    onDismissRequest = { expandedSede = false }
                ) {
                    viewModel.sedes.forEach { sede ->
                        DropdownMenuItem(
                            text = { Text(sede) },
                            onClick = {
                                viewModel.onSedeChange(sede)
                                expandedSede = false
                            }
                        )
                    }
                }
            }

            // Fecha y Hora
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.fecha,
                    onValueChange = viewModel::onFechaChange,
                    label = { Text("Fecha (${SolicitudConstantes.FORMATO_FECHA})") },
                    isError = uiState.errorFecha != null ||
                            (uiState.fecha.isNotBlank() && !uiState.fecha.esFormatoFechaValido()),
                    supportingText = uiState.errorFecha?.let {
                        { Text(it) }
                    } ?: if (uiState.fecha.isNotBlank() && !uiState.fecha.esFormatoFechaValido()) {
                        { Text("Usa el formato ${SolicitudConstantes.FORMATO_FECHA}") }
                    } else null,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = uiState.hora,
                    onValueChange = viewModel::onHoraChange,
                    label = { Text("Hora (${SolicitudConstantes.FORMATO_HORA})") },
                    isError = uiState.errorHora != null ||
                            (uiState.hora.isNotBlank() && !uiState.hora.esFormatoHoraValido()),
                    supportingText = uiState.errorHora?.let {
                        { Text(it) }
                    } ?: if (uiState.hora.isNotBlank() && !uiState.hora.esFormatoHoraValido()) {
                        { Text("Usa el formato ${SolicitudConstantes.FORMATO_HORA}") }
                    } else null,
                    modifier = Modifier.weight(1f)
                )
            }

            // Motivo
            OutlinedTextField(
                value = uiState.motivo,
                onValueChange = viewModel::onMotivoChange,
                label = { Text("Motivo de la consulta") },
                isError = uiState.errorMotivo != null,
                supportingText = uiState.errorMotivo?.let { { Text(it) } },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Error Global
            uiState.errorGlobal?.let { error ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::solicitarCita,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Confirmar Cita")
                }
            }
        }
    }
}