package com.example.jobfinder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserSession
import com.example.jobfinder.data.model.VacancyResponse
import kotlinx.coroutines.launch

@Composable
fun BrowseVacanciesScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var vacancies by remember { mutableStateOf<List<VacancyResponse>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val token = session.getToken() ?: ""
                val response = RetrofitClient.api.getAllVacancies("Bearer $token")
                if (response.isSuccessful) {
                    vacancies = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Ошибка загрузки вакансий", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Доступные вакансии", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(vacancies) { vacancy ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Должность: ${vacancy.title}", style = MaterialTheme.typography.titleMedium)
                        Text("Описание: ${vacancy.description}")
                        Text("Требования: ${vacancy.requirements}")
                        Text("Зарплата: ${vacancy.salary}")
                        Text("Работодатель: ${vacancy.employerUsername}")
                    }
                }
            }
        }
    }
}


