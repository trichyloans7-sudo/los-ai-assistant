package com.losaiassistant.feature.verification

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.losaiassistant.feature.verification.ui.DashboardScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class VerificationScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun dashboardShowsApplicantNavigation() {
        composeRule.setContent {
            MaterialTheme {
                DashboardScreen(
                    state = VerificationUiState(),
                    onApplicantClick = {},
                    onCoApplicantClick = {},
                    onOpenDocument = {},
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithText("LOS Assistant").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Applicant: Ananya Sharma").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Co-applicant: Ravi Sharma").assertIsDisplayed()
    }

    @Test
    fun openingDocumentRequestsDocumentDetails() {
        var openedDocumentId: String? = null
        composeRule.setContent {
            MaterialTheme {
                DashboardScreen(
                    state = VerificationUiState(),
                    onApplicantClick = {},
                    onCoApplicantClick = {},
                    onOpenDocument = { openedDocumentId = it },
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Aadhaar Card, Pending").performClick()

        composeRule.runOnIdle {
            assertEquals("app-aadhaar", openedDocumentId)
        }
    }
}
