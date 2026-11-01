package com.losaiassistant.domain.model

enum class ApplicantType(val title: String) {
    APPLICANT("Applicant"),
    CO_APPLICANT("Co-applicant"),
}

enum class DocumentArea(val title: String) {
    REQUIRED("Documents"),
    ADDITIONAL("Additional Documents"),
}

enum class DocumentStatus(val label: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    SENDBACK("Send back"),
}

enum class VerificationDecision(val label: String) {
    APPROVED("Approve"),
    SENDBACK("Send back"),
}

enum class PreviewType {
    IMAGE,
    PDF,
    UNSUPPORTED,
}

data class VerificationDocument(
    val id: String,
    val name: String,
    val applicantType: ApplicantType,
    val area: DocumentArea,
    val status: DocumentStatus = DocumentStatus.PENDING,
    val remarks: String = "",
    val previewType: PreviewType = PreviewType.UNSUPPORTED,
)
