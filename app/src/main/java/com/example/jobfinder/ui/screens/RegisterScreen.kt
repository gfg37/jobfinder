package com.example.jobfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserPreferences
import com.example.jobfinder.data.model.RegisterRequest
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var selectedRole by rememberSaveable { mutableStateOf("Соискатель") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Маппинг русских названий на английские для сервера
    val roleMapping = mapOf(
        "Соискатель" to "APPLICANT",
        "Работодатель" to "EMPLOYER"
    )

    val roles = listOf("Соискатель", "Работодатель")
    val apiService = RetrofitClient.api
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Выберите роль:")
        roles.forEach { role ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (role == selectedRole),
                        onClick = { selectedRole = role }
                    )
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (role == selectedRole),
                    onClick = { selectedRole = role }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(role)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (fullName.isBlank() || login.isBlank() || password.isBlank()) {
                    errorMessage = "Заполните все поля"
                    return@Button
                }

                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        // Преобразуем русскую роль в английскую для сервера
                        val serverRole = roleMapping[selectedRole] ?: "APPLICANT"

                        val response = apiService.register(
                            RegisterRequest(
                                name = fullName,
                                email = login,
                                password = password,
                                role = serverRole
                            )
                        )

                        if (response.isSuccessful) {
                            userPreferences.saveUserRole(selectedRole)
                            navController.navigate("login_screen") {
                                popUpTo("register_screen") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Ошибка регистрации: ${response.message()}"
                        }
                    } catch (e: Exception) {
                        errorMessage = "Ошибка соединения: ${e.localizedMessage}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Создать аккаунт")
            }
        }
    }
}