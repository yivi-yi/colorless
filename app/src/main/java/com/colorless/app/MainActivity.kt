package com.colorless.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.colorless.app.ui.chat.ChatScreen
import com.colorless.app.ui.theme.ColorlessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorlessTheme {
                ChatScreen()
            }
        }
    }
}
