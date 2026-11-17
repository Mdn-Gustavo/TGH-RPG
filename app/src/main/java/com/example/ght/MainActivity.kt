package com.example.ght

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ght.navigation.AppNavigation
import com.example.ght.ui.theme.GHTTheme

class MainActivity : ComponentActivity() {

    private val audioManager by lazy {
        (application as CasteloApp).container.audioManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GHTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        audioManager.iniciarMusica()
    }

    override fun onStop() {
        super.onStop()
        audioManager.pausarMusica()
    }
}
