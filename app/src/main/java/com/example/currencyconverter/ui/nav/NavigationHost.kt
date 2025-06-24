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
import com.example.currencyconverter.ui.screens.ExchangeScreen
import com.example.currencyconverter.ui.screens.TransactionsScreen
import com.example.currencyconverter.ui.viewmodel.CurrenciesViewModel
import com.example.currencyconverter.ui.viewmodel.ExchangeViewModel
import com.example.currencyconverter.ui.viewmodel.TransactionsViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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
            val currenciesViewModel: CurrenciesViewModel = hiltViewModel()
            CurrenciesScreen(viewModel = currenciesViewModel, navController = navController)
        }

        composable(
            route = "exchange/{from}/{to}/{amount}",
            enterTransition = {
                slideInHorizontally { 1000 }
            },
            exitTransition = {
                slideOutHorizontally { 1000 }
            }
        ) { backStackEntry ->
            val from = backStackEntry.arguments?.getString("from") ?: ""
            val to = backStackEntry.arguments?.getString("to") ?: ""
            val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
            val exchangeViewModel: ExchangeViewModel = hiltViewModel()

            ExchangeScreen(
                viewModel = exchangeViewModel,
                navController = navController,
                from = from,
                to = to,
                amount = amount
            )
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
            val transactionsViewModel: TransactionsViewModel = hiltViewModel()
            TransactionsScreen(viewModel = transactionsViewModel, navController = navController)
        }
    }
}
