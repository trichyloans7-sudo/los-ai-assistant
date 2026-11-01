package com.losaiassistant.feature.verification.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.losaiassistant.domain.model.PreviewType
import com.losaiassistant.domain.model.VerificationDocument

/**
 * Central rendering boundary for future local image and PDF viewers. File access is not enabled
 * in this foundation, so this intentionally presents an informative placeholder.
 */
@Composable
fun DocumentPreview(document: VerificationDocument) {
    val label = when (document.previewType) {
        PreviewType.IMAGE -> "Image preview will render here after local capture or import is added."
        PreviewType.PDF -> "PDF preview will render here after local capture or import is added."
        PreviewType.UNSUPPORTED -> "No source file is attached to this document yet."
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .semantics { contentDescription = "Document preview: $label" },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Preview", fontWeight = FontWeight.SemiBold)
            Text(label, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
        }
    }
}
