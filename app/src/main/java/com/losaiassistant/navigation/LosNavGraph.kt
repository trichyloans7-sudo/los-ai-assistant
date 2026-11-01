package com.losaiassistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.losaiassistant.domain.model.ApplicantType
import com.losaiassistant.domain.model.DocumentArea
import com.losaiassistant.feature.verification.VerificationEvent
import com.losaiassistant.feature.verification.VerificationUiState
import com.losaiassistant.feature.verification.ui.DashboardScreen
import com.losaiassistant.feature.verification.ui.DocumentsScreen
import com.losaiassistant.feature.verification.ui.PersonScreen

@Composable
fun LosNavGraph(state: VerificationUiState, onEvent: (VerificationEvent) -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppDestination.Dashboard.route) {
        composable(AppDestination.Dashboard.route) {
            DashboardScreen(
                state = state,
                onApplicantClick = { navController.navigate(AppDestination.Applicant.route) },
                onCoApplicantClick = { navController.navigate(AppDestination.CoApplicant.route) },
                onOpenDocument = { onEvent(VerificationEvent.OpenDocument(it)) },
                onEvent = onEvent,
            )
        }
        composable(AppDestination.Applicant.route) {
            PersonScreen(
                applicantType = ApplicantType.APPLICANT,
                name = state.applicantName,
                onNavigateDocuments = { area -> navController.navigate(documentDestination(ApplicantType.APPLICANT, area)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(AppDestination.CoApplicant.route) {
            PersonScreen(
                applicantType = ApplicantType.CO_APPLICANT,
                name = state.coApplicantName,
                onNavigateDocuments = { area -> navController.navigate(documentDestination(ApplicantType.CO_APPLICANT, area)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(AppDestination.ApplicantDocuments.route) {
            DocumentsScreen(ApplicantType.APPLICANT, DocumentArea.REQUIRED, state, onEvent) { navController.popBackStack() }
        }
        composable(AppDestination.ApplicantAdditionalDocuments.route) {
            DocumentsScreen(ApplicantType.APPLICANT, DocumentArea.ADDITIONAL, state, onEvent) { navController.popBackStack() }
        }
        composable(AppDestination.CoApplicantDocuments.route) {
            DocumentsScreen(ApplicantType.CO_APPLICANT, DocumentArea.REQUIRED, state, onEvent) { navController.popBackStack() }
        }
        composable(AppDestination.CoApplicantAdditionalDocuments.route) {
            DocumentsScreen(ApplicantType.CO_APPLICANT, DocumentArea.ADDITIONAL, state, onEvent) { navController.popBackStack() }
        }
    }
}
