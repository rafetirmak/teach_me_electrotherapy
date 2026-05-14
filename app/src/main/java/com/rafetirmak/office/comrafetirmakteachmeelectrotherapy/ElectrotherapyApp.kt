package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens.*
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens.AboutAcademicScreen
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.AboutDeveloper

import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens.RussianScreen

@Composable
fun ElectrotherapyApp() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onNavigateToMain = {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("main") {
            MainScreen(
                onNavigateToCurrent = { currentId ->
                    navController.navigate(currentId)
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToAbout = {
                    navController.navigate("about")
                }
            )
        }
        composable("about") {
            AboutAcademicScreen(
                profile = AboutDeveloper.getProfile(),
                onBack = { navController.popBackStack() }
            )
        }
        composable("tens") {
            TensScreen(onBack = { navController.popBackStack() })
        }
        composable("ifc") {
            IfcScreen(onBack = { navController.popBackStack() })
        }
        composable("faradic") {
            FaradicScreen(onBack = { navController.popBackStack() })
        }
        
        composable("russian") {
            RussianScreen(onBack = { navController.popBackStack() })
        }
        
        composable("galvanic") { 
            GalvanicScreen(onBack = { navController.popBackStack() }) 
        }
        
        composable("diadinamic") { 
            DiadinamicScreen(onBack = { navController.popBackStack() }) 
        }
        
        composable("high_voltage") { 
            HighVoltageScreen(onBack = { navController.popBackStack() }) 
        }

        composable("signal_generator") {
            SignalGeneratorScreen(onBack = { navController.popBackStack() })
        }

        composable("skin_filter") {
            SkinFilterScreen(onBack = { navController.popBackStack() })
        }

        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
