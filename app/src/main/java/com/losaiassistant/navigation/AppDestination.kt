package com.losaiassistant.navigation

import com.losaiassistant.domain.model.ApplicantType
import com.losaiassistant.domain.model.DocumentArea

sealed class AppDestination(val route: String) {
    data object Dashboard : AppDestination("dashboard")
    data object Applicant : AppDestination("applicant")
    data object CoApplicant : AppDestination("co-applicant")
    data object ApplicantDocuments : AppDestination("applicant/documents")
    data object ApplicantAdditionalDocuments : AppDestination("applicant/additional-documents")
    data object CoApplicantDocuments : AppDestination("co-applicant/documents")
    data object CoApplicantAdditionalDocuments : AppDestination("co-applicant/additional-documents")
}

fun documentDestination(applicantType: ApplicantType, area: DocumentArea): String = when (applicantType) {
    ApplicantType.APPLICANT -> if (area == DocumentArea.REQUIRED) {
        AppDestination.ApplicantDocuments.route
    } else {
        AppDestination.ApplicantAdditionalDocuments.route
    }
    ApplicantType.CO_APPLICANT -> if (area == DocumentArea.REQUIRED) {
        AppDestination.CoApplicantDocuments.route
    } else {
        AppDestination.CoApplicantAdditionalDocuments.route
    }
}
