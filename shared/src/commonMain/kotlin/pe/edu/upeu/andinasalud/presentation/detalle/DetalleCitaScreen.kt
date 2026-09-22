package pe.edu.upeu.andinasalud.presentation.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.andinasalud.presentation.components.EstadoVacio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    viewModel: DetalleCitaViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estado local para el diálogo (State Hoisting)
    var mostrarDialogoCancelacion by remember { mutableStateOf(false) }
    var motivoCancelacion by remember { mutableStateOf("") }

    // Si se canceló con éxito, volvemos automáticamente
    if (uiState is DetalleCitaUiState.CanceladoConExito) {
        LaunchedEffect(Unit) {
            onVolver()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la cita") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is DetalleCitaUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetalleCitaUiState.Detalle -> {
                    DetalleContent(
                        cita = state.cita,
                        onCancelarClick = { mostrarDialogoCancelacion = true }
                    )
                }
                is DetalleCitaUiState.Error -> {
                    EstadoVacio(
                        icono = Icons.Default.Error,
                        titulo = "Error de carga",
                        descripcion = state.mensaje,
                        colorIcono = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }

    // Diálogo de confirmación (RF-03)
    if (mostrarDialogoCancelacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCancelacion = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Cancelar cita?") },
            text = {
                Column {
                    Text("Esta acción no se puede deshacer.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = motivoCancelacion,
                        onValueChange = { motivoCancelacion = it },
                        label = { Text("Motivo de cancelación") },
                        isError = motivoCancelacion.isNotBlank() && motivoCancelacion.length < 5,
                        supportingText = {
                            if (motivoCancelacion.isNotBlank() && motivoCancelacion.length < 5) {
                                Text("El motivo debe tener al menos 5 caracteres")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelarCita(motivoCancelacion)
                        mostrarDialogoCancelacion = false
                    },
                    enabled = motivoCancelacion.length >= 5
                ) { Text("Confirmar cancelación") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCancelacion = false }) { Text("No, volver") }
            }
        )
    }
}

@Composable
private fun DetalleContent(
    cita: DetalleCitaUi,
    onCancelarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(cita.especialidad, style = MaterialTheme.typography.headlineSmall)
                Text(cita.medico, style = MaterialTheme.typography.titleMedium)

                HorizontalDivider()

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(cita.sede, style = MaterialTheme.typography.bodyLarge)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("${cita.fecha} a las ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
                }

                HorizontalDivider()

                AssistChip(
                    onClick = {},
                    label = { Text(cita.estadoTexto) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (cita.esProgramada) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                // Mostrar indicaciones si fue atendida (RF-03)
                if (cita.estadoTexto.startsWith("Atendida")) {
                    Text(
                        text = "Indicaciones: Control médico posterior.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Botón de cancelar solo si está programada (RN-03)
        if (cita.esProgramada) {
            Button(
                onClick = onCancelarClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cancelar cita")
            }
        }
    }
}