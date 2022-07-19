package com.globalhiddenodds.apicday.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.globalhiddenodds.apicday.ui.components.AppTabRow
import com.globalhiddenodds.apicday.ui.configuration.AppScreens
import com.globalhiddenodds.apicday.ui.screens.PicDayBody
import com.globalhiddenodds.apicday.ui.screens.SplashBody
import com.globalhiddenodds.apicday.ui.theme.APicDayTheme
import com.globalhiddenodds.apicday.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            changeDate(Utils.formatDateNow())
            PicDayApp()
            //localTranslator.current
            //CompositionLocalProvider(localTranslator)
        }
    }

//    @Composable
//    private fun CompositionLocalProvider(localTranslator: ProvidableCompositionLocal<Translator>) {
//        localTranslator.current.translate()
//        PicDayApp()
//    }

    companion object {
        //        val localTranslator: ProvidableCompositionLocal<Translator> =
//            staticCompositionLocalOf<Translator> {
//                error("CompositionLocal translator not present")
//            }
        var dateSearch: String = ""
        fun changeDate(date: String) {
            dateSearch = date
        }
    }
}

@Composable
fun PicDayApp() {
    APicDayTheme(true) {
        val allScreens = AppScreens.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = AppScreens.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(topBar = {
            AppTabRow(
                allScreens = allScreens,
                onTabSelected = {
                    navController.navigate(it.name)
                },
                currentScreen = currentScreen
            )
        }) {
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.name,
        modifier = modifier
    ) {
        composable(AppScreens.Splash.name) {
            SplashBody()
        }
        composable(AppScreens.PicDay.name) {
            PicDayBody()
        }
        composable(AppScreens.Close.name) {
            android.os.Process.SIGNAL_KILL
            exitProcess(1)
        }
    }
}
