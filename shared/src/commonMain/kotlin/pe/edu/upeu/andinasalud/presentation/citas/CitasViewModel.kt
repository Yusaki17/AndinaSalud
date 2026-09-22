package pe.edu.upeu.andinasalud.presentation.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase

class CitasViewModel(
    private val obtenerCitasUseCase: ObtenerCitasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CitasUiState>(CitasUiState.Cargando)
    val uiState: StateFlow<CitasUiState> = _uiState.asStateFlow()

    // Almacena la lista completa para filtrar localmente sin latencia
    private var citasOriginales: List<CitaUi> = emptyList()

    init {
        cargarCitas()
    }

    fun cargarCitas() {
        viewModelScope.launch {
            _uiState.value = CitasUiState.Cargando

            obtenerCitasUseCase()
                .onSuccess { citas ->
                    citasOriginales = citas.map { it.aUi() }
                    aplicarFiltros()
                }
                .onFailure {
                    _uiState.value = CitasUiState.Error("No se pudo cargar la lista de citas. Intente nuevamente.")
                }
        }
    }

    fun onFiltroChange(filtro: FiltroCita) {
        _uiState.update { currentState ->
            if (currentState is CitasUiState.ConCitas) {
                currentState.copy(filtro = filtro)
            } else currentState
        }
        aplicarFiltros()
    }

    fun onBusquedaChange(query: String) {
        _uiState.update { currentState ->
            if (currentState is CitasUiState.ConCitas) {
                currentState.copy(busqueda = query)
            } else currentState
        }
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val currentState = _uiState.value as? CitasUiState.ConCitas ?: return

        val filtradasPorEstado = when (currentState.filtro) {
            FiltroCita.TODAS -> citasOriginales
            FiltroCita.PROGRAMADA -> citasOriginales.filter { it.esProgramada }
            FiltroCita.ATENDIDA -> citasOriginales.filter { it.estadoTexto.startsWith("Atendida") }
            FiltroCita.CANCELADA -> citasOriginales.filter { it.estadoTexto.startsWith("Cancelada") }
        }

        val resultadoFinal = if (currentState.busqueda.isBlank()) {
            filtradasPorEstado
        } else {
            val queryNormalizada = currentState.busqueda.normalizarParaBusqueda()
            filtradasPorEstado.filter { cita ->
                cita.especialidad.normalizarParaBusqueda().contains(queryNormalizada) ||
                        cita.medico.normalizarParaBusqueda().contains(queryNormalizada)
            }
        }

        _uiState.value = if (resultadoFinal.isEmpty() && citasOriginales.isNotEmpty()) {
            CitasUiState.SinCitas // O un estado de "Sin resultados para esta búsqueda"
        } else if (resultadoFinal.isEmpty()) {
            CitasUiState.SinCitas
        } else {
            CitasUiState.ConCitas(resultadoFinal, currentState.filtro, currentState.busqueda)
        }
    }
}