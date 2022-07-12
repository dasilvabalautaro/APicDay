package com.globalhiddenodds.apicday.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PicDayApp()
        }
    }
}

@Composable
fun PicDayApp(viewModel: DownloadPicDayViewModel = hiltViewModel()) {
    APicDayTheme {
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
                modifier = Modifier.padding(it), viewModel
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: DownloadPicDayViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.name,
        modifier = modifier
    ) {
        composable(AppScreens.Splash.name) {
            SplashBody(viewModel = viewModel)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    APicDayTheme {
        //Greeting("Android")
    }
}