package com.example.jobfinder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen() {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var results by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf<List<String>>(emptyList()) }

    // Автозапуск поиска через 2 сек
    LaunchedEffect(query.text) {
        if (query.text.isNotEmpty()) {
            delay(2000)
            isLoading = true
            isError = false
            try {
                // Здесь симуляция "поиска"
                delay(1000)
                results = listOf("Результат 1", "Результат 2").filter {
                    it.contains(query.text, ignoreCase = true)
                }
                if (results.isNotEmpty()) {
                    history = (listOf(query.text) + history.filterNot { it == query.text })
                        .take(10)
                }
            } catch (e: Exception) {
                isError = true
            }
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Введите запрос") },
            trailingIcon = {
                if (query.text.isNotEmpty()) {
                    IconButton(onClick = {
                        query = TextFieldValue("")
                        results = emptyList()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (isError) {
            Text("Ошибка при поиске")
            Button(onClick = {
                Toast.makeText(context, "Повторный запрос", Toast.LENGTH_SHORT).show()
                // Повторить запрос
                scope.launch {
                    isLoading = true
                    delay(1000)
                    results = listOf("Результат 1", "Результат 2")
                    isLoading = false
                    isError = false
                }
            }) {
                Text("Обновить")
            }
        } else if (results.isEmpty() && query.text.isNotEmpty()) {
            Text("Ничего не найдено")
        } else {
            LazyColumn {
                items(results) { item ->
                    Text(text = item, modifier = Modifier.padding(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (query.text.isEmpty() && history.isNotEmpty()) {
            Text("История поиска:", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(history) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .clickable {
                                query = TextFieldValue(item)
                            }
                            .padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { history = emptyList() }) {
                Text("Очистить историю")
            }
        }
    }
}
