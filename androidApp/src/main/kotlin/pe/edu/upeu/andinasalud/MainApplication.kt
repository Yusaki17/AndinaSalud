package pe.edu.upeu.andinasalud

import android.app.Application
import pe.edu.upeu.andinasalud.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializamos Koin antes de que se dibuje cualquier Composable
        initKoin()
    }
}