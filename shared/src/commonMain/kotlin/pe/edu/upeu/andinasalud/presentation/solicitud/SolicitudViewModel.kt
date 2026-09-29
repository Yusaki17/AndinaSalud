package pe.edu.upeu.andinasalud.presentation.solicitud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.usecase.SolicitarCitaUseCase
import pe.upeu.andinasalud.domain.usecase.CitaInvalidaException

class SolicitudViewModel(
    private val solicitarCitaUseCase: SolicitarCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolicitudUiState())
    val uiState: StateFlow<SolicitudUiState> = _uiState.asStateFlow()

    // Listas desde constantes centralizadas (ya no hardcodeadas)
    val especialidades = SolicitudConstantes.ESPECIALIDADES
    val sedes = SolicitudConstantes.SEDES

    fun onEspecialidadChange(value: String) {
        _uiState.update { it.copy(especialidad = value, errorEspecialidad = null, errorGlobal = null) }
    }

    fun onSedeChange(value: String) {
        _uiState.update { it.copy(sede = value, errorSede = null, errorGlobal = null) }
    }

    fun onFechaChange(value: String) {
        _uiState.update { it.copy(fecha = value, errorFecha = null, errorGlobal = null) }
    }

    fun onHoraChange(value: String) {
        _uiState.update { it.copy(hora = value, errorHora = null, errorGlobal = null) }
    }

    fun onMotivoChange(value: String) {
        _uiState.update { it.copy(motivo = value, errorMotivo = null, errorGlobal = null) }
    }

    fun solicitarCita() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGlobal = null) }

            val state = _uiState.value
            val resultado = solicitarCitaUseCase(
                especialidad = state.especialidad,
                sede = state.sede,
                fecha = state.fecha,
                hora = state.hora,
                motivo = state.motivo
            )

            resultado.onSuccess {
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true)
                }
            }.onFailure { e ->
                if (e is CitaInvalidaException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorEspecialidad = e.errores.especialidad,
                            errorSede = e.errores.sede,
                            errorFecha = e.errores.fecha,
                            errorHora = e.errores.hora,
                            errorMotivo = e.errores.motivo,
                            errorGlobal = e.errores.global
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorGlobal = e.message ?: "Error desconocido")
                    }
                }
            }
        }
    }

    fun resetForm() {
        _uiState.value = SolicitudUiState()
    }
}