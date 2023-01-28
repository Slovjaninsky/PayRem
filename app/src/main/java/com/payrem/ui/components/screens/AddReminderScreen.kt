package com.payrem.ui.components.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payrem.Preferences
import com.payrem.backend.entities.ReminderGroup
import com.payrem.backend.service.BackendService
import com.payrem.ui.components.DatePickerField
import com.payrem.ui.components.TimePicker

val numberRegex = Regex("^\\d*\$")

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddReminderScreen(
    context: Context
) {
    val preferences = rememberSaveable { Preferences(context).read() }
    var reminderTitle by remember { mutableStateOf(TextFieldValue()) }
    var reminderDescription by remember { mutableStateOf(TextFieldValue()) }
    val reminderDate = remember { mutableStateOf("") }
    val reminderTime = remember { mutableStateOf("") }
    var reminderRecurrenceNumber by remember { mutableStateOf(TextFieldValue()) }
    var reminderRecurrencePeriod by remember { mutableStateOf(TextFieldValue()) }
    var isGroupReminder by remember { mutableStateOf(false) }
    var expandedDropDownRecurrence by remember { mutableStateOf(false) }
    var expandedDropdownGroups by remember { mutableStateOf(false) }
    var groupName by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf(0L) }
    var groupIdLast by remember { mutableStateOf(0L) }
    var groupList by remember { mutableStateOf(listOf<ReminderGroup>()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Add reminder",
                modifier = Modifier
                    .padding(10.dp),
                fontFamily = FontFamily(Typeface.DEFAULT),
                fontSize = 32.sp,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Button(
                    onClick = {
                        reminderTitle = TextFieldValue("")
                        reminderDescription = TextFieldValue("")
                        reminderDate.value = ""
                        reminderTime.value = ""
                        reminderRecurrenceNumber = TextFieldValue("")
                        reminderRecurrencePeriod = TextFieldValue("")
                        isGroupReminder = false
                        groupName = ""
                        groupId = 0L
                    },
                    modifier = Modifier
                        .padding(1.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if(groupId == 0L) {
                            BackendService(preferences).addPersonalReminder(
                                reminderTitle.text,
                                reminderDescription.text,
                                reminderRecurrenceNumber.text.toInt(),
                                reminderRecurrencePeriod.text,
                                reminderDate.value,
                                reminderTime.value
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(1.dp)
                ) {
                    Text("Save")
                }
            }
        }
        TextField(
            value = reminderTitle,
            onValueChange = { newSpendingName -> reminderTitle = newSpendingName },
            placeholder = { Text(text = "Title") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        TextField(
            value = reminderDescription,
            onValueChange = { reminderDescription = it },
            label = {
                Text(text = "Category")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            DatePickerField(
                reminderDate,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            TimePicker(
                reminderTime,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .align(Top)
                    .width(120.dp),
                value = reminderRecurrenceNumber,
                onValueChange = {
                    if (numberRegex.matches(it.text)) {
                        reminderRecurrenceNumber = it
                    }
                },
                label = {
                    Text(text = "Frequency")
                }
            )
            ExposedDropdownMenuBox(
                expanded = expandedDropDownRecurrence,
                onExpandedChange = {
                    expandedDropDownRecurrence = !expandedDropDownRecurrence
                },
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Bottom)
            ) {
                TextField(
                    value = reminderRecurrencePeriod,
                    onValueChange = { },
                    readOnly = true,
                    label = {
                        Text(text = "Period")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedDropDownRecurrence
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedDropDownRecurrence,
                    onDismissRequest = { expandedDropDownRecurrence = false },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    listOf("Day", "Month", "Year").forEach { period ->
                        DropdownMenuItem(
                            onClick = {
                                reminderRecurrencePeriod = TextFieldValue(period)
                                expandedDropDownRecurrence = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = period)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Checkbox(
                checked = isGroupReminder,
                onCheckedChange = {
                    isGroupReminder = it
                    if (isGroupReminder) {
                        groupId = groupIdLast
                    }
                    else {
                        groupIdLast = groupId
                        groupId = 0L
                    }
                    groupList = BackendService(preferences).getGroups()
                        .filter { group -> group.getId() != preferences.groupId }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "Group reminder",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .clickable {
                        isGroupReminder = !isGroupReminder
                        groupList = BackendService(preferences)
                            .getGroups()
                            .filter { group -> group.getId() != preferences.groupId }
                        if (isGroupReminder) {
                            groupId = groupIdLast
                        }
                        else {
                            groupIdLast = groupId
                            groupId = 0L
                        }
                    }
            )
        }
        AnimatedVisibility(
            visible = isGroupReminder,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedDropdownGroups,
                    onExpandedChange = {
                        expandedDropdownGroups = !expandedDropdownGroups
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = groupName,
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
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdownGroups,
                        onDismissRequest = { expandedDropdownGroups = false },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        groupList.forEach { group ->
                            DropdownMenuItem(
                                onClick = {
                                    groupName = group.getName()
                                    groupId = group.getId()
                                    expandedDropdownGroups = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(text = group.getName())
                            }
                        }
                    }
                }
            }
        }
    }
}
