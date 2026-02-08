package com.example.petpawsdemo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.model.GUEST_USERNAME
import com.example.petpawsdemo.model.UserProfile
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {
    @SuppressLint("UnsafeIntentLaunch")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode) {
                val context = LocalContext.current
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "${UserProfileObject.userName}'s Profile") },
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
                            ),
                        )
                    },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        if (UserProfile.loggedIn) {
                            ProfileScreen()
                        }
                        else {
                            intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen() {
    val accountHandle = UserProfileObject.userName
    val recentPurchases = UserProfileObject.getPurchasedItems()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isPressed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //account info
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 12.dp)
                .clip(CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                        }
                    )
                },
        ) {
            UserProfileObject.userPfpReference?.let { pfpId ->
                Image(
                    painter = painterResource(id = pfpId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isPressed) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.25f))
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = accountHandle,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        //recent item list
        Text(
            text = "Recent Purchases",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(recentPurchases) { product ->
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        //dark mode switch thingy
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(text = "Dark Mode", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.width(8.dp))

            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            Switch(
                checked = UserProfileObject.darkmode,
                onCheckedChange = {
                    UserProfileObject.darkmode = it
                    scope.launch {
                        UserProfileObject.saveUserProfile(context)
                    }
                }
            )
        }

        Button(
            onClick = {
                UserProfile.loggedIn = false
                scope.launch {
                    UserProfileObject.saveUserProfile(context)

                    UserProfileObject.userName = GUEST_USERNAME
                    UserProfileObject.saveCurrentUser(context)
                }

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Logout")
        }
    }
}