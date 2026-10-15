package com.mustafakoceerr.languagemanager.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint

// AppCompatActivity is required for per-app languages on API < 33 (AppCompat backport).
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LanguageScreen()
            }
        }
    }
}
