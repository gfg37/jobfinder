package com.example.jobfinder.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.jobfinder.ui.screens.BrowseResumesScreen
import com.example.jobfinder.ui.screens.BrowseVacanciesScreen
import com.example.jobfinder.ui.screens.CreateResumeScreen
import com.example.jobfinder.ui.screens.CreateVacancyScreen
import com.example.jobfinder.ui.screens.EmployerMainScreen
import com.example.jobfinder.ui.screens.LoginScreen
import com.example.jobfinder.ui.screens.RegisterScreen
import com.example.jobfinder.ui.screens.SearchScreen
import com.example.jobfinder.ui.screens.SeekerMainScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    toggleTheme: () -> Unit
) {
    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("register_screen") {
            RegisterScreen(navController)
        }
        composable("seeker_main") {
            SeekerMainScreen(navController, toggleTheme)
        }
        composable("employer_main") {
            EmployerMainScreen(navController, toggleTheme)
        }
        composable("search_screen") {
            SearchScreen()
        }

        composable("create_resume") { CreateResumeScreen(navController) }

        composable("browse_resumes") {
            BrowseResumesScreen(navController)
        }


        composable("create_vacancy") {
            CreateVacancyScreen(navController)
        }

        composable("browse_vacancies") {
            BrowseVacanciesScreen(navController)
        }





    }
}
