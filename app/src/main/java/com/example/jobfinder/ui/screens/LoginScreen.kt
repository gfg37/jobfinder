package com.example.jobfinder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserSession
import com.example.jobfinder.data.model.LoginRequest
import com.example.jobfinder.data.model.UserProfileResponse
import kotlinx.coroutines.launch



@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Вход", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // LoginScreen.kt
                scope.launch {
                    try {
                        val response = RetrofitClient.api.login(LoginRequest(email, password))
                        if (response.isSuccessful && response.body() != null) {
                            val authResponse = response.body()!!
                            session.saveToken(authResponse.token)

                            // Получаем роль из токена (без лишнего запроса!)
                            val role = authResponse.getRoleFromToken()

                            when (role?.uppercase()) {
                                "APPLICANT" -> navController.navigate("seeker_main")
                                "EMPLOYER" -> navController.navigate("employer_main")
                                else -> Toast.makeText(context, "Роль не определена", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Ошибка входа", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }

        TextButton(
            onClick = {
                navController.navigate("register_screen")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Зарегистрироваться")
        }
    }
}
