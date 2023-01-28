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
import com.payrem.ui.components.screens.AddReminderScreen
import com.payrem.ui.components.screens.GroupScreen
import com.payrem.ui.components.screens.PersonalScreen

@Composable
fun ScreenNavigation(
    navController: NavHostController,
    context: Context,
    startDestination: String
) {
    NavHost(navController, startDestination = startDestination) {
        composable(ScreenNavigationItem.Personal.route) {
            PersonalScreen(context)
        }
        composable(ScreenNavigationItem.Group.route) {
            GroupScreen(context)
        }
        composable(ScreenNavigationItem.AddSpending.route) {
            AddReminderScreen(context)
        }
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