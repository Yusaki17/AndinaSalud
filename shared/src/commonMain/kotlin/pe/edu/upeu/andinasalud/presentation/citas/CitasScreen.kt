package pe.edu.upeu.andinasalud.presentation.citas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.andinasalud.presentation.components.EstadoVacio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(
    viewModel: CitasViewModel,
    onNavegarADetalle: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // 1. Barra de Búsqueda (RF-05)
        val busqueda by viewModel.uiState.collectAsStateWithLifecycle()
        val textoBusqueda = (busqueda as? CitasUiState.ConCitas)?.busqueda ?: ""

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = viewModel::onBusquedaChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Buscar por especialidad o médico...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )

        // 2. Filtros por Estado (RF-02)
        val filtroActual = (busqueda as? CitasUiState.ConCitas)?.filtro ?: FiltroCita.TODAS

        ScrollableTabRow(
            selectedTabIndex = filtroActual.ordinal,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            FiltroCita.entries.forEach { filtro ->
                Tab(
                    selected = filtro == filtroActual,
                    onClick = { viewModel.onFiltroChange(filtro) },
                    text = {
                        Text(
                            filtro.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        }

        // 3. Contenido con Fases Exhaustivas (RF-08)
        Box(modifier = Modifier.weight(1f)) {
            when (val estado = uiState) {
                is CitasUiState.Cargando -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando tus citas...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                is CitasUiState.SinCitas -> {
                    EstadoVacio(
                        icono = Icons.Default.EventBusy,
                        titulo = "No hay citas",
                        descripcion = if (estado is CitasUiState.ConCitas && estado.busqueda.isNotBlank()) {
                            "No se encontraron citas con ese criterio de búsqueda."
                        } else {
                            "No tienes citas registradas en el sistema."
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is CitasUiState.ConCitas -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.citas, key = { it.id }) { cita ->
                            CitaItem(
                                cita = cita,
                                onClick = { onNavegarADetalle(cita.id) }
                            )
                        }
                    }
                }
                is CitasUiState.Error -> {
                    EstadoVacio(
                        icono = Icons.Default.Error,
                        titulo = "Error de conexión",
                        descripcion = estado.mensaje,
                        colorIcono = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        accion = {
                            Button(onClick = viewModel::cargarCitas) {
                                Text("Reintentar")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CitaItem(
    cita: CitaUi,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.especialidad, style = MaterialTheme.typography.titleMedium)
                Text(text = cita.medico, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${cita.fecha} a las ${cita.hora} · ${cita.sede}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AssistChip(
                    onClick = { },
                    label = { Text(cita.estadoTexto, style = MaterialTheme.typography.labelMedium) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (cita.esProgramada)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
    }
}