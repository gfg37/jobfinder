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
import com.example.jobfinder.data.model.ResumeResponse
import kotlinx.coroutines.launch

@Composable
fun BrowseResumesScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var resumes by remember { mutableStateOf<List<ResumeResponse>>(emptyList()) }

    LaunchedEffect(true) {
        scope.launch {
            try {
                val token = session.getToken() ?: ""
                val response = RetrofitClient.api.getAllResumes("Bearer $token")
                if (response.isSuccessful) {
                    resumes = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Ошибка загрузки резюме", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Резюме соискателей", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(resumes) { resume ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Должность: ${resume.position}", style = MaterialTheme.typography.titleMedium)
                        Text("Опыт: ${resume.experience}")
                        Text("Образование: ${resume.education}")
                        Text("Навыки: ${resume.skills}")
                    }
                }
            }
        }
    }
}
