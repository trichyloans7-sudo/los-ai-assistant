package com.losaiassistant.feature.verification.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.losaiassistant.domain.model.ApplicantType
import com.losaiassistant.domain.model.DocumentArea
import com.losaiassistant.domain.model.DocumentStatus
import com.losaiassistant.domain.model.VerificationDecision
import com.losaiassistant.domain.model.VerificationDocument
import com.losaiassistant.feature.verification.VerificationEvent
import com.losaiassistant.feature.verification.VerificationUiState

@Composable
fun DashboardScreen(
    state: VerificationUiState,
    onApplicantClick: () -> Unit,
    onCoApplicantClick: () -> Unit,
    onOpenDocument: (String) -> Unit,
    onEvent: (VerificationEvent) -> Unit,
) {
    Scaffold(topBar = { DashboardHeader(state.leadNumber) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Spacer(Modifier.height(2.dp)) }
            item { OverviewCard(state) }
            item { Text("Applicants", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            item {
                PersonCard("Applicant", state.applicantName, "Primary borrower", onApplicantClick)
            }
            item {
                PersonCard("Co-applicant", state.coApplicantName, "Joint borrower", onCoApplicantClick)
            }
            item { Text("Recent verification activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(state.documents.take(3), key = { it.id }) { document ->
                DocumentRow(document, onOpenDocument)
            }
            item {
                Text(
                    "This is an offline workflow foundation. It does not upload documents or perform AI verification.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 20.dp),
                )
            }
        }
    }
    DecisionDialogForSelectedDocument(state, onEvent)
    StatusMessage(state.message, onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardHeader(leadNumber: String) {
    SmallTopAppBar(
        title = {
            Column {
                Text("LOS Assistant", fontWeight = FontWeight.Bold)
                Text(leadNumber, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
    )
}

@Composable
private fun OverviewCard(state: VerificationUiState) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Verification workspace", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Review documents and record a clear human decision for this loan application.")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("${state.documents.size} documents") })
                AssistChip(onClick = {}, label = { Text("${state.completedDocuments} reviewed") })
            }
        }
    }
}

@Composable
private fun PersonCard(title: String, name: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics { contentDescription = "$title: $name" },
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    applicantType: ApplicantType,
    name: String,
    onNavigateDocuments: (DocumentArea) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(applicantType.title) }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Choose a document group to begin a human-controlled review.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            NavigationCard("Documents", "Required identity and application records", "Open documents") { onNavigateDocuments(DocumentArea.REQUIRED) }
            NavigationCard("Additional Documents", "Income, address, and supporting records", "Open additional documents") { onNavigateDocuments(DocumentArea.ADDITIONAL) }
        }
    }
}

@Composable
private fun NavigationCard(title: String, subtitle: String, description: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).semantics { contentDescription = description }) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    applicantType: ApplicantType,
    area: DocumentArea,
    state: VerificationUiState,
    onEvent: (VerificationEvent) -> Unit,
    onBack: () -> Unit,
) {
    val documents = state.documentsFor(applicantType, area)
    Scaffold(topBar = { SmallTopAppBar(title = { Text("${applicantType.title} - ${area.title}") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    "Open a document to view verification details, preview readiness, and reviewer decisions.",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(documents, key = { it.id }) { document -> DocumentRow(document) { onEvent(VerificationEvent.OpenDocument(it)) } }
        }
    }
    DecisionDialogForSelectedDocument(state, onEvent)
    StatusMessage(state.message, onEvent)
}

@Composable
private fun DocumentRow(document: VerificationDocument, onOpen: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onOpen(document.id) }.semantics { contentDescription = "${document.name}, ${document.status.label}" }) {
        Row(modifier = Modifier.padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(document.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                if (document.remarks.isNotBlank()) Text(document.remarks, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.width(12.dp))
            StatusPill(document.status)
        }
    }
}

@Composable
private fun StatusPill(status: DocumentStatus) {
    val color = when (status) {
        DocumentStatus.PENDING -> MaterialTheme.colorScheme.secondaryContainer
        DocumentStatus.APPROVED -> MaterialTheme.colorScheme.primaryContainer
        DocumentStatus.SENDBACK -> MaterialTheme.colorScheme.errorContainer
    }
    Surface(color = color, shape = MaterialTheme.shapes.small) {
        Text(status.label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun DecisionDialogForSelectedDocument(state: VerificationUiState, onEvent: (VerificationEvent) -> Unit) {
    val document = state.documents.firstOrNull { it.id == state.selectedDocumentId } ?: return
    var remarks by rememberSaveable(document.id) { mutableStateOf(document.remarks) }
    AlertDialog(
        onDismissRequest = { onEvent(VerificationEvent.DismissDocument) },
        title = { Text("Verify ${document.name}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Current status: ${document.status.label}", style = MaterialTheme.typography.bodyMedium)
                DocumentPreview(document)
                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Reviewer remarks") },
                    placeholder = { Text("Required when sending back") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Row {
                OutlinedButton(onClick = { onEvent(VerificationEvent.SaveDecision(VerificationDecision.SENDBACK, remarks)) }) { Text("Send back") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onEvent(VerificationEvent.SaveDecision(VerificationDecision.APPROVED, remarks)) }) { Text("Approve") }
            }
        },
        dismissButton = { TextButton(onClick = { onEvent(VerificationEvent.DismissDocument) }) { Text("Cancel") } },
    )
}

@Composable
private fun StatusMessage(message: String?, onEvent: (VerificationEvent) -> Unit) {
    if (message == null) return
    AlertDialog(
        onDismissRequest = { onEvent(VerificationEvent.ClearMessage) },
        title = { Text("Verification update") },
        text = { Text(message) },
        confirmButton = { TextButton(onClick = { onEvent(VerificationEvent.ClearMessage) }) { Text("OK") } },
    )
}
