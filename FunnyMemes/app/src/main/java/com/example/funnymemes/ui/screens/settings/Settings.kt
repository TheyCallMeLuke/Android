package com.example.funnymemes.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.funnymemes.ui.shared.SharedViewModel

@Composable
fun Settings(modifier: Modifier = Modifier, viewModel: SharedViewModel) {
    viewModel.title = "Settings"
    Box(modifier = modifier.fillMaxSize()) {
        Text("Settings Screen")
    }
}