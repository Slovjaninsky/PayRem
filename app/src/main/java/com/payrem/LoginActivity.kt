package com.payrem


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payrem.backend.entities.ApplicationUser
import com.payrem.backend.service.BackendService
import com.payrem.ui.components.screens.navigation.ScreenNavigation
import com.payrem.ui.components.screens.navigation.ScreenNavigationItem
import com.payrem.ui.theme.PayRemTheme


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PayRemTheme {
                val preferences = Preferences(LocalContext.current).read()
                if (!preferences.exists()) {
                MainSignInScreen()
                } else {
                    val context = LocalContext.current
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            }
        }
    }
}

@Composable
fun MainSignInScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            com.payrem.ui.components.TopAppBar(false)
        },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                ScreenNavigation(navController, LocalContext.current, ScreenNavigationItem.SignIn.route){}
            }
        }
    )
}

@Composable
fun SignInScreen(context: Context, navController: NavController) {
    val preferences = rememberSaveable { Preferences(context).read() }

    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 35.sp
                    )

                    CustomInput("Login", login)
                    CustomPasswordInput("Password", password)
                    PrimaryButton("SIGN IN", 20.dp, onClick = {
                        if (login.value.isEmpty() || password.value.isEmpty()) {
                            ContextCompat.getMainExecutor(context).execute {
                                Toast.makeText(
                                    context,
                                    "No empty fields allowed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        try {
                            val userFromBack = BackendService(preferences).login(login.value, password.value)

                            val preferencesData = PreferencesData("",  userFromBack.id)

                            Preferences(context).write(preferencesData)

                            context.startActivity(Intent(context, MainActivity::class.java))
                        } catch (error: Exception) {
                            println("Caught a FailedLoginException! You should see the error message on the screen")
                            showError.value = true
                            errorMessage.value = error.message.toString()
                        }

                    })
                    SecondaryButton("SIGN UP", 5.dp, onClick = {
                        navController.navigate(ScreenNavigationItem.SignUpMail.route) {
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
                    })

                    if (showError.value) ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            errorMessage.value,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    showError.value = false
                }

            }
        }
    )
}

@Composable
fun SignUpMailScreen(context: Context, navController: NavController) {
    val preferences = rememberSaveable { Preferences(context).read() }

    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val repeatPassword = remember { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }
    Scaffold(
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 35.sp
                    )

                    CustomInput("E-mail", email)
                    CustomInput("Username", username)
                    CustomPasswordInput("Password", password)
                    CustomPasswordInput("Repeat password", repeatPassword)
                    PrimaryButton("SIGN UP", 20.dp, onClick = {
                        if (
                            email.value.isEmpty()
                            || password.value.isEmpty()
                            || username.value.isEmpty()
                            || repeatPassword.value.isEmpty()
                        ) {
                            ContextCompat.getMainExecutor(context).execute {
                                Toast.makeText(
                                    context,
                                    "No empty fields allowed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@PrimaryButton
                        }
                        try {
                            if (password.value != repeatPassword.value) {
                                throw Exception("Passwords do not match")
                            }
                            val user = ApplicationUser(password.value, username.value, "", email.value)

                            val userFromBack = BackendService(preferences).createUser(user)

                            val preferencesData = PreferencesData("",  userFromBack.id)

                            Preferences(context).write(preferencesData)

                            context.startActivity(Intent(context, MainActivity::class.java))
                        } catch (error: Exception) {
                            println("Caught a ServerException!")
                            showError.value = true
                            errorMessage.value = error.message.toString()
                            ContextCompat.getMainExecutor(context).execute {
                                Toast.makeText(
                                    context,
                                    error.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })
                    SecondaryButton("SIGN IN", 5.dp, onClick = {
                        navController.navigate(ScreenNavigationItem.SignIn.route) {
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
                    })
//                    if (showError.value)
//                        ContextCompat.getMainExecutor(context).execute {
//                            Toast.makeText(
//                                context,
//                                errorMessage.value,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    else
//                        ContextCompat.getMainExecutor(context).execute {
//                            Toast.makeText(
//                                context,
//                                pass.value,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    showError.value = false
                }

            }
        }
    )
}

@Composable
fun CustomInput(label: String, textState: MutableState<String>) {
    TextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text(text = label) },
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CustomPasswordInput(label: String, textState: MutableState<String>) {
    val passwordVisible = remember { mutableStateOf(false) }
    TextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text(text = label) },
        modifier = Modifier.padding(8.dp),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible.value)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
fun PrimaryButton(label: String, paddingTop: Dp, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = paddingTop)
            .width(180.dp)
            .height(50.dp),
        shape = RoundedCornerShape(40.dp)
    ) {
        Text(
            text = label,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SecondaryButton(label: String, paddingTop: Dp, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .padding(top = paddingTop)
    ) {
        Text(
            text = label,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ReferenceButton(label: String, paddingTop: Dp, image: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = paddingTop)
            .width(250.dp)
            .height(45.dp),
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Box {
            Image(
                painterResource(id = image),
                contentDescription = "icon",
                modifier = Modifier
                    .size(25.dp)
            )
            Text(
                text = label,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp
            )
        }

    }
}