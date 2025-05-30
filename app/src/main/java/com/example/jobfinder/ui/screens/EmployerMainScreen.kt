package com.example.jobfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerMainScreen(navController: NavController, toggleTheme: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Меню работодателя") },
                actions = {
                    IconButton(onClick = toggleTheme) {
                        Icon(Icons.Default.Brightness6, contentDescription = "Сменить тему")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("create_vacancy") }) {
                Text("Создать вакансию")
            }
            Button(onClick = { navController.navigate("search_screen") }) {
                Text("Поиск резюме")
            }
            Button(onClick = { navController.navigate("vacancy_responses") }) {
                Text("Отклики на вакансии")
            }
            Button(onClick = { navController.navigate("favorites") }) {
                Text("Избранное")
            }

            Button(
                onClick = { navController.navigate("browse_resumes") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Посмотреть резюме")
            }



        }
    }
}
