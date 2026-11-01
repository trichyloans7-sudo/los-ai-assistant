package com.losaiassistant.feature.verification

import com.losaiassistant.domain.model.VerificationDecision

sealed interface VerificationEvent {
    data class OpenDocument(val documentId: String) : VerificationEvent
    data object DismissDocument : VerificationEvent
    data class SaveDecision(val decision: VerificationDecision, val remarks: String) : VerificationEvent
    data object ClearMessage : VerificationEvent
}
