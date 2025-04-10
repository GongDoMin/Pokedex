package co.kr.mvisample.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.kr.mvisample.ui.detail.DetailActivity
import co.kr.mvisample.ui.main.compose.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(
                onNavigateToNextPage = {
                    startActivity(Intent(this@MainActivity, DetailActivity::class.java))
                }
            )
        }
    }
}