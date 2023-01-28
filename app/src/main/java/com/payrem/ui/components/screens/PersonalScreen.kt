package com.payrem.ui.components.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.payrem.Preferences
import com.payrem.backend.service.ReminderItem
import com.payrem.ui.components.ExpandableList

@Composable
fun PersonalScreen(
    context: Context
) {
    val preferences = rememberSaveable { Preferences(context).read() }

    val scaleButtonWidth = 50
    val scaleButtonPadding = 8

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // content
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val data = listOf<ReminderItem>() // TODO Redesign here
            DisplayList(
                scaleButtonWidth, scaleButtonPadding, data
            )
        }
    }
}

@Composable
private fun DisplayList(
    scaleButtonWidth: Int,
    scaleButtonPadding: Int,
    data: List<ReminderItem>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding((scaleButtonWidth + scaleButtonPadding).dp, 0.dp, 0.dp, 0.dp),
        contentAlignment = TopCenter
    ) {
    }
}
