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
import java.util.*
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            changeDate(Utils.formatDateNow())
            PicDayApp()
        }
    }

    companion object {
        var availableOptionPicDay = false
        var dateSearch: String = Utils.formatDateNow()
        fun changeDate(date: String) {
            dateSearch = date
        }
    }
}

@Composable
private fun hourOfDay(): Boolean {
    val dark: Boolean
    val date = Date()
    val calendar = Calendar.getInstance()
    calendar.time = date
    dark = when(calendar.get(Calendar.HOUR_OF_DAY)) {
        in 0..6 -> true
        in 7..17 -> false
        in 18..23 -> true
        else -> true
    }
    return dark
}

@Composable
fun PicDayApp() {
    APicDayTheme(hourOfDay()) {
        val allScreens = AppScreens.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = AppScreens.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(topBar = {
            AppTabRow(
                allScreens = allScreens,
                onTabSelected = {
                    if (MainActivity.availableOptionPicDay) {
                        navController.navigate(it.name)
                    }
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
        composable(AppScreens.Settings.name) {

        }
    }
}
