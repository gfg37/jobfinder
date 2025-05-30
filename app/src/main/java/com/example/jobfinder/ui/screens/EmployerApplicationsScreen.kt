package com.example.jobfinder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserSession
import com.example.jobfinder.data.model.ApplicationResponse
import com.example.jobfinder.data.model.UpdateApplicationStatusRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerApplicationsScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var applications by remember { mutableStateOf<List<ApplicationResponse>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Отклики на вакансии") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LaunchedEffect(Unit) {
                scope.launch {
                    try {
                        val token = session.getToken() ?: ""
                        val response = RetrofitClient.api.getEmployerApplications("Bearer $token")
                        if (response.isSuccessful) {
                            applications = response.body() ?: emptyList()
                        } else {
                            Toast.makeText(context, "Ошибка загрузки откликов", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            LazyColumn {
                items(applications) { application ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Резюме: ${application.resumeOwner}", style = MaterialTheme.typography.titleMedium)
                            Text("Вакансия: ${application.vacancyTitle}")
                            Text("Статус: ${when(application.status) {
                                "PENDING" -> "На рассмотрении"
                                "ACCEPTED" -> "Принято"
                                "REJECTED" -> "Отклонено"
                                else -> application.status
                            }}")

                            if (application.status == "PENDING") {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val token = session.getToken() ?: ""
                                                    val response = RetrofitClient.api.updateApplicationStatus(
                                                        "Bearer $token",
                                                        application.id,
                                                        UpdateApplicationStatusRequest("ACCEPTED")
                                                    )
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(context, "Отклик принят", Toast.LENGTH_SHORT).show()
                                                        navController.popBackStack()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    ) {
                                        Text("Принять")
                                    }

                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val token = session.getToken() ?: ""
                                                    val response = RetrofitClient.api.updateApplicationStatus(
                                                        "Bearer $token",
                                                        application.id,
                                                        UpdateApplicationStatusRequest("REJECTED")
                                                    )
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(context, "Отклик отклонён", Toast.LENGTH_SHORT).show()
                                                        navController.popBackStack()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    ) {
                                        Text("Отклонить")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}