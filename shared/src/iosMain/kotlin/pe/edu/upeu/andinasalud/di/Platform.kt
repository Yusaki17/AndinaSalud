package pe.edu.upeu.andinasalud.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    // Aquí irían dependencias exclusivas de iOS si las necesitaras
}

// Función auxiliar para que Swift pueda iniciar Koin
fun initKoinIos() = initKoin { }