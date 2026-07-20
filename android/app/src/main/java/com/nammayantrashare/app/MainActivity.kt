package com.nammayantrashare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Entry point for the Compose-based UI
        setContent {
            NammaYantraTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun NammaYantraTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = Color(0xFF1B4332),
        secondary = Color(0xFFF59E0B),
        tertiary = Color(0xFF2D6A4F),
        background = Color(0xFF081C15),
        surface = Color(0xFF1B4332).copy(alpha = 0.1f),
        onPrimary = Color.White,
        onSecondary = Color.Black
    )
    MaterialTheme(colorScheme = colorScheme, content = content)
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    // Retrieve the current user session from the Repository
    val session by Repository.userSession
    
    val startDestination = if (session.role == Role.NONE) "role_selection" 
                          else if (session.role == Role.FARMER) "farmer_browse"
                          else "owner_dashboard"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("role_selection") { RoleSelectionScreen(navController) }
        composable("farmer_browse") { FarmerBrowseScreen(navController) }
        composable("booking/{machineId}") { backStackEntry -> 
            BookingScreen(navController, backStackEntry.arguments?.getString("machineId")) 
        }
        composable("my_bookings") { MyBookingsScreen(navController) }
        composable("price_calculator") { PriceCalculatorScreen(navController) }
        composable("owner_dashboard") { OwnerDashboardScreen(navController) }
        composable("owner_machines") { OwnerMachinesScreen(navController) }
        composable("owner_requests") { OwnerRequestsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}
