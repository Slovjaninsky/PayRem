package com.payrem.ui.components.screens.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.payrem.LoginActivity
import com.payrem.MainActivity
import com.payrem.SignInScreen
import com.payrem.SignUpMailScreen
import com.payrem.backend.entities.Reminder
import com.payrem.ui.components.screens.AddReminderScreen
import com.payrem.ui.components.screens.GroupScreen
import com.payrem.ui.components.screens.PersonalScreen

@Composable
fun ScreenNavigation(
    navController: NavHostController,
    context: Context,
    startDestination: String,
    edit: Reminder = Reminder(),
    editCallback: (edit: Reminder) -> Unit,
) {
    NavHost(navController, startDestination = startDestination) {
        composable(ScreenNavigationItem.Personal.route) {
            PersonalScreen(context, navController, editCallback)
        }
        composable(ScreenNavigationItem.Group.route) {
            GroupScreen(context, navController, editCallback)
        }
        composable(ScreenNavigationItem.AddSpending.route) {
            AddReminderScreen(context, edit)
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
