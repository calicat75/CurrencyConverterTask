package com.example.currencyconverter.ui.nav

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.currencyconverter.ui.screens.CurrenciesScreen
import com.example.currencyconverter.ui.screens.TransactionsScreen
import com.example.currencyconverter.ui.viewmodel.CurrenciesViewModel
import com.example.currencyconverter.ui.viewmodel.TransactionsViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val currenciesViewModel: CurrenciesViewModel = hiltViewModel()
    val transactionsViewModel: TransactionsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Currencies.route,
        modifier = modifier
    ) {
        composable(
            Screen.Currencies.route,
            enterTransition = {
                slideInHorizontally { -1000 }
            },
            exitTransition = {
                slideOutHorizontally { -1000 }
            }
        ) {
            CurrenciesScreen(currenciesViewModel = currenciesViewModel, navController = navController)
        }

        composable(
            Screen.Transactions.route,
            enterTransition = {
                slideInHorizontally { -1000 }
            },
            exitTransition = {
                slideOutHorizontally { -1000 }
            }
        ) {
            TransactionsScreen(transactionsViewModel = transactionsViewModel, navController = navController)
        }
    }
}
