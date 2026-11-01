package com.losaiassistant.feature.verification

import androidx.lifecycle.ViewModel
import com.losaiassistant.domain.model.DocumentStatus
import com.losaiassistant.domain.model.VerificationDecision
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VerificationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState.asStateFlow()

    fun onEvent(event: VerificationEvent) {
        when (event) {
            is VerificationEvent.OpenDocument -> _uiState.update { it.copy(selectedDocumentId = event.documentId) }
            VerificationEvent.DismissDocument -> _uiState.update { it.copy(selectedDocumentId = null) }
            VerificationEvent.ClearMessage -> _uiState.update { it.copy(message = null) }
            is VerificationEvent.SaveDecision -> saveDecision(event.decision, event.remarks)
        }
    }

    private fun saveDecision(decision: VerificationDecision, remarks: String) {
        val documentId = _uiState.value.selectedDocumentId ?: return
        if (decision == VerificationDecision.SENDBACK && remarks.isBlank()) {
            _uiState.update { it.copy(message = "Add remarks before sending a document back.") }
            return
        }

        val status = if (decision == VerificationDecision.APPROVED) {
            DocumentStatus.APPROVED
        } else {
            DocumentStatus.SENDBACK
        }
        _uiState.update { state ->
            state.copy(
                documents = state.documents.map { document ->
                    if (document.id == documentId) document.copy(status = status, remarks = remarks.trim()) else document
                },
                selectedDocumentId = null,
                message = "${decision.label} decision saved locally.",
            )
        }
    }
}
