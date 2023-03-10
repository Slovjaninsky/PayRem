package com.payrem

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.payrem.ui.theme.PayRemTheme
import java.net.HttpURLConnection
import java.net.URL

class LoadingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PayRemTheme {
                LoadingScreen()
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    val context = LocalContext.current
    val preferences = Preferences(LocalContext.current).read()
    val isLoading = remember { mutableStateOf(true) }

    val connected = remember { mutableStateOf(0) }

    Scaffold(
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Text(text = "PayRem", fontSize = 30.sp, modifier = Modifier.padding(90.dp))
                if (isLoading.value) {
                    CircularProgressIndicator()
                }
            }
        }
    )

    tryConnect(preferences) {
        connected.value = it
    }

    if (connected.value == 1) {
        if (!preferences.exists()) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        } else {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
    else if (connected.value == -1) {
        ContextCompat.getMainExecutor(context).execute {
            Toast.makeText(
                context,
                "Can't establish connection",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

fun tryConnect(
    preferences: PreferencesData,
    onConnect: (Int) -> Unit
) {
    val mainLooper = Looper.getMainLooper()

    Thread {
        try {
            val imageUrl = URL("http://${preferences.serverIp}/connect/test")

            val httpConnection = imageUrl.openConnection() as HttpURLConnection
            httpConnection.doInput = true
            httpConnection.connect()

            Handler(mainLooper).post {
                onConnect(1)
            }
        } catch (e: Exception) {
            onConnect(-1)
        }
    }.start()
}
