package com.payrem.ui.components.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payrem.Preferences
import com.payrem.backend.entities.Reminder
import com.payrem.backend.service.BackendService
import com.payrem.backend.service.ReminderItem
import com.payrem.ui.components.ExpandableList
import com.payrem.ui.components.screens.navigation.ScreenNavigationItem
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Edit
import compose.icons.evaicons.fill.PieChart
import compose.icons.evaicons.fill.Trash

@Composable
fun PersonalScreen(
    context: Context,
    navController: NavController
) {
    val preferences = rememberSaveable { Preferences(context).read() }

    val scaleButtonWidth = 50
    val scaleButtonPadding = 8

    // TODO: create a function to get personal from backend
    val data = remember { mutableStateOf(BackendService(preferences).getGroupList(groupId)) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DisplayList(
                scaleButtonWidth, scaleButtonPadding, data, navController
            )
        }
    }
}

@Composable
private fun DisplayList(
    scaleButtonWidth: Int,
    scaleButtonPadding: Int,
    data: MutableState<List<Reminder>>,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding((scaleButtonWidth + scaleButtonPadding).dp, 0.dp, 0.dp, 0.dp),
        contentAlignment = TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            items(items = data.value, itemContent = { item ->
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color.LightGray)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.getTitle(),
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
                            // TODO: navigate to needed screens
                        }) {
                            Icon(
                                EvaIcons.Fill.Edit, "Edit"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
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
