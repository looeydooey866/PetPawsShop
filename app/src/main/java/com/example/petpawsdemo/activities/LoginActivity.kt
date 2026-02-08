package com.example.petpawsdemo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import com.example.petpawsdemo.model.GUEST_USERNAME
import com.example.petpawsdemo.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val focusManager = LocalFocusManager.current
            val context = LocalContext.current
            val activity = this
            PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Login to Pet Paws")
                            },
                            navigationIcon = {
                                IconButton( onClick = { finish() } ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ){innerPadding ->
                    Column(
                        modifier = Modifier
                            .clickable (
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ){
                                focusManager.clearFocus()
                            }
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(9.dp),
                    ){
                        LoginScreen(activity)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(activity: ComponentActivity) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false)}
    var errorMessage by remember {mutableStateOf<String?>(null)}
    var isLoginMode by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = if (isLoginMode) "Welcome Back" else "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = if (isLoginMode) "Sign in to continue" else "Sign up to get started",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { input ->
                username = input.filter { it.isLetterOrDigit() }
            },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter username and password"
                }
                else if (username == GUEST_USERNAME) {
                    errorMessage = "Please enter a valid username"
                }
                else {
                    scope.launch {
                        if (isLoginMode) { // login
                            val exists = withContext(Dispatchers.IO) {
                                UserProfile.checkProfileExistsByUsername(context, username)
                            }
                            val correctPassword = withContext(Dispatchers.IO) {
                                UserProfile.checkPasswordByUsername(context, username, password)
                            }

                            if (!exists) {
                                errorMessage = "Nonexistent user"
                                return@launch
                            }
                            if (!correctPassword) {
                                errorMessage = "Wrong password"
                                return@launch
                            }

                            UserProfileObject.loadUserProfile(context, username)
                            UserProfileObject.saveCurrentUser(context)

                            Toast.makeText(context, "Logged in successfully", Toast.LENGTH_LONG).show()
                        } else { // sign up
                            val exists = withContext(Dispatchers.IO) {
                                UserProfile.checkProfileExistsByUsername(context, username)
                            }
                            if (exists) {
                                errorMessage = "This username was taken"
                                return@launch
                            }

                            UserProfileObject.userName = username
                            UserProfileObject.password = password
                            UserProfileObject.darkmode = false

                            UserProfileObject.saveUserProfile(context)
                            UserProfileObject.saveCurrentUser(context)

                            Toast.makeText(context, "Signed up successfully", Toast.LENGTH_LONG).show()
                        }

                        UserProfile.loggedIn = true
                        activity.finish()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = if (isLoginMode) "Sign In" else "Sign Up", fontSize = 16.sp)
        }

        TextButton(onClick = {
            isLoginMode = !isLoginMode
            errorMessage = null
        }) {
            Text(
                text = if (isLoginMode) "Don't have account? Sign Up" else "Already have account? Sign In",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}