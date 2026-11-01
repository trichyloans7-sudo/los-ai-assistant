package com.losaiassistant.feature.verification

import com.losaiassistant.domain.model.DocumentStatus
import com.losaiassistant.domain.model.VerificationDecision
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VerificationViewModelTest {
    @Test
    fun `opening a document selects it`() {
        val viewModel = VerificationViewModel()

        viewModel.onEvent(VerificationEvent.OpenDocument("app-aadhaar"))

        assertEquals("app-aadhaar", viewModel.uiState.value.selectedDocumentId)
    }

    @Test
    fun `approving a selected document saves decision and optional remarks`() {
        val viewModel = VerificationViewModel()
        viewModel.onEvent(VerificationEvent.OpenDocument("app-aadhaar"))

        viewModel.onEvent(VerificationEvent.SaveDecision(VerificationDecision.APPROVED, "Name matches application"))

        val document = viewModel.uiState.value.documents.first { it.id == "app-aadhaar" }
        assertEquals(DocumentStatus.APPROVED, document.status)
        assertEquals("Name matches application", document.remarks)
        assertNull(viewModel.uiState.value.selectedDocumentId)
        assertTrue(viewModel.uiState.value.message!!.contains("Approve"))
    }

    @Test
    fun `send back requires nonblank remarks`() {
        val viewModel = VerificationViewModel()
        viewModel.onEvent(VerificationEvent.OpenDocument("app-pan"))

        viewModel.onEvent(VerificationEvent.SaveDecision(VerificationDecision.SENDBACK, "  "))

        val document = viewModel.uiState.value.documents.first { it.id == "app-pan" }
        assertEquals(DocumentStatus.PENDING, document.status)
        assertEquals("app-pan", viewModel.uiState.value.selectedDocumentId)
        assertEquals("Add remarks before sending a document back.", viewModel.uiState.value.message)
    }

    @Test
    fun `send back saves decision with remarks`() {
        val viewModel = VerificationViewModel()
        viewModel.onEvent(VerificationEvent.OpenDocument("co-pan"))

        viewModel.onEvent(VerificationEvent.SaveDecision(VerificationDecision.SENDBACK, "Image is unreadable"))

        val document = viewModel.uiState.value.documents.first { it.id == "co-pan" }
        assertEquals(DocumentStatus.SENDBACK, document.status)
        assertEquals("Image is unreadable", document.remarks)
        assertFalse(viewModel.uiState.value.message.isNullOrBlank())
    }
}
