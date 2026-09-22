package pe.edu.upeu.andinasalud

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pe.edu.upeu.andinasalud.navigation.Screen
import pe.edu.upeu.andinasalud.presentation.components.EstadoVacio
import pe.edu.upeu.andinasalud.presentation.citas.CitasScreen
import pe.edu.upeu.andinasalud.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaScreen
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.andinasalud.presentation.inicio.InicioScreen
import pe.edu.upeu.andinasalud.presentation.perfil.PerfilScreen
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudScreen
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudViewModel
import pe.edu.upeu.andinasalud.theme.AndinaSaludTheme

private data class Destino(
    val screen: Screen,
    val titulo: String,
    val icono: ImageVector
)

private val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
    Destino(Screen.Citas, "Citas", Icons.Default.ListAlt),
    Destino(Screen.Perfil, "Perfil", Icons.Default.Person)
)

private val ScreenSaver = Saver<Screen, Int>(
    save = { pantalla ->
        DESTINOS.indexOfFirst { it.screen == pantalla }.takeIf { it != -1 } ?: 0
    },
    restore = { indice ->
        DESTINOS.getOrNull(indice)?.screen ?: Screen.Inicio
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() = KoinContext {

    var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) {
        mutableStateOf<Screen>(Screen.Inicio)
    }

    var darkTheme by rememberSaveable {
        mutableStateOf(false)
    }

    // Variable para guardar el ID de la cita seleccionada
    var citaIdSeleccionada by rememberSaveable { mutableStateOf(1L) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AndinaSaludTheme(darkTheme = darkTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerHeader()
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    DESTINOS.forEach { destino ->
                        NavigationDrawerItem(
                            label = { Text(destino.titulo) },
                            selected = pantallaActual == destino.screen,
                            onClick = {
                                pantallaActual = destino.screen
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = destino.icono,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider()
                    ModoOscuro(
                        activo = darkTheme,
                        onCambiar = { darkTheme = it }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = tituloDe(pantallaActual)) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (pantallaActual) {
                        Screen.Inicio -> InicioScreen(
                            onNavegar = { screen ->
                                pantallaActual = screen
                                scope.launch { drawerState.close() }
                            }
                        )

                        Screen.Citas -> {
                            val viewModel: CitasViewModel = koinViewModel()
                            CitasScreen(
                                viewModel = viewModel,
                                onNavegarADetalle = { id ->
                                    citaIdSeleccionada = id // Guardamos el ID seleccionado
                                    pantallaActual = Screen.DetalleCita
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }

                        Screen.DetalleCita -> {
                            // Inyectamos el ViewModel pasándole el ID guardado dinámicamente
                            val viewModel: DetalleCitaViewModel = koinViewModel { parametersOf(citaIdSeleccionada) }
                            DetalleCitaScreen(
                                viewModel = viewModel,
                                onVolver = {
                                    pantallaActual = Screen.Citas
                                }
                            )
                        }

                        Screen.SolicitudCita -> {
                            val viewModel: SolicitudViewModel = koinViewModel()
                            SolicitudScreen(
                                viewModel = viewModel,
                                onVolver = {
                                    pantallaActual = Screen.Citas
                                }
                            )
                        }

                        Screen.Perfil -> PerfilScreen(
                            darkTheme = darkTheme,
                            onThemeChange = { darkTheme = it }
                        )

                        else -> EstadoVacio(
                            icono = Icons.Default.Error,
                            titulo = "Pantalla no encontrada",
                            descripcion = "Módulo en construcción",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(28.dp)
            )
        }
        Column {
            Text(
                text = "AndinaSalud",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Gestión de citas médicas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ModoOscuro(
    activo: Boolean,
    onCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Modo oscuro",
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = activo,
            onCheckedChange = onCambiar
        )
    }
}

private fun tituloDe(screen: Screen): String {
    return when (screen) {
        Screen.Inicio -> "AndinaSalud"
        Screen.Citas -> "Mis Citas"
        Screen.DetalleCita -> "Detalle de Cita"
        Screen.SolicitudCita -> "Solicitar Cita"
        Screen.Perfil -> "Mi Perfil"
    }
}