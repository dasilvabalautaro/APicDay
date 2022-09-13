package com.globalhiddenodds.apicday.overview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Output
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.navigation.NavigationDirections
import com.globalhiddenodds.apicday.ui.screens.BestImageScreen
import com.globalhiddenodds.apicday.ui.screens.ImageScreen
import com.globalhiddenodds.apicday.ui.screens.ImageZoom
import com.globalhiddenodds.apicday.ui.theme.APicDayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APicDayTheme {
                val navController = rememberNavController()
                val backstackEntry = navController.currentBackStackEntryAsState()

                val showBackButton = when (backstackEntry.value?.destination?.route) {
                    NavigationDirections.main.destination -> false
                    else -> true
                }
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(id = R.string.app_name))
                        },
                        navigationIcon = {
                            if (showBackButton) {
                                IconButton(onClick = {
                                    navController.navigate(NavigationDirections.main.destination) {
                                        popUpTo(NavigationDirections.main.destination)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(NavigationDirections.best.destination)
                            }) {
                                Icon(Icons.Filled.List, contentDescription = "Best")
                            }
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                            IconButton(onClick = {
                                android.os.Process.SIGNAL_KILL
                                exitProcess(1)
                            }) {
                                Icon(Icons.Filled.Output, contentDescription = "Close")
                            }
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                }) {
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationDirections.main.destination
) {
    val persistenceViewModel: PersistenceViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavigationDirections.main.destination) {
            ImageScreen(navController, persistenceViewModel)
        }
        composable(
            NavigationDirections.routeZoom,
            NavigationDirections.zoom().arguments
        ) {
            val arg = it.arguments?.getString(NavigationDirections.KEY_URL)
            ImageZoom(url = arg)
        }
        composable(NavigationDirections.best.destination) {
            BestImageScreen(persistenceViewModel.listLikes)
        }
    }
}
