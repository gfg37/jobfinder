package com.example.jobfinder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.model.ResumeRequest
import com.example.jobfinder.data.UserSession
import kotlinx.coroutines.launch

@Composable
fun CreateResumeScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var position by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Создание резюме", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Желаемая должность") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Опыт работы") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = education,
            onValueChange = { education = it },
            label = { Text("Образование") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = skills,
            onValueChange = { skills = it },
            label = { Text("Навыки") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val token = session.getToken() ?: ""
                        val response = RetrofitClient.api.createResume(
                            "Bearer $token",
                            ResumeRequest(position, experience, skills, education)
                        )
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Резюме создано", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Ошибка при создании", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать")
        }
    }
}
