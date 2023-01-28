package com.payrem.ui.components.screens

import android.content.Context
import android.graphics.Typeface
import android.os.StrictMode
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
import com.payrem.backend.api.jsonToExpenseGroup
import com.payrem.backend.api.sendPost

val currencyRegex = """[A-Z]{0,3}""".toRegex()

@Composable
fun AddGroupScreen(
    context: Context,
    dialogController: MutableState<Boolean>,
    onCreate: (Long) -> Unit
) {
    val preferences = rememberSaveable { Preferences(context).read() }

    var groupName by remember { mutableStateOf(TextFieldValue()) }
    var groupCurrency by remember { mutableStateOf(TextFieldValue()) }
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
            value = groupCurrency,
            onValueChange = { newGroupCurrency ->
                if (currencyRegex.matches(newGroupCurrency.text)) {
                    groupCurrency = newGroupCurrency
                }
            },
            label = {
                Text(text = "Currency")
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
                    if (groupCurrency.text.length != 3 || groupName.text.isBlank()) {
                        ContextCompat.getMainExecutor(context).execute {
                            Toast.makeText(
                                context,
                                "Fill in all fields!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@Button
                    }
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val group = jsonToExpenseGroup(sendPost(
                        "http://${preferences.serverIp}/users/${preferences.userId}/groups",
                        "{\"currency\": \"${groupCurrency.text}\", \"name\": \"${groupName.text}\"}"
                    ))
                    ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            "Group was created!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dialogController.value = !dialogController.value
                    onCreate(group.getId())
                },
                modifier = Modifier
                    .padding(1.dp)
            ) {
                Text("Save")
            }
        }
    }
}