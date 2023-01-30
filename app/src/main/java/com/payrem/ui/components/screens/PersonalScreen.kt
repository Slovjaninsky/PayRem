package com.payrem.ui.components.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payrem.Preferences
import com.payrem.backend.entities.Reminder
import com.payrem.backend.service.BackendService
import com.payrem.ui.components.screens.navigation.ScreenNavigationItem
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Edit
import compose.icons.evaicons.fill.Trash

@Composable
fun PersonalScreen(
    context: Context,
    navController: NavController,
    editCallback: (edit: Reminder) -> Unit
) {
    val preferences = rememberSaveable { Preferences(context).read() }

    val scaleButtonWidth = 50
    val scaleButtonPadding = 8

    var flag = remember {mutableStateOf(true)}
    var data = rememberSaveable { mutableListOf<Reminder>() }
    data.clear()
    data.addAll(BackendService(preferences).getAllRemindersOfUser(preferences.userId))

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DisplayList(
                scaleButtonWidth, scaleButtonPadding, data, navController, editCallback
            ){ reminderId ->
                BackendService(preferences).deleteReminderFromUser(preferences.userId, reminderId)
                navController.navigate(ScreenNavigationItem.Group.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                navController.navigate(ScreenNavigationItem.Personal.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        }
    }
}

@Composable
private fun DisplayList(
    scaleButtonWidth: Int,
    scaleButtonPadding: Int,
    data: MutableList<Reminder>,
    navController: NavController,
    editCallback: (edit: Reminder) -> Unit,
    delete: (reminderId: Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            items(items = data, itemContent = { item ->
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color.LightGray)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.name,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
//                    Text(
//                        text = "${item.getAmount()} ${BackendService(preferences).getGroupById(groupId).getCurrency()}",
//                        fontSize = 14.sp,
//                        color = Color.hsl(358f, 0.63f, 0.49f),
//                        modifier = Modifier
//                            .padding(10.dp)
//                    )

                    Row {
                        IconButton(onClick = {
                            editCallback(item)
                            navController.navigate(ScreenNavigationItem.AddSpending.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }) {
                            Icon(
                                EvaIcons.Fill.Edit, "Edit"
                            )
                        }
                        IconButton(onClick = {
                            delete(item.id)
                        }) {
                            Icon(
                                EvaIcons.Fill.Trash, "Delete"
                            )
                        }
                    }
                }
            })
        }
    }
}
