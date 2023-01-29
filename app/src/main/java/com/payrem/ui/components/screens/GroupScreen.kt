package com.payrem.ui.components.screens

import android.os.StrictMode
import android.widget.Toast
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.payrem.backend.service.BackendService
import com.payrem.backend.api.jsonToApplicationUser
import com.payrem.backend.api.sendGet
import com.payrem.backend.api.sendPost
import com.payrem.Preferences
import com.payrem.PreferencesData
import com.payrem.backend.entities.Reminder
import com.payrem.backend.exceptions.ServerException
import com.payrem.ui.components.screens.navigation.ScreenNavigationItem
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Edit
import compose.icons.evaicons.fill.Trash

@Composable
fun GroupScreen(
    context: Context,
    navController: NavController,
    edit: MutableState<Reminder>
) {
    val preferences = rememberSaveable { Preferences(context).read() }

    // initialize tabs
    val tabItems = listOf("Reminders", "Members")
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) } // selected index of tab
    var groupId by rememberSaveable { mutableStateOf(-1L) }
    var userAddedSwitcher by rememberSaveable { mutableStateOf(false) }

    val data = remember { mutableListOf<Reminder>() }
    data.addAll(BackendService(preferences).getAllRemindersOfUser(preferences.userId))

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // top tabs
        DisplayTabs(tabItems, selectedTabIndex) { tabIndex ->
            selectedTabIndex = tabIndex
        }

        DisplayGroupSelection(context, preferences, groupId) { newGroupId ->
            groupId = newGroupId
        }

        // content
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (selectedTabIndex == 0) {
                DisplayList(
                    preferences, groupId, data, navController, edit
                )
            } else {
                DisplayMembers(preferences, groupId, userAddedSwitcher) {
                    userAddedSwitcher = !userAddedSwitcher
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DisplayGroupSelection(
    context: Context,
    preferences: PreferencesData,
    groupId: Long,
    onSwitch: (Long) -> Unit
) {
    var expandedDropdownGroups by remember { mutableStateOf(false) }
    val addGroupDialogController = remember { mutableStateOf(false) }
    Box (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expandedDropdownGroups,
            onExpandedChange = {
                expandedDropdownGroups = !expandedDropdownGroups
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
        ) {
            TextField(
                value = if(groupId == -1L) "" else BackendService(preferences).getGroupById(groupId).name,
                onValueChange = { },
                readOnly = true,
                label = {
                    Text(text = "Group")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedDropdownGroups
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 100.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedDropdownGroups,
                onDismissRequest = { expandedDropdownGroups = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val groupList = BackendService(preferences).getAllGroupOfUser(preferences.userId).filter { group -> group.id != preferences.groupId }
                groupList.forEach { group ->
                    DropdownMenuItem(
                        onClick = {
                            onSwitch(group.id)
                            expandedDropdownGroups = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(text = group.name)
                    }
                }
            }
        }
        Button(
            onClick = {
                addGroupDialogController.value = !addGroupDialogController.value
            },
            modifier = Modifier
                .padding(10.dp)
                .align(CenterEnd)
        ) {
            Text(text = "Create")
        }
    }
    if (addGroupDialogController.value) {
        Dialog(
            onDismissRequest = { addGroupDialogController.value = !addGroupDialogController.value },

        ) {
            Surface(
                elevation = 4.dp
            ) {
                AddGroupScreen(context, addGroupDialogController) { groupId ->
                    onSwitch(groupId)
                }
            }
        }
    }
}

@Composable
private fun DisplayInviteFields(
    preferences: PreferencesData,
    groupId: Long,
    onUserAdd: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var email by remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = email,
            onValueChange = { newEmail -> email = newEmail },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 100.dp),
            label = {
                Text(text = "Email")
            }
        )
        Button(
            onClick = {
                if(groupId == -1L){
                    ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            "Choose a group!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@Button
                }
                try{
                    BackendService(preferences).addUserToGroup(preferences.userId, groupId)
                    ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            "User in group",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    email = TextFieldValue(text = "")
                    onUserAdd()
                }
                catch(e: ServerException){
                    ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            "User not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@Button
                }
            },
            modifier = Modifier
                .padding(10.dp)
                .align(CenterEnd)
        ) {
            Text(text = "Invite")
        }
    }
}

@Composable
private fun DisplayTabs(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { onTabClick(tabIndex) },
                text = { Text(tab) },
                modifier = Modifier
                    .height(45.dp)
            )
        }
    }
}

@Composable
private fun DisplayMembers(
    preferences: PreferencesData,
    groupId: Long,
    userAdded: Boolean,
    onUserAdd: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState(),
                flingBehavior = null
            )
    ) {
        DisplayInviteFields(preferences, groupId, onUserAdd)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally),
            contentAlignment = Center
        ) {
            // list of group reminders here
        }
    }
}

@Composable
private fun DisplayList(
    preferences: PreferencesData,
    groupId: Long,
    data:  MutableList<Reminder>,
    navController: NavController,
    edit: MutableState<Reminder>
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Center
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
                            edit.value = item
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
                            data.remove(item)
                            BackendService(preferences).deleteReminderFromGroup(groupId, item.id)
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
