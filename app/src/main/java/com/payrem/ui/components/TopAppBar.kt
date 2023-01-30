package com.payrem.ui.components

import android.content.Intent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.payrem.*
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.LogOut

@Composable
fun TopAppBar(
    logout: Boolean
) {
    val context = LocalContext.current
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        actions = {
            if (logout) {
                IconButton(onClick = {
                    Preferences(context).write(PreferencesData("", -1))
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }) {
                    Icon(EvaIcons.Fill.LogOut, "Logout")
                }
            }
        }
    )
}
