package pe.edu.upeu.andinasalud.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pe.edu.upeu.andinasalud.data.repository.CitaRepositoryFake
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import pe.edu.upeu.andinasalud.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.andinasalud.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.andinasalud.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudViewModel
import pe.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase

val dataModule = module {
    single<CitaRepository> { CitaRepositoryFake() }
}

val domainModule = module {
    factory { ObtenerCitasUseCase(get()) }
    factory { SolicitarCitaUseCase(get()) }
    factory { CancelarCitaUseCase(get()) }
}

val presentationModule = module {
    // 1 parámetro
    viewModel { CitasViewModel(get()) }

    // 3 parámetros: Long (se pasa dinámicamente), ObtenerCitasUseCase, CancelarCitaUseCase
    viewModel { (citaId: Long) ->
        DetalleCitaViewModel(citaId, get(), get())
    }

    // 1 parámetro
    viewModel { SolicitudViewModel(get()) }
}

fun initKoin(configuracionAdicional: KoinApplication.() -> Unit = {}) {
    startKoin {
        configuracionAdicional()
        modules(
            dataModule,
            domainModule,
            presentationModule
        )
    }
}