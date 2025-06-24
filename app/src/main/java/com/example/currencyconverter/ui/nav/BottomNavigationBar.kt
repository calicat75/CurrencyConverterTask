package com.example.currencyconverter.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .background(
                color = NavigationBarDefaults.containerColor
            )
    ) {
        NavigationBar(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Screen.entries.forEach { screen ->
                val isSelected = currentRoute == screen.route

                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            fontSize = 12.sp
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
