package pe.edu.upeu.andinasalud.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.andinasalud.presentation.citas.aUi
import pe.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase

class DetalleCitaViewModel(
    private val citaId: Long,
    private val obtenerCitasUseCase: ObtenerCitasUseCase,
    private val cancelarCitaUseCase: CancelarCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleCitaUiState>(DetalleCitaUiState.Cargando)
    val uiState: StateFlow<DetalleCitaUiState> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    private fun cargarDetalle() {
        viewModelScope.launch {
            _uiState.value = DetalleCitaUiState.Cargando

            obtenerCitasUseCase()
                .onSuccess { citas ->
                    val citaEncontrada = citas.find { it.id == citaId }
                    if (citaEncontrada != null) {
                        // Usamos la nueva extensión en lugar de aUi()
                        _uiState.value = DetalleCitaUiState.Detalle(citaEncontrada.aDetalleUi())
                    } else { _uiState.value = DetalleCitaUiState.Error("La cita solicitada no existe") }
                }
                .onFailure {
                    _uiState.value = DetalleCitaUiState.Error("No se pudo cargar el detalle de la cita")
                }
        }
    }
    fun cancelarCita(motivo: String) {
        viewModelScope.launch {
            // Usamos un estado de carga temporal o mantenemos el actual
            _uiState.update { currentState ->
                if (currentState is DetalleCitaUiState.Detalle) currentState else currentState
            }

            cancelarCitaUseCase(citaId, motivo)
                .onSuccess {
                    _uiState.value = DetalleCitaUiState.CanceladoConExito
                }
                .onFailure { e ->
                    _uiState.value = DetalleCitaUiState.Error(e.message ?: "Error al cancelar la cita")
                }
        }
    }
}