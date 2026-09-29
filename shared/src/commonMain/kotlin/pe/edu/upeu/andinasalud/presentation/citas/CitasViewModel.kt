package pe.edu.upeu.andinasalud.presentation.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.andinasalud.domain.usecase.EliminarCitaUseCase
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase

class CitasViewModel(
    private val obtenerCitasUseCase: ObtenerCitasUseCase,
    private val cancelarCitaUseCase: CancelarCitaUseCase,
    private val eliminarCitaUseCase: EliminarCitaUseCase // ← NUEVO
) : ViewModel() {

    private val _uiState = MutableStateFlow<CitasUiState>(CitasUiState.Cargando)
    val uiState: StateFlow<CitasUiState> = _uiState.asStateFlow()

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
                    _uiState.value = CitasUiState.ConCitas(
                        citas = citasOriginales,
                        filtro = FiltroCita.TODAS,
                        busqueda = ""
                    )
                }
                .onFailure {
                    _uiState.value = CitasUiState.Error("No se pudo cargar la lista de citas. Intente nuevamente.")
                }
        }
    }

    fun cancelarCita(id: Long, motivo: String) {
        viewModelScope.launch {
            cancelarCitaUseCase(id, motivo)
                .onSuccess {
                    cargarCitas()
                }
                .onFailure { error ->
                    _uiState.value = CitasUiState.Error(error.message ?: "Error al cancelar la cita")
                }
        }
    }

    // ✅ NUEVO: Método para eliminar citas
    fun eliminarCita(id: Long) {
        viewModelScope.launch {
            eliminarCitaUseCase(id)
                .onSuccess {
                    cargarCitas() // Recarga la lista
                }
                .onFailure { error ->
                    _uiState.value = CitasUiState.Error(error.message ?: "Error al eliminar la cita")
                }
        }
    }

    fun onFiltroChange(filtro: FiltroCita) {
        _uiState.update { currentState ->
            if (currentState is CitasUiState.ConCitas) currentState.copy(filtro = filtro) else currentState
        }
        aplicarFiltros()
    }

    fun onBusquedaChange(query: String) {
        _uiState.update { currentState ->
            if (currentState is CitasUiState.ConCitas) currentState.copy(busqueda = query) else currentState
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
            CitasUiState.SinCitas
        } else if (resultadoFinal.isEmpty()) {
            CitasUiState.SinCitas
        } else {
            CitasUiState.ConCitas(resultadoFinal, currentState.filtro, currentState.busqueda)
        }
    }
}