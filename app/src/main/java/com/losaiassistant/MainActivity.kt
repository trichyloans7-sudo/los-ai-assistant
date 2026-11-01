package com.losaiassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.losaiassistant.feature.verification.VerificationViewModel
import com.losaiassistant.navigation.LosNavGraph
import com.losaiassistant.ui.theme.LosAiAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LosAiAssistantTheme {
                val verificationViewModel: VerificationViewModel = viewModel()
                val state by verificationViewModel.uiState.collectAsStateWithLifecycle()
                LosNavGraph(state = state, onEvent = verificationViewModel::onEvent)
            }
        }
    }
}
