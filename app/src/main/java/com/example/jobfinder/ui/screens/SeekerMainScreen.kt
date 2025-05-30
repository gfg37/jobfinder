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
fun SeekerMainScreen(navController: NavController, toggleTheme: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Меню соискателя") },
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
            Button(onClick = { navController.navigate("search_screen") }) {
                Text("Поиск вакансий")
            }
            Button(
                onClick = { navController.navigate("create_resume") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Создать резюме")
            }

            Button(onClick = { navController.navigate("my_responses") }) {
                Text("Мои отклики")
            }
            Button(onClick = { navController.navigate("favorites") }) {
                Text("Избранное")
            }

            Button(
                onClick = { navController.navigate("browse_vacancies")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Посмотреть вакансии")
            }


        }
    }
}
