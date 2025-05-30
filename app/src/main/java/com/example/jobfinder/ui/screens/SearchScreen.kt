package com.example.jobfinder.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserSession
import com.example.jobfinder.data.model.ResumeResponse
import com.example.jobfinder.data.model.VacancyResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import android.util.Base64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    toggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var searchHistory by remember { mutableStateOf(emptyList<String>()) }
    var searchResults by remember { mutableStateOf<List<Any>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var userRole by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val token = session.getToken() ?: return@LaunchedEffect
        val parts = token.split(".")
        if (parts.size > 1) {
            val payload = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING),
                Charsets.UTF_8
            )
            userRole = JSONObject(payload).getString("role")
        }
    }

    LaunchedEffect(searchQuery.text) {
        if (searchQuery.text.isNotEmpty()) {
            delay(1000)
            performSearch(
                query = searchQuery.text,
                userRole = userRole,
                session = session,
                scope = scope,
                context = context
            ) { results, error ->
                if (error) {
                    isError = true
                } else {
                    searchResults = results
                    if (results.isNotEmpty()) {
                        searchHistory = (listOf(searchQuery.text) +
                                searchHistory.filterNot { it == searchQuery.text }
                                    .take(10))
                    }
                }
                isLoading = false
            }
        } else {
            searchResults = emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Поиск") },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    isLoading = it.text.isNotEmpty()
                    isError = false
                },
                label = { Text("Введите поисковый запрос") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
                trailingIcon = {
                    if (searchQuery.text.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = TextFieldValue("")
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Очистить")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (isError) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ошибка при выполнении поиска")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        isLoading = true
                        isError = false
                        performSearch(
                            searchQuery.text,
                            userRole,
                            session,
                            scope,
                            context
                        ) { results, error ->
                            if (error) isError = true else searchResults = results
                            isLoading = false
                        }
                    }) {
                        Text("Повторить")
                    }
                }
            }

            if (searchQuery.text.isEmpty() && searchHistory.isNotEmpty()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(Icons.Default.History, contentDescription = "История поиска",
                            modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("История поиска", style = MaterialTheme.typography.titleMedium)
                    }

                    LazyColumn {
                        items(searchHistory) { historyItem ->
                            Text(
                                text = historyItem,
                                modifier = Modifier
                                    .clickable { searchQuery = TextFieldValue(historyItem) }
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    Button(
                        onClick = { searchHistory = emptyList() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Очистить историю")
                    }
                }
            }

            if (searchQuery.text.isNotEmpty() && searchResults.isEmpty() && !isLoading && !isError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ничего не найдено")
                }
            }

            if (searchResults.isNotEmpty()) {
                LazyColumn {
                    when (userRole) {
                        "EMPLOYER" -> {
                            items(searchResults.filterIsInstance<ResumeResponse>()) { resume ->
                                ResumeItem(resume, navController, session)
                            }
                        }
                        "APPLICANT" -> {
                            items(searchResults.filterIsInstance<VacancyResponse>()) { vacancy ->
                                VacancyItem(vacancy, navController, session)
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

private fun performSearch(
    query: String,
    userRole: String?,
    session: UserSession,
    scope: CoroutineScope,
    context: Context,
    onComplete: (List<Any>, Boolean) -> Unit
) {
    if (query.isEmpty()) {
        onComplete(emptyList(), false)
        return
    }

    scope.launch {
        try {
            val token = session.getToken() ?: ""
            when (userRole) {
                "EMPLOYER" -> {
                    val response = RetrofitClient.api.getAllResumes("Bearer $token")
                    if (response.isSuccessful) {
                        val allResumes = response.body() ?: emptyList()
                        val filtered = allResumes.filter {
                            it.position.contains(query, ignoreCase = true) ||
                                    it.skills.contains(query, ignoreCase = true) ||
                                    it.experience.contains(query, ignoreCase = true) ||
                                    it.education.contains(query, ignoreCase = true)
                        }
                        onComplete(filtered, false)
                    } else {
                        onComplete(emptyList(), true)
                    }
                }
                "APPLICANT" -> {
                    val response = RetrofitClient.api.getAllVacancies("Bearer $token")
                    if (response.isSuccessful) {
                        val allVacancies = response.body() ?: emptyList()
                        val filtered = allVacancies.filter {
                            it.title.contains(query, ignoreCase = true) ||
                                    it.description.contains(query, ignoreCase = true) ||
                                    it.requirements.contains(query, ignoreCase = true) ||
                                    it.salary.contains(query, ignoreCase = true)
                        }
                        onComplete(filtered, false)
                    } else {
                        onComplete(emptyList(), true)
                    }
                }
                else -> onComplete(emptyList(), true)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
            onComplete(emptyList(), true)
        }
    }
}

@Composable
private fun ResumeItem(
    resume: ResumeResponse,
    navController: NavController,
    session: UserSession
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Должность: ${resume.position}", style = MaterialTheme.typography.titleMedium)
            Text("Опыт: ${resume.experience}")
            Text("Образование: ${resume.education}")
            Text("Навыки: ${resume.skills}")

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val token = session.getToken() ?: ""
                            val response = RetrofitClient.api.addResumeToFavorites("Bearer $token", resume.id)
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В избранное")
            }
        }
    }
}

@Composable
private fun VacancyItem(
    vacancy: VacancyResponse,
    navController: NavController,
    session: UserSession
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Должность: ${vacancy.title}", style = MaterialTheme.typography.titleMedium)
            Text("Описание: ${vacancy.description}")
            Text("Требования: ${vacancy.requirements}")
            Text("Зарплата: ${vacancy.salary}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val token = session.getToken() ?: ""
                                val response = RetrofitClient.api.addVacancyToFavorites("Bearer $token", vacancy.id)
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text("В избранное")
                }

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val token = session.getToken() ?: ""
                                val resumesResponse = RetrofitClient.api.getMyResumes("Bearer $token")
                                if (resumesResponse.isSuccessful && resumesResponse.body()?.isNotEmpty() == true) {
                                    val resumeId = resumesResponse.body()!![0].id
                                    val response = RetrofitClient.api.applyForVacancy(
                                        "Bearer $token",
                                        com.example.jobfinder.data.model.ApplicationRequest(resumeId, vacancy.id)
                                    )
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Отклик отправлен", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Сначала создайте резюме", Toast.LENGTH_SHORT).show()
                                    navController.navigate("create_resume")
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text("Откликнуться")
                }
            }
        }
    }
}