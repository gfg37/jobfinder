package com.example.jobfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserSession
import com.example.jobfinder.data.model.ResumeResponse
import com.example.jobfinder.data.model.VacancyResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf(0) }
    var favoriteVacancies by remember { mutableStateOf<List<VacancyResponse>>(emptyList()) }
    var favoriteResumes by remember { mutableStateOf<List<ResumeResponse>>(emptyList()) }

    LaunchedEffect(selectedTab) {
        scope.launch {
            try {
                val token = session.getToken() ?: ""
                if (selectedTab == 0) {
                    val response = RetrofitClient.api.getFavoriteVacancies("Bearer $token")
                    if (response.isSuccessful) {
                        favoriteVacancies = response.body() ?: emptyList()
                    }
                } else {
                    val response = RetrofitClient.api.getFavoriteResumes("Bearer $token")
                    if (response.isSuccessful) {
                        favoriteResumes = response.body() ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                // Ошибка будет обработана в UI
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное") },
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
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Вакансии") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Резюме") }
                )
            }

            when (selectedTab) {
                0 -> FavoriteVacanciesList(
                    vacancies = favoriteVacancies,
                    onRemove = { id ->
                        scope.launch {
                            try {
                                val token = session.getToken() ?: ""
                                RetrofitClient.api.removeVacancyFromFavorites("Bearer $token", id)
                                favoriteVacancies = favoriteVacancies.filter { it.id != id }
                            } catch (e: Exception) {
                                // Обработка ошибки
                            }
                        }
                    }
                )
                1 -> FavoriteResumesList(
                    resumes = favoriteResumes,
                    onRemove = { id ->
                        scope.launch {
                            try {
                                val token = session.getToken() ?: ""
                                RetrofitClient.api.removeResumeFromFavorites("Bearer $token", id)
                                favoriteResumes = favoriteResumes.filter { it.id != id }
                            } catch (e: Exception) {
                                // Обработка ошибки
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FavoriteVacanciesList(
    vacancies: List<VacancyResponse>,
    onRemove: (Long) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(vacancies) { vacancy ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(vacancy.title, style = MaterialTheme.typography.titleMedium)
                    Text(vacancy.description)
                    Text("Зарплата: ${vacancy.salary}")
                    IconButton(
                        onClick = { onRemove(vacancy.id) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Удалить из избранного")
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteResumesList(
    resumes: List<ResumeResponse>,
    onRemove: (Long) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(resumes) { resume ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(resume.position, style = MaterialTheme.typography.titleMedium)
                    Text("Опыт: ${resume.experience}")
                    Text("Навыки: ${resume.skills}")
                    IconButton(
                        onClick = { onRemove(resume.id) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Удалить из избранного")
                    }
                }
            }
        }
    }
}