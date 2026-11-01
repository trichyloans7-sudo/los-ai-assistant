package com.losaiassistant.feature.verification

import com.losaiassistant.domain.model.ApplicantType
import com.losaiassistant.domain.model.DocumentArea
import com.losaiassistant.domain.model.DocumentStatus
import com.losaiassistant.domain.model.VerificationDocument

data class VerificationUiState(
    val leadNumber: String = "LOS-24018",
    val applicantName: String = "Ananya Sharma",
    val coApplicantName: String = "Ravi Sharma",
    val documents: List<VerificationDocument> = initialDocuments(),
    val selectedDocumentId: String? = null,
    val message: String? = null,
) {
    fun documentsFor(applicantType: ApplicantType, area: DocumentArea) =
        documents.filter { it.applicantType == applicantType && it.area == area }

    val completedDocuments: Int
        get() = documents.count { it.status != DocumentStatus.PENDING }
}

private fun initialDocuments() = listOf(
    VerificationDocument("app-aadhaar", "Aadhaar Card", ApplicantType.APPLICANT, DocumentArea.REQUIRED),
    VerificationDocument("app-pan", "PAN Card", ApplicantType.APPLICANT, DocumentArea.REQUIRED),
    VerificationDocument("app-form", "Application Form - Online", ApplicantType.APPLICANT, DocumentArea.REQUIRED),
    VerificationDocument("app-income", "Salary Slips", ApplicantType.APPLICANT, DocumentArea.ADDITIONAL),
    VerificationDocument("co-aadhaar", "Aadhaar Card", ApplicantType.CO_APPLICANT, DocumentArea.REQUIRED),
    VerificationDocument("co-pan", "PAN Card", ApplicantType.CO_APPLICANT, DocumentArea.REQUIRED),
    VerificationDocument("co-bank", "Bank Statement", ApplicantType.CO_APPLICANT, DocumentArea.ADDITIONAL),
)
