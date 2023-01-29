package com.payrem.ui.components.screens

import android.content.Context
import android.graphics.Typeface
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.payrem.Preferences
import com.payrem.backend.entities.Group
import com.payrem.backend.service.BackendService

@Composable
fun AddGroupScreen(
    context: Context,
    dialogController: MutableState<Boolean>,
    onCreate: (Long) -> Unit
) {
    val preferences = rememberSaveable { Preferences(context).read() }

    var groupName by remember { mutableStateOf(TextFieldValue()) }
    var groupDescription by remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = "Add group",
            modifier = Modifier
                .padding(10.dp),
            fontFamily = FontFamily(Typeface.DEFAULT),
            fontSize = 32.sp,
        )
        TextField(
            value = groupName,
            onValueChange = { newGroupName -> groupName = newGroupName },
            placeholder = { Text(text = "Name") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        TextField(
            value = groupDescription,
            onValueChange = { newGroupDesc -> groupDescription = newGroupDesc},
            label = {
                Text(text = "Description")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    dialogController.value = !dialogController.value
                },
                modifier = Modifier
                    .padding(1.dp)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    if (groupDescription.text.isBlank() || groupName.text.isBlank()) {
                        ContextCompat.getMainExecutor(context).execute {
                            Toast.makeText(
                                context,
                                "Fill in all fields!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@Button
                    }
                    BackendService(preferences).createGroup(Group(-1, groupName.text, groupDescription.text))
                    ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            "Group was created!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dialogController.value = !dialogController.value
                },
                modifier = Modifier
                    .padding(1.dp)
            ) {
                Text("Save")
            }
        }
    }
}
