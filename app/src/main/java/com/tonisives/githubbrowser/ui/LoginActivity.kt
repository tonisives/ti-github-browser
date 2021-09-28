package com.tonisives.githubbrowser.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.tonisives.githubbrowser.App.Companion.context
import com.tonisives.githubbrowser.repository.Status
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

@Composable
fun LoginView(viewModel: LoginViewModel) = AppTheme {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val user by viewModel.user.observeAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
    ) {
        when (user?.status) {
            Status.LOADING -> {
                CircularProgressIndicator()
            }
            Status.SUCCESS -> {
                if (user?.data == null) {
                    LoginFields(
                        email,
                        password,
                        onLoginClick = { viewModel.login("ttiganik@gmail.com", password) },
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it }
                    )
                } else {
                    Text("Login successful", fontSize = MaterialTheme.typography.h3.fontSize)
                    Text(
                        "User ${user?.data?.login}",
                        fontSize = MaterialTheme.typography.h4.fontSize
                    )
                }
            }
            Status.ERROR -> {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                LoginFields(
                    email,
                    password,
                    onLoginClick = { viewModel.login("ttiganik@gmail.com", password) },
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it }
                )

                coroutineScope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            "Login error: ${user?.message}",
                            actionLabel = "Dismiss",
                            duration = SnackbarDuration.Indefinite
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> viewModel.goToInitialState()
                    }
                }

                SnackbarHost(hostState = snackbarHostState)
            }
        }
    }
}

@Composable
fun LoginFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Please login")

        OutlinedTextField(
            value = email,
            placeholder = { Text(text = "user@email.com") },
            label = { Text(text = "email") },
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        OutlinedTextField(
            value = password,
            placeholder = { Text(text = "password") },
            label = { Text(text = "password") },
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Button(onClick = {
            if (email.isBlank() == false && password.isBlank() == false) {
                onLoginClick(email)
                focusManager.clearFocus()
            } else {
                Toast.makeText(
                    context,
                    "Please enter an email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text("Login")
        }
    }
}

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { LoginView(getViewModel()) }

        // animate the view up if text field selected
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val rootView = findViewById<View>(android.R.id.content).rootView
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            rootView.setPadding(0, 0, 0, imeHeight)
            insets
        }
    }
}