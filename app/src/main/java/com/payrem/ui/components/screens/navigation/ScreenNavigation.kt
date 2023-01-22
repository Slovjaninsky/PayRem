package com.payrem.ui.components.screens.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.payrem.LoginActivity
import com.payrem.MainActivity
import com.payrem.SignInScreen
import com.payrem.SignUpMailScreen
import com.payrem.*

@Composable
fun ScreenNavigation(
    navController: NavHostController,
    context: Context,
    startDestination: String
) {
    NavHost(navController, startDestination = startDestination) {
        composable(ScreenNavigationItem.SignIn.route) {
            SignInScreen(context, navController)
        }
        composable(ScreenNavigationItem.SignUpMail.route) {
            SignUpMailScreen(context, navController)
        }
        composable("mainActivity") {
            MainActivity()
        }
        composable("signInActivity") {
            LoginActivity()
        }
    }
}