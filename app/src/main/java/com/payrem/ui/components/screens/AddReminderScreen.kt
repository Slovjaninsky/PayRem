package com.payrem.ui.components.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.widget.Toast
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
import androidx.core.content.ContextCompat
import com.payrem.Preferences
import com.payrem.backend.entities.Group
import com.payrem.backend.entities.Reminder
import com.payrem.backend.service.BackendService
import com.payrem.ui.components.DatePickerField
import com.payrem.ui.components.TimePicker

val numberRegex = Regex("^\\d*\$")
val periodsMap = mapOf("Day" to 1, "Month" to -1, "Year" to -12)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddReminderScreen(
    context: Context,
    edit: Reminder
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
    var groupId by remember { mutableStateOf(-1L) }
    var groupIdLast by remember { mutableStateOf(-1L) }
    var groupList by remember { mutableStateOf(listOf<Group>()) }


    if (edit.id != -1L && reminderDate.value == "") {
        reminderTitle = TextFieldValue(edit.name)
        reminderDescription = TextFieldValue(edit.description)
        reminderDate.value = edit.date
        reminderTime.value = edit.time
        reminderRecurrenceNumber = if (edit.period >= 0)
                TextFieldValue(edit.period.toString())
            else
                TextFieldValue((-edit.period).toString())
        reminderRecurrencePeriod = if (edit.period >= 0)
                TextFieldValue("Day")
            else
                TextFieldValue("Month")
        isGroupReminder = (edit.groupId != -1L)
        if (isGroupReminder) {
            groupId = edit.groupId
            groupName = BackendService(preferences).getGroupById(edit.groupId).name
        } else {
            groupId = -1L
            groupName = ""
        }

    }

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
                        // Clear everything
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
                        // Edit existing reminder
                        if (edit.id != -1L) {
                            // For group
                            if (isGroupReminder) {
                                // If something is not filled in
                                if (
                                    reminderTitle.text == "" ||
                                    reminderDescription.text == "" ||
                                    reminderDate.value == "" ||
                                    reminderTime.value == "" ||
                                    reminderRecurrenceNumber.text == "" ||
                                    reminderRecurrencePeriod.text == "" ||
                                    groupId == 0L
                                ) {
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Please, fill in all the field",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                // Everything is OK
                                else {
                                    edit.name = reminderTitle.text
                                    edit.description = reminderDescription.text
                                    edit.date = reminderDate.value
                                    edit.time = reminderTime.value
                                    edit.period = reminderRecurrenceNumber.text.toLong() *
                                            periodsMap[reminderRecurrencePeriod.text]!!
                                    edit.groupId = groupId
                                    BackendService(preferences).editReminder(edit)
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Reminder saved successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            // For personal
                            else {
                                // If something is not filled in
                                if (
                                    reminderTitle.text == "" ||
                                    reminderDescription.text == "" ||
                                    reminderDate.value == "" ||
                                    reminderTime.value == "" ||
                                    reminderRecurrenceNumber.text == "" ||
                                    reminderRecurrencePeriod.text == ""
                                ) {
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Please, fill in all the field",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                // Everything is OK
                                else {
                                    edit.name = reminderTitle.text
                                    edit.description = reminderDescription.text
                                    edit.date = reminderDate.value
                                    edit.time = reminderTime.value
                                    edit.period = reminderRecurrenceNumber.text.toLong() *
                                            periodsMap[reminderRecurrencePeriod.text]!!
                                    BackendService(preferences).editReminder(edit)
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Reminder saved successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                        // Create new reminder
                        else {
                            // For group
                            if (isGroupReminder) {
                                // If something is not filled in
                                if (
                                    reminderTitle.text == "" ||
                                    reminderDescription.text == "" ||
                                    reminderDate.value == "" ||
                                    reminderTime.value == "" ||
                                    reminderRecurrenceNumber.text == "" ||
                                    reminderRecurrencePeriod.text == "" ||
                                    groupId == 0L
                                ) {
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Please, fill in all the field",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                // Everything is OK
                                else {
                                    edit.name = reminderTitle.text
                                    edit.description = reminderDescription.text
                                    edit.date = reminderDate.value
                                    edit.time = reminderTime.value
                                    edit.period = reminderRecurrenceNumber.text.toLong() *
                                            periodsMap[reminderRecurrencePeriod.text]!!
                                    edit.groupId = groupId
                                    BackendService(preferences).addReminderToGroup(groupId, edit)
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Reminder saved successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            // For personal
                            else {
                                // If something is not filled in
                                if (
                                    reminderTitle.text == "" ||
                                    reminderDescription.text == "" ||
                                    reminderDate.value == "" ||
                                    reminderTime.value == "" ||
                                    reminderRecurrenceNumber.text == "" ||
                                    reminderRecurrencePeriod.text == ""
                                ) {
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Please, fill in all the field",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                // Everything is OK
                                else {
                                    edit.name = reminderTitle.text
                                    edit.description = reminderDescription.text
                                    edit.date = reminderDate.value
                                    edit.time = reminderTime.value
                                    edit.period = reminderRecurrenceNumber.text.toLong() *
                                            periodsMap[reminderRecurrencePeriod.text]!!
                                    BackendService(preferences).addReminderToUser(preferences.userId, edit)
                                    ContextCompat.getMainExecutor(context).execute {
                                        Toast.makeText(
                                            context,
                                            "Reminder saved successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
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
                    groupList = BackendService(preferences)
                        .getAllGroupOfUser(preferences.userId)
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
                            .getAllGroupOfUser(preferences.userId)
                        if (isGroupReminder) {
                            groupId = groupIdLast
                        } else {
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
                                    groupName = group.name
                                    groupId = group.id
                                    expandedDropdownGroups = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(text = group.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

