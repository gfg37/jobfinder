package com.example.jobfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EmployerMainScreen(
    navController: NavController,
    toggleTheme: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text("Главная — Работодатель", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("search_screen")
        }) {
            Text("Поиск резюме")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("create_vacancy")

        }) {
            Text("Создать вакансию")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = toggleTheme) {
            Text("Переключить тему")
        }
    }
}
