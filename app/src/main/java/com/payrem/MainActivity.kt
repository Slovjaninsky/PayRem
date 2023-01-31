package com.payrem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.payrem.backend.entities.Reminder
import com.payrem.ui.components.BottomNavigationBar
import com.payrem.ui.components.PlusButton
import com.payrem.ui.components.screens.navigation.ScreenNavigation
import com.payrem.ui.components.screens.navigation.ScreenNavigationItem
import com.payrem.ui.theme.PayRemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PayRemTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val edit = remember { mutableStateOf(Reminder()) }
    val navController = rememberNavController()
    Scaffold(
        topBar = { com.payrem.ui.components.TopAppBar(logout = true)},
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = { PlusButton(navController){ editNew ->
            edit.value = editNew
        } },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ScreenNavigation(
                    navController,
                    LocalContext.current,
                    ScreenNavigationItem.Personal.route,
                    edit.value
                ) { editNew ->
                    edit.value = editNew
                }

            }
        }
    )
}
